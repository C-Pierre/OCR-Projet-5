import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonComponent } from 'src/app/components/elements/shared/button/button.component';

@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  template: `
    <div class="overlay">
      <div class="modal">
        <h3>{{ title }}</h3>
        <p>{{ message }}</p>

        <div class="actions">
          <app-button label="Annuler" (click)="cancel.emit()" title="Annuler" btnClass="secondary"></app-button>
          <app-button label="Confirmer" variant="danger" (click)="confirm.emit()" title="Confirmer"></app-button>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./confirm-modal.component.scss']
})
export class ConfirmModalComponent {
  @Input() title = 'Confirmation';
  @Input() message = 'Es-tu sûr ?';

  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
}
