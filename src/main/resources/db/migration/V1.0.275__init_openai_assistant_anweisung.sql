CREATE TABLE IF NOT EXISTS openai_assistant_anweisung
(
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    openai_assistant_id integer references openai_assistant (id),
    anweisung           TEXT,
    version             INTEGER                             NOT NULL,
    created_on          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by          TEXT                                NOT NULL,
    changed_on          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    changed_by          TEXT
);

ALTER TABLE openai_assistant_anweisung
    OWNER TO ibosng;


CREATE TABLE IF NOT EXISTS openai_assistant_anweisung_history
(
    id                            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    openai_assistant_anweisung_id INTEGER                             NOT NULL, -- Links to the original openai_assistant_anweisung record
    openai_assistant_id           integer references openai_assistant (id),
    anweisung                     TEXT,
    version                       INTEGER                             NOT NULL,
    created_on                    TIMESTAMP,
    created_by                    TEXT,
    changed_on                    TIMESTAMP,
    changed_by                    TEXT,
    action                        CHAR CHECK (action IN ('I', 'U', 'D')),       -- Insert, Update, Delete
    action_timestamp              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE openai_assistant_anweisung_history
    OWNER TO ibosng;


create or replace function openai_assistant_anweisung_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO openai_assistant_anweisung_history (openai_assistant_anweisung_id, openai_assistant_id, anweisung,
                                                        version,
                                                        created_on, created_by,
                                                        changed_on, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.openai_assistant_id, OLD.anweisung, OLD.version, OLD.created_on, OLD.created_by,
                OLD.changed_on,
                OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO openai_assistant_anweisung_history (openai_assistant_anweisung_id, openai_assistant_id, anweisung,
                                                        version,
                                                        created_on, created_by,
                                                        changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.openai_assistant_id, NEW.anweisung, NEW.version, NEW.created_on, NEW.created_by, NOW(),
                NEW.changed_by,
                'U', NOW());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO openai_assistant_anweisung_history (openai_assistant_anweisung_id, openai_assistant_id, anweisung,
                                                        version,
                                                        created_on, created_by,
                                                        changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.openai_assistant_id, NEW.anweisung, NEW.version, NEW.created_on, NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

ALTER FUNCTION openai_assistant_anweisung_audit() OWNER TO ibosng;


CREATE TRIGGER openai_assistant_anweisung_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON openai_assistant_anweisung
    FOR EACH ROW
EXECUTE FUNCTION openai_assistant_anweisung_audit();