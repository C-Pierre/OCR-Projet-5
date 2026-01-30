import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { PostService } from 'src/app/core/api/services/post/post.service';
import { UserService } from 'src/app/core/api/services/user/user.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { SubjectService } from 'src/app/core/api/services/subject/subject.service';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { ButtonBackComponent } from 'src/app/components/elements/shared/button-back/button-back.component';
import { PostFormSectionComponent } from 'src/app/components/sections/post/form/post-form-section.component';

@Component({
  selector: 'app-post-create',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    ButtonBackComponent,
    PostFormSectionComponent,
    ReactiveFormsModule
  ],
  template: `
    <app-header></app-header>
    <main class="post-create-container">
      <app-button-back></app-button-back>
      <h2>Créer un nouvel article</h2>
      <app-post-form-section
        [postForm]="postForm"
        [subjects]="subjects"
        (submitForm)="submit($event)">
      </app-post-form-section>
    </main>
  `,
  styleUrl: './post-create.component.scss'
})
export class PostCreateComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private postService = inject(PostService);
  private subjectService = inject(SubjectService);
  private userService = inject(UserService);
  private sessionService = inject(SessionService);
  private toastService = inject(ToastService);

  postForm: FormGroup = this.fb.group({
    title: [''],
    content: [''],
    subjectId: [null]
  });

  subjects: { id: number; name: string }[] = [];
  private currentUserId!: number;

  async ngOnInit() {
    try {
      this.subjects = await firstValueFrom(this.subjectService.all());
    } catch {
      this.toastService.error("Erreur lors du chargement des sujets.");
    }

    const session = this.sessionService.sessionInformation;
    if (session?.id) {
      try {
        const user = await firstValueFrom(this.userService.getById(session.id.toString()));
        if (user) this.currentUserId = user.id;
        else this.toastService.error("Utilisateur non trouvé.");
      } catch {
        this.toastService.error("Erreur lors de la récupération de l'utilisateur.");
      }
    } else {
      this.toastService.error("Vous devez être identifié pour accéder à cette ressource.");
    }
  }

  async submit(formValue: any) {
    if (!this.currentUserId) return this.toastService.error("Utilisateur non connecté.");

    const createPostRequest = { ...formValue, authorId: this.currentUserId };
    try {
      await firstValueFrom(this.postService.create(createPostRequest));
      this.toastService.success('Article créé avec succès.');
      await this.router.navigate(['/feed']);
    } catch {
      this.toastService.error("Erreur lors de la création de l'article.");
    }
  }
}
