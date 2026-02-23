create or replace function vordienstzeiten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis, status, created_on,
                                             created_by,
                                             changed_by,
                                             action,
                                             action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.vertragsart, OLD.firma, OLD.von, OLD.bis, OLD.wochenstunden,
                OLD.anrechenbar, OLD.nachweis, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis, status,
                                             created_on,
                                             created_by,
                                             changed_by,
                                             action,
                                             action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis, status,
                                             created_on,
                                             created_by,
                                             changed_by,
                                             action,
                                             action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis, NEW.status, NEW.created_on, NEW.created_by, NULL,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;