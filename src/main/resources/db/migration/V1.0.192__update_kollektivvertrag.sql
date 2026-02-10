ALTER TABLE kollektivvertrag add column lhr_nr integer;
ALTER TABLE kollektivvertrag_history add column lhr_nr integer;

ALTER TABLE kollektivvertrag add column lhr_kz text;
ALTER TABLE kollektivvertrag_history add column lhr_kz text;

create or replace function kollektivvertrag_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO kollektivvertrag_history (kollektivvertrag_id, name, lhr_nr, lhr_kz, created_on, created_by, changed_by,
                                              action,
                                              action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.lhr_nr, OLD.lhr_kz, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO kollektivvertrag_history (kollektivvertrag_id, name, lhr_nr, lhr_kz, created_on, created_by, changed_by,
                                              action,
                                              action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_nr, NEW.lhr_kz, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO kollektivvertrag_history (kollektivvertrag_id, name, lhr_nr, lhr_kz, created_on, created_by, changed_by,
                                              action,
                                              action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_nr, NEW.lhr_kz, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function kollektivvertrag_audit() owner to ibosng;

update kollektivvertrag set lhr_nr = 1, lhr_kz = 'BABE KV' where name = 'BABE';
update kollektivvertrag set lhr_nr = 3, lhr_kz = 'AMS TN' where name = 'AMS-Lehrteilnehmer';