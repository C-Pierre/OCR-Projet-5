import { usersFixtures } from '../../../../common/fixtures/user/users.fixtures.cy.ts';

describe('Login spec', () => {

  it('Should login successfully and redirect to subjects', () => {
    cy.loginAndStoreSession(usersFixtures[0])
    cy.url().should('include', '/themes')
  })

  it('Should login and logout successfully', () => {
    cy.loginAndStoreSession(usersFixtures[0])
    cy.url().should('include', '/themes')
    cy.contains('span', 'Déconnexion').click()
    cy.url().should('include', '/')
  })

  it('Should display error message when login fails', () => {
    cy.visit('/login')
    cy.get('input[formControlName=identifier]').type('wrong@user.com')
    cy.get('input[formControlName=password]').type('badpassword')
    cy.get('button[type=submit]').click()
    cy.get('.error').should('be.visible').and('contain.text', 'Identifiants incorrects')
    cy.url().should('include', '/login')
    cy.window().then(win => {
      expect(win.localStorage.getItem('session')).to.be.null
    })
  })

  it('Should disable submit when email is missing or invalid', () => {
    cy.visit('/login')
    cy.get('input[formControlName=password]').type('test!1234')
    cy.get('input[formControlName=identifier]').clear()
    cy.get('button[type=submit]').click()
    cy.get('.error').should('be.visible').and('contain.text', 'E-mail ou nom d’utilisateur requis')
  })

  it('Should disable submit when password is missing', () => {
    cy.visit('/login')
    cy.get('input[formControlName=identifier]').type(usersFixtures[0].username)
    cy.get('button[type=submit]').click()
    cy.get('.error').should('be.visible').and('contain.text', 'Mot de passe requis')
  })
})
