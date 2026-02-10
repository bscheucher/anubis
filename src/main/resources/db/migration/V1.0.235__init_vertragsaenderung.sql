create table if not exists vertragsaenderung
(
    id                   integer generated always as identity primary key,
    personalnummer       int references personalnummer (id),
    antragssteller       int references benutzer (id),
    gueltig_ab           DATE,
    successor            int references vertragsdaten (id),
    predecessor          int references vertragsdaten (id),
    interne_anmerkung    TEXT,
    offizielle_bemerkung TEXT,
    kommentar            TEXT,
    status               smallint                            not null,
    created_on           timestamp default CURRENT_TIMESTAMP not null,
    created_by           text                                not null,
    changed_on           timestamp,
    changed_by           text
);

create table if not exists vertragsaenderung_history
(
    id                   integer generated always as identity primary key,
    vertragsaenderung_id integer   not null,
    personalnummer       int,
    antragssteller       int,
    gueltig_ab           DATE,
    successor            int,
    predecessor          int,
    interne_anmerkung    TEXT,
    offizielle_bemerkung TEXT,
    kommentar            TEXT,
    status               smallint  not null,
    created_on           timestamp not null,
    created_by           text      not null,
    changed_on           timestamp,
    changed_by           text,
    action               char,
    action_timestamp     timestamp not null
);

create or replace function vertragsaenderung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vertragsaenderung_history (vertragsaenderung_id, personalnummer, antragssteller, gueltig_ab,
                                               successor, predecessor, interne_anmerkung, offizielle_bemerkung,
                                               kommentar, status,
                                               created_on, created_by, changed_on, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.antragssteller, OLD.gueltig_ab, OLD.successor, OLD.predecessor,
                OLD.interne_anmerkung, OLD.offizielle_bemerkung, OLD.kommentar, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsaenderung_history (vertragsaenderung_id, personalnummer, antragssteller, gueltig_ab,
                                               successor, predecessor, interne_anmerkung, offizielle_bemerkung,
                                               kommentar, status,
                                               created_on, created_by, changed_on, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.antragssteller, NEW.gueltig_ab, NEW.successor, NEW.predecessor,
                NEW.interne_anmerkung, NEW.offizielle_bemerkung, NEW.kommentar, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsaenderung_history (vertragsaenderung_id, personalnummer, antragssteller, gueltig_ab,
                                               successor, predecessor, interne_anmerkung, offizielle_bemerkung,
                                               kommentar, status,
                                               created_on, created_by, changed_on, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.antragssteller, NEW.gueltig_ab, NEW.successor, NEW.predecessor,
                NEW.interne_anmerkung, NEW.offizielle_bemerkung, NEW.kommentar, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_on, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function vertragsaenderung_audit() owner to ibosng;

create trigger vertragsaenderung_insert
    after insert
    on vertragsaenderung
    for each row
execute procedure vertragsaenderung_audit();

create trigger vertragsaenderung_update
    after update
    on vertragsaenderung
    for each row
execute procedure vertragsaenderung_audit();

create trigger vertragsaenderung_delete
    after delete
    on vertragsaenderung
    for each row
execute procedure vertragsaenderung_audit();
