/// <reference types="cypress" />

import { usersFixtures } from "../common/fixtures/user/users.fixtures.cy.ts";

Cypress.Commands.add('loginAndStoreSession', (user: any = usersFixtures[0]) => {
  cy.visit('/login');

  cy.intercept('POST', '/api/auth/login').as('login');

  cy.get('input[formControlName=identifier]').type(user.username);
  cy.get('input[formControlName=password]').type(user.password);
  cy.get('button[type=submit]').click();

  cy.wait('@login').then(({ response }) => {
    const tokenFromResponse = response?.body?.token;

    if (tokenFromResponse) {
      const userWithToken = { ...user, token: tokenFromResponse };

      cy.window().then(win => {
        win.localStorage.setItem('session', JSON.stringify(userWithToken));
      });
    } else {
      throw new Error("Login failed: token missing in response");
    }
  });
});
