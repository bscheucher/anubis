INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.lhr.tn-ueba-abmelden',
        (select id from languages where name = 'german'),
        'Überprüfung der Austrittsmeldung für TeilnehmerIn - Daten zur Überprüfung',
        'Liebes Lohnverrechnungsteam,<br><br>' ||
        'für die/den TeilnehmerIn %s wurde eine Austrittsmeldung zum %s übermittelt. Bitte überprüfen Sie die Daten im LHR-System überprüfen.<br><br>',
        1,
        current_user, current_timestamp);
