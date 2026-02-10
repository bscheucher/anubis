DO
$$
    DECLARE
        new_adresse_id INT;
    BEGIN
        -- Güssing Technologiezentrum
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 7540),
                        (SELECT id FROM plz WHERE plz = 7540),
                        'Technologiezentrum',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Güssing Technologiezentrum';

        -- Hall - KAOS
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6060),
                        (SELECT id FROM plz WHERE plz = 6060),
                        'Untere Lend 12a',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Hall - Untere Lend 12a - KAOS';

        -- Innsbruck locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Dörrstraße 85',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck Dörrstraße 85';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Maria-Theresien-Straße 40',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck Maria-Theresien-Straße 40';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Hypo Passage 2',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck, Hypo Passage 2';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Sebastian-Kneipp-Weg 27',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck, Sebastian-Kneipp-Weg 27';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Südtirolerplatz 14-16',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck, Südtirolerplatz 14-16';

        -- Krems
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3500),
                        (SELECT id FROM plz WHERE plz = 3500),
                        'Dr. Karl Dorrekstraße 20',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Krems (Dr. Karl Dorrekstraße 20)';

        -- Linz
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4020),
                        (SELECT id FROM plz WHERE plz = 4020),
                        'Paul-Hahn-Straße 1-3',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Linz Paul-Hahn-Straße 1-3';

        -- St. Johann/P.
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 5600),
                        (SELECT id FROM plz WHERE plz = 5600),
                        'Liechtensteinklammstr. 11',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'St. Johann/P. (Liechtensteinklammstr. 11)';

        -- St. Johann/T.
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6380),
                        (SELECT id FROM plz WHERE plz = 6380),
                        'Bahnhofstraße 3',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'St. Johann/T. Bahnhofstraße 3 - KAOS';

        -- Tamsweg
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 5580),
                        (SELECT id FROM plz WHERE plz = 5580),
                        'Q4 - Postplatz 4/3/22 & 23',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Tamsweg Q4 - Postplatz 4/3/22 & 23';

        -- Tulln
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3430),
                        (SELECT id FROM plz WHERE plz = 3430),
                        'Frauenhoferstraße 1-3',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Tulln (Frauenhoferstraße 1-3)';

        -- Waidhofen
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3830),
                        (SELECT id FROM plz WHERE plz = 3830),
                        'Raiffeisenpromenade 2/9',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Waidhofen a.d.Thaya (Raiffeisenpromenade 2/9)';

        -- Wien locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 1110),
                        (SELECT id FROM plz WHERE plz = 1110),
                        'Geiselbergstraße 15-19',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wien (Geiselbergstraße 15-19)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 1060),
                        (SELECT id FROM plz WHERE plz = 1060),
                        'Gumpendorfer Gürtel 2B',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wien (Gumpendorfer Gürtel 2B)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 1210),
                        (SELECT id FROM plz WHERE plz = 1210),
                        'Ignaz Köck Straße 17',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wien (Ignaz Köck Straße 17)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 1100),
                        (SELECT id FROM plz WHERE plz = 1100),
                        'Oberlaaerstraße 276, Obj. 3',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wien (Oberlaaerstraße 276, Obj. 3)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 1020),
                        (SELECT id FROM plz WHERE plz = 1020),
                        'Praterstraße 62',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wien (Praterstraße 62)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 1100),
                        (SELECT id FROM plz WHERE plz = 1100),
                        'Quellenstraße 2c, Gudrunstraße 11',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wien Quellenstraße 2c, Gudrunstraße 11';

        -- Wörgl locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6300),
                        (SELECT id FROM plz WHERE plz = 6300),
                        'Josef Speckbacher-Straße 7+9',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wörgl Josef Speckbacher-Straße 7+9';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6300),
                        (SELECT id FROM plz WHERE plz = 6300),
                        'Rainerstraße 4',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wörgl Rainerstraße 4';

        -- Wiener Neustadt locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 2700),
                        (SELECT id FROM plz WHERE plz = 2700),
                        'Badener Str.12/Josef Mohr-Gasse7',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wr. Neustadt (Badener Str.12/Josef Mohr-Gasse7)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 2700),
                        (SELECT id FROM plz WHERE plz = 2700),
                        'Stadionstraße 36',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wr. Neustadt (Stadionstraße 36)';

