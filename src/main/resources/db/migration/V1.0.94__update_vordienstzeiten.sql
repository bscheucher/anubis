ALTER TABLE vordienstzeiten ADD COLUMN nachweis_file_name text;
ALTER TABLE vordienstzeiten_history ADD COLUMN nachweis_file_name text;

CREATE OR REPLACE FUNCTION vordienstzeiten_audit() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                                     wochenstunden, anrechenbar, nachweis_status, nachweis_file_name, status, created_on,
                                                     created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.vertragsart, OLD.firma, OLD.von, OLD.bis, OLD.wochenstunden,
                        OLD.anrechenbar, OLD.nachweis_status, OLD.nachweis_file_name, OLD.status, OLD.created_on, OLD.created_by,
                        OLD.changed_by, 'D', now());
        RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis_file_name, nachweis_status, status,
                                             created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis_file_name, NEW.nachweis_status, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis_file_name, nachweis_status, status,
                                             created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis_file_name, NEW.nachweis_status, NEW.status, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

ALTER FUNCTION vordienstzeiten_audit() OWNER TO ibosng;

