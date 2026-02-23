-- Rename blob_name to source_path in the report table
ALTER TABLE report
    RENAME COLUMN blob_name TO source_path;

-- Add the new column main_report_file to the report table
ALTER TABLE report
    ADD COLUMN main_report_file text;

-- Rename blob_name to source_path in the report_history table
ALTER TABLE report_history
    RENAME COLUMN blob_name TO source_path;

-- Add the new column main_report_file to the report_history table
ALTER TABLE report_history
    ADD COLUMN main_report_file text;

-- Update the report_audit function to reflect the changes
CREATE OR REPLACE FUNCTION report_audit() RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, action, action_timestamp)
        VALUES (OLD.id, OLD.report_name, OLD.source_path, OLD.main_report_file, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, action, action_timestamp)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, action, action_timestamp)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, 'I', now());
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

ALTER FUNCTION report_audit() OWNER TO ibosng;

