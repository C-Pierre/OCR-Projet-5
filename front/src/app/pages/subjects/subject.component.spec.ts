import { provideRouter } from '@angular/router';
import { SubjectsComponent } from './subjects.component';
import { BehaviorSubject, of, throwError, take } from 'rxjs';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Subject } from 'src/app/core/models/subject/subject.interface';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { SubjectService } from 'src/app/core/api/services/subject/subject.service';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';
import { UserSubscriptionService } from 'src/app/core/services/subscription/user-subscription.service';

describe('SubjectsComponent', () => {
    let component: SubjectsComponent;
    let fixture: ComponentFixture<SubjectsComponent>;
    let toastService: jest.Mocked<ToastService>;
    let subjectService: jest.Mocked<SubjectService>;
    let sessionService: any;
    let userSubscriptionService: any;
    
    beforeEach(async () => {
        toastService = { error: jest.fn() } as any;
        
        subjectService = { 
            all: jest.fn(), 
            allForUser: jest.fn() 
        } as any;
        
        sessionService = {
            isLogged$: new BehaviorSubject<boolean>(false),
            hasSession: jest.fn().mockReturnValue(false),
            sessionInformation: null
        };
        
        userSubscriptionService = {
            openSubscribeModal: jest.fn(),
            subscriptions$: new BehaviorSubject<Subscription[]>([]) // <- initialisation obligatoire
        };
        
        await TestBed.configureTestingModule({
            imports: [SubjectsComponent],
            providers: [
                provideRouter([]),
                { provide: ToastService, useValue: toastService },
                { provide: SubjectService, useValue: subjectService },
                { provide: SessionService, useValue: sessionService },
                { provide: UserSubscriptionService, useValue: userSubscriptionService }
            ]
        }).compileComponents();
        
        fixture = TestBed.createComponent(SubjectsComponent);
        component = fixture.componentInstance;
    });
    
    it('devrait créer le composant', () => {
        expect(component).toBeTruthy();
    });
    
    it('initialise themes$ avec subscribed=false par défaut', async () => {
        const subjects: Subject[] = [
            { id: 1, name: 'Angular' } as Subject,
            { id: 2, name: 'RxJS' } as Subject
        ];
        
        subjectService.all.mockReturnValue(of(subjects));
        
        fixture.detectChanges();
        
        const result = await component.themes$.pipe(take(1)).toPromise();
        expect(result).toHaveLength(2);
        expect(result?.[0].subscribed).toBe(false);
        expect(result?.[1].subscribed).toBe(false);
    });
    
    it('marque subscribed=true si l’utilisateur est connecté et abonné', async () => {
        sessionService.sessionInformation = { id: 123 };
        sessionService.isLogged$ = new BehaviorSubject(true);
        sessionService.hasSession = jest.fn().mockReturnValue(true);

        const subjects: (Subject & { subscribed: boolean })[] = [
            { id: 1, name: 'Angular', subscribed: true, description: "Description" },
            { id: 2, name: 'RxJS', subscribed: false, description: "Description" }
        ];
        subjectService.allForUser.mockReturnValue(of(subjects));

        // Simuler un abonnement existant dans subscriptions$
        userSubscriptionService.subscriptions$.next([{ id: 1, name: 'Angular', description: 'Description' }]);

        fixture.detectChanges();

        const result = await component.themes$.pipe(take(1)).toPromise();

        expect(subjectService.allForUser).toHaveBeenCalledWith('123');
        expect(result).toHaveLength(2);
        expect(result?.[0].subscribed).toBe(true);  // abonné via subscriptions$
        expect(result?.[1].subscribed).toBe(false); // non abonné
    });
    
    it('affiche un toast si le chargement échoue', async () => {
        subjectService.all.mockReturnValue(throwError(() => new Error('fail')));
        
        fixture.detectChanges();
        
        const result = await component.themes$.pipe(take(1)).toPromise();
        
        expect(result).toEqual([]);
        expect(toastService.error).toHaveBeenCalledWith(
            'Erreur lors du chargement des thèmes.'
        );
    });
    
    it('subscribe() affiche une alerte si utilisateur non connecté', () => {
        const alertSpy = jest.spyOn(window, 'alert').mockImplementation();
        
        component.subscribe(1);
        
        expect(alertSpy).toHaveBeenCalledWith(
            'Vous devez être connecté pour vous abonner.'
        );
        expect(userSubscriptionService.openSubscribeModal).not.toHaveBeenCalled();
    });
    
    it('subscribe() ouvre la modale si utilisateur connecté', () => {
        sessionService.sessionInformation = { id: 1 };
        
        component.subscribe(42);
        
        expect(userSubscriptionService.openSubscribeModal).toHaveBeenCalledWith(42);
    });
    
    it('trackById retourne l’id du sujet', () => {
        const subject = { id: 99 } as Subject;
        expect(component.trackById(0, subject)).toBe(99);
    });
});
