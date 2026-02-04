import { usersFixtures } from '../../../../common/fixtures/user/users.fixtures.cy.ts';
import { newUserFixtures } from '../../../../common/fixtures/user/newUser.fixtures.cy.ts';

describe('Register page', () => {

  const user = usersFixtures[0];

  beforeEach(() => {
    cy.visit('/signin')
  })

  it('Should display register form correctly', () => {
    cy.get('h2').should('have.text', 'Inscription')
    cy.get('input[formControlName=userName]').should('exist')
    cy.get('input[formControlName=email]').should('exist')
    cy.get('input[formControlName=password]').should('exist')
  })

  it('Should register successfully and redirect to login', () => {
    cy.intercept('POST', '/api/auth/register', { statusCode: 200, body: {} }).as('register')
    cy.get('input[formControlName=userName]').type(newUserFixtures.username)
    cy.get('input[formControlName=email]').type(newUserFixtures.email)
    cy.get('input[formControlName=password]').type(newUserFixtures.password)
    cy.get('button[type=submit]').click()
    cy.wait('@register')
    cy.url().should('include', '/login')
  })

  it('Should display error message when register fails', () => {
    cy.intercept('POST', '/api/auth/register', { statusCode: 500 }).as('register')
    cy.get('input[formControlName=userName]').type(user.username)
    cy.get('input[formControlName=email]').type(user.email)
    cy.get('input[formControlName=password]').type(newUserFixtures.password)
    cy.get('button[type=submit]').click()
    cy.get('.error').should('be.visible').and('contain.text', "Une erreur est survenue")
    cy.url().should('include', '/signin')
  })

  it('Should display error when password not conain one uppercase letter', () => {
    cy.intercept('POST', '/api/auth/register', { statusCode: 500 }).as('register')
    cy.get('input[formControlName=userName]').type(newUserFixtures.username)
    cy.get('input[formControlName=email]').type(newUserFixtures.email)
    cy.get('input[formControlName=password]').type('test!1234')
    cy.get('button[type=submit]').click()
    cy.get('.error').should('be.visible').and('contain.text', "Password must contain at least one uppercase letter")

  })
})
