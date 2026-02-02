import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
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
    return this.httpClient.post<SubscriptionRequest>(this.pathService, request);
  }

  public unSubscribeUser(request: SubscriptionRequest): Observable<SubscriptionRequest> {
    return this.httpClient.request<SubscriptionRequest>('DELETE', this.pathService, { body: request });
  }
}
