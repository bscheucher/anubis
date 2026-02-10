create table if not exists lhr_job
(
    id             integer generated always as identity primary key,
    eintritt       DATE,
    personalnummer integer references personalnummer (id),
    status         smallint                            not null,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_on     timestamp,
    changed_by     text
);

create table if not exists lhr_job_history
(
    id               integer generated always as identity primary key,
    lhr_job_id       integer                             not null,
    eintritt         DATE,
    personalnummer   integer,
    status           smallint                            not null,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp                           not null
);

create or replace function lhr_job_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO lhr_job_history (lhr_job_id, eintritt, personalnummer, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.eintritt, OLD.personalnummer, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO lhr_job_history (lhr_job_id, eintritt, personalnummer, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.eintritt, NEW.personalnummer, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO lhr_job_history (lhr_job_id, eintritt, personalnummer, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.eintritt, NEW.personalnummer, NEW.status,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function lhr_job_audit() owner to ibosng;

create trigger lhr_job_insert
    after insert
    on lhr_job
    for each row
execute procedure lhr_job_audit();

create trigger lhr_job_update
    after update
    on lhr_job
    for each row
execute procedure lhr_job_audit();

create trigger lhr_job_delete
    after delete
    on lhr_job
    for each row
execute procedure lhr_job_audit();
