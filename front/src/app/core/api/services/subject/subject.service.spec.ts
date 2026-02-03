import { TestBed } from '@angular/core/testing';
import { SubjectService } from './subject.service';
import { provideHttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Subject } from 'src/app/core/models/subject/subject.interface';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('SubjectService', () => {
  let service: SubjectService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.baseUrl}/subjects`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        SubjectService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(SubjectService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('Unit Tests - all', () => {
    const mockSubjects: Subject[] = [
      {
        id: 1,
        name: 'Angular',
        description: 'Learn Angular framework',
        subscribed: true
      },
      {
        id: 2,
        name: 'React',
        description: 'Learn React library',
        subscribed: true
      },
      {
        id: 3,
        name: 'Vue',
        description: 'Learn Vue.js framework',
        subscribed: true
      }
    ];

    it('should send GET request to correct endpoint', () => {
      service.all().subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.method).toBe('GET');
      
      req.flush([]);
    });

    it('should return array of all subjects', () => {
      service.all().subscribe(subjects => {
        expect(subjects).toEqual(mockSubjects);
        expect(subjects.length).toBe(3);
        expect(subjects[0].name).toBe('Angular');
        expect(subjects[1].name).toBe('React');
        expect(subjects[2].name).toBe('Vue');
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(mockSubjects);
    });

    it('should return empty array when no subjects exist', () => {
      service.all().subscribe(subjects => {
        expect(subjects).toEqual([]);
        expect(subjects.length).toBe(0);
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush([]);
    });

    it('should handle subjects with all properties', () => {
      const fullSubject: Subject = {
        id: 1,
        name: 'TypeScript',
        description: 'Complete TypeScript course with advanced topics',
        subscribed: true
      };

      service.all().subscribe(subjects => {
        expect(subjects[0]).toEqual(fullSubject);
        expect(subjects[0].id).toBe(1);
        expect(subjects[0].name).toBe('TypeScript');
        expect(subjects[0].description).toBe('Complete TypeScript course with advanced topics');
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush([fullSubject]);
    });

    it('should handle server error (500)', () => {
      service.all().subscribe({
        next: () => fail('should have failed with 500 error'),
        error: (error) => {
          expect(error.status).toBe(500);
          expect(error.statusText).toBe('Internal Server Error');
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });

    it('should handle unauthorized error (401)', () => {
      service.all().subscribe({
        next: () => fail('should have failed with 401 error'),
        error: (error) => {
          expect(error.status).toBe(401);
          expect(error.statusText).toBe('Unauthorized');
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    });

    it('should handle service unavailable error (503)', () => {
      service.all().subscribe({
        next: () => fail('should have failed with 503 error'),
        error: (error) => {
          expect(error.status).toBe(503);
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Service unavailable', { status: 503, statusText: 'Service Unavailable' });
    });

    it('should not send multiple requests for single call', () => {
      service.all().subscribe();

      const requests = httpMock.match(baseUrl);
      expect(requests.length).toBe(1);
      
      requests[0].flush([]);
    });

    it('should handle large number of subjects', () => {
      const manySubjects: Subject[] = Array.from({ length: 200 }, (_, i) => ({
        id: i + 1,
        name: `Subject ${i + 1}`,
        description: `Description for subject ${i + 1}`,
        subscribed: false
      }));

      service.all().subscribe(subjects => {
        expect(subjects.length).toBe(200);
        expect(subjects[0].name).toBe('Subject 1');
        expect(subjects[199].name).toBe('Subject 200');
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(manySubjects);
    });

    it('should handle subjects with special characters in name', () => {
      const specialSubjects: Subject[] = [
        {
            id: 1,
            name: 'C++ & C#',
            description: 'Learn C++ and C# programming',
            subscribed: true
        },
        {
            id: 2,
            name: 'Node.js & Express',
            description: 'Backend development with <Node.js>',
            subscribed: true
        },
        {
            id: 3,
            name: '日本語 (Japanese)',
            description: 'Learn Japanese language 🇯🇵',
            subscribed: true
        }
      ];

      service.all().subscribe(subjects => {
        expect(subjects[0].name).toContain('C++');
        expect(subjects[1].name).toContain('Node.js');
        expect(subjects[2].name).toContain('日本語');
        expect(subjects[2].description).toContain('🇯🇵');
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(specialSubjects);
    });

    it('should handle subjects with empty descriptions', () => {
      const subjectsWithEmptyDesc: Subject[] = [
        {
            id: 1,
            name: 'Python',
            description: '',
            subscribed: true
        }
      ];

      service.all().subscribe(subjects => {
        expect(subjects[0].description).toBe('');
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(subjectsWithEmptyDesc);
    });

    it('should handle subjects with very long descriptions', () => {
      const longDescription = 'A'.repeat(5000);
      const subjectWithLongDesc: Subject[] = [
        {
            id: 1,
            name: 'Advanced Course',
            description: longDescription,
            subscribed: true
        }
      ];

      service.all().subscribe(subjects => {
        expect(subjects[0].description.length).toBe(5000);
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(subjectWithLongDesc);
    });
  });

  describe('Unit Tests - allForUser', () => {
    const userId = '123';
    const mockUserSubjects: Subject[] = [
      {
        id: 1,
        name: 'Angular',
        description: 'Learn Angular framework',
        subscribed: true
      },
      {
        id: 2,
        name: 'TypeScript',
        description: 'Learn TypeScript',
        subscribed: true
      }
    ];

    it('should send GET request to correct endpoint', () => {
      service.allForUser(userId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      expect(req.request.method).toBe('GET');
      
      req.flush([]);
    });

    it('should return array of subjects for a specific user', () => {
      service.allForUser(userId).subscribe(subjects => {
        expect(subjects).toEqual(mockUserSubjects);
        expect(subjects.length).toBe(2);
        expect(subjects[0].name).toBe('Angular');
        expect(subjects[1].name).toBe('TypeScript');
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush(mockUserSubjects);
    });

    it('should return empty array when user has no subjects', () => {
      service.allForUser(userId).subscribe(subjects => {
        expect(subjects).toEqual([]);
        expect(subjects.length).toBe(0);
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush([]);
    });

    it('should handle different user IDs', () => {
      const userIds = ['1', '999', 'user-abc-123'];

      userIds.forEach(id => {
        service.allForUser(id).subscribe();
        const req = httpMock.expectOne(`${baseUrl}/user/${id}`);
        expect(req.request.url).toContain(`/user/${id}`);
        req.flush([]);
      });
    });

    it('should handle 404 error when user not found', () => {
      service.allForUser(userId).subscribe({
        next: () => fail('should have failed with 404 error'),
        error: (error) => {
          expect(error.status).toBe(404);
          expect(error.statusText).toBe('Not Found');
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush('User not found', { status: 404, statusText: 'Not Found' });
    });

    it('should handle unauthorized error (401)', () => {
      service.allForUser(userId).subscribe({
        next: () => fail('should have failed with 401 error'),
        error: (error) => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    });

    it('should handle forbidden error (403)', () => {
      service.allForUser(userId).subscribe({
        next: () => fail('should have failed with 403 error'),
        error: (error) => {
          expect(error.status).toBe(403);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush('Forbidden', { status: 403, statusText: 'Forbidden' });
    });

    it('should handle server error (500)', () => {
      service.allForUser(userId).subscribe({
        next: () => fail('should have failed with 500 error'),
        error: (error) => {
          expect(error.status).toBe(500);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });

    it('should handle user with single subject', () => {
      const singleSubject: Subject[] = [
        {
            id: 1,
            name: 'JavaScript',
            description: 'Learn JavaScript basics',
            subscribed: true
        }
      ];

      service.allForUser(userId).subscribe(subjects => {
        expect(subjects.length).toBe(1);
        expect(subjects[0].name).toBe('JavaScript');
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush(singleSubject);
    });

    it('should handle user with many subjects', () => {
      const manySubjects: Subject[] = Array.from({ length: 50 }, (_, i) => ({
        id: i + 1,
        name: `Subject ${i + 1}`,
        description: `Description ${i + 1}`,
        subscribed: true
      }));

      service.allForUser(userId).subscribe(subjects => {
        expect(subjects.length).toBe(50);
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush(manySubjects);
    });

    it('should not send multiple requests for single call', () => {
      service.allForUser(userId).subscribe();

      const requests = httpMock.match(`${baseUrl}/user/${userId}`);
      expect(requests.length).toBe(1);
      
      requests[0].flush([]);
    });

    it('should handle very long user IDs', () => {
      const longUserId = 'a'.repeat(500);

      service.allForUser(longUserId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/user/${longUserId}`);
      expect(req.request.url).toContain(longUserId);
      
      req.flush([]);
    });

    it('should handle user IDs with special characters', () => {
      const specialUserId = 'user-123_test@domain';

      service.allForUser(specialUserId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/user/${specialUserId}`);
      
      req.flush([]);
    });
  });

  describe('Integration Tests', () => {
    it('should retrieve all subjects and then filter by user', (done) => {
      const userId = '123';
      const allSubjects: Subject[] = [
        { id: 1, name: 'Angular', description: 'Angular framework', subscribed: false },
        { id: 2, name: 'React', description: 'React library', subscribed: false },
        { id: 3, name: 'Vue', description: 'Vue.js framework', subscribed: false }
      ];

      const userSubjects: Subject[] = [
        { id: 1, name: 'Angular', description: 'Angular framework', subscribed: true },
        { id: 3, name: 'Vue', description: 'Vue.js framework', subscribed: true }
      ];

      // Step 1: Get all subjects
      service.all().subscribe(subjects => {
        expect(subjects.length).toBe(3);

        // Step 2: Get subjects for specific user
        service.allForUser(userId).subscribe(userSubs => {
          expect(userSubs.length).toBe(2);
          expect(userSubs.some(s => s.name === 'Angular')).toBe(true);
          expect(userSubs.some(s => s.name === 'Vue')).toBe(true);
          expect(userSubs.some(s => s.name === 'React')).toBe(false);
          done();
        });

        const userReq = httpMock.expectOne(`${baseUrl}/user/${userId}`);
        userReq.flush(userSubjects);
      });

      const allReq = httpMock.expectOne(baseUrl);
      allReq.flush(allSubjects);
    });

    it('should compare all subjects with user subjects', (done) => {
      const userId = '123';
      const allSubjects: Subject[] = [
        { id: 1, name: 'Subject 1', description: 'Description 1', subscribed: false },
        { id: 2, name: 'Subject 2', description: 'Description 2', subscribed: false },
        { id: 3, name: 'Subject 3', description: 'Description 3', subscribed: false }
      ];

      const userSubjects: Subject[] = [
        { id: 1, name: 'Subject 1', description: 'Description 1', subscribed: true }
      ];

      service.all().subscribe(all => {
        service.allForUser(userId).subscribe(user => {
          // User has subscribed to 1 out of 3 subjects
          expect(all.length).toBe(3);
          expect(user.length).toBe(1);
          expect(all.length - user.length).toBe(2); // 2 subjects not subscribed
          done();
        });

        const userReq = httpMock.expectOne(`${baseUrl}/user/${userId}`);
        userReq.flush(userSubjects);
      });

      const allReq = httpMock.expectOne(baseUrl);
      allReq.flush(allSubjects);
    });

    it('should handle multiple users subscribing to different subjects', (done) => {
      const user1Id = '1';
      const user2Id = '2';

      const user1Subjects: Subject[] = [
        { id: 1, name: 'Angular', description: 'Angular', subscribed: false }
      ];

      const user2Subjects: Subject[] = [
        { id: 2, name: 'React', description: 'React', subscribed: false }
      ];

      service.allForUser(user1Id).subscribe(subjects1 => {
        expect(subjects1.length).toBe(1);
        expect(subjects1[0].name).toBe('Angular');

        service.allForUser(user2Id).subscribe(subjects2 => {
          expect(subjects2.length).toBe(1);
          expect(subjects2[0].name).toBe('React');
          
          expect(subjects1[0].id).not.toBe(subjects2[0].id);
          done();
        });

        const user2Req = httpMock.expectOne(`${baseUrl}/user/${user2Id}`);
        user2Req.flush(user2Subjects);
      });

      const user1Req = httpMock.expectOne(`${baseUrl}/user/${user1Id}`);
      user1Req.flush(user1Subjects);
    });

    it('should handle error retrieving all subjects but success for user subjects', (done) => {
      const userId = '123';

      service.all().subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(500);

          service.allForUser(userId).subscribe(subjects => {
            expect(subjects).toEqual([]);
            done();
          });

          const userReq = httpMock.expectOne(`${baseUrl}/user/${userId}`);
          userReq.flush([]);
        }
      });

      const allReq = httpMock.expectOne(baseUrl);
      allReq.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });

    it('should handle new user with no subjects when all subjects exist', (done) => {
      const newUserId = 'new-user-999';
      const allSubjects: Subject[] = [
        { id: 1, name: 'Subject 1', description: 'Description 1', subscribed: false },
        { id: 2, name: 'Subject 2', description: 'Description 2', subscribed: false }
      ];

      service.all().subscribe(all => {
        expect(all.length).toBeGreaterThan(0);

        service.allForUser(newUserId).subscribe(user => {
          expect(user.length).toBe(0);
          expect(all.length).toBeGreaterThan(user.length);
          done();
        });

        const userReq = httpMock.expectOne(`${baseUrl}/user/${newUserId}`);
        userReq.flush([]);
      });

      const allReq = httpMock.expectOne(baseUrl);
      allReq.flush(allSubjects);
    });

    it('should retrieve user subjects multiple times and get consistent results', (done) => {
      const userId = '123';
      const userSubjects: Subject[] = [
        { id: 1, name: 'Consistent Subject', description: 'Always the same', subscribed: true }
      ];

      let callCount = 0;

      const checkConsistency = () => {
        service.allForUser(userId).subscribe(subjects => {
          callCount++;
          expect(subjects.length).toBe(1);
          expect(subjects[0].name).toBe('Consistent Subject');

          if (callCount === 3) {
            done();
          } else {
            checkConsistency();
          }
        });

        const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
        req.flush(userSubjects);
      };

      checkConsistency();
    });
  });

  describe('HTTP Configuration', () => {
    it('should use correct base URL from environment for all()', () => {
      service.all().subscribe();

      const req = httpMock.expectOne(`${environment.baseUrl}/subjects`);
      expect(req.request.url).toContain(environment.baseUrl);
      
      req.flush([]);
    });

    it('should use correct base URL from environment for allForUser()', () => {
      const userId = '123';
      service.allForUser(userId).subscribe();

      const req = httpMock.expectOne(`${environment.baseUrl}/subjects/user/${userId}`);
      expect(req.request.url).toContain(environment.baseUrl);
      
      req.flush([]);
    });

    it('should construct correct URLs for both methods', () => {
      const userId = '123';

      service.all().subscribe();
      const allReq = httpMock.expectOne(baseUrl);
      expect(allReq.request.url).toBe(baseUrl);
      allReq.flush([]);

      service.allForUser(userId).subscribe();
      const userReq = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      expect(userReq.request.url).toBe(`${baseUrl}/user/${userId}`);
      userReq.flush([]);
    });

    it('should use GET method for both endpoints', () => {
      const userId = '123';

      service.all().subscribe();
      const allReq = httpMock.expectOne(baseUrl);
      expect(allReq.request.method).toBe('GET');
      allReq.flush([]);

      service.allForUser(userId).subscribe();
      const userReq = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      expect(userReq.request.method).toBe('GET');
      userReq.flush([]);
    });
  });

  describe('Edge Cases', () => {
    it('should handle null or undefined in response gracefully', () => {
      service.all().subscribe(subjects => {
        expect(subjects).toBeDefined();
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(null as any);
    });

    it('should handle subjects with minimum data', () => {
      const minimalSubject: Subject[] = [
        {
            id: 1,
            name: 'A',
            description: '',
            subscribed: false
        }
      ];

      service.all().subscribe(subjects => {
        expect(subjects[0].name.length).toBe(1);
        expect(subjects[0].description).toBe('');
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(minimalSubject);
    });

    it('should handle rapid consecutive calls', () => {
      const userId = '123';
      const mockSubjects: Subject[] = [{ id: 1, name: 'Test', description: 'Test', subscribed: false }];

      for (let i = 0; i < 5; i++) {
        service.allForUser(userId).subscribe();
      }

      const requests = httpMock.match(`${baseUrl}/user/${userId}`);
      expect(requests.length).toBe(5);
      
      requests.forEach(req => req.flush(mockSubjects));
    });
  });
});