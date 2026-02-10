DELETE
FROM sprachkenntnis;
ALTER TABLE sprachkenntnis
    ADD COLUMN bewertung_coach INTEGER REFERENCES sprachkenntnis_niveau (id);
ALTER TABLE sprachkenntnis
    ADD COLUMN bewertung_datum DATE;
ALTER TABLE sprachkenntnis
    ADD COLUMN teilnehmer INTEGER NOT NULL REFERENCES teilnehmer (id);

DELETE
FROM sprachkenntnis_history;
ALTER TABLE sprachkenntnis_history
    ADD COLUMN bewertung_coach INTEGER;
ALTER TABLE sprachkenntnis_history
    ADD COLUMN bewertung_datum DATE;
ALTER TABLE sprachkenntnis_history
    ADD COLUMN teilnehmer INTEGER NOT NULL;

CREATE
    OR REPLACE FUNCTION sprachkenntnis_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
        TG_OP = 'DELETE' THEN
        INSERT INTO sprachkenntnis_history (sprachkenntnis_id, sprache, sprache_niveau, created_on, created_by,
                                            changed_on, changed_by,
                                            action, action_timestamp, bewertung_coach, bewertung_datum, teilnehmer)
        VALUES (OLD.id,
                OLD.sprache,
                OLD.sprache_niveau,
                OLD.created_on,
                OLD.created_by,
                OLD.changed_on,
                OLD.changed_by,
                'D', NOW(),
                OLD.bewertung_coach,
                OLD.bewertung_datum,
                OLD.teilnehmer);
        RETURN OLD;
    ELSIF
        TG_OP = 'UPDATE' THEN
        INSERT INTO sprachkenntnis_history (sprachkenntnis_id, sprache, sprache_niveau, created_on, created_by,
                                            changed_on, changed_by,
                                            action, action_timestamp, bewertung_coach, bewertung_datum, teilnehmer)
        VALUES (NEW.id,
                NEW.sprache,
                NEW.sprache_niveau,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by,
                'U', NOW(),
                NEW.bewertung_coach,
                NEW.bewertung_datum,
                NEW.teilnehmer);
        RETURN NEW;
    ELSIF
        TG_OP = 'INSERT' THEN
        INSERT INTO sprachkenntnis_history (sprachkenntnis_id, sprache, sprache_niveau, created_on, created_by,
                                            changed_on, changed_by,
                                            action, action_timestamp, bewertung_coach, bewertung_datum, teilnehmer)
        VALUES (NEW.id,
                NEW.sprache,
                NEW.sprache_niveau,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by,
                'I', NOW(),
                NEW.bewertung_coach,
                NEW.bewertung_datum,
                NEW.teilnehmer);

        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

ALTER FUNCTION sprachkenntnis_audit() OWNER TO ibosng;

ALTER TABLE teilnehmer_data_status
    DROP COLUMN teilnehmer2sprachkenntnis;
ALTER TABLE teilnehmer_data_status_history
    DROP COLUMN teilnehmer2sprachkenntnis;
ALTER TABLE teilnehmer_data_status
    DROP COLUMN data_type;

