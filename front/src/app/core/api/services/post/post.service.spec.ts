import { PostService } from './post.service';
import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { Post } from '../../../models/post/post.interface';
import { environment } from 'src/environments/environment';
import { PostRequest } from 'src/app/core/models/post/request/postRequest.interface';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.baseUrl}/posts`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PostService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('Unit Tests - create', () => {
    const mockPostRequest: PostRequest = {
        title: 'New Post',
        content: 'This is the content of the new post',
        subjectId: 1,
        authorId: 1
    };

    const mockPostResponse: PostRequest = {
        title: 'New Post',
        content: 'This is the content of the new post',
        subjectId: 1,
        authorId: 1
    };

    it('should send POST request to correct endpoint', () => {
      service.create(mockPostRequest).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.method).toBe('POST');
      
      req.flush(mockPostResponse);
    });

    it('should send post data in request body', () => {
      service.create(mockPostRequest).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.body).toEqual(mockPostRequest);
      expect(req.request.body.title).toBe('New Post');
      expect(req.request.body.content).toBe('This is the content of the new post');
      expect(req.request.body.subjectId).toBe(1);
      
      req.flush(mockPostResponse);
    });

    it('should return created post', () => {
      service.create(mockPostRequest).subscribe(response => {
        expect(response).toEqual(mockPostResponse);
        expect(response.title).toBe('New Post');
        expect(response.content).toBe('This is the content of the new post');
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(mockPostResponse);
    });

    it('should handle validation error (400)', () => {
      const invalidPost: PostRequest = {
        title: '',
        content: 'Content',
        subjectId: 1,
        authorId: 1
      };

      service.create(invalidPost).subscribe({
        next: () => fail('should have failed with 400 error'),
        error: (error) => {
          expect(error.status).toBe(400);
          expect(error.statusText).toBe('Bad Request');
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Invalid post data', { status: 400, statusText: 'Bad Request' });
    });

    it('should handle unauthorized error (401)', () => {
      service.create(mockPostRequest).subscribe({
        next: () => fail('should have failed with 401 error'),
        error: (error) => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    });

    it('should handle forbidden error (403)', () => {
      service.create(mockPostRequest).subscribe({
        next: () => fail('should have failed with 403 error'),
        error: (error) => {
          expect(error.status).toBe(403);
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Forbidden', { status: 403, statusText: 'Forbidden' });
    });

    it('should handle subject not found error (404)', () => {
      const postWithInvalidSubject: PostRequest = {
        title: 'Post',
        content: 'Content',
        subjectId: 999999,
        authorId: 1
      };

      service.create(postWithInvalidSubject).subscribe({
        next: () => fail('should have failed with 404 error'),
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Subject not found', { status: 404, statusText: 'Not Found' });
    });

    it('should create post with long content', () => {
      const longContent = 'A'.repeat(5000);
      const longPostRequest: PostRequest = {
        title: 'Long Post',
        content: longContent,
        subjectId: 1,
        authorId: 1
      };

      service.create(longPostRequest).subscribe(response => {
        expect(response.content).toBe(longContent);
        expect(response.content.length).toBe(5000);
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(longPostRequest);
    });

    it('should handle special characters in title and content', () => {
      const specialPost: PostRequest = {
        title: 'Post with émojis 🚀 and <html>tags</html>',
        content: 'Content with symbols: & < > " \' and unicode: 你好',
        subjectId: 1,
        authorId: 1
      };

      service.create(specialPost).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.body.title).toContain('🚀');
      expect(req.request.body.content).toContain('你好');
      
      req.flush(specialPost);
    });

    it('should not send multiple requests for single call', () => {
      service.create(mockPostRequest).subscribe();

      const requests = httpMock.match(baseUrl);
      expect(requests.length).toBe(1);
      
      requests[0].flush(mockPostResponse);
    });
  });

  describe('Unit Tests - getAllForUser', () => {
    const userId = '123';
    const mockPosts: Post[] = [
      {
        id: 1,
        title: 'First Post',
        content: 'Content of first post',
        createdAt: new Date('2024-01-01'),
        authorUsername: "AUthor",
        subjectName: "Subject"
      },
      {
        id: 2,
        title: 'Second Post',
        content: 'Content of second post',
        createdAt: new Date('2024-01-02'),
        authorUsername: "AUthor",
        subjectName: "Subject"
      }
    ];

    it('should send GET request to correct endpoint', () => {
      service.getAllForUser(userId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      expect(req.request.method).toBe('GET');
      
      req.flush([]);
    });

    it('should return array of posts for a user', () => {
      service.getAllForUser(userId).subscribe(posts => {
        expect(posts).toEqual(mockPosts);
        expect(posts.length).toBe(2);
        expect(posts[0].title).toBe('First Post');
        expect(posts[1].title).toBe('Second Post');
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush(mockPosts);
    });

    it('should return empty array when user has no posts', () => {
      service.getAllForUser(userId).subscribe(posts => {
        expect(posts).toEqual([]);
        expect(posts.length).toBe(0);
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush([]);
    });

    it('should handle different user IDs', () => {
      const userIds = ['1', '999', 'user-abc-123'];

      userIds.forEach(id => {
        service.getAllForUser(id).subscribe();
        const req = httpMock.expectOne(`${baseUrl}/user/${id}`);
        expect(req.request.url).toContain(`/user/${id}`);
        req.flush([]);
      });
    });

    it('should handle 404 error when user not found', () => {
      service.getAllForUser(userId).subscribe({
        next: () => fail('should have failed with 404 error'),
        error: (error) => {
          expect(error.status).toBe(404);
          expect(error.statusText).toBe('Not Found');
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush('User not found', { status: 404, statusText: 'Not Found' });
    });

    it('should handle server error', () => {
      service.getAllForUser(userId).subscribe({
        next: () => fail('should have failed with 500 error'),
        error: (error) => {
          expect(error.status).toBe(500);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });

    it('should handle posts with all properties', () => {
      const fullPost: Post = {
        id: 1,
        title: 'Full Post',
        content: 'Full content',
        createdAt: new Date('2024-01-01T10:00:00Z'),
        authorUsername: "AUthor",
        subjectName: "Subject"
      };

      service.getAllForUser(userId).subscribe(posts => {
        expect(posts[0]).toEqual(fullPost);
        expect(posts[0].authorUsername).toBe('fulluser');
        expect(posts[0].subjectName).toBe('Full Subject');
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush([fullPost]);
    });

    it('should handle large number of posts', () => {
      const manyPosts: Post[] = Array.from({ length: 100 }, (_, i) => ({
        id: i + 1,
        title: `Post ${i + 1}`,
        content: `Content ${i + 1}`,
        createdAt: new Date('2024-01-01'),
        authorUsername: "AUthor",
        subjectName: "Subject"
      }));

      service.getAllForUser(userId).subscribe(posts => {
        expect(posts.length).toBe(100);
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush(manyPosts);
    });
  });

  describe('Unit Tests - getOneById', () => {
    const postId = '456';
    const mockPost: Post = {
        id: 456,
        title: 'Single Post',
        content: 'Content of single post',
        createdAt: new Date('2024-01-01'),
        authorUsername: "AUthor",
        subjectName: "Subject"
    };

    it('should send GET request to correct endpoint', () => {
      service.getOneById(postId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/${postId}`);
      expect(req.request.method).toBe('GET');
      
      req.flush(mockPost);
    });

    it('should return single post by ID', () => {
      service.getOneById(postId).subscribe(post => {
        expect(post).toEqual(mockPost);
        expect(post.id).toBe(456);
        expect(post.title).toBe('Single Post');
      });

      const req = httpMock.expectOne(`${baseUrl}/${postId}`);
      req.flush(mockPost);
    });

    it('should handle different post IDs', () => {
      const postIds = ['1', '999', 'post-abc-123'];

      postIds.forEach(id => {
        service.getOneById(id).subscribe();
        const req = httpMock.expectOne(`${baseUrl}/${id}`);
        expect(req.request.url).toContain(`/${id}`);
        req.flush(mockPost);
      });
    });

    it('should handle 404 error when post not found', () => {
      service.getOneById(postId).subscribe({
        next: () => fail('should have failed with 404 error'),
        error: (error) => {
          expect(error.status).toBe(404);
          expect(error.statusText).toBe('Not Found');
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/${postId}`);
      req.flush('Post not found', { status: 404, statusText: 'Not Found' });
    });

    it('should handle unauthorized error (401)', () => {
      service.getOneById(postId).subscribe({
        next: () => fail('should have failed with 401 error'),
        error: (error) => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/${postId}`);
      req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    });

    it('should handle server error', () => {
      service.getOneById(postId).subscribe({
        next: () => fail('should have failed with 500 error'),
        error: (error) => {
          expect(error.status).toBe(500);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/${postId}`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });

    it('should return post with all properties correctly', () => {
      service.getOneById(postId).subscribe(post => {
        expect(post.id).toBeDefined();
        expect(post.title).toBeDefined();
        expect(post.content).toBeDefined();
        expect(post.createdAt).toBeDefined();
        expect(post.authorUsername).toBeDefined();
        expect(post.subjectName).toBeDefined();
        expect(post.subjectName).toBe('Angular');
      });

      const req = httpMock.expectOne(`${baseUrl}/${postId}`);
      req.flush(mockPost);
    });

    it('should not send multiple requests for single call', () => {
      service.getOneById(postId).subscribe();

      const requests = httpMock.match(`${baseUrl}/${postId}`);
      expect(requests.length).toBe(1);
      
      requests[0].flush(mockPost);
    });
  });

  describe('Integration Tests', () => {
    it('should create a post and then retrieve it by ID', (done) => {
      const newPost: PostRequest = {
        title: 'Integration Test Post',
        content: 'This is an integration test',
        subjectId: 1,
        authorId: 1
      };

      const createdPost: Post = {
        id: 1,
        title: 'Integration Test Post',
        content: 'This is an integration test',
        createdAt: new Date(),
        authorUsername: "Author",
        subjectName: "Subject"
      };

      service.create(newPost).subscribe(response => {
        expect(response.title).toBe('Integration Test Post');

        service.getOneById('1').subscribe(post => {
          expect(post.title).toBe('Integration Test Post');
          expect(post.content).toBe('This is an integration test');
          done();
        });

        const getReq = httpMock.expectOne(`${baseUrl}/1`);
        getReq.flush(createdPost);
      });

      const createReq = httpMock.expectOne(baseUrl);
      createReq.flush(newPost);
    });

    it('should create multiple posts and retrieve them for a user', (done) => {
      const userId = '123';
      const posts: PostRequest[] = [
        { title: 'Post 1', content: 'Content 1', subjectId: 1, authorId: 1 },
        { title: 'Post 2', content: 'Content 2', subjectId: 2, authorId: 1 },
        { title: 'Post 3', content: 'Content 3', subjectId: 1, authorId: 1 }
      ];

      let createdCount = 0;

      posts.forEach((post, index) => {
        service.create(post).subscribe(() => {
          createdCount++;
          
          if (createdCount === posts.length) {
            service.getAllForUser(userId).subscribe(userPosts => {
              expect(userPosts.length).toBe(3);
              expect(userPosts.some(p => p.title === 'Post 1')).toBe(true);
              expect(userPosts.some(p => p.title === 'Post 2')).toBe(true);
              expect(userPosts.some(p => p.title === 'Post 3')).toBe(true);
              done();
            });

            const getReq = httpMock.expectOne(`${baseUrl}/user/${userId}`);
            getReq.flush(posts.map((p, i) => ({
              id: i + 1,
              title: p.title,
              content: p.content,
              createdAt: new Date(),
              author: {
                id: 123,
                username: 'user123',
                firstName: 'John',
                lastName: 'Doe'
              },
              subject: {
                id: parseInt(p.subjectId.toString()),
                name: `Subject ${p.subjectId}`
              }
            })));
          }
        });

        const createReq = httpMock.expectOne(baseUrl);
        createReq.flush(post);
      });
    });

    it('should retrieve all posts for a user and then get details of specific post', (done) => {
      const userId = '123';
      const mockPosts: Post[] = [
        {
            id: 1,
            title: 'Post 1',
            content: 'Content 1',
            createdAt: new Date(),
            authorUsername: "Author",
            subjectName: "Subject"
        },
        {
            id: 2,
            title: 'Post 2',
            content: 'Content 2',
            createdAt: new Date(),
            authorUsername: "Author",
            subjectName: "Subject"
        }
      ];

      service.getAllForUser(userId).subscribe(posts => {
        expect(posts.length).toBe(2);
        const firstPostId = posts[0].id.toString();

        service.getOneById(firstPostId).subscribe(detailedPost => {
          expect(detailedPost.id).toBe(1);
          expect(detailedPost.title).toBe('Post 1');
          done();
        });

        const getOneReq = httpMock.expectOne(`${baseUrl}/${firstPostId}`);
        getOneReq.flush(mockPosts[0]);
      });

      const getAllReq = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      getAllReq.flush(mockPosts);
    });

    it('should handle creating post and getting error on retrieval', (done) => {
      const newPost: PostRequest = {
        title: 'Test Post',
        content: 'Test Content',
        subjectId: 1,
        authorId: 1
      };

      service.create(newPost).subscribe(() => {
        service.getOneById('1').subscribe({
          next: () => fail('should have failed'),
          error: (error) => {
            expect(error.status).toBe(404);
            done();
          }
        });

        const getReq = httpMock.expectOne(`${baseUrl}/1`);
        getReq.flush('Post not found', { status: 404, statusText: 'Not Found' });
      });

      const createReq = httpMock.expectOne(baseUrl);
      createReq.flush(newPost);
    });

    it('should retrieve empty list for new user with no posts', () => {
      const newUserId = 'new-user-999';

      service.getAllForUser(newUserId).subscribe(posts => {
        expect(posts).toEqual([]);
        expect(posts.length).toBe(0);
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${newUserId}`);
      req.flush([]);
    });
  });

  describe('HTTP Configuration', () => {
    it('should use correct base URL from environment', () => {
      service.getOneById('123').subscribe();

      const req = httpMock.expectOne(`${environment.baseUrl}/posts/123`);
      expect(req.request.url).toContain(environment.baseUrl);
      
      req.flush({});
    });

    it('should construct correct URLs for all methods', () => {
      const userId = '123';
      const postId = '456';
      const postRequest: PostRequest = { title: 'Test', content: 'Test', subjectId: 1, authorId: 123 };

      service.create(postRequest).subscribe();
      const createReq = httpMock.expectOne(baseUrl);
      expect(createReq.request.url).toBe(baseUrl);
      createReq.flush({});

      service.getAllForUser(userId).subscribe();
      const getAllReq = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      expect(getAllReq.request.url).toBe(`${baseUrl}/user/${userId}`);
      getAllReq.flush([]);

      service.getOneById(postId).subscribe();
      const getOneReq = httpMock.expectOne(`${baseUrl}/${postId}`);
      expect(getOneReq.request.url).toBe(`${baseUrl}/${postId}`);
      getOneReq.flush({});
    });
  });

  describe('Edge Cases', () => {
    it('should handle very long post IDs', () => {
      const longPostId = 'a'.repeat(500);

      service.getOneById(longPostId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/${longPostId}`);
      expect(req.request.url).toContain(longPostId);
      
      req.flush({});
    });

    it('should handle post IDs with special characters', () => {
      const specialPostId = 'post-123_test@domain';

      service.getOneById(specialPostId).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/${specialPostId}`);
      
      req.flush({});
    });

    it('should handle empty title and content', () => {
      const emptyPost: PostRequest = {
        title: '',
        content: '',
        subjectId: 1,
        authorId: 1
      };

      service.create(emptyPost).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.body.title).toBe('');
      expect(req.request.body.content).toBe('');
      
      req.flush(emptyPost);
    });

    it('should handle user with single post', () => {
      const userId = '123';
      const singlePost: Post = {
        id: 1,
        title: 'Only Post',
        content: 'Content',
        createdAt: new Date(),
        authorUsername: "UserName",
        subjectName: "Subject"
      };

      service.getAllForUser(userId).subscribe(posts => {
        expect(posts.length).toBe(1);
        expect(posts[0].title).toBe('Only Post');
      });

      const req = httpMock.expectOne(`${baseUrl}/user/${userId}`);
      req.flush([singlePost]);
    });
  });
});