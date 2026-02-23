-- Create the vereinbarung table
CREATE TABLE IF NOT EXISTS vereinbarung (
    id                INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vereinbarung_name TEXT NOT NULL,
    personalnummer    INTEGER NOT NULL REFERENCES personalnummer(id),
    fuehrungskraft    TEXT,
    status            TEXT NOT NULL,
    firma             INTEGER REFERENCES ibis_firma(id),
    gueltig_ab        DATE,
    created_on        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(255),
    changed_on        TIMESTAMP,
    changed_by        VARCHAR(255)
    );

ALTER TABLE vereinbarung
    OWNER TO ibosng;

-- Create the vereinbarung_history table for audit logging
CREATE TABLE IF NOT EXISTS vereinbarung_history (
    id               INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vereinbarung_id  INTEGER NOT NULL,
    vereinbarung_name TEXT,
    personalnummer   INTEGER,
    fuehrungskraft   TEXT,
    status           TEXT,
    firma            INTEGER,
    gueltig_ab       DATE,
    created_on       TIMESTAMP,
    created_by       VARCHAR(255),
    changed_on       TIMESTAMP,
    changed_by       VARCHAR(255),
    action           CHAR,
    action_timestamp TIMESTAMP NOT NULL
    );

ALTER TABLE vereinbarung_history
    OWNER TO ibosng;

-- Create or replace audit function for vereinbarung
CREATE OR REPLACE FUNCTION vereinbarung_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vereinbarung_history (vereinbarung_id, vereinbarung_name, personalnummer, fuehrungskraft, status, firma, gueltig_ab, created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vereinbarung_name, OLD.personalnummer, OLD.fuehrungskraft, OLD.status, OLD.firma, OLD.gueltig_ab, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', NOW());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vereinbarung_history (vereinbarung_id, vereinbarung_name, personalnummer, fuehrungskraft, status, firma, gueltig_ab, created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vereinbarung_name, NEW.personalnummer, NEW.fuehrungskraft, NEW.status, NEW.firma, NEW.gueltig_ab, NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', NOW());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vereinbarung_history (vereinbarung_id, vereinbarung_name, personalnummer, fuehrungskraft, status, firma, gueltig_ab, created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vereinbarung_name, NEW.personalnummer, NEW.fuehrungskraft, NEW.status, NEW.firma, NEW.gueltig_ab, NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', NOW());
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

ALTER FUNCTION vereinbarung_audit() OWNER TO ibosng;

-- Create triggers for vereinbarung
CREATE TRIGGER vereinbarung_insert
    AFTER INSERT
    ON vereinbarung
    FOR EACH ROW
    EXECUTE FUNCTION vereinbarung_audit();

CREATE TRIGGER vereinbarung_update
    AFTER UPDATE
    ON vereinbarung
    FOR EACH ROW
    EXECUTE FUNCTION vereinbarung_audit();

CREATE TRIGGER vereinbarung_delete
    AFTER DELETE
    ON vereinbarung
    FOR EACH ROW
    EXECUTE FUNCTION vereinbarung_audit();


create table if not exists vereinbarung_report_parameter
(
    id                integer generated always as identity primary key,
    vereinbarung_id   integer not null,
    name              text not null,
    value             text not null,
    type              text not null  -- New column added
);

alter table vereinbarung_report_parameter
    owner to ibosng;

create table if not exists vereinbarung_report_parameter_history
(
    id                     integer generated always as identity primary key,
    vereinbarung_id        integer,
    name                   text,
    value                  text,
    type                   text, -- New column added
    action                 char,
    action_timestamp       timestamp not null
);

alter table vereinbarung_report_parameter_history
    owner to ibosng;

create or replace function vereinbarung_report_parameter_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vereinbarung_report_parameter_history (vereinbarung_id, name, value, type, action, action_timestamp)
        VALUES (OLD.vereinbarung_id, OLD.name, OLD.value, OLD.type, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vereinbarung_report_parameter_history (vereinbarung_id, name, value, type, action, action_timestamp)
        VALUES (NEW.vereinbarung_id, NEW.name, NEW.value, NEW.type, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vereinbarung_report_parameter_history (vereinbarung_id, name, value, type, action, action_timestamp)
        VALUES (NEW.vereinbarung_id, NEW.name, NEW.value, NEW.type, 'I', now());
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

alter function vereinbarung_report_parameter_audit() owner to ibosng;

create trigger vereinbarung_report_parameter_insert
    after insert
    on vereinbarung_report_parameter
    for each row
    execute procedure vereinbarung_report_parameter_audit();

create trigger vereinbarung_report_parameter_update
    after update
    on vereinbarung_report_parameter
    for each row
    execute procedure vereinbarung_report_parameter_audit();

create trigger vereinbarung_report_parameter_delete
    after delete
    on vereinbarung_report_parameter
    for each row
    execute procedure vereinbarung_report_parameter_audit();
