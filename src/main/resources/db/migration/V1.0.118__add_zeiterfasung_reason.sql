create table if not exists zeiterfassung_reason
(
    id                  integer generated always as identity primary key,
    bezeichnung         text,
    short_bezeichnung   text,
    created_on          timestamp default CURRENT_TIMESTAMP not null,
    created_by          text not null,
    changed_by          text
);

create table if not exists zeiterfassung_reason_history
(
    id                  integer generated always as identity primary key,
    zeiterfassung_reason_id    integer,
    bezeichnung         text,
    short_bezeichnung   text,
    created_on          timestamp default CURRENT_TIMESTAMP,
    created_by          text,
    changed_by          text,
    action              char,
    action_timestamp    timestamp not null
);

alter table zeiterfassung_reason owner to ibosng;
alter table zeiterfassung_reason_history owner to ibosng;

create or replace function zeiterfassung_reason_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeiterfassung_reason_history (zeiterfassung_reason_id, bezeichnung, short_bezeichnung, created_on, created_by, changed_by, action, action_timestamp )
        VALUES (OLD.id, OLD.bezeichnung, OLD.short_bezeichnung, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeiterfassung_reason_history (zeiterfassung_reason_id, bezeichnung, short_bezeichnung, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bezeichnung, NEW.short_bezeichnung, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeiterfassung_reason_history ( zeiterfassung_reason_id, bezeichnung, short_bezeichnung, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bezeichnung, NEW.short_bezeichnung, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function zeiterfassung_reason_audit() owner to ibosng;

INSERT INTO zeiterfassung_reason(short_bezeichnung, bezeichnung, created_by) VALUES
('U', 'Urlaub', 'DB-service'),
('X', 'Anwesend', 'DB-service'),
('E', 'Entschuldigt', 'DB-service'),
('NE', 'Nicht entschuldigt', 'DB-service'),
('KSB', 'Krankenstand mit Bestätigung', 'DB-service'),
('PF', 'Pflegefreistellung', 'DB-service'),
('P', 'Praktikum', 'DB-service'),
('B', 'Berufsschule', 'DB-service'),
('X', 'Arbeitsaufnahme', 'DB-service'),
('X', 'Kursabbruch', 'DB-service'),
('X', 'Ausschluss', 'DB-service'),
('X', 'Kursende', 'DB-service'),
('NE', 'Arbeitsaufnahme', 'DB-service'),
('NE', 'Kursabbruch', 'DB-service'),
('NE', 'Ausschluss', 'DB-service'),
('NE', 'Kursende', 'DB-service');