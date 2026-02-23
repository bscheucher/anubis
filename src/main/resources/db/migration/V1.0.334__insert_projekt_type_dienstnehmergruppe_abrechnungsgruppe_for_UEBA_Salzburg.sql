INSERT INTO projekt_type (name, status, created_by)
VALUES ('ÜBA Salzburg', 1, current_user);

INSERT INTO dienstnehmergruppe (abbreviation, bezeichnung, created_on, created_by)
VALUES ('T_SBG', 'Salzburg', CURRENT_TIMESTAMP, current_user);

INSERT INTO abrechnungsgruppe (abbreviation, bezeichnung, created_on, created_by)
VALUES ('T_SBG', 'Tn Salzburg', CURRENT_TIMESTAMP, current_user);