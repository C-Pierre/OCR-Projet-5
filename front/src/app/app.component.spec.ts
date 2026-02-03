import { BehaviorSubject } from 'rxjs';
import { AppComponent } from './app.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SessionService } from './core/api/services/auth/session.service';
import { ToastComponent } from './components/elements/shared/toast/toast.component';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  let isLoggedSubject: BehaviorSubject<boolean>;
  let sessionServiceMock: Partial<SessionService>;

  beforeEach(async () => {
    isLoggedSubject = new BehaviorSubject<boolean>(false);

    sessionServiceMock = {
      isLogged$: isLoggedSubject.asObservable()
    };

    await TestBed.configureTestingModule({
      imports: [
        AppComponent,
        RouterTestingModule,
        ToastComponent
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  describe('isLogged getter', () => {
    it('should expose sessionService.isLogged$', (done) => {
      component.isLogged.subscribe(value => {
        expect(value).toBe(false);
        done();
      });
    });

    it('should emit true when user logs in', (done) => {
      const values: boolean[] = [];

      component.isLogged.subscribe(v => values.push(v));

      isLoggedSubject.next(true);

      expect(values).toEqual([false, true]);
      done();
    });
  });

  describe('Template', () => {
    it('should render router-outlet', () => {
      const element: HTMLElement = fixture.nativeElement;
      expect(element.querySelector('router-outlet')).toBeTruthy();
    });

    it('should render app-toast component', () => {
      const element: HTMLElement = fixture.nativeElement;
      expect(element.querySelector('app-toast')).toBeTruthy();
    });
  });

  describe('Integration tests', () => {
    it('should react to session state changes', (done) => {
      const emitted: boolean[] = [];

      component.isLogged.subscribe(value => emitted.push(value));

      isLoggedSubject.next(true);
      isLoggedSubject.next(false);

      expect(emitted).toEqual([false, true, false]);
      done();
    });
  });
});
