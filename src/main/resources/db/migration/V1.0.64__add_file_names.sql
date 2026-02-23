ALTER TABLE bank_daten
    ADD COLUMN card_file_name text;

ALTER TABLE bank_daten
    RENAME COLUMN card TO card_status;

ALTER TABLE bank_daten_history
    RENAME COLUMN card TO card_status;

ALTER TABLE bank_daten_history
    ADD COLUMN card_file_name text;


CREATE OR REPLACE FUNCTION bank_daten_audit() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, card_status, card_file_name, status, created_on, created_by,
                                        changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.bank, OLD.iban, OLD.bic, OLD.card_status, OLD.card_file_name, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, card_status, card_file_name, status, created_on, created_by,
                                        changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bank, NEW.iban, NEW.bic, NEW.card_status, NEW.card_file_name, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, card_status, card_file_name, status, created_on, created_by,
                                        changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bank, NEW.iban, NEW.bic, NEW.card_status, NEW.card_file_name, NEW.status, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

ALTER FUNCTION bank_daten_audit() OWNER TO ibosng;




-- Update Stammdaten Table

ALTER TABLE stammdaten ADD COLUMN arbeitsgenehmigung_file_name text;

ALTER TABLE stammdaten ADD COLUMN ecard_file_name text;

ALTER TABLE stammdaten RENAME COLUMN arbeitsgenehmigung TO arbeitsgenehmigung_status;

ALTER TABLE stammdaten RENAME COLUMN ecard TO ecard_status;

ALTER TABLE stammdaten_history ADD COLUMN arbeitsgenehmigung_file_name text;

ALTER TABLE stammdaten_history ADD COLUMN ecard_file_name text;

ALTER TABLE stammdaten_history RENAME COLUMN arbeitsgenehmigung TO arbeitsgenehmigung_status;

ALTER TABLE stammdaten_history RENAME COLUMN ecard TO ecard_status;



create or replace  function stammdaten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard_status, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        arbeitsgenehmigung_file_name, arbeitsgenehmigung_status, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp, ecard_file_name)
        VALUES (OLD.id, OLD.personalnummer, OLD.anrede, OLD.titel, OLD.titel2, OLD.nachname, OLD.vorname,
                OLD.geburtsname, OLD.svnr, OLD.ecard_status, OLD.geschlecht, OLD.familienstand,
                OLD.geburtsdatum, OLD.lebensalter, OLD.staatsbuergerschaft, OLD.muttersprache,
                OLD.adresse, OLD.email, OLD.mobilnummer, OLD.handy_signatur, OLD.bank,
                OLD.arbeitsgenehmigung_file_name, OLD.arbeitsgenehmigung_status, OLD.gueltigBis, OLD.arbeitsgenehmigungDok, OLD.foto,
                OLD.status, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', now(), OLD.ecard_file_name);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard_status, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        arbeitsgenehmigung_file_name, arbeitsgenehmigung_status, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp, ecard_file_name)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard_status, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.handy_signatur, NEW.bank,
                NEW.arbeitsgenehmigung_file_name, NEW.arbeitsgenehmigung_status, NEW.gueltigBis, NEW.arbeitsgenehmigungDok, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, now(), NEW.changed_by,
                'U', now(), NEW.ecard_file_name);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard_status, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        arbeitsgenehmigung_file_name, arbeitsgenehmigung_status, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp, ecard_file_name)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard_status, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.handy_signatur, NEW.bank,
                NEW.arbeitsgenehmigung_file_name, NEW.arbeitsgenehmigung_status, NEW.gueltigBis, NEW.arbeitsgenehmigungDok, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, NULL, NULL, 'I', now(), NEW.ecard_file_name);
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function stammdaten_audit() owner to ibosng;




-- Update Vordienstzeiten Table

ALTER TABLE vordienstzeiten
    RENAME COLUMN nachweis TO nachweis_status;

ALTER TABLE vordienstzeiten
    ADD COLUMN nachweis_file_name text;

ALTER TABLE vordienstzeiten_history
    RENAME COLUMN nachweis TO nachweis_status;

ALTER TABLE vordienstzeiten_history
    ADD COLUMN nachweis_file_name text;

CREATE OR REPLACE FUNCTION vordienstzeiten_audit() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis_file_name, nachweis_status, status, created_on,
                                             created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.vertragsart, OLD.firma, OLD.von, OLD.bis, OLD.wochenstunden,
                OLD.anrechenbar, OLD.nachweis_file_name, OLD.nachweis_status, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis_file_name, nachweis_status, status,
                                             created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis_file_name, NEW.nachweis_status, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis_file_name, nachweis_status, status,
                                             created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis_file_name, NEW.nachweis_status, NEW.status, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

ALTER FUNCTION vordienstzeiten_audit() OWNER TO ibosng;

