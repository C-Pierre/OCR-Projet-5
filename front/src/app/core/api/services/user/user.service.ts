import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { User } from 'src/app/core/models/user/user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private pathService =  `${environment.baseUrl}/users`;

  constructor(private httpClient: HttpClient) { }

  public getById(id: string): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/${id}`);
  }

  public update(id: string, user: User): Observable<User> {
    return this.httpClient.put<User>(`${this.pathService}/${id}`,user);
  }
}
