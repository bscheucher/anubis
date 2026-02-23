ALTER TABLE teilnehmer_notiz
    ALTER COLUMN kategorie DROP NOT NULL;
ALTER TABLE teilnehmer_notiz_history
    ALTER COLUMN kategorie DROP NOT NULL;

ALTER TABLE teilnehmer
    RENAME COLUMN notiz TO vermittlungs_notiz;
ALTER TABLE teilnehmer_history
    RENAME COLUMN notiz TO vermittlungs_notiz;

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
                                        action, action_timestamp, anrede, ursprung, ziel, vermittelbar_ab, vermittlungs_notiz)
        VALUES (OLD.titel, OLD.id, OLD.nachname, OLD.vorname, OLD.geschlecht,
                OLD.sv_nummer, OLD.geburtsdatum, OLD.adresse,
                OLD.email, OLD.status, OLD.import_filename, OLD.info, OLD.created_on, OLD.created_by, now(),
                OLD.changed_by,
                'D', now(), OLD.anrede, OLD.ursprung, OLD.ziel, OLD.vermittelbar_ab, OLD.vermittlungs_notiz);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp, anrede, ursprung, ziel, vermittelbar_ab, vermittlungs_notiz)
        VALUES (NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.created_on, NEW.created_by, now(),
                NEW.changed_by,
                'U', now(), NEW.anrede, NEW.ursprung, NEW.ziel, NEW.vermittelbar_ab, NEW.vermittlungs_notiz);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp, anrede, ursprung, ziel, vermittelbar_ab, vermittlungs_notiz)
        VALUES (NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.created_on, NEW.created_by, now(),
                NULL,
                'I', now(), NEW.anrede, NEW.ursprung, NEW.ziel, NEW.vermittelbar_ab, NEW.vermittlungs_notiz);
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function teilnehmer_audit() owner to ibosng;


create table teilnehmer_data_status
(
    id                        integer generated always as identity
        primary key,
    teilnehmer                integer
        references teilnehmer,
    teilnehmer2sprachkenntnis integer references teilnehmer_2_sprachkenntnis,
    teilnehmernotiz           integer references teilnehmer_notiz,
    data_type                 TEXT                                NOT NULL,
    error                     text,
    cause                     text,
    created_on                timestamp default CURRENT_TIMESTAMP not null,
    created_by                text                                not null,
    changed_on                timestamp,
    changed_by                text
);

create table teilnehmer_data_status_history
(
    id                        integer generated always as identity
        primary key,
    teilnehmer_data_status_id integer   not null,
    teilnehmer                integer,
    teilnehmer2sprachkenntnis integer,
    teilnehmernotiz           integer,
    error                     text,
    cause                     text,
    created_on                timestamp not null,
    created_by                text      not null,
    changed_on                timestamp,
    changed_by                text,
    action                    char,
    action_timestamp          timestamp not null
);


