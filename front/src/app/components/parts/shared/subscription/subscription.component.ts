import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

@Component({
  selector: 'app-subscription',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  styleUrl: './subscription.component.scss',
  template: `
    <div class="subscription">
      <h3>{{ name }}</h3>
      <p>{{ description }}</p>

      @if (!subscribed) {
        <app-button
          title="S'abonner"
          class="secondary"
          (click)="subscribe.emit()">
        </app-button>
      } @else {
        <app-button
          title="Se désabonner"
          class="primary"
          [disabled]="disabled"
          (click)="unsubscribe.emit()">
        </app-button>
      }
    </div>
  `,
})
export class SubscriptionComponent {
  @Input({ required: true }) name!: string;
  @Input({ required: true }) description!: string;
  @Input() subscribed = false;
  @Input() disabled = false;

  @Output() subscribe = new EventEmitter<void>();
  @Output() unsubscribe = new EventEmitter<void>();
}
