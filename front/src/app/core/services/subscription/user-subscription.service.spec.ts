import { of, throwError } from 'rxjs';
import { TestBed } from '@angular/core/testing';
import { UserSubscriptionService } from './user-subscription.service';
import { ToastService } from 'src/app/core/services/toast/toast.service';
import { SessionService } from 'src/app/core/api/services/auth/session.service';
import { Subscription } from 'src/app/core/models/subscription/subscription.interface';
import { SubscriptionService } from 'src/app/core/api/services/subscription/subscription.service';

describe('UserSubscriptionService', () => {
    let service: UserSubscriptionService;
    let subscriptionServiceSpy: {
        subscribeUser: jest.Mock;
        unSubscribeUser: jest.Mock;
    };
    
    let toastServiceSpy: {
        success: jest.Mock;
        error: jest.Mock;
    };
    let sessionServiceMock: Partial<SessionService>;
    
    beforeEach(() => {
        subscriptionServiceSpy = {
            subscribeUser: jest.fn(),
            unSubscribeUser: jest.fn()
        };
        
        toastServiceSpy = {
            success: jest.fn(),
            error: jest.fn()
        };
        
        sessionServiceMock = {
            sessionInformation: {
                id: 'user-123'
            } as any
        };
        
        TestBed.configureTestingModule({
            providers: [
                UserSubscriptionService,
                { provide: SubscriptionService, useValue: subscriptionServiceSpy },
                { provide: ToastService, useValue: toastServiceSpy },
                { provide: SessionService, useValue: sessionServiceMock }
            ]
        });
        
        service = TestBed.inject(UserSubscriptionService);
    });
    
    
    it('should be created', () => {
        expect(service).toBeTruthy();
    });
    
    describe('State management', () => {
        it('should set subscriptions', (done) => {
            const subs: Subscription[] = [
                { id: 1, name: 'Sujet 1', description: 'Desc 1' }
            ];
            
            service.subscriptions$.subscribe(value => {
                expect(value).toEqual(subs);
                done();
            });
            
            service.setSubscriptions(subs);
        });
    });
    
    describe('Subscribe modal', () => {
        it('should open subscribe modal with subject id', (done) => {
            service.showSubscribeModal$.subscribe(show => {
                expect(show).toBeTruthy();
                done();
            });
            
            service.openSubscribeModal(10);
        });
        
        it('should cancel subscribe modal', (done) => {
            service.openSubscribeModal(10);
            
            service.showSubscribeModal$.subscribe(show => {
                expect(show).toBeFalsy();
                done();
            });
            
            service.cancelSubscribe();
        });
    });
    
    describe('Unsubscribe modal', () => {
        it('should open unsubscribe modal with subject id', (done) => {
            service.showUnsubscribeModal$.subscribe(show => {
                expect(show).toBeTruthy();
                done();
            });
            
            service.openUnsubscribeModal(20);
        });
        
        it('should cancel unsubscribe modal', (done) => {
            service.openUnsubscribeModal(20);
            
            service.showUnsubscribeModal$.subscribe(show => {
                expect(show).toBeFalsy();
                done();
            });
            
            service.cancelUnsubscribe();
        });
    });
    
    describe('confirmSubscribe', () => {
        it('should not subscribe if subjectId is null', () => {
            service.confirmSubscribe();
            expect(subscriptionServiceSpy.subscribeUser).not.toHaveBeenCalled();
        });
        
        it('should subscribe user and update subscriptions', (done) => {
            subscriptionServiceSpy.subscribeUser.mockReturnValue(of({} as any));
            
            service.openSubscribeModal(10);
            
            service.subscriptions$.subscribe(subs => {
                if (subs.length === 1) {
                    expect(subs[0].id).toBe(10);
                    expect(toastServiceSpy.success).toBeTruthy();
                    done();
                }
            });
            
            service.confirmSubscribe();
        });
        
        it('should show error toast on subscribe error', () => {
            subscriptionServiceSpy.subscribeUser.mockReturnValue(
                throwError(() => new Error('error'))
            );
            
            service.openSubscribeModal(10);
            service.confirmSubscribe();
            
            expect(toastServiceSpy.error).toHaveBeenCalledWith(
                'Erreur lors de l’abonnement.'
            );
        });
    });
    
    describe('confirmUnsubscribe', () => {
        beforeEach(() => {
            service.setSubscriptions([
                { id: 10, name: 'Sujet 10', description: 'Desc' },
                { id: 20, name: 'Sujet 20', description: 'Desc' }
            ]);
        });
        
        it('should unsubscribe user and update subscriptions', (done) => {
            subscriptionServiceSpy.unSubscribeUser.mockReturnValue(of({} as any));
            
            service.openUnsubscribeModal(10);
            
            service.subscriptions$.subscribe(subs => {
                if (subs.length === 1) {
                    expect(subs[0].id).toBe(20);
                    expect(toastServiceSpy.success).toBeTruthy();
                    done();
                }
            });
            
            service.confirmUnsubscribe();
        });
        
        it('should show error toast on unsubscribe error', () => {
            subscriptionServiceSpy.unSubscribeUser.mockReturnValue(
                throwError(() => new Error('error'))
            );
            
            service.openUnsubscribeModal(10);
            service.confirmUnsubscribe();
            
            expect(toastServiceSpy.error).toHaveBeenCalledWith(
                'Erreur lors du désabonnement.'
            );
        });
        
        it('should not call unSubscribeUser if subjectId is null', () => {
            service['selectedUnsubscribeSubjectIdSubject'].next(null);
            
            service.confirmUnsubscribe();
            
            expect(subscriptionServiceSpy.unSubscribeUser).not.toHaveBeenCalled();
            expect(toastServiceSpy.success).not.toHaveBeenCalled();
            expect(toastServiceSpy.error).not.toHaveBeenCalled();
        });
        
        it('should not call unSubscribeUser if userId is null', () => {
            (service as any).sessionService.sessionInformation.id = null;
            service.openUnsubscribeModal(10);
            
            service.confirmUnsubscribe();
            
            expect(subscriptionServiceSpy.unSubscribeUser).not.toHaveBeenCalled();
            expect(toastServiceSpy.success).not.toHaveBeenCalled();
            expect(toastServiceSpy.error).not.toHaveBeenCalled();
        });
    });
    
    describe('Integration tests', () => {
        it('should subscribe then unsubscribe a subject', (done) => {
            subscriptionServiceSpy.subscribeUser.mockReturnValue(of({} as any));
            subscriptionServiceSpy.unSubscribeUser.mockReturnValue(of({} as any));
            
            service.openSubscribeModal(10);
            service.confirmSubscribe();
            
            service.subscriptions$.subscribe(subs => {
                if (subs.length === 1) {
                    expect(subs[0].id).toBe(10);
                    
                    service.openUnsubscribeModal(10);
                    service.confirmUnsubscribe();
                    
                    service.subscriptions$.subscribe(updated => {
                        if (updated.length === 0) {
                            expect(toastServiceSpy.success).toHaveBeenCalledTimes(2);
                            done();
                        }
                    });
                }
            });
        });
    });
});
