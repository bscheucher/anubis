
create or replace function zeiterfassung_transfer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeiterfassung_transfer_history (zeiterfassung_transfer_id, datum_von, datum_bis, datum_sent, status,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (OLD.id, OLD.datum_von, OLD.datum_bis, OLD.datum_sent, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeiterfassung_transfer_history (zeiterfassung_transfer_id, datum_von, datum_bis, datum_sent, status,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.datum_von, NEW.datum_bis, NEW.datum_sent, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeiterfassung_transfer_history (zeiterfassung_transfer_id, datum_von, datum_bis, datum_sent, status,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.datum_von, NEW.datum_bis, NEW.datum_sent, NEW.status,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;
