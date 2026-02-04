/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable<Subject = any> {
    /**
     * Custom command to login and store session
     * @example cy.loginAndStoreSession()
     */
    loginAndStoreSession(user?: any): Chainable<void>;
  }
}