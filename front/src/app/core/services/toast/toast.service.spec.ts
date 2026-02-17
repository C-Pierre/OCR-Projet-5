import { TestBed } from '@angular/core/testing';
import { ToastService, Toast } from './toast.service';

describe('ToastService', () => {
  let service: ToastService;

  beforeEach(() => {
    jest.useFakeTimers();

    TestBed.configureTestingModule({
      providers: [ToastService]
    });

    service = TestBed.inject(ToastService);
  });

  afterEach(() => {
    jest.clearAllTimers();
    jest.useRealTimers();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('Initial state', () => {
    it('should emit null initially', (done) => {
      service.toast$.subscribe(toast => {
        expect(toast).toBeNull();
        done();
      });
    });
  });

  describe('error()', () => {
    it('should emit error toast', (done) => {
      const message = 'Erreur test';

      service.toast$.subscribe(toast => {
        if (toast) {
          expect(toast).toEqual({
            message,
            type: 'error'
          });
          done();
        }
      });

      service.error(message);
    });
  });

  describe('success()', () => {
    it('should emit success toast', (done) => {
      const message = 'Succès test';

      service.toast$.subscribe(toast => {
        if (toast) {
          expect(toast).toEqual({
            message,
            type: 'success'
          });
          done();
        }
      });

      service.success(message);
    });
  });

  describe('Auto dismiss', () => {
    it('should clear toast after 3 seconds', () => {
      const values: (Toast | null)[] = [];

      service.toast$.subscribe(value => values.push(value));

      service.success('Auto dismiss test');

      expect(values[1]).toEqual({
        message: 'Auto dismiss test',
        type: 'success'
      });

      jest.advanceTimersByTime(3000);

      expect(values[2]).toBeNull();
    });
  });

  describe('Integration tests', () => {
    it('should replace previous toast with a new one', () => {
      const values: (Toast | null)[] = [];
      service.toast$.subscribe(v => values.push(v));

      service.success('First');
      service.error('Second');

      expect(values[1]).toEqual({
        message: 'First',
        type: 'success'
      });

      expect(values[2]).toEqual({
        message: 'Second',
        type: 'error'
      });
    });

    it('should auto dismiss the last toast only', () => {
      const values: (Toast | null)[] = [];
      service.toast$.subscribe(v => values.push(v));

      service.success('First');
      jest.advanceTimersByTime(1000);

      service.error('Second');
      jest.advanceTimersByTime(3000);

      expect(values[values.length - 1]).toBeNull();
    });
  });
});
