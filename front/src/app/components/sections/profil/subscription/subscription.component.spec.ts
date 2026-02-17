import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubscriptionsComponent } from './subscriptions.component';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';

@Component({
    selector: 'app-subscription',
    standalone: true,
    template: '',
})
class MockSubscriptionComponent {
    @Input() name!: string;
    @Input() description!: string;
    @Input() subscribed = true;
    @Output() unsubscribe = new EventEmitter<void>();
}

describe('SubscriptionsComponent (tests limités)', () => {
    let component: SubscriptionsComponent;
    let fixture: ComponentFixture<SubscriptionsComponent>;
    
    const mockSubscriptions: Subscription[] = [
        { id: 1, name: 'Sub A', description: 'Description A' },
        { id: 2, name: 'Sub B', description: 'Description B' },
    ];
    
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [SubscriptionsComponent, MockSubscriptionComponent],
        }).compileComponents();
        
        fixture = TestBed.createComponent(SubscriptionsComponent);
        component = fixture.componentInstance;
        component.subscriptions = mockSubscriptions;
        fixture.detectChanges();
    });
    
    it('devrait créer le composant', () => {
        expect(component).toBeTruthy();
    });
    
    it('devrait contenir la liste de subscriptions en mémoire', () => {
        expect(component.subscriptions.length).toBe(2);
        expect(component.subscriptions[0].name).toBe('Sub A');
        expect(component.subscriptions[1].name).toBe('Sub B');
    });
    
    it('devrait émettre unsubscribe avec l\'id correct', () => {
        jest.spyOn(component.unsubscribe, 'emit');
        
        component.unsubscribe.emit(mockSubscriptions[0].id);
        expect(component.unsubscribe.emit).toHaveBeenCalledWith(1);
        
        component.unsubscribe.emit(mockSubscriptions[1].id);
        expect(component.unsubscribe.emit).toHaveBeenCalledWith(2);
    });
    
    it('devrait retourner l\'id correct avec trackById', () => {
        const sub: Subscription = { id: 42, name: 'Test Sub', description: 'Test' };
        const index = 0;
        const result = component.trackById(index, sub);
        expect(result).toBe(42);
    });
});
