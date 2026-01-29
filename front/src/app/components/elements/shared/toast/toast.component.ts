import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from 'src/app/core/services/toast/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="toast$ | async as toast"
         class="toast"
         [class.error]="toast.type === 'error'"
         [class.success]="toast.type === 'success'">
      {{ toast.message }}
    </div>
  `,
  styleUrls: ['./toast.component.scss']
})
export class ToastComponent {
  toast$ = this.toastService.toast$;

  constructor(private toastService: ToastService) {}
}
