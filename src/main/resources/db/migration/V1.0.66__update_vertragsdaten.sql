-- Remove the strasse and land columns
ALTER TABLE vertragsdaten
DROP COLUMN strasse,
DROP COLUMN land;

-- Add the adresse column referencing the adresse table
ALTER TABLE vertragsdaten
    ADD COLUMN adresse integer REFERENCES adresse;


ALTER TABLE vertragsdaten_history
DROP COLUMN strasse,
DROP COLUMN land;

-- Add the adresse column referencing the adresse table
ALTER TABLE vertragsdaten_history
    ADD COLUMN adresse integer;


CREATE OR REPLACE FUNCTION vertragsdaten_audit()
RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           job_bezeichnung, notiz_allgemein, mobile_working,
                                           weitere_adresse_zu_hauptwohnsitz, adresse, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.eintritt, OLD.is_befristet, OLD.befristung_bis,
                OLD.dienstort, OLD.kostenstelle, OLD.fuerhrungskraft, OLD.startcoach, OLD.kategorie,
                OLD.taetigkeit, OLD.job_bezeichnung, OLD.notiz_allgemein, OLD.mobile_working,
                OLD.weitere_adresse_zu_hauptwohnsitz, OLD.adresse, OLD.status, OLD.created_on,
                OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', CURRENT_TIMESTAMP);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           job_bezeichnung, notiz_allgemein, mobile_working,
                                           weitere_adresse_zu_hauptwohnsitz, adresse, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.is_befristet, NEW.befristung_bis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.job_bezeichnung, NEW.notiz_allgemein, NEW.mobile_working,
                NEW.weitere_adresse_zu_hauptwohnsitz, NEW.adresse, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'U', CURRENT_TIMESTAMP);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           job_bezeichnung, notiz_allgemein, mobile_working,
                                           weitere_adresse_zu_hauptwohnsitz, adresse, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.is_befristet, NEW.befristung_bis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.job_bezeichnung, NEW.notiz_allgemein, NEW.mobile_working,
                NEW.weitere_adresse_zu_hauptwohnsitz, NEW.adresse, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'I', CURRENT_TIMESTAMP);
RETURN NEW;
END IF;
RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

ALTER FUNCTION vertragsdaten_audit() OWNER TO ibosng;


ALTER TABLE stammdaten RENAME COLUMN arbeitsgenehmigungDok TO arbeitsgenehmigung;
ALTER TABLE stammdaten_history RENAME COLUMN arbeitsgenehmigungDok TO arbeitsgenehmigung;




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
                                        arbeitsgenehmigung_file_name, arbeitsgenehmigung_status, gueltigBis, arbeitsgenehmigung, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp, ecard_file_name)
        VALUES (OLD.id, OLD.personalnummer, OLD.anrede, OLD.titel, OLD.titel2, OLD.nachname, OLD.vorname,
                OLD.geburtsname, OLD.svnr, OLD.ecard_status, OLD.geschlecht, OLD.familienstand,
                OLD.geburtsdatum, OLD.lebensalter, OLD.staatsbuergerschaft, OLD.muttersprache,
                OLD.adresse, OLD.email, OLD.mobilnummer, OLD.handy_signatur, OLD.bank,
                OLD.arbeitsgenehmigung_file_name, OLD.arbeitsgenehmigung_status, OLD.gueltigBis, OLD.arbeitsgenehmigung, OLD.foto,
                OLD.status, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', now(), OLD.ecard_file_name);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard_status, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        arbeitsgenehmigung_file_name, arbeitsgenehmigung_status, gueltigBis, arbeitsgenehmigung, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp, ecard_file_name)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard_status, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.handy_signatur, NEW.bank,
                NEW.arbeitsgenehmigung_file_name, NEW.arbeitsgenehmigung_status, NEW.gueltigBis, NEW.arbeitsgenehmigung, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, now(), NEW.changed_by,
                'U', now(), NEW.ecard_file_name);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard_status, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        arbeitsgenehmigung_file_name, arbeitsgenehmigung_status, gueltigBis, arbeitsgenehmigung, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp, ecard_file_name)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard_status, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.handy_signatur, NEW.bank,
                NEW.arbeitsgenehmigung_file_name, NEW.arbeitsgenehmigung_status, NEW.gueltigBis, NEW.arbeitsgenehmigung, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, NULL, NULL, 'I', now(), NEW.ecard_file_name);
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function stammdaten_audit() owner to ibosng;