ALTER TABLE adresse ADD COLUMN status SMALLINT DEFAULT 0 NOT NULL;
ALTER TABLE adresse_history ADD COLUMN status SMALLINT;

create or replace function adresse_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO adresse_history (adresse_id, ort, plz, strasse, status, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.ort, OLD.plz, OLD.strasse, OLD.status, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO adresse_history (adresse_id, ort, plz, strasse, status, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.ort, NEW.plz, NEW.strasse, NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO adresse_history (adresse_id, ort, plz, strasse, status, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.ort, NEW.plz, NEW.strasse, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;