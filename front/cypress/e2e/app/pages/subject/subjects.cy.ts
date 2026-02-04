import { subjectsFixtures } from '../../../../common/fixtures/subject/subjects.fixtures.cy.ts';

describe('Subjects/themes List page', () => {
    
    const subject = subjectsFixtures[2];
    
    beforeEach(() => {
        cy.loginAndStoreSession()
        cy.visit('/themes');
    })
    
    it('Should display subject information', () => {
        cy.get('h3').contains(subjectsFixtures[0].name).parent().find('p').contains(subjectsFixtures[0].description);
    })
    
    it('Should open modal when subscribe to subject and close modal when cancel', () => {
        cy.get('h3').contains(subject.name).parent().find('button').contains("S'abonner").click();
        cy.get('div.modal').should('be.visible')
        cy.contains('button', "Annuler").click()
        cy.contains('button', "S'abonner").click()
        cy.get('div.modal').should('be.visible')
    })
    
    it('Should subscribe and see subscription in profil page', () => {
        cy.get('h3').contains(subject.name).parent().find('button').contains("S'abonner").click();
        cy.get('div.modal').should('be.visible')
        cy.contains('button', "Confirmer").click()
        cy.get('div.toast').should('be.visible')
        cy.visit('/profil');
        cy.get('H2').and('contain.text', "Abonnements")
        cy.get('h3').contains(subject.name) 
    })
    
    it('Should unsubscribe new subscription and go pack to subjects page', () => {
        cy.contains('button', "Se désabonner").should('be.disabled')
        cy.visit('/profil');
        cy.get('H2').and('contain.text', "Abonnements")
        cy.get('h3').contains(subject.name).parent().find('button').click();     
        cy.get('div.modal').should('be.visible')
        cy.contains('button', "Confirmer").click()
        cy.get('div.toast').should('be.visible')
        cy.visit('/themes');
    })
})