-- Amstetten
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3300),
                        (SELECT id FROM plz WHERE plz = 3300),
                        'Waidhofener Str. 42-42b',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Amstetten (Waidhofener Str. 42-42b)';

        -- Baden
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 2500),
                        (SELECT id FROM plz WHERE plz = 2500),
                        'Goethegasse 14',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Baden (Goethegasse 14)';

        -- Bischofshofen
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 5500),
                        (SELECT id FROM plz WHERE plz = 5500),
                        'Salzburger Straße 46',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Bischofshofen (Salzburger Straße 46)';

        -- Bludenz locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6700),
                        (SELECT id FROM plz WHERE plz = 6700),
                        'Bahnhofplatz 1c -Rätikoncenter',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Bludenz (Bahnhofplatz 1c -Rätikoncenter)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6700),
                        (SELECT id FROM plz WHERE plz = 6700),
                        'Untersteinstraße 8',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Bludenz (Untersteinstraße 8)';

        -- Bregenz
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6900),
                        (SELECT id FROM plz WHERE plz = 6900),
                        'Bachgasse 1a',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Bregenz (Bachgasse 1a)';

        -- Bruck an der Leitha
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 2460),
                        (SELECT id FROM plz WHERE plz = 2460),
                        'Leithagürtel 2',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Bruck an der Leitha (Leithagürtel 2)';

        -- Dornbirn locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6850),
                        (SELECT id FROM plz WHERE plz = 6850),
                        'Bildgasse 18, Top 3',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Dornbirn (Bildgasse 18, Top 3)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6850),
                        (SELECT id FROM plz WHERE plz = 6850),
                        'Schulgasse 18',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Dornbirn (Schulgasse 18)';

        -- Eisenstadt
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 7000),
                        (SELECT id FROM plz WHERE plz = 7000),
                        'Esterhazystraße 22/4',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Eisenstadt (Esterhazystraße 22/4)';

        -- Feldkirch locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6800),
                        (SELECT id FROM plz WHERE plz = 6800),
                        'Albert-Schädler-Straße 12',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Feldkirch (Albert-Schädler-Straße 12)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6800),
                        (SELECT id FROM plz WHERE plz = 6800),
                        'Beim Gräble 2 - Lamello',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Feldkirch (Beim Gräble 2 - Lamello)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6800),
                        (SELECT id FROM plz WHERE plz = 6800),
                        'Schießstätte 12, Ganahl-Areal',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Feldkirch (Schießstätte 12, Ganahl-Areal)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6800),
                        (SELECT id FROM plz WHERE plz = 6800),
                        'Schießstätte 16, Ganahl-Areal',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Feldkirch (Schießstätte 16, Ganahl-Areal)';

        -- Freistadt
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4240),
                        (SELECT id FROM plz WHERE plz = 4240),
                        'St. Peter Str. 7',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Freistadt (St. Peter Str. 7)';

        -- Gmünd
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3950),
                        (SELECT id FROM plz WHERE plz = 3950),
                        'Zweiländerstraße 8',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Gmünd (Zweiländerstraße 8)';

        -- Grieskirchen
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4710),
                        (SELECT id FROM plz WHERE plz = 4710),
                        'Trattnachtalstraße 5-7',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Grieskirchen (Trattnachtalstraße 5-7)';

        -- Hallein
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 5400),
                        (SELECT id FROM plz WHERE plz = 5400),
                        'Löwensternstraße 18',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Hallein (Löwensternstraße 18)';

        -- Hollabrunn
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 2020),
                        (SELECT id FROM plz WHERE plz = 2020),
                        'Hauptplatz12',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Hollabrunn (Hauptplatz12)';

        -- Horn
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3580),
                        (SELECT id FROM plz WHERE plz = 3580),
                        'Wiener Straße 2',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Horn (Wiener Straße 2)';

        -- Imst locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6460),
                        (SELECT id FROM plz WHERE plz = 6460),
                        'Kramergasse 9-11',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Imst (Kramergasse 9-11)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6460),
                        (SELECT id FROM plz WHERE plz = 6460),
                        'Kramerg.11',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Imst - Kramerg.11 - KAOS';

        -- Additional Innsbruck locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Eduard Bodem Gasse 8',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck (Eduard Bodem Gasse 8)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Fürstenweg 87',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck (Fürstenweg 87)';


