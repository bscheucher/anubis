UPDATE email_template
SET subject = 'Neue/r Mitarbeiter/in - Moxis Unterschriftsprozess fehlgeschlagen',
    body    = 'Liebes HR Team,<br><br>Der Moxis Unterschriftsprozess ist zum Zeitpunkt %s Uhr fehlgeschlagen. Bitte prüfen Sie die Übermittlung nach der untenstehenden Begründung. Die Daten von %s, die an Moxis übermittelt werden sollten, sind dieser E-Mail als Anhang beigefügt.<br><br>'
WHERE identifier = 'moxis-service.hr.neuer-ma-moxis-unterschriftsprozess-fehlgeschlagen';