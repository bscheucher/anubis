create table if not exists ausschreibung
(
    id                  integer generated always as identity primary key,
    ausschreibung_nummer integer,
    bezeichnung         text,
    created_on          timestamp default CURRENT_TIMESTAMP not null,
    changed_on          timestamp,
    created_by          text not null,
    changed_by          text,
    bundesland          integer not null
);

create table if not exists ausschreibung_history
(
    id                  integer generated always as identity primary key,
    ausschreibung_id    integer not null,
    ausschreibung_nummer integer,
    bezeichnung         text,
    created_on          timestamp not null,
    changed_on          timestamp,
    created_by          text not null,
    changed_by          text,
    bundesland          integer not null,
    action              char,
    action_timestamp    timestamp default CURRENT_TIMESTAMP not null
);

alter table ausschreibung_history owner to ibosng;

create or replace function ausschreibung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ausschreibung_history (ausschreibung_id, ausschreibung_nummer, bezeichnung, created_on, changed_on, created_by, changed_by, bundesland, action, action_timestamp)
        VALUES (OLD.id, OLD.ausschreibung_nummer, OLD.bezeichnung, OLD.created_on, OLD.changed_on, OLD.created_by, OLD.changed_by, OLD.bundesland, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO ausschreibung_history (ausschreibung_id, ausschreibung_nummer, bezeichnung, created_on, changed_on, created_by, changed_by, bundesland, action, action_timestamp)
        VALUES (NEW.id, NEW.ausschreibung_nummer, NEW.bezeichnung, NEW.created_on, NEW.changed_on, NEW.created_by, NEW.changed_by, NEW.bundesland, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO ausschreibung_history (ausschreibung_id, ausschreibung_nummer, bezeichnung, created_on, changed_on, created_by, changed_by, bundesland, action, action_timestamp)
        VALUES (NEW.id, NEW.ausschreibung_nummer, NEW.bezeichnung, NEW.created_on, NEW.changed_on, NEW.created_by, NULL, NEW.bundesland, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function ausschreibung_audit() owner to ibosng;

create trigger ausschreibung_insert
    after insert
    on ausschreibung
    for each row
    execute procedure ausschreibung_audit();

create trigger ausschreibung_update
    after update
    on ausschreibung
    for each row
    execute procedure ausschreibung_audit();

create trigger ausschreibung_delete
    after delete
    on ausschreibung
    for each row
    execute procedure ausschreibung_audit();


create table if not exists projekt
(
    id                    integer generated always as identity primary key,
    projekt_nummer        integer,
    auftrag_nummer        integer,
    bezeichnung           text,
    start_date            date,
    end_date              date,
    kostenstelle_gruppe   integer,
    kostenstelle          integer,
    kostenstelle_sub      integer,
    kostentraeger         integer,
    created_on            timestamp default CURRENT_TIMESTAMP not null,
    changed_on            timestamp,
    created_by            text not null,
    changed_by            text,
    ausschreibung_id      integer references ausschreibung(id)
);

create table if not exists projekt_history
(
    id                    integer generated always as identity primary key,
    projekt_id            integer not null,
    projekt_nummer        integer,
    auftrag_nummer        integer,
    bezeichnung           text,
    start_date            date,
    end_date              date,
    kostenstelle_gruppe   integer,
    kostenstelle          integer,
    kostenstelle_sub      integer,
    kostentraeger         integer,
    created_on            timestamp not null,
    changed_on            timestamp,
    created_by            text not null,
    changed_by            text,
    ausschreibung_id      integer,
    action                char,
    action_timestamp      timestamp default CURRENT_TIMESTAMP not null
);

alter table projekt_history owner to ibosng;

create or replace function projekt_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO projekt_history (projekt_id, projekt_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, kostenstelle_sub, kostentraeger, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (OLD.id, OLD.projekt_nummer, OLD.auftrag_nummer, OLD.bezeichnung, OLD.start_date, OLD.end_date, OLD.kostenstelle_gruppe, OLD.kostenstelle, OLD.kostenstelle_sub, OLD.kostentraeger, OLD.created_on, OLD.changed_on, OLD.created_by, OLD.changed_by, OLD.ausschreibung_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO projekt_history (projekt_id, projekt_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, kostenstelle_sub, kostentraeger, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (NEW.id, NEW.projekt_nummer, NEW.auftrag_nummer, NEW.bezeichnung, NEW.start_date, NEW.end_date, NEW.kostenstelle_gruppe, NEW.kostenstelle, NEW.kostenstelle_sub, NEW.kostentraeger, NEW.created_on, NEW.changed_on, NEW.created_by, NEW.changed_by, NEW.ausschreibung_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO projekt_history (projekt_id, projekt_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, kostenstelle_sub, kostentraeger, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (NEW.id, NEW.projekt_nummer, NEW.auftrag_nummer, NEW.bezeichnung, NEW.start_date, NEW.end_date, NEW.kostenstelle_gruppe, NEW.kostenstelle, NEW.kostenstelle_sub, NEW.kostentraeger, NEW.created_on, NEW.changed_on, NEW.created_by, NULL, NEW.ausschreibung_id, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function projekt_audit() owner to ibosng;

create trigger projekt_insert
    after insert
    on projekt
    for each row
execute procedure projekt_audit();

create trigger projekt_update
    after update
    on projekt
    for each row
execute procedure projekt_audit();

create trigger projekt_delete
    after delete
    on projekt
    for each row
execute procedure projekt_audit();
