ALTER TABLE tn_teilnahme DROP COLUMN  anwesend;
ALTER TABLE tn_teilnahme DROP COLUMN  nicht_entschuldigt;
ALTER TABLE tn_teilnahme DROP COLUMN  sonstige;

ALTER TABLE tn_teilnahme_history DROP COLUMN  anwesend;
ALTER TABLE tn_teilnahme_history DROP COLUMN  nicht_entschuldigt;
ALTER TABLE tn_teilnahme_history DROP COLUMN  sonstige;

CREATE OR REPLACE FUNCTION tn_teilnahme_audit() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO tn_teilnahme_history (
            tn_teilnahme_id, datum, status, seminar,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
            OLD.id, OLD.datum, OLD.status, OLD.seminar,
            OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', clock_timestamp());
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO tn_teilnahme_history (
            tn_teilnahme_id, datum, status, seminar,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
            NEW.id, NEW.datum, NEW.status, NEW.seminar,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', clock_timestamp());
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO tn_teilnahme_history (
            tn_teilnahme_id, datum, status, seminar,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
            NEW.id, NEW.datum, NEW.status, NEW.seminar, 
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', clock_timestamp());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;