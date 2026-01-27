import { Observable } from 'rxjs';
import { Component } from '@angular/core';
import { SessionService } from './core/services/auth/session.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent {

  constructor(public sessionService: SessionService) {}

  get isLogged(): Observable<boolean> {
    return this.sessionService.isLogged$;
  }
}