-- Continuing Innsbruck locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'J. Thomanstraße 12',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)

        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck (J. Thomanstraße 12)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Kranebitter Allee 97',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck (Kranebitter Allee 97)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Leopoldstr.31a',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck (Leopoldstr.31a)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Leopoldstraße 24',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck (Leopoldstraße 24)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Resselstraße 18',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck (Resselstraße 18)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Andreas-Hofer-Straße 44',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck - Andreas-Hofer-Straße 44 - KAOS';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Mitterweg 25a',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck - Mitterweg 25a - KAOS';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6020),
                        (SELECT id FROM plz WHERE plz = 6020),
                        'Südtiroler Platz 14-16',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Innsbruck - Südtiroler Platz 14-16 - KAOS';

        -- Jenbach
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6200),
                        (SELECT id FROM plz WHERE plz = 6200),
                        'Austraße 21',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Jenbach (Austraße 21)';

-- Kitzbühel
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6370),
                        (SELECT id FROM plz WHERE plz = 6370),
                        'Achenweg 22',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Kitzbühel (Achenweg 22) KAOS';

-- Kufstein locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6330),
                        (SELECT id FROM plz WHERE plz = 6330),
                        'Kreuzgasse 3, Top 2',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Kufstein (Kreuzgasse 3) Top 2 - KAOS';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6330),
                        (SELECT id FROM plz WHERE plz = 6330),
                        'Kinkstr. 12',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Kufstein - Kinkstr. 12 - KAOS';

-- Landeck locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6500),
                        (SELECT id FROM plz WHERE plz = 6500),
                        'Bruggfeldstraße 5',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Landeck (Bruggfeldstraße 5)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6500),
                        (SELECT id FROM plz WHERE plz = 6500),
                        'Flirstraße 30a',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Landeck (Flirstraße 30a) -KAOS';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6500),
                        (SELECT id FROM plz WHERE plz = 6500),
                        'Schrofensteinstraße 1',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Landeck (Schrofensteinstraße 1)  - KAOS';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6500),
                        (SELECT id FROM plz WHERE plz = 6500),
                        'Römerstr. 14',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Landeck - Römerstr. 14 - KAOS';

-- Lienz locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 9900),
                        (SELECT id FROM plz WHERE plz = 9900),
                        'Schweizergasse 18',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Lienz (Schweizergasse 18)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 9900),
                        (SELECT id FROM plz WHERE plz = 9900),
                        'Kärntner-Str. 42',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Lienz - Kärntner-Str. 42 - KAOS';

-- Linz additional location
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4020),
                        (SELECT id FROM plz WHERE plz = 4020),
                        'Hafferlstraße 7',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Linz (Hafferlstraße 7)';

-- Melk
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3390),
                        (SELECT id FROM plz WHERE plz = 3390),
                        'Wiener Straße 45',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Melk (Wiener Straße 45)';

-- Reutte locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6600),
                        (SELECT id FROM plz WHERE plz = 6600),
                        'Lindenstr. 35',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Reutte (Lindenstr. 35) - KAOS';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6600),
                        (SELECT id FROM plz WHERE plz = 6600),
                        'Lindenstraße 35',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Reutte (Lindenstraße 35)';

-- Salzburg locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 5020),
                        (SELECT id FROM plz WHERE plz = 5020),
                        'Eichstraße 3',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Salzburg (Eichstraße 3)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 5020),
                        (SELECT id FROM plz WHERE plz = 5020),
                        'Münchner Bundesstr.158-160/2.OG',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Salzburg (Münchner Bundesstr.158-160/2.OG)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 5020),
                        (SELECT id FROM plz WHERE plz = 5020),
                        'Sterneckstr. 52/2.OG+',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Salzburg (Sterneckstr. 52/2.OG+)';

-- Sattledt locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4642),
                        (SELECT id FROM plz WHERE plz = 4642),
                        'Hauptstr.41',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Sattledt (Hauptstr.41)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4642),
                        (SELECT id FROM plz WHERE plz = 4642),
                        'Hauptstraße 39a',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Sattledt (Hauptstraße 39a)';

-- Schärding
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4780),
                        (SELECT id FROM plz WHERE plz = 4780),
                        'Bahnhofstraße 57',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Schärding (Bahnhofstraße 57)';

