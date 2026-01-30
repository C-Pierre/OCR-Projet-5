import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton } from '@angular/material/button';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-comment-form',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIcon, MatIconButton],
  styleUrl: './comment-form.component.scss',
  template: `
    <form class="comment-form" (ngSubmit)="onSubmit()">
      <div class="form-field">
        <textarea
          id="comment"
          rows="5"
          name="comment"
          [(ngModel)]="newComment"
          (ngModelChange)="newCommentChange.emit($event)"
          placeholder="Écrivez votre commentaire ici">
        </textarea>
      </div>

      <button mat-icon-button class="submit-btn" type="submit" [disabled]="!newComment.trim()">
        <mat-icon>send_outlined</mat-icon>
      </button>
    </form>
  `,
})
export class CommentFormComponent {
  @Input() newComment = '';
  @Output() newCommentChange = new EventEmitter<string>();
  @Output() commentSubmit = new EventEmitter<void>();

  onSubmit(): void {
    if (!this.newComment.trim()) return;
    this.commentSubmit.emit();
  }
}
