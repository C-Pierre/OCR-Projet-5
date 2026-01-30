import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';
import { SubscriptionRequest } from 'src/app/core/models/subscription/request/subscriptionRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

  private pathService = `${environment.baseUrl}/subscriptions`;

  constructor(private httpClient: HttpClient) {}

  public getAllForUser(userId: string): Observable<Subscription[]> {
    return this.httpClient.get<Subscription[]>(`${this.pathService}/user/${userId}`);
  }

  public subscribeUser(request: SubscriptionRequest): Observable<SubscriptionRequest> {
    const params = new HttpParams()
      .set('userId', request.userId)
      .set('subjectId', request.subjectId);

    return this.httpClient.post<SubscriptionRequest>(this.pathService, null, { params });
  }

  public unSubscribeUser(request: SubscriptionRequest): Observable<void> {
    const params = new HttpParams()
      .set('userId', request.userId)
      .set('subjectId', request.subjectId);

    return this.httpClient.delete<void>(this.pathService, { params });
  }
}
