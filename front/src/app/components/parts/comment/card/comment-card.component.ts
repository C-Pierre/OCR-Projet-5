import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Comment } from 'src/app/core/models/comment/comment.interface';

@Component({
  selector: 'app-comment-card',
  standalone: true,
  imports: [CommonModule],
  styleUrl: './comment-card.component.scss',
  template: `
      <div class="comment" *ngIf="comment">
        <div class="username">
          {{ comment.authorUsername }}
        </div>
        <div class="content">
          {{ comment.content }}
        </div>
      </div>
  `,
})
export class CommentCardComponent {

  @Input({ required: true }) comment!: Comment;
  @Input() newComment = '';
}
