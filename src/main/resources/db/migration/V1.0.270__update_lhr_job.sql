ALTER TABLE lhr_job ADD job_type varchar(255);
ALTER TABLE lhr_job_history ADD job_type varchar(255);

create or replace function lhr_job_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO lhr_job_history (lhr_job_id, eintritt, personalnummer, status, job_type,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.eintritt, OLD.personalnummer, OLD.status, OLD.job_type,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO lhr_job_history (lhr_job_id, eintritt, personalnummer, status, job_type,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.eintritt, NEW.personalnummer, NEW.status, NEW.job_type,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO lhr_job_history (lhr_job_id, eintritt, personalnummer, status, job_type,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.eintritt, NEW.personalnummer, NEW.status, NEW.job_type,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function lhr_job_audit() owner to ibosng;
