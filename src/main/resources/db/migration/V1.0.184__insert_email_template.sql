INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('lhr-service.tn-onboarding-fertig',
        (select id from languages where name = 'german'),
        '%s %s im System angelegt',
        'Liebes HR-Team, Es wurden die User für einen neuen ÜBA Lehrling erfolgreich angelegt. Das Onboarding ist somit erfolgreich abgeschlossen.' ||
        '%s %s, Eintrittsdatum: %s.<br><br>',
        1,
        current_user, current_timestamp);
