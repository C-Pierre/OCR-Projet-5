import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Component, HostListener, ElementRef } from '@angular/core';
import { SessionService } from 'src/app/core/services/auth/session.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, MatToolbarModule, MatButtonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  burgerOpen = false;

  constructor(
    private elRef: ElementRef,
    private sessionService: SessionService
  ) {}

  toggleBurger(): void {
    this.burgerOpen = !this.burgerOpen;
  }

  logout(): void {
    this.sessionService.logOut();
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const clickedInside = this.elRef.nativeElement.contains(event.target);
    if (!clickedInside) {
      this.burgerOpen = false;
    }
  }
}
