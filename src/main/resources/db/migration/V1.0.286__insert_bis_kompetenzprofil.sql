WITH new_report AS (
INSERT INTO public.report (
    report_name, source_path, main_report_file, created_by, data_source, report_type
)
VALUES (
    'BIS Kompetenzprofil',
    'bis_kompetenzprofil',
    'BIS Kompetenzprofil.jrxml',
    'ibosng',
    'POSTGRES',
    'MANUAL'
    )
    RETURNING id
    )
INSERT INTO public.report_parameter (
    name, type, description, report_id, required, label, default_value
)
VALUES
    ('Teilnehmer', 'INTEGER', null, (SELECT id FROM new_report), null, null, null),
    ('mit Score', 'BOOLEAN', null, (SELECT id FROM new_report), null, null, null);