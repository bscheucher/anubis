INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.lhr.neuer-teilnehmer-onboarding',
        (select id from languages where name = 'german'),
        'Neuer Lehrling - Daten zur Überprüfung',
        'Liebes Lohnverrechnungsteam,<br><br>' ||
        'für einen neuen Lehrling wurden Stamm- und Vertragsdaten erfasst. ' ||
        'Bitte diese Daten in ibosNG überprüfen.<br><br>',
        1,
        current_user, current_timestamp);

INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.hr.neuer-teilnehmer-onboarding-akzeptiert',
        (select id from languages where name = 'german'),
        'Neuer Teilnehmer - Daten von der Lohnverrechnung akzeptiert',
        'Liebes HR Team,<br><br>' ||
        'für einen neuen Teilnehmer wurden Stamm- und/oder Vertragsdaten von der Lohnverrechnung freigegeben.<br><br>',
        1,
        current_user, current_timestamp);


INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.hr.neuer-teilnehmer-onboarding-abgelehnt',
        (select id from languages where name = 'german'),
        'Neuer Teilnehmer - Daten von der Lohnverrechnung abgelehnt',
        'Liebes HR Team,<br><br>' ||
        'für einen neuen Teilnehmer wurden Stamm- und/oder Vertragsdaten von der Lohnverrechnung nicht freigegeben. ' ||
        'Bitte diese Daten in ibosNG überprüfen und entsprechend korrigieren.<br><br>',
        1,
        current_user, current_timestamp);