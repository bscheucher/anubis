create table if not exists projekt_type
(
    id             integer generated always as identity primary key,
    name           text,
    status         smallint                            not null,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_on     timestamp,
    changed_by     text
);

create table if not exists projekt_type_history
(
    id               integer generated always as identity primary key,
    projekt_type_id       integer                             not null,
    name           text,
    status           smallint                            not null,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp                           not null
);

create or replace function projekt_type_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO projekt_type_history (projekt_type_id, name, status,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO projekt_type_history (projekt_type_id, name, status,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO projekt_type_history (projekt_type_id, name, status,
                                                    created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.status,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function projekt_type_audit() owner to ibosng;

create trigger projekt_type_insert
    after insert
    on projekt_type
    for each row
execute procedure projekt_type_audit();

create trigger projekt_type_update
    after update
    on projekt_type
    for each row
execute procedure projekt_type_audit();

create trigger projekt_type_delete
    after delete
    on projekt_type
    for each row
execute procedure projekt_type_audit();

INSERT INTO projekt_type (name, status, created_by)
VALUES
    ('Normal', 1, current_user),
    ('ÜBA Wien', 1, current_user),
    ('ÜBA NÖ', 1, current_user);




alter table projekt drop column kostenstelle_sub;
alter table projekt_history drop column kostenstelle_sub;

alter table projekt add column projekt_type integer references projekt_type(id);
alter table projekt_history add column projekt_type integer;

create or replace function projekt_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO projekt_history (projekt_id, projekt_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, projekt_type, kostentraeger, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (OLD.id, OLD.projekt_nummer, OLD.auftrag_nummer, OLD.bezeichnung, OLD.start_date, OLD.end_date, OLD.kostenstelle_gruppe, OLD.kostenstelle, OLD.projekt_type, OLD.kostentraeger, OLD.created_on, OLD.changed_on, OLD.created_by, OLD.changed_by, OLD.ausschreibung_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO projekt_history (projekt_id, projekt_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, projekt_type, kostentraeger, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (NEW.id, NEW.projekt_nummer, NEW.auftrag_nummer, NEW.bezeichnung, NEW.start_date, NEW.end_date, NEW.kostenstelle_gruppe, NEW.kostenstelle, NEW.projekt_type, NEW.kostentraeger, NEW.created_on, NEW.changed_on, NEW.created_by, NEW.changed_by, NEW.ausschreibung_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO projekt_history (projekt_id, projekt_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, projekt_type, kostentraeger, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (NEW.id, NEW.projekt_nummer, NEW.auftrag_nummer, NEW.bezeichnung, NEW.start_date, NEW.end_date, NEW.kostenstelle_gruppe, NEW.kostenstelle, NEW.projekt_type, NEW.kostentraeger, NEW.created_on, NEW.changed_on, NEW.created_by, NULL, NEW.ausschreibung_id, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function projekt_audit() owner to ibosng;

