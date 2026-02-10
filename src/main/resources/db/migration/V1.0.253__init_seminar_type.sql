create table if not exists seminar_type
(
    id             integer generated always as identity primary key,
    name           text,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_on     timestamp,
    changed_by     text
);

create table if not exists seminar_type_history
(
    id               integer generated always as identity primary key,
    seminar_type_id  integer                             not null,
    name             text,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp                           not null
);

create or replace function seminar_type_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO seminar_type_history (seminar_type_id, name,
                                          created_on, created_by, changed_by,
                                          action,
                                          action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D',now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO seminar_type_history (seminar_type_id, name,
                                          created_on, created_by, changed_by,
                                          action,
                                          action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U',now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO seminar_type_history (seminar_type_id, name,
                                          created_on, created_by, changed_by,
                                          action,
                                          action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function seminar_type_audit() owner to ibosng;

create trigger seminar_type_insert
    after insert
    on seminar_type
    for each row
execute procedure seminar_type_audit();

create trigger seminar_type_update
    after update
    on seminar_type
    for each row
execute procedure seminar_type_audit();

create trigger seminar_type_delete
    after delete
    on seminar_type
    for each row
execute procedure seminar_type_audit();

-- TODO check if this is necessary or if we want to do it over the sync logic.
INSERT INTO seminar_type (name, created_by)
VALUES
    ('Seminar',  current_user),
    ('Workshop', current_user),
    ('Nachbetreuung', current_user);

alter table seminar drop column type;
alter table seminar_history drop column type;

alter table seminar add column seminar_type integer references seminar_type(id);
alter table seminar_history add column seminar_type integer;
alter table seminar add column massnahmen_nr text;
alter table seminar_history add column massnahmen_nr text;

create or replace function seminar_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO seminar_history (seminar_id, seminar_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, seminar_type, massnahmen_nr, kostentraeger, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (OLD.id, OLD.seminar_nummer, OLD.auftrag_nummer, OLD.bezeichnung, OLD.start_date, OLD.end_date, OLD.kostenstelle_gruppe, OLD.kostenstelle, OLD.seminar_type, OLD.massnahmen_nr, OLD.kostentraeger, OLD.created_on, OLD.changed_on, OLD.created_by, OLD.changed_by, OLD.ausschreibung_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO seminar_history (seminar_id, seminar_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, seminar_type, massnahmen_nr, kostentraeger, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (NEW.id, NEW.seminar_nummer, NEW.auftrag_nummer, NEW.bezeichnung, NEW.start_date, NEW.end_date, NEW.kostenstelle_gruppe, NEW.kostenstelle, NEW.seminar_type, NEW.massnahmen_nr, NEW.kostentraeger, NEW.created_on, NEW.changed_on, NEW.created_by, NEW.changed_by, NEW.ausschreibung_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO seminar_history (seminar_id, seminar_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, seminar_type, massnahmen_nr, kostentraeger, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (NEW.id, NEW.seminar_nummer, NEW.auftrag_nummer, NEW.bezeichnung, NEW.start_date, NEW.end_date, NEW.kostenstelle_gruppe, NEW.kostenstelle, NEW.seminar_type, NEW.massnahmen_nr, NEW.kostentraeger, NEW.created_on, NEW.changed_on, NEW.created_by, NULL, NEW.ausschreibung_id, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function seminar_audit() owner to ibosng;

