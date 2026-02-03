import { CommonModule } from '@angular/common';
import { ProfilFormComponent } from './profil-form.component';
import { User } from 'src/app/core/models/user/user.interface';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
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
        fixture.detectChanges();
    });
    
    it('should create', () => {
        expect(component).toBeTruthy();
    });
    
    it('should patch form values when user input changes', () => {
        component.user = mockUser;
        component.ngOnChanges({ user: { currentValue: mockUser, previousValue: null, firstChange: true, isFirstChange: () => true } });
        fixture.detectChanges();
        
        expect(component.profilForm.get('username')?.value).toBe('JohnDoe');
        expect(component.profilForm.get('email')?.value).toBe('john@example.com');
        expect(component.profilForm.get('password')?.value).toBe('');
    });
    
    it('should not emit save if form is invalid', () => {
        const emitSpy = jest.spyOn(component.save, 'emit');
        
        component.profilForm.get('username')?.setValue('');
        component.profilForm.get('email')?.setValue('invalid-email');
        component.submit();
        
        expect(emitSpy).not.toHaveBeenCalled();
    });
    
    it('should emit save with password if provided', () => {
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
    
    it('should emit save without password if empty', () => {
        const emitSpy = jest.spyOn(component.save, 'emit');
        
        component.profilForm.setValue({
            username: 'JaneDoe',
            email: 'jane@example.com',
            password: ''
        });
        
        component.submit();
        
        expect(emitSpy).toHaveBeenCalledWith({
            userName: 'JaneDoe',
            email: 'jane@example.com'
        });
    });
    
    it('should validate required and email fields', () => {
        const username = component.profilForm.get('username')!;
        const email = component.profilForm.get('email')!;
        
        username.setValue('');
        email.setValue('invalid-email');
        fixture.detectChanges();
        
        expect(username.valid).toBeFalsy();
        expect(email.valid).toBeFalsy();
        
        username.setValue('ValidName');
        email.setValue('valid@example.com');
        fixture.detectChanges();
        
        expect(username.valid).toBeTruthy();
        expect(email.valid).toBeTruthy();
    });
});
