CREATE
OR REPLACE FUNCTION verwandtschaft_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
TG_OP = 'DELETE' THEN
        INSERT INTO verwandtschaft_history (verwandtschaft_id, name, lhr_kz, status, created_on, created_by , changed_on, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.lhr_kz, OLD.status, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', NOW());
RETURN OLD;
ELSIF
TG_OP = 'UPDATE' THEN
        INSERT INTO verwandtschaft_history (verwandtschaft_id, name, lhr_kz, status, created_on, created_by , changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_kz, NEW.status, NEW.created_on, NEW.created_by , NEW.changed_on, NEW.changed_by, 'U', NOW());
RETURN NEW;
ELSIF
TG_OP = 'INSERT' THEN
        INSERT INTO verwandtschaft_history (verwandtschaft_id, name, lhr_kz, status, created_on, created_by , changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_kz, NEW.status, NEW.created_on, NEW.created_by , NEW.changed_on, NEW.changed_by, 'I', NOW());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function verwandtschaft_audit() owner to ibosng;
