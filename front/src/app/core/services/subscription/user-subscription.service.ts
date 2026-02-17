import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';
import { SubscriptionService } from 'src/app/core/api/services/subscription/subscription.service';
import { SubscriptionRequest } from 'src/app/core/models/subscription/request/subscriptionRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class UserSubscriptionService {
  private subscriptionsSubject = new BehaviorSubject<Subscription[]>([]);
  subscriptions$ = this.subscriptionsSubject.asObservable();

  private showSubscribeModalSubject = new BehaviorSubject<boolean>(false);
  showSubscribeModal$ = this.showSubscribeModalSubject.asObservable();

  private showUnsubscribeModalSubject = new BehaviorSubject<boolean>(false);
  showUnsubscribeModal$ = this.showUnsubscribeModalSubject.asObservable();

  private selectedSubscribeSubjectIdSubject = new BehaviorSubject<number | null>(null);
  private selectedUnsubscribeSubjectIdSubject = new BehaviorSubject<number | null>(null);

  constructor(
    private subscriptionService: SubscriptionService,
    private toastService: ToastService,
    private sessionService: SessionService
  ) {}

  setSubscriptions(subscriptions: Subscription[]): void {
    this.subscriptionsSubject.next(subscriptions);
  }

  openSubscribeModal(subjectId: number): void {
    this.selectedSubscribeSubjectIdSubject.next(subjectId);
    this.showSubscribeModalSubject.next(true);
  }

  cancelSubscribe(): void {
    this.selectedSubscribeSubjectIdSubject.next(null);
    this.showSubscribeModalSubject.next(false);
  }

  openUnsubscribeModal(subjectId: number): void {
    this.selectedUnsubscribeSubjectIdSubject.next(subjectId);
    this.showUnsubscribeModalSubject.next(true);
  }

  cancelUnsubscribe(): void {
    this.selectedUnsubscribeSubjectIdSubject.next(null);
    this.showUnsubscribeModalSubject.next(false);
  }

  confirmSubscribe(): void {
    const subjectId = this.selectedSubscribeSubjectIdSubject.value;
    const userId = this.sessionService.sessionInformation?.id;
    if (!subjectId || !userId) return;

    const request: SubscriptionRequest = { userId, subjectId };

    this.subscriptionService.subscribeUser(request).subscribe({
      next: () => {
        const newSub: Subscription = {
          id: subjectId,
          name: `Sujet ${subjectId}`,
          description: 'Description générée côté client'
        };

        const updated = [...this.subscriptionsSubject.value, newSub];
        this.subscriptionsSubject.next(updated);

        this.toastService.success('Abonnement effectué avec succès.');
        this.cancelSubscribe();
      },
      error: () => this.toastService.error('Erreur lors de l’abonnement.')
    });
  }

  confirmUnsubscribe(): void {
    const subjectId = this.selectedUnsubscribeSubjectIdSubject.value;
    const userId = this.sessionService.sessionInformation?.id;
    if (!subjectId || !userId) return;

    const request: SubscriptionRequest = { userId, subjectId };

    this.subscriptionService.unSubscribeUser(request).subscribe({
      next: () => {
        const updated = this.subscriptionsSubject.value.filter(sub => sub.id !== subjectId);
        this.subscriptionsSubject.next(updated);

        this.toastService.success('Désabonnement effectué avec succès.');
        this.cancelUnsubscribe();
      },
      error: () => this.toastService.error('Erreur lors du désabonnement.')
    });
  }
}
