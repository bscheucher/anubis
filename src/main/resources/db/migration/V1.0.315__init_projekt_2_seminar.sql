CREATE TABLE IF NOT EXISTS projekt_2_manager (
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    projekt     INTEGER REFERENCES projekt(id),
    manager     INTEGER REFERENCES benutzer(id),
    start_date  DATE,
    end_date    DATE,
    created_on  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS projekt_2_manager_history (
    id                       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    projekt_2_manager_id     INTEGER NOT NULL,
    projekt                  INTEGER,
    manager                  INTEGER,
    start_date               DATE,
    end_date                 DATE,
    created_on               TIMESTAMP,
    action                   CHAR(1),
    action_timestamp         TIMESTAMP NOT NULL
);

CREATE OR REPLACE FUNCTION projekt_2_manager_audit() RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO projekt_2_manager_history (
            projekt_2_manager_id, projekt, manager, start_date, end_date, created_on,
            action, action_timestamp)
        VALUES (
            OLD.id, OLD.projekt, OLD.manager, OLD.start_date, OLD.end_date, OLD.created_on,
            'D', NOW());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO projekt_2_manager_history (
            projekt_2_manager_id, projekt, manager, start_date, end_date, created_on,
            action, action_timestamp)
        VALUES (
            NEW.id, NEW.projekt, NEW.manager, NEW.start_date, NEW.end_date, NEW.created_on,
            'U', NOW());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO projekt_2_manager_history (
            projekt_2_manager_id, projekt, manager, start_date, end_date, created_on,
            action, action_timestamp)
        VALUES (
            NEW.id, NEW.projekt, NEW.manager, NEW.start_date, NEW.end_date, NEW.created_on,
            'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

CREATE TRIGGER projekt_2_manager_insert
AFTER INSERT ON projekt_2_manager
FOR EACH ROW
EXECUTE FUNCTION projekt_2_manager_audit();

CREATE TRIGGER projekt_2_manager_update
AFTER UPDATE ON projekt_2_manager
FOR EACH ROW
EXECUTE FUNCTION projekt_2_manager_audit();

CREATE TRIGGER projekt_2_manager_delete
AFTER DELETE ON projekt_2_manager
FOR EACH ROW
EXECUTE FUNCTION projekt_2_manager_audit();

ALTER FUNCTION projekt_2_manager_audit() OWNER TO ibosng;