CREATE
    OR REPLACE FUNCTION teilnehmer_data_status_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_data_status_history (teilnehmer_data_status_id, teilnehmer,
                                                    error, cause,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (OLD.id, OLD.teilnehmer, OLD.error,
                OLD.cause, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_data_status_history (teilnehmer_data_status_id, teilnehmer,
                                                    error, cause,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer, NEW.error,
                NEW.cause, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_data_status_history (teilnehmer_data_status_id, teilnehmer,
                                                    error, cause,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer, NEW.error,
                NEW.cause, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

ALTER FUNCTION teilnehmer_data_status_audit() OWNER TO ibosng;

DROP TABLE teilnehmer_2_sprachkenntnis;
DROP TABLE teilnehmer_2_sprachkenntnis_history;
DROP FUNCTION teilnehmer_2_sprachkenntnis_audit;

INSERT INTO seminar_pruefung_niveau (name)
VALUES ('PSA');

DELETE
FROM sprachkenntnis_niveau
WHERE name IN ('C1', 'C2');

INSERT INTO sprachkenntnis_niveau (name)
VALUES ('PSA');

create table seminar_gesamtbeurteilung
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    type       INTEGER REFERENCES seminar_pruefung_gegenstand (id),
    ergebnis   INTEGER REFERENCES seminar_pruefung_ergebnis_type (id),
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by TEXT      DEFAULT CURRENT_USER      NOT NULL,
    changed_on TIMESTAMP,
    changed_by TEXT
);

CREATE TABLE seminar_gesamtbeurteilung_history
(
    id                           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    seminar_gesamtbeurteilung_id INTEGER                             NOT NULL,
    type                         INTEGER,
    ergebnis                     INTEGER,
    created_on                   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by                   TEXT      DEFAULT CURRENT_USER      NOT NULL,
    changed_on                   TIMESTAMP,
    changed_by                   TEXT,
    action                       CHAR,
    action_timestamp             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE
    OR REPLACE FUNCTION seminar_gesamtbeurteilung_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
        TG_OP = 'DELETE' THEN
        INSERT INTO seminar_gesamtbeurteilung_history (seminar_gesamtbeurteilung_id, type, ergebnis,
                                                       created_on,
                                                       created_by,
                                                       changed_on, changed_by,
                                                       action, action_timestamp)
        VALUES (OLD.id,
                OLD.type,
                OLD.ergebnis,
                OLD.created_on,
                OLD.created_by,
                OLD.changed_on,
                OLD.changed_by,
                'D', NOW());
        RETURN OLD;
    ELSIF
        TG_OP = 'UPDATE' THEN
        INSERT INTO seminar_gesamtbeurteilung_history (seminar_gesamtbeurteilung_id, type, ergebnis,
                                                       created_on,
                                                       created_by,
                                                       changed_on, changed_by,
                                                       action, action_timestamp)
        VALUES (NEW.id,
                NEW.type,
                NEW.ergebnis,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'U', NOW());
        RETURN NEW;
    ELSIF
        TG_OP = 'INSERT' THEN
        INSERT INTO seminar_gesamtbeurteilung_history (seminar_gesamtbeurteilung_id, type, ergebnis,
                                                       created_on,
                                                       created_by,
                                                       changed_on, changed_by,
                                                       action, action_timestamp)
        VALUES (NEW.id,
                NEW.type,
                NEW.ergebnis,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'I', NOW());

        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

ALTER FUNCTION seminar_gesamtbeurteilung_audit() OWNER TO ibosng;

CREATE TRIGGER seminar_gesamtbeurteilung_insert
    AFTER INSERT
    ON seminar_gesamtbeurteilung
    FOR EACH ROW
EXECUTE PROCEDURE seminar_gesamtbeurteilung_audit();

CREATE TRIGGER seminar_gesamtbeurteilung_update
    AFTER UPDATE
    ON seminar_gesamtbeurteilung
    FOR EACH ROW
EXECUTE PROCEDURE seminar_gesamtbeurteilung_audit();

CREATE TRIGGER seminar_gesamtbeurteilung_delete
    AFTER DELETE
    ON seminar_gesamtbeurteilung
    FOR EACH ROW
EXECUTE PROCEDURE seminar_gesamtbeurteilung_audit();

ALTER TABLE teilnehmer_2_seminar
    ADD COLUMN gesamtbeurteilung INTEGER REFERENCES seminar_gesamtbeurteilung (id);
ALTER TABLE teilnehmer_2_seminar_history
    ADD COLUMN gesamtbeurteilung INTEGER,
    ADD COLUMN created_by        TEXT DEFAULT CURRENT_USER NOT NULL,
    ADD COLUMN changed_on        TIMESTAMP,
    ADD COLUMN changed_by        TEXT;

CREATE OR REPLACE FUNCTION teilnehmer_2_seminar_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, teilnahme_von, teilnahme_bis, zubuchung,
                                                  rgs, status, import_filename,
                                                  created_on, austrittsgrund, begehren_bis, zusaetzliche_unterstuetzung,
                                                  lernfortschritt, fruehwarnung, anteil_anwesenheit,
                                                  action,
                                                  action_timestamp, gesamtbeurteilung, created_by, changed_on, changed_by)
        VALUES (OLD.id, OLD.teilnehmer_id, OLD.seminar_id, OLD.betreuer, OLD.buchungsstatus, OLD.anmerkung,
                OLD.massnahmennummer, OLD.veranstaltungsnummer, OLD.geplant, OLD.eintritt, OLD.austritt,
                OLD.teilnahme_von, OLD.teilnahme_bis, OLD.zubuchung,
                OLD.rgs, OLD.status, OLD.import_filename, OLD.created_on, OLD.austrittsgrund, OLD.begehren_bis,
                OLD.zusaetzliche_unterstuetzung, OLD.lernfortschritt, OLD.fruehwarnung, OLD.anteil_anwesenheit, 'D',
                now(), OLD.gesamtbeurteilung, OLD.created_by, OLD.changed_on, OLD.changed_by);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, teilnahme_von, teilnahme_bis, zubuchung,
                                                  rgs, status, import_filename,
                                                  created_on, austrittsgrund, begehren_bis, zusaetzliche_unterstuetzung,
                                                  lernfortschritt, fruehwarnung, anteil_anwesenheit,
                                                  action,
                                                  action_timestamp, gesamtbeurteilung, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.betreuer, NEW.buchungsstatus, NEW.anmerkung,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.geplant, NEW.eintritt, NEW.austritt,
                NEW.teilnahme_von, NEW.teilnahme_bis, NEW.zubuchung,
                NEW.rgs, NEW.status, NEW.import_filename, NEW.created_on, NEW.austrittsgrund, NEW.begehren_bis,
                NEW.zusaetzliche_unterstuetzung, NEW.lernfortschritt, NEW.fruehwarnung, NEW.anteil_anwesenheit, 'U',
                now(), NEW.gesamtbeurteilung, NEW.created_by, NEW.changed_on, NEW.changed_by);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, teilnahme_von, teilnahme_bis, zubuchung,
                                                  rgs, status, import_filename,
                                                  created_on, austrittsgrund, begehren_bis, zusaetzliche_unterstuetzung,
                                                  lernfortschritt, fruehwarnung, anteil_anwesenheit,
                                                  action,
                                                  action_timestamp, gesamtbeurteilung, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.betreuer, NEW.buchungsstatus, NEW.anmerkung,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.geplant, NEW.eintritt, NEW.austritt,
                NEW.teilnahme_von, NEW.teilnahme_bis, NEW.zubuchung,
                NEW.rgs, NEW.status, NEW.import_filename, NEW.created_on, NEW.austrittsgrund, NEW.begehren_bis,
                NEW.zusaetzliche_unterstuetzung, NEW.lernfortschritt, NEW.fruehwarnung, NEW.anteil_anwesenheit, 'I',
                now(), NEW.gesamtbeurteilung, NEW.created_by, NEW.changed_on, NEW.changed_by);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function teilnehmer_2_seminar_audit() owner to ibosng;

ALTER TABLE teilnehmer
    ADD COLUMN vermittelbar_ausserhalb_ams           BOOLEAN   DEFAULT FALSE;

ALTER TABLE teilnehmer_history
    ADD COLUMN vermittelbar_ausserhalb_ams           BOOLEAN;

create or replace function teilnehmer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp, anrede, ursprung, ziel, vermittelbar_ab, vermittlungs_notiz, vermittelbar_ausserhalb_ams)
        VALUES (OLD.titel, OLD.id, OLD.nachname, OLD.vorname, OLD.geschlecht,
                OLD.sv_nummer, OLD.geburtsdatum, OLD.adresse,
                OLD.email, OLD.status, OLD.import_filename, OLD.info, OLD.created_on, OLD.created_by, now(),
                OLD.changed_by,
                'D', now(), OLD.anrede, OLD.ursprung, OLD.ziel, OLD.vermittelbar_ab, OLD.vermittlungs_notiz, OLD.vermittelbar_ausserhalb_ams);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp, anrede, ursprung, ziel, vermittelbar_ab, vermittlungs_notiz, vermittelbar_ausserhalb_ams)
        VALUES (NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.created_on, NEW.created_by, now(),
                NEW.changed_by,
                'U', now(), NEW.anrede, NEW.ursprung, NEW.ziel, NEW.vermittelbar_ab, NEW.vermittlungs_notiz, NEW.vermittelbar_ausserhalb_ams);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp, anrede, ursprung, ziel, vermittelbar_ab, vermittlungs_notiz, vermittelbar_ausserhalb_ams)
        VALUES (NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.created_on, NEW.created_by, now(),
                NULL,
                'I', now(), NEW.anrede, NEW.ursprung, NEW.ziel, NEW.vermittelbar_ab, NEW.vermittlungs_notiz, NEW.vermittelbar_ausserhalb_ams);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function teilnehmer_audit() owner to ibosng;

