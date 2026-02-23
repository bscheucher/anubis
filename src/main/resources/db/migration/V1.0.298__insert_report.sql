WITH new_report AS (
INSERT INTO report (report_name, source_path, main_report_file, created_by, data_source, report_type)
VALUES ('Teilnehmer Anwesenheitsquote pro Seminar', 'anwesenheit_teilnehmer_quote', 'Teilnehmer Anwesenheitsquote pro Seminar.jrxml', current_user, 'MARIADB', 'MANUAL')
    RETURNING id
    )
INSERT INTO report_parameter (name, type, description, report_id, required, label, default_value)
VALUES
    ('Projekt', 'INTEGER', 'bitte Angabe der iBOS-ID des Projekts (ablesbar in der Adresszeile des Browers auf Projektebene)', (SELECT id FROM new_report), true, 'Projekt', null),
    ('Stichtag', 'DATE', 'Anwesenheiten berücksichtigten bis inkl diesem Datum', (SELECT id FROM new_report), true, 'Stichtag', null);