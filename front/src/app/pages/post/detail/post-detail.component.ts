import { switchMap } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/core/models/post/post.interface';
import { Comment } from 'src/app/core/models/comment/comment.interface';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { PostService } from 'src/app/core/api/services/post/post.service';
import { UserService } from 'src/app/core/api/services/user/user.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { CommentService } from 'src/app/core/api/services/comment/comment.service';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { CommentRequest } from 'src/app/core/models/comment/request/commentRequest.interface';
import { CommentSectionComponent } from 'src/app/components/sections/comment/comment-section.component';
import { ButtonBackComponent } from 'src/app/components/elements/shared/button-back/button-back.component';
import { PostDetailSectionComponent } from 'src/app/components/sections/post/detail/post-detail-section.component';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    PostDetailSectionComponent,
    CommentSectionComponent,
    ButtonBackComponent
  ],
  styleUrl: './post-detail.component.scss',
  template: `
    <app-header />

    @if (post) {
      <main class="post-container">
        <app-button-back />
        <app-post-detail-section [post]="post" />
        <app-comment-section
          [comments]="comments"
          [(newComment)]="newComment"
          (submitComment)="submitComment()"
        />
      </main>
    }
  `,
})
export class PostDetailComponent implements OnInit {

  post!: Post;
  comments: Comment[] = [];
  newComment = '';
  userId!: number;
  authorUsername = '';

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService,
    private userService: UserService,
    private sessionService: SessionService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    const postId = this.route?.snapshot?.paramMap.get('id');
    if (!postId) return;

    this.postService.getOneById(postId).pipe(
      switchMap(post => {
        this.post = post;
        return this.commentService.getAllByPost(postId);
      })
    ).subscribe({
      next: comments => {
        this.comments = comments;
      },
      error: () => this.toastService.error("Erreur lors du chargement du post ou des commentaires.")
    });

    const session = this.sessionService.sessionInformation;
    if (session?.id) {
      this.userService.getById(session.id.toString()).subscribe({
        next: currentUser => {
          if (currentUser) {
            this.userId = currentUser.id;
            this.authorUsername = currentUser.userName;
          } else {
            this.toastService.error("Utilisateur non connecté.")
          }
        },
        error: () => this.toastService.error("Erreur lors de la récupération de l'utilisateur.")
      });
    } else {
      this.toastService.error('Aucune session trouvée.')
    }
  }

  submitComment(): void {
    if (!this.newComment.trim()) return;

    const comment: CommentRequest = {
      content: this.newComment,
      postId: this.post.id,
      userId: this.userId
    };

    this.commentService.create(comment).subscribe({
      next: created => {
        this.comments.push(created);
        this.newComment = '';
        this.toastService.success('Commentaire ajouté avec succès.');
      },
      error: () => this.toastService.error('Erreur lors de la création du commentaire.')
    });
  }

  back(): void {
    window.history.back();
  }
}
