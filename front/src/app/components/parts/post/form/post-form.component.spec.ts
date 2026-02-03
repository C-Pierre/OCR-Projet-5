import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostFormComponent } from './post-form.component';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';

describe('PostFormComponent', () => {
  let component: PostFormComponent;
  let fixture: ComponentFixture<PostFormComponent>;
  let form: FormGroup;

  const subjectsMock = [
    { id: 1, name: 'Math' },
    { id: 2, name: 'Science' },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostFormComponent, ReactiveFormsModule, CommonModule],
    }).compileComponents();

    form = new FormGroup({
      subjectId: new FormControl('', Validators.required),
      title: new FormControl('', [Validators.required, Validators.maxLength(200)]),
      content: new FormControl('', [Validators.required, Validators.maxLength(5000)]),
    });

    fixture = TestBed.createComponent(PostFormComponent);
    component = fixture.componentInstance;
    component.postForm = form;
    component.subjects = subjectsMock;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render subjects in select options', () => {
    const options = fixture.debugElement.queryAll(By.css('select#subjectId option'));
    // +1 because of the disabled placeholder
    expect(options.length).toBe(subjectsMock.length + 1);
    expect(options[1].nativeElement.textContent.trim()).toBe('Math');
    expect(options[2].nativeElement.textContent.trim()).toBe('Science');
  });

  it('should show validation error when title is invalid and touched', () => {
    const titleControl = form.get('title')!;
    titleControl.markAsTouched();
    fixture.detectChanges();

    const errorEl = fixture.debugElement.query(By.css('.form-group .error'));
    expect(errorEl).toBeTruthy();
    expect(errorEl.nativeElement.textContent).toContain('Titre requis');
  });

  it('should show validation error when content is invalid and touched', () => {
    const contentControl = form.get('content')!;
    contentControl.markAsTouched();
    fixture.detectChanges();

    const errorEl = fixture.debugElement.query(By.css('.form-group .error'));
    expect(errorEl).toBeTruthy();
    expect(errorEl.nativeElement.textContent).toContain('Contenu requis');
  });

  it('should bind form controls to input values', () => {
    form.get('title')!.setValue('Test Title');
    form.get('content')!.setValue('Test content');
    fixture.detectChanges();

    const titleInput = fixture.debugElement.query(By.css('#title')).nativeElement;
    const contentTextarea = fixture.debugElement.query(By.css('#content')).nativeElement;

    expect(titleInput.value).toBe('Test Title');
    expect(contentTextarea.value).toBe('Test content');
  });
});
