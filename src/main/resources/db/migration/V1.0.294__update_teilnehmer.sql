DELETE FROM tn_ausbildung_type WHERE name = 'Akademie';

INSERT INTO tn_ausbildung_type (name)
VALUES ('Lehrabschlussprüfung'),
       ('Ohne Pflichtschulabschluss'),
       ('Keine Schulbildung'),
       ('Matura'),
       ('Studium (Uni, FH,…)'),
       ('Ausländ. Studium nicht anerkannt'),
       ('Colleg'),
       ('Fachschule'),
       ('Ausländ. Studium anerkannt');

DELETE FROM sprachkenntnis WHERE TRUE;

UPDATE teilnehmer_data_status
SET error = 'plz'
WHERE error = 'plz_ort';

UPDATE teilnehmer_data_status
SET error = 'svNummer'
WHERE error = 'sv_nummer';

UPDATE teilnehmer_data_status
SET error = 'svNummer'
WHERE error = 'svn_already_existing';