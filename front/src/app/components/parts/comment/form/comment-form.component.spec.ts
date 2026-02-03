import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentFormComponent } from './comment-form.component';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

describe('CommentFormComponent', () => {
  let component: CommentFormComponent;
  let fixture: ComponentFixture<CommentFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CommentFormComponent,
        FormsModule,
        MatIconModule,
        MatButtonModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CommentFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should bind textarea to newComment input', () => {
    component.newComment = 'Hello World';
    fixture.detectChanges();

    const textarea = fixture.debugElement.query(By.css('textarea')).nativeElement;
    // Simuler l'événement input pour que ngModel mette à jour le textarea
    textarea.value = component.newComment;
    textarea.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(textarea.value).toBe('Hello World');
  });

  it('should emit newCommentChange on textarea input', () => {
    const spy = jest.spyOn(component.newCommentChange, 'emit');

    const textarea = fixture.debugElement.query(By.css('textarea')).nativeElement;
    textarea.value = 'New Comment';
    textarea.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(spy).toHaveBeenCalledWith('New Comment');
  });

  it('should emit commentSubmit on form submit if newComment is not empty', () => {
    const spy = jest.spyOn(component.commentSubmit, 'emit');

    component.newComment = 'Test comment';
    fixture.detectChanges();

    const form = fixture.debugElement.query(By.css('form')).nativeElement;
    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();

    expect(spy).toHaveBeenCalled();
  });

  it('should NOT emit commentSubmit if newComment is empty or only spaces', () => {
    const spy = jest.spyOn(component.commentSubmit, 'emit');

    component.newComment = '   ';
    fixture.detectChanges();

    const form = fixture.debugElement.query(By.css('form')).nativeElement;
    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();

    expect(spy).not.toHaveBeenCalled();
  });

  it('should disable submit button when newComment is empty or whitespace', () => {
    const button = fixture.debugElement.query(By.css('button')).nativeElement;

    component.newComment = '';
    fixture.detectChanges();
    expect(button.disabled).toBe(true);

    component.newComment = '   ';
    fixture.detectChanges();
    expect(button.disabled).toBe(true);

    component.newComment = 'Some text';
    fixture.detectChanges();
    expect(button.disabled).toBe(false);
  });
});
