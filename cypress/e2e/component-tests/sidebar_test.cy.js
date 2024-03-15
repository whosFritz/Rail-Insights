describe('dark_mode_toggle_test', () => {
    beforeEach(() => {
        cy.visit('http://127.0.1.1:8085')
    })

    it('ecxpect_sidebar_functions', () => {
        cy.resolveCookieBanner()
        cy.get('vaadin-drawer-toggle[aria-label$=\'panel\']').should('exist')
        cy.get('img[alt=\'Dynamic Theme Demo logo\']').should('exist')
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout').should('exist')

        // expect app-layout
        cy.get('vaadin-app-layout[primary-section=\'drawer\']').should('exist')
        // expect its opened
        cy.get('vaadin-app-layout[primary-section=\'drawer\']').should('have.attr', 'drawer-opened', '')

        cy.get('vaadin-drawer-toggle[aria-label$=\'panel\']').click()

        // vaadin-app layout should not have drawer-opened attribute
        cy.get('vaadin-app-layout[primary-section=\'drawer\']').should('not.have.attr', 'drawer-opened', '')
    })

    it('ecxpect_main_buttons', () => {
        cy.resolveCookieBanner()
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(1)').should('exist')
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(1)').contains('Home').should('exist')

        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(2)').should('exist')
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(2)').contains('Verspätungen').should('exist')

        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(3)').should('exist')
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(3)').contains('Zugstatistiken').should('exist')

        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(4)').should('exist')
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(4)').contains('Bahnhöfe').should('exist')

        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(5)').should('exist')
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(5)').contains('Verbindungsprognose').should('exist')

        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(6)').should('exist')
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-scroller > vaadin-vertical-layout > vaadin-side-nav:nth-of-type(1) > vaadin-side-nav-item:nth-of-type(6)').contains('CSV-Export').should('exist')
    })

    it('ecxpect_secondary_buttons', () => {
        cy.resolveCookieBanner()
        cy.get('vaadin-side-nav-item[path=\'https://github.com/whosFritz/Rail-Insights\']').should('exist')
        cy.get('vaadin-side-nav-item[path=\'impressum\']').should('exist')
        cy.get('vaadin-side-nav-item[path=\'impressum\']').should('exist')
        cy.get('vaadin-side-nav-item[path=\'datenschutzerklaerung\']').should('exist')
        cy.get('vaadin-side-nav-item[path=\'https://github.com/whosFritz/Rail-Insights/tree/master/RailInsights-API-Documentation\']').should('exist')
    })

    it('sidebar_intense', () => {
        cy.resolveCookieBanner()

        let i = 0
        for (let i = 0; i < 500; i++) {
            cy.get('vaadin-drawer-toggle[aria-label$=\'panel\']').click()
            if (i % 2 === 0) {
                cy.get('vaadin-app-layout[primary-section=\'drawer\']').should('not.have.attr', 'drawer-opened', '')
            } else {
                cy.get('vaadin-app-layout[primary-section=\'drawer\']').should('have.attr', 'drawer-opened', '')
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