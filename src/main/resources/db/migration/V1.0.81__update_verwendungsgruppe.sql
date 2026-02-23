alter table verwendungsgruppe add column kollektivvertrag integer references kollektivvertrag(id);
alter table verwendungsgruppe_history add column kollektivvertrag integer;

UPDATE verwendungsgruppe
SET kollektivvertrag = (SELECT id FROM kollektivvertrag WHERE name = 'BABE');

ALTER TABLE verwendungsgruppe
    ALTER COLUMN kollektivvertrag SET NOT NULL;

create or replace function verwendungsgruppe_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO verwendungsgruppe_history (verwendungsgruppe_id, name, kollektivvertrag, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.kollektivvertrag, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO verwendungsgruppe_history (verwendungsgruppe_id, name, kollektivvertrag, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.kollektivvertrag, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO verwendungsgruppe_history (verwendungsgruppe_id, name, kollektivvertrag, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.kollektivvertrag, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function verwendungsgruppe_audit() owner to ibosng;
