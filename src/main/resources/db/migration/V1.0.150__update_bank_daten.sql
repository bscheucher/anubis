drop table dienstnehmer;
drop table dienstnehmer_history;

drop table beruf;
drop table beruf_history;

drop table bankverbindung;
drop table bankverbindung_history;


alter table bank_daten
    add column lautend_auf text;
alter table bank_daten_history
    add column lautend_auf text;

alter table bank_daten
    add column auftraggeber_bank integer references auftraggeber_bank (id);
alter table bank_daten_history
    add column auftraggeber_bank integer;

alter table bank_daten
    add column bar_bei_austritt boolean;
alter table bank_daten_history
    add column bar_bei_austritt boolean;


create
or replace function bank_daten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF
(TG_OP = 'DELETE') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, blz, land, card_status, lautend_auf, auftraggeber_bank, bar_bei_austritt, status, created_on, created_by,
                                        changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.bank, OLD.iban, OLD.bic, OLD.blz, OLD.land, OLD.card_status, OLD.lautend_auf, OLD.auftraggeber_bank, OLD.bar_bei_austritt, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
RETURN OLD;
ELSIF
(TG_OP = 'UPDATE') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, blz, land, card_status, lautend_auf, auftraggeber_bank, bar_bei_austritt, status, created_on, created_by,
                                        changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bank, NEW.iban, NEW.bic, NEW.blz, NEW.land, NEW.card_status, NEW.lautend_auf, NEW.auftraggeber_bank, NEW.bar_bei_austritt, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
RETURN NEW;
ELSIF
(TG_OP = 'INSERT') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, blz, land, card_status, lautend_auf, auftraggeber_bank, bar_bei_austritt, status, created_on, created_by,
                                        changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bank, NEW.iban, NEW.bic, NEW.blz, NEW.land, NEW.card_status, NEW.lautend_auf, NEW.auftraggeber_bank, NEW.bar_bei_austritt, NEW.status, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function bank_daten_audit() owner to ibosng;

