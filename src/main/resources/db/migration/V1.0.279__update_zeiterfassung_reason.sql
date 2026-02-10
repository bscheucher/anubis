alter table zeiterfassung_reason add column ibos_id integer;
alter table zeiterfassung_reason_history add column ibos_id integer;

create or replace function zeiterfassung_reason_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeiterfassung_reason_history (zeiterfassung_reason_id, bezeichnung, short_bezeichnung, ibos_id, lhr_kz,
                                                  created_on, created_by, changed_by, action, action_timestamp,
                                                  anwesend)
        VALUES (OLD.id, OLD.bezeichnung, OLD.short_bezeichnung, OLD.ibos_id, OLD.lhr_kz, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now(), OLD.anwesend);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeiterfassung_reason_history (zeiterfassung_reason_id, bezeichnung, short_bezeichnung, ibos_id, lhr_kz,
                                                  created_on, created_by, changed_by, action, action_timestamp,
                                                  anwesend)
        VALUES (NEW.id, NEW.bezeichnung, NEW.short_bezeichnung, NEW.ibos_id, NEW.lhr_kz, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now(), NEW.anwesend);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeiterfassung_reason_history (zeiterfassung_reason_id, bezeichnung, short_bezeichnung, ibos_id, lhr_kz,
                                                  created_on, created_by, changed_by, action, action_timestamp,
                                                  anwesend)
        VALUES (NEW.id, NEW.bezeichnung, NEW.short_bezeichnung, NEW.ibos_id, NEW.lhr_kz, NEW.created_on, NEW.created_by, NULL, 'I',
                now(), NEW.anwesend);
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function zeiterfassung_reason_audit() owner to ibosng;

grant execute on function zeiterfassung_reason_audit() to ibosng;

update zeiterfassung_reason set ibos_id = 4480 where short_bezeichnung = 'K';
update zeiterfassung_reason set ibos_id = 4480 where short_bezeichnung = 'KS';
update zeiterfassung_reason set ibos_id = 3842 where short_bezeichnung = 'KSB';
update zeiterfassung_reason set ibos_id = 4477 where short_bezeichnung = 'NE';
update zeiterfassung_reason set ibos_id = 4477 where short_bezeichnung = 'U';
update zeiterfassung_reason set ibos_id = 4480 where short_bezeichnung = 'K';