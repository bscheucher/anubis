ALTER TABLE abwesenheit ADD COLUMN status smallint not null default 1;
ALTER TABLE abwesenheit_history ADD COLUMN status smallint;

create
or replace function abwesenheit_audit() returns trigger
    language plpgsql
    as
    $$
BEGIN
    IF
(TG_OP = 'DELETE') THEN
    INSERT INTO abwesenheit_history (abwesenheit_id,
        grund, beschreibung,kommentar,art,id_lhr,von,bis,typ,tage,saldo,personalnummer,status,
        created_on, created_by, changed_on, changed_by,
        action,
        action_timestamp)
    VALUES (OLD.id, OLD.grund, OLD.beschreibung, OLD.kommentar,OLD.art,OLD.id_lhr,OLD.von,OLD.bis,OLD.typ,OLD.tage,OLD.saldo,OLD.personalnummer,OLD.status,
        OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D',
        now());
RETURN OLD;
ELSIF
(TG_OP = 'UPDATE') THEN
    INSERT INTO abwesenheit_history (abwesenheit_id,grund, beschreibung,kommentar,art,id_lhr,von,bis,typ,tage,saldo,personalnummer,status,
       created_on, created_by, changed_on, changed_by,
       action,
       action_timestamp)
    VALUES (NEW.id, NEW.grund, NEW.beschreibung, NEW.kommentar,NEW.art,NEW.id_lhr,NEW.von,NEW.bis,NEW.typ,NEW.tage,NEW.saldo,NEW.personalnummer,NEW.status,
        NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U',
        now());
RETURN NEW;
ELSIF
(TG_OP = 'INSERT') THEN
    INSERT INTO abwesenheit_history (abwesenheit_id,grund, beschreibung,kommentar,art,id_lhr,von,bis,typ,tage,saldo,personalnummer,status,
                    created_on, created_by, changed_on, changed_by,
       action,
       action_timestamp)
    VALUES (NEW.id,  NEW.grund, NEW.beschreibung, NEW.kommentar,NEW.art,NEW.id_lhr,NEW.von,NEW.bis,NEW.typ,NEW.tage,NEW.saldo,NEW.personalnummer,NEW.status,
        NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;