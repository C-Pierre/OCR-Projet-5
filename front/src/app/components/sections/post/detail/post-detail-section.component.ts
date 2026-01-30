import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Post } from 'src/app/core/models/post/post.interface';
import { PostDetailComponent } from '../../../parts/post/detail/post-detail.component';

@Component({
    selector: 'app-post-detail-section',
    standalone: true,
    imports: [CommonModule, PostDetailComponent],
    styleUrl: './post-detail-section.component.scss',
    template: `
        <section class="post-section" *ngIf="post">
            <h1 class="post-title">{{ post.title }}</h1>
            <app-post-detail [post]="post" />
        </section>
    `,
})
export class PostDetailSectionComponent {

  @Input({ required: true }) post!: Post;

}
