import { Observable } from 'rxjs';
import { Component } from '@angular/core';
import { SessionService } from './core/api/services/auth/session.service';
@Component({
  selector: 'app-root',
  template: `
    <router-outlet></router-outlet>
    <app-toast></app-toast>
  `
})
export class AppComponent {

  constructor(public sessionService: SessionService) {}

  get isLogged(): Observable<boolean> {
    return this.sessionService.isLogged$;
  }
}
