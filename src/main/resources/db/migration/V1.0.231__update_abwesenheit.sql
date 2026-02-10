ALTER TABLE abwesenheit ADD COLUMN comment_fuehrungskraft TEXT;
ALTER TABLE abwesenheit_history ADD COLUMN comment_fuehrungskraft TEXT;

create
or replace function abwesenheit_audit() returns trigger
    language plpgsql
    as
    $$
BEGIN
    IF
(TG_OP = 'DELETE') THEN
    INSERT INTO abwesenheit_history (abwesenheit_id,
        grund, beschreibung,kommentar,art,id_lhr,von,bis,typ,tage,saldo,personalnummer,status,comment_fuehrungskraft,
        created_on, created_by, changed_on, changed_by,
        action,
        action_timestamp)
    VALUES (OLD.id, OLD.grund, OLD.beschreibung, OLD.kommentar,OLD.art,OLD.id_lhr,OLD.von,OLD.bis,OLD.typ,OLD.tage,OLD.saldo,OLD.personalnummer,OLD.status,OLD.comment_fuehrungskraft,
        OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D',
        now());
RETURN OLD;
ELSIF
(TG_OP = 'UPDATE') THEN
    INSERT INTO abwesenheit_history (abwesenheit_id,grund, beschreibung,kommentar,art,id_lhr,von,bis,typ,tage,saldo,personalnummer,status,comment_fuehrungskraft,
       created_on, created_by, changed_on, changed_by,
       action,
       action_timestamp)
    VALUES (NEW.id, NEW.grund, NEW.beschreibung, NEW.kommentar,NEW.art,NEW.id_lhr,NEW.von,NEW.bis,NEW.typ,NEW.tage,NEW.saldo,NEW.personalnummer,NEW.status,NEW.comment_fuehrungskraft,
        NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U',
        now());
RETURN NEW;
ELSIF
(TG_OP = 'INSERT') THEN
    INSERT INTO abwesenheit_history (abwesenheit_id,grund, beschreibung,kommentar,art,id_lhr,von,bis,typ,tage,saldo,personalnummer,status,comment_fuehrungskraft,
                    created_on, created_by, changed_on, changed_by,
       action,
       action_timestamp)
    VALUES (NEW.id,  NEW.grund, NEW.beschreibung, NEW.kommentar,NEW.art,NEW.id_lhr,NEW.von,NEW.bis,NEW.typ,NEW.tage,NEW.saldo,NEW.personalnummer,NEW.status,NEW.comment_fuehrungskraft,
        NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

ALTER TABLE zeitausgleich ADD COLUMN comment_fuehrungskraft TEXT;
ALTER TABLE zeitausgleich_history ADD comment_fuehrungskraft TEXT;

create or replace function zeitausgleich_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO zeitausgleich_history (
            zeitausgleich_id, personalnummer, datum, time_von, time_bis, comment, comment_fuehrungskraft,
            created_on, created_by, changed_on, changed_by, status, action, action_timestamp
        )
        VALUES (
                   OLD.id, OLD.personalnummer, OLD.datum, OLD.time_von, OLD.time_bis, OLD.comment, OLD.comment_fuehrungskraft,
                   OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, OLD.status, 'D', NOW()
               );
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO zeitausgleich_history (
            zeitausgleich_id, personalnummer, datum, time_von, time_bis, comment, comment_fuehrungskraft,
            created_on, created_by, changed_on, changed_by, status, action, action_timestamp
        )
        VALUES (
                   NEW.id, NEW.personalnummer, NEW.datum, NEW.time_von, NEW.time_bis, NEW.comment, NEW.comment_fuehrungskraft,
                   NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.status, 'U', NOW()
               );
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO zeitausgleich_history (
            zeitausgleich_id, personalnummer, datum, time_von, time_bis, comment, comment_fuehrungskraft,
            created_on, created_by, changed_on, changed_by, status, action, action_timestamp
        )
        VALUES (
                   NEW.id, NEW.personalnummer, NEW.datum, NEW.time_von, NEW.time_bis, NEW.comment, NEW.comment_fuehrungskraft,
                   NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.status, 'I', NOW()
               );
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function zeitausgleich_audit() owner to ibosng;

