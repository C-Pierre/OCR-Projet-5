import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Component, HostListener, ElementRef } from '@angular/core';
import { SessionService } from 'src/app/core/api/services/auth/session.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, MatToolbarModule, MatButtonModule, RouterModule],
  styleUrls: ['./header.component.scss'],
  template: `
    <mat-toolbar class="header">
      <div class="header-left">
        <a routerLink="/"><img src="/assets/images/logo_p6.png" alt="logo" /></a>
      </div>

      <div class="header-right">
        <button class="burger" mat-icon-button (click)="toggleBurger()">
          <img src="/assets/icons/icon_menu.png" alt="menu" />
        </button>

        <nav class="menu" [class.mobile-open]="burgerOpen">

          @if (isLogged$ | async) {
            <button *ngIf="isLogged$ | async" mat-button (click)="logout()">Déconnexion</button>
          } @else {
            <a mat-button routerLink="/">Accueil</a>
          }

          <a mat-button routerLink="/">Articles</a>
          <a mat-button routerLink="/themes">Thèmes</a>
          <a class="user" routerLink="/profil"><img src="/assets/icons/icon_user.png" alt="account" /></a>
        </nav>
      </div>
      <div class="overlay" *ngIf="burgerOpen" (click)="toggleBurger()"></div>
    </mat-toolbar>
  `
})
export class HeaderComponent {

  burgerOpen = false;
  isLogged$: Observable<boolean>;

  constructor(
    private elRef: ElementRef,
    private sessionService: SessionService
  ) {
    this.isLogged$ = this.sessionService.isLogged$;
  }

  toggleBurger(): void {
    this.burgerOpen = !this.burgerOpen;
  }

  logout(): void {
    this.sessionService.logOut();
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const clickedInside = this.elRef.nativeElement.contains(event.target);
    if (!clickedInside) {
      this.burgerOpen = false;
    }
  }
}
