describe('home_page_test', () => {
    beforeEach(() => {
        cy.visit('http://127.0.1.1:8085')
    })

    it('home_page_test', () => {
        cy.resolveCookieBanner()

        // header exists
        cy.get('h1').contains('Willkommen').should('exist')

        // success badge exists
        cy.get('span[theme=\'badge success pill\']').should('exist')

        // the dark mode switch exists
        cy.get('#input-vaadin-checkbox-3').should('exist')

        // the dark mode switch is toogled
        cy.get('#input-vaadin-checkbox-3').click()

        // erfasste stopps exists
        cy.get('vaadin-vertical-layout[class=\'p-l border border-contrast-10 rounded-m\'] h2[class*=\'m-0\']').should('exist')

        // erfasste haltepunkte exists
        cy.get('vaadin-vertical-layout[class=\'p-l border border-contrast-10 rounded-m\'] h2[class*=\'m-0\']').should('exist')

        // haltepunkte für die stopps erfasst werden exists
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout > rail-insights-info-board > vaadin-board-row:nth-of-type(1) > vaadin-vertical-layout:nth-of-type(3) > h2').should('exist')

        // tagesaktuelle meldungen exists
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout > rail-insights-info-board > vaadin-vertical-layout > h2').should('exist')
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout > rail-insights-info-board > vaadin-vertical-layout > h2').contains('Tagesaktuelle Meldungen').should('exist')

        // stopps zeitverlauf exists
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout > rail-insights-info-board > vaadin-board-row:nth-of-type(2) > div:nth-of-type(1) > vaadin-vertical-layout > vaadin-chart').should('exist')

        // übersicht ausgefallener, verspäteter und pünktlicher stopps exists
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout > rail-insights-info-board > vaadin-board-row:nth-of-type(2) > div:nth-of-type(2) > vaadin-vertical-layout > vaadin-chart').should('exist')

        // pünktlichkeit- /ausfall- und verspätungsverteilung exists
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout > rail-insights-info-board > vaadin-board-row:nth-of-type(2) > div:nth-of-type(2) > vaadin-vertical-layout > vaadin-chart').should('exist')
    })

    it('dark_mode_toggle', () => {
        cy.resolveCookieBanner()
        cy.get('vaadin-checkbox[theme=\'toggle-button\']').should('exist')

        // click on the button
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'dark')
        cy.get('vaadin-checkbox[theme=\'toggle-button\']').click()
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'light')
        cy.get('vaadin-checkbox[theme=\'toggle-button\']').click()
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'dark')
        cy.get('vaadin-checkbox[theme=\'toggle-button\']').click()
        cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'light')

        let i = 0
        // toggle the button fifty times
        for (let i = 0; i < 100; i++) {
            cy.get('vaadin-checkbox[theme=\'toggle-button\']').click()
            if (i % 2 === 0) {
                cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'dark')
            } else {
                cy.get('html > body > div > flow-container-root-2521314 > vaadin-app-layout > vaadin-vertical-layout').should('have.css', 'color-scheme', 'light')
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
