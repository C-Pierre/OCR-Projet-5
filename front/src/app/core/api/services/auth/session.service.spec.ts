import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInfo } from 'src/app/core/models/auth/sessionInfo.interface';

describe('SessionService', () => {
  let service: SessionService;
  let mockSessionInfo: SessionInfo;

  beforeEach(() => {
    localStorage.clear();

    TestBed.configureTestingModule({
      providers: [SessionService]
    });

    service = TestBed.inject(SessionService);

    mockSessionInfo = {
      token: 'fake-jwt-token',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      email: 'Test@mail.com'
    };
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('Initialization', () => {
    it('should initialize with no session', () => {
      expect(service.sessionInformation).toBeNull();
      expect(service.hasSession()).toBe(false);
    });

    it('should initialize isLogged$ as false when no session exists', (done) => {
      service.isLogged$.subscribe(isLogged => {
        expect(isLogged).toBe(false);
        done();
      });
    });

    it('should load session from localStorage on initialization', () => {
      // Arrange: put session in localStorage before creating service
      localStorage.setItem('session', JSON.stringify(mockSessionInfo));
      
      // Act: create new service instance
      const newService = new SessionService();
      
      // Assert
      expect(newService.sessionInformation).toEqual(mockSessionInfo);
      expect(newService.hasSession()).toBe(true);
    });

    it('should set isLogged$ to true when session exists in localStorage', (done) => {
      localStorage.setItem('session', JSON.stringify(mockSessionInfo));
      const newService = new SessionService();
      
      newService.isLogged$.subscribe(isLogged => {
        expect(isLogged).toBe(true);
        done();
      });
    });
  });

  describe('logIn', () => {
    it('should store session information', () => {
      service.logIn(mockSessionInfo);
      
      expect(service.sessionInformation).toEqual(mockSessionInfo);
    });

    it('should save session to localStorage', () => {
      service.logIn(mockSessionInfo);
      
      const storedSession = localStorage.getItem('session');
      expect(storedSession).toBeTruthy();
      expect(JSON.parse(storedSession!)).toEqual(mockSessionInfo);
    });

    it('should set isLogged$ to true', (done) => {
      service.logIn(mockSessionInfo);
      
      service.isLogged$.subscribe(isLogged => {
        expect(isLogged).toBe(true);
        done();
      });
    });

    it('should update hasSession to return true', () => {
      expect(service.hasSession()).toBe(false);
      
      service.logIn(mockSessionInfo);
      
      expect(service.hasSession()).toBe(true);
    });

    it('should override existing session', () => {
      const firstSession: SessionInfo = { ...mockSessionInfo, id: 1 };
      const secondSession: SessionInfo = { ...mockSessionInfo, id: 2 };
      
      service.logIn(firstSession);
      expect(service.sessionInformation?.id).toBe(1);
      
      service.logIn(secondSession);
      expect(service.sessionInformation?.id).toBe(2);
    });

    it('should emit isLogged$ observable when logging in', (done) => {
      const emissions: boolean[] = [];
      
      service.isLogged$.subscribe(isLogged => {
        emissions.push(isLogged);
        
        if (emissions.length === 2) {
          expect(emissions).toEqual([false, true]);
          done();
        }
      });
      
      service.logIn(mockSessionInfo);
    });
  });

  describe('logOut', () => {
    beforeEach(() => {
      service.logIn(mockSessionInfo);
    });

    it('should clear session information', () => {
      service.logOut();
      
      expect(service.sessionInformation).toBeNull();
    });

    it('should remove session from localStorage', () => {
      service.logOut();
      
      const storedSession = localStorage.getItem('session');
      expect(storedSession).toBeNull();
    });

    it('should set isLogged$ to false', (done) => {
      service.logOut();
      
      service.isLogged$.subscribe(isLogged => {
        expect(isLogged).toBe(false);
        done();
      });
    });

    it('should update hasSession to return false', () => {
      expect(service.hasSession()).toBe(true);
      
      service.logOut();
      
      expect(service.hasSession()).toBe(false);
    });

    it('should emit isLogged$ observable when logging out', (done) => {
      const emissions: boolean[] = [];
      
      service.isLogged$.subscribe(isLogged => {
        emissions.push(isLogged);
        
        if (emissions.length === 2) {
          expect(emissions).toEqual([true, false]);
          done();
        }
      });
      
      service.logOut();
    });

    it('should handle logout when no session exists', () => {
      service.logOut();
      service.logOut(); // Call twice
      
      expect(service.sessionInformation).toBeNull();
      expect(service.hasSession()).toBe(false);
    });
  });

  describe('sessionInformation getter', () => {
    it('should return null when no session exists', () => {
      expect(service.sessionInformation).toBeNull();
    });

    it('should return current session when logged in', () => {
      service.logIn(mockSessionInfo);
      
      expect(service.sessionInformation).toEqual(mockSessionInfo);
    });

    it('should return immutable reference to session', () => {
      service.logIn(mockSessionInfo);
      
      const session1 = service.sessionInformation;
      const session2 = service.sessionInformation;
      
      expect(session1).toBe(session2);
    });
  });

  describe('hasSession', () => {
    it('should return false when no session exists', () => {
      expect(service.hasSession()).toBe(false);
    });

    it('should return true when session exists', () => {
      service.logIn(mockSessionInfo);
      
      expect(service.hasSession()).toBe(true);
    });

    it('should return false after logout', () => {
      service.logIn(mockSessionInfo);
      service.logOut();
      
      expect(service.hasSession()).toBe(false);
    });
  });

  describe('isLogged$ Observable', () => {
    it('should be an observable', (done) => {
      expect(service.isLogged$.subscribe).toBeDefined();
      service.isLogged$.subscribe(() => done());
    });

    it('should emit current login state to new subscribers', (done) => {
      service.logIn(mockSessionInfo);
      
      service.isLogged$.subscribe(isLogged => {
        expect(isLogged).toBe(true);
        done();
      });
    });

    it('should emit multiple values through login/logout cycle', (done) => {
      const emissions: boolean[] = [];
      
      service.isLogged$.subscribe(isLogged => {
        emissions.push(isLogged);
        
        if (emissions.length === 3) {
          expect(emissions).toEqual([false, true, false]);
          done();
        }
      });
      
      service.logIn(mockSessionInfo);
      service.logOut();
    });
  });

  describe('localStorage integration', () => {
    it('should use correct storage key', () => {
      service.logIn(mockSessionInfo);
      
      expect(localStorage.getItem('session')).toBeTruthy();
      expect(localStorage.getItem('wrong-key')).toBeNull();
    });

    it('should persist session across service instances', () => {
      service.logIn(mockSessionInfo);
      
      const newService = new SessionService();
      
      expect(newService.sessionInformation).toEqual(mockSessionInfo);
      expect(newService.hasSession()).toBe(true);
    });

    it('should handle corrupted localStorage data gracefully', () => {
      localStorage.setItem('session', 'invalid-json');
      
      expect(() => new SessionService()).toThrow();
    });

    it('should store complete session object', () => {
      service.logIn(mockSessionInfo);
      
      const stored = JSON.parse(localStorage.getItem('session')!);
      
      expect(stored.token).toBe(mockSessionInfo.token);
      expect(stored.username).toBe(mockSessionInfo.username);
      expect(stored.email).toBe(mockSessionInfo.email);
    });
  });

  describe('Edge cases', () => {
    it('should handle empty session object', () => {
      const emptySession = {} as SessionInfo;
      
      service.logIn(emptySession);
      
      expect(service.hasSession()).toBe(true);
      expect(service.sessionInformation).toEqual(emptySession);
    });

    it('should handle rapid login/logout cycles', () => {
      for (let i = 0; i < 10; i++) {
        service.logIn({ ...mockSessionInfo, id: i });
        expect(service.hasSession()).toBe(true);
        service.logOut();
        expect(service.hasSession()).toBe(false);
      }
    });

    it('should load session from localStorage when creating new instance after login', () => {
        service.logIn(mockSessionInfo);
        expect(service.hasSession()).toBe(true);
        
        const newService = new SessionService();
        
        expect(newService.hasSession()).toBe(true);
        expect(newService.sessionInformation).toEqual(mockSessionInfo);
    });

    it('should have independent in-memory state between instances', () => {
        localStorage.clear();
        
        const service1 = new SessionService();
        const service2 = new SessionService();
        
        service1.logIn(mockSessionInfo);
        
        expect(service1.hasSession()).toBe(true);
        expect(service2.hasSession()).toBe(false);
        
        expect(localStorage.getItem('session')).toBeTruthy();
    });
  });
});