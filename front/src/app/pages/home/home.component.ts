import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  styleUrls: ['./home.component.scss'],
  template: `
    <div class="center">
      <img src="/assets/images/logo_p6.png" alt="logo" />
      <div class="buttons">
        <app-button
          *ngIf="isLogged"
          title="Accéder"
          type="a"
          [routerLink]="'/themes'"
          btnClass="primary">
        </app-button>

        <ng-container *ngIf="!isLogged">
          <app-button
            title="Se connecter"
            type="a"
            [routerLink]="'/login'"
            btnClass="secondary">
          </app-button>

          <app-button
            title="S'inscrire"
            type="a"
            [routerLink]="'/signin'"
            btnClass="secondary">
          </app-button>
        </ng-container>
    </div>
  `,
})
export class HomeComponent implements OnInit {
  isLogged = false;

  constructor(private sessionService: SessionService) {}

  ngOnInit(): void {
    this.sessionService.isLogged$.subscribe(status => {
      this.isLogged = status;
    });

    this.isLogged = this.sessionService.hasSession();
  }
}
