-- Add new column to the report table
ALTER TABLE report
    ADD COLUMN output_format VARCHAR(255);

-- Add new column to the report_history table
ALTER TABLE report_history
    ADD COLUMN output_format VARCHAR(255);

-- Update trigger function to include the new column
CREATE OR REPLACE FUNCTION report_audit() RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, output_format, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (OLD.id, OLD.report_name, OLD.source_path, OLD.main_report_file, OLD.output_format, 'D', now(), OLD.created_on, OLD.created_by, now(), current_user);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, output_format, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, NEW.output_format, 'U', now(), NEW.created_on, NEW.created_by, now(), current_user);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, output_format, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, NEW.output_format, 'I', now(), NEW.created_on, NEW.created_by, NULL, NULL);
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

-- Update function owner
ALTER FUNCTION report_audit() OWNER TO ibosng;