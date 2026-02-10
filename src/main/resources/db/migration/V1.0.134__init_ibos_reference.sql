create table if not exists ibos_reference
(
    id         integer generated always as identity primary key,
    ibosng_id  integer,
    ibos_id    integer,
    data text,
    status     smallint                            not null,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists ibos_reference_history
(
    id                integer generated always as identity primary key,
    ibos_reference_id integer                             not null,
    ibosng_id         integer,
    ibos_id           integer,
    data text,
    status            smallint                            not null,
    created_on        timestamp default CURRENT_TIMESTAMP not null,
    created_by        text                                not null,
    changed_on        timestamp,
    changed_by        text,
    action            char,
    action_timestamp  timestamp                           not null
);

create or replace function ibos_reference_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ibos_reference_history (ibos_reference_id, ibosng_id, ibos_id, data, status,
                                            created_on, created_by, changed_by,
                                            action,
                                            action_timestamp)
        VALUES (OLD.id, OLD.ibosng_id, OLD.ibos_id, NEW.data, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO ibos_reference_history (ibos_reference_id, ibosng_id, ibos_id, data, status,
                                            created_on, created_by, changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.ibosng_id, NEW.ibos_id, NEW.data, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO ibos_reference_history (ibos_reference_id, ibosng_id, ibos_id, data, status,
                                            created_on, created_by, changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.ibosng_id, NEW.ibos_id, NEW.data, NEW.status,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function ibos_reference_audit() owner to ibosng;

create trigger ibos_reference_insert
    after insert
    on ibos_reference
    for each row
execute procedure ibos_reference_audit();

create trigger ibos_reference_update
    after update
    on ibos_reference
    for each row
execute procedure ibos_reference_audit();

create trigger ibos_reference_delete
    after delete
    on ibos_reference
    for each row
execute procedure ibos_reference_audit();

alter table vertragsdaten
    drop column fuehrungskraft;
alter table vertragsdaten_history
    drop column fuehrungskraft;

alter table vertragsdaten
    drop column startcoach;
alter table vertragsdaten_history
    drop column startcoach;

alter table vertragsdaten
    add column fuehrungskraft integer references ibos_reference (id);
alter table vertragsdaten_history
    add column fuehrungskraft integer;

alter table vertragsdaten
    add column startcoach integer references ibos_reference (id);;
alter table vertragsdaten_history
    add column startcoach integer;