create table if not exists abwesenheit_fuehrungskraft
(
    id         integer generated always as identity primary key,
    benutzer_id       integer references benutzer(id),
    abwesenheit_id    integer references abwesenheit(id)
);

create table if not exists abwesenheit_fuehrungskraft_history
(
    id               integer generated always as identity primary key,
    abwesenheit_fuehrungskraft_id     integer   not null,
    benutzer_id       integer,
    abwesenheit_id    integer,
    action           char,
    action_timestamp timestamp not null
);

create or replace function abwesenheit_fuehrungskraft_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO abwesenheit_fuehrungskraft_history (abwesenheit_fuehrungskraft_id, benutzer_id, abwesenheit_id,
                                                action,
                                                action_timestamp)
        VALUES (OLD.id, OLD.benutzer_id, OLD.abwesenheit_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO abwesenheit_fuehrungskraft_history (abwesenheit_fuehrungskraft_id, benutzer_id, abwesenheit_id,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.abwesenheit_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO abwesenheit_fuehrungskraft_history (abwesenheit_fuehrungskraft_id, benutzer_id, abwesenheit_id,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.abwesenheit_id,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


alter function abwesenheit_fuehrungskraft_audit() owner to ibosng;

create trigger abwesenheit_fuehrungskraft_insert
    after insert
    on abwesenheit_fuehrungskraft
    for each row
execute procedure abwesenheit_fuehrungskraft_audit();

create trigger abwesenheit_fuehrungskraft_update
    after update
    on abwesenheit_fuehrungskraft
    for each row
execute procedure abwesenheit_fuehrungskraft_audit();

create trigger abwesenheit_fuehrungskraft_delete
    after delete
    on abwesenheit_fuehrungskraft
    for each row
execute procedure abwesenheit_fuehrungskraft_audit();