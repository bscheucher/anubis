INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.tn-anabweseneheit.anabweseneheit-transfer-success',
        (select id from languages where name = 'german'),
        'TN tritt aus',
        'Liebes Lohnverrechnungsteamsteam,<br><br>' ||
        'Für die/den TeilnehmerIn %s wurde eine Austrittsmeldung zum %s übermittelt. Bitte überprüfen Sie die Daten im LHR-System überprüfen.<br><br>',
        1,
        current_user, current_timestamp);