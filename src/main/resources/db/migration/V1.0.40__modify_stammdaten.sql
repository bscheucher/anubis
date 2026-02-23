alter table stammdaten add column handy_signatur boolean default false;

alter table stammdaten_history add column handy_signatur boolean default false;


create or replace function stammdaten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.anrede, OLD.titel, OLD.titel2, OLD.nachname, OLD.vorname,
                OLD.geburtsname, OLD.svnr, OLD.ecard, OLD.geschlecht, OLD.familienstand,
                OLD.geburtsdatum, OLD.lebensalter, OLD.staatsbuergerschaft, OLD.muttersprache,
                OLD.adresse, OLD.email, OLD.mobilnummer, OLD.handy_signatur, OLD.bank,
                OLD.arbeitsgenehmigung, OLD.gueltigBis, OLD.arbeitsgenehmigungDok, OLD.foto,
                OLD.status, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.handy_signatur, NEW.bank,
                NEW.arbeitsgenehmigung, NEW.gueltigBis, NEW.arbeitsgenehmigungDok, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, now(), NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.handy_signatur, NEW.bank,
                NEW.arbeitsgenehmigung, NEW.gueltigBis, NEW.arbeitsgenehmigungDok, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, NULL, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function stammdaten_audit() owner to ibosng;

