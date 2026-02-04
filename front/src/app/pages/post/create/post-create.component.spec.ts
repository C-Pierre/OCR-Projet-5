import { of, throwError } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { PostCreateComponent } from './post-create.component';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { Subject } from 'src/app/core/models/subject/subject.interface';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { PostService } from 'src/app/core/api/services/post/post.service';
import { UserService } from 'src/app/core/api/services/user/user.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { SubjectService } from 'src/app/core/api/services/subject/subject.service';

describe('PostCreateComponent', () => {
    let component: PostCreateComponent;
    let fixture: ComponentFixture<PostCreateComponent>;
    
    let postService: Partial<PostService>;
    let userService: Partial<UserService>;
    let sessionService: Partial<SessionService>;
    let toastService: Partial<ToastService>;
    let subjectService: Partial<SubjectService>;
    let router: Partial<Router>;
    
    beforeEach(async () => {
        postService = { create: jest.fn() };
        userService = { getById: jest.fn() };
        sessionService = { sessionInformation: { id: 123, token: 'token', email: 'email', username: 'username', type: 'type' } };
        toastService = { error: jest.fn(), success: jest.fn() };
        subjectService = { all: jest.fn(), allForUser: jest.fn() };
        router = { navigate: jest.fn() };
        
        const activatedRouteMock = {
            params: of({}),
            snapshot: { paramMap: { get: () => null } }
        };
        
        await TestBed.configureTestingModule({
            imports: [PostCreateComponent],
            providers: [
                { provide: PostService, useValue: postService },
                { provide: UserService, useValue: userService },
                { provide: SessionService, useValue: sessionService },
                { provide: ToastService, useValue: toastService },
                { provide: SubjectService, useValue: subjectService },
                { provide: Router, useValue: router },
                { provide: ActivatedRoute, useValue: activatedRouteMock }
            ]
        }).compileComponents();
        
        fixture = TestBed.createComponent(PostCreateComponent);
        component = fixture.componentInstance;
    });
    
    it('charge uniquement les sujets abonnés pour l’utilisateur', async () => {
        const allSubjects: Subject[] = [
            { id: 1, name: 'Sujet 1', subscribed: true, description: "Description" },
            { id: 2, name: 'Sujet 2', subscribed: false, description: "Description" },
            { id: 3, name: 'Sujet 3', subscribed: true, description: "Description" }
        ];

        (userService.getById as jest.Mock).mockReturnValue(of({ id: 123, userName: 'testuser' }));
        (subjectService.allForUser as jest.Mock).mockReturnValue(of(allSubjects));

        await component.ngOnInit();

        expect(component.subjects.length).toBe(2);
        expect(component.subjects.map(s => s.id)).toEqual([1, 3]);
        expect(component['currentUserId']).toBe(123);
    });
    
    it('affiche une erreur si subjectService.all échoue', async () => {
        (subjectService.all as jest.Mock).mockReturnValue(throwError(() => new Error('fail')));
        
        await component.ngOnInit();
        
        expect(toastService.error).toHaveBeenCalledWith("Erreur lors de la récupération de l'utilisateur.");
    });
    
    it('affiche une erreur si aucune session n’est trouvée', async () => {
        (sessionService.sessionInformation as any) = undefined;
        (subjectService.all as jest.Mock).mockReturnValue(of([]));
        
        await component.ngOnInit();
        
        expect(toastService.error).toHaveBeenCalledWith(
            'Vous devez être identifié pour accéder à cette ressource.'
        );
    });
    
    it('affiche une erreur si l’utilisateur n’est pas trouvé', async () => {
        (sessionService.sessionInformation as any) = { id: 123 };
        (subjectService.all as jest.Mock).mockReturnValue(of([]));
        (userService.getById as jest.Mock).mockReturnValue(of(null));
        
        await component.ngOnInit();
        
        expect(toastService.error).toHaveBeenCalledWith('Utilisateur non trouvé.');
    });
    
    it('affiche une erreur si userService.getById échoue', async () => {
        (sessionService.sessionInformation as any) = { id: 123 };
        (subjectService.all as jest.Mock).mockReturnValue(of([]));
        (userService.getById as jest.Mock).mockReturnValue(throwError(() => new Error('fail')));
        
        await component.ngOnInit();
        
        expect(toastService.error).toHaveBeenCalledWith(
            'Erreur lors de la récupération de l\'utilisateur.'
        );
    });
    
    it('ne soumet pas si currentUserId est undefined', async () => {
        component['currentUserId'] = undefined;
        await component.submit({ title: 'Test', content: 'Contenu', subjectId: 1 } as any);
        expect(toastService.error).toHaveBeenCalledWith('Utilisateur non connecté.');
    });
    
    it('ne soumet pas si subjectId est null', async () => {
        component['currentUserId'] = 123;
        await component.submit({ title: 'Test', content: 'Contenu', subjectId: null } as any);
        expect(toastService.error).toHaveBeenCalledWith('Veuillez sélectionner un sujet.');
    });
    
    it('soumet correctement et navigue', async () => {
        component['currentUserId'] = 123;
        (postService.create as jest.Mock).mockReturnValue(of({ id: 1 }));
        (router.navigate as jest.Mock).mockResolvedValue(true);
        
        await component.submit({ title: 'Test', content: 'Contenu', subjectId: 1 });
        
        expect(toastService.success).toHaveBeenCalledWith('Article créé avec succès.');
        expect(router.navigate).toHaveBeenCalledWith(['/feed']);
    });
    
    it('affiche une erreur si la création échoue', async () => {
        component['currentUserId'] = 123;
        (postService.create as jest.Mock).mockReturnValue(throwError(() => new Error('fail')));
        
        await component.submit({ title: 'Test', content: 'Contenu', subjectId: 1 });
        
        expect(toastService.error).toHaveBeenCalledWith("Erreur lors de la création de l'article.");
    });

// =======================
// Tests submit() - erreurs détaillées
// =======================
describe('submit() erreurs détaillées', () => {
    const formValue = { title: 'Test', content: 'Contenu', subjectId: 1 };

    it('définit errorMessage si postService.create échoue avec une Error', async () => {
        component['currentUserId'] = 123;
        (postService.create as jest.Mock).mockReturnValue(
            throwError(() => new Error('fail'))
        );

        await component.submit(formValue as any);

        expect(component.errorMessage).toBe('fail');
        expect(router.navigate).not.toHaveBeenCalled();
        expect(toastService.error).toHaveBeenCalledWith("Erreur lors de la création de l'article.");
    });

    it('définit errorMessage à partir de error.error.message', async () => {
        component['currentUserId'] = 123;
        (postService.create as jest.Mock).mockReturnValue(
            throwError(() => ({
                error: { message: 'Titre déjà utilisé' }
            }))
        );

        await component.submit(formValue as any);

        expect(component.errorMessage).toBe('Titre déjà utilisé');
        expect(router.navigate).not.toHaveBeenCalled();
        expect(toastService.error).toHaveBeenCalledWith("Erreur lors de la création de l'article.");
    });

    it('définit un message générique pour une erreur non objet', async () => {
        component['currentUserId'] = 123;
        (postService.create as jest.Mock).mockReturnValue(
            throwError(() => 'boom')
        );

        await component.submit(formValue as any);

        expect(component.errorMessage).toBe('Une erreur est survenue');
        expect(router.navigate).not.toHaveBeenCalled();
        expect(toastService.error).toHaveBeenCalledWith("Erreur lors de la création de l'article.");
    });

    it('définit un message générique pour une erreur vide ou falsy', async () => {
        component['currentUserId'] = 123;
        (postService.create as jest.Mock).mockReturnValue(
            throwError(() => '')
        );

        await component.submit(formValue as any);

        expect(component.errorMessage).toBe('Une erreur est survenue');
        expect(router.navigate).not.toHaveBeenCalled();
        expect(toastService.error).toHaveBeenCalledWith("Erreur lors de la création de l'article.");
    });
});
});
