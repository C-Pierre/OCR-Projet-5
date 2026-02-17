import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Comment } from 'src/app/core/models/comment/comment.interface';

@Component({
  selector: 'app-comment-detail',
  standalone: true,
  imports: [CommonModule],
  styleUrl: './comment-detail.component.scss',
  template: `
    @if (comment) {
      <div class="comment">
        <div class="username">
          {{ comment.authorUsername }}
        </div>
        <div class="content">
          {{ comment.content }}
        </div>
      </div>
    }
  `,
})
export class CommentDetailComponent {

  @Input({ required: true }) comment!: Comment;
  @Input() newComment = '';
}
