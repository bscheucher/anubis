create table if not exists mitarbeiter_data_status
(
    id              integer generated always as identity primary key,
    stammdaten      INTEGER references stammdaten (id),
    vertragsdaten   INTEGER references vertragsdaten (id),
    vordienstzeiten INTEGER references vordienstzeiten (id),
    personalnummer  integer references personalnummer (id),
    data_type       TEXT                                NOT NULL,
    error           text,
    cause           text,
    created_on      timestamp default CURRENT_TIMESTAMP not null,
    created_by      text                                not null,
    changed_on      timestamp,
    changed_by      text,
    CHECK (stammdaten IS NOT NULL OR vertragsdaten IS NOT NULL OR vordienstzeiten IS NOT NULL),
    CHECK (NOT (stammdaten IS NOT NULL AND vertragsdaten IS NOT NULL AND vordienstzeiten IS NOT NULL))
);

create table if not exists mitarbeiter_data_status_history
(
    id                         integer generated always as identity primary key,
    mitarbeiter_data_status_id integer   not null,
    stammdaten                 INTEGER,
    vertragsdaten              INTEGER,
    vordienstzeiten            INTEGER,
    personalnummer             integer,
    error                      text,
    cause                      text,
    created_on                 timestamp not null,
    created_by                 text      not null,
    changed_on                 timestamp,
    changed_by                 text,
    action                     char,
    action_timestamp           timestamp not null
);

create or replace function mitarbeiter_data_status_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO mitarbeiter_data_status_history (mitarbeiter_data_status_id, stammdaten, vertragsdaten,
                                                     vordienstzeiten, personalnummer,
                                                     error, cause,
                                                     created_on, created_by, changed_by,
                                                     action,
                                                     action_timestamp)
        VALUES (OLD.id, OLD.stammdaten, OLD.vertragsdaten, OLD.vordienstzeiten, OLD.personalnummer, OLD.error,
                OLD.cause, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO mitarbeiter_data_status_history (mitarbeiter_data_status_id, stammdaten, vertragsdaten,
                                                     vordienstzeiten, personalnummer,
                                                     error, cause,
                                                     created_on, created_by, changed_by,
                                                     action,
                                                     action_timestamp)
        VALUES (NEW.id, NEW.stammdaten, NEW.vertragsdaten, NEW.vordienstzeiten, NEW.personalnummer, NEW.error,
                NEW.cause, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO mitarbeiter_data_status_history (mitarbeiter_data_status_id, stammdaten, vertragsdaten,
                                                     vordienstzeiten, personalnummer,
                                                     error, cause,
                                                     created_on, created_by, changed_by,
                                                     action,
                                                     action_timestamp)
        VALUES (NEW.id, NEW.stammdaten, NEW.vertragsdaten, NEW.vordienstzeiten, NEW.personalnummer, NEW.error,
                NEW.cause, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function mitarbeiter_data_status_audit() owner to ibosng;

create trigger mitarbeiter_data_status_insert
    after insert
    on mitarbeiter_data_status
    for each row
execute procedure mitarbeiter_data_status_audit();

create trigger mitarbeiter_data_status_update
    after update
    on mitarbeiter_data_status
    for each row
execute procedure mitarbeiter_data_status_audit();

create trigger mitarbeiter_data_status_delete
    after delete
    on mitarbeiter_data_status
    for each row
execute procedure mitarbeiter_data_status_audit();