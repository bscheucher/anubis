INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('lhr-service.ma-auszahlbare-ueberstunden',
        (select id from languages where name = 'german'),
        'Umbuchung rechtzeitig erledigen',
        'Lieber MA,<br><br>' ||
        'Aktuell liegen auf deinem Zeitkonto Mehrstunden zur Auszahlung.' ||
        'Vergiss nicht deine Umbuchung bis zum 10. des Folgemonats zu erledigen.<br><br>',
        1,
        current_user, current_timestamp);
