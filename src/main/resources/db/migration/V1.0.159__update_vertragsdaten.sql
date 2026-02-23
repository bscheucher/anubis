ALTER TABLE vertragsdaten
    ADD COLUMN klasse INTEGER references klasse(id);

ALTER TABLE vertragsdaten_history
    ADD COLUMN klasse INTEGER;

CREATE OR REPLACE FUNCTION vertragsdaten_audit() RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, eintritt, befristung_bis, kategorie, taetigkeit, job_bezeichnung,
                                           notiz_allgemein, mobile_working, weitere_adresse_zu_hauptwohnsitz, status,
                                           created_on, created_by, changed_on, changed_by, personalnummer, dienstort,
                                           is_befristet, adresse, notiz_zusatz_vereinbarung, abrechnungsgruppe,
                                           kommunalsteuergemeinde, dienstnehmergruppe, kostenstelle, fuehrungskraft,
                                           startcoach, klasse, action, action_timestamp)
        VALUES (OLD.id, OLD.eintritt, OLD.befristung_bis, OLD.kategorie, OLD.taetigkeit, OLD.job_bezeichnung,
                OLD.notiz_allgemein, OLD.mobile_working, OLD.weitere_adresse_zu_hauptwohnsitz, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, OLD.personalnummer, OLD.dienstort,
                OLD.is_befristet, OLD.adresse, OLD.notiz_zusatz_vereinbarung, OLD.abrechnungsgruppe,
                OLD.kommunalsteuergemeinde, OLD.dienstnehmergruppe, OLD.kostenstelle, OLD.fuehrungskraft,
                OLD.startcoach, OLD.klasse, 'D', NOW());
        RETURN OLD;

    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, eintritt, befristung_bis, kategorie, taetigkeit, job_bezeichnung,
                                           notiz_allgemein, mobile_working, weitere_adresse_zu_hauptwohnsitz, status,
                                           created_on, created_by, changed_on, changed_by, personalnummer, dienstort,
                                           is_befristet, adresse, notiz_zusatz_vereinbarung, abrechnungsgruppe,
                                           kommunalsteuergemeinde, dienstnehmergruppe, kostenstelle, fuehrungskraft,
                                           startcoach, klasse, action, action_timestamp)
        VALUES (NEW.id, NEW.eintritt, NEW.befristung_bis, NEW.kategorie, NEW.taetigkeit, NEW.job_bezeichnung,
                NEW.notiz_allgemein, NEW.mobile_working, NEW.weitere_adresse_zu_hauptwohnsitz, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.personalnummer, NEW.dienstort,
                NEW.is_befristet, NEW.adresse, NEW.notiz_zusatz_vereinbarung, NEW.abrechnungsgruppe,
                NEW.kommunalsteuergemeinde, NEW.dienstnehmergruppe, NEW.kostenstelle, NEW.fuehrungskraft,
                NEW.startcoach, NEW.klasse, 'U', NOW());
        RETURN NEW;

    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, eintritt, befristung_bis, kategorie, taetigkeit, job_bezeichnung,
                                           notiz_allgemein, mobile_working, weitere_adresse_zu_hauptwohnsitz, status,
                                           created_on, created_by, changed_on, changed_by, personalnummer, dienstort,
                                           is_befristet, adresse, notiz_zusatz_vereinbarung, abrechnungsgruppe,
                                           kommunalsteuergemeinde, dienstnehmergruppe, kostenstelle, fuehrungskraft,
                                           startcoach, klasse, action, action_timestamp)
        VALUES (NEW.id, NEW.eintritt, NEW.befristung_bis, NEW.kategorie, NEW.taetigkeit, NEW.job_bezeichnung,
                NEW.notiz_allgemein, NEW.mobile_working, NEW.weitere_adresse_zu_hauptwohnsitz, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, NEW.personalnummer, NEW.dienstort,
                NEW.is_befristet, NEW.adresse, NEW.notiz_zusatz_vereinbarung, NEW.abrechnungsgruppe,
                NEW.kommunalsteuergemeinde, NEW.dienstnehmergruppe, NEW.kostenstelle, NEW.fuehrungskraft,
                NEW.startcoach, NEW.klasse, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;