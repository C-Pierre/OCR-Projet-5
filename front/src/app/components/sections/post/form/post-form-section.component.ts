import { CommonModule } from '@angular/common';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PostFormComponent } from 'src/app/components/parts/post/form/post-form.component';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

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
  styleUrl: './post-form-section.component.scss'
})
export class PostFormSectionComponent {
  @Input() postForm!: FormGroup;
  @Input() subjects: { id: number; name: string }[] = [];
  @Output() submitForm = new EventEmitter<any>();

  onSubmit() {
    if (this.postForm.valid) {
      this.submitForm.emit(this.postForm.value);
    } else {
      this.postForm.markAllAsTouched();
    }
  }
}