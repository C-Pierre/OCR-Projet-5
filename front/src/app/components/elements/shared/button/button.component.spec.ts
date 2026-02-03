import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ButtonComponent } from './button.component';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';

describe('ButtonComponent', () => {
  let component: ButtonComponent;
  let fixture: ComponentFixture<ButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ButtonComponent, RouterTestingModule], // ← Ajouté
    }).compileComponents();

    fixture = TestBed.createComponent(ButtonComponent);
    component = fixture.componentInstance;
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('devrait afficher le titre par défaut', () => {
    fixture.detectChanges();
    const btn = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(btn.textContent.trim()).toBe('Button');
  });

  it('devrait afficher le titre passé en input', () => {
    component.title = 'Clique ici';
    fixture.detectChanges();
    const btn = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(btn.textContent.trim()).toBe('Clique ici');
  });

  it('devrait appliquer la classe btnClass', () => {
    component.btnClass = 'secondary';
    fixture.detectChanges();
    const btn = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(btn.classList).toContain('secondary');
  });

  it('devrait désactiver le bouton quand disabled=true', () => {
    component.disabled = true;
    fixture.detectChanges();
    const btn = fixture.debugElement.query(By.css('button')).nativeElement;
    expect(btn.disabled).toBe(true);
  });

  it('devrait rendre un lien quand type="a"', () => {
    component.type = 'a';
    component.routerLink = '/home';
    fixture.detectChanges();
    const a = fixture.debugElement.query(By.css('a')).nativeElement;
    expect(a).toBeTruthy();
  });

  it('devrait empêcher le clic si le lien est disabled', () => {
    component.type = 'a';
    component.routerLink = '/home';
    component.disabled = true;
    fixture.detectChanges();

    const a = fixture.debugElement.query(By.css('a')).nativeElement;
    const event = new MouseEvent('click');
    jest.spyOn(event, 'preventDefault');

    a.dispatchEvent(event);

    expect(event.preventDefault).toHaveBeenCalled();
  });
});
