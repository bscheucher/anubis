create table if not exists zeiterfassung
(
    id                  integer    generated always as identity primary key,
    teilnehmer          integer references teilnehmer(id),
    zeiterfassung_reason       integer references zeiterfassung_reason(id),
    seminar             integer references seminar(id),
    datum               date,
    datum_bis           date,
    created_on          timestamp default CURRENT_TIMESTAMP not null,
    changed_on          timestamp default CURRENT_TIMESTAMP ,
    created_by          text,
    changed_by          text
);

create table if not exists zeiterfassung_history
(
    id                      integer generated always as identity primary key,
    zeiterfassung_id        integer,
    teilnehmer              integer,
    zeiterfassung_reason    integer,
    datum                   date,
    datum_bis               date,
    created_on              timestamp default CURRENT_TIMESTAMP,
    changed_on              timestamp default CURRENT_TIMESTAMP,
    created_by              text,
    changed_by              text,
    action                  char,
    action_timestamp        timestamp not null
);


create or replace function zeiterfassung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeiterfassung_history (
            zeiterfassung_id, teilnehmer, zeiterfassung_reason, datum, datum_bis,
            created_on, created_by, changed_by, action, action_timestamp)
        VALUES (
            OLD.id, OLD.teilnehmer, OLD.zeiterfassung_reason, OLD.datum, OLD.datum_bis,
            OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeiterfassung_history (
            zeiterfassung_id, teilnehmer, zeiterfassung_reason, datum, datum_bis,
            created_on, created_by, changed_by, action, action_timestamp)
        VALUES (
            NEW.id, NEW.teilnehmer, NEW.zeiterfassung_reason, NEW.datum, NEW.datum_bis,
            NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeiterfassung_history (
            zeiterfassung_id, teilnehmer, zeiterfassung_reason, datum, datum_bis,
            created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer, NEW.zeiterfassung_reason, NEW.datum, NEW.datum_bis,
                NEW.created_on, NEW.created_by, NULL, 'I', now(), NEW.anrede);
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

