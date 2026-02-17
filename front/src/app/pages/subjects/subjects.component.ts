import { CommonModule } from '@angular/common';
import { combineLatest, Observable, of } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { Component, inject, OnInit } from '@angular/core';
import { catchError, map, startWith, switchMap } from 'rxjs/operators';
import { Subject } from 'src/app/core/models/subject/subject.interface';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { SubjectService } from 'src/app/core/api/services/subject/subject.service';
import { HeaderComponent } from '../../components/parts/shared/header/header.component';
import { ConfirmModalComponent } from 'src/app/components/parts/shared/modal/confirm-modal.component';
import { UserSubscriptionService } from 'src/app/core/services/subscription/user-subscription.service';
import { SubscriptionComponent } from 'src/app/components/parts/shared/subscription/subscription.component';

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
  private toastService = inject(ToastService);
  private subjectService = inject(SubjectService);
  private sessionService = inject(SessionService);
  public userSubscriptionService = inject(UserSubscriptionService);

  themes$!: Observable<(Subject & { subscribed: boolean })[]>;

  ngOnInit(): void {
    const isLogged$ = this.sessionService.isLogged$.pipe(
      startWith(this.sessionService.hasSession())
    );

    this.themes$ = isLogged$.pipe(
      switchMap(isLogged => {
        const userId = this.sessionService.sessionInformation?.id?.toString();

        const subjects$ = isLogged && userId
          ? this.subjectService.allForUser(userId)
          : this.subjectService.all();

        return combineLatest([subjects$, this.userSubscriptionService.subscriptions$]).pipe(
          map(([subjects, subscriptions]) =>
            subjects.map(subject => ({
              ...subject,
              subscribed: !!subscriptions.find(sub => sub.id === subject.id) || !!(subject as Subject).subscribed
            }))
          ),
          catchError(() => {
            this.toastService.error("Erreur lors du chargement des thèmes.");
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
