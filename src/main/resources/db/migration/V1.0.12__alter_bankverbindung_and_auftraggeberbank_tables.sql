ALTER TABLE auftraggeber_bank DROP CONSTRAINT uk_bankleitzahl;
ALTER TABLE auftraggeber_bank drop column bankleitzahl;
ALTER TABLE bankverbindung ADD COLUMN bankleitzahl text;

ALTER TABLE auftraggeber_bank_history DROP COLUMN bankleitzahl;
ALTER TABLE bankverbindung_history ADD COLUMN bankleitzahl text;

create or replace function auftraggeber_bank_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO auftraggeber_bank_history (auftraggeber_bank_id, firmenbank_name, firmenbank_nummer,
                                                zahlungsart, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.firmenbank_name, OLD.firmenbank_nummer, OLD.zahlungsart,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO auftraggeber_bank_history (auftraggeber_bank_id, firmenbank_name, firmenbank_nummer,
                                                zahlungsart, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.firmenbank_name, NEW.firmenbank_nummer, NEW.zahlungsart,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO auftraggeber_bank_history (auftraggeber_bank_id, firmenbank_name, firmenbank_nummer,
                                                zahlungsart, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.firmenbank_name, NEW.firmenbank_nummer, NEW.zahlungsart,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

create or replace function bankverbindung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO bankverbindung_history (bankverbindung_id, bankbezeichnung, bar_bei_austritt, iban_or_kto_nummer,
                                            bankleitzahl, land, lautend_auf, auftraggeber_bank, created_on,
                                            created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.bankbezeichnung, OLD.bar_bei_austritt, OLD.iban_or_kto_nummer, OLD.bankleitzahl, OLD.land,
                OLD.lautend_auf, OLD.auftraggeber_bank, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO bankverbindung_history (bankverbindung_id, bankbezeichnung, bar_bei_austritt, iban_or_kto_nummer,
                                            bankleitzahl, land, lautend_auf, auftraggeber_bank, created_on,
                                            created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bankbezeichnung, NEW.bar_bei_austritt, NEW.iban_or_kto_nummer, NEW.bankleitzahl, NEW.land,
                NEW.lautend_auf, NEW.auftraggeber_bank, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO bankverbindung_history (bankverbindung_id, bankbezeichnung, bar_bei_austritt, iban_or_kto_nummer,
                                            bankleitzahl, land, lautend_auf, auftraggeber_bank, created_on,
                                            created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bankbezeichnung, NEW.bar_bei_austritt, NEW.iban_or_kto_nummer, NEW.bankleitzahl, NEW.land,
                NEW.lautend_auf, NEW.auftraggeber_bank, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;
