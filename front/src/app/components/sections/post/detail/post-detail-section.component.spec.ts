import { By } from '@angular/platform-browser';
import { Component, Input } from '@angular/core';
import { Post } from 'src/app/core/models/post/post.interface';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostDetailSectionComponent } from './post-detail-section.component';

@Component({
    selector: 'app-post-detail',
    standalone: true,
    template: '<div class="mock-post-detail">{{post.title}}</div>',
})
class MockPostDetailComponent {
    @Input() post!: Post;
}

describe('PostDetailSectionComponent', () => {
    let component: PostDetailSectionComponent;
    let fixture: ComponentFixture<PostDetailSectionComponent>;
    
    const mockPost: Post = {
        id: 1,
        title: 'Titre du post',
        content: 'Contenu du post',
        authorUsername: "TestAuthor",
        subjectName: "TestSubject",
        createdAt: new Date(),
    };
    
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [PostDetailSectionComponent, MockPostDetailComponent],
        }).compileComponents();
        
        fixture = TestBed.createComponent(PostDetailSectionComponent);
        component = fixture.componentInstance;
        component.post = mockPost;
        fixture.detectChanges();
    });
    
    it('devrait créer le composant', () => {
        expect(component).toBeTruthy();
    });
    
    it('devrait afficher le titre du post', () => {
        const titleEl = fixture.debugElement.query(By.css('.post-title'));
        expect(titleEl).toBeTruthy();
        expect(titleEl.nativeElement.textContent).toContain('Titre du post');
    });
    
    it('devrait afficher la section du post si post est défini', () => {
        const section = fixture.debugElement.query(By.css('.post-section'));
        expect(section).not.toBeNull();
    });
    
    it('devrait ne pas afficher la section du post si post est undefined', () => {
        component.post = undefined as any;
        fixture.detectChanges();
        
        const section = fixture.debugElement.query(By.css('.post-section'));
        expect(section).toBeNull();
    });
});
