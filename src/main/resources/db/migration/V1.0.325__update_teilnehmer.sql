ALTER TABLE teilnehmer
    ADD COLUMN muttersprache INT REFERENCES muttersprache(id);

ALTER TABLE teilnehmer_history
    ADD COLUMN muttersprache INT;

CREATE OR REPLACE FUNCTION teilnehmer_audit() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_history (
            titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
            adresse, email, status, import_filename, info, created_on, created_by,
            changed_on, changed_by, action, action_timestamp, anrede,
            personalnummer, muttersprache
        )
        VALUES (
            OLD.titel, OLD.id, OLD.nachname, OLD.vorname, OLD.geschlecht,
            OLD.sv_nummer, OLD.geburtsdatum, OLD.adresse,
            OLD.email, OLD.status, OLD.import_filename, OLD.info, OLD.created_on, OLD.created_by,
            now(), OLD.changed_by,
            'D', now(), OLD.anrede, OLD.personalnummer, OLD.muttersprache
        );
RETURN OLD;

ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_history (
            titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
            adresse, email, status, import_filename, info, created_on, created_by,
            changed_on, changed_by, action, action_timestamp, anrede,
            personalnummer, muttersprache
        )
        VALUES (
            NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
            NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
            NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.created_on, NEW.created_by,
            now(), NEW.changed_by,
            'U', now(), NEW.anrede, NEW.personalnummer, NEW.muttersprache
        );
RETURN NEW;

ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_history (
            titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
            adresse, email, status, import_filename, info, created_on, created_by,
            changed_on, changed_by, action, action_timestamp, anrede,
            personalnummer, muttersprache
        )
        VALUES (
            NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
            NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
            NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.created_on, NEW.created_by,
            now(), NULL,
            'I', now(), NEW.anrede, NEW.personalnummer, NEW.muttersprache
        );
RETURN NEW;
END IF;

RETURN NULL;
END;
$$;

ALTER FUNCTION teilnehmer_audit() OWNER TO ibosng;