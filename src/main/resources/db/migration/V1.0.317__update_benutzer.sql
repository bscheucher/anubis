alter table benutzer add column upn text;
alter table ibosng.public.benutzer_history add column upn text;


create or replace function benutzer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO benutzer_history (benutzer_id, azure_id, first_name, last_name, email, upn, personalnummer, status, moxis_classifier, created_on,
                                      created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.azure_id, OLD.first_name, OLD.last_name, OLD.email, OLD.upn, OLD.personalnummer, OLD.status, OLD.moxis_classifier, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO benutzer_history (benutzer_id, azure_id, first_name, last_name, email, upn, personalnummer, status, moxis_classifier, created_on,
                                      created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.azure_id, NEW.first_name, NEW.last_name, NEW.email, NEW.upn, NEW.personalnummer, NEW.status, NEW.moxis_classifier, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO benutzer_history (benutzer_id, azure_id, first_name, last_name, email, upn, personalnummer, status, moxis_classifier, created_on,
                                      created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.azure_id, NEW.first_name, NEW.last_name, NEW.email, NEW.upn, NEW.personalnummer, NEW.status, NEW.moxis_classifier, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function benutzer_audit() owner to ibosng;

grant execute on function benutzer_audit() to ibosngserviceuser;

