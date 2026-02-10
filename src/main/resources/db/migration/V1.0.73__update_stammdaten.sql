alter table stammdaten drop column ecard_file_name;
alter table stammdaten_history drop column ecard_file_name;

alter table stammdaten drop column arbeitsgenehmigung_file_name;
alter table stammdaten_history drop column arbeitsgenehmigung_file_name;

alter table stammdaten add column abweichende_adresse integer references adresse(id);
alter table stammdaten_history add column abweichende_adresse integer references adresse(id);


create or replace function stammdaten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard_status, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        abweichende_adresse, arbeitsgenehmigung_status, gueltigBis, arbeitsgenehmigung, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.anrede, OLD.titel, OLD.titel2, OLD.nachname, OLD.vorname,
                OLD.geburtsname, OLD.svnr, OLD.ecard_status, OLD.geschlecht, OLD.familienstand,
                OLD.geburtsdatum, OLD.lebensalter, OLD.staatsbuergerschaft, OLD.muttersprache,
                OLD.adresse, OLD.email, OLD.mobilnummer, OLD.handy_signatur, OLD.bank,
                OLD.abweichende_adresse, OLD.arbeitsgenehmigung_status, OLD.gueltigBis, OLD.arbeitsgenehmigung, OLD.foto,
                OLD.status, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard_status, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        abweichende_adresse, arbeitsgenehmigung_status, gueltigBis, arbeitsgenehmigung, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard_status, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.handy_signatur, NEW.bank,
                NEW.abweichende_adresse, NEW.arbeitsgenehmigung_status, NEW.gueltigBis, NEW.arbeitsgenehmigung, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, now(), NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard_status, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, handy_signatur, bank,
                                        abweichende_adresse, arbeitsgenehmigung_status, gueltigBis, arbeitsgenehmigung, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard_status, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.handy_signatur, NEW.bank,
                NEW.abweichende_adresse, NEW.arbeitsgenehmigung_status, NEW.gueltigBis, NEW.arbeitsgenehmigung, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, NULL, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;





alter table vordienstzeiten drop column nachweis_file_name;
alter table vordienstzeiten_history drop column nachweis_file_name;


create or replace function vordienstzeiten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis_status, status, created_on,
                                             created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.vertragsart, OLD.firma, OLD.von, OLD.bis, OLD.wochenstunden,
                OLD.anrechenbar, OLD.nachweis_status, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis_status, status,
                                             created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis_status, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vordienstzeiten_history (vordienstzeiten_id, vertragsdaten_id, vertragsart, firma, von, bis,
                                             wochenstunden, anrechenbar, nachweis_status, status,
                                             created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vertragsart, NEW.firma, NEW.von, NEW.bis, NEW.wochenstunden,
                NEW.anrechenbar, NEW.nachweis_status, NEW.status, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;