import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { LoginComponent } from './login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from 'src/app/core/api/services/auth/auth.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { ToastService } from 'src/app/core/services/toast/toast.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;

  const mockAuthService = {
    login: jest.fn()
  };

  const mockSessionService = {
    logIn: jest.fn()
  };

  const mockToastService = {
    error: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        LoginComponent,
        ReactiveFormsModule,
        RouterTestingModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: ToastService, useValue: mockToastService }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate').mockResolvedValue(true);

    jest.clearAllMocks();
    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('le formulaire est invalide par défaut', () => {
    expect(component.loginForm.invalid).toBe(true);
  });

  it('le formulaire devient valide avec des valeurs correctes', () => {
    component.loginForm.setValue({
      identifier: 'test@test.com',
      password: 'password123'
    });

    expect(component.loginForm.valid).toBe(true);
  });

  it('submit() ne fait rien si le formulaire est invalide', async () => {
    component.loginForm.setValue({
      identifier: '',
      password: ''
    });

    const markSpy = jest.spyOn(component.loginForm, 'markAllAsTouched');

    await component.submit();

    expect(markSpy).toHaveBeenCalled();
    expect(mockAuthService.login).not.toHaveBeenCalled();
    expect(mockSessionService.logIn).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('submit() connecte l’utilisateur et navigue en cas de succès', async () => {
    const sessionInfo = { id: 1, token: 'token' };

    component.loginForm.setValue({
      identifier: 'test@test.com',
      password: 'password123'
    });

    mockAuthService.login.mockReturnValue(of(sessionInfo));

    await component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith({
      identifier: 'test@test.com',
      password: 'password123'
    });

    expect(mockSessionService.logIn).toHaveBeenCalledWith(sessionInfo);
    expect(router.navigate).toHaveBeenCalledWith(['/themes']);
    expect(component.errorMessage).toBeUndefined();
  });

  it('submit() définit errorMessage si le login échoue avec une Error', async () => {
    component.loginForm.setValue({
      identifier: 'test@test.com',
      password: 'password123'
    });

    mockAuthService.login.mockReturnValue(
      throwError(() => new Error('401'))
    );

    await component.submit();

    expect(component.errorMessage).toBe('401');
    expect(mockSessionService.logIn).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
    expect(mockToastService.error).toHaveBeenCalled();
  });

  it('submit() définit un message générique si l’erreur n’est pas une Error', async () => {
    component.loginForm.setValue({
      identifier: 'test@test.com',
      password: 'password123'
    });

    mockAuthService.login.mockReturnValue(
      throwError(() => ({ error: {} }))
    );

    await component.submit();

    expect(component.errorMessage).toBe('Une erreur est survenue');
    expect(mockSessionService.logIn).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('appelle AuthService.login avec les valeurs du formulaire', async () => {
    mockAuthService.login.mockReturnValue(of({ id: 1, token: 'token' }));

    component.loginForm.setValue({
      identifier: 'user@test.com',
      password: 'password123'
    });

    await component.submit();

    expect(mockAuthService.login).toHaveBeenCalledTimes(1);
    expect(mockAuthService.login).toHaveBeenCalledWith({
      identifier: 'user@test.com',
      password: 'password123'
    });
  });

  it('submit() définit un message générique pour une erreur non objet', async () => {
    component.loginForm.setValue({
      identifier: 'test@test.com',
      password: 'password123'
    });

    mockAuthService.login.mockReturnValue(
      throwError(() => 'boom')
    );

    await component.submit();

    expect(component.errorMessage).toBe('Une erreur est survenue');
    expect(mockSessionService.logIn).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });
});
