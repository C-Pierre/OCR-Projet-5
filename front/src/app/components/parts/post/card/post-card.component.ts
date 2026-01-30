import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Post } from 'src/app/core/models/post/post.interface';

@Component({
    selector: 'app-post-card',
    standalone: true,
    imports: [CommonModule],
    styleUrl: './post-card.component.scss',
    template: `
        <div class="post-card" *ngIf="post">
            <div class="post-meta">
                <span class="date">{{ post.createdAt | date:'dd/MM/yyyy' }}</span>
                <span class="author">{{ post.authorUsername }}</span>
                <span class="subject">{{ post.subjectName }}</span>
            </div>
            <div class="post-content">{{ post.content }}</div>
        </div>
    `,
})
export class PostCardComponent {

  @Input({ required: true }) post!: Post;
}
