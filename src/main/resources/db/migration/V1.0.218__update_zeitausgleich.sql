ALTER TABLE zeitausgleich ADD COLUMN comment TEXT;
ALTER TABLE zeitausgleich_history ADD comment TEXT;

create or replace function zeitausgleich_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO zeitausgleich_history (
            zeitausgleich_id, personalnummer, datum, time_von, time_bis, comment,
            created_on, created_by, changed_on, changed_by, status, action, action_timestamp
        )
        VALUES (
                   OLD.id, OLD.personalnummer, OLD.datum, OLD.time_von, OLD.time_bis, OLD.comment,
                   OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, OLD.status, 'D', NOW()
               );
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO zeitausgleich_history (
            zeitausgleich_id, personalnummer, datum, time_von, time_bis, comment,
            created_on, created_by, changed_on, changed_by, status, action, action_timestamp
        )
        VALUES (
                   NEW.id, NEW.personalnummer, NEW.datum, NEW.time_von, NEW.time_bis, NEW.comment,
                   NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.status, 'U', NOW()
               );
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO zeitausgleich_history (
            zeitausgleich_id, personalnummer, datum, time_von, time_bis, comment,
            created_on, created_by, changed_on, changed_by, status, action, action_timestamp
        )
        VALUES (
                   NEW.id, NEW.personalnummer, NEW.datum, NEW.time_von, NEW.time_bis, NEW.comment,
                   NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.status, 'I', NOW()
               );
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function zeitausgleich_audit() owner to ibosng;

