ALTER TABLE teilnehmer
    ADD COLUMN has_bis_document BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE teilnehmer_history
    ADD COLUMN has_bis_document BOOLEAN NOT NULL DEFAULT FALSE;

CREATE OR REPLACE FUNCTION teilnehmer_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse, email, status, import_filename, info, is_ueba, has_bis_document, created_on, created_by,
                                        changed_on, changed_by, action, action_timestamp, anrede, personalnummer)
        VALUES (OLD.titel, OLD.id, OLD.nachname, OLD.vorname, OLD.geschlecht,
                OLD.sv_nummer, OLD.geburtsdatum, OLD.adresse, OLD.email, OLD.status, OLD.import_filename,
                OLD.info, OLD.is_ueba, OLD.has_bis_document, OLD.created_on, OLD.created_by, now(),
                OLD.changed_by, 'D', now(), OLD.anrede, OLD.personalnummer);
        RETURN OLD;

    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse, email, status, import_filename, info, is_ueba, has_bis_document, created_on, created_by,
                                        changed_on, changed_by, action, action_timestamp, anrede, personalnummer)
        VALUES (NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse, NEW.email, NEW.status, NEW.import_filename,
                NEW.info, NEW.is_ueba, NEW.has_bis_document, NEW.created_on, NEW.created_by, now(),
                NEW.changed_by, 'U', now(), NEW.anrede, NEW.personalnummer);
        RETURN NEW;

    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse, email, status, import_filename, info, is_ueba, has_bis_document, created_on, created_by,
                                        changed_on, changed_by, action, action_timestamp, anrede, personalnummer)
        VALUES (NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse, NEW.email, NEW.status, NEW.import_filename,
                NEW.info, NEW.is_ueba, NEW.has_bis_document, NEW.created_on, NEW.created_by, now(),
                NEW.changed_by, 'I', now(), NEW.anrede, NEW.personalnummer);
        RETURN NEW;
    END IF;

    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

-- Change the owner of the function to ibosng
    ALTER FUNCTION teilnehmer_audit() OWNER TO ibosng;
