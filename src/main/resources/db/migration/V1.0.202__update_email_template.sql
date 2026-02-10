UPDATE email_template
SET body = 'Liebes HR Team,<br><br>' ||
           'für einen neuen Mitarbeiter wurde automatisch ein Dienstvertrag erstellt und an den Personalakt angehängt. Bitte die Vollständigkeit des Dienstvertrages im System prüfen %s <br><br>'
WHERE identifier = 'gateway-service.hr.neuer-ma-dv-erstellt';