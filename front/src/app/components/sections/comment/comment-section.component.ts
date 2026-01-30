import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Comment } from 'src/app/core/models/comment/comment.interface';
import { CommentFormComponent } from '../../parts/comment/form/comment-form.component';
import { CommentCardComponent } from '../../parts/comment/card/comment-card.component';

@Component({
  selector: 'app-comment-section',
  standalone: true,
  imports: [
    CommonModule,
    CommentFormComponent,
    CommentCardComponent
  ],
  styleUrl: './comment-section.component.scss',
  template: `
    <section class="comment-section">
      <h2 class="title">Commentaires</h2>
      <app-comment-card *ngFor="let comment of comments" [comment]="comment" />
      <app-comment-form [newComment]="newComment" (newCommentChange)="newCommentChange.emit($event)" (commentSubmit)="submitComment.emit()" />
    </section>
  `,
})
export class CommentSectionComponent {

  @Input({ required: true }) comments: Comment[] = [];
  @Input() newComment = '';

  @Output() newCommentChange = new EventEmitter<string>();
  @Output() submitComment = new EventEmitter<void>();

  onSubmit(): void {
    this.submitComment.emit();
  }
}
