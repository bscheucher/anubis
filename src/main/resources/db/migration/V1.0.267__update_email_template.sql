UPDATE email_template
SET body = 'Liebe KollegInnen,<br><br>für einen Mitarbeiter wurde eine Vertragsänderung zur Prüfung vorgelegt. Bitte deren Daten im System prüfen. <a href="%s">Link</a>'
WHERE identifier = 'gateway-service.vertragsaenderung.people-informiren';

UPDATE email_template
SET body = 'People hat den Vorgang unvollständig bewertet. Bitte um Vervollständigung der Daten und erneutes Vorliegen zur Prüfung. <a href="%s">Link</a>'
WHERE identifier = 'gateway-service.vertragsaenderung.people-lehnt-ab';

UPDATE email_template
SET body = 'Liebe KollegInnen,<br><br>für einen Mitarbeiter wurde eine Vertragsänderung zur Prüfung vorgelegt. Bitte deren Daten im System prüfen. <a href="%s">Link</a>'
WHERE identifier = 'gateway-service.vertragsaenderung.lohnverrechnung-informiren';

UPDATE email_template
SET body = 'Liebe KollegInnen,<br><br>für einen Mitarbeiter wurde eine Vertragsänderung zur Genehmigung vorgelegt. Bitte den Zusatz im System bewerten. <a href="%s">Link</a>'
WHERE identifier = 'gateway-service.vertragsaenderung.genehmigenden-informiren';