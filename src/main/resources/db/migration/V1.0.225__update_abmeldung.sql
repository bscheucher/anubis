alter table abmeldung add column workflow INT references w_workflows(id);

alter table abmeldung_history add column workflow INT;


create or replace function abmeldung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF
        TG_OP = 'DELETE' THEN
        INSERT INTO abmeldung_history (abmeldung_id, personalnummer, austritts_datum, austrittsgrund, bemerkung, sv_nummer, workflow, created_on, created_by, changed_on,
                                       changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.austritts_datum, OLD.austrittsgrund, OLD.bemerkung, OLD.sv_nummer, OLD.workflow, OLD.created_on, OLD.created_by,
                OLD.changed_on, OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF
        TG_OP = 'UPDATE' THEN
        INSERT INTO abmeldung_history (abmeldung_id, personalnummer, austritts_datum, austrittsgrund, bemerkung, sv_nummer, workflow, created_on, created_by,
                                       changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.austritts_datum, NEW.austrittsgrund, NEW.bemerkung, NEW.sv_nummer, NEW.workflow, NEW.created_on, NEW.created_by,
                NEW.changed_on, NEW.changed_by, 'U', NOW());
        RETURN NEW;
    ELSIF
        TG_OP = 'INSERT' THEN
        INSERT INTO abmeldung_history (abmeldung_id, personalnummer, austritts_datum, austrittsgrund, bemerkung, sv_nummer, workflow, created_on, created_by,
                                       changed_on,
                                       changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.austritts_datum, NEW.austrittsgrund, NEW.bemerkung, NEW.sv_nummer, NEW.workflow, NEW.created_on, NEW.created_by,
                NEW.changed_on, NEW.changed_by, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function abmeldung_audit() owner to ibosng;

