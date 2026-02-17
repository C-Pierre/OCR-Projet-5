import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Post } from 'src/app/core/models/post/post.interface';

@Component({
    selector: 'app-post-detail',
    standalone: true,
    imports: [CommonModule],
    styleUrl: './post-detail.component.scss',
    template: `
    @if (post) {
        <div class="post-card">
            <div class="post-meta">
                <span class="date">{{ post.createdAt | date:'dd/MM/yyyy' }}</span>
                <span class="author">{{ post.authorUsername }}</span>
                <span class="subject">{{ post.subjectName }}</span>
            </div>
            <div class="post-content">
                {{ post.content }}
            </div>
        </div>
    }
`,
})
export class PostDetailComponent {

  @Input({ required: true }) post!: Post;
}
