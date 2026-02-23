-- Step 1: Add the new column
ALTER TABLE mitarbeiter_data_status
    ADD COLUMN unterhaltsberechtigte INTEGER REFERENCES unterhaltsberechtigte (id);

-- Step 2: Drop the existing CHECK constraints
ALTER TABLE mitarbeiter_data_status
DROP CONSTRAINT IF EXISTS mitarbeiter_data_status_check;
ALTER TABLE mitarbeiter_data_status
DROP CONSTRAINT IF EXISTS mitarbeiter_data_status_check1;

-- Step 3: Add the updated CHECK constraints
ALTER TABLE mitarbeiter_data_status
    ADD CONSTRAINT mitarbeiter_data_status_check
        CHECK (stammdaten IS NOT NULL OR vertragsdaten IS NOT NULL OR vordienstzeiten IS NOT NULL OR unterhaltsberechtigte IS NOT NULL);

ALTER TABLE mitarbeiter_data_status
    ADD CONSTRAINT mitarbeiter_data_status_check1
        CHECK (NOT (stammdaten IS NOT NULL AND vertragsdaten IS NOT NULL AND vordienstzeiten IS NOT NULL AND unterhaltsberechtigte IS NOT NULL));


ALTER TABLE mitarbeiter_data_status_history
    ADD COLUMN unterhaltsberechtigte INTEGER;


create or replace function mitarbeiter_data_status_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO mitarbeiter_data_status_history (mitarbeiter_data_status_id, stammdaten, vertragsdaten,
                                                     vordienstzeiten, unterhaltsberechtigte, personalnummer,
                                                     error, cause,
                                                     created_on, created_by, changed_by,
                                                     action,
                                                     action_timestamp)
        VALUES (OLD.id, OLD.stammdaten, OLD.vertragsdaten, OLD.vordienstzeiten, OLD.unterhaltsberechtigte, OLD.personalnummer, OLD.error,
                OLD.cause, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D',
                now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO mitarbeiter_data_status_history (mitarbeiter_data_status_id, stammdaten, vertragsdaten,
                                                     vordienstzeiten, unterhaltsberechtigte, personalnummer,
                                                     error, cause,
                                                     created_on, created_by, changed_by,
                                                     action,
                                                     action_timestamp)
        VALUES (NEW.id, NEW.stammdaten, NEW.vertragsdaten, NEW.vordienstzeiten, NEW.unterhaltsberechtigte, NEW.personalnummer, NEW.error,
                NEW.cause, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U',
                now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO mitarbeiter_data_status_history (mitarbeiter_data_status_id, stammdaten, vertragsdaten,
                                                     vordienstzeiten, unterhaltsberechtigte, personalnummer,
                                                     error, cause,
                                                     created_on, created_by, changed_by,
                                                     action,
                                                     action_timestamp)
        VALUES (NEW.id, NEW.stammdaten, NEW.vertragsdaten, NEW.vordienstzeiten, NEW.unterhaltsberechtigte, NEW.personalnummer, NEW.error,
                NEW.cause, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function mitarbeiter_data_status_audit() owner to ibosng;