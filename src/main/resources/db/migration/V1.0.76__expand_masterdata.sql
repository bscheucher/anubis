-- Create the main table for Abrechnungsgruppe
create table if not exists abrechnungsgruppe
(
    id           integer generated always as identity primary key,
    abbreviation text not null,
    bezeichnung  text not null,
    created_on   timestamp default CURRENT_TIMESTAMP not null,
    created_by   text not null,
    changed_on   timestamp,
    changed_by   text
);

-- Create the history table for Abrechnungsgruppe
create table if not exists abrechnungsgruppe_history
(
    id                   integer generated always as identity primary key,
    abrechnungsgruppe_id integer not null,
    abbreviation         text not null,
    bezeichnung          text not null,
    created_on           timestamp not null,
    created_by           text not null,
    changed_on           timestamp,
    changed_by           text,
    action               char,
    action_timestamp     timestamp not null
);

-- Create the audit function for Abrechnungsgruppe
create or replace function abrechnungsgruppe_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO abrechnungsgruppe_history (abrechnungsgruppe_id, abbreviation, bezeichnung, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.abbreviation, OLD.bezeichnung, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO abrechnungsgruppe_history (abrechnungsgruppe_id, abbreviation, bezeichnung, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.abbreviation, NEW.bezeichnung, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO abrechnungsgruppe_history (abrechnungsgruppe_id, abbreviation, bezeichnung, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.abbreviation, NEW.bezeichnung, NEW.created_on, NEW.created_by, NULL, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function abrechnungsgruppe_audit() owner to ibosng;

-- Create triggers for Abrechnungsgruppe
create trigger abrechnungsgruppe_insert
    after insert
    on abrechnungsgruppe
    for each row
    execute procedure abrechnungsgruppe_audit();

create trigger abrechnungsgruppe_update
    after update
    on abrechnungsgruppe
    for each row
    execute procedure abrechnungsgruppe_audit();

create trigger abrechnungsgruppe_delete
    after delete
    on abrechnungsgruppe
    for each row
    execute procedure abrechnungsgruppe_audit();


INSERT INTO abrechnungsgruppe (abbreviation, bezeichnung, created_on, created_by)
VALUES
    ('F_FDN', 'Freie Dienstnehmer', CURRENT_TIMESTAMP, current_user),
    ('A_GF', 'GF & Geschäftsfeldleit.', CURRENT_TIMESTAMP, current_user),
    ('A_BER', 'GF Beratung', CURRENT_TIMESTAMP, current_user),
    ('A_DE', 'GF DaF/DaZ', CURRENT_TIMESTAMP, current_user),
    ('A_ERW', 'GF Erwachsene', CURRENT_TIMESTAMP, current_user),
    ('A_FR', 'GF Frauen', CURRENT_TIMESTAMP, current_user),
    ('A_JUG', 'GF Jugend', CURRENT_TIMESTAMP, current_user);



-- Create the main table for Dienstnehmergruppe
create table if not exists dienstnehmergruppe
(
    id           integer generated always as identity primary key,
    abbreviation text not null,
    bezeichnung  text not null,
    created_on   timestamp default CURRENT_TIMESTAMP not null,
    created_by   text not null,
    changed_on   timestamp,
    changed_by   text
);

-- Create the history table for Dienstnehmergruppe
create table if not exists dienstnehmergruppe_history
(
    id                     integer generated always as identity primary key,
    dienstnehmergruppe_id  integer not null,
    abbreviation           text not null,
    bezeichnung            text not null,
    created_on             timestamp not null,
    created_by             text not null,
    changed_on             timestamp,
    changed_by             text,
    action                 char,
    action_timestamp       timestamp not null
);

-- Create the audit function for Dienstnehmergruppe
create or replace function dienstnehmergruppe_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO dienstnehmergruppe_history (dienstnehmergruppe_id, abbreviation, bezeichnung, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.abbreviation, OLD.bezeichnung, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO dienstnehmergruppe_history (dienstnehmergruppe_id, abbreviation, bezeichnung, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.abbreviation, NEW.bezeichnung, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO dienstnehmergruppe_history (dienstnehmergruppe_id, abbreviation, bezeichnung, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.abbreviation, NEW.bezeichnung, NEW.created_on, NEW.created_by, NULL, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function dienstnehmergruppe_audit() owner to ibosng;

-- Create triggers for Dienstnehmergruppe
create trigger dienstnehmergruppe_insert
    after insert
    on dienstnehmergruppe
    for each row
    execute procedure dienstnehmergruppe_audit();

create trigger dienstnehmergruppe_update
    after update
    on dienstnehmergruppe
    for each row
    execute procedure dienstnehmergruppe_audit();

create trigger dienstnehmergruppe_delete
    after delete
    on dienstnehmergruppe
    for each row
    execute procedure dienstnehmergruppe_audit();


INSERT INTO dienstnehmergruppe (abbreviation, bezeichnung, created_on, created_by)
VALUES
    ('A_BGL', 'ANG BGLD', CURRENT_TIMESTAMP, current_user),
    ('A_NOE', 'ANG NÖ', CURRENT_TIMESTAMP, current_user),
    ('A_OOE', 'ANG OÖ', CURRENT_TIMESTAMP, current_user),
    ('A_SBG', 'ANG SBG', CURRENT_TIMESTAMP, current_user),
    ('A_TIR', 'ANG Tirol', CURRENT_TIMESTAMP, current_user),
    ('A_VBG', 'ANG VBG', CURRENT_TIMESTAMP, current_user),
    ('A_WIE', 'ANG Wien', CURRENT_TIMESTAMP, current_user);


-- Create the main table for Kommunalsteuergemeinde
create table if not exists kommunalsteuergemeinde
(
    id           integer generated always as identity primary key,
    lhr_id       text not null,
    ort          text not null,
    created_on   timestamp default CURRENT_TIMESTAMP not null,
    created_by   text not null,
    changed_on   timestamp,
    changed_by   text
);

-- Create the history table for Kommunalsteuergemeinde
create table if not exists kommunalsteuergemeinde_history
(
    id                          integer generated always as identity primary key,
    kommunalsteuergemeinde_id   integer not null,
    lhr_id                      text not null,
    ort                         text not null,
    created_on                  timestamp not null,
    created_by                  text not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

-- Create the audit function for Kommunalsteuergemeinde
create or replace function kommunalsteuergemeinde_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO kommunalsteuergemeinde_history (kommunalsteuergemeinde_id, lhr_id, ort, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.lhr_id, OLD.ort, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO kommunalsteuergemeinde_history (kommunalsteuergemeinde_id, lhr_id, ort, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.lhr_id, NEW.ort, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO kommunalsteuergemeinde_history (kommunalsteuergemeinde_id, lhr_id, ort, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.lhr_id, NEW.ort, NEW.created_on, NEW.created_by, NULL, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function kommunalsteuergemeinde_audit() owner to ibosng;

-- Create triggers for Kommunalsteuergemeinde
create trigger kommunalsteuergemeinde_insert
    after insert
    on kommunalsteuergemeinde
    for each row
    execute procedure kommunalsteuergemeinde_audit();

create trigger kommunalsteuergemeinde_update
    after update
    on kommunalsteuergemeinde
    for each row
    execute procedure kommunalsteuergemeinde_audit();

create trigger kommunalsteuergemeinde_delete
    after delete
    on kommunalsteuergemeinde
    for each row
    execute procedure kommunalsteuergemeinde_audit();
