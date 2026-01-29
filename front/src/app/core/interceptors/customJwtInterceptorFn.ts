import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';

@Injectable()
export class CustomJwtInterceptor implements HttpInterceptor {

  constructor(private sessionService: SessionService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {

    const token = this.sessionService.sessionInformation?.token;

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(request);
  }
}
