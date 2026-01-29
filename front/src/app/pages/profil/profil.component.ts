import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { User } from 'src/app/core/models/user/user.interface';
import { filter, switchMap, catchError, tap } from 'rxjs/operators';
import { BehaviorSubject, Observable, of, firstValueFrom } from 'rxjs';
import { UserService } from 'src/app/core/api/services/user/user.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';
import { ProfilFormComponent } from 'src/app/components/parts/profil/form/profil-form.component';
import { ConfirmModalComponent } from 'src/app/components/parts/shared/modal/confirm-modal.component';
import { SubscriptionService } from 'src/app/core/api/services/subscription/subscription.service';
import { UserSubscriptionService } from 'src/app/core/services/subscription/user-subscription.service';
import { SubscriptionsComponent } from 'src/app/components/sections/profil/subscription/subscriptions.component';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [
    CommonModule,
    ButtonComponent,
    HeaderComponent,
    ProfilFormComponent,
    SubscriptionsComponent,
    ConfirmModalComponent
  ],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']
})
export class ProfilComponent implements OnInit {
  private sessionService = inject(SessionService);
  private userService = inject(UserService);
  private subscriptionService = inject(SubscriptionService);
  private userSubscriptionService = inject(UserSubscriptionService);

  private userSubject = new BehaviorSubject<User | null>(null);
  user$: Observable<User | null> = this.userSubject.asObservable();

  subscriptions$ = this.userSubscriptionService.subscriptions$;
  showUnsubscribeModal$ = this.userSubscriptionService.showUnsubscribeModal$;

  ngOnInit(): void {
    this.sessionService.isLogged$
      .pipe(
        filter(Boolean),
        switchMap(() => {
          const session = this.sessionService.sessionInformation!;
          return this.userService.getById(session.id.toString());
        }),
        tap(user => this.userSubject.next(user)),
        switchMap(user => {
          if (!user) {
            return of([]);
          }
          return this.subscriptionService.getAllForUser(user.id.toString());
        }),
        catchError(err => {
          console.error(
            'Erreur lors du chargement du profil ou des subscriptions',
            err
          );
          return of([]);
        })
      )
      .subscribe(subscriptions => {
        this.userSubscriptionService.setSubscriptions(subscriptions);
      });
  }

  async onSaveProfile(updatedUser: Partial<User>): Promise<void> {
    const userId = this.sessionService.sessionInformation?.id;
    if (!userId) return;

    try {
      const user = await firstValueFrom(
        this.userService.update(userId.toString(), updatedUser as User)
      );

      this.userSubject.next(user);
    } catch (err) {
      console.error('Erreur lors de la mise à jour du profil', err);
    }
  }

  openUnsubscribeModal(subjectId: number): void {
    this.userSubscriptionService.openUnsubscribeModal(subjectId);
  }

  cancelUnsubscribe(): void {
    this.userSubscriptionService.cancelUnsubscribe();
  }

  confirmUnsubscribe(): void {
    this.userSubscriptionService.confirmUnsubscribe();
  }

  unsubscribe(subjectId: number): void {
    this.userSubscriptionService.unsubscribeWithConfirm(subjectId);
  }
}
