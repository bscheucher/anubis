ALTER TABLE teilnehmer_2_seminar
    add column status smallint not null default 0;
ALTER TABLE teilnehmer_2_seminar
    add column import_filename text;
ALTER TABLE teilnehmer_2_seminar_history
    add column status smallint not null default 0;
ALTER TABLE teilnehmer_2_seminar_history
    add column import_filename text;

update teilnehmer_2_seminar set status = 1 where seminar_id is not null;
update teilnehmer_2_seminar set status = 2 where seminar_id is null;

create or replace function teilnehmer_2_seminar_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, zubuchung, rgs, status, import_filename, created_on,
                                                  action,
                                                  action_timestamp)
        VALUES (OLD.id, OLD.teilnehmer_id, OLD.seminar_id, OLD.betreuer, OLD.buchungsstatus, OLD.anmerkung,
                OLD.massnahmennummer, OLD.veranstaltungsnummer, OLD.geplant, OLD.eintritt, OLD.austritt, OLD.zubuchung,
                OLD.rgs, OLD.status, OLD.import_filename, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, zubuchung, rgs, status, import_filename, created_on,
                                                  action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.betreuer, NEW.buchungsstatus, NEW.anmerkung,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.geplant, NEW.eintritt, NEW.austritt, NEW.zubuchung,
                NEW.status, NEW.import_filename,
                NEW.rgs, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, zubuchung, rgs, status, import_filename, created_on,
                                                  action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.betreuer, NEW.buchungsstatus, NEW.anmerkung,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.geplant, NEW.eintritt, NEW.austritt, NEW.zubuchung,
                NEW.rgs, NEW.status, NEW.import_filename, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;
