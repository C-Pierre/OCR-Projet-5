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
            <div class="form-group">
            <label for="subjectId">Sujet</label>
            <select id="subjectId" formControlName="subjectId">
                <option value="" disabled selected>Choisir un sujet</option>
                <option *ngFor="let subject of subjects" [value]="subject.id">{{ subject.name }}</option>
            </select>
            <div class="error" *ngIf="postForm.get('subjectId')?.invalid && postForm.get('subjectId')?.touched">
                Sujet requis
            </div>
            </div>

            <div class="form-group">
            <label for="title">Titre</label>
            <input id="title" type="text" formControlName="title">
            <div class="error" *ngIf="postForm.get('title')?.invalid && postForm.get('title')?.touched">
                Titre requis (max 200 caractères)
            </div>
            </div>

            <div class="form-group">
            <label for="content">Contenu</label>
            <textarea id="content" formControlName="content" rows="6"></textarea>
            <div class="error" *ngIf="postForm.get('content')?.invalid && postForm.get('content')?.touched">
                Contenu requis (max 5000 caractères)
            </div>
            </div>
        </div>
    `
})
export class PostFormComponent {
  @Input() postForm!: FormGroup;
  @Input() subjects: { id: number; name: string }[] = [];
}