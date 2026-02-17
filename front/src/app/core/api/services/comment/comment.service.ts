import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Comment } from 'src/app/core/models/comment/comment.interface';
import { CommentRequest } from 'src/app/core/models/comment/request/commentRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private pathService =  `${environment.baseUrl}/comments`;

  constructor(private httpClient: HttpClient) {}

  public getAllByPost(postId: string): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(`${this.pathService}/post/${postId}`);
  }

  public create(comment: CommentRequest): Observable<CommentRequest> {
    return this.httpClient.post<CommentRequest>(this.pathService, comment);
  }
}
