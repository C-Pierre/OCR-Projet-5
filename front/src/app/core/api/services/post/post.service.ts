import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Post } from '../../../models/post/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private pathService =  `${environment.baseUrl}/posts`;

  constructor(private httpClient: HttpClient) {}

  getAll(): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.pathService);
  }

  public getAllForUser(userId: string): Observable<Post[]> {
    return this.httpClient.get<Post[]>(`${this.pathService}/user/${userId}`);
  }
}
