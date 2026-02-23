ALTER TABLE leistungserfassung ADD COLUMN moxis_status varchar;
ALTER TABLE leistungserfassung_history ADD COLUMN moxis_status varchar;

CREATE OR REPLACE FUNCTION leistungserfassung_audit() RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO leistungserfassung_history (leistungserfassung_id, personalnummer, leistungsdatum, leistungstyp,
                                                created_on, created_by, changed_on, changed_by, is_locked, is_synced,
                                                moxis_status, action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.leistungsdatum, OLD.leistungstyp, OLD.created_on, OLD.created_by,
            OLD.changed_on, OLD.changed_by, OLD.is_locked, OLD.is_synced, OLD.moxis_status, 'D', NOW());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO leistungserfassung_history (leistungserfassung_id, personalnummer, leistungsdatum, leistungstyp,
                                                created_on, created_by, changed_on, changed_by, is_locked, is_synced,
                                                moxis_status, action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.leistungsdatum, NEW.leistungstyp, NEW.created_on, NEW.created_by,
            NEW.changed_on, NEW.changed_by, NEW.is_locked, NEW.is_synced, NEW.moxis_status, 'U', NOW());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO leistungserfassung_history (leistungserfassung_id, personalnummer, leistungsdatum, leistungstyp,
                                                created_on, created_by, changed_on, changed_by, is_locked, is_synced,
                                                moxis_status, action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.leistungsdatum, NEW.leistungstyp, NEW.created_on, NEW.created_by,
            NEW.changed_on, NEW.changed_by, NEW.is_locked, NEW.is_synced, NEW.moxis_status, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;
