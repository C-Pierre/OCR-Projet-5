import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { AuthService } from 'src/app/core/api/services/auth/auth.service';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RegisterRequest } from 'src/app/core/models/auth/request/registerRequest.interface';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';
import { ButtonBackComponent } from 'src/app/components/elements/shared/button-back/button-back.component';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        ButtonComponent,
        HeaderComponent,
        ButtonBackComponent
    ],
    templateUrl: './register.component.html'
})
export class RegisterComponent {

    private authService = inject(AuthService);
    private fb = inject(FormBuilder);
    private router = inject(Router);
    public onError = false;

    registerForm = this.fb.group({
        userName: ['', [Validators.required, Validators.maxLength(250)]],
        email: ['', [Validators.required, Validators.email, Validators.maxLength(250)]],
        password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(250)]]
    });

    public async submit(): Promise<void> {
        if (this.registerForm.invalid) {
            this.registerForm.markAllAsTouched();
            return;
        }

        const { userName, email, password } = this.registerForm.value;

        const registerRequest: RegisterRequest = {
            userName: userName ?? '',
            email: email ?? '',
            password: password ?? ''
        };

        try {
            await firstValueFrom(this.authService.register(registerRequest));
            this.onError = false;
            await this.router.navigate(['/login']);
        } catch (error) {
            this.onError = true;
        }
    }
}
