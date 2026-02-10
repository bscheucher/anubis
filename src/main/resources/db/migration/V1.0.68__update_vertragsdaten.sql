
ALTER TABLE vertragsdaten
    ADD COLUMN notiz_zusatz_vereinbarung TEXT;

ALTER TABLE vertragsdaten_history
    ADD COLUMN notiz_zusatz_vereinbarung TEXT;

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
                                           job_bezeichnung, notiz_allgemein, mobile_working,
                                           weitere_adresse_zu_hauptwohnsitz, notiz_zusatz_vereinbarung, adresse, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.eintritt, OLD.is_befristet, OLD.befristung_bis,
                OLD.dienstort, OLD.kostenstelle, OLD.fuerhrungskraft, OLD.startcoach, OLD.kategorie,
                OLD.taetigkeit, OLD.job_bezeichnung, OLD.notiz_allgemein, OLD.mobile_working,
                OLD.weitere_adresse_zu_hauptwohnsitz, OLD.notiz_zusatz_vereinbarung, OLD.adresse, OLD.status, OLD.created_on,
                OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           job_bezeichnung, notiz_allgemein, mobile_working,
                                           weitere_adresse_zu_hauptwohnsitz, notiz_zusatz_vereinbarung, adresse, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.is_befristet, NEW.befristung_bis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.job_bezeichnung, NEW.notiz_allgemein, NEW.mobile_working,
                NEW.weitere_adresse_zu_hauptwohnsitz, NEW.notiz_zusatz_vereinbarung, NEW.adresse, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           job_bezeichnung, notiz_allgemein, mobile_working,
                                           weitere_adresse_zu_hauptwohnsitz, notiz_zusatz_vereinbarung, adresse, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.is_befristet, NEW.befristung_bis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.job_bezeichnung, NEW.notiz_allgemein, NEW.mobile_working,
                NEW.weitere_adresse_zu_hauptwohnsitz, NEW.notiz_zusatz_vereinbarung, NEW.adresse, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

alter function vertragsdaten_audit() owner to ibosng;

