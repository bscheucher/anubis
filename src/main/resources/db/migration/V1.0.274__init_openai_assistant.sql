CREATE TABLE IF NOT EXISTS openai_assistant
(
    id             INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    assistant_name TEXT                                NOT NULL,
    assistant_id   TEXT                                NOT NULL,
    created_on     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by     TEXT                                NOT NULL,
    changed_on     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    changed_by     TEXT
);

ALTER TABLE openai_assistant
    OWNER TO ibosng;


CREATE TABLE IF NOT EXISTS openai_assistant_history
(
    id                  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    openai_assistant_id INTEGER                             NOT NULL, -- Links to the original openai_assistant record
    assistant_name      TEXT                                NOT NULL,
    assistant_id        TEXT                                NOT NULL,
    created_on          TIMESTAMP,
    created_by          TEXT,
    changed_on          TIMESTAMP,
    changed_by          TEXT,
    action              CHAR CHECK (action IN ('I', 'U', 'D')),       -- Insert, Update, Delete
    action_timestamp    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE openai_assistant_history
    OWNER TO ibosng;


create or replace function openai_assistant_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO openai_assistant_history (openai_assistant_id, assistant_name, assistant_id, created_on, created_by,
                                              changed_on, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.assistant_name, OLD.assistant_id, OLD.created_on, OLD.created_by, OLD.changed_on,
                OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO openai_assistant_history (openai_assistant_id, assistant_name, assistant_id, created_on, created_by,
                                              changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.assistant_name, NEW.assistant_id, NEW.created_on, NEW.created_by, NOW(), NEW.changed_by,
                'U', NOW());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO openai_assistant_history (openai_assistant_id, assistant_name, assistant_id, created_on, created_by,
                                              changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.assistant_name, NEW.assistant_id, NEW.created_on, NEW.created_by, NEW.changed_on,
                NEW.changed_by, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

ALTER FUNCTION openai_assistant_audit() OWNER TO ibosng;


CREATE TRIGGER openai_assistant_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON openai_assistant
    FOR EACH ROW
EXECUTE FUNCTION openai_assistant_audit();