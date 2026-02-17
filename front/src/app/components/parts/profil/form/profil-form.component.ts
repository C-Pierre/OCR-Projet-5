import { CommonModule } from '@angular/common';
import { User } from 'src/app/core/models/user/user.interface';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';
import { Component, EventEmitter, Output, Input, OnChanges, SimpleChanges } from '@angular/core';

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
      username: ['', [Validators.maxLength(250)]],
      email: ['', [Validators.email]],
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
    this.errorMessage = undefined;

    if (this.profilForm.invalid) {
      this.errorMessage = this.buildErrorMessage();
      this.profilForm.markAllAsTouched();
      return;
    }

    const { username, email, password } = this.profilForm.value;
    const payload: Partial<User> = {};

    if (username && username !== this.user?.userName) {
      payload.userName = username;
    }

    if (email && email !== this.user?.email) {
      payload.email = email;
    }

    if (password) {
      payload.password = password;
    }

    if (Object.keys(payload).length === 0) {
      this.errorMessage = 'Aucune modification à enregistrer.';
      return;
    }

    this.save.emit(payload);
}

  private buildErrorMessage(): string {
    const errors: string[] = [];

    const usernameCtrl = this.profilForm.get('username');
    if (usernameCtrl?.errors?.['maxlength']) {
      errors.push('Le nom d’utilisateur est trop long.');
    }

    const emailCtrl = this.profilForm.get('email');
    if (emailCtrl?.errors?.['email']) {
      errors.push('Le format de l’email est invalide.');
    }

    return errors.join(' ');
  }
}
