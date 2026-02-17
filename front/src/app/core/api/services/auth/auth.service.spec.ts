import { AuthService } from './auth.service';
import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { SessionInfo } from 'src/app/core/models/auth/sessionInfo.interface';
import { LoginRequest } from 'src/app/core/models/auth/request/loginRequest.interface';
import { RegisterRequest } from 'src/app/core/models/auth/request/registerRequest.interface';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.baseUrl}/auth`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('login', () => {
    it('should send POST request to /auth/login with credentials', () => {
      const loginRequest: LoginRequest = {
        identifier: 'test@example.com',
        password: 'password123'
      };

      const mockResponse: SessionInfo = {
        token: 'fake-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'testuser',
        email: 'Test@mail.com'
      };

      service.login(loginRequest).subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(response.token).toBe('fake-jwt-token');
        expect(response.username).toBe('testuser');
      });

      const req = httpMock.expectOne(`${baseUrl}/login`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(loginRequest);
      
      req.flush(mockResponse);
    });

    it('should handle login error', () => {
      const loginRequest: LoginRequest = {
        identifier: 'wrong@example.com',
        password: 'wrongpassword'
      };

      const mockError = {
        status: 401,
        statusText: 'Unauthorized'
      };

      service.login(loginRequest).subscribe({
        next: () => fail('should have failed with 401 error'),
        error: (error) => {
          expect(error.status).toBe(401);
          expect(error.statusText).toBe('Unauthorized');
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/login`);
      req.flush('Unauthorized', mockError);
    });

    it('should send request with correct headers', () => {
      const loginRequest: LoginRequest = {
        identifier: 'test@example.com',
        password: 'password123'
      };

      service.login(loginRequest).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/login`);
      expect(req.request.headers.has('Content-Type')).toBeFalsy();
      
      req.flush({});
    });
  });

  describe('register', () => {
    it('should send POST request to /auth/register with user data', () => {
      const registerRequest: RegisterRequest = {
        email: 'newuser@example.com',
        userName: 'New',
        password: 'password123'
      };

      service.register(registerRequest).subscribe(response => {
        expect(response).toBeUndefined();
      });

      const req = httpMock.expectOne(`${baseUrl}/register`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(registerRequest);
      
      req.flush(null);
    });

    it('should handle registration error when email already exists', () => {
      const registerRequest: RegisterRequest = {
        email: 'existing@example.com',
        userName: 'Test',
        password: 'password123'
      };

      const mockError = {
        status: 409,
        statusText: 'Conflict'
      };

      service.register(registerRequest).subscribe({
        next: () => fail('should have failed with 409 error'),
        error: (error) => {
          expect(error.status).toBe(409);
          expect(error.statusText).toBe('Conflict');
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/register`);
      req.flush('Email already exists', mockError);
    });

    it('should handle registration error when password is invalid', () => {
      const registerRequest: RegisterRequest = {
        email: 'test@example.com',
        userName: 'Test',
        password: '123'
      };

      const mockError = {
        status: 400,
        statusText: 'Bad Request'
      };

      service.register(registerRequest).subscribe({
        next: () => fail('should have failed with 400 error'),
        error: (error) => {
          expect(error.status).toBe(400);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/register`);
      req.flush('Invalid password', mockError);
    });

    it('should complete successfully with valid registration data', (done) => {
      const registerRequest: RegisterRequest = {
        email: 'valid@example.com',
        userName: 'Valid',
        password: 'ValidPassword123!'
      };

      service.register(registerRequest).subscribe({
        next: () => {
          expect(true).toBeTruthy();
        },
        error: () => fail('should not have errored'),
        complete: () => done()
      });

      const req = httpMock.expectOne(`${baseUrl}/register`);
      req.flush(null);
    });
  });

  describe('HTTP request validation', () => {
    it('should use correct base URL from environment', () => {
      const loginRequest: LoginRequest = {
        identifier: 'test@example.com',
        password: 'password123'
      };

      service.login(loginRequest).subscribe();

      const req = httpMock.expectOne(`${environment.baseUrl}/auth/login`);
      expect(req.request.url).toContain(environment.baseUrl);
      
      req.flush({});
    });

    it('should not send multiple requests for single login call', () => {
      const loginRequest: LoginRequest = {
        identifier: 'test@example.com',
        password: 'password123'
      };

      service.login(loginRequest).subscribe();

      const requests = httpMock.match(`${baseUrl}/login`);
      expect(requests.length).toBe(1);
      
      requests[0].flush({});
    });
  });
});