ALTER TABLE seminar_pruefung_ergebnis
    ADD COLUMN teilnehmer INTEGER NOT NULL REFERENCES teilnehmer (id);

ALTER TABLE seminar_pruefung_ergebnis_history
    ADD COLUMN teilnehmer INTEGER NOT NULL;


CREATE
    OR REPLACE FUNCTION seminar_pruefung_ergebnis_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
        TG_OP = 'DELETE' THEN
        INSERT INTO seminar_pruefung_ergebnis_history (seminar_pruefung_ergebnis_id, seminar_2_pruefung, antritt,
                                                       ergebnis, ergebnis_in_prozent, pruefung_am, notiz, created_on,
                                                       created_by,
                                                       changed_on, changed_by,
                                                       action, action_timestamp, teilnehmer)
        VALUES (OLD.id,
                OLD.seminar_2_pruefung,
                OLD.antritt,
                OLD.ergebnis,
                OLD.ergebnis_in_prozent,
                OLD.pruefung_am,
                OLD.notiz,
                OLD.created_on,
                OLD.created_by,
                OLD.changed_on,
                OLD.changed_by,
                'D', NOW(), OLD.teilnehmer);
        RETURN OLD;
    ELSIF
        TG_OP = 'UPDATE' THEN
        INSERT INTO seminar_pruefung_ergebnis_history (seminar_pruefung_ergebnis_id, seminar_2_pruefung, antritt,
                                                       ergebnis, ergebnis_in_prozent, pruefung_am, notiz, created_on,
                                                       created_by,
                                                       changed_on, changed_by,
                                                       action, action_timestamp, teilnehmer)
        VALUES (NEW.id,
                NEW.seminar_2_pruefung,
                NEW.antritt,
                NEW.ergebnis,
                NEW.ergebnis_in_prozent,
                NEW.pruefung_am,
                NEW.notiz,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'U', NOW(), NEW.teilnehmer);
        RETURN NEW;
    ELSIF
        TG_OP = 'INSERT' THEN
        INSERT INTO seminar_pruefung_ergebnis_history (seminar_pruefung_ergebnis_id, seminar_2_pruefung, antritt,
                                                       ergebnis, ergebnis_in_prozent, pruefung_am, notiz, created_on,
                                                       created_by,
                                                       changed_on, changed_by,
                                                       action, action_timestamp, teilnehmer)
        VALUES (NEW.id,
                NEW.seminar_2_pruefung,
                NEW.antritt,
                NEW.ergebnis,
                NEW.ergebnis_in_prozent,
                NEW.pruefung_am,
                NEW.notiz,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'I', NOW(), NEW.teilnehmer);

        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

ALTER FUNCTION seminar_pruefung_ergebnis_audit() OWNER TO ibosng;

ALTER TABLE seminar_notiz_kategorie RENAME TO teilnehmer_notiz_kategorie;
ALTER TABLE seminar_notiz_kategorie_history RENAME TO teilnehmer_notiz_kategorie_history;
ALTER TABLE teilnehmer_notiz_kategorie_history
    RENAME COLUMN seminar_notiz_kategorie_id TO teilnehmer_notiz_kategorie_id;

DROP TRIGGER IF EXISTS seminar_notiz_kategorie_insert ON teilnehmer_notiz_kategorie;
DROP TRIGGER IF EXISTS seminar_notiz_kategorie_update ON teilnehmer_notiz_kategorie;
DROP TRIGGER IF EXISTS seminar_notiz_kategorie_delete ON teilnehmer_notiz_kategorie;
DROP FUNCTION IF EXISTS seminar_notiz_kategorie_audit();



CREATE
    OR REPLACE FUNCTION teilnehmer_notiz_kategorie_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
        TG_OP = 'DELETE' THEN
        INSERT INTO teilnehmer_notiz_kategorie_history (teilnehmer_notiz_kategorie_id, name, created_on, created_by,
                                                        changed_on, changed_by, status,
                                                        action, action_timestamp)
        VALUES (OLD.id,
                OLD.name,
                OLD.created_on,
                OLD.created_by,
                OLD.changed_on,
                OLD.changed_by,
                OLD.status,
                'D', NOW());
        RETURN OLD;
    ELSIF
        TG_OP = 'UPDATE' THEN
        INSERT INTO teilnehmer_notiz_kategorie_history (teilnehmer_notiz_kategorie_id, name, created_on, created_by,
                                                        changed_on, changed_by, status,
                                                        action, action_timestamp)
        VALUES (NEW.id,
                NEW.name,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by,
                NEW.status, 'U', NOW());
        RETURN NEW;
    ELSIF
        TG_OP = 'INSERT' THEN
        INSERT INTO teilnehmer_notiz_kategorie_history (teilnehmer_notiz_kategorie_id, name, created_on, created_by,
                                                        changed_on, changed_by, status,
                                                        action, action_timestamp)
        VALUES (NEW.id,
                NEW.name,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by,
                NEW.status, 'I', NOW());

        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

