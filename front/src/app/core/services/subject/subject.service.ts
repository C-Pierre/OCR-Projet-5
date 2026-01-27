import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Subject } from '../../models/subject/subject.interface';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {

  private pathService =  `${environment.baseUrl}/subjects`;

  constructor(private httpClient: HttpClient) {}

  public all(): Observable<Subject[]> {
    return this.httpClient.get<Subject[]>(this.pathService);
  }
}
