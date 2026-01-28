import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { HeaderComponent } from 'src/app/components/parts/shared/header/header.component';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';
import { UserService } from 'src/app/core/services/user/user.service';
import { SessionService } from 'src/app/core/services/auth/session.service';
import { firstValueFrom, Observable, of, BehaviorSubject } from 'rxjs';
import { filter, switchMap, catchError, tap } from 'rxjs/operators';
import { User } from 'src/app/core/models/user/user.interface';
import { ProfilFormComponent } from 'src/app/components/parts/profil/form/profil-form.component';
import { SubscriptionsComponent } from 'src/app/components/sections/profil/subscription/subscriptions.component';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [
    CommonModule,
    ButtonComponent,
    HeaderComponent,
    ProfilFormComponent,
    SubscriptionsComponent
  ],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']
})
export class ProfilComponent implements OnInit {
  private sessionService = inject(SessionService);
  private userService = inject(UserService);

  private userSubject = new BehaviorSubject<User | null>(null);
  public user$: Observable<User | null> = this.userSubject.asObservable();

  public subscriptions: Subscription[] = [
    { id: 1, title: 'Titre du thème 1', description: 'Description: lorem ipsum...' },
    { id: 2, title: 'Titre du thème 2', description: 'Description: lorem ipsum...' }
  ];

  ngOnInit(): void {
    this.sessionService.isLogged$.pipe(
      filter(Boolean),
      switchMap(() => {
        const session = this.sessionService.sessionInformation!;
        return this.userService.getById(`${session.id}`);
      }),
      catchError(err => {
        console.error('Impossible de charger l’utilisateur', err);
        return of(null);
      }),
      tap(user => this.userSubject.next(user))
    ).subscribe();
  }

  async onSaveProfile(updatedUser: Partial<User>): Promise<void> {
    const userId = this.sessionService.sessionInformation?.id;
    if (!userId) return;

    try {
      const user = await firstValueFrom(
        this.userService.update(userId.toString(), updatedUser as User)
      );
      console.log('Profil mis à jour', user);

      // 🔹 On push la nouvelle valeur pour rafraîchir le formulaire et la vue
      this.userSubject.next(user);

    } catch (err) {
      console.error('Erreur lors de la mise à jour du profil', err);
    }
  }

  unsubscribe(subId: number): void {
    console.log('Se désabonner de', subId);
  }
}
