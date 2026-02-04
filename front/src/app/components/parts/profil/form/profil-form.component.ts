import { Component, EventEmitter, Output, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { User } from 'src/app/core/models/user/user.interface';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

@Component({
  selector: 'app-profil-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonComponent],
  templateUrl: './profil-form.component.html'
})
export class ProfilFormComponent implements OnChanges {
  @Input() user!: User | null;
  @Output() save = new EventEmitter<Partial<User>>();
  @Input() errorMessage?: string;

  public profilForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.profilForm = this.fb.group({
      username: ['', [Validators.required, Validators.maxLength(250)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['']
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['user'] && this.user) {
      this.profilForm.patchValue({
        username: this.user.userName,
        email: this.user.email,
        password: ''
      });
    }
  }

  public submit(): void {
    if (this.profilForm.invalid) {
      this.errorMessage = this.buildErrorMessage();
      this.profilForm.markAllAsTouched();
      return;
    }

    const formValue = { ...this.profilForm.value };

    if (!formValue.password) {
      delete formValue.password;
    }

    const payload: Partial<User> = {
      userName: formValue.username,
      email: formValue.email,
      ...(formValue.password ? { password: formValue.password } : {})
    };

    this.save.emit(payload);
  }

  private buildErrorMessage(): string {
    const errors: string[] = [];

    const usernameCtrl = this.profilForm.get('username');
    if (usernameCtrl?.errors) {
      if (usernameCtrl.errors['required']) {
        errors.push('Le nom d’utilisateur est obligatoire.');
      }
      if (usernameCtrl.errors['maxlength']) {
        errors.push('Le nom d’utilisateur est trop long.');
      }
    }

    const emailCtrl = this.profilForm.get('email');
    if (emailCtrl?.errors) {
      if (emailCtrl.errors['required']) {
        errors.push('L’email est obligatoire.');
      }
      if (emailCtrl.errors['email']) {
        errors.push('Le format de l’email est invalide.');
      }
    }

    return errors.join(' ');
  }

}
