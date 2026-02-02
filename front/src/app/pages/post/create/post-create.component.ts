import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { User } from 'src/app/core/models/user/user.interface';
import { Subject } from 'src/app/core/models/subject/subject.interface';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { PostService } from 'src/app/core/api/services/post/post.service';
import { UserService } from 'src/app/core/api/services/user/user.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { SubjectService } from 'src/app/core/api/services/subject/subject.service';
import { PostRequest } from 'src/app/core/models/post/request/postRequest.interface';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { ButtonBackComponent } from 'src/app/components/elements/shared/button-back/button-back.component';
import { PostFormSectionComponent } from 'src/app/components/sections/post/form/post-form-section.component';

interface PostFormValue {
  title: string;
  content: string;
  subjectId: number | null;
}

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
  private router = inject(Router);
  private postService = inject(PostService);
  private subjectService = inject(SubjectService);
  private userService = inject(UserService);
  private sessionService = inject(SessionService);
  private toastService = inject(ToastService);

  postForm: FormGroup<{
    title: FormControl<string>;
    content: FormControl<string>;
    subjectId: FormControl<number | null>;
  }> = new FormGroup({
    title: new FormControl('', { nonNullable: true }),
    content: new FormControl('', { nonNullable: true }),
    subjectId: new FormControl<number | null>(null)
  });

  subjects: Subject[] = [];
  private currentUserId?: number;

  async ngOnInit() {
    try {
      this.subjects = await firstValueFrom(this.subjectService.all());
    } catch {
      this.toastService.error("Erreur lors du chargement des sujets.");
    }

    const session = this.sessionService.sessionInformation;
    if (session?.id) {
      try {
        const user: User = await firstValueFrom(this.userService.getById(session.id.toString()));
        if (user) this.currentUserId = user.id;
        else this.toastService.error("Utilisateur non trouvé.");
      } catch {
        this.toastService.error("Erreur lors de la récupération de l'utilisateur.");
      }
    } else {
      this.toastService.error("Vous devez être identifié pour accéder à cette ressource.");
    }
  }

  async submit(formValue: PostFormValue) {
    if (!this.currentUserId) {
      return this.toastService.error("Utilisateur non connecté.");
    }

    if (formValue.subjectId === null) {
      return this.toastService.error("Veuillez sélectionner un sujet.");
    }

    const createPostRequest: PostRequest = {
      authorId: this.currentUserId,
      title: formValue.title,
      content: formValue.content,
      subjectId: formValue.subjectId
    };

    try {
      await firstValueFrom(this.postService.create(createPostRequest));
      this.toastService.success('Article créé avec succès.');
      await this.router.navigate(['/feed']);
    } catch {
      this.toastService.error("Erreur lors de la création de l'article.");
    }
  }
}
