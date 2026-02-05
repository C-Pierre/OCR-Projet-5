import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './register.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from 'src/app/core/api/services/auth/auth.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { SessionInfo } from 'src/app/core/models/auth/sessionInfo.interface';

describe('RegisterComponent', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let router: Router;

    const mockAuthService = {
        register: jest.fn(),
        login: jest.fn()
    };

    const mockSessionService = {
        logIn: jest.fn()
    };

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [RegisterComponent, ReactiveFormsModule, RouterTestingModule],
            providers: [
                { provide: AuthService, useValue: mockAuthService },
                { provide: SessionService, useValue: mockSessionService }
            ],
            schemas: [NO_ERRORS_SCHEMA]
        }).compileComponents();

        fixture = TestBed.createComponent(RegisterComponent);
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
        expect(component.registerForm.invalid).toBe(true);
    });

    it('le formulaire devient valide avec des valeurs correctes', () => {
        component.registerForm.setValue({
            userName: 'user',
            email: 'user@test.com',
            password: 'password123'
        });
        expect(component.registerForm.valid).toBe(true);
    });

    it('submit() ne fait rien si le formulaire est invalide', async () => {
        component.registerForm.setValue({ userName: '', email: '', password: '' });
        const markSpy = jest.spyOn(component.registerForm, 'markAllAsTouched');
        await component.submit();

        expect(markSpy).toHaveBeenCalled();
        expect(mockAuthService.register).not.toHaveBeenCalled();
        expect(router.navigate).not.toHaveBeenCalled();
        expect(mockAuthService.login).not.toHaveBeenCalled();
        expect(mockSessionService.logIn).not.toHaveBeenCalled();
    });

    it('submit() enregistre, log-in et navigue vers /themes', async () => {
        component.registerForm.setValue({ userName: 'user', email: 'user@test.com', password: 'password123' });

        const fakeSession: SessionInfo = {
            id: 1,
            type: 'USER',
            username: 'user',
            email: 'user@test.com',
            token: 'jwt-token'
        };

        mockAuthService.register.mockReturnValue(of(void 0));
        mockAuthService.login.mockReturnValue(of(fakeSession));

        await component.submit();

        expect(mockAuthService.register).toHaveBeenCalledWith({
            userName: 'user',
            email: 'user@test.com',
            password: 'password123'
        });

        expect(mockAuthService.login).toHaveBeenCalledWith({
            identifier: 'user@test.com',
            password: 'password123'
        });

        expect(mockSessionService.logIn).toHaveBeenCalledWith(fakeSession);
        expect(router.navigate).toHaveBeenCalledWith(['/themes']);
        expect(component.errorMessage).toBeUndefined();
    });

    it('submit() définit errorMessage si register échoue', async () => {
        component.registerForm.setValue({ userName: 'user', email: 'user@test.com', password: 'password123' });
        mockAuthService.register.mockReturnValue(throwError(() => new Error('400')));

        await component.submit();

        expect(component.errorMessage).toBe('400');
        expect(router.navigate).not.toHaveBeenCalled();
    });

    it('submit() définit errorMessage à partir de error.error.message', async () => {
        component.registerForm.setValue({ userName: 'user', email: 'user@test.com', password: 'password123' });
        mockAuthService.register.mockReturnValue(throwError(() => ({ error: { message: 'Email déjà utilisé' } })));

        await component.submit();

        expect(component.errorMessage).toBe('Email déjà utilisé');
        expect(router.navigate).not.toHaveBeenCalled();
    });

    it('submit() définit un message générique pour une erreur non objet', async () => {
        component.registerForm.setValue({ userName: 'user', email: 'user@test.com', password: 'password123' });
        mockAuthService.register.mockReturnValue(throwError(() => 'boom'));

        await component.submit();

        expect(component.errorMessage).toBe('Une erreur est survenue');
        expect(router.navigate).not.toHaveBeenCalled();
    });

    describe('Submit Method - Nullish Coalescing Coverage (??)', () => {
        it('utilise "" si userName est null/undefined (pas vide)', async () => {
            component.registerForm.patchValue({
                userName: null as any,
                email: 'user@test.com',
                password: 'password123'
            });

            component.registerForm.controls.userName.clearValidators();
            component.registerForm.controls.userName.updateValueAndValidity();

            const fakeSession: SessionInfo = {
                id: 1,
                type: 'USER',
                username: 'user',
                email: 'user@test.com',
                token: 'jwt-token'
            };

            mockAuthService.register.mockReturnValue(of(void 0));
            mockAuthService.login.mockReturnValue(of(fakeSession));

            await component.submit();

            expect(mockAuthService.register).toHaveBeenCalledWith({
                userName: '',
                email: 'user@test.com',
                password: 'password123'
            });
        });

        it('utilise "" si email ou password sont null/undefined', async () => {
            component.registerForm.patchValue({
                userName: 'user',
                email: null as any,
                password: null as any
            });

            component.registerForm.controls.email.clearValidators();
            component.registerForm.controls.email.updateValueAndValidity();
            component.registerForm.controls.password.clearValidators();
            component.registerForm.controls.password.updateValueAndValidity();

            const fakeSession: SessionInfo = {
                id: 2,
                type: 'USER',
                username: 'user',
                email: 'user@test.com',
                token: 'jwt-token-2'
            };

            mockAuthService.register.mockReturnValue(of(void 0));
            mockAuthService.login.mockReturnValue(of(fakeSession));

            await component.submit();

            expect(mockAuthService.register).toHaveBeenCalledWith({
                userName: 'user',
                email: '',
                password: ''
            });

            expect(mockAuthService.login).toHaveBeenCalledWith({
                identifier: '',
                password: ''
            });
        });
    });
});
