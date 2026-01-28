import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';
import { SubscriptionComponent } from 'src/app/components/parts/profil/subscription/subscription.component';

@Component({
  selector: 'app-subscriptions',
  standalone: true,
  imports: [CommonModule, SubscriptionComponent],
  styleUrl: './subscriptions.component.scss',
  template: `
    <section class="subscriptions-section">
        <h2>Abonnements</h2>
        @if (subscriptions && subscriptions.length > 0) {
            <div>
                <app-subscription
                *ngFor="let sub of subscriptions"
                [subscription]="sub"
                (unsubscribe)="unsubscribe.emit($event)">
                </app-subscription>
            </div>
        }
    </section>
  `,
})
export class SubscriptionsComponent {
  @Input() subscriptions: Subscription[] = [];
  @Output() unsubscribe = new EventEmitter<number>();
}
