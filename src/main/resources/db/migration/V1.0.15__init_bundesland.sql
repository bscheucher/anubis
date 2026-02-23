create table if not exists bundesland
(
    id         integer generated always as identity primary key,
    name       text,
    plz_id     integer,
    created_on timestamp not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists bundesland_history
(
    id               integer generated always as identity primary key,
    bundesland_id    integer   not null,
    name             text,
    plz_id           integer,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function bundesland_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO bundesland_history (bundesland_id, name, plz_id, created_on, created_by, changed_by,
                                        action,
                                        action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.plz_id, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO bundesland_history (bundesland_id, name, plz_id, created_on, created_by, changed_by,
                                        action,
                                        action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.plz_id, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO bundesland_history (bundesland_id, name, plz_id, created_on, created_by, changed_by,
                                        action,
                                        action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.plz_id, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function bundesland_audit() owner to ibosng;

create trigger bundesland_insert
    after insert
    on bundesland
    for each row
execute procedure bundesland_audit();

create trigger bundesland_update
    after update
    on bundesland
    for each row
execute procedure bundesland_audit();

create trigger bundesland_delete
    after delete
    on bundesland
    for each row
execute procedure bundesland_audit();

insert into bundesland (plz_id, name, created_on, created_by, changed_by)
values (1, 'Wien', current_date, current_user, null),
       (2, 'Niederösterreich, Burgenland/Nord', current_date, current_user, null),
       (3, 'Niederösterreich', current_date, current_user, null),
       (4, 'Oberösterreich', current_date, current_user, null),
       (5, 'Salzburg', current_date, current_user, null),
       (6, 'Tirol, Vorarlberg', current_date, current_user, null),
       (7, 'Burgenland/Süd', current_date, current_user, null),
       (8, 'Steiermark', current_date, current_user, null),
       (9, 'Kärnten', current_date, current_user, null);

ALTER TABLE plz
    drop column bundesland;

ALTER TABLE plz_history
    drop column bundesland;

ALTER TABLE plz
    ADD COLUMN bundesland INTEGER REFERENCES bundesland (id);

ALTER TABLE plz_history
    ADD COLUMN bundesland INTEGER;

UPDATE plz
SET bundesland = (SELECT id FROM bundesland WHERE SUBSTRING(plz::TEXT FROM 1 FOR 1)::INTEGER = plz_id LIMIT 1);

ALTER TABLE plz
    ALTER COLUMN bundesland SET NOT NULL;