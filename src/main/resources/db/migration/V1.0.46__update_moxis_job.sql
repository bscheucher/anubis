ALTER TABLE moxis_job ADD COLUMN instance_id INTEGER;
ALTER TABLE moxis_job ADD COLUMN status SMALLINT NOT NULL DEFAULT 0;
ALTER TABLE moxis_job_history ADD COLUMN instance_id INTEGER;
ALTER TABLE moxis_job_history ADD COLUMN status SMALLINT;

create or replace function moxis_job_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO moxis_job_history (moxis_job_id, instance_id, constituent, position_type, description, category, expiration_date, reference_id, status, created_on, created_by,
                                       changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.instance_id, OLD.constituent, OLD.position_type, OLD.description, OLD.category, OLD.expiration_date, OLD.reference_id, OLD.status, OLD.created_on, OLD.created_by,
                CURRENT_USER, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO moxis_job_history (moxis_job_id, instance_id, constituent, position_type, description, category, expiration_date, reference_id, status, created_on, created_by,
                                       changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.instance_id, NEW.constituent, NEW.position_type, NEW.description, NEW.category, NEW.expiration_date, NEW.reference_id, NEW.status, NEW.created_on, NEW.created_by,
                CURRENT_USER, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO moxis_job_history (moxis_job_id, instance_id, constituent, position_type, description, category, expiration_date, reference_id, status, created_on, created_by,
                                       changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.instance_id, NEW.constituent, NEW.position_type, NEW.description, NEW.category, NEW.expiration_date, NEW.reference_id, NEW.status, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;