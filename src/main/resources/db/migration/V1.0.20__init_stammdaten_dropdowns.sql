create table if not exists anrede
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists anrede_history
(
    id               integer generated always as identity primary key,
    anrede_id        integer   not null,
    name             text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function anrede_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO anrede_history (anrede_id, name, created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO anrede_history (anrede_id, name, created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO anrede_history (anrede_id, name, created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function anrede_audit() owner to ibosng;

create trigger anrede_insert
    after insert
    on anrede
    for each row
execute procedure anrede_audit();

create trigger anrede_update
    after update
    on anrede
    for each row
execute procedure anrede_audit();

create trigger anrede_delete
    after delete
    on anrede
    for each row
execute procedure anrede_audit();

insert into anrede (name, created_by)
values ('Herr', current_user),
       ('Frau', current_user),
       ('keine Angabe', current_user);


--GESCHLECHT
create table if not exists geschlecht
(
    id           integer generated always as identity primary key,
    name         text,
    abbreviation text,
    created_on   timestamp default CURRENT_TIMESTAMP not null,
    created_by   text                                not null,
    changed_on   timestamp,
    changed_by   text
);

create table if not exists geschlecht_history
(
    id               integer generated always as identity primary key,
    geschlecht_id    integer   not null,
    name             text,
    abbreviation     text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function geschlecht_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO geschlecht_history (geschlecht_id, name, abbreviation, created_on, created_by, changed_by,
                                        action,
                                        action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.abbreviation, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO geschlecht_history (geschlecht_id, name, abbreviation, created_on, created_by, changed_by,
                                        action,
                                        action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.abbreviation, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO geschlecht_history (geschlecht_id, name, abbreviation, created_on, created_by, changed_by,
                                        action,
                                        action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.abbreviation, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function geschlecht_audit() owner to ibosng;

create trigger geschlecht_insert
    after insert
    on geschlecht
    for each row
execute procedure geschlecht_audit();

create trigger geschlecht_update
    after update
    on geschlecht
    for each row
execute procedure geschlecht_audit();

create trigger geschlecht_delete
    after delete
    on geschlecht
    for each row
execute procedure geschlecht_audit();

insert into geschlecht (name, abbreviation, created_by)
values ('Männlich', 'M', current_user),
       ('Weiblich', 'W', current_user),
       ('divers', 'D', current_user),
       ('offen', 'O', current_user),
       ('inter', 'I', current_user),
       ('keine Angabe', 'K', current_user);


--FAMILIENSTAND
create table if not exists familienstand
(
    id           integer generated always as identity primary key,
    name         text,
    abbreviation text,
    created_on   timestamp default CURRENT_TIMESTAMP not null,
    created_by   text                                not null,
    changed_on   timestamp,
    changed_by   text
);

create table if not exists familienstand_history
(
    id               integer generated always as identity primary key,
    familienstand_id integer   not null,
    name             text,
    abbreviation     text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function familienstand_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO familienstand_history (familienstand_id, name, abbreviation, created_on, created_by, changed_by,
                                           action,
                                           action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.abbreviation, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO familienstand_history (familienstand_id, name, abbreviation, created_on, created_by, changed_by,
                                           action,
                                           action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.abbreviation, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO familienstand_history (familienstand_id, name, abbreviation, created_on, created_by, changed_by,
                                           action,
                                           action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.abbreviation, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function familienstand_audit() owner to ibosng;

create trigger familienstand_insert
    after insert
    on familienstand
    for each row
execute procedure familienstand_audit();

create trigger familienstand_update
    after update
    on familienstand
    for each row
execute procedure familienstand_audit();

create trigger familienstand_delete
    after delete
    on familienstand
    for each row
execute procedure familienstand_audit();

insert into familienstand (name, abbreviation, created_by)
values ('Ledig', 'L', current_user),
       ('Verheiratet', 'V', current_user),
       ('Verwitwet', 'W', current_user),
       ('Partnerschaft', 'P', current_user),
       ('Partnerschaft aufgelöst', 'PA', current_user),
       ('Gericht', 'A', current_user),
       ('Tod', 'T', current_user);


--ARBEITSGENEHMIGUNG
create table if not exists arbeitsgenehmigung
(
    id         integer generated always as identity primary key,
    name       text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists arbeitsgenehmigung_history
(
    id                    integer generated always as identity primary key,
    arbeitsgenehmigung_id integer   not null,
    name                  text,
    created_on            timestamp not null,
    created_by            text      not null,
    changed_on            timestamp,
    changed_by            text,
    action                char,
    action_timestamp      timestamp not null
);

create or replace function arbeitsgenehmigung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO arbeitsgenehmigung_history (arbeitsgenehmigung_id, name, created_on, created_by, changed_by,
                                                action,
                                                action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO arbeitsgenehmigung_history (arbeitsgenehmigung_id, name, created_on, created_by, changed_by,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO arbeitsgenehmigung_history (arbeitsgenehmigung_id, name, created_on, created_by, changed_by,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function arbeitsgenehmigung_audit() owner to ibosng;

create trigger arbeitsgenehmigung_insert
    after insert
    on arbeitsgenehmigung
    for each row
execute procedure arbeitsgenehmigung_audit();

create trigger arbeitsgenehmigung_update
    after update
    on arbeitsgenehmigung
    for each row
execute procedure arbeitsgenehmigung_audit();

create trigger arbeitsgenehmigung_delete
    after delete
    on arbeitsgenehmigung
    for each row
execute procedure arbeitsgenehmigung_audit();

insert into arbeitsgenehmigung (name, created_by)
values ('Rot-Weiß-Rot - Karte', current_user),
       ('Rot-Weiß-Rot - Karte plus', current_user),
       ('Blaue Karte EU', current_user),
       ('Konventionspass', current_user),
       ('Subsidiäre Schutzberechtigung', current_user),
       ('EU Freizügigkeitsbescheinigung', current_user),
       ('Sonstige', current_user),
       ('Daueraufenthalt-EG', current_user),
       ('Daueraufenthalt-EU', current_user);


--TITEL
create table if not exists titel
(
    id         integer generated always as identity primary key,
    name       text,
    position   smallint                            not null,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists titel_history
(
    id               integer generated always as identity primary key,
    titel_id         integer   not null,
    name             text,
    position         smallint  not null,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function titel_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO titel_history (titel_id, name, position, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.position, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO titel_history (titel_id, name, position, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.position, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO titel_history (titel_id, name, position, created_on, created_by, changed_by,
                                   action,
                                   action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.position, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function titel_audit() owner to ibosng;

create trigger titel_insert
    after insert
    on titel
    for each row
execute procedure titel_audit();

create trigger titel_update
    after update
    on titel
    for each row
execute procedure titel_audit();

create trigger titel_delete
    after delete
    on titel
    for each row
execute procedure titel_audit();

insert into titel (name, position, created_by)
values ('DDr.', 1, current_user),
       ('DI (FH)', 1, current_user),
       ('DI Mag.', 1, current_user),
       ('Dipl. Ing.', 1, current_user),
       ('Dipl.-Wirt', 1, current_user),
       ('Dipl.Soz.P', 1, current_user),
       ('Dkffr.', 1, current_user),
       ('Dkfm.', 1, current_user),
       ('Dr.', 1, current_user),
       ('Dr. Ing.', 1, current_user),
       ('Dr. Phil.', 1, current_user),
       ('DAS', 1, current_user),
       ('Ing.', 1, current_user),
       ('Ing. (FH)', 1, current_user),
       ('Ing. Mag.', 1, current_user),
       ('Kom.Rat', 1, current_user),
       ('Mag(FH)', 1, current_user),
       ('Mag.', 1, current_user),
       ('Mag. Dr.', 1, current_user),
       ('Mag. phil.', 1, current_user),
       ('Mag. a', 1, current_user),
       ('Mag. a (FH)', 1, current_user),
       ('Mag. a phil', 1, current_user),
       ('Mmag.', 1, current_user),
       ('MMag.a', 1, current_user),
       ('Univ. Doz.', 1, current_user),
       ('Univ.Ass.', 1, current_user),
       ('Univ.Prof.', 1, current_user),
       ('BSc', 0, current_user),
       ('BA', 0, current_user),
       ('Bakk.', 0, current_user),
       ('BEd', 0, current_user),
       ('BSc', 0, current_user),
       ('Di.Reg.Wis', 0, current_user),
       ('Dipl-Soz.P', 0, current_user),
       ('Dipl.Päd.', 0, current_user),
       ('MA', 0, current_user),
       ('MAS', 0, current_user),
       ('MBA', 0, current_user),
       ('MSc', 0, current_user),
       ('MTD', 0, current_user);