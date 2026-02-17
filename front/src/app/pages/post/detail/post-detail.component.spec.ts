
import { of, throwError } from 'rxjs';
import { ActivatedRoute, ActivatedRouteSnapshot, ParamMap } from '@angular/router';
import { PostDetailComponent } from './post-detail.component';
import { Post } from 'src/app/core/models/post/post.interface';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { Comment } from 'src/app/core/models/comment/comment.interface';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { PostService } from 'src/app/core/api/services/post/post.service';
import { UserService } from 'src/app/core/api/services/user/user.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { CommentService } from 'src/app/core/api/services/comment/comment.service';

describe('PostDetailComponent', () => {
    let component: PostDetailComponent;
    let fixture: ComponentFixture<PostDetailComponent>;
    
    let postService: Partial<PostService>;
    let commentService: Partial<CommentService>;
    let userService: Partial<UserService>;
    let sessionService: Partial<SessionService>;
    let toastService: Partial<ToastService>;
    let route: Partial<ActivatedRoute>;
    
    beforeEach(async () => {
        postService = { getOneById: jest.fn() };
        commentService = { getAllByPost: jest.fn(), create: jest.fn() };
        userService = { getById: jest.fn() };
        sessionService = { sessionInformation: { id: 123, token: "token", email: "email", username: "username", type: "type" } };
        toastService = { error: jest.fn(), success: jest.fn() };
        
        route = {
            snapshot: {
                paramMap: {
                    get: jest.fn().mockReturnValue('1'),
                    getAll: jest.fn().mockReturnValue([]),
                    has: jest.fn().mockReturnValue(true),
                    keys: []
                } as unknown as ParamMap
            } as ActivatedRouteSnapshot
        };
        
        await TestBed.configureTestingModule({
            imports: [PostDetailComponent],
            providers: [
                { provide: PostService, useValue: postService },
                { provide: CommentService, useValue: commentService },
                { provide: UserService, useValue: userService },
                { provide: SessionService, useValue: sessionService },
                { provide: ToastService, useValue: toastService },
                { provide: ActivatedRoute, useValue: route }
            ]
        }).compileComponents();
        
        fixture = TestBed.createComponent(PostDetailComponent);
        component = fixture.componentInstance;
    });
    
    
    it('devrait initialiser le post et les commentaires', async () => {
        const mockPost: Post = { id: 1, title: 'Post 1', content: 'Contenu' } as Post;
        const mockComments: Comment[] = [{ id: 1, content: 'Comment 1' } as Comment];
        
        (postService.getOneById as jest.Mock).mockReturnValue(of(mockPost));
        (commentService.getAllByPost as jest.Mock).mockReturnValue(of(mockComments));
        (userService.getById as jest.Mock).mockReturnValue(of({ id: 123, userName: 'testuser' }));
        
        fixture.detectChanges();
        await fixture.whenStable();
        
        expect(component.post).toEqual(mockPost);
        expect(component.comments).toEqual(mockComments);
        expect(component.userId).toBe(123);
        expect(component.authorUsername).toBe('testuser');
    });
    
    it('devrait appeler toast.error si le chargement du post échoue', async () => {
        (postService.getOneById as jest.Mock).mockReturnValue(throwError(() => new Error('fail')));
        (userService.getById as jest.Mock).mockReturnValue(of({ id: 123, userName: 'testuser' }));
        
        fixture.detectChanges();
        await fixture.whenStable();
        
        expect(toastService.error).toHaveBeenCalledWith(
            'Erreur lors du chargement du post ou des commentaires.'
        );
    });
    
    it('devrait appeler toast.error si l’utilisateur n’existe pas', async () => {
        const mockPost: Post = { id: 1, title: 'Post 1', content: 'Contenu' } as Post;
        (postService.getOneById as jest.Mock).mockReturnValue(of(mockPost));
        (commentService.getAllByPost as jest.Mock).mockReturnValue(of([]));
        (userService.getById as jest.Mock).mockReturnValue(of(null));
        
        fixture.detectChanges();
        await fixture.whenStable();
        
        expect(toastService.error).toHaveBeenCalledWith('Utilisateur non connecté.');
    });
    
    it('devrait soumettre un commentaire avec succès', async () => {
        component.post = { id: 1 } as Post;
        component.userId = 123;
        component.newComment = 'Nouveau commentaire';
        const createdComment: Comment = { id: 1, content: 'Nouveau commentaire' } as Comment;
        
        (commentService.create as jest.Mock).mockReturnValue(of(createdComment));
        
        component.submitComment();
        
        expect(component.comments).toContain(createdComment);
        expect(component.newComment).toBe('');
        expect(toastService.success).toHaveBeenCalledWith('Commentaire ajouté avec succès.');
    });
    
    it('ne devrait pas soumettre un commentaire vide', () => {
        component.newComment = '   ';
        component.submitComment();
        expect(commentService.create).not.toHaveBeenCalled();
    });
    
    it('devrait afficher une erreur si la création de commentaire échoue', () => {
        component.post = { id: 1 } as Post;
        component.userId = 123;
        component.newComment = 'Nouveau commentaire';
        
        (commentService.create as jest.Mock).mockReturnValue(throwError(() => new Error('fail')));
        
        component.submitComment();
        
        expect(toastService.error).toHaveBeenCalledWith(
            'Erreur lors de la création du commentaire.'
        );
    });
    
    it('devrait appeler window.history.back sur back()', () => {
        const spy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
        component.back();
        expect(spy).toHaveBeenCalled();
        spy.mockRestore();
    });
    
    it('ne fait rien si le postId est null', () => {
        (route!.snapshot?.paramMap.get as jest.Mock).mockReturnValue(null);
        fixture.detectChanges();
        expect(component.post).toBeUndefined();
        expect(component.comments).toEqual([]);
    });
    
    it("appelle toast.error si la récupération de l'utilisateur échoue", async () => {
        const mockPost: Post = { id: 1, title: 'Post 1', content: 'Contenu' } as Post;
        (postService.getOneById as jest.Mock).mockReturnValue(of(mockPost));
        (commentService.getAllByPost as jest.Mock).mockReturnValue(of([]));
        (userService.getById as jest.Mock).mockReturnValue(throwError(() => new Error('fail')));
        
        fixture.detectChanges();
        await fixture.whenStable();
        
        expect(toastService.error).toHaveBeenCalledWith(
            "Erreur lors de la récupération de l'utilisateur."
        );
    });
    
    it("appelle toast.error si aucune session n'est trouvée", async () => {
        (sessionService as any).sessionInformation = undefined;
        
        (postService.getOneById as jest.Mock).mockReturnValue(of({ id: 1, title: 'Post 1', content: 'Contenu' }));
        (commentService.getAllByPost as jest.Mock).mockReturnValue(of([]));
        
        fixture.detectChanges();
        await fixture.whenStable();
        
        expect(toastService.error).toHaveBeenCalledWith('Aucune session trouvée.');
    });
});
