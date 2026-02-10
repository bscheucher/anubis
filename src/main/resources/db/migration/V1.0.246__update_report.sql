-- Rename column in the report table
ALTER TABLE report
    RENAME COLUMN output_format TO data_source;

-- Rename column in the report_history table
ALTER TABLE report_history
    RENAME COLUMN output_format TO data_source;

-- Update trigger function to use the renamed column
CREATE OR REPLACE FUNCTION report_audit() RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, data_source, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (OLD.id, OLD.report_name, OLD.source_path, OLD.main_report_file, OLD.data_source, 'D', now(), OLD.created_on, OLD.created_by, now(), current_user);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, data_source, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, NEW.data_source, 'U', now(), NEW.created_on, NEW.created_by, now(), current_user);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, data_source, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, NEW.data_source, 'I', now(), NEW.created_on, NEW.created_by, NULL, NULL);
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

-- Update function owner
ALTER FUNCTION report_audit() OWNER TO ibosng;
