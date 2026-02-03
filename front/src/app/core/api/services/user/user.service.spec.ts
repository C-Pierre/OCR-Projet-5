import { TestBed } from '@angular/core/testing';
import { UserService } from './user.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { User } from 'src/app/core/models/user/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.baseUrl}/users`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        UserService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('Unit Tests - getById', () => {
    const userId = 123;

    const mockUser: User = {
        id: 123,
        userName: 'johndoe',
        email: 'john.doe@test.com',
        password: 'password',
        createdAt: new Date()
    };

    it('should send GET request to correct endpoint', () => {
      service.getById(userId.toString()).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      expect(req.request.method).toBe('GET');

      req.flush(mockUser);
    });

    it('should return user by id', () => {
      service.getById(userId.toString()).subscribe(user => {
        expect(user).toEqual(mockUser);
        expect(user.id).toBe(userId);
        expect(user.userName).toBe('johndoe');
      });

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      req.flush(mockUser);
    });

    it('should handle different user IDs', () => {
      const ids = ['1', '999', 'user-abc-123'];

      ids.forEach(id => {
        service.getById(id).subscribe();
        const req = httpMock.expectOne(`${baseUrl}/${id}`);
        expect(req.request.url).toContain(`/${id}`);
        req.flush(mockUser);
      });
    });

    it('should handle 404 error when user not found', () => {
      service.getById(userId.toString()).subscribe({
        next: () => fail('should have failed with 404'),
        error: error => {
          expect(error.status).toBe(404);
          expect(error.statusText).toBe('Not Found');
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      req.flush('User not found', { status: 404, statusText: 'Not Found' });
    });

    it('should handle unauthorized error (401)', () => {
      service.getById(userId.toString()).subscribe({
        next: () => fail('should have failed with 401'),
        error: error => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    });

    it('should handle server error (500)', () => {
      service.getById(userId.toString()).subscribe({
        next: () => fail('should have failed with 500'),
        error: error => {
          expect(error.status).toBe(500);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });

    it('should not send multiple requests for single call', () => {
      service.getById(userId.toString()).subscribe();

      const requests = httpMock.match(`${baseUrl}/${userId}`);
      expect(requests.length).toBe(1);

      requests[0].flush(mockUser);
    });
  });

  describe('Unit Tests - update', () => {
    const userId = 123;

    const updateRequest: User = {
        id: 123,
        userName: 'johndoe',
        email: 'john.updated@test.com',
        password: 'password',
        createdAt: new Date()
    };

    it('should send PUT request to correct endpoint', () => {
      service.update(userId.toString(), updateRequest).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      expect(req.request.method).toBe('PUT');

      req.flush(updateRequest);
    });

    it('should send updated user in request body', () => {
      service.update(userId.toString(), updateRequest).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      expect(req.request.body).toEqual(updateRequest);
      expect(req.request.body.email).toBe('john.updated@test.com');

      req.flush(updateRequest);
    });

    it('should return updated user', () => {
      service.update(userId.toString(), updateRequest).subscribe(user => {
        expect(user).toEqual(updateRequest);
        expect(user.userName).toBe('Updated');
      });

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      req.flush(updateRequest);
    });

    it('should handle validation error (400)', () => {
      const invalidUser: User = {
        ...updateRequest,
        email: ''
      };

      service.update(userId.toString(), invalidUser).subscribe({
        next: () => fail('should have failed with 400'),
        error: error => {
          expect(error.status).toBe(400);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      req.flush('Invalid data', { status: 400, statusText: 'Bad Request' });
    });

    it('should handle forbidden error (403)', () => {
      service.update(userId.toString(), updateRequest).subscribe({
        next: () => fail('should have failed with 403'),
        error: error => {
          expect(error.status).toBe(403);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      req.flush('Forbidden', { status: 403, statusText: 'Forbidden' });
    });
  });

  describe('Integration Tests', () => {
    const userId = 123;

    const originalUser: User = {
        id: userId,
        userName: 'johndoe',
        email: 'john@test.com',
        password: 'password',
        createdAt: new Date()
    };

    const updatedUser: User = {
      ...originalUser,
      userName: 'Updated'
    };

    it('should retrieve a user and then update it', (done) => {
      service.getById(userId.toString()).subscribe(user => {
        expect(user.userName).toBe('johndoe');

        service.update(userId.toString(), updatedUser).subscribe(updated => {
          expect(updated.userName).toBe('Updated');
          done();
        });

        const updateReq = httpMock.expectOne(`${baseUrl}/${userId}`);
        updateReq.flush(updatedUser);
      });

      const getReq = httpMock.expectOne(`${baseUrl}/${userId}`);
      getReq.flush(originalUser);
    });

    it('should handle update error after successful retrieval', (done) => {
      service.getById(userId.toString()).subscribe(() => {
        service.update(userId.toString(), updatedUser).subscribe({
          next: () => fail('should have failed'),
          error: error => {
            expect(error.status).toBe(500);
            done();
          }
        });

        const updateReq = httpMock.expectOne(`${baseUrl}/${userId}`);
        updateReq.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
      });

      const getReq = httpMock.expectOne(`${baseUrl}/${userId}`);
      getReq.flush(originalUser);
    });
  });

  describe('HTTP Configuration', () => {
    it('should use correct base URL from environment', () => {
      service.getById('123').subscribe();

      const req = httpMock.expectOne(`${environment.baseUrl}/users/123`);
      expect(req.request.url).toContain(environment.baseUrl);

      req.flush({});
    });

    it('should construct correct URLs for all methods', () => {
      const userId = 123;
      const user: User = {
        id: userId,
        userName: 'test',
        email: 'test@test.com',
        password: 'password',
        createdAt: new Date()
      };

      service.getById(userId.toString()).subscribe();
      const getReq = httpMock.expectOne(`${baseUrl}/${userId}`);
      expect(getReq.request.url).toBe(`${baseUrl}/${userId}`);
      getReq.flush(user);

      service.update(userId.toString(), user).subscribe();
      const updateReq = httpMock.expectOne(`${baseUrl}/${userId}`);
      expect(updateReq.request.url).toBe(`${baseUrl}/${userId}`);
      updateReq.flush(user);
    });
  });

  describe('Edge Cases', () => {
    it('should handle very long user IDs', () => {
      const longId = 'a'.repeat(500);

      service.getById(longId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/${longId}`);
      expect(req.request.url).toContain(longId);

      req.flush({});
    });

    it('should handle special characters in user ID', () => {
      const specialId = 'user-123_test@domain';

      service.getById(specialId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/${specialId}`);
      req.flush({});
    });
  });
});
