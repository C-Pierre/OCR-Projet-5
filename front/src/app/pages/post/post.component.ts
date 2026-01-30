import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/core/models/post/post.interface';
import { Comment } from 'src/app/core/models/comment/comment.interface';
import { PostService } from 'src/app/core/api/services/post/post.service';
import { CommentService } from 'src/app/core/api/services/comment/comment.service';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { PostSectionComponent } from 'src/app/components/sections/post/post-section.component';
import { CommentSectionComponent } from 'src/app/components/sections/comment/comment-section.component';
import { ButtonBackComponent } from 'src/app/components/elements/shared/button-back/button-back.component';

@Component({
    selector: 'app-post',
    standalone: true,
    imports: [
        CommonModule,
        HeaderComponent,
        PostSectionComponent,
        CommentSectionComponent,
        ButtonBackComponent
    ],
    styleUrl: './post.component.scss',
    template: `
        <app-header />
        <main class="post-container" *ngIf="post">
            <app-button-back />
            <app-post-section [post]="post" />
            <app-comment-section
                [comments]="comments"
                [(newComment)]="newComment"
                (submit)="submitComment()"
            />
        </main>
    `,
})
export class PostComponent implements OnInit {

  post!: Post;
  comments: Comment[] = [];
  newComment = '';

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    const postId = this.route.snapshot.paramMap.get('id');
    if (!postId) return;

    this.loadPost(postId);
    this.loadComments(postId);
  }

  loadPost(postId: string): void {
    this.postService.getOneById(postId).subscribe(post => {
      this.post = post;
    });
  }

  loadComments(postId: string): void {
    this.commentService.getAllByPost(postId).subscribe(comments => {
      this.comments = comments;
    });
  }

  submitComment(): void {
    console.log('submit')
  }

  back() {
    window.history.back();
  }
}
