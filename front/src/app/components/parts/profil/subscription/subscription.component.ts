import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';

@Component({
  selector: 'app-subscription',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  styleUrl: './subscription.component.scss',
  template: `
    <div class="subscription">
      <h3>{{ subscription.title }}</h3>
      <p>{{ subscription.description }}</p>
      <app-button
        title="Se désabonner"
        class="secondary"
        (click)="unsubscribe.emit(subscription.id)">
      </app-button>
    </div>
  `,
})
export class SubscriptionComponent {
  @Input() subscription!: Subscription;
  @Output() unsubscribe = new EventEmitter<number>();
}
