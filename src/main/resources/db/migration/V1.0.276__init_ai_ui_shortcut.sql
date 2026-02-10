CREATE TABLE IF NOT EXISTS ai_ui_shortcut
(
    id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    shortcut   TEXT                                NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by TEXT                                NOT NULL,
    changed_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    changed_by TEXT
);

ALTER TABLE ai_ui_shortcut
    OWNER TO ibosng;


CREATE TABLE IF NOT EXISTS ai_ui_shortcut_history
(
    id                INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ai_ui_shortcut_id INTEGER                             NOT NULL, -- Links to the original ai_ui_shortcut record
    shortcut          TEXT                                NOT NULL,
    created_on        TIMESTAMP,
    created_by        TEXT,
    changed_on        TIMESTAMP,
    changed_by        TEXT,
    action            CHAR CHECK (action IN ('I', 'U', 'D')),       -- Insert, Update, Delete
    action_timestamp  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE ai_ui_shortcut_history
    OWNER TO ibosng;


create or replace function ai_ui_shortcut_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ai_ui_shortcut_history (ai_ui_shortcut_id, shortcut, created_on, created_by,
                                            changed_on, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.shortcut, OLD.created_on, OLD.created_by, OLD.changed_on,
                OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO ai_ui_shortcut_history (ai_ui_shortcut_id, shortcut, created_on, created_by,
                                            changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.shortcut, NEW.created_on, NEW.created_by, NOW(), NEW.changed_by,
                'U', NOW());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO ai_ui_shortcut_history (ai_ui_shortcut_id, shortcut, created_on, created_by,
                                            changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.shortcut, NEW.created_on, NEW.created_by, NEW.changed_on,
                NEW.changed_by, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

ALTER FUNCTION ai_ui_shortcut_audit() OWNER TO ibosng;


CREATE TRIGGER ai_ui_shortcut_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON ai_ui_shortcut
    FOR EACH ROW
EXECUTE FUNCTION ai_ui_shortcut_audit();

insert into ai_ui_shortcut(shortcut, created_by)
VALUES ('Wie läuft mein erster Arbeitstag ab?', current_user),
       ('Welche Zugänge benötige ich und wie erhalte ich sie?', current_user),
       ('Wo finde ich Informationen zu meinem Dienstvertrag?', current_user),
       ('Welche Ansprechpersonen sind für mich zuständig?', current_user);
