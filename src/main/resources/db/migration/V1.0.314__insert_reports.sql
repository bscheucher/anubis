WITH new_report AS (
INSERT INTO report (report_name, source_path, main_report_file, created_by, data_source, report_type)
VALUES ('Urlaubsauswertung Kostenstelle', 'urlaubskontostand', 'Urlaubsauswertung Kostenstelle.jrxml', current_user, 'LHR', 'MANUAL')
    RETURNING id
    )
INSERT INTO report_parameter (name, type, description, report_id, required, label, default_value)
VALUES
    ('Kostenstelle', 'STRING', 'Kostenstelle', (SELECT id FROM new_report), true, '', null);


WITH new_report AS (
INSERT INTO report (report_name, source_path, main_report_file, created_by, data_source, report_type)
VALUES ('Restsaldo per Stichtag', 'urlaubskontostand', 'Restsaldo per Stichtag.jrxml', current_user, 'LHR', 'MANUAL')
    RETURNING id
    )
INSERT INTO report_parameter (name, type, description, report_id, required, label, default_value)
VALUES
    ('pnr', 'STRING', 'Angabe: Personalnummer', (SELECT id FROM new_report), true, 'Mitarbeiter_in', null),
    ('Restsaldo bis', 'DATE', '', (SELECT id FROM new_report), true, 'Restsaldo am', null),
    ('Vorgesetze_r', 'STRING', 'Angabe: Vorname Nachname', (SELECT id FROM new_report), true, 'Vorgesetze_r', null);


WITH new_report AS (
INSERT INTO report (report_name, source_path, main_report_file, created_by, data_source, report_type)
VALUES ('Urlaubsauswertung SU', 'urlaubskontostand', 'Urlaubsauswertung SU.jrxml', current_user, 'LHR', 'MANUAL')
    RETURNING id
    )
INSERT INTO report_parameter (name, type, description, report_id, required, label, default_value)
VALUES
    ('SU', 'STRING', 'Angabe: Vorname Nachname', (SELECT id FROM new_report), true, 'Vorgesetzte_r', null);

WITH new_report AS (
INSERT INTO report (report_name, source_path, main_report_file, created_by, data_source, report_type)
VALUES ('Urlaubskalender Kostenstelle', 'urlaubskontostand', 'Urlaubskalender Kostenstelle.jrxml', current_user, 'LHR', 'MANUAL')
    RETURNING id
    )
INSERT INTO report_parameter (name, type, description, report_id, required, label, default_value)
VALUES
    ('Datum von', 'DATE', '', (SELECT id FROM new_report), true, 'Datum von', null),
    ('Datum bis', 'DATE', '', (SELECT id FROM new_report), true, 'Datum bis', null),
    ('Kostenstelle', 'STRING', '', (SELECT id FROM new_report), true, 'Kostenstelle', null);

WITH new_report AS (
INSERT INTO report (report_name, source_path, main_report_file, created_by, data_source, report_type)
VALUES ('Urlaubskalender SU', 'urlaubskontostand', 'Urlaubskalender SU.jrxml', current_user, 'LHR', 'MANUAL')
    RETURNING id
    )
INSERT INTO report_parameter (name, type, description, report_id, required, label, default_value)
VALUES
    ('Datum von', 'DATE', 'Description', (SELECT id FROM new_report), true, 'Von', null),
    ('Datum bis', 'DATE', 'Description', (SELECT id FROM new_report), true, 'Bis', null),
    ('SU', 'STRING', 'Angabe: Vorname Nachname', (SELECT id FROM new_report), true, 'Vorgesetzte_r', null);