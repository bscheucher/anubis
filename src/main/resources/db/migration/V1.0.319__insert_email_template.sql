INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('moxis-service.lv.neuer-ma-moxis-unterschriftsprozess-fehlgeschlagen',
        (select id from languages where name = 'german'),
        'Neue/r Mitarbeiter/in - Moxis Unterschriftsprozess fehlgeschlagen',
        'Liebes LV Team,<br><br>' ||
        'Der Moxis Unterschriftsprozes ist zum Zeitpunkt %s Uhr fehlgeschlagen. Bitte prüfen Sie die Übermittlung nach der untenstehenden Begründung. ' ||
        'Die Daten, die an Moxis übermittelt werden sollten, sind dieser E-Mail als Anhang beigefügt.<br><br>',
        1,
        current_user, current_timestamp);