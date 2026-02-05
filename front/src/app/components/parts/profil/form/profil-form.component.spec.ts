import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { ProfilFormComponent } from './profil-form.component';
import { User } from 'src/app/core/models/user/user.interface';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

describe('ProfilFormComponent', () => {
    let component: ProfilFormComponent;
    let fixture: ComponentFixture<ProfilFormComponent>;
    
    const mockUser: User = {
        userName: 'JohnDoe',
        email: 'john@example.com',
        id: 1
    } as User;
    
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ProfilFormComponent, ReactiveFormsModule, CommonModule, ButtonComponent]
        }).compileComponents();
        
        fixture = TestBed.createComponent(ProfilFormComponent);
        component = fixture.componentInstance;
        component.user = mockUser;
        component.ngOnChanges({
            user: { currentValue: mockUser, previousValue: null, firstChange: true, isFirstChange: () => true }
        });
        fixture.detectChanges();
    });
    
    it('should create', () => {
        expect(component).toBeTruthy();
    });
    
    it('should patch form values when user input changes', () => {
        expect(component.profilForm.get('username')?.value).toBe('JohnDoe');
        expect(component.profilForm.get('email')?.value).toBe('john@example.com');
        expect(component.profilForm.get('password')?.value).toBe('');
    });
    
    it('should emit save when only username is changed', () => {
        const emitSpy = jest.spyOn(component.save, 'emit');

        component.profilForm.get('username')?.setValue('JaneDoe');
        component.submit();

        expect(emitSpy).toHaveBeenCalledWith({
            userName: 'JaneDoe'
        });
    });

    it('should emit save when only email is changed', () => {
        const emitSpy = jest.spyOn(component.save, 'emit');

        component.profilForm.get('email')?.setValue('jane@example.com');
        component.submit();

        expect(emitSpy).toHaveBeenCalledWith({
            email: 'jane@example.com'
        });
    });

    it('should emit save when only password is filled', () => {
        const emitSpy = jest.spyOn(component.save, 'emit');

        component.profilForm.get('password')?.setValue('Test!1234');
        component.submit();

        expect(emitSpy).toHaveBeenCalledWith({
            password: 'Test!1234'
        });
    });

    it('should not emit save if no values are changed', () => {
        const emitSpy = jest.spyOn(component.save, 'emit');

        component.profilForm.get('username')?.setValue('JohnDoe');
        component.profilForm.get('email')?.setValue('john@example.com');
        component.profilForm.get('password')?.setValue('');
        component.submit();

        expect(emitSpy).not.toHaveBeenCalled();
        expect(component.errorMessage).toBe('Aucune modification à enregistrer.');
    });

    it('should emit save with multiple changes', () => {
        const emitSpy = jest.spyOn(component.save, 'emit');

        component.profilForm.setValue({
            username: 'JaneDoe',
            email: 'jane@example.com',
            password: 'secret123'
        });

        component.submit();

        expect(emitSpy).toHaveBeenCalledWith({
            userName: 'JaneDoe',
            email: 'jane@example.com',
            password: 'secret123'
        });
    });

    it('should set errorMessage when username is too long', () => {
        const longUsername = 'a'.repeat(251);

        component.profilForm.setValue({
            username: longUsername,
            email: '',
            password: ''
        });

        component.submit();

        expect(component.errorMessage)
            .toContain('Le nom d’utilisateur est trop long.');
    });

    it('should not emit save if email format is invalid', () => {
        const emitSpy = jest.spyOn(component.save, 'emit');

        component.profilForm.setValue({
            username: '',
            email: 'invalid-email',
            password: ''
        });

        component.submit();

        expect(emitSpy).not.toHaveBeenCalled();
        expect(component.errorMessage)
            .toContain('Le format de l’email est invalide.');
    });
});
