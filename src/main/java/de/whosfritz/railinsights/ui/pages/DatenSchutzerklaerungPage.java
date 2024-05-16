package de.whosfritz.railinsights.ui.pages;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.ui.layout.MainView;
import de.whosfritz.railinsights.ui.pages.AbstractAnimationViewClass.AbstractAnimationViewClass;

@Route(value = "datenschutzerklaerung", layout = MainView.class)
public class DatenSchutzerklaerungPage extends VerticalLayout implements AbstractAnimationViewClass {

    public DatenSchutzerklaerungPage() {
        add(new Html("<div>\n" +
                "<h1>Datenschutzerklärung</h1>\n" +
                "<h2 id=\"m716\">Präambel</h2>\n" +
                "<p>Mit der folgenden Datenschutzerklärung möchten wir Sie darüber aufklären, welche Arten Ihrer personenbezogenen Daten\n" +
                "    (nachfolgend auch kurz als \"Daten\" bezeichnet) wir zu welchen Zwecken und in welchem Umfang verarbeiten. Die\n" +
                "    Datenschutzerklärung gilt für alle von uns durchgeführten Verarbeitungen personenbezogener Daten, sowohl im Rahmen\n" +
                "    der Erbringung unserer Leistungen als auch insbesondere auf unseren Webseiten, in mobilen Applikationen sowie\n" +
                "    innerhalb externer Onlinepräsenzen, wie z. B. unserer Social-Media-Profile (nachfolgend zusammenfassend bezeichnet\n" +
                "    als \"Onlineangebot\").</p>\n" +
                "<p>Die verwendeten Begriffe sind nicht geschlechtsspezifisch.</p>\n" +
                "\n" +
                "<p>Stand: 30. Januar 2024</p>\n" +
                "<h2>Inhaltsübersicht</h2>\n" +
                "<ul class=\"index\">\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m716\">Präambel</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m3\">Verantwortlicher</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#mOverview\">Übersicht der Verarbeitungen</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m2427\">Maßgebliche Rechtsgrundlagen</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m27\">Sicherheitsmaßnahmen</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m25\">Übermittlung von personenbezogenen Daten</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m24\">Internationale Datentransfers</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m10\">Rechte der betroffenen Personen</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m134\">Einsatz von Cookies</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m225\">Bereitstellung des Onlineangebotes und Webhosting</a>\n" +
                "    </li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m263\">Webanalyse, Monitoring und Optimierung</a></li>\n" +
                "    <li><a class=\"index-link\" href=\"/datenschutzerklaerung#m328\">Plugins und eingebettete Funktionen sowie Inhalte</a>\n" +
                "    </li>\n" +
                "</ul>\n" +
                "<h2 id=\"m3\">Verantwortlicher</h2>\n" +
                "<p>Fritz Schubert<br>Schiebestraße, 4a<br>04129, Leipzig, Deutschland</p>\n" +
                "<p>\n" +
                "    E-Mail-Adresse: <a href=\"mailto:fritzschubert21@outlook.de\">fritzschubert21@outlook.de</a>\n" +
                "</p>\n" +
                "<p>\n" +
                "    Impressum: <a href=\"/impressum\" target=\"_blank\">https://www.railinsights.de/impressum</a>\n" +
                "</p>\n" +
                "<h2 id=\"mOverview\">Übersicht der Verarbeitungen</h2>\n" +
                "<p>Die nachfolgende Übersicht fasst die Arten der verarbeiteten Daten und die Zwecke ihrer Verarbeitung zusammen und\n" +
                "    verweist auf die betroffenen Personen.</p>\n" +
                "<h3>Arten der verarbeiteten Daten</h3>\n" +
                "<ul>\n" +
                "    <li>Standortdaten.</li>\n" +
                "    <li>Inhaltsdaten.</li>\n" +
                "    <li>Nutzungsdaten.</li>\n" +
                "    <li>Meta-, Kommunikations- und Verfahrensdaten.</li>\n" +
                "</ul>\n" +
                "<h3>Kategorien betroffener Personen</h3>\n" +
                "<ul>\n" +
                "    <li>Nutzer.</li>\n" +
                "</ul>\n" +
                "<h3>Zwecke der Verarbeitung</h3>\n" +
                "<ul>\n" +
                "    <li>Sicherheitsmaßnahmen.</li>\n" +
                "    <li>Reichweitenmessung.</li>\n" +
                "    <li>Profile mit nutzerbezogenen Informationen.</li>\n" +
                "    <li>Bereitstellung unseres Onlineangebotes und Nutzerfreundlichkeit.</li>\n" +
                "    <li>Informationstechnische Infrastruktur.</li>\n" +
                "</ul>\n" +
                "<h2 id=\"m2427\">Maßgebliche Rechtsgrundlagen</h2>\n" +
                "<p><strong>Maßgebliche Rechtsgrundlagen nach der DSGVO: </strong>Im Folgenden erhalten Sie eine Übersicht der\n" +
                "    Rechtsgrundlagen der DSGVO, auf deren Basis wir personenbezogene Daten verarbeiten. Bitte nehmen Sie zur Kenntnis,\n" +
                "    dass neben den Regelungen der DSGVO nationale Datenschutzvorgaben in Ihrem bzw. unserem Wohn- oder Sitzland gelten\n" +
                "    können. Sollten ferner im Einzelfall speziellere Rechtsgrundlagen maßgeblich sein, teilen wir Ihnen diese in der\n" +
                "    Datenschutzerklärung mit.</p>\n" +
                "<ul>\n" +
                "    <li><strong>Einwilligung (Art. 6 Abs. 1 S. 1 lit. a) DSGVO)</strong> - Die betroffene Person hat ihre Einwilligung\n" +
                "        in die Verarbeitung der sie betreffenden personenbezogenen Daten für einen spezifischen Zweck oder mehrere\n" +
                "        bestimmte Zwecke gegeben.\n" +
                "    </li>\n" +
                "    <li><strong>Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO)</strong> - Die Verarbeitung ist zur Wahrung\n" +
                "        der berechtigten Interessen des Verantwortlichen oder eines Dritten erforderlich, sofern nicht die Interessen\n" +
                "        oder Grundrechte und Grundfreiheiten der betroffenen Person, die den Schutz personenbezogener Daten erfordern,\n" +
                "        überwiegen.\n" +
                "    </li>\n" +
                "</ul>\n" +
                "<p><strong>Nationale Datenschutzregelungen in Deutschland: </strong>Zusätzlich zu den Datenschutzregelungen der DSGVO\n" +
                "    gelten nationale Regelungen zum Datenschutz in Deutschland. Hierzu gehört insbesondere das Gesetz zum Schutz vor\n" +
                "    Missbrauch personenbezogener Daten bei der Datenverarbeitung (Bundesdatenschutzgesetz – BDSG). Das BDSG enthält\n" +
                "    insbesondere Spezialregelungen zum Recht auf Auskunft, zum Recht auf Löschung, zum Widerspruchsrecht, zur\n" +
                "    Verarbeitung besonderer Kategorien personenbezogener Daten, zur Verarbeitung für andere Zwecke und zur Übermittlung\n" +
                "    sowie automatisierten Entscheidungsfindung im Einzelfall einschließlich Profiling. Ferner können\n" +
                "    Landesdatenschutzgesetze der einzelnen Bundesländer zur Anwendung gelangen.</p>\n" +
                "<p><strong>Hinweis auf Geltung DSGVO und Schweizer DSG: </strong>Diese Datenschutzhinweise dienen sowohl der\n" +
                "    Informationserteilung nach dem schweizerischen Bundesgesetz über den Datenschutz (Schweizer DSG) als auch nach der\n" +
                "    Datenschutzgrundverordnung (DSGVO). Aus diesem Grund bitten wir Sie zu beachten, dass aufgrund der breiteren\n" +
                "    räumlichen Anwendung und Verständlichkeit die Begriffe der DSGVO verwendet werden. Insbesondere statt der im\n" +
                "    Schweizer DSG verwendeten Begriffe „Bearbeitung\" von „Personendaten\", \"überwiegendes Interesse\" und \"besonders\n" +
                "    schützenswerte Personendaten\" werden die in der DSGVO verwendeten Begriffe „Verarbeitung\" von „personenbezogenen\n" +
                "    Daten\" sowie \"berechtigtes Interesse\" und \"besondere Kategorien von Daten\" verwendet. Die gesetzliche Bedeutung der\n" +
                "    Begriffe wird jedoch im Rahmen der Geltung des Schweizer DSG weiterhin nach dem Schweizer DSG bestimmt.</p>\n" +
                "\n" +
                "<h2 id=\"m27\">Sicherheitsmaßnahmen</h2>\n" +
                "<p>Wir treffen nach Maßgabe der gesetzlichen Vorgaben unter Berücksichtigung des Stands der Technik, der\n" +
                "    Implementierungskosten und der Art, des Umfangs, der Umstände und der Zwecke der Verarbeitung sowie der\n" +
                "    unterschiedlichen Eintrittswahrscheinlichkeiten und des Ausmaßes der Bedrohung der Rechte und Freiheiten natürlicher\n" +
                "    Personen geeignete technische und organisatorische Maßnahmen, um ein dem Risiko angemessenes Schutzniveau zu\n" +
                "    gewährleisten.</p>\n" +
                "<p>Zu den Maßnahmen gehören insbesondere die Sicherung der Vertraulichkeit, Integrität und Verfügbarkeit von Daten durch\n" +
                "    Kontrolle des physischen und elektronischen Zugangs zu den Daten als auch des sie betreffenden Zugriffs, der\n" +
                "    Eingabe, der Weitergabe, der Sicherung der Verfügbarkeit und ihrer Trennung. Des Weiteren haben wir Verfahren\n" +
                "    eingerichtet, die eine Wahrnehmung von Betroffenenrechten, die Löschung von Daten und Reaktionen auf die Gefährdung\n" +
                "    der Daten gewährleisten. Ferner berücksichtigen wir den Schutz personenbezogener Daten bereits bei der Entwicklung\n" +
                "    bzw. Auswahl von Hardware, Software sowie Verfahren entsprechend dem Prinzip des Datenschutzes, durch\n" +
                "    Technikgestaltung und durch datenschutzfreundliche Voreinstellungen.</p>\n" +
                "<p>Kürzung der IP-Adresse: Sofern IP-Adressen von uns oder von den eingesetzten Dienstleistern und Technologien\n" +
                "    verarbeitet werden und die Verarbeitung einer vollständigen IP-Adresse nicht erforderlich ist, wird die IP-Adresse\n" +
                "    gekürzt (auch als \"IP-Masking\" bezeichnet). Hierbei werden die letzten beiden Ziffern, bzw. der letzte Teil der\n" +
                "    IP-Adresse nach einem Punkt entfernt, bzw. durch Platzhalter ersetzt. Mit der Kürzung der IP-Adresse soll die\n" +
                "    Identifizierung einer Person anhand ihrer IP-Adresse verhindert oder wesentlich erschwert werden.</p>\n" +
                "<p>TLS/SSL-Verschlüsselung (https): Um die Daten der Benutzer, die über unsere Online-Dienste übertragen werden, zu\n" +
                "    schützen, verwenden wir TLS/SSL-Verschlüsselung. Secure Sockets Layer (SSL) ist die Standardtechnologie zur\n" +
                "    Sicherung von Internetverbindungen durch Verschlüsselung der zwischen einer Website oder App und einem Browser (oder\n" +
                "    zwischen zwei Servern) übertragenen Daten. Transport Layer Security (TLS) ist eine aktualisierte und sicherere\n" +
                "    Version von SSL. Hyper Text Transfer Protocol Secure (HTTPS) wird in der URL angezeigt, wenn eine Website durch ein\n" +
                "    SSL/TLS-Zertifikat gesichert ist.</p>\n" +
                "\n" +
                "<h2 id=\"m25\">Übermittlung von personenbezogenen Daten</h2>\n" +
                "<p>Im Rahmen unserer Verarbeitung von personenbezogenen Daten kommt es vor, dass die Daten an andere Stellen,\n" +
                "    Unternehmen, rechtlich selbstständige Organisationseinheiten oder Personen übermittelt oder sie ihnen gegenüber\n" +
                "    offengelegt werden. Zu den Empfängern dieser Daten können z. B. mit IT-Aufgaben beauftragte Dienstleister oder\n" +
                "    Anbieter von Diensten und Inhalten, die in eine Webseite eingebunden werden, gehören. In solchen Fällen beachten wir\n" +
                "    die gesetzlichen Vorgaben und schließen insbesondere entsprechende Verträge bzw. Vereinbarungen, die dem Schutz\n" +
                "    Ihrer Daten dienen, mit den Empfängern Ihrer Daten ab.</p>\n" +
                "\n" +
                "<h2 id=\"m24\">Internationale Datentransfers</h2>\n" +
                "<p>Datenverarbeitung in Drittländern: Sofern wir Daten in einem Drittland (d. h., außerhalb der Europäischen Union (EU),\n" +
                "    des Europäischen Wirtschaftsraums (EWR)) verarbeiten oder die Verarbeitung im Rahmen der Inanspruchnahme von\n" +
                "    Diensten Dritter oder der Offenlegung bzw. Übermittlung von Daten an andere Personen, Stellen oder Unternehmen\n" +
                "    stattfindet, erfolgt dies nur im Einklang mit den gesetzlichen Vorgaben. Sofern das Datenschutzniveau in dem\n" +
                "    Drittland mittels eines Angemessenheitsbeschlusses anerkannt wurde (Art. 45 DSGVO), dient dieser als Grundlage des\n" +
                "    Datentransfers. Im Übrigen erfolgen Datentransfers nur dann, wenn das Datenschutzniveau anderweitig gesichert ist,\n" +
                "    insbesondere durch Standardvertragsklauseln (Art. 46 Abs. 2 lit. c) DSGVO), ausdrückliche Einwilligung oder im Fall\n" +
                "    vertraglicher oder gesetzlich erforderlicher Übermittlung (Art. 49 Abs. 1 DSGVO). Im Übrigen teilen wir Ihnen die\n" +
                "    Grundlagen der Drittlandübermittlung bei den einzelnen Anbietern aus dem Drittland mit, wobei die\n" +
                "    Angemessenheitsbeschlüsse als Grundlagen vorrangig gelten. Informationen zu Drittlandtransfers und vorliegenden\n" +
                "    Angemessenheitsbeschlüssen können dem Informationsangebot der EU-Kommission entnommen werden: <a\n" +
                "            href=\"https://commission.europa.eu/law/law-topic/data-protection/international-dimension-data-protection_en?prefLang=de\"\n" +
                "            target=\"_blank\">https://commission.europa.eu/law/law-topic/data-protection/international-dimension-data-protection_en?prefLang=de.</a>\n" +
                "</p>\n" +
                "<p>EU-US Trans-Atlantic Data Privacy Framework: Im Rahmen des sogenannten „Data Privacy Framework\" (DPF) hat die\n" +
                "    EU-Kommission das Datenschutzniveau ebenfalls für bestimmte Unternehmen aus den USA im Rahmen der\n" +
                "    Angemessenheitsbeschlusses vom 10.07.2023 als sicher anerkannt. Die Liste der zertifizierten Unternehmen als auch\n" +
                "    weitere Informationen zu dem DPF können Sie der Webseite des Handelsministeriums der USA unter <a\n" +
                "            href=\"https://www.dataprivacyframework.gov/\" target=\"_blank\">https://www.dataprivacyframework.gov/</a> (in\n" +
                "    Englisch) entnehmen. Wir informieren Sie im Rahmen der Datenschutzhinweise, welche von uns eingesetzten\n" +
                "    Diensteanbieter unter dem Data Privacy Framework zertifiziert sind.</p>\n" +
                "\n" +
                "<h2 id=\"m10\">Rechte der betroffenen Personen</h2>\n" +
                "<p>Rechte der betroffenen Personen aus der DSGVO: Ihnen stehen als Betroffene nach der DSGVO verschiedene Rechte zu, die\n" +
                "    sich insbesondere aus Art. 15 bis 21 DSGVO ergeben:</p>\n" +
                "<ul>\n" +
                "    <li><strong>Widerspruchsrecht: Sie haben das Recht, aus Gründen, die sich aus Ihrer besonderen Situation ergeben,\n" +
                "        jederzeit gegen die Verarbeitung der Sie betreffenden personenbezogenen Daten, die aufgrund von Art. 6 Abs. 1\n" +
                "        lit. e oder f DSGVO erfolgt, Widerspruch einzulegen; dies gilt auch für ein auf diese Bestimmungen gestütztes\n" +
                "        Profiling. Werden die Sie betreffenden personenbezogenen Daten verarbeitet, um Direktwerbung zu betreiben, haben\n" +
                "        Sie das Recht, jederzeit Widerspruch gegen die Verarbeitung der Sie betreffenden personenbezogenen Daten zum\n" +
                "        Zwecke derartiger Werbung einzulegen; dies gilt auch für das Profiling, soweit es mit solcher Direktwerbung in\n" +
                "        Verbindung steht.</strong></li>\n" +
                "    <li><strong>Widerrufsrecht bei Einwilligungen:</strong> Sie haben das Recht, erteilte Einwilligungen jederzeit zu\n" +
                "        widerrufen.\n" +
                "    </li>\n" +
                "    <li><strong>Auskunftsrecht:</strong> Sie haben das Recht, eine Bestätigung darüber zu verlangen, ob betreffende\n" +
                "        Daten verarbeitet werden und auf Auskunft über diese Daten sowie auf weitere Informationen und Kopie der Daten\n" +
                "        entsprechend den gesetzlichen Vorgaben.\n" +
                "    </li>\n" +
                "    <li><strong>Recht auf Berichtigung:</strong> Sie haben entsprechend den gesetzlichen Vorgaben das Recht, die\n" +
                "        Vervollständigung der Sie betreffenden Daten oder die Berichtigung der Sie betreffenden unrichtigen Daten zu\n" +
                "        verlangen.\n" +
                "    </li>\n" +
                "    <li><strong>Recht auf Löschung und Einschränkung der Verarbeitung:</strong> Sie haben nach Maßgabe der gesetzlichen\n" +
                "        Vorgaben das Recht, zu verlangen, dass Sie betreffende Daten unverzüglich gelöscht werden, bzw. alternativ nach\n" +
                "        Maßgabe der gesetzlichen Vorgaben eine Einschränkung der Verarbeitung der Daten zu verlangen.\n" +
                "    </li>\n" +
                "    <li><strong>Recht auf Datenübertragbarkeit:</strong> Sie haben das Recht, Sie betreffende Daten, die Sie uns\n" +
                "        bereitgestellt haben, nach Maßgabe der gesetzlichen Vorgaben in einem strukturierten, gängigen und\n" +
                "        maschinenlesbaren Format zu erhalten oder deren Übermittlung an einen anderen Verantwortlichen zu fordern.\n" +
                "    </li>\n" +
                "    <li><strong>Beschwerde bei Aufsichtsbehörde:</strong> Sie haben unbeschadet eines anderweitigen\n" +
                "        verwaltungsrechtlichen oder gerichtlichen Rechtsbehelfs das Recht auf Beschwerde bei einer Aufsichtsbehörde,\n" +
                "        insbesondere in dem Mitgliedstaat ihres gewöhnlichen Aufenthaltsorts, ihres Arbeitsplatzes oder des Orts des\n" +
                "        mutmaßlichen Verstoßes, wenn Sie der Ansicht sind, dass die Verarbeitung der Sie betreffenden personenbezogenen\n" +
                "        Daten gegen die Vorgaben der DSGVO verstößt.\n" +
                "    </li>\n" +
                "</ul>\n" +
                "\n" +
                "<h2 id=\"m134\">Einsatz von Cookies</h2>\n" +
                "<p>Cookies sind kleine Textdateien, bzw. sonstige Speichervermerke, die Informationen auf Endgeräten speichern und\n" +
                "    Informationen aus den Endgeräten auslesen. Z. B. um den Login-Status in einem Nutzerkonto, einen Warenkorbinhalt in\n" +
                "    einem E-Shop, die aufgerufenen Inhalte oder verwendete Funktionen eines Onlineangebotes speichern. Cookies können\n" +
                "    ferner zu unterschiedlichen Zwecken eingesetzt werden, z. B. zu Zwecken der Funktionsfähigkeit, Sicherheit und\n" +
                "    Komfort von Onlineangeboten sowie der Erstellung von Analysen der Besucherströme. </p>\n" +
                "<p><strong>Hinweise zur Einwilligung: </strong>Wir setzen Cookies im Einklang mit den gesetzlichen Vorschriften ein.\n" +
                "    Daher holen wir von den Nutzern eine vorhergehende Einwilligung ein, außer wenn diese gesetzlich nicht gefordert\n" +
                "    ist. Eine Einwilligung ist insbesondere nicht notwendig, wenn das Speichern und das Auslesen der Informationen, also\n" +
                "    auch von Cookies, unbedingt erforderlich sind, um dem den Nutzern einen von ihnen ausdrücklich gewünschten\n" +
                "    Telemediendienst (also unser Onlineangebot) zur Verfügung zu stellen. Zu den unbedingt erforderlichen Cookies\n" +
                "    gehören in der Regel Cookies mit Funktionen, die der Anzeige und Lauffähigkeit des Onlineangebotes , dem\n" +
                "    Lastausgleich, der Sicherheit, der Speicherung der Präferenzen und Auswahlmöglichkeiten der Nutzer oder ähnlichen\n" +
                "    mit der Bereitstellung der Haupt- und Nebenfunktionen des von den Nutzern angeforderten Onlineangebotes\n" +
                "    zusammenhängenden Zwecken dienen. Die widerrufliche Einwilligung wird gegenüber den Nutzern deutlich kommuniziert\n" +
                "    und enthält die Informationen zu der jeweiligen Cookie-Nutzung.</p>\n" +
                "<p><strong>Hinweise zu datenschutzrechtlichen Rechtsgrundlagen: </strong>Auf welcher datenschutzrechtlichen\n" +
                "    Rechtsgrundlage wir die personenbezogenen Daten der Nutzer mit Hilfe von Cookies verarbeiten, hängt davon ab, ob wir\n" +
                "    Nutzer um eine Einwilligung bitten. Falls die Nutzer einwilligen, ist die Rechtsgrundlage der Verarbeitung Ihrer\n" +
                "    Daten die erklärte Einwilligung. Andernfalls werden die mithilfe von Cookies verarbeiteten Daten auf Grundlage\n" +
                "    unserer berechtigten Interessen (z. B. an einem betriebswirtschaftlichen Betrieb unseres Onlineangebotes und\n" +
                "    Verbesserung seiner Nutzbarkeit) verarbeitet oder, wenn dies im Rahmen der Erfüllung unserer vertraglichen Pflichten\n" +
                "    erfolgt, wenn der Einsatz von Cookies erforderlich ist, um unsere vertraglichen Verpflichtungen zu erfüllen. Zu\n" +
                "    welchen Zwecken die Cookies von uns verarbeitet werden, darüber klären wir im Laufe dieser Datenschutzerklärung oder\n" +
                "    im Rahmen von unseren Einwilligungs- und Verarbeitungsprozessen auf.</p>\n" +
                "<p><strong>Speicherdauer: </strong>Im Hinblick auf die Speicherdauer werden die folgenden Arten von Cookies\n" +
                "    unterschieden:</p>\n" +
                "<ul>\n" +
                "    <li><strong>Temporäre Cookies (auch: Session- oder Sitzungs-Cookies):</strong> Temporäre Cookies werden spätestens\n" +
                "        gelöscht, nachdem ein Nutzer ein Online-Angebot verlassen und sein Endgerät (z. B. Browser oder mobile\n" +
                "        Applikation) geschlossen hat.\n" +
                "    </li>\n" +
                "    <li><strong>Permanente Cookies:</strong> Permanente Cookies bleiben auch nach dem Schließen des Endgerätes\n" +
                "        gespeichert. So können beispielsweise der Login-Status gespeichert oder bevorzugte Inhalte direkt angezeigt\n" +
                "        werden, wenn der Nutzer eine Website erneut besucht. Ebenso können die mit Hilfe von Cookies erhobenen Daten der\n" +
                "        Nutzer zur Reichweitenmessung verwendet werden. Sofern wir Nutzern keine expliziten Angaben zur Art und\n" +
                "        Speicherdauer von Cookies mitteilen (z. B. im Rahmen der Einholung der Einwilligung), sollten Nutzer davon\n" +
                "        ausgehen, dass Cookies permanent sind und die Speicherdauer bis zu zwei Jahre betragen kann.\n" +
                "    </li>\n" +
                "</ul>\n" +
                "<p><strong>Allgemeine Hinweise zum Widerruf und Widerspruch (sog. \"Opt-Out\"): </strong>Nutzer können die von ihnen\n" +
                "    abgegebenen Einwilligungen jederzeit widerrufen und der Verarbeitung entsprechend den gesetzlichen Vorgaben\n" +
                "    widersprechen. Hierzu können Nutzer unter anderem die Verwendung von Cookies in den Einstellungen ihres Browsers\n" +
                "    einschränken (wobei dadurch auch die Funktionalität unseres Onlineangebotes eingeschränkt sein kann). Ein\n" +
                "    Widerspruch gegen die Verwendung von Cookies zu Online-Marketing-Zwecken kann auch über die Websites <a\n" +
                "            href=\"https://optout.aboutads.info/\" target=\"_new\">https://optout.aboutads.info</a> und <a\n" +
                "            href=\"https://www.youronlinechoices.com/\" target=\"_new\">https://www.youronlinechoices.com/</a> erklärt\n" +
                "    werden.</p>\n" +
                "<ul class=\"m-elements\">\n" +
                "    <li class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO).\n" +
                "        Einwilligung (Art. 6 Abs. 1 S. 1 lit. a) DSGVO).\n" +
                "    </li>\n" +
                "</ul>\n" +
                "<p><strong>Weitere Hinweise zu Verarbeitungsprozessen, Verfahren und Diensten:</strong></p>\n" +
                "<ul class=\"m-elements\">\n" +
                "    <li><strong>Verarbeitung von Cookie-Daten auf Grundlage einer Einwilligung: </strong>Wir setzen ein Verfahren zum\n" +
                "        Einwilligungsmanagement: Verfahren zur Einholung, Protokollierung, Verwaltung und des Widerrufs von\n" +
                "        Einwilligungen, insbesondere für den Einsatz von Cookies und ähnlichen Technologien zur Speicherung, Auslesen\n" +
                "        und Verarbeitung von Informationen auf Endgeräten der Nutzer sowie deren Verarbeitung ein, in dessen Rahmen die\n" +
                "        Einwilligungen der Nutzer in den Einsatz von Cookies, bzw. der im Rahmen des Einwilligungsmanagement: Verfahren\n" +
                "        zur Einholung, Protokollierung, Verwaltung und des Widerrufs von Einwilligungen, insbesondere für den Einsatz\n" +
                "        von Cookies und ähnlichen Technologien zur Speicherung, Auslesen und Verarbeitung von Informationen auf\n" +
                "        Endgeräten der Nutzer sowie deren Verarbeitung-Verfahrens genannten Verarbeitungen und Anbieter eingeholt sowie\n" +
                "        von den Nutzern verwaltet und widerrufen werden können. Hierbei wird die Einwilligungserklärung gespeichert, um\n" +
                "        deren Abfrage nicht erneut wiederholen zu müssen und die Einwilligung entsprechend der gesetzlichen\n" +
                "        Verpflichtung nachweisen zu können. Die Speicherung kann serverseitig und/oder in einem Cookie (sogenanntes\n" +
                "        Opt-In-Cookie, bzw. mithilfe vergleichbarer Technologien) erfolgen, um die Einwilligung einem Nutzer, bzw.\n" +
                "        dessen Gerät zuordnen zu können. Vorbehaltlich individueller Angaben zu den Anbietern von\n" +
                "        Cookie-Management-Diensten, gelten die folgenden Hinweise: Die Dauer der Speicherung der Einwilligung kann bis\n" +
                "        zu zwei Jahren betragen. Hierbei wird ein pseudonymer Nutzer-Identifikator gebildet und mit dem Zeitpunkt der\n" +
                "        Einwilligung, Angaben zur Reichweite der Einwilligung (z. B. welche Kategorien von Cookies und/oder\n" +
                "        Diensteanbieter) sowie dem Browser, System und verwendeten Endgerät gespeichert; <span class=\"\"><strong>Rechtsgrundlagen:</strong> Einwilligung (Art. 6 Abs. 1 S. 1 lit. a) DSGVO).</span>\n" +
                "    </li>\n" +
                "</ul>\n" +
                "<h2 id=\"m225\">Bereitstellung des Onlineangebotes und Webhosting</h2>\n" +
                "<p>Wir verarbeiten die Daten der Nutzer, um ihnen unsere Online-Dienste zur Verfügung stellen zu können. Zu diesem Zweck\n" +
                "    verarbeiten wir die IP-Adresse des Nutzers, die notwendig ist, um die Inhalte und Funktionen unserer Online-Dienste\n" +
                "    an den Browser oder das Endgerät der Nutzer zu übermitteln.</p>\n" +
                "<ul class=\"m-elements\">\n" +
                "    <li><strong>Verarbeitete Datenarten:</strong> Nutzungsdaten (z. B. besuchte Webseiten, Interesse an Inhalten,\n" +
                "        Zugriffszeiten); Meta-, Kommunikations- und Verfahrensdaten (z. B. IP-Adressen, Zeitangaben,\n" +
                "        Identifikationsnummern, Einwilligungsstatus); Inhaltsdaten (z. .B. Eingaben in Onlineformularen).\n" +
                "    </li>\n" +
                "    <li><strong>Betroffene Personen:</strong> Nutzer (z. .B. Webseitenbesucher, Nutzer von Onlinediensten).</li>\n" +
                "    <li><strong>Zwecke der Verarbeitung:</strong> Bereitstellung unseres Onlineangebotes und Nutzerfreundlichkeit;\n" +
                "        Informationstechnische Infrastruktur (Betrieb und Bereitstellung von Informationssystemen und technischen\n" +
                "        Geräten (Computer, Server etc.).). Sicherheitsmaßnahmen.\n" +
                "    </li>\n" +
                "    <li class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO).</li>\n" +
                "</ul>\n" +
                "<p><strong>Weitere Hinweise zu Verarbeitungsprozessen, Verfahren und Diensten:</strong></p>\n" +
                "<ul class=\"m-elements\">\n" +
                "    <li><strong>Bereitstellung Onlineangebot auf gemietetem Speicherplatz: </strong>Für die Bereitstellung unseres\n" +
                "        Onlineangebotes nutzen wir Speicherplatz, Rechenkapazität und Software, die wir von einem entsprechenden\n" +
                "        Serveranbieter (auch \"Webhoster\" genannt) mieten oder anderweitig beziehen; <span class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO).</span>\n" +
                "    </li>\n" +
                "    <li><strong>Erhebung von Zugriffsdaten und Logfiles: </strong>Der Zugriff auf unser Onlineangebot wird in Form von\n" +
                "        so genannten \"Server-Logfiles\" protokolliert. Zu den Serverlogfiles können die Adresse und Name der abgerufenen\n" +
                "        Webseiten und Dateien, Datum und Uhrzeit des Abrufs, übertragene Datenmengen, Meldung über erfolgreichen Abruf,\n" +
                "        Browsertyp nebst Version, das Betriebssystem des Nutzers, Referrer URL (die zuvor besuchte Seite) und im\n" +
                "        Regelfall IP-Adressen und der anfragende Provider gehören. Die Serverlogfiles können zum einen zu Zwecken der\n" +
                "        Sicherheit eingesetzt werden, z. B., um eine Überlastung der Server zu vermeiden (insbesondere im Fall von\n" +
                "        missbräuchlichen Angriffen, sogenannten DDoS-Attacken) und zum anderen, um die Auslastung der Server und ihre\n" +
                "        Stabilität sicherzustellen; <span class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO). </span><strong>Löschung\n" +
                "            von Daten:</strong> Logfile-Informationen werden für die Dauer von maximal 30 Tagen gespeichert und danach\n" +
                "        gelöscht oder anonymisiert. Daten, deren weitere Aufbewahrung zu Beweiszwecken erforderlich ist, sind bis zur\n" +
                "        endgültigen Klärung des jeweiligen Vorfalls von der Löschung ausgenommen.\n" +
                "    </li>\n" +
                "    <li><strong>E-Mail-Versand und -Hosting: </strong>Die von uns in Anspruch genommenen Webhosting-Leistungen umfassen\n" +
                "        ebenfalls den Versand, den Empfang sowie die Speicherung von E-Mails. Zu diesen Zwecken werden die Adressen der\n" +
                "        Empfänger sowie Absender als auch weitere Informationen betreffend den E-Mailversand (z. B. die beteiligten\n" +
                "        Provider) sowie die Inhalte der jeweiligen E-Mails verarbeitet. Die vorgenannten Daten können ferner zu Zwecken\n" +
                "        der Erkennung von SPAM verarbeitet werden. Wir bitten darum, zu beachten, dass E-Mails im Internet grundsätzlich\n" +
                "        nicht verschlüsselt versendet werden. Im Regelfall werden E-Mails zwar auf dem Transportweg verschlüsselt, aber\n" +
                "        (sofern kein sogenanntes Ende-zu-Ende-Verschlüsselungsverfahren eingesetzt wird) nicht auf den Servern, von\n" +
                "        denen sie abgesendet und empfangen werden. Wir können daher für den Übertragungsweg der E-Mails zwischen dem\n" +
                "        Absender und dem Empfang auf unserem Server keine Verantwortung übernehmen; <span class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO).</span>\n" +
                "    </li>\n" +
                "    <li><strong>STRATO: </strong>Leistungen auf dem Gebiet der Bereitstellung von informationstechnischer Infrastruktur\n" +
                "        und verbundenen Dienstleistungen (z. B. Speicherplatz und/oder Rechenkapazitäten);\n" +
                "        <strong>Dienstanbieter:</strong> STRATO AG, Pascalstraße 10,10587 Berlin, Deutschland; <span class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO); </span><strong>Website:</strong>\n" +
                "        <a href=\"https://www.strato.de\" target=\"_blank\">https://www.strato.de</a>;\n" +
                "        <strong>Datenschutzerklärung:</strong> <a href=\"https://www.strato.de/datenschutz/\" target=\"_blank\">https://www.strato.de/datenschutz/</a>.\n" +
                "        <strong>Auftragsverarbeitungsvertrag:</strong> Wird vom Dienstanbieter bereitgestellt.\n" +
                "    </li>\n" +
                "</ul>\n" +
                "<h2 id=\"m263\">Webanalyse, Monitoring und Optimierung</h2>\n" +
                "<p>Die Webanalyse (auch als \"Reichweitenmessung\" bezeichnet) dient der Auswertung der Besucherströme unseres\n" +
                "    Onlineangebotes und kann Verhalten, Interessen oder demographische Informationen zu den Besuchern, wie z. B. das\n" +
                "    Alter oder das Geschlecht, als pseudonyme Werte umfassen. Mit Hilfe der Reichweitenanalyse können wir z. B.\n" +
                "    erkennen, zu welcher Zeit unser Onlineangebot oder dessen Funktionen oder Inhalte am häufigsten genutzt werden oder\n" +
                "    zur Wiederverwendung einladen. Ebenso können wir nachvollziehen, welche Bereiche der Optimierung bedürfen. </p>\n" +
                "<p>Neben der Webanalyse können wir auch Testverfahren einsetzen, um z. B. unterschiedliche Versionen unseres\n" +
                "    Onlineangebotes oder seiner Bestandteile zu testen und optimieren.</p>\n" +
                "<p>Sofern nachfolgend nicht anders angegeben, können zu diesen Zwecken Profile, d. h. zu einem Nutzungsvorgang\n" +
                "    zusammengefasste Daten angelegt und Informationen in einem Browser, bzw. in einem Endgerät gespeichert und aus\n" +
                "    diesem ausgelesen werden. Zu den erhobenen Angaben gehören insbesondere besuchte Webseiten und dort genutzte\n" +
                "    Elemente sowie technische Angaben, wie der verwendete Browser, das verwendete Computersystem sowie Angaben zu\n" +
                "    Nutzungszeiten. Sofern Nutzer in die Erhebung ihrer Standortdaten uns gegenüber oder gegenüber den Anbietern der von\n" +
                "    uns eingesetzten Dienste einverstanden erklärt haben, können auch Standortdaten verarbeitet werden.</p>\n" +
                "<p>Es werden ebenfalls die IP-Adressen der Nutzer gespeichert. Jedoch nutzen wir ein IP-Masking-Verfahren (d. h.,\n" +
                "    Pseudonymisierung durch Kürzung der IP-Adresse) zum Schutz der Nutzer. Generell werden die im Rahmen von Webanalyse,\n" +
                "    A/B-Testings und Optimierung keine Klardaten der Nutzer (wie z. B. E-Mail-Adressen oder Namen) gespeichert, sondern\n" +
                "    Pseudonyme. D. h., wir als auch die Anbieter der eingesetzten Software kennen nicht die tatsächliche Identität der\n" +
                "    Nutzer, sondern nur den für Zwecke der jeweiligen Verfahren in deren Profilen gespeicherten Angaben.</p>\n" +
                "<ul class=\"m-elements\">\n" +
                "    <li><strong>Verarbeitete Datenarten:</strong> Nutzungsdaten (z. B. besuchte Webseiten, Interesse an Inhalten,\n" +
                "        Zugriffszeiten); Meta-, Kommunikations- und Verfahrensdaten (z. .B. IP-Adressen, Zeitangaben,\n" +
                "        Identifikationsnummern, Einwilligungsstatus).\n" +
                "    </li>\n" +
                "    <li><strong>Betroffene Personen:</strong> Nutzer (z. .B. Webseitenbesucher, Nutzer von Onlinediensten).</li>\n" +
                "    <li><strong>Zwecke der Verarbeitung:</strong> Reichweitenmessung (z. B. Zugriffsstatistiken, Erkennung\n" +
                "        wiederkehrender Besucher). Profile mit nutzerbezogenen Informationen (Erstellen von Nutzerprofilen).\n" +
                "    </li>\n" +
                "    <li><strong>Sicherheitsmaßnahmen:</strong> IP-Masking (Pseudonymisierung der IP-Adresse).</li>\n" +
                "</ul>\n" +
                "<h2 id=\"m328\">Plugins und eingebettete Funktionen sowie Inhalte</h2>\n" +
                "<p>Wir binden in unser Onlineangebot Funktions- und Inhaltselemente ein, die von den Servern ihrer jeweiligen Anbieter\n" +
                "    (nachfolgend bezeichnet als \"Drittanbieter\") bezogen werden. Dabei kann es sich zum Beispiel um Grafiken, Videos\n" +
                "    oder Stadtpläne handeln (nachfolgend einheitlich bezeichnet als \"Inhalte\").</p>\n" +
                "<p>Die Einbindung setzt immer voraus, dass die Drittanbieter dieser Inhalte die IP-Adresse der Nutzer verarbeiten, da\n" +
                "    sie ohne die IP-Adresse die Inhalte nicht an deren Browser senden könnten. Die IP-Adresse ist damit für die\n" +
                "    Darstellung dieser Inhalte oder Funktionen erforderlich. Wir bemühen uns, nur solche Inhalte zu verwenden, deren\n" +
                "    jeweilige Anbieter die IP-Adresse lediglich zur Auslieferung der Inhalte verwenden. Drittanbieter können ferner\n" +
                "    sogenannte Pixel-Tags (unsichtbare Grafiken, auch als \"Web Beacons\" bezeichnet) für statistische oder\n" +
                "    Marketingzwecke verwenden. Durch die \"Pixel-Tags\" können Informationen, wie der Besucherverkehr auf den Seiten\n" +
                "    dieser Webseite, ausgewertet werden. Die pseudonymen Informationen können ferner in Cookies auf dem Gerät der Nutzer\n" +
                "    gespeichert werden und unter anderem technische Informationen zum Browser und zum Betriebssystem, zu verweisenden\n" +
                "    Webseiten, zur Besuchszeit sowie weitere Angaben zur Nutzung unseres Onlineangebotes enthalten als auch mit solchen\n" +
                "    Informationen aus anderen Quellen verbunden werden.</p>\n" +
                "<ul class=\"m-elements\">\n" +
                "    <li><strong>Verarbeitete Datenarten:</strong> Nutzungsdaten (z. B. besuchte Webseiten, Interesse an Inhalten,\n" +
                "        Zugriffszeiten); Meta-, Kommunikations- und Verfahrensdaten (z. B. IP-Adressen, Zeitangaben,\n" +
                "        Identifikationsnummern, Einwilligungsstatus). Standortdaten (Angaben zur geografischen Position eines Gerätes\n" +
                "        oder einer Person).\n" +
                "    </li>\n" +
                "    <li><strong>Betroffene Personen:</strong> Nutzer (z. .B. Webseitenbesucher, Nutzer von Onlinediensten).</li>\n" +
                "    <li><strong>Zwecke der Verarbeitung:</strong> Bereitstellung unseres Onlineangebotes und Nutzerfreundlichkeit.</li>\n" +
                "    <li class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO).\n" +
                "        Einwilligung (Art. 6 Abs. 1 S. 1 lit. a) DSGVO).\n" +
                "    </li>\n" +
                "</ul>\n" +
                "<p><strong>Weitere Hinweise zu Verarbeitungsprozessen, Verfahren und Diensten:</strong></p>\n" +
                "<ul class=\"m-elements\">\n" +
                "    <li><strong>Einbindung von Drittsoftware, Skripten oder Frameworks (z. B. jQuery): </strong>Wir binden in unser\n" +
                "        Onlineangebot Software ein, die wir von Servern anderer Anbieter abrufen (z. B. Funktions-Bibliotheken, die wir\n" +
                "        zwecks Darstellung oder Nutzerfreundlichkeit unseres Onlineangebotes verwenden). Hierbei erheben die jeweiligen\n" +
                "        Anbieter die IP-Adresse der Nutzer und können diese zu Zwecken der Übermittlung der Software an den Browser der\n" +
                "        Nutzer sowie zu Zwecken der Sicherheit, als auch zur Auswertung und Optimierung ihres Angebotes verarbeiten. -\n" +
                "        Wir binden in unser Onlineangebot Software ein, die wir von Servern anderer Anbieter abrufen (z. B.\n" +
                "        Funktions-Bibliotheken, die wir zwecks Darstellung oder Nutzerfreundlichkeit unseres Onlineangebotes verwenden).\n" +
                "        Hierbei erheben die jeweiligen Anbieter die IP-Adresse der Nutzer und können diese zu Zwecken der Übermittlung\n" +
                "        der Software an den Browser der Nutzer sowie zu Zwecken der Sicherheit, als auch zur Auswertung und Optimierung\n" +
                "        ihres Angebotes verarbeiten; <span class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO).</span>\n" +
                "    </li>\n" +
                "    <li><strong>Google Fonts (Bereitstellung auf eigenem Server): </strong>Bereitstellung von Schriftarten-Dateien\n" +
                "        zwecks einer nutzerfreundlichen Darstellung unseres Onlineangebotes; <strong>Dienstanbieter:</strong> Die Google\n" +
                "        Fonts werden auf unserem Server gehostet, es werden keine Daten an Google übermittelt; <span class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO).</span>\n" +
                "    </li>\n" +
                "    <li><strong>Google Fonts (Bezug vom Google Server): </strong>Bezug von Schriften (und Symbolen) zum Zwecke einer\n" +
                "        technisch sicheren, wartungsfreien und effizienten Nutzung von Schriften und Symbolen im Hinblick auf Aktualität\n" +
                "        und Ladezeiten, deren einheitliche Darstellung und Berücksichtigung möglicher lizenzrechtlicher Beschränkungen.\n" +
                "        Dem Anbieter der Schriftarten wird die IP-Adresse des Nutzers mitgeteilt, damit die Schriftarten im Browser des\n" +
                "        Nutzers zur Verfügung gestellt werden können. Darüber hinaus werden technische Daten (Spracheinstellungen,\n" +
                "        Bildschirmauflösung, Betriebssystem, verwendete Hardware) übermittelt, die für die Bereitstellung der Schriften\n" +
                "        in Abhängigkeit von den verwendeten Geräten und der technischen Umgebung notwendig sind. Diese Daten können auf\n" +
                "        einem Server des Anbieters der Schriftarten in den USA verarbeitet werden - Beim Besuch unseres Onlineangebotes\n" +
                "        senden die Browser der Nutzer ihre Browser HTTP-Anfragen an die Google Fonts Web API (d. h. eine\n" +
                "        Softwareschnittstelle für den Abruf der Schriftarten). Die Google Fonts Web API stellt den Nutzern die Cascading\n" +
                "        Style Sheets (CSS) von Google Fonts und danach die in der CCS angegebenen Schriftarten zur Verfügung. Zu diesen\n" +
                "        HTTP-Anfragen gehören (1) die vom jeweiligen Nutzer für den Zugriff auf das Internet verwendete IP-Adresse, (2)\n" +
                "        die angeforderte URL auf dem Google-Server und (3) die HTTP-Header, einschließlich des User-Agents, der die\n" +
                "        Browser- und Betriebssystemversionen der Websitebesucher beschreibt, sowie die Verweis-URL (d. h. die Webseite,\n" +
                "        auf der die Google-Schriftart angezeigt werden soll). IP-Adressen werden weder auf Google-Servern protokolliert\n" +
                "        noch gespeichert und sie werden nicht analysiert. Die Google Fonts Web API protokolliert Details der\n" +
                "        HTTP-Anfragen (angeforderte URL, User-Agent und Verweis-URL). Der Zugriff auf diese Daten ist eingeschränkt und\n" +
                "        streng kontrolliert. Die angeforderte URL identifiziert die Schriftfamilien, für die der Nutzer Schriftarten\n" +
                "        laden möchte. Diese Daten werden protokolliert, damit Google bestimmen kann, wie oft eine bestimmte\n" +
                "        Schriftfamilie angefordert wird. Bei der Google Fonts Web API muss der User-Agent die Schriftart anpassen, die\n" +
                "        für den jeweiligen Browsertyp generiert wird. Der User-Agent wird in erster Linie zum Debugging protokolliert\n" +
                "        und verwendet, um aggregierte Nutzungsstatistiken zu generieren, mit denen die Beliebtheit von Schriftfamilien\n" +
                "        gemessen wird. Diese zusammengefassten Nutzungsstatistiken werden auf der Seite „Analysen\" von Google Fonts\n" +
                "        veröffentlicht. Schließlich wird die Verweis-URL protokolliert, sodass die Daten für die Wartung der Produktion\n" +
                "        verwendet und ein aggregierter Bericht zu den Top-Integrationen basierend auf der Anzahl der\n" +
                "        Schriftartenanfragen generiert werden kann. Google verwendet laut eigener Auskunft keine der von Google Fonts\n" +
                "        erfassten Informationen, um Profile von Endnutzern zu erstellen oder zielgerichtete Anzeigen zu schalten;\n" +
                "        <strong>Dienstanbieter:</strong> Google Ireland Limited, Gordon House, Barrow Street, Dublin 4, Irland; <span\n" +
                "                class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO); </span><strong>Website:</strong>\n" +
                "        <a href=\"https://fonts.google.com/\" target=\"_blank\">https://fonts.google.com/</a>;\n" +
                "        <strong>Datenschutzerklärung:</strong> <a href=\"https://policies.google.com/privacy\" target=\"_blank\">https://policies.google.com/privacy</a>;\n" +
                "        <strong>Grundlage Drittlandübermittlung:</strong> <span class=\"\"> EU-US Data Privacy Framework (DPF)</span>.\n" +
                "        <strong>Weitere Informationen:</strong> <a href=\"https://developers.google.com/fonts/faq/privacy?hl=de\"\n" +
                "                                                   target=\"_blank\">https://developers.google.com/fonts/faq/privacy?hl=de</a>.\n" +
                "    </li>\n" +
                "    <li><strong>Font Awesome (Bereitstellung auf eigenem Server): </strong>Darstellung von Schriftarten und Symbolen;\n" +
                "        <strong>Dienstanbieter:</strong> Die Font Awesome Icons werden auf unserem Server gehostet, es werden keine\n" +
                "        Daten an den Anbieter von Font Awesome übermittelt; <span class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO).</span>\n" +
                "    </li>\n" +
                "    <li><strong>Google Maps: </strong>Wir binden die Landkarten des Dienstes \"Google Maps\" des Anbieters Google ein. Zu\n" +
                "        den verarbeiteten Daten können insbesondere IP-Adressen und Standortdaten der Nutzer gehören; <strong>Dienstanbieter:</strong>\n" +
                "        Google Cloud EMEA Limited, 70 Sir John Rogerson’s Quay, Dublin 2, Irland; <span class=\"\"><strong>Rechtsgrundlagen:</strong> Einwilligung (Art. 6 Abs. 1 S. 1 lit. a) DSGVO); </span><strong>Website:</strong>\n" +
                "        <a href=\"https://mapsplatform.google.com/\" target=\"_blank\">https://mapsplatform.google.com/</a>; <strong>Datenschutzerklärung:</strong>\n" +
                "        <a href=\"https://policies.google.com/privacy\" target=\"_blank\">https://policies.google.com/privacy</a>. <strong>Grundlage\n" +
                "            Drittlandübermittlung:</strong> <span class=\"\"> EU-US Data Privacy Framework (DPF)</span>.\n" +
                "    </li>\n" +
                "    <li><strong>MyFonts: </strong>Schriftarten; im Rahmen des Schriftartenabrufs verarbeitete Daten die\n" +
                "        Identifikationsnummer des Webfont-Projektes (anonymisiert), die URL der lizenzierten Website, die mit einer\n" +
                "        Kundennummer verknüpft ist, um den Lizenznehmer und die lizenzierten Webfonts zu identifizieren, und die\n" +
                "        Referrer URL; die anonymisierte Webfont-Projekt-Identifikationsnummer wird in verschlüsselten Protokolldateien\n" +
                "        mit solchen Daten für 30 Tage, um die monatliche Anzahl der Seitenaufrufe zu ermitteln, gespeichert; Nach einer\n" +
                "        solchen Extraktion und Speicherung der Anzahl der Seitenaufrufe werden die Protokolldateien gelöscht; <strong>Dienstanbieter:</strong>\n" +
                "        Monotype Imaging Holdings Inc., 600 Unicorn Park Drive, Woburn, Massachusetts 01801, USA; <span\n" +
                "                class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO); </span><strong>Website:</strong>\n" +
                "        <a href=\"https://www.myfonts.co\" target=\"_blank\">https://www.myfonts.co</a>.\n" +
                "        <strong>Datenschutzerklärung:</strong> <a href=\"https://www.myfonts.com/info/legal/#Privacy\" target=\"_blank\">https://www.myfonts.com/info/legal/#Privacy</a>.\n" +
                "    </li>\n" +
                "    <li><strong>OpenStreetMap: </strong>Wir binden die Landkarten des Dienstes \"OpenStreetMap\" ein, die auf Grundlage\n" +
                "        der Open Data Commons Open Database Lizenz (ODbL) durch die OpenStreetMap Foundation (OSMF) angeboten werden.\n" +
                "        Die Daten der Nutzer werden durch OpenStreetMap ausschließlich zu Zwecken der Darstellung der Kartenfunktionen\n" +
                "        und zur Zwischenspeicherung der gewählten Einstellungen verwendet. Zu diesen Daten können insbesondere\n" +
                "        IP-Adressen und Standortdaten der Nutzer gehören, die jedoch nicht ohne deren Einwilligung (im Regelfall im\n" +
                "        Rahmen der Einstellungen ihrer Endgeräte oder Browser vollzogen) erhoben werden;\n" +
                "        <strong>Dienstanbieter:</strong> OpenStreetMap Foundation (OSMF); <span\n" +
                "                class=\"\"><strong>Rechtsgrundlagen:</strong> Berechtigte Interessen (Art. 6 Abs. 1 S. 1 lit. f) DSGVO); </span><strong>Website:</strong>\n" +
                "        <a href=\"https://www.openstreetmap.de\" target=\"_blank\">https://www.openstreetmap.de</a>. <strong>Datenschutzerklärung:</strong>\n" +
                "        <a href=\"https://osmfoundation.org/wiki/Privacy_Policy\" target=\"_blank\">https://osmfoundation.org/wiki/Privacy_Policy</a>.\n" +
                "    </li>\n" +
                "</ul>\n" +
                "<p class=\"seal\"><a href=\"https://datenschutz-generator.de/\"\n" +
                "                   rel=\"noopener noreferrer nofollow\" target=\"_blank\"\n" +
                "                   title=\"Rechtstext von Dr. Schwenke - für weitere Informationen bitte anklicken.\">Erstellt mit\n" +
                "    kostenlosem Datenschutz-Generator.de von Dr. Thomas\n" +
                "    Schwenke</a></p>" +
                "</div>"));
    }
}