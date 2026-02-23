alter table zeiterfassung_reason add column lhr_kz text;
alter table zeiterfassung_reason_history add column lhr_kz text;

create or replace function zeiterfassung_reason_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeiterfassung_reason_history (zeiterfassung_reason_id, bezeichnung, short_bezeichnung, lhr_kz, created_on, created_by, changed_by, action, action_timestamp )
        VALUES (OLD.id, OLD.bezeichnung, OLD.short_bezeichnung, OLD.lhr_kz, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeiterfassung_reason_history (zeiterfassung_reason_id, bezeichnung, short_bezeichnung, lhr_kz, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bezeichnung, NEW.short_bezeichnung, NEW.lhr_kz, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeiterfassung_reason_history ( zeiterfassung_reason_id, bezeichnung, short_bezeichnung, lhr_kz, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bezeichnung, NEW.short_bezeichnung, NEW.lhr_kz, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function zeiterfassung_reason_audit() owner to ibosng;




update zeiterfassung
set zeiterfassung_reason = null
where zeiterfassung_reason in (select id
                               from zeiterfassung_reason
                               where (bezeichnung = 'Pflegefreistellung' and short_bezeichnung = 'PF')
                                  or (bezeichnung = 'Praktikum' and short_bezeichnung = 'P')
                                  or (bezeichnung = 'Berufsschule' and short_bezeichnung = 'B')
                                  or (bezeichnung = 'Arbeitsaufnahme' and short_bezeichnung = 'X')
                                  or (bezeichnung = 'Kursabbruch' and short_bezeichnung = 'X')
                                  or (bezeichnung = 'Ausschluss' and short_bezeichnung = 'X')
                                  or (bezeichnung = 'Kursende' and short_bezeichnung = 'X')
                                  or (bezeichnung = 'Urlaub' and short_bezeichnung = 'U')
                                  or (bezeichnung = 'Anwesend' and short_bezeichnung = 'X')
                                  or (bezeichnung = 'Entschuldigt' and short_bezeichnung = 'E'));

delete
from zeiterfassung_reason
where (bezeichnung = 'Pflegefreistellung' and short_bezeichnung = 'PF')
   or (bezeichnung = 'Praktikum' and short_bezeichnung = 'P')
   or (bezeichnung = 'Berufsschule' and short_bezeichnung = 'B')
   or (bezeichnung = 'Arbeitsaufnahme' and short_bezeichnung = 'X')
   or (bezeichnung = 'Kursabbruch' and short_bezeichnung = 'X')
   or (bezeichnung = 'Ausschluss' and short_bezeichnung = 'X')
   or (bezeichnung = 'Kursende' and short_bezeichnung = 'X')
   or (bezeichnung = 'Urlaub' and short_bezeichnung = 'U')
   or (bezeichnung = 'Anwesend' and short_bezeichnung = 'X')
   or (bezeichnung = 'Entschuldigt' and short_bezeichnung = 'E');

update zeiterfassung_reason set lhr_kz = 'NE' where bezeichnung != 'Krankenstand mit Bestätigung';
update zeiterfassung_reason set lhr_kz = 'K' where bezeichnung = 'Krankenstand mit Bestätigung';