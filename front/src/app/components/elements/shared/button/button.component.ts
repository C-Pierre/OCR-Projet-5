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
    <a
      *ngIf="isLink(); else buttonTpl"
      [routerLink]="!disabled ? routerLink : null"
      class="btn"
      [ngClass]="btnClass"
      [attr.aria-disabled]="disabled"
      [class.disabled]="disabled"
      (click)="disabled && $event.preventDefault()"
    >
      {{ title ?? 'Button' }}
    </a>

    <ng-template #buttonTpl>
      <button
        class="btn"
        [ngClass]="!disabled ? btnClass : ''"
        [type]="type"
        [disabled]="disabled"
      >
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
  @Input() disabled: boolean = false;

  isLink(): boolean {
    return this.type === 'a';
  }
}
