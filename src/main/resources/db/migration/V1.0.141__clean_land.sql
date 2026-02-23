UPDATE adresse
SET land = (
    CASE
        WHEN land = (select id from land where land_name = 'Vereinigtes Königreich') THEN (select id from land where land_name = 'Großbritannien')
        WHEN land = (select id from land where land_name = 'Abchasien') THEN (select id from land where land_name = 'Georgien (ohne Abchasien)')
        WHEN land = (select id from land where land_name = 'Saint-Pierre & Miquelon') THEN (select id from land where land_name = 'St.Pierre')
        WHEN land = (select id from land where land_name = 'Eswatini') THEN (select id from land where land_name = 'Swasiland')
        END
    )
WHERE land IN (select id from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini'));

UPDATE stammdaten
SET staatsbuergerschaft = (
    CASE
        WHEN staatsbuergerschaft = (select id from land where land_name = 'Vereinigtes Königreich') THEN (select id from land where land_name = 'Großbritannien')
        WHEN staatsbuergerschaft = (select id from land where land_name = 'Abchasien') THEN (select id from land where land_name = 'Georgien (ohne Abchasien)')
        WHEN staatsbuergerschaft = (select id from land where land_name = 'Saint-Pierre & Miquelon') THEN (select id from land where land_name = 'St.Pierre')
        WHEN staatsbuergerschaft = (select id from land where land_name = 'Eswatini') THEN (select id from land where land_name = 'Swasiland')
        END
    )
WHERE staatsbuergerschaft IN (select id from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini'));

UPDATE international_plz
SET land = (
    CASE
        WHEN land = (select id from land where land_name = 'Vereinigtes Königreich') THEN (select id from land where land_name = 'Großbritannien')
        WHEN land = (select id from land where land_name = 'Abchasien') THEN (select id from land where land_name = 'Georgien (ohne Abchasien)')
        WHEN land = (select id from land where land_name = 'Saint-Pierre & Miquelon') THEN (select id from land where land_name = 'St.Pierre')
        WHEN land = (select id from land where land_name = 'Eswatini') THEN (select id from land where land_name = 'Swasiland')
        END
    )
WHERE land IN (select id from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini'));

UPDATE telefon
SET land_id = (
    CASE
        WHEN land_id = (select id from land where land_name = 'Vereinigtes Königreich') THEN (select id from land where land_name = 'Großbritannien')
        WHEN land_id = (select id from land where land_name = 'Abchasien') THEN (select id from land where land_name = 'Georgien (ohne Abchasien)')
        WHEN land_id = (select id from land where land_name = 'Saint-Pierre & Miquelon') THEN (select id from land where land_name = 'St.Pierre')
        WHEN land_id = (select id from land where land_name = 'Eswatini') THEN (select id from land where land_name = 'Swasiland')
        END
    )
WHERE land_id IN (select id from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini'));

UPDATE vorwahl
SET land_vorwahl = (
    CASE
        WHEN land_vorwahl = (select id from land where land_name = 'Vereinigtes Königreich') THEN (select id from land where land_name = 'Großbritannien')
        WHEN land_vorwahl = (select id from land where land_name = 'Abchasien') THEN (select id from land where land_name = 'Georgien (ohne Abchasien)')
        WHEN land_vorwahl = (select id from land where land_name = 'Saint-Pierre & Miquelon') THEN (select id from land where land_name = 'St.Pierre')
        WHEN land_vorwahl = (select id from land where land_name = 'Eswatini') THEN (select id from land where land_name = 'Swasiland')
        END
    )
WHERE land_vorwahl IN (select id from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini'));

UPDATE bankverbindung
SET land = (
    CASE
        WHEN land = (select id from land where land_name = 'Vereinigtes Königreich') THEN (select id from land where land_name = 'Großbritannien')
        WHEN land = (select id from land where land_name = 'Abchasien') THEN (select id from land where land_name = 'Georgien (ohne Abchasien)')
        WHEN land = (select id from land where land_name = 'Saint-Pierre & Miquelon') THEN (select id from land where land_name = 'St.Pierre')
        WHEN land = (select id from land where land_name = 'Eswatini') THEN (select id from land where land_name = 'Swasiland')
        END
    )
WHERE land IN (select id from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini'));

UPDATE teilnehmer_nations
SET land_id = (
    CASE
        WHEN land_id = (select id from land where land_name = 'Vereinigtes Königreich') THEN (select id from land where land_name = 'Großbritannien')
        WHEN land_id = (select id from land where land_name = 'Abchasien') THEN (select id from land where land_name = 'Georgien (ohne Abchasien)')
        WHEN land_id = (select id from land where land_name = 'Saint-Pierre & Miquelon') THEN (select id from land where land_name = 'St.Pierre')
        WHEN land_id = (select id from land where land_name = 'Eswatini') THEN (select id from land where land_name = 'Swasiland')
        END
    )
WHERE land_id IN (select id from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini'));

UPDATE kommunalsteuergemeinde
SET lhr_land = (
    CASE
        WHEN lhr_land = (select id from land where land_name = 'Vereinigtes Königreich') THEN (select id from land where land_name = 'Großbritannien')
        WHEN lhr_land = (select id from land where land_name = 'Abchasien') THEN (select id from land where land_name = 'Georgien (ohne Abchasien)')
        WHEN lhr_land = (select id from land where land_name = 'Saint-Pierre & Miquelon') THEN (select id from land where land_name = 'St.Pierre')
        WHEN lhr_land = (select id from land where land_name = 'Eswatini') THEN (select id from land where land_name = 'Swasiland')
        END
    )
WHERE lhr_land IN (select id from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini'));

UPDATE bank_daten
SET land = (
    CASE
        WHEN land = (select id from land where land_name = 'Vereinigtes Königreich') THEN (select id from land where land_name = 'Großbritannien')
        WHEN land = (select id from land where land_name = 'Abchasien') THEN (select id from land where land_name = 'Georgien (ohne Abchasien)')
        WHEN land = (select id from land where land_name = 'Saint-Pierre & Miquelon') THEN (select id from land where land_name = 'St.Pierre')
        WHEN land = (select id from land where land_name = 'Eswatini') THEN (select id from land where land_name = 'Swasiland')
        END
    )
WHERE land IN (select id from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini'));

delete from land where land_name in ('Vereinigtes Königreich', 'Abchasien', 'Saint-Pierre & Miquelon', 'Eswatini');