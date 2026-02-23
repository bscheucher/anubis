alter table bank_daten add column blz TEXT;
alter table bank_daten_history add column blz TEXT;

alter table bank_daten add column land integer references land(id);
alter table bank_daten_history add column land integer;

alter table bank_daten drop column card_file_name;
alter table bank_daten_history drop column card_file_name;

create or replace function bank_daten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, blz, land, card_status, status, created_on, created_by,
                                        changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.bank, OLD.iban, OLD.bic, OLD.blz, OLD.land, OLD.card_status, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, blz, land, card_status, status, created_on, created_by,
                                        changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bank, NEW.iban, NEW.bic, NEW.blz, NEW.land, NEW.card_status, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, blz, land, card_status, status, created_on, created_by,
                                        changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bank, NEW.iban, NEW.bic, NEW.blz, NEW.land, NEW.card_status, NEW.status, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;