alter table teilnehmer
    drop column buchungsstatus;
alter table teilnehmer
    drop column anmerkung;
alter table teilnehmer
    drop column zubuchung;
alter table teilnehmer
    drop column geplant;
alter table teilnehmer
    drop column eintritt;
alter table teilnehmer
    drop column austritt;
alter table teilnehmer
    drop column rgs;
alter table teilnehmer
    drop column massnahmennummer;
alter table teilnehmer
    drop column veranstaltungsnummer;
alter table teilnehmer
    drop column betreuer;

alter table teilnehmer_history
    drop column buchungsstatus;
alter table teilnehmer_history
    drop column anmerkung;
alter table teilnehmer_history
    drop column zubuchung;
alter table teilnehmer_history
    drop column geplant;
alter table teilnehmer_history
    drop column eintritt;
alter table teilnehmer_history
    drop column austritt;
alter table teilnehmer_history
    drop column rgs;
alter table teilnehmer_history
    drop column massnahmennummer;
alter table teilnehmer_history
    drop column veranstaltungsnummer;
alter table teilnehmer_history
    drop column betreuer;


alter table teilnehmer
    drop column titel;
alter table teilnehmer_history
    drop column titel;


create or replace function teilnehmer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_history (teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, nation, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (OLD.id, OLD.nachname, OLD.vorname, OLD.geschlecht,
                OLD.sv_nummer, OLD.geburtsdatum, OLD.adresse,
                OLD.email, OLD.status, OLD.import_filename, OLD.info, OLD.nation, OLD.created_on, OLD.created_by, now(),
                OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_history (teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, nation, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.nation, NEW.created_on, NEW.created_by, now(),
                NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_history (teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, nation, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id,  NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, OLD.nation, NEW.created_on, NEW.created_by, now(),
                NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter table teilnehmer_2_seminar
    add column buchungsstatus TEXT;
alter table teilnehmer_2_seminar
    add column anmerkung TEXT;
alter table teilnehmer_2_seminar
    add column massnahmennummer TEXT;
alter table teilnehmer_2_seminar
    add column veranstaltungsnummer TEXT;
alter table teilnehmer_2_seminar
    add column geplant date;
alter table teilnehmer_2_seminar
    add column eintritt date;
alter table teilnehmer_2_seminar
    add column austritt date;
alter table teilnehmer_2_seminar
    add column zubuchung date;
alter table teilnehmer_2_seminar
    add column rgs INTEGER references rgs (id);
alter table teilnehmer_2_seminar
    add column betreuer INTEGER references betreuer (id);


alter table teilnehmer_2_seminar_history
    add column buchungsstatus TEXT;
alter table teilnehmer_2_seminar_history
    add column anmerkung TEXT;
alter table teilnehmer_2_seminar_history
    add column massnahmennummer TEXT;
alter table teilnehmer_2_seminar_history
    add column veranstaltungsnummer TEXT;
alter table teilnehmer_2_seminar_history
    add column geplant date;
alter table teilnehmer_2_seminar_history
    add column eintritt date;
alter table teilnehmer_2_seminar_history
    add column austritt date;
alter table teilnehmer_2_seminar_history
    add column zubuchung date;
alter table teilnehmer_2_seminar_history
    add column rgs INTEGER;
alter table teilnehmer_2_seminar_history
    add column betreuer INTEGER;

ALTER TABLE teilnehmer_2_seminar_history
    RENAME COLUMN course_id TO seminar_id;



create or replace function teilnehmer_2_seminar_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, zubuchung, rgs, created_on, action,
                                                  action_timestamp)
        VALUES (OLD.id, OLD.teilnehmer_id, OLD.seminar_id, OLD.betreuer, OLD.buchungsstatus, OLD.anmerkung,
                OLD.massnahmennummer, OLD.veranstaltungsnummer, OLD.geplant, OLD.eintritt, OLD.austritt, OLD.zubuchung,
                OLD.rgs, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, zubuchung, rgs, created_on, action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.betreuer, NEW.buchungsstatus, NEW.anmerkung,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.geplant, NEW.eintritt, NEW.austritt, NEW.zubuchung,
                NEW.rgs, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, seminar_id, betreuer,
                                                  buchungsstatus, anmerkung, massnahmennummer, veranstaltungsnummer,
                                                  geplant, eintritt, austritt, zubuchung, rgs, created_on, action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.betreuer, NEW.buchungsstatus, NEW.anmerkung,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.geplant, NEW.eintritt, NEW.austritt, NEW.zubuchung,
                NEW.rgs, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


alter table teilnehmer
    drop column nation;
alter table teilnehmer_history
    drop column nation;

alter table teilnehmer
    add column nation INTEGER references land (id);
alter table teilnehmer_history
    add column nation INTEGER;

alter table teilnehmer
    drop column geschlecht;
alter table teilnehmer_history
    drop column geschlecht;

alter table teilnehmer
    add column geschlecht INTEGER references geschlecht (id);
alter table teilnehmer_history
    add column geschlecht INTEGER;



create table if not exists teilnehmer_titles
(
    id         integer generated always as identity primary key,
    teilnehmer_id       integer references teilnehmer(id),
    titel_id    integer references titel(id)
);

create table if not exists teilnehmer_titles_history
(
    id               integer generated always as identity primary key,
    teilnehmer_titles_id     integer   not null,
    teilnehmer_id       integer,
    titel_id    integer,
    action           char,
    action_timestamp timestamp not null
);

create or replace function teilnehmer_titles_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_titles_history (teilnehmer_titles_id, teilnehmer_id, titel_id,
                                               action,
                                               action_timestamp)
        VALUES (OLD.id, OLD.teilnehmer_id, OLD.titel_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_titles_history (teilnehmer_titles_id, teilnehmer_id, titel_id,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.titel_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_titles_history (teilnehmer_titles_id, teilnehmer_id, titel_id,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.titel_id,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;