CREATE
    OR REPLACE FUNCTION teilnehmer_data_status_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_data_status_history (teilnehmer_data_status_id, teilnehmer, teilnehmer2sprachkenntnis,
                                                    teilnehmernotiz,
                                                    error, cause,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (OLD.id, OLD.teilnehmer, OLD.teilnehmer2sprachkenntnis, OLD.teilnehmernotiz, OLD.error,
                OLD.cause, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_data_status_history (teilnehmer_data_status_id, teilnehmer, teilnehmer2sprachkenntnis,
                                                    teilnehmernotiz,
                                                    error, cause,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer, NEW.teilnehmer2sprachkenntnis, NEW.teilnehmernotiz, NEW.error,
                NEW.cause, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_data_status_history (teilnehmer_data_status_id, teilnehmer, teilnehmer2sprachkenntnis,
                                                    teilnehmernotiz,
                                                    error, cause,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer, NEW.teilnehmer2sprachkenntnis, NEW.teilnehmernotiz, NEW.error,
                NEW.cause, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

ALTER FUNCTION teilnehmer_data_status_audit() OWNER TO ibosng;

CREATE TRIGGER teilnehmer_data_status_insert
    AFTER INSERT
    ON teilnehmer_data_status
    FOR EACH ROW
EXECUTE PROCEDURE teilnehmer_data_status_audit();

CREATE TRIGGER teilnehmer_data_status_update
    AFTER UPDATE
    ON teilnehmer_data_status
    FOR EACH ROW
EXECUTE PROCEDURE teilnehmer_data_status_audit();

CREATE TRIGGER teilnehmer_data_status_delete
    AFTER DELETE
    ON teilnehmer_data_status
    FOR EACH ROW
EXECUTE PROCEDURE teilnehmer_data_status_audit();


INSERT INTO teilnehmer_data_status (teilnehmer, error, cause, created_on, created_by, data_type)
SELECT ds.teilnehmer_id,
       CASE ds.data_status_value
           WHEN 0 THEN 'titel'
           WHEN 1 THEN 'vorname'
           WHEN 2 THEN 'nachname'
           WHEN 3 THEN 'geschlecht'
           WHEN 4 THEN 'sv_nummer'
           WHEN 5 THEN 'geburtsdatum'
           WHEN 6 THEN 'buchungsstatus'
           WHEN 7 THEN 'anmerkung'
           WHEN 8 THEN 'zubuchung'
           WHEN 9 THEN 'geplant'
           WHEN 10 THEN 'eintritt'
           WHEN 11 THEN 'austritt'
           WHEN 12 THEN 'rgs'
           WHEN 13 THEN 'maßnahmennummer'
           WHEN 14 THEN 'veranstaltungsnummer'
           WHEN 15 THEN 'email'
           WHEN 16 THEN 'betreuer_titel'
           WHEN 17 THEN 'betreuer_vorname'
           WHEN 18 THEN 'betreuer_nachname'
           WHEN 19 THEN 'telefon'
           WHEN 20 THEN 'plz'
           WHEN 21 THEN 'ort'
           WHEN 22 THEN 'plz_ort'
           WHEN 23 THEN 'strasse'
           WHEN 24 THEN 'nation'
           WHEN 25 THEN 'seminar'
           WHEN 26 THEN 'anrede'
           WHEN 27 THEN 'svn_already_existing'
           END AS error,
       CASE ds.data_status_value
           WHEN 0 THEN 'Ungültiger Titel angegeben'
           WHEN 1 THEN 'Ungültiger Vorname angegeben'
           WHEN 2 THEN 'Ungültiger Nachname angegeben'
           WHEN 3 THEN 'Ungültiges Geschlecht angegeben'
           WHEN 4 THEN 'Ungültige SV-Nummer angegeben'
           WHEN 5 THEN 'Ungültiges Geburtsdatum angegeben'
           WHEN 6 THEN 'Ungültiger Buchungsstatus angegeben'
           WHEN 7 THEN 'Ungültige Anmerkung angegeben'
           WHEN 8 THEN 'Ungültige Zubuchung angegeben'
           WHEN 9 THEN 'Ungültige Planung angegeben'
           WHEN 10 THEN 'Ungültiger Eintritt angegeben'
           WHEN 11 THEN 'Ungültiger Austritt angegeben'
           WHEN 12 THEN 'Ungültige RGS angegeben'
           WHEN 13 THEN 'Ungültige Maßnahmennummer angegeben'
           WHEN 14 THEN 'Ungültige Veranstaltungsnummer angegeben'
           WHEN 15 THEN 'Ungültige E-Mail-Adresse angegeben'
           WHEN 16 THEN 'Ungültiger Betreuer-Titel angegeben'
           WHEN 17 THEN 'Ungültiger Betreuer-Vorname angegeben'
           WHEN 18 THEN 'Ungültiger Betreuer-Nachname angegeben'
           WHEN 19 THEN 'Ungültige Telefonnummer angegeben'
           WHEN 20 THEN 'Ungültige PLZ angegeben'
           WHEN 21 THEN 'Ungültiger Ort angegeben'
           WHEN 22 THEN 'Ungültige Zuordnung von PLZ und Ort'
           WHEN 23 THEN 'Ungültige Straße angegeben'
           WHEN 24 THEN 'Ungültige Nation angegeben'
           WHEN 25 THEN 'Ungültiges Seminar angegeben'
           WHEN 26 THEN 'Ungültige Anrede angegeben'
           WHEN 27 THEN 'SVN bereits vorhanden'
           END AS cause,
       CURRENT_TIMESTAMP,
       'Validation Service',
       'TeilnehmerDataStatus'
FROM data_status ds;


INSERT INTO sprachkenntnis (sprache, sprache_niveau)
SELECT m.id, n.id
FROM muttersprache m
         CROSS JOIN sprachkenntnis_niveau n
UNION
SELECT m.id, NULL
FROM muttersprache m;
