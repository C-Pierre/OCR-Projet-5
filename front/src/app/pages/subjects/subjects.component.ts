import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { Observable, of, combineLatest, BehaviorSubject } from 'rxjs';
import { map, switchMap, startWith, catchError } from 'rxjs/operators';
import { Subject } from 'src/app/core/models/subject/subject.interface';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { SubjectService } from 'src/app/core/api/services/subject/subject.service';
import { HeaderComponent } from '../../components/parts/shared/header/header.component';
import { SubscriptionService } from 'src/app/core/api/services/subscription/subscription.service';
import { ConfirmModalComponent } from 'src/app/components/parts/shared/modal/confirm-modal.component';
import { SubscriptionComponent } from 'src/app/components/parts/shared/subscription/subscription.component';
import { UserSubscriptionService } from 'src/app/core/services/subscription/user-subscription.service';

@Component({
  selector: 'app-subjects',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    MatButtonModule,
    SubscriptionComponent,
    ConfirmModalComponent
  ],
  styleUrls: ['./subjects.component.scss'],
  templateUrl: './subjects.component.html'
})
export class SubjectsComponent implements OnInit {
  private subjectService = inject(SubjectService);
  private sessionService = inject(SessionService);
  private subscriptionService = inject(SubscriptionService);
  private refreshSubscriptions$ = new BehaviorSubject<void>(undefined);

  public userSubscriptionService = inject(UserSubscriptionService);

  themes$!: Observable<(Subject & { subscribed: boolean })[]>;

  ngOnInit(): void {
    this.themes$ = this.sessionService.isLogged$.pipe(
      startWith(this.sessionService.hasSession()),
      switchMap(isLogged => {
        if (!isLogged) {
          return this.subjectService.all().pipe(
            map(subjects =>
              subjects.map(subject => ({ ...subject, subscribed: false }))
            ),
            catchError(() => of([]))
          );
        }

        const userId = this.sessionService.sessionInformation?.id;
        if (!userId) return of([]);

        return combineLatest([
          this.subjectService.allForUser(userId.toString()),
          this.refreshSubscriptions$.pipe(
            switchMap(() =>
              this.subscriptionService.getAllForUser(userId.toString())
            )
          )
        ]).pipe(
          map(([subjects, subscriptions]) => {
            const subscribedIds = new Set(subscriptions.map(sub => sub.id));
            return subjects.map(subject => ({
              ...subject,
              subscribed: subscribedIds.has(subject.id)
            }));
          }),
          catchError(err => {
            console.error('Erreur chargement sujets', err);
            return of([]);
          })
        );
      })
    );
  }

  subscribe(subjectId: number): void {
    const userId = this.sessionService.sessionInformation?.id;
    if (!userId) {
      alert('Vous devez être connecté pour vous abonner.');
      return;
    }

    this.userSubscriptionService.openSubscribeModal(subjectId);
  }

  trackById(index: number, item: Subject) {
    return item.id;
  }
}