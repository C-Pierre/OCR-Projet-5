import { TestBed } from '@angular/core/testing';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { CustomJwtInterceptorFn } from './customJwtInterceptorFn';
import { Injector, runInInjectionContext } from '@angular/core';

describe('CustomJwtInterceptorFn (unit test)', () => {
  let sessionService: any;
  let injector: Injector;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: SessionService,
          useValue: { sessionInformation: { token: 'fake-jwt-token' } },
        },
      ],
    });

    sessionService = TestBed.inject(SessionService);
    injector = TestBed.inject(Injector);
  });

  it('should add Authorization header when token exists', () => {
    const reqClone = { clone: jest.fn().mockImplementation((x) => x) } as any;
    const next = jest.fn().mockReturnValue('ok');

    const result = runInInjectionContext(injector, () =>
      CustomJwtInterceptorFn(reqClone, next)
    );

    expect(reqClone.clone).toHaveBeenCalledWith({
      setHeaders: { Authorization: `Bearer fake-jwt-token` },
    });
    expect(result).toBe('ok');
  });

  it('should NOT add Authorization header when token is missing', () => {
    Object.defineProperty(sessionService, 'sessionInformation', { get: () => null });

    const reqClone = { clone: jest.fn().mockImplementation((x) => x) } as any;
    const next = jest.fn().mockReturnValue('ok');

    const result = runInInjectionContext(injector, () =>
      CustomJwtInterceptorFn(reqClone, next)
    );

    expect(reqClone.clone).not.toHaveBeenCalled();
    expect(result).toBe('ok');
  });
});
