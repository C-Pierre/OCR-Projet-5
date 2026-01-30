import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { Post } from 'src/app/core/models/post/post.interface';
import { PostService } from 'src/app/core/api/services/post/post.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatSelectModule,
    FormsModule,
    HeaderComponent,
    ButtonComponent,
    RouterModule
],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.scss'
})
export class FeedComponent implements OnInit {
  posts: Post[] = [];
  sortKey: 'title' | 'date' | 'author' = 'date';

  constructor(
    private router: Router,
    private feedService: PostService,
    private sessionService: SessionService
  ) {}

  ngOnInit(): void {
    const userId = this.sessionService.sessionInformation?.id;
    if (!userId) {
      this.router.navigate(['/']);
      return
    }
    this.feedService.getAllForUser(userId.toString()).subscribe(data => {
      this.posts = data;
      this.sortPosts();
    });
  }

  sortPosts(): void {
    this.posts = [...this.posts].sort((a, b) => {
      if (this.sortKey === 'date') {
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      } else if (this.sortKey === 'title') {
        return a.title.localeCompare(b.title);
      } else if (this.sortKey === 'author') {
        return a.authorUsername.localeCompare(b.authorUsername);
      }
      return 0;
    });
  }
}
