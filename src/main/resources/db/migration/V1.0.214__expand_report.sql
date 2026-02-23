-- Add new audit columns to the report table
ALTER TABLE report
    ADD COLUMN created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ADD COLUMN created_by VARCHAR(255),
    ADD COLUMN changed_on TIMESTAMP WITHOUT TIME ZONE,
    ADD COLUMN changed_by VARCHAR(255);

-- Add new audit columns to the report_history table
ALTER TABLE report_history
    ADD COLUMN created_on TIMESTAMP,
    ADD COLUMN created_by VARCHAR(255),
    ADD COLUMN changed_on TIMESTAMP WITHOUT TIME ZONE,
    ADD COLUMN changed_by VARCHAR(255);


CREATE OR REPLACE FUNCTION report_audit() RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (OLD.id, OLD.report_name, OLD.source_path, OLD.main_report_file, 'D', now(), OLD.created_on, OLD.created_by, now(), current_user);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, 'U', now(), NEW.created_on, NEW.created_by, now(), current_user);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, 'I', now(), NEW.created_on, NEW.created_by, NULL, NULL);
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

-- Update function owner
ALTER FUNCTION report_audit() OWNER TO ibosng;