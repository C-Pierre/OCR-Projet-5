import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Post } from '../../../models/post/post.interface';
import { PostRequest } from 'src/app/core/models/post/request/postRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private pathService =  `${environment.baseUrl}/posts`;

  constructor(private httpClient: HttpClient) {}

  public create(post: PostRequest): Observable<PostRequest> {
    return this.httpClient.post<PostRequest>(this.pathService, post);
  }

  public getAllForUser(userId: string): Observable<Post[]> {
    return this.httpClient.get<Post[]>(`${this.pathService}/user/${userId}`);
  }

  public getOneById(postId: string): Observable<Post> {
    return this.httpClient.get<Post>(`${this.pathService}/${postId}`);
  }
}
