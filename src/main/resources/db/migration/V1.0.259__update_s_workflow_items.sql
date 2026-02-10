UPDATE s_workflow_items
SET name = CASE
               WHEN name = 'Vertragsaenderung Daten vervollstaendigen' THEN 'Daten vervollständigen (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Zusatzdokument erstellen' THEN 'Zusatzdokument erstellen (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung zur Pruefung vorlegen' THEN 'Zur Prüfung vorlegen (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung people informieren' THEN 'People informieren (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung people prueft' THEN 'People prüft (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Lohnverrechnung informieren' THEN 'Lohnverrechnung informieren (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Lohnverrechnung prueft' THEN 'Lohnverrechnung prüft (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Genehmiger informieren' THEN 'Genehmiger informieren (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Genehmiger prueft' THEN 'Genehmiger prüft (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Unterschriftenlauf starten' THEN 'Unterschriftenlauf starten (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Unterschriftenlauf durchfuehren' THEN 'Unterschriftenlauf durchführen (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Unterschriebenes Dokument speichern' THEN 'Unterschriebenes Dokument speichern (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Zusatz im System eintragen' THEN 'Zusatz im System eintragen (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Daten an LHR uebermitteln' THEN 'Daten an LHR übermitteln (Vertragsänderung)'
               WHEN name = 'Vertragsaenderung Beteiligten informieren' THEN 'Beteiligten informieren (Vertragsänderung)'
    END
WHERE name IN (
               'Vertragsaenderung Daten vervollstaendigen',
               'Vertragsaenderung Zusatzdokument erstellen',
               'Vertragsaenderung zur Pruefung vorlegen',
               'Vertragsaenderung people informieren',
               'Vertragsaenderung people prueft',
               'Vertragsaenderung Lohnverrechnung informieren',
               'Vertragsaenderung Lohnverrechnung prueft',
               'Vertragsaenderung Genehmiger informieren',
               'Vertragsaenderung Genehmiger prueft',
               'Vertragsaenderung Unterschriftenlauf starten',
               'Vertragsaenderung Unterschriftenlauf durchfuehren',
               'Vertragsaenderung Unterschriebenes Dokument speichern',
               'Vertragsaenderung Zusatz im System eintragen',
               'Vertragsaenderung Daten an LHR uebermitteln',
               'Vertragsaenderung Beteiligten informieren'
    );