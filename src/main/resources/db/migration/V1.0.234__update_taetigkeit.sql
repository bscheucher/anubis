ALTER TABLE taetigkeit add column lhr_nr integer;
ALTER TABLE taetigkeit_history add column lhr_nr integer;

ALTER TABLE taetigkeit add column lhr_kz text;
ALTER TABLE taetigkeit_history add column lhr_kz text;


create or replace function taetigkeit_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO taetigkeit_history (taetigkeit_id, name, lhr_kz, lhr_nr, created_on, created_by, changed_by,
                                        action,
                                        action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.lhr_kz, OLD.lhr_nr, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO taetigkeit_history (taetigkeit_id, name, lhr_kz, lhr_nr, created_on, created_by, changed_by,
                                        action,
                                        action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_kz, NEW.lhr_nr, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO taetigkeit_history (taetigkeit_id, name, lhr_kz, lhr_nr, created_on, created_by, changed_by,
                                        action,
                                        action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_kz, NEW.lhr_nr, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function taetigkeit_audit() owner to ibosng;

update taetigkeit set lhr_kz = 'AMS-LehrteilnehmerIn', lhr_nr = 27 where name = 'AMS-Lehrteilnehmer';
