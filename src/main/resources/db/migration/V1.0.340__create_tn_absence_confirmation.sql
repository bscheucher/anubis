CREATE TABLE IF NOT EXISTS tn_absence_confirmation
(
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vorname     VARCHAR(255)                        NOT NULL,
    nachname    VARCHAR(255)                        NOT NULL,
    sv_nummer   VARCHAR(10)                         NOT NULL,
    start_datum DATE                                NOT NULL,
    end_datum   DATE                                NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    );