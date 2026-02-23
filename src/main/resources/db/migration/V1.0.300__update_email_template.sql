UPDATE email_template
SET subject = 'Neue/r Mitarbeiter/in - Daten von der Lohnverrechnung abgelehnt',
    body    = 'Liebes HR Team,<br><br>für eine_n neue_n Mitarbeiter_in %s  wurden Stamm- und/oder Vertragsdaten von der Lohnverrechnung nicht freigegeben. Bitte diese Daten in ibosNG überprüfen und entsprechend korrigieren.<br><br>'
WHERE identifier = 'gateway-service.hr.neuer-ma-abgelehnt';

UPDATE email_template
SET subject = 'Neue/r Mitarbeiter/in - Daten von der Lohnverrechnung akzeptiert',
    body    = 'Liebes HR Team,<br><br>für eine_n neue_n Mitarbeiter_in %s wurden Stamm- und Vertragsdaten von der Lohnverrechnung freigegeben.<br><br>'
WHERE identifier = 'gateway-service.hr.neuer-ma-akzeptiert';

UPDATE email_template
SET subject = 'Neue/r Mitarbeiter/in - Dienstvertrag erstellt',
    body    = 'Liebes HR Team,<br><br>für eine_n neue_n Mitarbeiter %s wurde automatisch ein Dienstvertrag erstellt und an den Personalakt angehängt. Bitte die Vollständigkeit des Dienstvertrages im System prüfen %s <br><br>'
WHERE identifier = 'gateway-service.hr.neuer-ma-dv-erstellt';

UPDATE email_template
SET subject = 'Neue/r Mitarbeiter/in - Daten zur Überprüfung',
    body    = 'Liebe Kolleg_innen,<br><br>Für eine_n neue_n Mitarbeiter_in %s wurden Stamm- und Vertragsdaten erfasst. Bitte diese Daten in ibosNG überprüfen.<br><br>'
WHERE identifier = 'gateway-service.lhr.neuer-ma';

UPDATE email_template
SET subject = 'Neuer Lehrling - Daten zur Überprüfung',
    body    = 'Liebes LV-Team,<br><br>für einen neuen Lehrling wurden Stamm- und Vertragsdaten erfasst. Bitte diese Daten in ibosNG überprüfen. <ul><li> Name: %s </li> <li> Eintrittsdatum: %s </li> </ul> <br><br>'
WHERE identifier = 'gateway-service.lhr.neuer-teilnehmer-onboarding';

UPDATE email_template
SET subject = 'Überprüfung der Austrittsmeldung für TeilnehmerIn - Daten zur Überprüfung',
    body    = 'Liebes LV-Team,<br><br>für die/den Teilnehmer_in wurde eine Austrittsmeldung übermittelt. Bitte überprüfen Sie die Daten im LHR-System. <br><br> <ul><li> Name: %s </li> <li> Austrittsdatum: %s </li> </ul>'
WHERE identifier = 'gateway-service.lhr.tn-ueba-abmelden';

UPDATE email_template
SET subject = 'Abwesenheit erstellt, bitte genehmigen %s',
    body    = 'Liebe/r %s, Ihr/e KollegIn %s hat soeben einen Abwesenheitsantrag für den Zeitraum von %s bis %s gestellt. Bitte um Prüfung. <a href="%s">LINK</a>'
WHERE identifier = 'gateway-service.ma-abwesenheit-info';

UPDATE email_template
SET subject = 'Abwesenheit Storno genehmigt',
    body    = 'Liebe/r %s, Ihre Führungskraft hat Ihren Stornoantrag für den Zeitraum von %s bis %s genehmigt. Du kannst den Antrag hier anschauen. <a href="%s">LINK</a>'
WHERE identifier = 'gateway-service.ma-abwesenheit-storno-erfolgreich';

UPDATE email_template
SET subject = 'Abwesenheit Storno abgelehnt',
    body    = 'Liebe/r %s, Ihre Führungskraft hat Ihren Stornoantrag für den Zeitraum von %s bis %s abgelehnt. Du kannst die Begründung in der Übersichtsliste anschauen. <a href="%s">LINK</a>'
WHERE identifier = 'gateway-service.ma-abwesenheit-storno-fehlgeschlagen';

UPDATE email_template
SET subject = 'Abwesenheit wird storniert, bitte genehmigen. %s',
    body    = 'Liebe/r %s, Ihr/e KollegIn %s möchte eine Abwesenheit für den Zeitraum von %s bis %s stornieren. Du kannst den Stornoantrag hier genehmigen oder ablehnen. <a href="%s">LINK</a>'
WHERE identifier = 'gateway-service.ma-abwesenheit-storno-info';

