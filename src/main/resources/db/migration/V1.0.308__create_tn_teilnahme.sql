CREATE TABLE IF NOT EXISTS teilnahme_reason (
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kuerzel             TEXT,
    bezeichnung         TEXT,
    an_abwesenheit      boolean,
    created_on          TIMESTAMP,
    created_by          VARCHAR(255),
    changed_on          TIMESTAMP,
    changed_by          VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS teilnahme_reason_history (
    id                     INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    teilnahme_reason_id    INTEGER NOT NULL,
    kuerzel                TEXT,
    bezeichnung            TEXT,
    an_abwesenheit         BOOLEAN,
    created_on             TIMESTAMP,
    created_by             VARCHAR(255),
    changed_on             TIMESTAMP,
    changed_by             VARCHAR(255),
    action                 CHAR(1) NOT NULL,
    action_timestamp       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE OR REPLACE FUNCTION teilnahme_reason_audit() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO teilnahme_reason_history (
            teilnahme_reason_id, kuerzel, bezeichnung, an_abwesenheit,
            created_on, created_by, changed_on, changed_by,
            action, action_timestamp)
        VALUES (
            OLD.id, OLD.kuerzel, OLD.bezeichnung, OLD.an_abwesenheit,
            OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by,
            'D', clock_timestamp());
        RETURN OLD;

    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO teilnahme_reason_history (
            teilnahme_reason_id, kuerzel, bezeichnung, an_abwesenheit,
            created_on, created_by, changed_on, changed_by,
            action, action_timestamp)
        VALUES (
            NEW.id, NEW.kuerzel, NEW.bezeichnung, NEW.an_abwesenheit,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by,
            'U', clock_timestamp());
        RETURN NEW;

    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO teilnahme_reason_history (
            teilnahme_reason_id, kuerzel, bezeichnung, an_abwesenheit,
            created_on, created_by, changed_on, changed_by,
            action, action_timestamp)
        VALUES (
            NEW.id, NEW.kuerzel, NEW.bezeichnung, NEW.an_abwesenheit,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by,
            'I', clock_timestamp());
        RETURN NEW;
    END IF;

    RETURN NULL;
END;
$$;

CREATE TRIGGER teilnahme_reason_insert
AFTER INSERT ON teilnahme_reason
FOR EACH ROW EXECUTE FUNCTION teilnahme_reason_audit();

CREATE TRIGGER teilnahme_reason_update
AFTER UPDATE ON teilnahme_reason
FOR EACH ROW EXECUTE FUNCTION teilnahme_reason_audit();

CREATE TRIGGER teilnahme_reason_delete
AFTER DELETE ON teilnahme_reason
FOR EACH ROW EXECUTE FUNCTION teilnahme_reason_audit();

INSERT INTO teilnahme_reason (kuerzel, bezeichnung, an_abwesenheit, created_on, created_by)
VALUES
('X',   'Anwesend',                    TRUE,   CURRENT_TIMESTAMP, current_user),
('Ausl','Auslandsaufenthalt',          FALSE,  CURRENT_TIMESTAMP, current_user),
('B',   'Berufsschulbesuch',           FALSE,  CURRENT_TIMESTAMP, current_user),
('E',   'Entschuldigt',                FALSE,  CURRENT_TIMESTAMP, current_user),
('F',   'Frei',                        FALSE,  CURRENT_TIMESTAMP, current_user),
('FE',  'Ferien',                      FALSE,  CURRENT_TIMESTAMP, current_user),
('G',   'Gruppentag',                  FALSE,  CURRENT_TIMESTAMP, current_user),
('K',   'Krank ohne Beleg',            FALSE,  CURRENT_TIMESTAMP, current_user),
('KSB', 'Krankenstand mit Bestätigung',FALSE,  CURRENT_TIMESTAMP, current_user),
('NE',  'Nicht Entschuldigt',          FALSE,  CURRENT_TIMESTAMP, current_user),
('PF',  'Pflegefreistellung',          FALSE,  CURRENT_TIMESTAMP, current_user),
('P',   'Praktikum',                   FALSE,  CURRENT_TIMESTAMP, current_user),
('PD',  'Präsenzdienstübung',          FALSE,  CURRENT_TIMESTAMP, current_user),
('S',   'Schnuppern',                  FALSE,  CURRENT_TIMESTAMP, current_user),
('SO',  'Selbstorganisation',          FALSE,  CURRENT_TIMESTAMP, current_user),
('U',   'Urlaub',                      FALSE,  CURRENT_TIMESTAMP, current_user),
('V',   'Vorstellungstermin',          FALSE,  CURRENT_TIMESTAMP, current_user),
(null,  'Wochenende, Feiertag',        FALSE,  CURRENT_TIMESTAMP, current_user),
('ZA',  'Zeitausgleich',               FALSE,  CURRENT_TIMESTAMP, current_user),
('?',   'Offen',                       FALSE,  CURRENT_TIMESTAMP, current_user);

CREATE TABLE IF NOT EXISTS tn_teilnahme (
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    datum               DATE,
    status              smallint,
    seminar             INTEGER REFERENCES seminar(id),
    anwesend            INTEGER,
    nicht_entschuldigt  INTEGER,
    sonstige            INTEGER,
    created_on          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(255),
    changed_on          TIMESTAMP,
    changed_by          VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS tn_teilnahme_history (
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tn_teilnahme_id     INTEGER NOT NULL,
    datum               DATE,
    status              smallint,
    seminar             INTEGER,
    anwesend            INTEGER,
    nicht_entschuldigt  INTEGER,
    sonstige            INTEGER,
    created_on          TIMESTAMP,
    created_by          VARCHAR(255),
    changed_on          TIMESTAMP,
    changed_by          VARCHAR(255),
    action              CHAR(1) NOT NULL,
    action_timestamp    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE OR REPLACE FUNCTION tn_teilnahme_audit() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO tn_teilnahme_history (
            tn_teilnahme_id, datum, status, seminar, anwesend, nicht_entschuldigt, sonstige,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
            OLD.id, OLD.datum, OLD.status, OLD.seminar, OLD.anwesend, OLD.nicht_entschuldigt, OLD.sonstige,
            OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', clock_timestamp());
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO tn_teilnahme_history (
            tn_teilnahme_id, datum, status, seminar, anwesend, nicht_entschuldigt, sonstige,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
            NEW.id, NEW.datum, NEW.status, NEW.seminar, NEW.anwesend, NEW.nicht_entschuldigt, NEW.sonstige,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', clock_timestamp());
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO tn_teilnahme_history (
            tn_teilnahme_id, datum, status, seminar, anwesend, nicht_entschuldigt, sonstige,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
            NEW.id, NEW.datum, NEW.status, NEW.seminar, NEW.anwesend, NEW.nicht_entschuldigt, NEW.sonstige,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', clock_timestamp());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

CREATE TRIGGER tn_teilnahme_insert
AFTER INSERT ON tn_teilnahme
FOR EACH ROW EXECUTE FUNCTION tn_teilnahme_audit();

CREATE TRIGGER tn_teilnahme_update
AFTER UPDATE ON tn_teilnahme
FOR EACH ROW EXECUTE FUNCTION tn_teilnahme_audit();

CREATE TRIGGER tn_teilnahme_delete
AFTER DELETE ON tn_teilnahme
FOR EACH ROW EXECUTE FUNCTION tn_teilnahme_audit();


CREATE TABLE IF NOT EXISTS tn_teilnahme_status (
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    teilnehmer          INTEGER REFERENCES teilnehmer(id),
    teilnahme           INTEGER REFERENCES tn_teilnahme(id),
    tn_reason           INTEGER REFERENCES teilnahme_reason(id),
    info                TEXT,
    status              smallint,
    created_on          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(255),
    changed_on          TIMESTAMP,
    changed_by          VARCHAR(255)
    );


CREATE TABLE IF NOT EXISTS tn_teilnahme_status_history (
    id                      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tn_teilnahme_status_id  INTEGER NOT NULL,
    teilnehmer              INTEGER,
    teilnahme               INTEGER,
    tn_reason               INTEGER,
    info                    TEXT,
    status                  smallint,
    created_on              TIMESTAMP,
    created_by              VARCHAR(255),
    changed_on              TIMESTAMP,
    changed_by              VARCHAR(255),
    action                  CHAR(1) NOT NULL,
    action_timestamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE OR REPLACE FUNCTION tn_teilnahme_status_audit() RETURNS TRIGGER
LANGUAGE plpgsql AS
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO tn_teilnahme_status_history (
            tn_teilnahme_status_id, teilnehmer, teilnahme, tn_reason, info, status,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
            OLD.id, OLD.teilnehmer, OLD.teilnahme, OLD.tn_reason, OLD.info, OLD.status,
            OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', clock_timestamp());
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO tn_teilnahme_status_history (
            tn_teilnahme_status_id, teilnehmer, teilnahme, tn_reason, info, status,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
            NEW.id, NEW.teilnehmer, NEW.teilnahme, NEW.tn_reason, NEW.info, NEW.status,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', clock_timestamp());
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO tn_teilnahme_status_history (
            tn_teilnahme_status_id, teilnehmer, teilnahme, tn_reason, info, status,
            created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (
            NEW.id, NEW.teilnehmer, NEW.teilnahme, NEW.tn_reason, NEW.info, NEW.status,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', clock_timestamp());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

CREATE TRIGGER tn_teilnahme_status_insert
AFTER INSERT ON tn_teilnahme_status
FOR EACH ROW EXECUTE FUNCTION tn_teilnahme_status_audit();

CREATE TRIGGER tn_teilnahme_status_update
AFTER UPDATE ON tn_teilnahme_status
FOR EACH ROW EXECUTE FUNCTION tn_teilnahme_status_audit();

CREATE TRIGGER tn_teilnahme_status_delete
AFTER DELETE ON tn_teilnahme_status
FOR EACH ROW EXECUTE FUNCTION tn_teilnahme_status_audit();
