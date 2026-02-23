alter table seminar drop column kostentraeger;
alter table seminar_history drop column kostentraeger;

create or replace function seminar_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO seminar_history (seminar_id, seminar_nummer, project, identifier, seminar_type, bezeichnung,
                                     start_date, end_date, start_time, end_time, status, massnahmen_nr, created_on,
                                     changed_on, created_by, changed_by, standort, schiene_uhrzeit,
                                     action, action_timestamp)
        VALUES (OLD.id, OLD.seminar_nummer, OLD.project, OLD.identifier, OLD.seminar_type, OLD.bezeichnung,
                OLD.start_date, OLD.end_date, OLD.start_time, OLD.end_time, OLD.status, OLD.massnahmen_nr,
                OLD.created_on, OLD.changed_on, OLD.created_by, OLD.changed_by, OLD.standort,
                OLD.schiene_uhrzeit, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO seminar_history (seminar_id, seminar_nummer, project, identifier, seminar_type, bezeichnung,
                                     start_date, end_date, start_time, end_time, status, massnahmen_nr, created_on,
                                     changed_on, created_by, changed_by, standort, schiene_uhrzeit,
                                     action, action_timestamp)
        VALUES (NEW.id, NEW.seminar_nummer, NEW.project, NEW.identifier, NEW.seminar_type, NEW.bezeichnung,
                NEW.start_date, NEW.end_date, NEW.start_time, NEW.end_time, NEW.status, NEW.massnahmen_nr,
                NEW.created_on, NEW.changed_on, NEW.created_by, NEW.changed_by, NEW.standort,
                NEW.schiene_uhrzeit, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO seminar_history (seminar_id, seminar_nummer, project, identifier, seminar_type, bezeichnung,
                                     start_date, end_date, start_time, end_time, status, massnahmen_nr, created_on,
                                     changed_on, created_by, changed_by, standort, schiene_uhrzeit,
                                     action, action_timestamp)
        VALUES (NEW.id, NEW.seminar_nummer, NEW.project, NEW.identifier, NEW.seminar_type, NEW.bezeichnung,
                NEW.start_date, NEW.end_date, NEW.start_time, NEW.end_time, NEW.status, NEW.massnahmen_nr,
                NEW.created_on, NEW.changed_on, NEW.created_by, NEW.changed_by, NEW.standort,
                NEW.schiene_uhrzeit, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function seminar_audit() owner to ibosng;

grant execute on function seminar_audit() to ibosng;

