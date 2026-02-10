create table if not exists jobticket
(
    id             integer generated always as identity primary key,
    name           text not null,
    status         smallint                            not null,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_on     timestamp,
    changed_by     text
);

create table if not exists jobticket_history
(
    id               integer generated always as identity primary key,
    jobticket_id     integer                             not null,
    name             text ,
    status           smallint                            not null,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp                           not null
);

create or replace function jobticket_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO jobticket_history (jobticket_id, name, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO jobticket_history (jobticket_id, name, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO jobticket_history (jobticket_id, name, status,
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

alter function jobticket_audit() owner to ibosng;

create trigger jobticket_insert
    after insert
    on jobticket
    for each row
execute procedure jobticket_audit();

create trigger jobticket_update
    after update
    on jobticket
    for each row
execute procedure jobticket_audit();

create trigger jobticket_delete
    after delete
    on jobticket
    for each row
execute procedure jobticket_audit();

INSERT INTO jobticket (name, status, created_by) VALUES ('Klimaticket', 1, 'db-service');
INSERT INTO jobticket (name, status, created_by) VALUES ('Wochenkarte', 1, 'db-service');
INSERT INTO jobticket (name, status, created_by) VALUES ('Monatskarte', 1, 'db-service');
INSERT INTO jobticket (name, status, created_by) VALUES ('Jahreskarte', 1, 'db-service');
INSERT INTO jobticket (name, status, created_by) VALUES ('Sonstiges', 1, 'db-service');
