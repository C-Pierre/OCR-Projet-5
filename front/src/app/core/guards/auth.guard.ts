import {Injectable} from "@angular/core";
import { Router } from "@angular/router";
import { SessionService } from "../api/services/auth/session.service";

@Injectable({ providedIn: 'root' })
export class AuthGuard  {

  constructor(private router: Router, private sessionService: SessionService) {}

  public async canActivate(): Promise<boolean> {
    if (!this.sessionService.hasSession()) {
      this.router.navigate(['login']);
      return false;
    }
    return true;
  }
}