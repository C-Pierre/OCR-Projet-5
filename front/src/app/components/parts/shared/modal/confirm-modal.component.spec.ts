import { By } from '@angular/platform-browser';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConfirmModalComponent } from './confirm-modal.component';

describe('ConfirmModalComponent', () => {
  let component: ConfirmModalComponent;
  let fixture: ComponentFixture<ConfirmModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmModalComponent], // composant standalone
    }).compileComponents();

    fixture = TestBed.createComponent(ConfirmModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('devrait afficher le titre et le message par défaut', () => {
    const h3 = fixture.debugElement.query(By.css('h3')).nativeElement;
    const p = fixture.debugElement.query(By.css('p')).nativeElement;

    expect(h3.textContent).toBe('Confirmation');
    expect(p.textContent).toBe('Es-tu sûr ?');
  });

  it('devrait afficher le titre et le message passés en input', () => {
    component.title = 'Supprimer ?';
    component.message = 'Veux-tu vraiment supprimer cet item ?';
    fixture.detectChanges();

    const h3 = fixture.debugElement.query(By.css('h3')).nativeElement;
    const p = fixture.debugElement.query(By.css('p')).nativeElement;

    expect(h3.textContent).toBe('Supprimer ?');
    expect(p.textContent).toBe('Veux-tu vraiment supprimer cet item ?');
  });

  it('devrait émettre cancel quand on clique sur le bouton Annuler', () => {
    const spy = jest.spyOn(component.cancel, 'emit');

    const cancelBtn = fixture.debugElement.queryAll(By.css('app-button'))[0];
    cancelBtn.triggerEventHandler('click', null);

    expect(spy).toHaveBeenCalled();
  });

  it('devrait émettre confirm quand on clique sur le bouton Confirmer', () => {
    const spy = jest.spyOn(component.confirm, 'emit');

    const confirmBtn = fixture.debugElement.queryAll(By.css('app-button'))[1];
    confirmBtn.triggerEventHandler('click', null);

    expect(spy).toHaveBeenCalled();
  });
});
