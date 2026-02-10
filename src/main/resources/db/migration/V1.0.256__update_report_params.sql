-- Add default_value column to report_parameter table
ALTER TABLE report_parameter ADD COLUMN default_value text;

-- Add default_value column to report_parameter_history table
ALTER TABLE report_parameter_history ADD COLUMN default_value text;

-- Update the audit function to include default_value
CREATE OR REPLACE FUNCTION report_parameter_audit() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO report_parameter_history (report_parameter_id, name, type, description, report_id, required, label, default_value, action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.type, OLD.description, OLD.report_id, OLD.required, OLD.label, OLD.default_value, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO report_parameter_history (report_parameter_id, name, type, description, report_id, required, label, default_value, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.type, NEW.description, NEW.report_id, NEW.required, NEW.label, NEW.default_value, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO report_parameter_history (report_parameter_id, name, type, description, report_id, required, label, default_value, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.type, NEW.description, NEW.report_id, NEW.required, NEW.label, NEW.default_value, 'I', now());
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

-- Set the owner for the updated function
ALTER FUNCTION report_parameter_audit() OWNER TO ibosng;
