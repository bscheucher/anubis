ALTER TABLE zeiterfassung
    add column status smallint not null default 0;
ALTER TABLE zeiterfassung_history
    add column status smallint not null default 0;

create or replace function zeiterfassung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeiterfassung_history (
            zeiterfassung_id, teilnehmer, zeiterfassung_reason, datum, datum_bis, status,
            created_on, created_by, changed_by, action, action_timestamp)
        VALUES (
            OLD.id, OLD.teilnehmer, OLD.zeiterfassung_reason, OLD.datum, OLD.datum_bis, NEW.status,
            OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeiterfassung_history (
            zeiterfassung_id, teilnehmer, zeiterfassung_reason, datum, datum_bis, status,
            created_on, created_by, changed_by, action, action_timestamp)
        VALUES (
            NEW.id, NEW.teilnehmer, NEW.zeiterfassung_reason, NEW.datum, NEW.datum_bis, NEW.status,
            NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeiterfassung_history (
            zeiterfassung_id, teilnehmer, zeiterfassung_reason, datum, datum_bis, status,
            created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer, NEW.zeiterfassung_reason, NEW.datum, NEW.datum_bis, NEW.status,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

