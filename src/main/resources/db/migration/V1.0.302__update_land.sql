alter table land add column is_in_sepa BOOLEAN DEFAULT FALSE;
alter table land_history add column is_in_sepa text;

create or replace function land_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, lhr_kz, telefonvorwahl, is_in_eu_eea_ch, is_in_sepa,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (OLD.id, OLD.land_name, OLD.land_code, OLD.elda_code, OLD.lhr_kz, OLD.telefonvorwahl, OLD.is_in_eu_eea_ch, OLD.is_in_sepa,
                OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, lhr_kz, telefonvorwahl, is_in_eu_eea_ch, is_in_sepa,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.lhr_kz, NEW.telefonvorwahl, NEW.is_in_eu_eea_ch, NEW.is_in_sepa,
                NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, lhr_kz, telefonvorwahl, is_in_eu_eea_ch, is_in_sepa,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.lhr_kz, NEW.telefonvorwahl, NEW.is_in_eu_eea_ch, NEW.is_in_sepa,
                NEW.created_on, NEW.created_by, NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function land_audit() owner to ibosng;

UPDATE land
SET is_in_sepa = TRUE
WHERE land_code IN ('AT', 'AD', 'BE', 'BG', 'HR', 'CY', 'CZ', 'DK', 'EE', 'FI', 'FR', 'DE', 'GI', 'GR', 'HU', 'IS', 'IE', 'IT', 'LV', 'LI', 'LT', 'LU', 'MT', 'MC', 'NL', 'NO', 'PL', 'PT', 'RO', 'SM', 'SK', 'SI', 'ES', 'SE', 'CH', 'GB');


