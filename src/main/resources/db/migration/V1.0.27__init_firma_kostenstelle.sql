ALTER TABLE ibis_firma
    ALTER COLUMN bmd_client TYPE INTEGER USING bmd_client::INTEGER;


ALTER TABLE ibis_firma_history
    ALTER COLUMN bmd_client TYPE INTEGER USING bmd_client::INTEGER;


INSERT INTO ibis_firma (bmd_client, name, created_by)
VALUES (1, 'ibis acam Bildungs GmbH', current_user),
       (2, 'ibis acam gemeinnützige Bildungs GmbH', current_user),
       (3, 'KAOS gemeinnützige Bildungsservice GmbH', current_user),
       (10, 'Aspire Beteiligungs GmbH', current_user),
       (11, 'Aspire Operations GmbH', current_user),
       (12, 'Aspire Invest Holding GmbH', current_user),
       (13, 'Aspire Education GmbH', current_user),
       (14, 'Aspire Education Group Holding GmbH', current_user);

create table if not exists kostenstelle
(
    id          integer generated always as identity primary key,
    bezeichnung text,
    nummer      integer,
    status      smallint  not null,
    created_on  timestamp not null,
    created_by  text      not null,
    changed_on  timestamp,
    changed_by  text
);

create table if not exists kostenstelle_history
(
    id               integer generated always as identity primary key,
    kostenstelle_id  integer   not null,
    bezeichnung      text,
    nummer           integer,
    status           smallint  not null,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function kostenstelle_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO kostenstelle_history (kostenstelle_id, bezeichnung, nummer, status, created_on, created_by,
                                          changed_by,
                                          action,
                                          action_timestamp)
        VALUES (OLD.id, OLD.bezeichnung, OLD.nummer, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO kostenstelle_history (kostenstelle_id, bezeichnung, nummer, status, created_on, created_by,
                                          changed_by,
                                          action,
                                          action_timestamp)
        VALUES (NEW.id, NEW.bezeichnung, NEW.nummer, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO kostenstelle_history (kostenstelle_id, bezeichnung, nummer, status, created_on, created_by,
                                          changed_by,
                                          action,
                                          action_timestamp)
        VALUES (NEW.id, NEW.bezeichnung, NEW.nummer, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function kostenstelle_audit() owner to ibosng;

create trigger kostenstelle_insert
    after insert
    on kostenstelle
    for each row
execute procedure kostenstelle_audit();

create trigger kostenstelle_update
    after update
    on kostenstelle
    for each row
execute procedure kostenstelle_audit();

create trigger kostenstelle_delete
    after delete
    on kostenstelle
    for each row
execute procedure kostenstelle_audit();


INSERT INTO kostenstelle (nummer, bezeichnung, status, created_on, created_by)
VALUES (10, 'People', 1, CURRENT_TIMESTAMP, current_user),
       (11, 'Vorarlberg', 1, CURRENT_TIMESTAMP, current_user),
       (12, 'Tirol', 1, CURRENT_TIMESTAMP, current_user),
       (13, 'Salzburg', 1, CURRENT_TIMESTAMP, current_user),
       (14, 'Kärnten', 1, CURRENT_TIMESTAMP, current_user),
       (15, 'Steiermark', 1, CURRENT_TIMESTAMP, current_user),
       (16, 'Burgenland', 1, CURRENT_TIMESTAMP, current_user),
       (17, 'Oberösterreich', 1, CURRENT_TIMESTAMP, current_user),
       (18, 'Niederösterreich', 1, CURRENT_TIMESTAMP, current_user),
       (19, 'Wien-allgemein', 1, CURRENT_TIMESTAMP, current_user),
       (20, 'Aqua', 1, CURRENT_TIMESTAMP, current_user),
       (22, 'Service Center', 1, CURRENT_TIMESTAMP, current_user),
       (23, 'Online Marketing', 1, CURRENT_TIMESTAMP, current_user),
       (24, 'worklifelearn.com', 1, CURRENT_TIMESTAMP, current_user),
       (25, 'aspidoo', 1, CURRENT_TIMESTAMP, current_user),
       (26, 'Content Team', 1, CURRENT_TIMESTAMP, current_user),
       (28, 'Wien-Jugend', 1, CURRENT_TIMESTAMP, current_user),
       (29, 'Wien-Deutsch', 1, CURRENT_TIMESTAMP, current_user),
       (32, 'NN 1', 1, CURRENT_TIMESTAMP, current_user),
       (40, 'Jugend', 1, CURRENT_TIMESTAMP, current_user),
       (41, 'Sprachen&Integration', 1, CURRENT_TIMESTAMP, current_user),
       (42, 'Erwachsene', 1, CURRENT_TIMESTAMP, current_user),
       (43, 'Frauen', 1, CURRENT_TIMESTAMP, current_user),
       (44, 'Talent Link', 1, CURRENT_TIMESTAMP, current_user),
       (45, 'Beratung & Coaching', 1, CURRENT_TIMESTAMP, current_user),
       (46, 'Unternehmensberatung', 1, CURRENT_TIMESTAMP, current_user),
       (47, NULL, 1, CURRENT_TIMESTAMP, current_user), -- or use '' for an empty string if preferred
       (80, 'Team Angebotslegung', 1, CURRENT_TIMESTAMP, current_user),
       (81, 'IT Services', 1, CURRENT_TIMESTAMP, current_user),
       (82, 'App Management', 1, CURRENT_TIMESTAMP, current_user),
       (83, 'Standortmanagement', 1, CURRENT_TIMESTAMP, current_user),
       (90, 'Geschäftsführung', 1, CURRENT_TIMESTAMP, current_user),
       (91, 'IDC', 1, CURRENT_TIMESTAMP, current_user),
       (92, 'Betriebsrat', 1, CURRENT_TIMESTAMP, current_user),
       (93, 'Performance & Finance', 1, CURRENT_TIMESTAMP, current_user),
       (94, 'Holding', 1, CURRENT_TIMESTAMP, current_user);