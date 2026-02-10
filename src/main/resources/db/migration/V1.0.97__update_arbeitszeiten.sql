ALTER TABLE arbeitszeiten ADD COLUMN montag_netto double precision;
ALTER TABLE arbeitszeiten_history ADD COLUMN montag_netto double precision;

ALTER TABLE arbeitszeiten ADD COLUMN dienstag_netto double precision;
ALTER TABLE arbeitszeiten_history ADD COLUMN dienstag_netto double precision;

ALTER TABLE arbeitszeiten ADD COLUMN mittwoch_netto double precision;
ALTER TABLE arbeitszeiten_history ADD COLUMN mittwoch_netto double precision;

ALTER TABLE arbeitszeiten ADD COLUMN donnerstag_netto double precision;
ALTER TABLE arbeitszeiten_history ADD COLUMN donnerstag_netto double precision;

ALTER TABLE arbeitszeiten ADD COLUMN freitag_netto double precision;
ALTER TABLE arbeitszeiten_history ADD COLUMN freitag_netto double precision;

ALTER TABLE arbeitszeiten ADD COLUMN samstag_netto double precision;
ALTER TABLE arbeitszeiten_history ADD COLUMN samstag_netto double precision;

ALTER TABLE arbeitszeiten ADD COLUMN sonntag_von time;
ALTER TABLE arbeitszeiten ADD COLUMN sonntag_bis time;
ALTER TABLE arbeitszeiten ADD COLUMN sonntag_netto double precision;
ALTER TABLE arbeitszeiten_history ADD COLUMN sonntag_von time;
ALTER TABLE arbeitszeiten_history ADD COLUMN sonntag_bis time;
ALTER TABLE arbeitszeiten_history ADD COLUMN sonntag_netto double precision;

CREATE OR REPLACE FUNCTION arbeitszeiten_audit() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO arbeitszeiten_history (arbeitszeiten_id, type, arbeitszeiten_info_id,
                                           montag_von, montag_bis, montag_netto,
                                           dienstag_von, dienstag_bis, dienstag_netto,
                                           mittwoch_von, mittwoch_bis, mittwoch_netto,
                                           donnerstag_von, donnerstag_bis, donnerstag_netto,
                                           freitag_von, freitag_bis, freitag_netto,
                                           samstag_von, samstag_bis, samstag_netto,
                                           sonntag_von, sonntag_bis, sonntag_netto,
                                           is_kernzeit,
                                           created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.type, OLD.arbeitszeiten_info_id,
                OLD.montag_von, OLD.montag_bis, OLD.montag_netto,
                OLD.dienstag_von, OLD.dienstag_bis, OLD.dienstag_netto,
                OLD.mittwoch_von, OLD.mittwoch_bis, OLD.mittwoch_netto,
                OLD.donnerstag_von, OLD.donnerstag_bis, OLD.donnerstag_netto,
                OLD.freitag_von, OLD.freitag_bis, OLD.freitag_netto,
                OLD.samstag_von, OLD.samstag_bis, OLD.samstag_netto,
                OLD.sonntag_von, OLD.sonntag_bis, OLD.sonntag_netto,
                OLD.is_kernzeit,
                OLD.created_on, OLD.created_by,
                OLD.changed_on, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO arbeitszeiten_history (arbeitszeiten_id, type, arbeitszeiten_info_id,
                                           montag_von, montag_bis, montag_netto,
                                           dienstag_von, dienstag_bis, dienstag_netto,
                                           mittwoch_von, mittwoch_bis, mittwoch_netto,
                                           donnerstag_von, donnerstag_bis, donnerstag_netto,
                                           freitag_von, freitag_bis, freitag_netto,
                                           samstag_von, samstag_bis, samstag_netto,
                                           sonntag_von, sonntag_bis, sonntag_netto,
                                           is_kernzeit,
                                           created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.arbeitszeiten_info_id,
                NEW.montag_von, NEW.montag_bis, NEW.montag_netto,
                NEW.dienstag_von, NEW.dienstag_bis, NEW.dienstag_netto,
                NEW.mittwoch_von, NEW.mittwoch_bis, NEW.mittwoch_netto,
                NEW.donnerstag_von, NEW.donnerstag_bis, NEW.donnerstag_netto,
                NEW.freitag_von, NEW.freitag_bis, NEW.freitag_netto,
                NEW.samstag_von, NEW.samstag_bis, NEW.samstag_netto,
                NEW.sonntag_von, NEW.sonntag_bis, NEW.sonntag_netto,
                NEW.is_kernzeit,
                NEW.created_on, NEW.created_by,
                now(), NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO arbeitszeiten_history (arbeitszeiten_id, type, arbeitszeiten_info_id,
                                           montag_von, montag_bis, montag_netto,
                                           dienstag_von, dienstag_bis, dienstag_netto,
                                           mittwoch_von, mittwoch_bis, mittwoch_netto,
                                           donnerstag_von, donnerstag_bis, donnerstag_netto,
                                           freitag_von, freitag_bis, freitag_netto,
                                           samstag_von, samstag_bis, samstag_netto,
                                           sonntag_von, sonntag_bis, sonntag_netto,
                                           is_kernzeit,
                                           created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.arbeitszeiten_info_id,
                NEW.montag_von, NEW.montag_bis, NEW.montag_netto,
                NEW.dienstag_von, NEW.dienstag_bis, NEW.dienstag_netto,
                NEW.mittwoch_von, NEW.mittwoch_bis, NEW.mittwoch_netto,
                NEW.donnerstag_von, NEW.donnerstag_bis, NEW.donnerstag_netto,
                NEW.freitag_von, NEW.freitag_bis, NEW.freitag_netto,
                NEW.samstag_von, NEW.samstag_bis, NEW.samstag_netto,
                NEW.sonntag_von, NEW.sonntag_bis, NEW.sonntag_netto,
                NEW.is_kernzeit,
                NEW.created_on, NEW.created_by,
                NULL, NULL, -- assuming no change info for new records
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;

$$;

ALTER FUNCTION vordienstzeiten_audit() OWNER TO ibosng;

