INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.tn-anabweseneheit.anabweseneheit-transfer-success',
        (select id from languages where name = 'german'),
        'TN An-/Abwesenheiten an LHR übermittelt für %s %s',
        'Liebes LV-Team,<br><br>' ||
        'Die TN An-/Abwesenheiten für den Zeitraum %s %s an LHR übermittelt. Die Lohnabrechnung kann für die beteiligten Lehrlinge erfolgen.<br><br>',
        1,
        current_user, current_timestamp);


INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.tn-anabweseneheit.transfer-error',
        (select id from languages where name = 'german'),
        'Achtung: TN An-/Abwesenheiten an LHR fehlgeschlagen für Periode %s %s',
        'Liebes LV-Team,<br><br>' ||
        'Die TN An-/Abwesenheiten für den Zeitrum %s %s konnten an LHR nicht übermittelt werden. Die Lohnabrechnung kann für die Periode noch nicht erfolgen.<br><br>',
        1,
        current_user, current_timestamp);