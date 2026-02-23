create table if not exists vertragsart
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists vertragsart_history
(
    id               integer generated always as identity primary key,
    vertragsart_id   integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function vertragsart_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vertragsart_history (vertragsart_id, name, created_on, created_by, changed_by,
                                         action,
                                         action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsart_history (vertragsart_id, name, created_on, created_by, changed_by,
                                         action,
                                         action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsart_history (vertragsart_id, name, created_on, created_by, changed_by,
                                         action,
                                         action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function vertragsart_audit() owner to ibosng;

create trigger vertragsart_insert
    after insert
    on vertragsart
    for each row
execute procedure vertragsart_audit();

create trigger vertragsart_update
    after update
    on vertragsart
    for each row
execute procedure vertragsart_audit();

create trigger vertragsart_delete
    after delete
    on vertragsart
    for each row
execute procedure vertragsart_audit();

insert into vertragsart (name, created_by)
values ('Fix', current_user),
       ('Freier Dienstnehmer', current_user),
       ('Werkvertrag', current_user),
       ('Selbständig', current_user);



create table if not exists vordienstzeiten
(
    id               integer generated always as identity primary key,
    vertragsdaten_id INTEGER REFERENCES vertragsdaten (id),
    vertragsart      integer references vertragsart (id),
    firma            text,
    von              date,
    bis              date,
    wochenstunden    double precision,
    anrechenbar      double precision,
    nachweis         text,
    status           smallint  not null,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text
);

create table if not exists vordienstzeiten_history
(
    id                 integer generated always as identity primary key,
    vordienstzeiten_id integer   not null,
    vertragsdaten_id   INTEGER,
    vertragsart        integer,
    firma              text,
    von                date,
    bis                date,
    wochenstunden      double precision,
    anrechenbar        double precision,
    nachweis           text,
    status             smallint  not null,
    created_on         timestamp not null,
    created_by         text      not null,
    changed_on         timestamp,
    changed_by         text,
    action             char,
    action_timestamp   timestamp not null
);

create or replace function vordienstzeiten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis, status, created_on,
                                             created_by,
                                             changed_by,
                                             action,
                                             action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.vertragsart, OLD.firma, OLD.von, OLD.bis, OLD.wochenstunden,
                OLD.anrechenbar, OLD.nachweis, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis, status, status,
                                             created_on,
                                             created_by,
                                             changed_by,
                                             action,
                                             action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis, status, status,
                                             created_on,
                                             created_by,
                                             changed_by,
                                             action,
                                             action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis, NEW.status, NEW.created_on, NEW.created_by, NULL,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


