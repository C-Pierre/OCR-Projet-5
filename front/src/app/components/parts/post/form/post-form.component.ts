import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
    selector: 'app-post-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    styleUrl: './post-form.component.scss',
    template: `
        <div [formGroup]="postForm" class="form-container">
            <h2>Créer un nouvel article</h2>

            <div class="form-group">
            <label for="subjectId">Sujet</label>
            <select id="subjectId" formControlName="subjectId">
                <option value="" disabled selected>Choisir un sujet</option>

                @for (subject of subjects; track subject.id) {
                <option [value]="subject.id">
                    {{ subject.name }}
                </option>
                }
            </select>

            @if (postForm.get('subjectId')?.invalid && postForm.get('subjectId')?.touched) {
                <div class="error">
                Sujet requis
                </div>
            }
            </div>

            <div class="form-group">
            <label for="title">Titre</label>
            <input id="title" type="text" formControlName="title">

            @if (postForm.get('title')?.invalid && postForm.get('title')?.touched) {
                <div class="error">
                Titre requis (max 200 caractères)
                </div>
            }
            </div>

            <div class="form-group">
            <label for="content">Contenu</label>
            <textarea id="content" formControlName="content" rows="6"></textarea>

            @if (postForm.get('content')?.invalid && postForm.get('content')?.touched) {
                <div class="error">
                Contenu requis (max 5000 caractères)
                </div>
            }
            </div>
        </div>
    `,
})
export class PostFormComponent {
  @Input() postForm!: FormGroup;
  @Input() subjects: { id: number; name: string }[] = [];
}