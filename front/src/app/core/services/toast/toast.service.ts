import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Toast {
  message: string;
  type: 'error' | 'success';
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private toastSubject = new BehaviorSubject<Toast | null>(null);
  toast$ = this.toastSubject.asObservable();

  error(message: string) {
    this.show({ message, type: 'error' });
  }

  success(message: string) {
    this.show({ message, type: 'success' });
  }

  private show(toast: Toast) {
    this.toastSubject.next(toast);
    setTimeout(() => this.toastSubject.next(null), 3000);
  }
}
