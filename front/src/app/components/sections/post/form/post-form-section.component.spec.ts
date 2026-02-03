import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormGroup, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { PostFormSectionComponent, PostFormControls, PostFormValue } from './post-form-section.component';

describe('PostFormSectionComponent avec NO_ERRORS_SCHEMA', () => {
  let fixture: ComponentFixture<PostFormSectionComponent>;
  let component: PostFormSectionComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, PostFormSectionComponent],
      schemas: [NO_ERRORS_SCHEMA] // ignore les composants non reconnus
    }).compileComponents();

    fixture = TestBed.createComponent(PostFormSectionComponent);
    component = fixture.componentInstance;

    component.postForm = new FormGroup<PostFormControls>({
      title: new FormControl('Titre', { nonNullable: true, validators: [Validators.required] }),
      content: new FormControl('Contenu', { nonNullable: true, validators: [Validators.required] }),
      subjectId: new FormControl(1, { nonNullable: true }),
    });

    component.subjects = [
      { id: 1, name: 'S1' },
      { id: 2, name: 'S2' }
    ];

    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('le formulaire est initialisé correctement', () => {
    expect(component.postForm.value).toEqual({
      title: 'Titre',
      content: 'Contenu',
      subjectId: 1
    });
    expect(component.subjects.length).toBe(2);
  });

  it('devrait émettre submitForm avec les bonnes valeurs quand le formulaire est valide', () => {
    const emitSpy = jest.spyOn(component.submitForm, 'emit');
    component.onSubmit();
    expect(emitSpy).toHaveBeenCalledWith({
      title: 'Titre',
      content: 'Contenu',
      subjectId: 1
    } as PostFormValue);
  });

  it('ne doit pas émettre submitForm et marque tous les champs comme touchés si le formulaire est invalide', () => {
    const emitSpy = jest.spyOn(component.submitForm, 'emit');
    const markSpy = jest.spyOn(component.postForm, 'markAllAsTouched');

    component.postForm.controls.title.setValue('');
    component.onSubmit();

    expect(emitSpy).not.toHaveBeenCalled();
    expect(markSpy).toHaveBeenCalled();
  });
});
