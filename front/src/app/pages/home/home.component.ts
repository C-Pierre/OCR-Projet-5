import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  styleUrls: ['./home.component.scss'],
  template: `
    <div class="center">
      <img src="/assets/logo_p6.png" alt="logo" />
      <div class="buttons">
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
      </div>
    </div>
  `,
})
export class HomeComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  start() {
    alert('Commencez par lire le README et à vous de jouer !');
  }
}
