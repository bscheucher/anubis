create table if not exists dienstort
(
    id         integer generated always as identity primary key,
    name       text,
    adresse    integer references adresse (id),
    telefon    integer references telefon (id),
    email      text,
    status     smallint  not null,
    created_on timestamp not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists dienstort_history
(
    id               integer generated always as identity primary key,
    dienstort_id     integer   not null,
    name             text,
    adresse          integer,
    telefon          integer,
    email            text,
    status           smallint  not null,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function dienstort_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO dienstort_history (dienstort_id, name, adresse, telefon, email, status, created_on, created_by,
                                       changed_by,
                                       action,
                                       action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.adresse, OLD.telefon, OLD.email, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO dienstort_history (dienstort_id, name, adresse, telefon, email, status, status, created_on,
                                       created_by,
                                       changed_by,
                                       action,
                                       action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.adresse, NEW.telefon, NEW.email, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO dienstort_history (dienstort_id, name, adresse, telefon, email, status, status, created_on,
                                       created_by,
                                       changed_by,
                                       action,
                                       action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.adresse, NEW.telefon, NEW.email, NEW.status, NEW.created_on, NEW.created_by, NULL,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


ALTER TABLE vertragsdaten
    ALTER COLUMN fuerhrungskraft TYPE TEXT
        USING CASE WHEN fuerhrungskraft THEN 'true' ELSE 'false' END;

ALTER TABLE vertragsdaten
    ALTER COLUMN startcoach TYPE TEXT
        USING CASE WHEN startcoach THEN 'true' ELSE 'false' END;


ALTER TABLE vertragsdaten_history
    ALTER COLUMN fuerhrungskraft TYPE TEXT
        USING CASE WHEN fuerhrungskraft THEN 'true' ELSE 'false' END;

ALTER TABLE vertragsdaten_history
    ALTER COLUMN startcoach TYPE TEXT
        USING CASE WHEN startcoach THEN 'true' ELSE 'false' END;

alter table vertragsdaten
    drop column dienstort;
alter table vertragsdaten_history
    drop column dienstort;

alter table vertragsdaten
    add column dienstort integer references dienstort (id);
alter table vertragsdaten_history
    add column dienstort integer;

