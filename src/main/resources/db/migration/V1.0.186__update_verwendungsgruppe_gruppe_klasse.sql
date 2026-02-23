alter table verwendungsgruppe add column lhr_gruppe TEXT;
alter table verwendungsgruppe_history add column lhr_gruppe TEXT;
alter table verwendungsgruppe add column lhr_klasse TEXT;
alter table verwendungsgruppe_history add column lhr_klasse TEXT;

create or replace function verwendungsgruppe_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO verwendungsgruppe_history (verwendungsgruppe_id, name, lhr_klasse, lhr_gruppe, kollektivvertrag, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.lhr_klasse, OLD.lhr_gruppe, OLD.kollektivvertrag, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO verwendungsgruppe_history (verwendungsgruppe_id, name, lhr_klasse, lhr_gruppe, kollektivvertrag, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_klasse ,NEW.lhr_gruppe, NEW.kollektivvertrag, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO verwendungsgruppe_history (verwendungsgruppe_id, name, lhr_klasse, lhr_gruppe, kollektivvertrag, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_klasse, NEW.lhr_gruppe, NEW.kollektivvertrag, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function verwendungsgruppe_audit() owner to ibosng;


-- Update statement maybe here
UPDATE verwendungsgruppe SET lhr_klasse = 'VB', lhr_gruppe = '1' WHERE name = 'VB 1';
UPDATE verwendungsgruppe SET lhr_klasse = 'VB', lhr_gruppe = '2' WHERE name = 'VB 2';
UPDATE verwendungsgruppe SET lhr_klasse = 'VB', lhr_gruppe = '3' WHERE name = 'VB 3';
UPDATE verwendungsgruppe SET lhr_klasse = 'VB', lhr_gruppe = '4' WHERE name = 'VB 4';
UPDATE verwendungsgruppe SET lhr_klasse = 'VB', lhr_gruppe = '4a' WHERE name = 'VB 4a';
UPDATE verwendungsgruppe SET lhr_klasse = 'VB', lhr_gruppe = '5' WHERE name = 'VB 5';
UPDATE verwendungsgruppe SET lhr_klasse = 'VB', lhr_gruppe = '6' WHERE name = 'VB 6';
UPDATE verwendungsgruppe SET lhr_klasse = 'VB', lhr_gruppe = '7' WHERE name = 'VB 7';
UPDATE verwendungsgruppe SET lhr_klasse = 'VB', lhr_gruppe = '8' WHERE name = 'VB 8';