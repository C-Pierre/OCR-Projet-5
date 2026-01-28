import { firstValueFrom } from "rxjs";
import {Injectable} from "@angular/core";
import { Router } from "@angular/router";
import { SessionService } from "../core/services/auth/session.service";

@Injectable({ providedIn: 'root' })
export class UnauthGuard  {

  constructor(
    private router: Router,
    private sessionService: SessionService,
  ) {
  }

  public async canActivate(): Promise<boolean> {
    const logged = await firstValueFrom(this.sessionService.isLogged$);

    if (logged) {
      this.router.navigate(['/']);
      return false;
    }

    return true;
  }
}
