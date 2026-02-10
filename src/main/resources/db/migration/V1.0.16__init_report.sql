create table if not exists report
(
    id          integer generated always as identity primary key,
    report_name text,
    blob_name   text
);

alter table report
    owner to ibosng;

create table if not exists report_history
(
    id               integer generated always as identity primary key,
    report_id        integer not null,
    report_name      text,
    blob_name        text,
    action           char,
    action_timestamp timestamp not null
);

alter table report_history
    owner to ibosng;

create or replace function report_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO report_history (report_id, report_name, blob_name, action, action_timestamp)
        VALUES (OLD.id, OLD.report_name, OLD.blob_name, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO report_history (report_id, report_name, blob_name, action, action_timestamp)
        VALUES (NEW.id, NEW.report_name, NEW.blob_name, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO report_history (report_id, report_name, blob_name, action, action_timestamp)
        VALUES (NEW.id, NEW.report_name, NEW.blob_name, 'I', now());
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

alter function report_audit() owner to ibosng;

create trigger report_insert
    after insert
    on report
    for each row
    execute procedure report_audit();

create trigger report_update
    after update
    on report
    for each row
    execute procedure report_audit();

create trigger report_delete
    after delete
    on report
    for each row
    execute procedure report_audit();
