-- Add new column to the report table
ALTER TABLE report
    ADD COLUMN report_type VARCHAR(50) DEFAULT 'MANUAL';

-- Add new column to the report_history table
ALTER TABLE report_history
    ADD COLUMN report_type VARCHAR(50);

-- Update DV entry to not show up in the normal Report List
UPDATE report
SET report_type = ''
WHERE report_name = 'Dienstvertrag';

-- Update trigger function to include the new column
CREATE OR REPLACE FUNCTION report_audit() RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, data_source, report_type, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (OLD.id, OLD.report_name, OLD.source_path, OLD.main_report_file, OLD.data_source, OLD.report_type, 'D', now(), OLD.created_on, OLD.created_by, now(), current_user);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, data_source, report_type, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, NEW.data_source, NEW.report_type, 'U', now(), NEW.created_on, NEW.created_by, now(), current_user);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO report_history (report_id, report_name, source_path, main_report_file, data_source, report_type, action, action_timestamp, created_on, created_by, changed_on, changed_by)
        VALUES (NEW.id, NEW.report_name, NEW.source_path, NEW.main_report_file, NEW.data_source, NEW.report_type, 'I', now(), NEW.created_on, NEW.created_by, NULL, NULL);
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

-- Update function owner
ALTER FUNCTION report_audit() OWNER TO ibosng;
