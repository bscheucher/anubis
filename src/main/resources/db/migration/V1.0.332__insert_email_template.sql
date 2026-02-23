INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('lhr-service.abwesenheit.error',
        (select id from languages where name = 'german'),
        'Bitte Abwesenheit für %s bis %s neu beantragen',
        'Liebe/r %s, <br><br>bitte erstelle einen neuen Abwesenheitsantrag für den Zeitraum %s bis %s, sofern die Abwesenheit weiterhin relevant ist. Dein ursprünglicher Antrag wurde im Rahmen einer Datenbereinigung archiviert. Wir bitten die Unannehmlichkeiten zu entschuldigen',
        1,
        current_user, current_timestamp);