WITH new_adresse AS (
INSERT
INTO adresse (ort, plz, strasse, land, created_by)
VALUES (
    'Bruck/Leitha',
    (select p.id from plz p where p.plz = 2460),
    'Kirchengasse 28',
    (SELECT id FROM land WHERE land_name = 'Österreich'),
    current_user
    )
    RETURNING id
    )
INSERT
INTO dienstort (name, adresse, status, created_on, created_by,
                lhr_nr, lhr_kz, short_name)
VALUES (
    'Bruck/Leitha Kirchengasse 28',
    (SELECT id FROM new_adresse),
    1,
    CURRENT_TIMESTAMP,
    current_user,
    151,
    'Bruck/Leitha Kirchengasse 28',
    'BruckK'
    );