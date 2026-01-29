import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'src/app/core/models/subject/subject.interface';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {

  private pathService =  `${environment.baseUrl}/subjects`;

  constructor(private httpClient: HttpClient) {}

  public all(): Observable<Subject[]> {
    return this.httpClient.get<Subject[]>(this.pathService);
  }

  public allForUser(userId: string): Observable<Subject[]> {
    return this.httpClient.get<Subject[]>(`${this.pathService}/user/${userId}`);
  }
}
