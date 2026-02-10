alter table arbeitszeiten add column is_kernzeit boolean default false;
alter table arbeitszeiten_history add column is_kernzeit boolean;


CREATE OR REPLACE FUNCTION arbeitszeiten_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO arbeitszeiten_history (arbeitszeiten_id, type, arbeitszeiten_info_id,
                                           montag_von, montag_bis,
                                           dienstag_von, dienstag_bis,
                                           mittwoch_von, mittwoch_bis,
                                           donnerstag_von, donnerstag_bis,
                                           freitag_von, freitag_bis,
                                           samstag_von, samstag_bis,
                                           is_kernzeit,
                                           created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.type, OLD.arbeitszeiten_info_id,
                OLD.montag_von, OLD.montag_bis,
                OLD.dienstag_von, OLD.dienstag_bis,
                OLD.mittwoch_von, OLD.mittwoch_bis,
                OLD.donnerstag_von, OLD.donnerstag_bis,
                OLD.freitag_von, OLD.freitag_bis,
                OLD.samstag_von, OLD.samstag_bis,
                OLD.is_kernzeit,
                OLD.created_on, OLD.created_by,
                OLD.changed_on, OLD.changed_by,
                'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO arbeitszeiten_history (arbeitszeiten_id, type, arbeitszeiten_info_id,
                                           montag_von, montag_bis,
                                           dienstag_von, dienstag_bis,
                                           mittwoch_von, mittwoch_bis,
                                           donnerstag_von, donnerstag_bis,
                                           freitag_von, freitag_bis,
                                           samstag_von, samstag_bis,
                                           is_kernzeit,
                                           created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.arbeitszeiten_info_id,
                NEW.montag_von, NEW.montag_bis,
                NEW.dienstag_von, NEW.dienstag_bis,
                NEW.mittwoch_von, NEW.mittwoch_bis,
                NEW.donnerstag_von, NEW.donnerstag_bis,
                NEW.freitag_von, NEW.freitag_bis,
                NEW.samstag_von, NEW.samstag_bis,
                NEW.is_kernzeit,
                NEW.created_on, NEW.created_by,
                now(), NEW.changed_by,
                'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO arbeitszeiten_history (arbeitszeiten_id, type, arbeitszeiten_info_id,
                                           montag_von, montag_bis,
                                           dienstag_von, dienstag_bis,
                                           mittwoch_von, mittwoch_bis,
                                           donnerstag_von, donnerstag_bis,
                                           freitag_von, freitag_bis,
                                           samstag_von, samstag_bis,
                                           is_kernzeit,
                                           created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.arbeitszeiten_info_id,
                NEW.montag_von, NEW.montag_bis,
                NEW.dienstag_von, NEW.dienstag_bis,
                NEW.mittwoch_von, NEW.mittwoch_bis,
                NEW.donnerstag_von, NEW.donnerstag_bis,
                NEW.freitag_von, NEW.freitag_bis,
                NEW.samstag_von, NEW.samstag_bis,
                NEW.is_kernzeit,
                NEW.created_on, NEW.created_by,
                NULL, NULL, -- assuming no change info for new records
                'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

ALTER FUNCTION arbeitszeiten_audit() OWNER TO ibosng;


