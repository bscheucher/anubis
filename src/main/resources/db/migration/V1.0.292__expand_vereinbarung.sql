ALTER TABLE vereinbarung
    ADD COLUMN IF NOT EXISTS vereinbarung_file TEXT;

ALTER TABLE vereinbarung_history
    ADD COLUMN IF NOT EXISTS vereinbarung_file TEXT;

ALTER TABLE vereinbarung
    ADD COLUMN IF NOT EXISTS wworkflow_id INTEGER references w_workflows;

ALTER TABLE vereinbarung_history
    ADD COLUMN IF NOT EXISTS wworkflow_id TEXT;

CREATE OR REPLACE FUNCTION vereinbarung_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vereinbarung_history (
            vereinbarung_id, vereinbarung_name, personalnummer, fuehrungskraft, status, firma, gueltig_ab,
            created_on, created_by, changed_on, changed_by, vereinbarung_file, wworkflow_id,
            action, action_timestamp
        )
        VALUES (
            OLD.id, OLD.vereinbarung_name, OLD.personalnummer, OLD.fuehrungskraft, OLD.status, OLD.firma, OLD.gueltig_ab,
            OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, OLD.vereinbarung_file, OLD.wworkflow_id,
            'D', NOW()
        );
RETURN OLD;

ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vereinbarung_history (
            vereinbarung_id, vereinbarung_name, personalnummer, fuehrungskraft, status, firma, gueltig_ab,
            created_on, created_by, changed_on, changed_by, vereinbarung_file, wworkflow_id,
            action, action_timestamp
        )
        VALUES (
            NEW.id, NEW.vereinbarung_name, NEW.personalnummer, NEW.fuehrungskraft, NEW.status, NEW.firma, NEW.gueltig_ab,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.vereinbarung_file, NEW.wworkflow_id,
            'U', NOW()
        );
RETURN NEW;

ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vereinbarung_history (
            vereinbarung_id, vereinbarung_name, personalnummer, fuehrungskraft, status, firma, gueltig_ab,
            created_on, created_by, changed_on, changed_by, vereinbarung_file, wworkflow_id,
            action, action_timestamp
        )
        VALUES (
            NEW.id, NEW.vereinbarung_name, NEW.personalnummer, NEW.fuehrungskraft, NEW.status, NEW.firma, NEW.gueltig_ab,
            NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.vereinbarung_file, NEW.wworkflow_id,
            'I', NOW()
        );
RETURN NEW;
END IF;

RETURN NULL;
END;
$$;

ALTER FUNCTION vereinbarung_audit() OWNER TO ibosng;