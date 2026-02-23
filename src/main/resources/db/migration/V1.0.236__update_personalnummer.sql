alter table personalnummer add column onboarded_on TIMESTAMP;
alter table personalnummer_history add column onboarded_on TIMESTAMP;

create or replace function personalnummer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO personalnummer_history (personalnummer_id, firma, nummer, personalnummer, mitarbeiter_type, status, is_ibosng_onboarded, onboarded_on, created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (OLD.id, OLD.firma, OLD.nummer, OLD.personalnummer, OLD.mitarbeiter_type, OLD.status, OLD.is_ibosng_onboarded, OLD.onboarded_on, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO personalnummer_history (personalnummer_id, firma, nummer, personalnummer, mitarbeiter_type, status, is_ibosng_onboarded, onboarded_on, created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.firma, NEW.nummer, NEW.personalnummer, NEW.mitarbeiter_type, NEW.status, NEW.is_ibosng_onboarded, NEW.onboarded_on, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO personalnummer_history (personalnummer_id, firma, nummer, personalnummer, mitarbeiter_type, status, is_ibosng_onboarded, onboarded_on, created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.firma, NEW.nummer, NEW.personalnummer, NEW.mitarbeiter_type, NEW.status, NEW.is_ibosng_onboarded, NEW.onboarded_on, NEW.created_on, NEW.created_by, NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function personalnummer_audit() owner to ibosng;

