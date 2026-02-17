import { postsFixtures } from '../../../../common/fixtures/post/posts.fixtures.cy.ts';
import { newPostFixtures } from '../../../../common/fixtures/post/newPost.fixtures.cy.ts';

describe('Register page', () => {
    
    beforeEach(() => {
        cy.loginAndStoreSession()
        cy.visit('/feed')
    })
    
    it('Posts / Feed - Should not acced when not authentificated', () => {
        cy.contains('button', "Déconnexion").click()
        cy.visit('/feed')
        cy.url().should('include', '/login')
    })
    
    it('Posts / Feed - Should see page and display articles after login', () => {
        cy.get('button').and('contain.text', "Créer un article")
        cy.get('h3').contains(postsFixtures[1].title);
    })

    it('Posts / Feed - Should see create article page and go back', () => {
        cy.contains('button', "Créer un article").click()
        cy.url().should('include', '/posts/create')
        cy.get('h2').contains("Créer un nouvel article");
        cy.contains('button', 'arrow_back').click()
        cy.url().should('include', '/feed')
    })

    it('Posts / Feed - Should see one article and go back', () => {
        cy.get('h3').contains(postsFixtures[1].title).click();
        cy.url().should('include', '/posts/' + postsFixtures[1].id)
        cy.contains('button', 'arrow_back').click()
        cy.url().should('include', '/feed')
    })

    it('Posts / Feed - Should create new article', () => {
        cy.contains('button', "Créer un article").click()
        cy.url().should('include', '/posts/create')
        cy.get('select[formControlName=subjectId]').select("Angular")
        cy.get('input[formControlName=title]').type(newPostFixtures.title)
        cy.get('textarea[formControlName=content]').type(newPostFixtures.content)
        cy.get('button[type=submit]').click()
        cy.get('div.toast').should('be.visible').contains("Article créé avec succès.")
        cy.url().should('include', '/feed')
    })

    it('Posts / Feed - Should see error when try create article without subject', () => {
        cy.contains('button', "Créer un article").click()
        cy.url().should('include', '/posts/create')
        cy.get('input[formControlName=title]').type(newPostFixtures.title)
        cy.get('textarea[formControlName=content]').type(newPostFixtures.content)
        cy.get('button[type=submit]').click()
        cy.get('div.toast').should('be.visible').contains("Veuillez sélectionner un sujet.")

    })

    it('Posts / Feed - Should see error when try create article without content', () => {
        cy.contains('button', "Créer un article").click()
        cy.url().should('include', '/posts/create')
        cy.get('select[formControlName=subjectId]').select("Angular")
        cy.get('input[formControlName=title]').type(newPostFixtures.title)
        cy.get('button[type=submit]').click()
        cy.get('div.toast').should('be.visible').contains("Erreur lors de la création de l'article.")
        cy.get('.error').should('be.visible').and('contain.text', "Content must not be blank")
    })

    it('Posts / Feed - Should see error when try create article without title', () => {
        cy.contains('button', "Créer un article").click()
        cy.url().should('include', '/posts/create')
        cy.get('select[formControlName=subjectId]').select("Angular")
        cy.get('textarea[formControlName=content]').type(newPostFixtures.content)
        cy.get('button[type=submit]').click()
        cy.get('div.toast').should('be.visible').contains("Erreur lors de la création de l'article.")
        cy.get('.error').should('be.visible').and('contain.text', "Title must not be blank")
    })
})
