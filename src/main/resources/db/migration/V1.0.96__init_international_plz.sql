create table if not exists base_plz(
    id integer primary key
);

INSERT INTO base_plz(id) SELECT id FROM plz;
ALTER TABLE base_plz ALTER COLUMN id set default nextval('plz_id_seq');
ALTER TABLE adresse DROP CONSTRAINT adresse_plz_fkey;
ALTER TABLE adresse ADD CONSTRAINT adresse_plz_fkey FOREIGN KEY (plz) REFERENCES base_plz (id);

create table if not exists international_plz
(
    id         integer                             primary key references base_plz,
    plz        text                                ,
    ort        text                                ,
    land       integer                             references land,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_by text
);


alter table international_plz
    owner to ibosng;

create table if not exists international_plz_history
(
    id               integer generated always as identity primary key ,
    plz_id           integer                             not null,
    plz              text                             ,
    ort              text                                ,
    land             integer                             ,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_by       text,
    action           char                                not null,
    action_timestamp timestamp default CURRENT_TIMESTAMP not null
);

alter table international_plz_history
    owner to ibosng;

create or replace function international_plz_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO international_plz_history (plz_id, plz, ort, land, created_on, created_by, changed_by, action,
                                 action_timestamp)
        VALUES (OLD.id, OLD.plz, OLD.ort, OLD.land, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO international_plz_history (plz_id, plz, ort, land, created_on, created_by, changed_by, action,
                                 action_timestamp)
        VALUES (NEW.id, NEW.plz, NEW.ort, NEW.land, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO international_plz_history (plz_id, plz, ort, land, created_on, created_by, changed_by, action,
                                 action_timestamp)
        VALUES (NEW.id, NEW.plz, NEW.ort, NEW.land, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function international_plz_audit() owner to ibosng;

create trigger international_plz_insert
    after insert
    on international_plz
    for each row
execute procedure international_plz_audit();

create trigger international_plz_update
    after update
    on international_plz
    for each row
execute procedure international_plz_audit();

create trigger international_plz_delete
    after delete
    on international_plz
    for each row
execute procedure international_plz_audit();