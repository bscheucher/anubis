INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.hr.neuer-ma-akzeptiert',
        (select id from languages where name = 'german'),
        'Neue/r Mitarbeiter/in - Daten von der Lohnverrechnung akzeptiert',
        'Liebes HR Team,<br><br>' ||
        'für einen neuen Mitarbeiter wurden Stamm- und Vertragsdaten von der Lohnverrechnung freigegeben.<br><br>',
        1,
        current_user, current_timestamp);


INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.hr.neuer-ma-abgelehnt',
        (select id from languages where name = 'german'),
        'Neue/r Mitarbeiter/in - Daten von der Lohnverrechnung abgelehnt',
        'Liebes HR Team,<br><br>' ||
        'für einen neuen Mitarbeiter wurden Stamm- und/oder Vertragsdaten von der Lohnverrechnung nicht freigegeben. ' ||
        'Bitte diese Daten in ibosNG überprüfen und entsprechend korrigieren.<br><br>',
        1,
        current_user, current_timestamp);


INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.hr.neuer-ma-dv-erstellt',
        (select id from languages where name = 'german'),
        'Neue/r Mitarbeiter/in - Dienstvertrag erstellt',
        'Liebes HR Team,<br><br>' ||
        'für einen neuen Mitarbeiter wurde automatisch ein Dienstvertrag erstellt und an den Personalakt angehängt.<br><br>',
        1,
        current_user, current_timestamp);

INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('lhr-service.hr.neuer-ma-lhr-uebermittlung-fehlgeschlagen',
        (select id from languages where name = 'german'),
        'Neue/r Mitarbeiter/in - LHR Übermittlung fehlgeschlagen',
        'Liebes HR Team,<br><br>' ||
        'Die Übermittlung von Mitarbeitern an LHR ist zum Zeitpunkt %s Uhr fehlgeschlagen. Bitte prüfen Sie die Übermittlung nach der untenstehenden Begründung. ' ||
        'Die Daten, die an LHR übermittelt werden sollten, sind dieser E-Mail als Anhang beigefügt.<br><br>',
        1,
        current_user, current_timestamp);