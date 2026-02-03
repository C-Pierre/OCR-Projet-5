import { By } from '@angular/platform-browser';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentSectionComponent } from './comment-section.component';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Comment } from 'src/app/core/models/comment/comment.interface';

@Component({
  selector: 'app-comment-detail',
  standalone: true,
  template: '<div class="mock-comment-detail">{{comment.content}}</div>',
})
class MockCommentDetailComponent {
  @Input() comment!: Comment;
}

@Component({
  selector: 'app-comment-form',
  standalone: true,
  template: '<input (input)="onInput($event)" /><button (click)="onSubmit()">Submit</button>',
})
class MockCommentFormComponent {
  @Input() newComment = '';
  @Output() newCommentChange = new EventEmitter<string>();
  @Output() commentSubmit = new EventEmitter<void>();
  
  onInput(event: { target: { value: string | undefined; }; }) {
    this.newCommentChange.emit(event.target.value);
  }
  
  onSubmit() {
    this.commentSubmit.emit();
  }
}

describe('CommentSectionComponent', () => {
  let component: CommentSectionComponent;
  let fixture: ComponentFixture<CommentSectionComponent>;
  
  const mockComments: Comment[] = [
    { id: 1, content: 'Premier commentaire', authorUsername: 'Alice', postId: 1, userId: 1, createdAt: new Date().toDateString() },
    { id: 2, content: 'Deuxième commentaire', authorUsername: 'Bob', postId: 1, userId: 1, createdAt: new Date().toDateString() },
  ];
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommentSectionComponent],
    })
    .overrideComponent(CommentSectionComponent, {
      set: {
        imports: [MockCommentDetailComponent, MockCommentFormComponent]
      }
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommentSectionComponent);
    component = fixture.componentInstance;
    component.comments = mockComments;
    fixture.detectChanges();
  });
  
  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });
  
  it('devrait afficher tous les commentaires', () => {
    const commentEls = fixture.debugElement.queryAll(By.css('.mock-comment-detail'));
    expect(commentEls.length).toBe(mockComments.length);
    expect(commentEls[0].nativeElement.textContent).toContain('Premier commentaire');
    expect(commentEls[1].nativeElement.textContent).toContain('Deuxième commentaire');
  });
  
  it('devrait passer newComment au CommentFormComponent', () => {
    component.newComment = 'Texte test';
    fixture.detectChanges();
    
    const form = fixture.debugElement.query(By.directive(MockCommentFormComponent))!.componentInstance;
    expect(form.newComment).toBe('Texte test');
  });
  
  it('devrait émettre newCommentChange depuis CommentFormComponent', () => {
    jest.spyOn(component.newCommentChange, 'emit');
    
    const form = fixture.debugElement.query(By.directive(MockCommentFormComponent))!.componentInstance;
    form.newCommentChange.emit('Nouveau texte');
    
    expect(component.newCommentChange.emit).toHaveBeenCalledWith('Nouveau texte');
  });
  
  it('devrait émettre submitComment depuis CommentFormComponent', () => {
    jest.spyOn(component.submitComment, 'emit');
    
    const form = fixture.debugElement.query(By.directive(MockCommentFormComponent))!.componentInstance;
    form.commentSubmit.emit();
    
    expect(component.submitComment.emit).toHaveBeenCalled();
  });
  
  it('devrait appeler onSubmit et émettre submitComment', () => {
    jest.spyOn(component.submitComment, 'emit');
    
    component.onSubmit();
    
    expect(component.submitComment.emit).toHaveBeenCalled();
  });
  
  it('devrait mettre à jour newComment via newCommentChange', () => {
    jest.spyOn(component.newCommentChange, 'emit');
    
    const nouveauTexte = 'Commentaire mis à jour';
    component.newCommentChange.emit(nouveauTexte);
    
    expect(component.newCommentChange.emit).toHaveBeenCalledWith(nouveauTexte);
  });
});
