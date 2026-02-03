import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './register.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from 'src/app/core/api/services/auth/auth.service';

describe('RegisterComponent', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let router: Router;
    
    const mockAuthService = {
        register: jest.fn()
    };
    
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [
                RegisterComponent,
                ReactiveFormsModule,
                RouterTestingModule
            ],
            providers: [
                { provide: AuthService, useValue: mockAuthService }
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
        component.registerForm.setValue({
            userName: '',
            email: '',
            password: ''
        });
        
        const markSpy = jest.spyOn(component.registerForm, 'markAllAsTouched');
        
        await component.submit();
        
        expect(markSpy).toHaveBeenCalled();
        expect(mockAuthService.register).not.toHaveBeenCalled();
        expect(router.navigate).not.toHaveBeenCalled();
    });
    
    it('submit() enregistre l’utilisateur et navigue vers /login', async () => {
        component.registerForm.setValue({
            userName: 'user',
            email: 'user@test.com',
            password: 'password123'
        });
        
        mockAuthService.register.mockReturnValue(of(void 0));
        
        await component.submit();
        
        expect(mockAuthService.register).toHaveBeenCalledWith({
            userName: 'user',
            email: 'user@test.com',
            password: 'password123'
        });
        
        expect(component.onError).toBe(false);
        expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });
    
    it('submit() met onError à true si l’inscription échoue', async () => {
        component.registerForm.setValue({
            userName: 'user',
            email: 'user@test.com',
            password: 'password123'
        });
        
        mockAuthService.register.mockReturnValue(
            throwError(() => new Error('400'))
        );
        
        await component.submit();
        
        expect(component.onError).toBe(true);
        expect(router.navigate).not.toHaveBeenCalled();
    });
    
    it('appelle AuthService.register avec les valeurs du formulaire', async () => {
        mockAuthService.register.mockReturnValue(of(void 0));
        
        component.registerForm.setValue({
            userName: 'anotherUser',
            email: 'another@test.com',
            password: 'password123'
        });
        
        await component.submit();
        
        expect(mockAuthService.register).toHaveBeenCalledTimes(1);
        expect(mockAuthService.register).toHaveBeenCalledWith({
            userName: 'anotherUser',
            email: 'another@test.com',
            password: 'password123'
        });
    });
    
    describe('Submit Method - Nullish Coalescing Coverage (??)', () => {
        it('devrait utiliser une chaîne vide si userName est null ou undefined', async () => {
            // Remplir le formulaire pour le rendre valide
            component.registerForm.setValue({
                userName: 'user',
                email: 'user@test.com',
                password: 'password123'
            });
            
            // Mock la propriété value pour retourner userName null
            Object.defineProperty(component.registerForm, 'value', {
                get: () => ({
                    userName: null,
                    email: 'user@test.com',
                    password: 'password123'
                }),
                configurable: true
            });
            
            mockAuthService.register.mockReturnValue(of(void 0));
            
            await component.submit();
            
            expect(mockAuthService.register).toHaveBeenCalledWith({
                userName: '',
                email: 'user@test.com',
                password: 'password123'
            });
        });
        
        it('devrait utiliser une chaîne vide si email ou password sont null/undefined', async () => {
            // Remplir le formulaire pour le rendre valide
            component.registerForm.setValue({
                userName: 'user',
                email: 'user@test.com',
                password: 'password123'
            });
            
            // Mock la propriété value pour retourner email et password null/undefined
            Object.defineProperty(component.registerForm, 'value', {
                get: () => ({
                    userName: 'user',
                    email: null,
                    password: undefined
                }),
                configurable: true
            });
            
            mockAuthService.register.mockReturnValue(of(void 0));
            
            await component.submit();
            
            expect(mockAuthService.register).toHaveBeenCalledWith({
                userName: 'user',
                email: '',
                password: ''
            });
        });
    });
});
