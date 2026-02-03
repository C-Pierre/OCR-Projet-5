import { CommonModule } from '@angular/common';
import { By } from '@angular/platform-browser';
import { PostDetailComponent } from './post-detail.component';
import { Post } from 'src/app/core/models/post/post.interface';
import { ComponentFixture, TestBed } from '@angular/core/testing';

describe('PostDetailComponent', () => {
  let component: PostDetailComponent;
  let fixture: ComponentFixture<PostDetailComponent>;

  const mockPost: Post = {
    id: 1,
    title: "Title",
    authorUsername: 'JohnDoe',
    subjectName: 'Math',
    content: 'This is a test post',
    createdAt: new Date('2026-02-04T10:00:00'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostDetailComponent, CommonModule],
    }).compileComponents();

    fixture = TestBed.createComponent(PostDetailComponent);
    component = fixture.componentInstance;
    component.post = mockPost;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display post content', () => {
    const contentEl = fixture.debugElement.query(By.css('.post-content')).nativeElement;
    expect(contentEl.textContent).toContain('This is a test post');
  });

  it('should display post meta', () => {
    const dateEl = fixture.debugElement.query(By.css('.date')).nativeElement;
    const authorEl = fixture.debugElement.query(By.css('.author')).nativeElement;
    const subjectEl = fixture.debugElement.query(By.css('.subject')).nativeElement;

    expect(dateEl.textContent).toBe('04/02/2026');
    expect(authorEl.textContent).toBe('JohnDoe');
    expect(subjectEl.textContent).toBe('Math');
  });
});
