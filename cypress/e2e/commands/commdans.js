Cypress.Commands.add('resolveCookieBanner', () => {
    // Überprüfen, ob das Div-Element mit der Datenschutzerklärung vorhanden ist
    cy.get('div[class=\'p-m\']').contains('Datenschutzerklärung').should('exist');

    // Überprüfen, ob der Akzeptieren-Button vorhanden ist und darauf klicken
    cy.get('vaadin-button[tabindex=\'0\']').contains('Akzeptieren').should('exist').click();

    // Überprüfen, ob das Div-Element nicht mehr vorhanden ist
    cy.get('div[class=\'p-m\']').should('not.exist');
});