-- Scheibbs
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3270),
                        (SELECT id FROM plz WHERE plz = 3270),
                        'Neubruck 2, Schloss Neubruck',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Scheibbs (Neubruck 2, Schloss Neubruck)';

-- Schwaz
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6130),
                        (SELECT id FROM plz WHERE plz = 6130),
                        'Münchnerstraße 48',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Schwaz (Münchnerstraße 48) KAOS';

-- Schwechat
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 2320),
                        (SELECT id FROM plz WHERE plz = 2320),
                        'Himbergerstraße 66',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Schwechat (Himbergerstraße 66)';

-- St. Pölten locations
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3100),
                        (SELECT id FROM plz WHERE plz = 3100),
                        'Heinrich Schneidmadl Straße 15',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'St. Pölten (Heinrich Schneidmadl Straße 15)';

        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3100),
                        (SELECT id FROM plz WHERE plz = 3100),
                        'Schießstattring 31-33',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'St. Pölten (Schießstattring 31-33)';

-- Entry for Tamsweg
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 5580),
                        (SELECT id FROM plz WHERE plz = 5580),
                        'Josef-Ehrenreich-Straße 1',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Tamsweg (Josef-Ehrenreich-Straße 1)';

-- Entry for Vöcklabruck
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4840),
                        (SELECT id FROM plz WHERE plz = 4840),
                        'Gmundner Straße 47-49',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Vöcklabruck (Gmundner Straße 47-49)';

-- Entry for Wels (Bauernstraße)
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4600),
                        (SELECT id FROM plz WHERE plz = 4600),
                        'Bauernstraße 9, OG 03',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wels (Bauernstraße 9, OG 03)';

-- Entry for Wels (Durisolstraße)
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4600),
                        (SELECT id FROM plz WHERE plz = 4600),
                        'Durisolstraße 7',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wels (Durisolstraße 7)';

-- Entry for Wels (Lager-Puchnerstr)
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4600),
                        (SELECT id FROM plz WHERE plz = 4600),
                        'Lager-Puchnerstr. 54',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wels (Lager-Puchnerstr. 54)';

-- Entry for Wels (Maria-Theresia-Straße)
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 4600),
                        (SELECT id FROM plz WHERE plz = 4600),
                        'Maria-Theresia-Straße 53 WDZ 6',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wels (Maria-Theresia-Straße 53 WDZ 6)';

-- Entry for Wiener Neustadt
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 2700),
                        (SELECT id FROM plz WHERE plz = 2700),
                        'Stadion-Straße 36',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wiener Neustadt (Stadion-Straße 36)';

-- Entry for Wörgl (Giselastraße)
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6300),
                        (SELECT id FROM plz WHERE plz = 6300),
                        'Giselastraße 3',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wörgl (Giselastraße 3)';

-- Entry for Wörgl (Poststraße)
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6300),
                        (SELECT id FROM plz WHERE plz = 6300),
                        'Poststraße 6 a-c',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wörgl (Poststraße 6 a-c)';

-- Entry for Wörgl (Rainerstraße)
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6300),
                        (SELECT id FROM plz WHERE plz = 6300),
                        'Rainerstraße 5-9',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wörgl (Rainerstraße 5-9)';

-- Entry for Wörgl (Salzburgerstraße)
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6300),
                        (SELECT id FROM plz WHERE plz = 6300),
                        'Salzburgerstraße 53a',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wörgl (Salzburgerstraße 53a) KAOS';

-- Entry for KAOS Wörgl
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 6300),
                        (SELECT id FROM plz WHERE plz = 6300),
                        'Angatherweg 5b',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Wörgl - Angatherweg 5b - KAOS';

-- Entry for Zell am See
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 5700),
                        (SELECT id FROM plz WHERE plz = 5700),
                        'Brucker Bundesstraße 92',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Zell am See (Brucker Bundesstraße 92)';

-- Entry for Zwettl
        WITH new_address AS (
            INSERT INTO adresse (ort, plz, strasse, created_by, land)
                VALUES ((SELECT ort FROM plz WHERE plz = 3910),
                        (SELECT id FROM plz WHERE plz = 3910),
                        'Landstr.52',
                        current_user,
                        (SELECT id FROM land WHERE land_name = 'Österreich'))
                RETURNING id)
        UPDATE dienstort
        SET adresse = (SELECT id FROM new_address)
        WHERE name = 'Zwettl (Landstr.52)';
    END
$$;