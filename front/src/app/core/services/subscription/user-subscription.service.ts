import { BehaviorSubject } from 'rxjs';
import { Injectable } from '@angular/core';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';
import { SubscriptionService } from 'src/app/core/api/services/subscription/subscription.service';

@Injectable({
  providedIn: 'root',
})
export class UserSubscriptionService {
  private subscriptionsSubject = new BehaviorSubject<Subscription[]>([]);
  subscriptions$ = this.subscriptionsSubject.asObservable();

  private showUnsubscribeModalSubject = new BehaviorSubject<boolean>(false);
  showUnsubscribeModal$ = this.showUnsubscribeModalSubject.asObservable();

  private selectedSubjectIdSubject = new BehaviorSubject<number | null>(null);

  private showSubscribeModalSubject = new BehaviorSubject<boolean>(false);
  showSubscribeModal$ = this.showSubscribeModalSubject.asObservable();

  private selectedSubscribeSubjectIdSubject = new BehaviorSubject<number | null>(null);

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
    this.selectedSubjectIdSubject.next(subjectId);
    this.showUnsubscribeModalSubject.next(true);
  }

  cancelUnsubscribe(): void {
    this.selectedSubjectIdSubject.next(null);
    this.showUnsubscribeModalSubject.next(false);
  }

  confirmSubscribe(): void {
    const subjectId = this.selectedSubscribeSubjectIdSubject.value;
    const userId = this.sessionService.sessionInformation?.id;
    if (!subjectId || !userId) return;

    this.subscriptionService
      .subscribeUser(userId.toString(), subjectId.toString())
      .subscribe({
        next: () => {
          const updated = [...this.subscriptionsSubject.value];
          updated.push({ id: subjectId, name: 'Sujet X', description: '...' } as any);
          this.subscriptionsSubject.next(updated);

          this.toastService.success('Abonnement effectué avec succès.');
          this.cancelSubscribe();
        },
        error: () => this.toastService.error('Erreur lors de l’abonnement.')
      });
  }

  confirmUnsubscribe(): void {
    const subjectId = this.selectedSubjectIdSubject.value;
    const userId = this.sessionService.sessionInformation?.id;

    if (!subjectId || !userId) return;

    this.subscriptionService
      .unSubscribeUser(userId.toString(), subjectId.toString())
      .subscribe({
        next: () => {
          const updatedSubscriptions = this.subscriptionsSubject.value.filter(
            sub => sub.id !== subjectId
          );

          this.subscriptionsSubject.next(updatedSubscriptions);
          this.toastService.success('Désabonnement effectué avec succès.');
          this.cancelUnsubscribe();
        },
        error: () => {
          this.toastService.error(
            'Une erreur est survenue lors du désabonnement.'
          );
        }
      });
  }

  unsubscribeWithConfirm(subjectId: number): void {
    const userId = this.sessionService.sessionInformation?.id;
    if (!userId) return;

    const confirmed = window.confirm(
      'Êtes-vous sûr de vouloir vous désabonner de ce sujet ?'
    );
    if (!confirmed) return;

    this.subscriptionService
      .unSubscribeUser(userId.toString(), subjectId.toString())
      .subscribe({
        next: () => {
          const updatedSubscriptions = this.subscriptionsSubject.value.filter(
            sub => sub.id !== subjectId
          );
          this.subscriptionsSubject.next(updatedSubscriptions);
          this.toastService.success('Désabonnement effectué.');
        },
        error: () => {
          this.toastService.error(
            'Une erreur est survenue lors du désabonnement.'
          );
        }
      });
  }
}
