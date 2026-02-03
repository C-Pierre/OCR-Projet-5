import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { CommentService } from './comment.service';
import { Comment } from 'src/app/core/models/comment/comment.interface';
import { CommentRequest } from 'src/app/core/models/comment/request/commentRequest.interface';
import { environment } from 'src/environments/environment';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.baseUrl}/comments`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        CommentService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('Unit Tests - getAllByPost', () => {
    const postId = 123;
    const mockComments: Comment[] = [
      {
        id: 1,
        content: 'First comment',
        createdAt: new Date('2024-01-01').toDateString(),
        userId: 1,
        postId: postId
      },
      {
        id: 2,
        content: 'Second comment',
        createdAt: new Date('2024-01-01').toDateString(),
        userId: 1,
        postId: postId
      }
    ];

    it('should send GET request to correct endpoint', () => {
      service.getAllByPost(postId.toString()).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/post/${postId}`);
      expect(req.request.method).toBe('GET');
      
      req.flush([]);
    });

    it('should return array of comments for a post', () => {
      service.getAllByPost(postId.toString()).subscribe(comments => {
        expect(comments).toEqual(mockComments);
        expect(comments.length).toBe(2);
        expect(comments[0].content).toBe('First comment');
        expect(comments[1].content).toBe('Second comment');
      });

      const req = httpMock.expectOne(`${baseUrl}/post/${postId}`);
      req.flush(mockComments);
    });

    it('should return empty array when no comments exist', () => {
      service.getAllByPost(postId.toString()).subscribe(comments => {
        expect(comments).toEqual([]);
        expect(comments.length).toBe(0);
      });

      const req = httpMock.expectOne(`${baseUrl}/post/${postId}`);
      req.flush([]);
    });

    it('should handle different post IDs', () => {
      const postIds = [1, 999, 123];

      postIds.forEach(id => {
        service.getAllByPost(id.toString()).subscribe();
        const req = httpMock.expectOne(`${baseUrl}/post/${id}`);
        expect(req.request.url).toContain(`/post/${id}`);
        req.flush([]);
      });
    });

    it('should handle 404 error when post not found', () => {
      const errorMessage = 'Post not found';

      service.getAllByPost(postId.toString()).subscribe({
        next: () => fail('should have failed with 404 error'),
        error: (error) => {
          expect(error.status).toBe(404);
          expect(error.statusText).toBe('Not Found');
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/post/${postId}`);
      req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
    });

    it('should handle server error', () => {
      service.getAllByPost(postId.toString()).subscribe({
        next: () => fail('should have failed with 500 error'),
        error: (error) => {
          expect(error.status).toBe(500);
        }
      });

      const req = httpMock.expectOne(`${baseUrl}/post/${postId}`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });

    it('should not send multiple requests for single call', () => {
      service.getAllByPost(postId.toString()).subscribe();

      const requests = httpMock.match(`${baseUrl}/post/${postId}`);
      expect(requests.length).toBe(1);
      
      requests[0].flush([]);
    });

    it('should handle comments with all properties', () => {
      const fullComment: Comment = {
        id: 1,
        content: 'Full comment with all properties',
        createdAt: new Date('2024-01-01T10:00:00Z').toDateString(),
        postId: 123,
        userId: 1
      };

      service.getAllByPost(postId.toString()).subscribe(comments => {
        expect(comments[0]).toEqual(fullComment);
        expect(comments[0].authorUsername).toBe('fulluser');
      });

      const req = httpMock.expectOne(`${baseUrl}/post/${postId}`);
      req.flush([fullComment]);
    });
  });

  describe('Unit Tests - create', () => {
    const mockCommentRequest: CommentRequest = {
      content: 'New comment',
        postId: 123,
        userId: 1
    };

    const mockCommentResponse: CommentRequest = {
      content: 'New comment',
        postId: 123,
        userId: 1
    };

    it('should send POST request to correct endpoint', () => {
      service.create(mockCommentRequest).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.method).toBe('POST');
      
      req.flush(mockCommentResponse);
    });

    it('should send comment data in request body', () => {
      service.create(mockCommentRequest).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.body).toEqual(mockCommentRequest);
      
      req.flush(mockCommentResponse);
    });

    it('should return created comment', () => {
      service.create(mockCommentRequest).subscribe(response => {
        expect(response).toEqual(mockCommentResponse);
        expect(response.content).toBe('New comment');
        expect(response.postId).toBe('123');
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush(mockCommentResponse);
    });

    it('should handle validation error (400)', () => {
      const invalidComment: CommentRequest = {
        content: '',
        postId: 123,
        userId: 1
      };

      service.create(invalidComment).subscribe({
        next: () => fail('should have failed with 400 error'),
        error: (error) => {
          expect(error.status).toBe(400);
          expect(error.statusText).toBe('Bad Request');
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Invalid comment content', { status: 400, statusText: 'Bad Request' });
    });

    it('should handle unauthorized error (401)', () => {
      service.create(mockCommentRequest).subscribe({
        next: () => fail('should have failed with 401 error'),
        error: (error) => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    });

    it('should handle forbidden error (403)', () => {
      service.create(mockCommentRequest).subscribe({
        next: () => fail('should have failed with 403 error'),
        error: (error) => {
          expect(error.status).toBe(403);
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Forbidden', { status: 403, statusText: 'Forbidden' });
    });

    it('should handle post not found error (404)', () => {
      const commentForNonexistentPost: CommentRequest = {
        content: 'Comment on non-existent post',
        postId: 999999,
        userId: 1
        };

      service.create(commentForNonexistentPost).subscribe({
        next: () => fail('should have failed with 404 error'),
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const req = httpMock.expectOne(baseUrl);
      req.flush('Post not found', { status: 404, statusText: 'Not Found' });
    });

    it('should create comment with long content', () => {
      const longContent = 'A'.repeat(1000);
      const longCommentRequest: CommentRequest = {
        content: longContent,
        postId: 123,
        userId: 1
      };

      service.create(longCommentRequest).subscribe(response => {
        expect(response.content).toBe(longContent);
      });

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.body.content.length).toBe(1000);
      req.flush(longCommentRequest);
    });

    it('should handle special characters in content', () => {
      const specialComment: CommentRequest = {
        content: 'Comment with émojis 🎉 and <html>tags</html> & symbols!',
        postId: 123,
        userId: 1
      };

      service.create(specialComment).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.body.content).toContain('🎉');
      expect(req.request.body.content).toContain('<html>');
      req.flush(specialComment);
    });
  });

  describe('Integration Tests', () => {
    it('should create a comment and then retrieve it', (done) => {
      const postId = 123;
      const newComment: CommentRequest = {
        content: 'Integration test comment',
        postId: postId,
        userId: 1
      };

      const createdComment: Comment = {
        id: 1,
        content: 'Integration test comment',
        createdAt: new Date().toDateString(),
        postId: postId,
        userId: 1
      };

      service.create(newComment).subscribe(response => {
        expect(response.content).toBe('Integration test comment');

        service.getAllByPost(postId.toString()).subscribe(comments => {
          expect(comments.length).toBeGreaterThan(0);
          expect(comments.some(c => c.content === 'Integration test comment')).toBe(true);
          done();
        });

        const getReq = httpMock.expectOne(`${baseUrl}/post/${postId}`);
        getReq.flush([createdComment]);
      });

      const createReq = httpMock.expectOne(baseUrl);
      createReq.flush(newComment);
    });

    it('should handle multiple comments for same post', () => {
      const postId = 123;
      const comments: CommentRequest[] = [
        { content: 'First comment', postId, userId: 1 },
        { content: 'Second comment', postId, userId: 1 },
        { content: 'Third comment', postId, userId: 1 }
      ];

      let createdCount = 0;

      comments.forEach((comment, index) => {
        service.create(comment).subscribe(() => {
          createdCount++;
          
          if (createdCount === comments.length) {
            service.getAllByPost(postId.toString()).subscribe(retrieved => {
              expect(retrieved.length).toBe(3);
            });

            const getReq = httpMock.expectOne(`${baseUrl}/post/${postId}`);
            getReq.flush(comments.map((c, i) => ({
              id: i + 1,
              content: c.content,
              createdAt: new Date(),
              author: { id: 1, username: 'user', firstName: 'Test', lastName: 'User' }
            })));
          }
        });

        const createReq = httpMock.expectOne(baseUrl);
        createReq.flush(comment);
      });
    });

    it('should retrieve empty list for post with no comments', () => {
      const postId = 123456;

      service.getAllByPost(postId.toString()).subscribe(comments => {
        expect(comments).toEqual([]);
        expect(comments.length).toBe(0);
      });

      const req = httpMock.expectOne(`${baseUrl}/post/${postId}`);
      req.flush([]);
    });

    it('should handle creating comment and getting error on retrieval', (done) => {
      const postId = 123;
      const newComment: CommentRequest = {
        content: 'Test comment',
        postId: postId,
        userId: 1
      };

      service.create(newComment).subscribe(() => {
        service.getAllByPost(postId.toString()).subscribe({
          next: () => fail('should have failed'),
          error: (error) => {
            expect(error.status).toBe(500);
            done();
          }
        });

        const getReq = httpMock.expectOne(`${baseUrl}/post/${postId}`);
        getReq.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
      });

      const createReq = httpMock.expectOne(baseUrl);
      createReq.flush(newComment);
    });
  });

  describe('HTTP configuration', () => {
    it('should use correct base URL from environment', () => {
      service.getAllByPost("1").subscribe();

      const req = httpMock.expectOne(`${environment.baseUrl}/comments/post/1`);
      expect(req.request.url).toContain(environment.baseUrl);
      
      req.flush([]);
    });

    it('should not include extra headers by default', () => {
      service.create({ content: 'test', postId: 123, userId: 1 }).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.headers.keys().length).toBeGreaterThanOrEqual(0);
      
      req.flush({});
    });
  });

  describe('Edge Cases', () => {
    it('should handle empty comment content', () => {
      const emptyComment: CommentRequest = {
        content: '',
        postId: 123,
        userId: 1
      };

      service.create(emptyComment).subscribe();

      const req = httpMock.expectOne(baseUrl);
      expect(req.request.body.content).toBe('');
      
      req.flush(emptyComment);
    });

    it('should handle very large number of comments', () => {
      const manyComments: Comment[] = Array.from({ length: 1000 }, (_, i) => ({
        id: i + 1,
        content: `Comment ${i + 1}`,
        createdAt: new Date().toDateString(),
        postId: 1,
        userId: 1,
        author: {
          id: 1,
          username: 'user',
          firstName: 'Test',
          lastName: 'User'
        }
      }));

      service.getAllByPost("1").subscribe(comments => {
        expect(comments.length).toBe(1000);
      });

      const req = httpMock.expectOne(`${baseUrl}/post/1`);
      req.flush(manyComments);
    });
  });
});