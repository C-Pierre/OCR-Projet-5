import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { AuthService } from 'src/app/core/api/services/auth/auth.service';
import { SessionInfo } from 'src/app/core/models/auth/sessionInfo.interface';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { LoginRequest } from 'src/app/core/models/auth/request/loginRequest.interface';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';
import { ButtonBackComponent } from 'src/app/components/elements/shared/button-back/button-back.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonComponent,
    HeaderComponent,
    ButtonBackComponent
  ],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private toastService = inject(ToastService);
  private authService = inject(AuthService);
  private sessionService = inject(SessionService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  public errorMessage?: string;

  loginForm = this.fb.nonNullable.group({
    identifier: ['', [Validators.required, Validators.maxLength(250)]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  async submit(): Promise<void> {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const loginRequest = this.loginForm.value as LoginRequest;

    try {
      const response: SessionInfo = await firstValueFrom(this.authService.login(loginRequest));
      this.sessionService.logIn(response);
      void this.router.navigate(['/themes']);
    } catch (error: unknown) {
      if (error instanceof Error) {
          this.errorMessage = error.message;
      } else if (error && typeof error === 'object' && 'error' in error) {
          this.errorMessage = (error as any).error?.message || 'Une erreur est survenue';
      } else {
          this.errorMessage = 'Une erreur est survenue';
      }

      this.toastService.error("Erreur lors de la connexion.")
    }
  }
}
