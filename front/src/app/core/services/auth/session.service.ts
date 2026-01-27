import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { SessionInfo } from '../../models/auth/sessionInfo.interface';

@Injectable({ providedIn: 'root' })
export class SessionService {

  private readonly STORAGE_KEY = 'session';

  private isLoggedSubject = new BehaviorSubject<boolean>(this.hasSession());
  public readonly isLogged$ = this.isLoggedSubject.asObservable();

  constructor(private router: Router) {}

  public get sessionInformation(): SessionInfo | undefined {
    const session = localStorage.getItem(this.STORAGE_KEY);
    return session ? JSON.parse(session) : undefined;
  }

  public logIn(session: SessionInfo): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(session));
    this.isLoggedSubject.next(true);
  }

  public logOut(): void {
    localStorage.removeItem(this.STORAGE_KEY);
    this.isLoggedSubject.next(false);
    this.router.navigate(['/']);
  }

  private hasSession(): boolean {
    return !!localStorage.getItem(this.STORAGE_KEY);
  }
}
