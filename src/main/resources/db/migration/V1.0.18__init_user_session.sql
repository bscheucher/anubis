CREATE TABLE IF NOT EXISTS user_session (
    id              integer generated always as identity primary key,
    token           TEXT,
    active          BOOLEAN,
    benutzer_id     INTEGER REFERENCES benutzer(id),
    created_on      TIMESTAMP,
    invalidated_on  TIMESTAMP
    );

ALTER TABLE user_session OWNER TO ibosng;

CREATE TABLE IF NOT EXISTS user_session_history (
    id               integer generated always as identity primary key,
    user_session_id  INTEGER NOT NULL,
    token            TEXT,
    active           BOOLEAN,
    benutzer_id      INTEGER,
    created_on       TIMESTAMP,
    invalidated_on   TIMESTAMP,
    action           CHAR,
    action_timestamp TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_session_history FOREIGN KEY (user_session_id) REFERENCES user_session(id)
    );

ALTER TABLE user_session_history OWNER TO ibosng;

CREATE OR REPLACE FUNCTION user_session_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO user_session_history (user_session_id, token, active, benutzer_id, created_on, invalidated_on, action, action_timestamp)
        VALUES (OLD.id, OLD.token, OLD.active, OLD.benutzer_id, OLD.created_on, OLD.invalidated_on, 'D', NOW());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO user_session_history (user_session_id, token, active, benutzer_id, created_on, invalidated_on, action, action_timestamp)
        VALUES (NEW.id, NEW.token, NEW.active, NEW.benutzer_id, NEW.created_on, NEW.invalidated_on, 'U', NOW());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO user_session_history (user_session_id, token, active, benutzer_id, created_on, invalidated_on, action, action_timestamp)
        VALUES (NEW.id, NEW.token, NEW.active, NEW.benutzer_id, NEW.created_on, NEW.invalidated_on, 'I', NOW());
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

ALTER FUNCTION user_session_audit() OWNER TO ibosng;

CREATE TRIGGER user_session_insert
    AFTER INSERT
    ON user_session
    FOR EACH ROW
    EXECUTE PROCEDURE user_session_audit();

CREATE TRIGGER user_session_update
    AFTER UPDATE
    ON user_session
    FOR EACH ROW
    EXECUTE PROCEDURE user_session_audit();

CREATE TRIGGER user_session_delete
    AFTER DELETE
    ON user_session
    FOR EACH ROW
    EXECUTE PROCEDURE user_session_audit();
