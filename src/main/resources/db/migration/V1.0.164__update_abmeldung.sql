ALTER TABLE abmeldung
    ADD COLUMN sv_nummer BIGINT;

ALTER TABLE abmeldung_history
    ADD COLUMN sv_nummer BIGINT;


CREATE
OR REPLACE FUNCTION abmeldung_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
TG_OP = 'DELETE' THEN
        INSERT INTO abmeldung_history (abmeldung_id, personalnummer, austritts_datum, austrittsgrund, bemerkung, sv_nummer, created_on, created_by, changed_on,
                                       changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.austritts_datum, OLD.austrittsgrund, OLD.bemerkung, OLD.sv_nummer, OLD.created_on, OLD.created_by,
                OLD.changed_on, OLD.changed_by, 'D', NOW());
RETURN OLD;
ELSIF
TG_OP = 'UPDATE' THEN
        INSERT INTO abmeldung_history (abmeldung_id, personalnummer, austritts_datum, austrittsgrund, bemerkung, sv_nummer, created_on, created_by,
                                       changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.austritts_datum, NEW.austrittsgrund, NEW.bemerkung, NEW.sv_nummer, NEW.created_on, NEW.created_by,
                NEW.changed_on, NEW.changed_by, 'U', NOW());
RETURN NEW;
ELSIF
TG_OP = 'INSERT' THEN
        INSERT INTO abmeldung_history (abmeldung_id, personalnummer, austritts_datum, austrittsgrund, bemerkung, sv_nummer, created_on, created_by,
                                       changed_on,
                                       changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.austritts_datum, NEW.austrittsgrund, NEW.bemerkung, NEW.sv_nummer, NEW.created_on, NEW.created_by,
                NEW.changed_on, NEW.changed_by, 'I', NOW());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function abmeldung_audit() owner to ibosng;

CREATE OR REPLACE TRIGGER abmeldung_insert
    AFTER INSERT
    ON abmeldung
    FOR EACH ROW
    EXECUTE FUNCTION abmeldung_audit();

CREATE OR REPLACE TRIGGER abmeldung_update
    AFTER UPDATE
    ON abmeldung
    FOR EACH ROW
    EXECUTE FUNCTION abmeldung_audit();

CREATE OR REPLACE TRIGGER abmeldung_delete
    AFTER DELETE
    ON abmeldung
    FOR EACH ROW
    EXECUTE FUNCTION abmeldung_audit();