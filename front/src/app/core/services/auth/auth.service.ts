import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { SessionInfo } from '../../models/auth/sessionInfo.interface';
import { LoginRequest } from '../../models/auth/loginRequest.interface';
import { RegisterRequest } from '../../models/auth/registerRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private pathService =  `${environment.baseUrl}/auth`;

  constructor(private httpClient: HttpClient) { }

  public login(loginRequest: LoginRequest): Observable<SessionInfo> {
    return this.httpClient.post<SessionInfo>(`${this.pathService}/login`, loginRequest);
  }

  public register(registerRequest: RegisterRequest): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/register`, registerRequest);
  }
}