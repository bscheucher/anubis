-- Create the report_parameter table
create table if not exists report_parameter
(
    id          integer generated always as identity primary key,
    name        text not null,
    type        text not null, -- Enum values will need to be validated in application logic
    description text,
    report_id   integer not null references report(id) on delete cascade,
    required    boolean, -- New column
    label       text -- New column
    );

alter table report_parameter
    owner to ibosng;

-- Create the report_parameter_history table for audit logging
create table if not exists report_parameter_history
(
    id               integer generated always as identity primary key,
    report_parameter_id integer not null,
    name             text,
    type             text,
    description      text,
    report_id        integer,
    required         boolean, -- New column
    label            text, -- New column
    action           char,
    action_timestamp timestamp not null
);

alter table report_parameter_history
    owner to ibosng;

-- Create or replace audit function for report_parameter
create or replace function report_parameter_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO report_parameter_history (report_parameter_id, name, type, description, report_id, required, label, action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.type, OLD.description, OLD.report_id, OLD.required, OLD.label, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO report_parameter_history (report_parameter_id, name, type, description, report_id, required, label, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.type, NEW.description, NEW.report_id, NEW.required, NEW.label, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO report_parameter_history (report_parameter_id, name, type, description, report_id, required, label, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.type, NEW.description, NEW.report_id, NEW.required, NEW.label, 'I', now());
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

alter function report_parameter_audit() owner to ibosng;

-- Create triggers for report_parameter
create trigger report_parameter_insert
    after insert
    on report_parameter
    for each row
    execute procedure report_parameter_audit();

create trigger report_parameter_update
    after update
    on report_parameter
    for each row
    execute procedure report_parameter_audit();

create trigger report_parameter_delete
    after delete
    on report_parameter
    for each row
    execute procedure report_parameter_audit();