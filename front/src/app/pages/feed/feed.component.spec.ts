import { of } from 'rxjs';
import { FeedComponent } from './feed.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostService } from 'src/app/core/api/services/post/post.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';

describe('FeedComponent', () => {
    let component: FeedComponent;
    let fixture: ComponentFixture<FeedComponent>;
    
    const mockRouter = { navigate: jest.fn() };
    const mockActivatedRoute = {};
    const mockPostService = { getAllForUser: jest.fn() };
    const mockSessionService = { sessionInformation: {} };
    
    const mockPosts = [ 
        { id: 1, title: 'B Post', content: "Content", subjectName: "Subject", authorUsername: 'Alice', subjectId: 1, createdAt: new Date() },
        { id: 2, title: 'A Post', content: "Content", subjectName: "Subject", authorUsername: 'Bob', subjectId: 1, createdAt: new Date() }, 
        { id: 3, title: 'C Post', content: "Content", subjectName: "Subject", authorUsername: 'Charlie', subjectId: 1, createdAt: new Date() },
    ];
    
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [FeedComponent],
            providers: [
                { provide: Router, useValue: mockRouter },
                { provide: ActivatedRoute, useValue: mockActivatedRoute },
                { provide: PostService, useValue: mockPostService },
                { provide: SessionService, useValue: mockSessionService },
            ],
            schemas: [NO_ERRORS_SCHEMA],
        }).compileComponents();
        
        fixture = TestBed.createComponent(FeedComponent);
        component = fixture.componentInstance;
        jest.clearAllMocks();
    });
    
    it('devrait créer le composant', () => {
        expect(component).toBeTruthy();
    });
    
    it('redirige vers "/" si l’utilisateur n’est pas connecté', () => {
        component.ngOnInit();
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
    });
    
    it('charge les posts pour l’utilisateur connecté et trie par date par défaut', () => {
        mockSessionService.sessionInformation = { id: 123 };
        mockPostService.getAllForUser.mockReturnValue(of(mockPosts));
        
        component.ngOnInit();
        
        expect(mockPostService.getAllForUser).toHaveBeenCalledWith("123");
        expect(component.posts.map(p => p.id)).toEqual([1, 2, 3]);
    });
    
    it('tri par titre', () => {
        component.posts = [...mockPosts];
        component.sortKey = 'title';
        component.sortPosts();
        expect(component.posts.map(p => p.title)).toEqual(['A Post', 'B Post', 'C Post']);
    });
    
    it('tri par auteur', () => {
        component.posts = [...mockPosts];
        component.sortKey = 'author';
        component.sortPosts();
        expect(component.posts.map(p => p.authorUsername)).toEqual(['Alice', 'Bob', 'Charlie']);
    });
    
    it('ne modifie pas l’ordre si sortKey n’est pas reconnu', () => {
        const postA = { id: 1, title: 'B Post', content: 'C', subjectName: "Subject", authorUsername: 'Bob', createdAt: new Date() };
        const postB = { id: 2, title: 'A Post', content: 'C', subjectName: "Subject", authorUsername: 'Alice', createdAt: new Date() };
        const postC = { id: 3, title: 'C Post', content: 'C', subjectName: "Subject", authorUsername: 'Charlie', createdAt: new Date() };
        
        component.posts = [postA, postB, postC];
        component.sortKey = 'unknown' as any;
        component.sortPosts();
        
        expect(component.posts).toEqual([postA, postB, postC]);
    });
});
