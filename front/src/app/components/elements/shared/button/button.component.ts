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
    @if (isLink()) {
      <a
        [routerLink]="!disabled ? routerLink : null"
        class="btn"
        [ngClass]="btnClass"
        [attr.aria-disabled]="disabled"
        [class.disabled]="disabled"
        (click)="disabled && $event.preventDefault()"
      >
        {{ title ?? 'Button' }}
      </a>
    } @else {
      <button
        class="btn"
        [ngClass]="!disabled ? btnClass : ''"
        [type]="type"
        [disabled]="disabled"
      >
        {{ title ?? 'Button' }}
      </button>
    }
  `
})
export class ButtonComponent {
  @Input() title?: string;
  @Input() type: ButtonType = 'button';
  @Input() routerLink?: string | (string | number)[];
  @Input() btnClass: string = 'primary';
  @Input() disabled: boolean = false;

  isLink(): boolean {
    return this.type === 'a';
  }
}
