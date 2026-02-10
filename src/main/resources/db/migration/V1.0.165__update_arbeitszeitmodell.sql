alter table arbeitszeitmodell
    add column lhr_kz text;
alter table arbeitszeitmodell_history
    add column lhr_kz text;

alter table arbeitszeitmodell
    add column lhr_nr integer;
alter table arbeitszeitmodell_history
    add column lhr_nr integer;

create or replace function arbeitszeitmodell_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO arbeitszeitmodell_history (arbeitszeitmodell_id, name, lhr_kz, lhr_nr, created_on, created_by,
                                               changed_by,
                                               action,
                                               action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.lhr_kz, OLD.lhr_nr, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO arbeitszeitmodell_history (arbeitszeitmodell_id, name, lhr_kz, lhr_nr, created_on, created_by,
                                               changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_kz, NEW.lhr_nr, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO arbeitszeitmodell_history (arbeitszeitmodell_id, name, lhr_kz, lhr_nr, created_on, created_by,
                                               changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.lhr_kz, NEW.lhr_nr, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function arbeitszeitmodell_audit() owner to ibosng;

insert into arbeitszeitmodell (name, lhr_kz, lhr_nr, created_by)
values ('Gleitzeit ÜP', 'GZÜP', 3, current_user),
       ('Fix mit 7h Pause (NÖ) Vollzeit', 'FVZ7P', 25, current_user),
       ('Fix mit 7h Pause (NÖ) Teilzeit', 'FTZ7P', 26, current_user);


update arbeitszeitmodell
set lhr_nr = 1,
    lhr_kz = 'GZVZ'
where name = 'Gleitzeit - VZ';
update arbeitszeitmodell
set lhr_nr = 2,
    lhr_kz = 'GZTZ'
where name = 'Gleitzeit - TZ';
update arbeitszeitmodell
set lhr_nr = 11,
    lhr_kz = 'FZVZ'
where name = 'Fixzeitmodell - VZ';
update arbeitszeitmodell
set lhr_nr = 12,
    lhr_kz = 'FZTZ'
where name = 'Fixzeitmodell - TZ';
update arbeitszeitmodell
set lhr_nr = 21,
    lhr_kz = 'FDVZ'
where name = 'Durchrechnung - VZ';
update arbeitszeitmodell
set lhr_nr = 22,
    lhr_kz = 'FDTZ'
where name = 'Durchrechnung - TZ';
update arbeitszeitmodell
set name   = 'All In Vollzeit AIVZ',
    lhr_nr = 5,
    lhr_kz = 'AIVZ'
where name = 'Durchrechnung - VZ Verwaltung';
update arbeitszeitmodell
set name   = 'All In Teilzeit',
    lhr_nr = 6,
    lhr_kz = 'AITZ'
where name = 'Durchrechnung - TZ Verwaltung';