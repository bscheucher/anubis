

create table if not exists zeitausgleich_fuehrungskraft
(
    id         integer generated always as identity primary key,
    benutzer_id       integer references benutzer(id),
    zeitausgleich_id    integer references zeitausgleich(id)
);

create table if not exists zeitausgleich_fuehrungskraft_history
(
    id               integer generated always as identity primary key,
    zeitausgleich_fuehrungskraft_id     integer   not null,
    benutzer_id       integer,
    zeitausgleich_id    integer,
    action           char,
    action_timestamp timestamp not null
);

create or replace function zeitausgleich_fuehrungskraft_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeitausgleich_fuehrungskraft_history (zeitausgleich_fuehrungskraft_id, benutzer_id, zeitausgleich_id,
                                                          action,
                                                          action_timestamp)
        VALUES (OLD.id, OLD.benutzer_id, OLD.zeitausgleich_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeitausgleich_fuehrungskraft_history (zeitausgleich_fuehrungskraft_id, benutzer_id, zeitausgleich_id,
                                                          action,
                                                          action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.zeitausgleich_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeitausgleich_fuehrungskraft_history (zeitausgleich_fuehrungskraft_id, benutzer_id, zeitausgleich_id,
                                                          action,
                                                          action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.zeitausgleich_id,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


alter function zeitausgleich_fuehrungskraft_audit() owner to ibosng;

create trigger zeitausgleich_fuehrungskraft_insert
    after insert
    on zeitausgleich_fuehrungskraft
    for each row
execute procedure zeitausgleich_fuehrungskraft_audit();

create trigger zeitausgleich_fuehrungskraft_update
    after update
    on zeitausgleich_fuehrungskraft
    for each row
execute procedure zeitausgleich_fuehrungskraft_audit();

create trigger zeitausgleich_fuehrungskraft_delete
    after delete
    on zeitausgleich_fuehrungskraft
    for each row
execute procedure zeitausgleich_fuehrungskraft_audit();