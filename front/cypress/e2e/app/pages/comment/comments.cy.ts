import { postsFixtures } from '../../../../common/fixtures/post/posts.fixtures.cy.ts';
import { newPostFixtures } from '../../../../common/fixtures/post/newPost.fixtures.cy.ts';
import { commentsFixtures } from '../../../../common/fixtures/comment/comments.fixtures.cy.ts';

describe('Register page', () => {
    
    const post = postsFixtures[1];
    const comment = commentsFixtures[2]

    beforeEach(() => {
        cy.loginAndStoreSession()
        cy.visit('/feed')
    })

    it('Comments - Should see article comments and go back', () => {
        cy.get('h3').contains(post.title).click();
        cy.url().should('include', '/posts/' + post.id)
        cy.get('h1').contains(post.title)
        cy.get('span.author').contains("UserTwo")
        cy.get('div').contains(postsFixtures[1].content)
        cy.get('div.content').contains(comment.content)
    })

    it('Comments - Should not create comment when content is empty', () => {
        cy.visit('/posts/' + post.id)
        cy.get('h1').contains(post.title)
        cy.get('button[type=submit]').should('be.disabled')
    })

    it('Comments - Should create comment and see it in article page', () => {
        cy.visit('/posts/' + post.id)
        cy.get('h1').contains(post.title)
        cy.get('textarea[name=comment]').type(newPostFixtures.content)
        cy.get('button[type=submit]').click()
        cy.get('div.toast').should('be.visible')
        cy.get('div').contains(newPostFixtures.content)
    })
})