ALTER FUNCTION teilnehmer_notiz_kategorie_audit() OWNER TO ibosng;

CREATE TRIGGER teilnehmer_notiz_kategorie_insert
    AFTER INSERT
    ON teilnehmer_notiz_kategorie
    FOR EACH ROW
EXECUTE PROCEDURE teilnehmer_notiz_kategorie_audit();

CREATE TRIGGER teilnehmer_notiz_kategorie_update
    AFTER UPDATE
    ON teilnehmer_notiz_kategorie
    FOR EACH ROW
EXECUTE PROCEDURE teilnehmer_notiz_kategorie_audit();

CREATE TRIGGER teilnehmer_notiz_kategorie_delete
    AFTER DELETE
    ON teilnehmer_notiz_kategorie
    FOR EACH ROW
EXECUTE PROCEDURE teilnehmer_notiz_kategorie_audit();


ALTER TABLE seminar_notiz RENAME TO teilnehmer_notiz;
ALTER TABLE teilnehmer_notiz RENAME COLUMN seminar TO teilnehmer;
ALTER TABLE teilnehmer_notiz
    DROP CONSTRAINT seminar_notiz_seminar_fkey,
    ADD CONSTRAINT teilnehmer_notiz_teilnehmer_fkey FOREIGN KEY (teilnehmer) REFERENCES teilnehmer (id);
ALTER TABLE teilnehmer_notiz
    DROP CONSTRAINT seminar_notiz_kategorie_fkey,
    ADD CONSTRAINT teilnehmer_notiz_kategorie_fkey FOREIGN KEY (kategorie) REFERENCES teilnehmer_notiz_kategorie (id);

ALTER TABLE seminar_notiz_history RENAME TO teilnehmer_notiz_history;
ALTER TABLE teilnehmer_notiz_history RENAME COLUMN seminar TO teilnehmer;
ALTER TABLE teilnehmer_notiz_history RENAME COLUMN seminar_notiz_id TO teilnehmer_notiz_id;


DROP TRIGGER IF EXISTS seminar_notiz_insert ON teilnehmer_notiz;
DROP TRIGGER IF EXISTS seminar_notiz_update ON teilnehmer_notiz;
DROP TRIGGER IF EXISTS seminar_notiz_delete ON teilnehmer_notiz;
DROP FUNCTION IF EXISTS seminar_notiz_audit();


CREATE
    OR REPLACE FUNCTION teilnehmer_notiz_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
        TG_OP = 'DELETE' THEN
        INSERT INTO teilnehmer_notiz_history (teilnehmer_notiz_id, teilnehmer, notiz, kategorie, created_on, created_by,
                                              changed_on,
                                              changed_by, action, action_timestamp)
        VALUES (OLD.id,
                OLD.teilnehmer,
                OLD.notiz,
                OLD.kategorie,
                OLD.created_on,
                OLD.created_by,
                OLD.changed_on,
                OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF
        TG_OP = 'UPDATE' THEN
        INSERT INTO teilnehmer_notiz_history (teilnehmer_notiz_id, teilnehmer, notiz, kategorie, created_on, created_by,
                                              changed_on,
                                              changed_by, action, action_timestamp)
        VALUES (NEW.id,
                NEW.teilnehmer,
                NEW.notiz,
                NEW.kategorie,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'U', NOW());
        RETURN NEW;
    ELSIF
        TG_OP = 'INSERT' THEN
        INSERT INTO teilnehmer_notiz_history (teilnehmer_notiz_id, teilnehmer, notiz, kategorie, created_on, created_by,
                                              changed_on,
                                              changed_by, action, action_timestamp)
        VALUES (NEW.id,
                NEW.teilnehmer,
                NEW.notiz,
                NEW.kategorie,
                NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'I', NOW());

        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

ALTER FUNCTION teilnehmer_notiz_audit() OWNER TO ibosng;

CREATE TRIGGER teilnehmer_notiz_insert
    AFTER INSERT
    ON teilnehmer_notiz
    FOR EACH ROW
EXECUTE PROCEDURE teilnehmer_notiz_audit();

CREATE TRIGGER teilnehmer_notiz_update
    AFTER UPDATE
    ON teilnehmer_notiz
    FOR EACH ROW
EXECUTE PROCEDURE teilnehmer_notiz_audit();

CREATE TRIGGER teilnehmer_notiz_delete
    AFTER DELETE
    ON teilnehmer_notiz
    FOR EACH ROW
EXECUTE PROCEDURE teilnehmer_notiz_audit();