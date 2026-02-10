create table if not exists zeiterfassung_transfer
(
    id             integer generated always as identity primary key,
    datum_von           date,
    datum_bis           date,
    datum_sent  date,
    status         smallint                            not null,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_on     timestamp,
    changed_by     text
);

create table if not exists zeiterfassung_transfer_history
(
    id               integer generated always as identity primary key,
    zeiterfassung_transfer_id       integer                             not null,
    datum_von           date,
    datum_bis           date,
    datum_sent  date,
    status           smallint                            not null,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp                           not null
);

create or replace function zeiterfassung_transfer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeiterfassung_transfer_history (zeiterfassung_transfer_id, datum_von, datum_bis, datum_sent, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.datum_von, OLD.datum_bis, OLD.datum_sent, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeiterfassung_transfer_history (zeiterfassung_transfer_id, datum_von, datum_bis, datum_sent, datum_sent, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.datum_von, NEW.datum_bis, NEW.datum_sent, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeiterfassung_transfer_history (zeiterfassung_transfer_id, datum_von, datum_bis, datum_sent, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.datum_von, NEW.datum_bis, NEW.datum_sent, NEW.status,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function zeiterfassung_transfer_audit() owner to ibosng;

create trigger zeiterfassung_transfer_insert
    after insert
    on zeiterfassung_transfer
    for each row
execute procedure zeiterfassung_transfer_audit();

create trigger zeiterfassung_transfer_update
    after update
    on zeiterfassung_transfer
    for each row
execute procedure zeiterfassung_transfer_audit();

create trigger zeiterfassung_transfer_delete
    after delete
    on zeiterfassung_transfer
    for each row
execute procedure zeiterfassung_transfer_audit();


alter table zeiterfassung add column zeiterfassung_transfer integer references zeiterfassung_transfer(id);
alter table zeiterfassung_history add column zeiterfassung_transfer integer;

create or replace function zeiterfassung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeiterfassung_history (
            zeiterfassung_id, teilnehmer, zeiterfassung_reason, datum, datum_bis, zeiterfassung_transfer,
            created_on, created_by, changed_by, action, action_timestamp)
        VALUES (
                   OLD.id, OLD.teilnehmer, OLD.zeiterfassung_reason, OLD.datum, OLD.datum_bis, OLD.zeiterfassung_transfer,
                   OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeiterfassung_history (
            zeiterfassung_id, teilnehmer, zeiterfassung_reason, datum, datum_bis, zeiterfassung_transfer,
            created_on, created_by, changed_by, action, action_timestamp)
        VALUES (
                   NEW.id, NEW.teilnehmer, NEW.zeiterfassung_reason, NEW.datum, NEW.datum_bis, NEW.zeiterfassung_transfer,
                   NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeiterfassung_history (
            zeiterfassung_id, teilnehmer, zeiterfassung_reason, datum, datum_bis, zeiterfassung_transfer,
            created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer, NEW.zeiterfassung_reason, NEW.datum, NEW.datum_bis, NEW.zeiterfassung_transfer,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


create table if not exists zeiterfassung_transfer_seminar
(
    id         integer generated always as identity primary key,
    zeiterfassung_transfer_id       integer references zeiterfassung_transfer(id),
    seminar_id    integer references seminar(id)
);

create table if not exists zeiterfassung_transfer_seminar_history
(
    id               integer generated always as identity primary key,
    zeiterfassung_transfer_seminar_id     integer   not null,
    zeiterfassung_transfer_id       integer,
    seminar_id    integer,
    action           char,
    action_timestamp timestamp not null
);

create or replace function zeiterfassung_transfer_seminar_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeiterfassung_transfer_seminar_history (zeiterfassung_transfer_seminar_id, zeiterfassung_transfer_id, seminar_id,
                                                action,
                                                action_timestamp)
        VALUES (OLD.id, OLD.zeiterfassung_transfer_id, OLD.seminar_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeiterfassung_transfer_seminar_history (zeiterfassung_transfer_seminar_id, zeiterfassung_transfer_id, seminar_id,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.zeiterfassung_transfer_id, NEW.seminar_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeiterfassung_transfer_seminar_history (zeiterfassung_transfer_seminar_id, zeiterfassung_transfer_id, seminar_id,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.zeiterfassung_transfer_id, NEW.seminar_id,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


alter function zeiterfassung_transfer_seminar_audit() owner to ibosng;

create trigger zeiterfassung_transfer_seminar_insert
    after insert
    on zeiterfassung_transfer_seminar
    for each row
execute procedure zeiterfassung_transfer_seminar_audit();

create trigger zeiterfassung_transfer_seminar_update
    after update
    on zeiterfassung_transfer_seminar
    for each row
execute procedure zeiterfassung_transfer_seminar_audit();

create trigger zeiterfassung_transfer_seminar_delete
    after delete
    on zeiterfassung_transfer_seminar
    for each row
execute procedure zeiterfassung_transfer_seminar_audit();