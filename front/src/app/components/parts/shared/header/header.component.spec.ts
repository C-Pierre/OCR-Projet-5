import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { SessionService } from 'src/app/core/api/services/auth/session.service';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let router: Router;

  let isLoggedSubject: BehaviorSubject<boolean>;
  let sessionServiceMock: Partial<SessionService>;

  beforeEach(async () => {
    isLoggedSubject = new BehaviorSubject<boolean>(false);

    sessionServiceMock = {
      isLogged$: isLoggedSubject.asObservable(),
      logOut: jest.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HeaderComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);

    (component as any).elRef = { nativeElement: fixture.nativeElement };

    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('devrait refléter isLogged$ observable', (done) => {
    component.isLogged$.subscribe((logged) => {
      expect(logged).toBe(true);
      done();
    });

    isLoggedSubject.next(true);
  });

  it('devrait toggler burgerOpen quand toggleBurger est appelé', () => {
    expect(component.burgerOpen).toBe(false);
    component.toggleBurger();
    expect(component.burgerOpen).toBe(true);
    component.toggleBurger();
    expect(component.burgerOpen).toBe(false);
  });

  it('devrait appeler logOut et naviguer vers "/"', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.logout();

    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
  });

  it('devrait fermer le menu burger quand on clique en dehors', () => {
    component.burgerOpen = true;

    const event = new MouseEvent('click', { bubbles: true });
    Object.defineProperty(event, 'target', { value: document.body });

    component.onDocumentClick(event);
    expect(component.burgerOpen).toBe(false);
  });

  it('ne devrait pas fermer le menu burger quand on clique à l’intérieur', () => {
    component.burgerOpen = true;

    const child = document.createElement('div');
    (component as any).elRef.nativeElement.appendChild(child);

    const event = new MouseEvent('click', { bubbles: true });
    Object.defineProperty(event, 'target', { value: child });

    component.onDocumentClick(event);
    expect(component.burgerOpen).toBe(true);
  });
});
