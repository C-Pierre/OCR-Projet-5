import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton } from '@angular/material/button';

@Component({
    selector: 'app-button-back',
    standalone: true,
    imports: [CommonModule, MatIcon, MatIconButton],
    template: `
        <button mat-icon-button (click)="back()">
            <mat-icon>arrow_back</mat-icon>
        </button>
    `,
    styles: `
        button {
            position: absolute; left: -3.5rem; top: 1.5rem;
        }
    `
})
export class ButtonBackComponent {

  back() {
    window.history.back();
  }
}
