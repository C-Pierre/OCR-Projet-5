import { provideRouter } from '@angular/router';
import { ProfilComponent } from './profil.component';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { User } from 'src/app/core/models/user/user.interface';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { UserService } from 'src/app/core/api/services/user/user.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { SubscriptionService } from 'src/app/core/api/services/subscription/subscription.service';
import { UserSubscriptionService } from 'src/app/core/services/subscription/user-subscription.service';

describe('ProfilComponent', () => {
  let component: ProfilComponent;
  let fixture: ComponentFixture<ProfilComponent>;
  let toastService: jest.Mocked<ToastService>;
  let sessionService: any;
  let userService: jest.Mocked<UserService>;
  let subscriptionService: jest.Mocked<SubscriptionService>;
  let userSubscriptionService: jest.Mocked<UserSubscriptionService>;

  beforeEach(async () => {
    toastService = {
      success: jest.fn(),
      error: jest.fn()
    } as any;

    sessionService = {
      isLogged$: new BehaviorSubject<boolean>(false),
      sessionInformation: null
    };

    userService = {
      getById: jest.fn(),
      update: jest.fn()
    } as any;

    subscriptionService = {
      getAllForUser: jest.fn()
    } as any;

    userSubscriptionService = {
      subscriptions$: of([]),
      showUnsubscribeModal$: of(false),
      setSubscriptions: jest.fn(),
      openUnsubscribeModal: jest.fn(),
      cancelUnsubscribe: jest.fn(),
      confirmUnsubscribe: jest.fn()
    } as any;

    await TestBed.configureTestingModule({
      imports: [ProfilComponent],
      providers: [
        provideRouter([]),
        { provide: ToastService, useValue: toastService },
        { provide: SessionService, useValue: sessionService },
        { provide: UserService, useValue: userService },
        { provide: SubscriptionService, useValue: subscriptionService },
        { provide: UserSubscriptionService, useValue: userSubscriptionService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProfilComponent);
    component = fixture.componentInstance;
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('ne charge rien si l’utilisateur n’est pas connecté', () => {
    fixture.detectChanges();

    expect(userService.getById).not.toHaveBeenCalled();
    expect(subscriptionService.getAllForUser).not.toHaveBeenCalled();
  });

  it('charge le profil et les subscriptions quand l’utilisateur est connecté', () => {
    const user: User = { id: 1 } as User;

    sessionService.sessionInformation = { id: 1 };
    sessionService.isLogged$.next(true);

    userService.getById.mockReturnValue(of(user));
    subscriptionService.getAllForUser.mockReturnValue(of([]));

    fixture.detectChanges();

    expect(userService.getById).toHaveBeenCalledWith('1');
    expect(subscriptionService.getAllForUser).toHaveBeenCalledWith('1');
    expect(userSubscriptionService.setSubscriptions).toHaveBeenCalled();
  });

  it('affiche un toast d’erreur si le chargement échoue', () => {
    sessionService.sessionInformation = { id: 1 };
    sessionService.isLogged$.next(true);

    userService.getById.mockReturnValue(
      throwError(() => new Error('fail'))
    );

    fixture.detectChanges();

    expect(toastService.error).toHaveBeenCalledWith(
      'Erreur lors du chargement du profil ou des subscriptions.'
    );
  });

  it('onSaveProfile met à jour le profil avec succès', async () => {
    const user: User = { id: 1 } as User;
    sessionService.sessionInformation = { id: 1 };

    userService.update.mockReturnValue(of(user));

    await component.onSaveProfile({});

    expect(userService.update).toHaveBeenCalled();
    expect(toastService.success).toHaveBeenCalledWith('Profil mis à jour.');
  });

  it('onSaveProfile affiche une erreur si la mise à jour échoue', async () => {
    sessionService.sessionInformation = { id: 1 };
    userService.update.mockReturnValue(
      throwError(() => new Error('fail'))
    );

    await component.onSaveProfile({});

    expect(toastService.error).toHaveBeenCalledWith(
      'Erreur lors de la mise à jour du profil.'
    );
  });

  it('onSaveProfile ne fait rien si aucun utilisateur connecté', async () => {
    sessionService.sessionInformation = null;

    await component.onSaveProfile({});

    expect(userService.update).not.toHaveBeenCalled();
  });

  it('openUnsubscribeModal délègue au UserSubscriptionService', () => {
    component.openUnsubscribeModal(10);
    expect(userSubscriptionService.openUnsubscribeModal).toHaveBeenCalledWith(10);
  });

  it('cancelUnsubscribe délègue au UserSubscriptionService', () => {
    component.cancelUnsubscribe();
    expect(userSubscriptionService.cancelUnsubscribe).toHaveBeenCalled();
  });

  it('confirmUnsubscribe délègue au UserSubscriptionService', () => {
    component.confirmUnsubscribe();
    expect(userSubscriptionService.confirmUnsubscribe).toHaveBeenCalled();
  });

    it('retourne un tableau vide si user est null après chargement', (done) => {
        sessionService.sessionInformation = { id: 1 };
        sessionService.isLogged$.next(true);

        userService.getById.mockReturnValue(of(null as any));

        fixture.detectChanges();

        setTimeout(() => {
            expect(subscriptionService.getAllForUser).not.toHaveBeenCalled();
            expect(userSubscriptionService.setSubscriptions).toHaveBeenCalledWith([]);
            done();
        }, 100);
    });

  describe('onSaveProfile() erreurs détaillées', () => {
    const profileData = {};

    it('définit errorMessage si userService.update échoue avec une Error', async () => {
      sessionService.sessionInformation = { id: 1 };
      (userService.update as jest.Mock).mockReturnValue(
          throwError(() => new Error('fail'))
      );

      await component.onSaveProfile(profileData);

      expect(component.errorMessage).toBe('fail');
      expect(toastService.error).toHaveBeenCalledWith(
          'Erreur lors de la mise à jour du profil.'
      );
    });

    it('définit errorMessage à partir de error.error.message', async () => {
        sessionService.sessionInformation = { id: 1 };
        (userService.update as jest.Mock).mockReturnValue(
            throwError(() => ({
                error: { message: 'Email déjà utilisé' }
            }))
        );

        await component.onSaveProfile(profileData);

        expect(component.errorMessage).toBe('Email déjà utilisé');
        expect(toastService.error).toHaveBeenCalledWith(
            'Erreur lors de la mise à jour du profil.'
        );
    });

    it('définit un message générique pour une erreur non objet', async () => {
        sessionService.sessionInformation = { id: 1 };
        (userService.update as jest.Mock).mockReturnValue(
            throwError(() => 'boom')
        );

        await component.onSaveProfile(profileData);

        expect(component.errorMessage).toBe('Une erreur est survenue');
        expect(toastService.error).toHaveBeenCalledWith(
            'Erreur lors de la mise à jour du profil.'
        );
    });

    it('définit un message générique pour une erreur vide ou falsy', async () => {
        sessionService.sessionInformation = { id: 1 };
        (userService.update as jest.Mock).mockReturnValue(
            throwError(() => '')
        );

        await component.onSaveProfile(profileData);

        expect(component.errorMessage).toBe('Une erreur est survenue');
        expect(toastService.error).toHaveBeenCalledWith(
            'Erreur lors de la mise à jour du profil.'
        );
    });
  });
});
