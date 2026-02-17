import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';
import { SubscriptionComponent } from 'src/app/components/parts/shared/subscription/subscription.component';

@Component({
  selector: 'app-subscriptions',
  standalone: true,
  imports: [CommonModule, SubscriptionComponent],
  styleUrl: './subscriptions.component.scss',
  template: `
    <section class="subscriptions-section">
      <h2>Abonnements</h2>

      <div class="subscriptions-list">
        @for (sub of subscriptions; track sub.id) {
          <app-subscription
            [name]="sub.name"
            [description]="sub.description"
            [subscribed]="true"
            (unsubscribe)="unsubscribe.emit(sub.id)"
          >
          </app-subscription>
        }
      </div>
    </section>
  `,
})
export class SubscriptionsComponent {
  @Input() subscriptions: Subscription[] = [];
  @Output() unsubscribe = new EventEmitter<number>();

  trackById(index: number, sub: Subscription): number {
    return sub.id;
  }
}
