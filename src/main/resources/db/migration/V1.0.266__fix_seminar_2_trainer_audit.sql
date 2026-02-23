CREATE OR REPLACE FUNCTION seminar_2_trainer_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO seminar_2_trainer_history (seminar_2_trainer_id, seminar_id, trainer_id, dienstvertrag_nr, role,
                                               trainer_type, start_date, end_date, status, created_on,
                                               action, action_timestamp)
        VALUES (OLD.id, OLD.seminar_id, OLD.trainer_id, OLD.dienstvertrag_nr, OLD.role,
                OLD.trainer_type, OLD.start_date, OLD.end_date, OLD.status, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO seminar_2_trainer_history (seminar_2_trainer_id, seminar_id, trainer_id, dienstvertrag_nr, role,
                                               trainer_type, start_date, end_date, status, created_on,
                                               action, action_timestamp)
        VALUES (NEW.id, NEW.seminar_id, NEW.trainer_id, NEW.dienstvertrag_nr, NEW.role,
                NEW.trainer_type, NEW.start_date, NEW.end_date, NEW.status, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO seminar_2_trainer_history (seminar_2_trainer_id, seminar_id, trainer_id, dienstvertrag_nr, role,
                                               trainer_type, start_date, end_date, status, created_on,
                                               action, action_timestamp)
        VALUES (NEW.id, NEW.seminar_id, NEW.trainer_id, NEW.dienstvertrag_nr, NEW.role,
                NEW.trainer_type, NEW.start_date, NEW.end_date, NEW.status, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

ALTER FUNCTION seminar_2_trainer_audit() OWNER TO ibosng;