UPDATE email_template
SET subject = 'Achtung: TN An-/Abwesenheiten an LHR fehlgeschlagen für Periode %s %s',
    body    = 'Liebes LV-Team,<br><br>Die TN An-/Abwesenheiten für den Zeitraum %s %s konnten an LHR nicht übermittelt werden. Die Lohnabrechnung kann für die Periode noch nicht erfolgen.<br><br>'
WHERE identifier =
      'gateway-service.tn-anabweseneheit.anabweseneheit-transfer-error';

UPDATE email_template
SET subject = 'TN tritt aus',
    body    = 'Liebes LV-Team,<br><br>Für die/den Teilnehmer_in wurde eine Austrittsmeldung übermittelt. Bitte überprüfen Sie die Daten im LHR-System.<br><br> <ul><li> Name: %s </li> <li> Austrittsdatum: %s </li> </ul><br><br>'
WHERE identifier = 'gateway-service.tn-austritt-transfer-success';

UPDATE email_template
SET subject = 'Neue/r Mitarbeiter/in - LHR Übermittlung fehlgeschlagen',
    body    = 'Liebes LV-Team,<br><br>Die Übermittlung von Mitarbeiter_in %s  an LHR ist zum Zeitpunkt %s Uhr fehlgeschlagen. Bitte prüfen Sie die Übermittlung nach der untenstehenden Begründung. Die Daten, die an LHR übermittelt werden sollten, sind dieser E-Mail als Anhang beigefügt.<br><br>'
WHERE identifier = 'lhr-service.hr.neuer-ma-lhr-uebermittlung-f7ehlgeschlagen';

UPDATE email_template
SET subject = 'Umbuchung rechtzeitig erledigen',
    body    = 'Lieber MA,<br><br>Aktuell liegen auf deinem Zeitkonto Mehrstunden zur Auszahlung. Vergiss nicht deine Umbuchung bis zum  15. des Folgemonats zu erledigen.<br><br>'
WHERE identifier = 'lhr-service.ma-auszahlbare-ueberstunden';

UPDATE email_template
SET subject = 'Neue/r Mitarbeiter/in - Moxis Unterschriftsprozess fehlgeschlagen',
    body    = 'Liebes HR Team,<br><br>Der Moxis Unterschriftsprozes ist zum Zeitpunkt %s Uhr fehlgeschlagen. Bitte prüfen Sie die Übermittlung nach der untenstehenden Begründung. Die Daten von %s, die an Moxis übermittelt werden sollten, sind dieser E-Mail als Anhang beigefügt.<br><br>'
WHERE identifier = 'moxis-service.hr.neuer-ma-moxis-unterschriftsprozess-fehlgeschlagen';


UPDATE email_template
SET body = 'Liebe/r %s,<br><br>Ihr/e Führungskraft hat Ihren Abwesenheitsantrag für den Zeitraum von %s bis %s abgelehnt. Du kannst die Begründung in der Übersichtsliste anschauen. <a href="%s">LINK</a>'
WHERE identifier = 'gateway-service.ma-abwesenheit-abgelehnt';

UPDATE email_template
SET body       = 'Liebe/r %s,<br><br>Ihr/e Führungskraft hat Ihren Abwesenheitsantrag für den Zeitraum von %s bis %s genehmigt. Du kannst den Antrag hier anschauen. <a href="%s">LINK</a>',
    identifier = 'gateway-service.ma-abwesenheit-genehmigt'
WHERE identifier = 'gateway-service.ma-abwesenheit-genehmigt,';

UPDATE email_template
SET body = 'Liebe/r %s,<br><br>Ihr/e KollegIn %s hat soeben einen Abwesenheitsantrag für den Zeitraum von %s bis %s gestellt. Bitte um Prüfung. <a href="%s">LINK</a>'
WHERE identifier = 'gateway-service.ma-abwesenheit-info';

UPDATE email_template
SET body = 'Liebe/r %s,<br><br>Ihr/e Führungskraft hat Ihren Stornoantrag für den Zeitraum von %s bis %s genehmigt. Du kannst den Antrag hier anschauen. <a href="%s">LINK</a>'
WHERE identifier = 'gateway-service.ma-abwesenheit-storno-erfolgreich';

UPDATE email_template
SET body = 'Liebe/r %s,<br><br>Ihr/e Führungskraft hat Ihren Stornoantrag für den Zeitraum von %s bis %s abgelehnt. Du kannst die Begründung in der Übersichtsliste anschauen. <a href="%s">LINK</a>'
WHERE identifier = 'gateway-service.ma-abwesenheit-storno-fehlgeschlagen';

UPDATE email_template
SET body = 'Liebe/r %s,<br><br>Ihr/e KollegIn %s möchte eine Abwesenheit für den Zeitraum von %s bis %s stornieren. Du kannst den Stornoantrag hier genehmigen oder ablehnen. <a href="%s">LINK</a>'
WHERE identifier = 'gateway-service.ma-abwesenheit-storno-info';

