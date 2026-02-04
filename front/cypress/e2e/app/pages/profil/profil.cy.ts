import { usersFixtures } from '../../../../common/fixtures/user/users.fixtures.cy.ts';

describe('Profil page', () => {
    
    const user = usersFixtures[0];
    
    beforeEach(() => {
        cy.loginAndStoreSession()
        cy.visit('/profil');
    })
    
    it('Profil - Should swho profil page and see username and email', () => {
        cy.get("H2").contains("Profil utilisateur")
        cy.get('input[formControlName=username]').should('have.value', user.username)
        cy.get('input[formControlName=email]').should('have.value', user.email)
    })

    it('Profil - Should update username then re-update username with original', () => {
        cy.get('input[formControlName=username]').clear().type(user.username + '-test')
        cy.get('button[type=submit]').click()
        cy.get('div.toast').should('be.visible').and('contain.text', 'Profil mis à jour.')
        cy.get('input[formControlName=username]').should('have.value', user.username + '-test')
        cy.get('input[formControlName=username]').clear().type(user.username)
        cy.get('button[type=submit]').click()
        cy.get('div.toast').should('be.visible').and('contain.text', 'Profil mis à jour.')
        cy.get('input[formControlName=username]').should('have.value', user.username)
    })

    it('Profil - Should update email then re-update email with original', () => {
        cy.get('input[formControlName=email]').clear().type('testProfil@mail.com')
        cy.get('button[type=submit]').click()
        cy.get('div.toast').should('be.visible').and('contain.text', 'Profil mis à jour.')
        cy.get('input[formControlName=email]').should('have.value', 'testProfil@mail.com')
        cy.get('input[formControlName=email]').clear().type(user.email)
        cy.get('button[type=submit]').click()
        cy.get('div.toast').should('be.visible').and('contain.text', 'Profil mis à jour.')
        cy.get('input[formControlName=email]').should('have.value', user.email)
    })

    it('Profil - Should show erros when bad update email or usernamel', () => {
        cy.get('input[formControlName=email]').clear().type('testProfil')
        cy.get('button[type=submit]').click()
        cy.get('.error').should('be.visible').and('contain.text', "Le format de l’email est invalide.")
        cy.get('input[formControlName=email]').clear()
        cy.get('button[type=submit]').click()
        cy.get('.error').should('be.visible').and('contain.text', "L’email est obligatoire.")
        cy.get('input[formControlName=email]').clear().type(user.email)
        cy.get('input[formControlName=username]').clear()
        cy.get('button[type=submit]').click()
        cy.get('.error').should('be.visible').and('contain.text', "Le nom d’utilisateur est obligatoire.")
    })

    it('Profil - Should show erros when bad update pasword', () => {
        cy.get('input[formControlName=password]').clear().type('test')
        cy.get('button[type=submit]').click()
        cy.get('.error').should('be.visible').and('contain.text', "Password must be between 6 and 250 characters")
        cy.get('input[formControlName=password]').clear().type('ttttttttttt')
        cy.get('button[type=submit]').click()
        cy.get('.error').should('be.visible').and('contain.text', "Password must contain at least one uppercase letter; Password must contain at least one special character; Password must contain at least one digit")
    })
})
