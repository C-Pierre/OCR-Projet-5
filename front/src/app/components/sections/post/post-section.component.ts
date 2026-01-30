import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Post } from 'src/app/core/models/post/post.interface';
import { PostCardComponent } from '../../parts/post/card/post-card.component';

@Component({
    selector: 'app-post-section',
    standalone: true,
    imports: [CommonModule, PostCardComponent],
    styleUrl: './post-section.component.scss',
    template: `
        <section class="post-section" *ngIf="post">
            <h1 class="post-title">{{ post.title }}</h1>
            <app-post-card [post]="post" />
        </section>
    `,
})
export class PostSectionComponent {

  @Input({ required: true }) post!: Post;

}
