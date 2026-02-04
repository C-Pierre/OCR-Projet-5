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
            </div>

            <div class="form-group">
                <label for="title">Titre</label>
                <input id="title" type="text" formControlName="title">
            </div>

            <div class="form-group">
            <label for="content">Contenu</label>
                <textarea id="content" formControlName="content" rows="6"></textarea>
            </div>

            @if (errorMessage) {
             <div class="error">{{ errorMessage }}</div>
            }
        </div>
    `,
})
export class PostFormComponent {
  @Input() postForm!: FormGroup;
  @Input() subjects: { id: number; name: string }[] = [];
  @Input() errorMessage?: string;
}