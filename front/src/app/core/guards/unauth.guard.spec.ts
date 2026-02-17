import { expect } from '@jest/globals';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { UnauthGuard } from './unauth.guard';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SessionService } from '../api/services/auth/session.service';

@Component({ standalone: true, template: '<p>dummy</p>' })
class DummyComponent {}

describe('UnauthGuard', () => {
  let guard: UnauthGuard;
  let router: Router;
  let location: Location;
  let isLogged$: BehaviorSubject<boolean>;
  let mockSessionService: any;

  beforeEach(async () => {
    isLogged$ = new BehaviorSubject<boolean>(false);
    
    mockSessionService = {
      isLogged$,
      hasSession: jest.fn(() => isLogged$.value),
      sessionInformation: null
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'unauth', component: DummyComponent, canActivate: [UnauthGuard] },
          { path: '', component: DummyComponent }
        ]),
        DummyComponent
      ],
      providers: [
        UnauthGuard,
        { provide: SessionService, useValue: mockSessionService }
      ]
    }).compileComponents();

    guard = TestBed.inject(UnauthGuard);
    router = TestBed.inject(Router);
    location = TestBed.inject(Location);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should block access and redirect to / if logged', async () => {
    isLogged$.next(true);
    mockSessionService.hasSession.mockReturnValue(true);
    const spy = jest.spyOn(router, 'navigate');

    const result = await guard.canActivate();

    expect(result).toBe(false);
    expect(spy).toHaveBeenCalledWith(['/']);
  });

  it('should allow access if not logged', async () => {
    isLogged$.next(false);
    mockSessionService.hasSession.mockReturnValue(false);
    const spy = jest.spyOn(router, 'navigate');

    const result = await guard.canActivate();

    expect(result).toBe(true);
    expect(spy).not.toHaveBeenCalled();
  });

  it('should redirect to / if user is logged and tries to access unauth route', async () => {
    isLogged$.next(true);
    mockSessionService.hasSession.mockReturnValue(true);
    await router.navigate(['/unauth']);
    expect(location.path()).toBe('/');
  });

  it('should allow navigation to unauth route when user is not logged', async () => {
    isLogged$.next(false);
    mockSessionService.hasSession.mockReturnValue(false);
    await router.navigate(['/unauth']);
    expect(location.path()).toBe('/unauth');
  });

  describe('UnauthGuard – Integration tests (Router)', () => {
    beforeEach(async () => {
      router.initialNavigation();
    });

    it('should prevent navigation and stay on "/" when logged user navigates to /unauth', async () => {
      isLogged$.next(true);
      mockSessionService.hasSession.mockReturnValue(true);

      await router.navigateByUrl('/unauth');

      expect(location.path()).toBe('/');
    });

    it('should allow navigation to /unauth when user is not logged', async () => {
      isLogged$.next(false);
      mockSessionService.hasSession.mockReturnValue(false);

      await router.navigateByUrl('/unauth');

      expect(location.path()).toBe('/unauth');
    });

    it('should allow returning to unauth page after logout', async () => {
      isLogged$.next(true);
      mockSessionService.hasSession.mockReturnValue(true);
      await router.navigateByUrl('/unauth');
      expect(location.path()).toBe('/');

      isLogged$.next(false);
      mockSessionService.hasSession.mockReturnValue(false);
      await router.navigateByUrl('/unauth');

      expect(location.path()).toBe('/unauth');
    });
  });
});