package de.whosfritz.railinsights.ui.components;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class InfoPanel extends VerticalLayout {

    public InfoPanel() {
        setId("info-panel");
        Paragraph welcomeText = new Paragraph("Hey! Herzlich Willkommen bei RailInsights. Hier findest du " +
                "interessante Statistiken und Daten rund um die Deutsche Bahn. Viel Spaß! :)");
        Paragraph infoText = new Paragraph("Die Daten werden alle 15 Minuten aktualisiert für eine Stunde " +
                "vorausschauend. Damit du ein möglichst flüssiges Ergebnis in der Navigation hast, aktualisieren wir " +
                "die Daten, in der Benutzeroberfläche, nur alle 20 Minuten. Ob du gerade einen aktuellen Stand siehst oder " +
                "gerade neue Daten geladen werden siehst du oben rechts an dem angezeigten Icon. Durch aktualisieren " +
                "der Seite, kannst du immer prüfen, ob du auf dem aktuellen Stand bist.In dem Navigationsmenü auf der " +
                "linken Seite findest du verschiedene Bereiche, die du dir anschauen kannst. Viel Spaß beim Erkunden!");
        Paragraph infoForCalculation = new Paragraph("Informationen zu den Berechnungen: Alle angezeigten Statistiken" +
                " werden nach Berechnungsgrundlage der Deutschen Bahn berechnet erstellt (wenn nicht anders angegeben)." +
                " Der Berechnungszeitraum ist immer auf der aktuellen Seite und/oder Statistik angegeben. " +
                " Die Daten werden nicht manuell kontrolliert und können daher von der Realität oder von Statistiken die" +
                " von der Deutschen Bahn veröffentlicht werden abweichen. Wenn du dich von der Qualität der Daten überzeugen" +
                " möchtest, kannst du gerne unseren Quellcode auf GitHub einsehen, die Daten als CSV herunterladen oder unsere REST-API nutzen.");

        add(welcomeText, infoText, infoForCalculation);
    }

}
