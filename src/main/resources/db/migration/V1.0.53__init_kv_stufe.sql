create table if not exists kv_stufe
(
    id         integer generated always as identity primary key,
    name       text,
    min_months INT NOT NULL,
    max_months INT NOT NULL,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists kv_stufe_history
(
    id               integer generated always as identity primary key,
    kv_stufe_id        integer   not null,
    name             text,
    min_months INT NOT NULL,
    max_months INT NOT NULL,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function kv_stufe_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO kv_stufe_history (kv_stufe_id, name, min_months, max_months, created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.min_months, OLD.max_months, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO kv_stufe_history (kv_stufe_id, name, min_months, max_months, created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.min_months, NEW.max_months, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO kv_stufe_history (kv_stufe_id, name, min_months, max_months, created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.min_months, NEW.max_months, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function kv_stufe_audit() owner to ibosng;

create trigger kv_stufe_insert
    after insert
    on kv_stufe
    for each row
execute procedure kv_stufe_audit();

create trigger kv_stufe_update
    after update
    on kv_stufe
    for each row
execute procedure kv_stufe_audit();

create trigger kv_stufe_delete
    after delete
    on kv_stufe
    for each row
execute procedure kv_stufe_audit();

INSERT INTO kv_stufe (name, min_months, max_months, created_by) VALUES
                                                     ('Stufe 1', 0, 24, current_user),
                                                     ('Stufe 2', 24, 48, current_user),
                                                     ('Stufe 3', 48, 84, current_user),
                                                     ('Stufe 4', 84, 120, current_user),
                                                     ('Stufe 5', 120, 156, current_user),
                                                     ('Stufe 6', 156, 192, current_user),
                                                     ('Stufe 7', 192, 240, current_user),
                                                     ('Stufe 8', 240, 99999, current_user);