import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Component, Input } from '@angular/core';

type ButtonType = 'button' | 'submit' | 'reset' | 'a';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule, RouterModule],
  styleUrls: ['./button.component.scss'],
  template: `
    <a *ngIf="isLink(); else buttonTpl"
       [routerLink]="routerLink"
       class="btn"
       [ngClass]="btnClass">
      {{ title ?? 'Button' }}
    </a>

    <ng-template #buttonTpl>
      <button
        class="btn"
        [ngClass]="btnClass"
        [attr.type]="type">
        {{ title ?? 'Button' }}
      </button>
    </ng-template>
  `
})
export class ButtonComponent {
  @Input() title?: string;
  @Input() type: ButtonType = 'button';
  @Input() routerLink?: string | any[];
  @Input() btnClass: string = 'primary';

  isLink(): boolean {
    return this.type === 'a';
  }
}
