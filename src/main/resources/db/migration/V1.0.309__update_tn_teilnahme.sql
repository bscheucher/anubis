alter table tn_teilnahme_status rename column teilnahme to tn_teilnahme;
alter table tn_teilnahme_status_history rename column teilnahme to tn_teilnahme;


CREATE OR REPLACE FUNCTION tn_teilnahme_status_audit() RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO tn_teilnahme_status_history (
            tn_teilnahme_status_id, teilnehmer, tn_teilnahme, tn_reason, info, status,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
                   OLD.id, OLD.teilnehmer, OLD.tn_teilnahme, OLD.tn_reason, OLD.info, OLD.status,
                   OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', clock_timestamp());
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO tn_teilnahme_status_history (
            tn_teilnahme_status_id, teilnehmer, tn_teilnahme, tn_reason, info, status,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
                   NEW.id, NEW.teilnehmer, NEW.tn_teilnahme, NEW.tn_reason, NEW.info, NEW.status,
                   NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', clock_timestamp());
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO tn_teilnahme_status_history (
            tn_teilnahme_status_id, teilnehmer, tn_teilnahme, tn_reason, info, status,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
                   NEW.id, NEW.teilnehmer, NEW.tn_teilnahme, NEW.tn_reason, NEW.info, NEW.status,
                   NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', clock_timestamp());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;