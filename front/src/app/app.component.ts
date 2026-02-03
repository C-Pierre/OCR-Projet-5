import { Observable } from 'rxjs';
import { Component } from '@angular/core';
import { SessionService } from './core/api/services/auth/session.service';
import { RouterOutlet } from '@angular/router';
import { ToastComponent } from './components/elements/shared/toast/toast.component';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastComponent],
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
