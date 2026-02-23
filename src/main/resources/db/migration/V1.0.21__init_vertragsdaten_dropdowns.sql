create table if not exists beschaeftigungsausmass
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists beschaeftigungsausmass_history
(
    id               integer generated always as identity primary key,
    beschaeftigungsausmass_id        integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function beschaeftigungsausmass_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO beschaeftigungsausmass_history (beschaeftigungsausmass_id, name, created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO beschaeftigungsausmass_history (beschaeftigungsausmass_id, name, created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO beschaeftigungsausmass_history (beschaeftigungsausmass_id, name, created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function beschaeftigungsausmass_audit() owner to ibosng;

create trigger beschaeftigungsausmass_insert
    after insert
    on beschaeftigungsausmass
    for each row
execute procedure beschaeftigungsausmass_audit();

create trigger beschaeftigungsausmass_update
    after update
    on beschaeftigungsausmass
    for each row
execute procedure beschaeftigungsausmass_audit();

create trigger beschaeftigungsausmass_delete
    after delete
    on beschaeftigungsausmass
    for each row
execute procedure beschaeftigungsausmass_audit();

insert into beschaeftigungsausmass (name, created_by)
values ('Vollversicherung', current_user),
       ('Geringfügig', current_user);


create table if not exists beschaeftigungsstatus
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists beschaeftigungsstatus_history
(
    id                    integer generated always as identity primary key,
    beschaeftigungsstatus_id integer   not null,
    name                  text,
    created_on            timestamp not null,
    created_by            text      not null,
    changed_on            timestamp,
    changed_by            text,
    action                char,
    action_timestamp      timestamp not null
);

create or replace function beschaeftigungsstatus_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO beschaeftigungsstatus_history (beschaeftigungsstatus_id, name, created_on, created_by, changed_by,
                                                action,
                                                action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO beschaeftigungsstatus_history (beschaeftigungsstatus_id, name, created_on, created_by, changed_by,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO beschaeftigungsstatus_history (beschaeftigungsstatus_id, name, created_on, created_by, changed_by,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function beschaeftigungsstatus_audit() owner to ibosng;

create trigger beschaeftigungsstatus_insert
    after insert
    on beschaeftigungsstatus
    for each row
execute procedure beschaeftigungsstatus_audit();

create trigger beschaeftigungsstatus_update
    after update
    on beschaeftigungsstatus
    for each row
execute procedure beschaeftigungsstatus_audit();

create trigger beschaeftigungsstatus_delete
    after delete
    on beschaeftigungsstatus
    for each row
execute procedure beschaeftigungsstatus_audit();

insert into beschaeftigungsstatus (name, created_by)
values ('Angestellter', current_user),
       ('Lehrlinge', current_user),
       ('Volontäre', current_user),
       ('Praktikant', current_user);


create table if not exists jobbeschreibung
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists jobbeschreibung_history
(
    id               integer generated always as identity primary key,
    jobbeschreibung_id         integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function jobbeschreibung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO jobbeschreibung_history (jobbeschreibung_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO jobbeschreibung_history (jobbeschreibung_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO jobbeschreibung_history (jobbeschreibung_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function jobbeschreibung_audit() owner to ibosng;

create trigger jobbeschreibung_insert
    after insert
    on jobbeschreibung
    for each row
execute procedure jobbeschreibung_audit();

create trigger jobbeschreibung_update
    after update
    on jobbeschreibung
    for each row
execute procedure jobbeschreibung_audit();

create trigger jobbeschreibung_delete
    after delete
    on jobbeschreibung
    for each row
execute procedure jobbeschreibung_audit();

insert into jobbeschreibung (name, created_by)
values ('Trainerin', current_user),
       ('Betriebskontakterin', current_user),
       ('Case Mangerin', current_user),
       ('Technikerin', current_user),
       ('Logistikerin', current_user),
       ('Lohnverrechnerin', current_user),
       ('Buchhalterin', current_user),
       ('uvm', current_user);


create table if not exists kategorie
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists kategorie_history
(
    id               integer generated always as identity primary key,
    kategorie_id         integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function kategorie_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO kategorie_history (kategorie_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO kategorie_history (kategorie_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO kategorie_history (kategorie_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function kategorie_audit() owner to ibosng;

create trigger kategorie_insert
    after insert
    on kategorie
    for each row
execute procedure kategorie_audit();

create trigger kategorie_update
    after update
    on kategorie
    for each row
execute procedure kategorie_audit();

create trigger kategorie_delete
    after delete
    on kategorie
    for each row
execute procedure kategorie_audit();

insert into kategorie (name, created_by)
values ('Führungskraft', current_user),
       ('Schlüsselkraft', current_user),
       ('Training', current_user),
       ('Extern', current_user);


create table if not exists kollektivvertrag
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists kollektivvertrag_history
(
    id               integer generated always as identity primary key,
    kollektivvertrag_id         integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function kollektivvertrag_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO kollektivvertrag_history (kollektivvertrag_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO kollektivvertrag_history (kollektivvertrag_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO kollektivvertrag_history (kollektivvertrag_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function kollektivvertrag_audit() owner to ibosng;

create trigger kollektivvertrag_insert
    after insert
    on kollektivvertrag
    for each row
execute procedure kollektivvertrag_audit();

create trigger kollektivvertrag_update
    after update
    on kollektivvertrag
    for each row
execute procedure kollektivvertrag_audit();

create trigger kollektivvertrag_delete
    after delete
    on kollektivvertrag
    for each row
execute procedure kollektivvertrag_audit();

insert into kollektivvertrag (name, created_by)
values ('BABE', current_user);


create table if not exists taetigkeit
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists taetigkeit_history
(
    id               integer generated always as identity primary key,
    taetigkeit_id         integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function taetigkeit_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO taetigkeit_history (taetigkeit_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO taetigkeit_history (taetigkeit_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO taetigkeit_history (taetigkeit_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function taetigkeit_audit() owner to ibosng;

create trigger taetigkeit_insert
    after insert
    on taetigkeit
    for each row
execute procedure taetigkeit_audit();

create trigger taetigkeit_update
    after update
    on taetigkeit
    for each row
execute procedure taetigkeit_audit();

create trigger taetigkeit_delete
    after delete
    on taetigkeit
    for each row
execute procedure taetigkeit_audit();

insert into taetigkeit (name, created_by)
values ('Sachbearbeitung', current_user),
       ('Training&Coaching', current_user),
       ('Lehrling', current_user),
       ('Assistenz', current_user),
       ('Projektleitung', current_user),
       ('Geschäftsführung', current_user),
       ('Streamleitung', current_user),
       ('Teamleitung', current_user),
       ('Geschäftsfeldleitung', current_user);


create table if not exists verwendungsgruppe
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists verwendungsgruppe_history
(
    id               integer generated always as identity primary key,
    verwendungsgruppe_id         integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function verwendungsgruppe_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO verwendungsgruppe_history (verwendungsgruppe_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO verwendungsgruppe_history (verwendungsgruppe_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO verwendungsgruppe_history (verwendungsgruppe_id, name, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function verwendungsgruppe_audit() owner to ibosng;

create trigger verwendungsgruppe_insert
    after insert
    on verwendungsgruppe
    for each row
execute procedure verwendungsgruppe_audit();

create trigger verwendungsgruppe_update
    after update
    on verwendungsgruppe
    for each row
execute procedure verwendungsgruppe_audit();

create trigger verwendungsgruppe_delete
    after delete
    on verwendungsgruppe
    for each row
execute procedure verwendungsgruppe_audit();

insert into verwendungsgruppe (name, created_by)
values ('VB 1', current_user),
       ('VB 2', current_user),
       ('VB 3', current_user),
       ('VB 4', current_user),
       ('VB 4a', current_user),
       ('VB 5', current_user),
       ('VB 6', current_user),
       ('VB 7', current_user),
       ('VB 8', current_user);



--abgeltung_ueberstunden
create table if not exists abgeltung_ueberstunden
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists abgeltung_ueberstunden_history
(
    id               integer generated always as identity primary key,
    abgeltung_ueberstunden_id         integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function abgeltung_ueberstunden_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO abgeltung_ueberstunden_history (abgeltung_ueberstunden_id, name, created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO abgeltung_ueberstunden_history (abgeltung_ueberstunden_id, name, created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO abgeltung_ueberstunden_history (abgeltung_ueberstunden_id, name, created_on, created_by, changed_by,
                                                    action,
                                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function abgeltung_ueberstunden_audit() owner to ibosng;

create trigger abgeltung_ueberstunden_insert
    after insert
    on abgeltung_ueberstunden
    for each row
execute procedure abgeltung_ueberstunden_audit();

create trigger abgeltung_ueberstunden_update
    after update
    on abgeltung_ueberstunden
    for each row
execute procedure abgeltung_ueberstunden_audit();

create trigger abgeltung_ueberstunden_delete
    after delete
    on abgeltung_ueberstunden
    for each row
execute procedure abgeltung_ueberstunden_audit();

insert into abgeltung_ueberstunden (name, created_by)
values ('All in', current_user),
       ('ÜST-Pauschale', current_user);



--
create table if not exists arbeitszeitmodell
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists arbeitszeitmodell_history
(
    id               integer generated always as identity primary key,
    arbeitszeitmodell_id         integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function arbeitszeitmodell_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO arbeitszeitmodell_history (arbeitszeitmodell_id, name, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO arbeitszeitmodell_history (arbeitszeitmodell_id, name, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO arbeitszeitmodell_history (arbeitszeitmodell_id, name, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function arbeitszeitmodell_audit() owner to ibosng;

create trigger arbeitszeitmodell_insert
    after insert
    on arbeitszeitmodell
    for each row
execute procedure arbeitszeitmodell_audit();

create trigger arbeitszeitmodell_update
    after update
    on arbeitszeitmodell
    for each row
execute procedure arbeitszeitmodell_audit();

create trigger arbeitszeitmodell_delete
    after delete
    on arbeitszeitmodell
    for each row
execute procedure arbeitszeitmodell_audit();

insert into arbeitszeitmodell (name, created_by)
values ('Gleizeit - VZ', current_user),
       ('Gleizeit - TZ', current_user),
       ('Durchrechnung - VZ', current_user),
       ('Durchrechnung - TZ', current_user),
       ('Durchrechnung - VZ Verwaltung', current_user),
       ('Durchrechnung - TZ Verwaltung', current_user),
       ('Fixzeitmodell - VZ', current_user),
       ('Fixzeitmodell - TZ', current_user);



--
create table if not exists art_der_zulage
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists art_der_zulage_history
(
    id               integer generated always as identity primary key,
    art_der_zulage_id         integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function art_der_zulage_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO art_der_zulage_history (art_der_zulage_id, name, created_on, created_by, changed_by,
                                            action,
                                            action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO art_der_zulage_history (art_der_zulage_id, name, created_on, created_by, changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO art_der_zulage_history (art_der_zulage_id, name, created_on, created_by, changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function art_der_zulage_audit() owner to ibosng;

create trigger art_der_zulage_insert
    after insert
    on art_der_zulage
    for each row
execute procedure art_der_zulage_audit();

create trigger art_der_zulage_update
    after update
    on art_der_zulage
    for each row
execute procedure art_der_zulage_audit();

create trigger art_der_zulage_delete
    after delete
    on art_der_zulage
    for each row
execute procedure art_der_zulage_audit();

insert into art_der_zulage (name, created_by)
values ('Funktionszulage', current_user),
       ('Fixzulage', current_user),
       ('Leitungszulage', current_user);