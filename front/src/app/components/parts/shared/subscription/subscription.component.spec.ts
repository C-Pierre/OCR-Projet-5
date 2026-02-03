import { By } from '@angular/platform-browser';
import { SubscriptionComponent } from './subscription.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';

describe('SubscriptionComponent', () => {
  let component: SubscriptionComponent;
  let fixture: ComponentFixture<SubscriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubscriptionComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SubscriptionComponent);
    component = fixture.componentInstance;

    component.name = 'Newsletter';
    component.description = 'Recevez nos dernières nouvelles';

    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('devrait afficher le nom et la description', () => {
    const h3 = fixture.debugElement.query(By.css('h3')).nativeElement;
    const p = fixture.debugElement.query(By.css('p')).nativeElement;

    expect(h3.textContent).toBe('Newsletter');
    expect(p.textContent).toBe('Recevez nos dernières nouvelles');
  });

  it('devrait afficher le bouton S\'abonner quand subscribed=false', () => {
    component.subscribed = false;
    fixture.detectChanges();

    const btn = fixture.debugElement.query(By.css('app-button')).nativeElement;
    expect(btn.title).toBe("S'abonner");
  });

  it('devrait afficher le bouton Se désabonner quand subscribed=true', () => {
    component.subscribed = true;
    fixture.detectChanges();

    const btn = fixture.debugElement.query(By.css('app-button')).nativeElement;
    expect(btn.title).toBe('Se désabonner');
  });

  it('devrait émettre subscribe quand on clique sur S\'abonner', () => {
    component.subscribed = false;
    fixture.detectChanges();

    const spy = jest.spyOn(component.subscribe, 'emit');

    const btn = fixture.debugElement.query(By.css('app-button'));
    btn.triggerEventHandler('click', null);

    expect(spy).toHaveBeenCalled();
  });

  it('devrait émettre unsubscribe quand on clique sur Se désabonner', () => {
    component.subscribed = true;
    fixture.detectChanges();

    const spy = jest.spyOn(component.unsubscribe, 'emit');

    const btn = fixture.debugElement.query(By.css('app-button'));
    btn.triggerEventHandler('click', null);

    expect(spy).toHaveBeenCalled();
  });
});
