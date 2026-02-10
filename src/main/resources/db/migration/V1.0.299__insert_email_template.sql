INSERT INTO email_template (identifier, language, subject, body, status, created_on, created_by)
VALUES
    ('gateway-service.ma-beauftragung-fehlgeschlagen', (select id from languages where name = 'german'),
     'Userbeauftragung für %s bei IT ist fehlgeschlagen',
     'Liebe Kolleg_innen, <br> <br> die Beauftragung für den Mitarbeiter %s im Rahmen des Onboardings ist fehlgeschlagen.',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('gateway-service.ma-im-system-anlegen-erfolgreich', (select id from languages where name = 'german'),
     'Neue/r Mitarbeiter/in anlegen %s - Erfolgreich',
     'Liebe Kolleg_innen, <br> <br> das Anlegen von Mitarbeiter_in %s war zum Zeitpunkt %s Uhr erfolgreich.',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('gateway-service.ma-im-system-anlegen-fehlgeschlagen', (select id from languages where name = 'german'),
     'Neue/r Mitarbeiter/in anlegen %s - Fehlgeschlagen',
     'Liebe Kolleg_innen, <br> <br> das Anlegen von Mitarbeiter_in %s hat zum Zeitpunkt %s Uhr fehlgeschlagen. Bitte vervollständigen Sie die Angaben nach der untenstehenden Begründung.%s',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('gateway-service.ma-verwaltung-beteiligten-informieren', (select id from languages where name = 'german'),
     'Neue Vereinbarung zwischen FK: %s MA: %s',
     'Liebe/r Kolleg/In, zwischen FK: %s und MA: %s wurde eine neue Vereinbarung für %s ab %s getroffen. Über den folgenden Link kann die unterschriebene Vereinbarung eingesehen werden: <a href="%s"> LINK </a> ',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('lhr-service.hr-neuer-ma-lhr-uebermittlung-erfolgreich', (select id from languages where name = 'german'),
     'Neue/r Mitarbeiter/in %s - LHR Übermittlung erfolgreich',
     'Liebes LV-Team,<br><br>Die Übermittlung von Mitarbeiter_in %s an LHR war zum Zeitpunkt %s Uhr erfolgreich.',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('lhr-service.ma-mitarbeiterdaten-lesen-und-bearbeiten-fehlgeschlagen', (select id from languages where name = 'german'),
     'Die Aktualisierung von Mitarbeiterdaten %s an LHR war erfolglos',
     'Liebe/r Kolleg/in, die Aktualisierung von Mitarbeiterdaten %s an LHR ist zum Zeitpunkt %s fehlgeschlagen. Prüfe bitte die Übermittlung nach der untenstehenden Begründung. %s',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('lhr-service.ma-schnittstelle-nicht-verfuegbar', (select id from languages where name = 'german'),
     'Die Dokumentenschnittstelle zu LHR ist nicht verfügbar',
     'Liebe/r KollegIn, es ist nicht gelungen, neue Personalunterlagen aus dem LHR-System herunterzuladen und in der Personalakt zu speichern. ',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('lhr-service.ma-verwaltung-fehlgeschlagen', (select id from languages where name = 'german'),
     'Achtung: Fehler im LHR - Zusatz für %s konnte nicht übermittelt werden',
     'Liebes LV-Team, <br> <br> die Aktualisierung von Änderungen eines Zusatzes von %s an LHR ist zum Zeitpunkt %s fehlgeschlagen. Prüfe bitte die Übermittlung nach der untenstehenden Begründung. %s',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('lhr-service.ma-zusatz-vereinbart-info', (select id from languages where name = 'german'),
     'Neuer Zusatz %s wurde vereinbart',
     'Es wurde ein Zusatz für MA %s vereinbart. Der Zusatz umfasst folgende Änderung(en): %s',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('lhr-service.people-unterschriftenlauf-fehlgeschlagen', (select id from languages where name = 'german'),
     'Unterschriftenlauf für einen Zusatz für %s ist fehlgeschlagen',
     'Liebe Kolleg_innen, <br> <br> der Unterschriftenlauf konnte wegen eines Fehler mit moxis nicht gestartet werden.',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('lhr-service.tn-anabwesenheit-mitarbeiter-anlegen-erfolgreich', (select id from languages where name = 'german'),
     'Das Anlegen vom ÜBA Lehrling war erfolgreich %s',
     'Liebes LV-Team,<br><br>das Anlegen vom ÜBA Lehrling_in %s an LHR war zum Zeitpunkt %s Uhr erfolgreich.',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('lhr-service.tn-anabwesenheit-mitarbeiter-anlegen-fehlgeschlagen', (select id from languages where name = 'german'),
     'Achtung: Das Anlegen vom ÜBA Lehrling %s ist fehlgeschlagen',
     'Liebes LV-Team,<br><br>das Anlegen vom ÜBA Lehrling %s an LHR ist zum Zeitpunkt %s Uhr fehlgeschlagen. Bitte prüfen Sie die Übermittlung nach der untenstehenden Begründung. Die Daten, die an LHR übermittelt werden sollten, sind dieser E-Mail als Anhang beigefügt.<br><br>',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('lhr-service.tn-anabwesenheit-uebergabe-fehlgeschlagen', (select id from languages where name = 'german'),
     'Achtung: Datenübergabe an LHR fehlgeschlagen %s',
     'Liebes LV-Team,<br><br>Die Übermittlung von Mitarbeiter_in %s an LHR ist zum Zeitpunkt %s Uhr fehlgeschlagen. Bitte prüf die Übermittlung nach der untenstehenden Begründung. Die Daten, die an LHR übermittelt werden sollten, sind dieser E-Mail als Anhang beigefügt.<br><br>',
     1, CURRENT_TIMESTAMP, CURRENT_USER),

    ('moxis-service.hr.neuer-ma-moxis-unterschriftprozess-erfolgreich', (select id from languages where name = 'german'),
     'Neue/r Mitarbeiter/in %s - Moxis Unterschriftsprozess erfolgreich',
     'Liebe Kolleg_innen, <br>br>der Unterschriftenlauf für %s wurde erfolgreich abgeschlossen.',
     1, CURRENT_TIMESTAMP, CURRENT_USER);
