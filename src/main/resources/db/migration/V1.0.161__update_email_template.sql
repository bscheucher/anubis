UPDATE email_template
SET body = 'Liebes HR Team,<br><br>' ||
           'für einen neuen Lehrling wurden Stamm- und/oder Vertragsdaten von der Lohnverrechnung freigegeben.<br><br>',
    subject = 'Neuer Lehrling - Daten von der Lohnverrechnung akzeptiert'
WHERE identifier = 'gateway-service.hr.neuer-teilnehmer-onboarding-akzeptiert';


UPDATE email_template
SET body = 'Liebes HR Team,<br><br>' ||
           'für einen neuen Lehrling wurden Stamm- und/oder Vertragsdaten von der Lohnverrechnung nicht freigegeben. ' ||
           'Bitte diese Daten in ibosNG überprüfen und entsprechend korrigieren.<br><br>',
    subject = 'Neuer Lehrling - Daten von der Lohnverrechnung abgelehnt'
WHERE identifier = 'gateway-service.hr.neuer-teilnehmer-onboarding-abgelehnt';