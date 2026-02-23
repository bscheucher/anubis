ALTER TABLE teilnehmer_2_seminar
    add column teilnahme_von date;
ALTER TABLE teilnehmer_2_seminar_history
    add column teilnahme_von date;
ALTER TABLE teilnehmer_2_seminar
    add column teilnahme_bis date;
ALTER TABLE teilnehmer_2_seminar_history
    add column teilnahme_bis date;

-- auto-generated definition
create or replace function teilnehmer_2_seminar_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, teilnahme_von, teilnahme_bis, zubuchung,
                                                  rgs, status, import_filename,
                                                  created_on,
                                                  action,
                                                  action_timestamp)
        VALUES (OLD.id, OLD.teilnehmer_id, OLD.seminar_id, OLD.betreuer, OLD.buchungsstatus, OLD.anmerkung,
                OLD.massnahmennummer, OLD.veranstaltungsnummer, OLD.geplant, OLD.eintritt, OLD.austritt,
                OLD.teilnahme_von, OLD.teilnahme_bis, OLD.zubuchung,
                OLD.rgs, OLD.status, OLD.import_filename, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, teilnahme_von, teilnahme_bis, zubuchung,
                                                  rgs, status, import_filename,
                                                  created_on,
                                                  action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.betreuer, NEW.buchungsstatus, NEW.anmerkung,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.geplant, NEW.eintritt, NEW.austritt,
                NEW.teilnahme_von, NEW.teilnahme_bis, NEW.zubuchung,
                NEW.rgs, NEW.status, NEW.import_filename, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, teilnahme_von, teilnahme_bis, zubuchung,
                                                  rgs, status, import_filename,
                                                  created_on,
                                                  action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.betreuer, NEW.buchungsstatus, NEW.anmerkung,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.geplant, NEW.eintritt, NEW.austritt,
                NEW.teilnahme_von, NEW.teilnahme_bis, NEW.zubuchung,
                NEW.rgs, NEW.status, NEW.import_filename, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function teilnehmer_2_seminar_audit() owner to ibosng;

