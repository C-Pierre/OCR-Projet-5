import { CommonModule } from '@angular/common';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PostFormComponent } from 'src/app/components/parts/post/form/post-form.component';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

export interface PostFormValue {
  title: string;
  content: string;
  subjectId: number | null;
}

export interface PostFormControls {
  title: FormControl<string>;
  content: FormControl<string>;
  subjectId: FormControl<number | null>;
}

@Component({
  selector: 'app-post-form-section',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PostFormComponent, ButtonComponent],
  template: `
    <form [formGroup]="postForm" (ngSubmit)="onSubmit()">
      <app-post-form [postForm]="postForm" [subjects]="subjects"></app-post-form>
      <app-button title="Créer le post" type="submit" class="primary"></app-button>
    </form>
  `,
  styles: `h2 { text-align: center; margin-bottom: 1.5rem; }`
})
export class PostFormSectionComponent {
  @Input() postForm!: FormGroup<PostFormControls>;
  @Input() subjects: { id: number; name: string }[] = [];
  @Output() submitForm = new EventEmitter<PostFormValue>();

  onSubmit() {
    if (this.postForm.valid) {
      // extraire les valeurs typées
      const value: PostFormValue = {
        title: this.postForm.controls.title.value,
        content: this.postForm.controls.content.value,
        subjectId: this.postForm.controls.subjectId.value
      };
      this.submitForm.emit(value);
    } else {
      this.postForm.markAllAsTouched();
    }
  }
}
