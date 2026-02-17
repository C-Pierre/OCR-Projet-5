import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ButtonBackComponent } from './button-back.component';
import { By } from '@angular/platform-browser';

describe('ButtonBackComponent', () => {
  let component: ButtonBackComponent;
  let fixture: ComponentFixture<ButtonBackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ButtonBackComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ButtonBackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('devrait afficher un bouton', () => {
    const button = fixture.debugElement.query(By.css('button'));
    expect(button).toBeTruthy();
  });

  it('devrait contenir un icône "arrow_back"', () => {
    const icon = fixture.debugElement.query(By.css('mat-icon'));
    expect(icon.nativeElement.textContent.trim()).toBe('arrow_back');
  });

  it('devrait appeler window.history.back() au clic', () => {
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    const button = fixture.debugElement.query(By.css('button')).nativeElement;

    button.click();

    expect(spy).toHaveBeenCalled();
    spy.mockRestore();
  });
});
