ALTER TABLE land
    ADD COLUMN is_in_eu_eea_ch BOOLEAN DEFAULT FALSE;

ALTER TABLE land_history
    ADD COLUMN is_in_eu_eea_ch BOOLEAN DEFAULT FALSE;


create or replace function land_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, telefonvorwahl, is_in_eu_eea_ch,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (OLD.id, OLD.land_name, OLD.land_code, OLD.elda_code, OLD.telefonvorwahl, OLD.is_in_eu_eea_ch,
                OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, telefonvorwahl, is_in_eu_eea_ch,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.telefonvorwahl, NEW.is_in_eu_eea_ch,
                NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, telefonvorwahl, is_in_eu_eea_ch,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.telefonvorwahl, NEW.is_in_eu_eea_ch, NEW.created_on, NEW.created_by,
                NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function land_audit() owner to ibosng;


UPDATE land
SET is_in_eu_eea_ch = TRUE
WHERE land_code IN ('AT', 'BE', 'BG', 'HR', 'CY', 'CZ', 'DK', 'EE', 'FI', 'FR', 'DE', 'GR', 'HU', 'IE', 'IT', 'LV', 'LT', 'LU', 'MT', 'NL', 'PL', 'PT', 'RO', 'SK', 'SI', 'ES', 'SE', -- EU countries
                    'IS', 'LI', 'NO', -- EEA countries not in the EU
                    'CH'); -- Switzerland


