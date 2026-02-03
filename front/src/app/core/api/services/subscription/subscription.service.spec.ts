import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { SubscriptionService } from './subscription.service';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { SubscriptionRequest } from 'src/app/core/models/subscription/request/subscriptionRequest.interface';

describe('SubscriptionService', () => {
  let service: SubscriptionService;
  let httpMock: HttpTestingController;

  const baseUrl = `${environment.baseUrl}/subscriptions`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        SubscriptionService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(SubscriptionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  describe('Unit Tests - getAllForUser', () => {
    const userId = '123';

    const mockSubscriptions: Subscription[] = [
      {
        id: 1,
        name: "TestName",
        description: "Description"
      },
      {
        id: 2,
        name: "TestName2",
        description: "Description"
      }
    ];

    it('should send GET request to correct endpoint', () => {
      service.getAllForUser(userId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      expect(req.request.method).toBe('GET');

      req.flush([]);
    });

    it('should return subscriptions for a user', () => {
      service.getAllForUser(userId).subscribe(subscriptions => {
        expect(subscriptions.length).toBe(2);
        expect(subscriptions).toEqual(mockSubscriptions);
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush(mockSubscriptions);
    });

    it('should return empty array when user has no subscriptions', () => {
      service.getAllForUser(userId).subscribe(subscriptions => {
        expect(subscriptions).toEqual([]);
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush([]);
    });

    it('should handle 404 error when user not found', () => {
      service.getAllForUser(userId).subscribe({
        next: () => fail('should have failed with 404'),
        error: error => {
          expect(error.status).toBe(404);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush('User not found', { status: 404, statusText: 'Not Found' });
    });

    it('should handle server error (500)', () => {
      service.getAllForUser(userId).subscribe({
        next: () => fail('should have failed with 500'),
        error: error => {
          expect(error.status).toBe(500);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });

    it('should not send multiple requests for a single call', () => {
      service.getAllForUser(userId).subscribe();

      const requests = httpMock.match(`${baseUrl}/user/${userId}`);
      expect(requests.length).toBe(1);

      requests[0].flush([]);
    });
  });


  describe('Unit Tests - subscribeUser', () => {
    const mockRequest: SubscriptionRequest = {
      userId: 123,
      subjectId: 10
    };

    it('should send POST request to correct endpoint', () => {
      service.subscribeUser(mockRequest).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.method).toBe('POST');

      req.flush(mockRequest);
    });

    it('should send request body correctly', () => {
      service.subscribeUser(mockRequest).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.body).toEqual(mockRequest);

      req.flush(mockRequest);
    });

    it('should return subscription request on success', () => {
      service.subscribeUser(mockRequest).subscribe(response => {
        expect(response).toEqual(mockRequest);
        expect(response.userId).toBe('123');
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(mockRequest);
    });

    it('should handle conflict error (409) when already subscribed', () => {
      service.subscribeUser(mockRequest).subscribe({
        next: () => fail('should have failed with 409'),
        error: error => {
          expect(error.status).toBe(409);
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Already subscribed', { status: 409, statusText: 'Conflict' });
    });
  });

  describe('Unit Tests - unSubscribeUser', () => {
    const mockRequest: SubscriptionRequest = {
      userId: 123,
      subjectId: 10
    };

    it('should send DELETE request to correct endpoint', () => {
      service.unSubscribeUser(mockRequest).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.method).toBe('DELETE');

      req.flush(mockRequest);
    });

    it('should send request body in DELETE call', () => {
      service.unSubscribeUser(mockRequest).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.body).toEqual(mockRequest);

      req.flush(mockRequest);
    });

    it('should return request on successful unsubscribe', () => {
      service.unSubscribeUser(mockRequest).subscribe(response => {
        expect(response).toEqual(mockRequest);
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(mockRequest);
    });

    it('should handle 404 error when subscription does not exist', () => {
      service.unSubscribeUser(mockRequest).subscribe({
        next: () => fail('should have failed with 404'),
        error: error => {
          expect(error.status).toBe(404);
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Subscription not found', { status: 404, statusText: 'Not Found' });
    });
  });


  describe('Integration Tests', () => {
    const userId = 123;

    it('should subscribe a user and then retrieve subscriptions', (done) => {
      const request: SubscriptionRequest = {
        userId,
        subjectId: 10
      };

      const subscriptions: Subscription[] = [
        {
            id: 1,
            name: "Subscription",
            description: "Description"
        }
      ];

      service.subscribeUser(request).subscribe(() => {
        service.getAllForUser(userId.toString()).subscribe(result => {
          expect(result.length).toBe(1);
          expect(result[0].name).toBe("Subscription");
          done();
        });

        const getReq = httpMock.expectOne(`${baseUrl}/user/${userId}`);
        getReq.flush(subscriptions);
      });

      const postReq = httpMock.expectOne(baseUrl);
      postReq.flush(request);
    });

    it('should subscribe then unsubscribe a user', (done) => {
      const request: SubscriptionRequest = {
        userId,
        subjectId: 10
      };

      service.subscribeUser(request).subscribe(() => {
        service.unSubscribeUser(request).subscribe(response => {
          expect(response).toEqual(request);
          done();
        });

        const deleteReq = httpMock.expectOne(baseUrl);
        deleteReq.flush(request);
      });

      const postReq = httpMock.expectOne(baseUrl);
      postReq.flush(request);
    });
  });

  describe('HTTP Configuration', () => {
    it('should use correct base URL from environment', () => {
      service.getAllForUser('123').subscribe();

      const req = httpMock.expectOne(`${environment.baseUrl}/subscriptions/user/123`);
      expect(req.request.url).toContain(environment.baseUrl);

      req.flush([]);
    });
  });
});
