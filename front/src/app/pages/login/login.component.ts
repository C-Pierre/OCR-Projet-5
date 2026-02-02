import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
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

  private authService = inject(AuthService);
  private sessionService = inject(SessionService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  public onError = false;

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
    } catch (error) {
      this.onError = true;
    }
  }

  back() {
      window.history.back();
  }
}
