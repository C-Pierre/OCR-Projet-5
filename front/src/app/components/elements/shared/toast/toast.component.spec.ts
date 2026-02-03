import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ToastComponent } from './toast.component';
import { BehaviorSubject } from 'rxjs';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { By } from '@angular/platform-browser';

interface Toast {
  type: 'success' | 'error';
  message: string;
}

describe('ToastComponent', () => {
  let component: ToastComponent;
  let fixture: ComponentFixture<ToastComponent>;

  let toastSubject: BehaviorSubject<Toast | null>;
  let toastServiceMock: Partial<ToastService>;

  beforeEach(async () => {
    toastSubject = new BehaviorSubject<Toast | null>(null);

    toastServiceMock = {
      toast$: toastSubject.asObservable(),
    };

    await TestBed.configureTestingModule({
      imports: [ToastComponent],
      providers: [
        { provide: ToastService, useValue: toastServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ToastComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('ne devrait rien afficher si toast$ est null', () => {
    const toastEl = fixture.debugElement.query(By.css('.toast'));
    expect(toastEl).toBeNull();
  });

  it('devrait afficher un toast success', () => {
    toastSubject.next({ type: 'success', message: 'Opération réussie' });
    fixture.detectChanges();

    const toastEl = fixture.debugElement.query(By.css('.toast')).nativeElement;
    expect(toastEl.textContent).toContain('Opération réussie');
    expect(toastEl.classList).toContain('success');
    expect(toastEl.classList).not.toContain('error');
  });

  it('devrait afficher un toast error', () => {
    toastSubject.next({ type: 'error', message: 'Une erreur est survenue' });
    fixture.detectChanges();

    const toastEl = fixture.debugElement.query(By.css('.toast')).nativeElement;
    expect(toastEl.textContent).toContain('Une erreur est survenue');
    expect(toastEl.classList).toContain('error');
    expect(toastEl.classList).not.toContain('success');
  });

  it('devrait masquer le toast si toast$ repasse à null', () => {
    toastSubject.next({ type: 'success', message: 'Opération réussie' });
    fixture.detectChanges();

    toastSubject.next(null);
    fixture.detectChanges();

    const toastEl = fixture.debugElement.query(By.css('.toast'));
    expect(toastEl).toBeNull();
  });
});
