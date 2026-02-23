alter table vertragsdaten drop column befristungVon;
alter table vertragsdaten_history drop column befristungVon;

alter table vertragsdaten rename column befristungBis to befristung_bis;
alter table vertragsdaten_history rename column befristungBis to befristung_bis;

alter table vertragsdaten add column is_befristet boolean;
alter table vertragsdaten_history add column is_befristet boolean;


create or replace function vertragsdaten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           jobBezeichnung, notizAllgemein, mobileWorking,
                                           weitereAdressezuHauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.eintritt, OLD.is_befristet, OLD.befristung_bis,
                OLD.dienstort, OLD.kostenstelle, OLD.fuerhrungskraft, OLD.startcoach, OLD.kategorie,
                OLD.taetigkeit, OLD.jobBezeichnung, OLD.notizAllgemein, OLD.mobileWorking,
                OLD.weitereAdressezuHauptwohnsitz, OLD.strasse, OLD.land, OLD.status, OLD.created_on,
                OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', CURRENT_TIMESTAMP);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           jobBezeichnung, notizAllgemein, mobileWorking,
                                           weitereAdressezuHauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.is_befristet, NEW.befristung_bis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.jobBezeichnung, NEW.notizAllgemein, NEW.mobileWorking,
                NEW.weitereAdressezuHauptwohnsitz, NEW.strasse, NEW.land, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'U', CURRENT_TIMESTAMP);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           jobBezeichnung, notizAllgemein, mobileWorking,
                                           weitereAdressezuHauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.is_befristet, NEW.befristung_bis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.jobBezeichnung, NEW.notizAllgemein, NEW.mobileWorking,
                NEW.weitereAdressezuHauptwohnsitz, NEW.strasse, NEW.land, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'I', CURRENT_TIMESTAMP);
RETURN NEW;
END IF;
RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

alter function vertragsdaten_audit() owner to ibosng;

