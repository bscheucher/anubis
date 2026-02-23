ALTER TABLE zeitausgleich ADD COLUMN status smallint;

ALTER TABLE zeitausgleich_history ADD COLUMN status smallint;


CREATE OR REPLACE FUNCTION zeitausgleich_audit() RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO zeitausgleich_history (
            zeitausgleich_id, personalnummer, datum, time_von, time_bis,
            created_on, created_by, changed_on, changed_by, status, action, action_timestamp
        )
        VALUES (
            OLD.id, OLD.personalnummer, OLD.datum, OLD.time_von, OLD.time_bis,
            OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, OLD.status, 'D', NOW()
        );
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO zeitausgleich_history (
            zeitausgleich_id, personalnummer, datum, time_von, time_bis,
            created_on, created_by, changed_on, changed_by, status, action, action_timestamp
        )
        VALUES (
            NEW.id, NEW.personalnummer, NEW.datum, NEW.time_von, NEW.time_bis,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.status, 'U', NOW()
        );
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO zeitausgleich_history (
            zeitausgleich_id, personalnummer, datum, time_von, time_bis,
            created_on, created_by, changed_on, changed_by, status, action, action_timestamp
        )
        VALUES (
            NEW.id, NEW.personalnummer, NEW.datum, NEW.time_von, NEW.time_bis,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.status, 'I', NOW()
        );
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;
