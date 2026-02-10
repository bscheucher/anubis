-- Create the 'rollen_gruppen' table
create table if not exists rollen_gruppen
(
    id          integer generated always as identity
                primary key,
    name        text,
    description text,
    created_on  timestamp default CURRENT_TIMESTAMP not null,
    created_by  text                                not null,
    changed_by  text,
    status      smallint  default 0                 not null
);

alter table rollen_gruppen
    owner to ibosng;

-- Create the 'rollen_gruppen_history' table
create table if not exists rollen_gruppen_history
(
    id                  integer generated always as identity,
    rollen_gruppen_id   integer not null,
    name                text,
    description         text,
    created_on          timestamp not null,
    created_by          text      not null,
    changed_by          text,
    status              smallint  not null,
    action              char,
    action_timestamp    timestamp not null
);

alter table rollen_gruppen_history
    owner to ibosng;

create or replace function rollen_gruppen_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO rollen_gruppen_history (rollen_gruppen_id, name, description, status, created_on, created_by, changed_by,
                                            action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.description, OLD.status, OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO rollen_gruppen_history (rollen_gruppen_id, name, description, status, created_on, created_by, changed_by,
                                            action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.description, NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO rollen_gruppen_history (rollen_gruppen_id, name, description, status, created_on, created_by, changed_by,
                                            action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.description, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

-- Create the 'rollen_gruppen_audit' function
alter function rollen_gruppen_audit() owner to ibosng;

create trigger rollen_gruppen_insert
    after insert
    on rollen_gruppen
    for each row
execute procedure rollen_gruppen_audit();

create trigger rollen_gruppen_update
    after update
    on rollen_gruppen
    for each row
execute procedure rollen_gruppen_audit();

create trigger rollen_gruppen_delete
    after delete
    on rollen_gruppen
    for each row
execute procedure rollen_gruppen_audit();

---------------------------------------------------------------------------------------------------------------------

-- Create the 'rollen' table
create table if not exists rollen
(
    id                   integer generated always as identity
        primary key,
    name                 text,
    description          text,
    created_on           timestamp default CURRENT_TIMESTAMP not null,
    created_by           text                                not null,
    changed_by           text,
    status               smallint  default 0                 not null
);

alter table rollen
    owner to ibosng;

-- Create the 'rollen_history' table
create table if not exists rollen_history
(
    id                  integer generated always as identity,
    rollen_id           integer not null,
    name                text,
    description         text,
    created_on          timestamp not null,
    created_by          text      not null,
    changed_by          text,
    status              smallint  not null,
    action              char,
    action_timestamp    timestamp not null
);

alter table rollen_history
    owner to ibosng;

-- Create the 'rollen_audit' function
create or replace function rollen_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO rollen_history (rollen_id,  name, description, status, created_on, created_by, changed_by,
                                    action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.description, OLD.status, OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO rollen_history (rollen_id, name, description, status, created_on, created_by, changed_by,
                                    action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.description, NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO rollen_history (rollen_id, name, description, status, created_on, created_by, changed_by,
                                    action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.description, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function rollen_audit() owner to ibosng;

-- Create triggers for the 'rollen' table
create trigger rollen_insert
    after insert
    on rollen
    for each row
execute procedure rollen_audit();

create trigger rollen_update
    after update
    on rollen
    for each row
execute procedure rollen_audit();

create trigger rollen_delete
    after delete
    on rollen
    for each row
execute procedure rollen_audit();

--------------------------------------------------------------------------------------------------------------------
-- Create the 'funktionen' table
create table if not exists funktionen
(
    id                integer generated always as identity
        primary key,
    name              text,
    description       text,
    created_on        timestamp default CURRENT_TIMESTAMP not null,
    created_by        text                                not null,
    changed_by        text,
    status            smallint  default 0                 not null
);

alter table funktionen
    owner to ibosng;

-- Create the 'funktionen_history' table
create table if not exists funktionen_history
(
    id                  integer generated always as identity,
    funktion_id         integer not null,
    name                text,
    description         text,
    created_on          timestamp not null,
    created_by          text      not null,
    changed_by          text,
    status              smallint  not null,
    action              char,
    action_timestamp    timestamp not null
);

alter table funktionen_history
    owner to ibosng;

-- Create the 'funktionen_audit' function
create or replace function funktionen_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO funktionen_history (funktion_id, name, description, status, created_on, created_by, changed_by,
                                       action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.description, OLD.status, OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO funktionen_history (funktion_id, name, description, status, created_on, created_by, changed_by,
                                       action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.description, NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO funktionen_history (funktion_id, name, description, status, created_on, created_by, changed_by,
                                       action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.description, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function funktionen_audit() owner to ibosng;

-- Create triggers for the 'funktionen' table
create trigger funktionen_insert
    after insert
    on funktionen
    for each row
execute procedure funktionen_audit();

create trigger funktionen_update
    after update
    on funktionen
    for each row
execute procedure funktionen_audit();

create trigger funktionen_delete
    after delete
    on funktionen
    for each row
execute procedure funktionen_audit();

--------------------------------------------------------------------------------------------------------------------
create table if not exists benutzer_2_rolle
(
    id            integer generated always as identity
        primary key,
    benutzer_id integer                             not null
        references benutzer,
    rollen_id    integer                             not null
        references rollen,
    created_on    timestamp default CURRENT_TIMESTAMP not null
);

alter table benutzer_2_rolle
    owner to ibosng;

create table if not exists benutzer_2_rolle_history
(
    id                      integer generated always as identity
        primary key,
    benutzer_2_rolle_id     integer                             not null,
    benutzer_id             integer                             not null,
    rollen_id               integer                             not null,
    created_on              timestamp,
    action                  char,
    action_timestamp        timestamp default CURRENT_TIMESTAMP not null
);

alter table benutzer_2_rolle_history
    owner to ibosng;

create or replace function benutzer_2_rolle_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO benutzer_2_rolle_history (benutzer_2_rolle_id, benutzer_id, rollen_id, created_on, action,
                                                  action_timestamp)
        VALUES (OLD.id, OLD.benutzer_id, OLD.rollen_id, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO benutzer_2_rolle_history (benutzer_2_rolle_id, benutzer_id, rollen_id, created_on, action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.rollen_id, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO benutzer_2_rolle_history (benutzer_2_rolle_id, benutzer_id, rollen_id, created_on, action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.rollen_id, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function benutzer_2_rolle_audit() owner to ibosng;

create trigger benutzer_2_rolle_insert
    after insert
    on benutzer_2_rolle
    for each row
execute procedure benutzer_2_rolle_audit();

create trigger benutzer_2_rolle_update
    after update
    on benutzer_2_rolle
    for each row
execute procedure benutzer_2_rolle_audit();

create trigger benutzer_2_rolle_delete
    after delete
    on benutzer_2_rolle
    for each row
execute procedure benutzer_2_rolle_audit();




create table if not exists benutzer_2_rollengruppe
(
    id            integer generated always as identity
        primary key,
    benutzer_id integer                             not null
        references benutzer,
    rollengruppe_id    integer                             not null
        references rollen_gruppen,
    created_on    timestamp default CURRENT_TIMESTAMP not null
);

alter table benutzer_2_rollengruppe
    owner to ibosng;

create table if not exists benutzer_2_rollengruppe_history
(
    id                      integer generated always as identity
        primary key,
    benutzer_2_rollengruppe_id     integer                             not null,
    benutzer_id             integer                             not null,
    rollengruppe_id               integer                             not null,
    created_on              timestamp,
    action                  char,
    action_timestamp        timestamp default CURRENT_TIMESTAMP not null
);

alter table benutzer_2_rollengruppe_history
    owner to ibosng;

create or replace function benutzer_2_rollengruppe_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO benutzer_2_rollengruppe_history (benutzer_2_rollengruppe_id, benutzer_id, rollengruppe_id, created_on, action,
                                                     action_timestamp)
        VALUES (OLD.id, OLD.benutzer_id, OLD.rollengruppe_id, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO benutzer_2_rollengruppe_history (benutzer_2_rollengruppe_id, benutzer_id, rollengruppe_id, created_on, action,
                                                     action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.rollengruppe_id, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO benutzer_2_rollengruppe_history (benutzer_2_rollengruppe_id, benutzer_id, rollengruppe_id, created_on, action,
                                                     action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.rollengruppe_id, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function benutzer_2_rollengruppe_audit() owner to ibosng;

create trigger benutzer_2_rollengruppe_insert
    after insert
    on benutzer_2_rollengruppe
    for each row
execute procedure benutzer_2_rollengruppe_audit();

create trigger benutzer_2_rollengruppe_update
    after update
    on benutzer_2_rollengruppe
    for each row
execute procedure benutzer_2_rollengruppe_audit();

create trigger benutzer_2_rollengruppe_delete
    after delete
    on benutzer_2_rollengruppe
    for each row
execute procedure benutzer_2_rollengruppe_audit();


create table if not exists benutzer_2_funktion
(
    id            integer generated always as identity
        primary key,
    benutzer_id integer                             not null
        references benutzer,
    funktion_id    integer                             not null
        references funktionen,
    created_on    timestamp default CURRENT_TIMESTAMP not null
);

alter table benutzer_2_funktion
    owner to ibosng;

create table if not exists benutzer_2_funktion_history
(
    id                      integer generated always as identity
        primary key,
    benutzer_2_funktion_id     integer                             not null,
    benutzer_id             integer                             not null,
    funktion_id               integer                             not null,
    created_on              timestamp,
    action                  char,
    action_timestamp        timestamp default CURRENT_TIMESTAMP not null
);

alter table benutzer_2_funktion_history
    owner to ibosng;

create or replace function benutzer_2_funktion_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO benutzer_2_funktion_history (benutzer_2_funktion_id, benutzer_id, funktion_id, created_on, action,
                                                 action_timestamp)
        VALUES (OLD.id, OLD.benutzer_id, OLD.funktion_id, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO benutzer_2_funktion_history (benutzer_2_funktion_id, benutzer_id, funktion_id, created_on, action,
                                                 action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.funktion_id, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO benutzer_2_funktion_history (benutzer_2_funktion_id, benutzer_id, funktion_id, created_on, action,
                                                 action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.funktion_id, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function benutzer_2_funktion_audit() owner to ibosng;

create trigger benutzer_2_funktion_insert
    after insert
    on benutzer_2_funktion
    for each row
execute procedure benutzer_2_funktion_audit();

create trigger benutzer_2_funktion_update
    after update
    on benutzer_2_funktion
    for each row
execute procedure benutzer_2_funktion_audit();

create trigger benutzer_2_funktion_delete
    after delete
    on benutzer_2_funktion
    for each row
execute procedure benutzer_2_funktion_audit();




create table if not exists rollengruppe_2_rollen
(
    id            integer generated always as identity
        primary key,
    rolle_id integer                             not null
        references rollen,
    rollengruppe_id    integer                             not null
        references rollen_gruppen,
    created_on    timestamp default CURRENT_TIMESTAMP not null
);

alter table rollengruppe_2_rollen
    owner to ibosng;

create table if not exists rollengruppe_2_rollen_history
(
    id                      integer generated always as identity
        primary key,
    rollengruppe_2_rollen_id     integer                             not null,
    rolle_id             integer                             not null,
    rollengruppe_id               integer                             not null,
    created_on              timestamp,
    action                  char,
    action_timestamp        timestamp default CURRENT_TIMESTAMP not null
);

alter table rollengruppe_2_rollen_history
    owner to ibosng;

create or replace function rollengruppe_2_rollen_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO rollengruppe_2_rollen_history (rollengruppe_2_rollen_id, rolle_id, rollengruppe_id, created_on, action,
                                                   action_timestamp)
        VALUES (OLD.id, OLD.rolle_id, OLD.rollengruppe_id, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO rollengruppe_2_rollen_history (rollengruppe_2_rollen_id, rolle_id, rollengruppe_id, created_on, action,
                                                   action_timestamp)
        VALUES (NEW.id, NEW.rolle_id, NEW.rollengruppe_id, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO rollengruppe_2_rollen_history (rollengruppe_2_rollen_id, rolle_id, rollengruppe_id, created_on, action,
                                                   action_timestamp)
        VALUES (NEW.id, NEW.rolle_id, NEW.rollengruppe_id, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function rollengruppe_2_rollen_audit() owner to ibosng;

create trigger rollengruppe_2_rollen_insert
    after insert
    on rollengruppe_2_rollen
    for each row
execute procedure rollengruppe_2_rollen_audit();

create trigger rollengruppe_2_rollen_update
    after update
    on rollengruppe_2_rollen
    for each row
execute procedure rollengruppe_2_rollen_audit();

create trigger rollengruppe_2_rollen_delete
    after delete
    on rollengruppe_2_rollen
    for each row
execute procedure rollengruppe_2_rollen_audit();




create table if not exists rollen_2_funktionen
(
    id            integer generated always as identity
        primary key,
    rolle_id integer                             not null
        references rollen,
    funktion_id    integer                             not null
        references funktionen,
    created_on    timestamp default CURRENT_TIMESTAMP not null
);

alter table rollen_2_funktionen
    owner to ibosng;

create table if not exists rollen_2_funktionen_history
(
    id                      integer generated always as identity
        primary key,
    rollengruppe_2_rollen_id     integer                             not null,
    rolle_id             integer                             not null,
    funktion_id               integer                             not null,
    created_on              timestamp,
    action                  char,
    action_timestamp        timestamp default CURRENT_TIMESTAMP not null
);

alter table rollen_2_funktionen_history
    owner to ibosng;

create or replace function rollen_2_funktionen_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO rollen_2_funktionen_history (rollengruppe_2_rollen_id, rolle_id, funktion_id, created_on, action,
                                                 action_timestamp)
        VALUES (OLD.id, OLD.rolle_id, OLD.funktion_id, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO rollen_2_funktionen_history (rollengruppe_2_rollen_id, rolle_id, funktion_id, created_on, action,
                                                 action_timestamp)
        VALUES (NEW.id, NEW.rolle_id, NEW.funktion_id, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO rollen_2_funktionen_history (rollengruppe_2_rollen_id, rolle_id, funktion_id, created_on, action,
                                                 action_timestamp)
        VALUES (NEW.id, NEW.rolle_id, NEW.funktion_id, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function rollen_2_funktionen_audit() owner to ibosng;

create trigger rollen_2_funktionen_insert
    after insert
    on rollen_2_funktionen
    for each row
execute procedure rollen_2_funktionen_audit();

create trigger rollen_2_funktionen_update
    after update
    on rollen_2_funktionen
    for each row
execute procedure rollen_2_funktionen_audit();

create trigger rollen_2_funktionen_delete
    after delete
    on rollen_2_funktionen
    for each row
execute procedure rollen_2_funktionen_audit();






insert into funktionen (name , description, created_by)
values ('FN_WIDGET_MT', 'Widget Meine Seminare.', current_user),
       ('FN_WIDGET_MT_FEHLER_TN', 'Widget Fehlerhafte Teilnehmerdaten.', current_user),
       ('FN_WIDGET_MPD', 'Widget meine persönlichen Data anzeigen.', current_user),
       ('FN_WIDGET_PC_EIGEN', 'Widget Project Controlling anzeigen (eigene Projekte).', current_user),
       ('FN_WIDGET_PC_ALL', 'Widget Project Controlling anzeigen (alle Projekte).', current_user),
       ('FN_MA_ERFASSEN', 'Mitarbeiter erfassen', current_user);