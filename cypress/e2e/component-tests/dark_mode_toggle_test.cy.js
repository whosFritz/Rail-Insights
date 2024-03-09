describe('dark_mode_toggle_test', () => {
    beforeEach(() => {
        cy.visit('http://127.0.1.1:8085')
    })

    it('simple_check', () => {
        cy.resolveCookieBanner()
        cy.get('vaadin-checkbox[theme=\'toggle-button\']').should('exist')

        // click on the button
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'dark')
        cy.get('img[alt=\'Dynamic Theme Demo logo\']').should('have.attr', 'src', 'images/darkmode.png')
        cy.get('vaadin-checkbox[theme=\'toggle-button\']').click()
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'light')
        cy.get('img[alt=\'Dynamic Theme Demo logo\']').should('have.attr', 'src', 'images/lightmode.png')
        cy.get('vaadin-checkbox[theme=\'toggle-button\']').click()
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'dark')
        cy.get('img[alt=\'Dynamic Theme Demo logo\']').should('have.attr', 'src', 'images/darkmode.png')
        cy.get('vaadin-checkbox[theme=\'toggle-button\']').click()
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'light')
        cy.get('img[alt=\'Dynamic Theme Demo logo\']').should('have.attr', 'src', 'images/lightmode.png')

    })

    it('intense_test', () => {
        cy.resolveCookieBanner()

        let i = 0
        // toggle the button fifty times
        for (let i = 0; i < 500; i++) {
            cy.get('vaadin-checkbox[theme=\'toggle-button\']').click()
            if (i % 2 === 0) {
                cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'light')
            } else {
                cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'dark')
            }
        }
    })

    Cypress.Commands.add('resolveCookieBanner', () => {
        // Überprüfen, ob das Div-Element mit der Datenschutzerklärung vorhanden ist
        cy.get('div[class=\'p-m\']').contains('Datenschutzerklärung').should('exist');

        // Überprüfen, ob der Akzeptieren-Button vorhanden ist und darauf klicken
        cy.get('vaadin-button[tabindex=\'0\']').contains('Akzeptieren').should('exist').click();

        // Überprüfen, ob das Div-Element nicht mehr vorhanden ist
        cy.get('div[class=\'p-m\']').should('not.exist');
    });

})