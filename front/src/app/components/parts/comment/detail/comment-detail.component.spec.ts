import { By } from '@angular/platform-browser';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentDetailComponent } from './comment-detail.component';
import { Comment } from 'src/app/core/models/comment/comment.interface';

describe('CommentDetailComponent', () => {
  let component: CommentDetailComponent;
  let fixture: ComponentFixture<CommentDetailComponent>;

  const fakeComment: Comment = {
    id: 1,
    authorUsername: 'JohnDoe',
    content: 'This is a test comment',
    postId: 1,
    userId: 1
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommentDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CommentDetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display comment content when comment is defined', () => {
    component.comment = fakeComment;
    fixture.detectChanges();

    const usernameEl = fixture.debugElement.query(By.css('.username')).nativeElement;
    const contentEl = fixture.debugElement.query(By.css('.content')).nativeElement;

    expect(usernameEl.textContent).toContain(fakeComment.authorUsername);
    expect(contentEl.textContent).toContain(fakeComment.content);
  });

  it('should not render comment when comment is undefined', () => {
    component.comment = undefined as any;
    fixture.detectChanges();

    const commentEl = fixture.debugElement.query(By.css('.comment'));
    expect(commentEl).toBeNull();
  });

  it('should allow newComment input', () => {
    component.newComment = 'Hello';
    expect(component.newComment).toBe('Hello');
  });
});
