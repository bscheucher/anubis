alter table unterhaltsberechtigte add column verwandtschaft INTEGER references verwandtschaft(id);
alter table unterhaltsberechtigte_history add column verwandtschaft INTEGER;

create or replace function unterhaltsberechtigte_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO unterhaltsberechtigte_history (unterhaltsberechtigte_id, vertragsdaten_id, vorname, nachname, svn,
                                                   geburtsdatum, verwandtschaft, status, created_on,
                                                   created_by,
                                                   changed_by,
                                                   action,
                                                   action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, NEW.vorname, NEW.nachname, NEW.svn, NEW.geburtsdatum, OLD. verwandtschaft, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO unterhaltsberechtigte_history (unterhaltsberechtigte_id, vertragsdaten_id, vorname, nachname, svn,
                                                   geburtsdatum, verwandtschaft, status, status,
                                                   created_on,
                                                   created_by,
                                                   changed_by,
                                                   action,
                                                   action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vorname, NEW.nachname, NEW.svn, NEW.geburtsdatum, NEW. verwandtschaft, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO unterhaltsberechtigte_history (unterhaltsberechtigte_id, vertragsdaten_id, vorname, nachname, svn,
                                                   geburtsdatum, verwandtschaft, status, status,
                                                   created_on,
                                                   created_by,
                                                   changed_by,
                                                   action,
                                                   action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vorname, NEW.nachname, NEW.svn, NEW.geburtsdatum, NEW.verwandtschaft, NEW.status, NEW.created_on, NEW.created_by, NULL,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function unterhaltsberechtigte_audit() owner to ibosng;

