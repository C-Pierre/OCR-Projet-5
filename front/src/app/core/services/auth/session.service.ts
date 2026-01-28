import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { SessionInfo } from '../../models/auth/sessionInfo.interface';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly STORAGE_KEY = 'session';
  private _session: SessionInfo | null = null;
  private isLoggedSubject = new BehaviorSubject<boolean>(false);
  public readonly isLogged$ = this.isLoggedSubject.asObservable();

  constructor() {
    this.loadSessionFromStorage();
  }

  private loadSessionFromStorage() {
    const session = localStorage.getItem(this.STORAGE_KEY);
    if (session) {
      this._session = JSON.parse(session);
      this.isLoggedSubject.next(true);
    }
  }

  public get sessionInformation(): SessionInfo | null {
    return this._session;
  }

  public logIn(session: SessionInfo) {
    this._session = session;
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(session));
    this.isLoggedSubject.next(true);
  }

  public logOut() {
    this._session = null;
    localStorage.removeItem(this.STORAGE_KEY);
    this.isLoggedSubject.next(false);
  }

  public hasSession(): boolean {
    return !!this._session;
  }
}
