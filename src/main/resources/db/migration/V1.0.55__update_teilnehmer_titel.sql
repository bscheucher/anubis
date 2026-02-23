alter table teilnehmer add column titel text;
alter table teilnehmer_history add column titel text;

create or replace function teilnehmer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info,  created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (OLD.titel, OLD.id, OLD.nachname, OLD.vorname, OLD.geschlecht,
                OLD.sv_nummer, OLD.geburtsdatum, OLD.adresse,
                OLD.email, OLD.status, OLD.import_filename, OLD.info,  OLD.created_on, OLD.created_by, now(),
                OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info,  created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info,  NEW.created_on, NEW.created_by, now(),
                NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.titel, NEW.id,  NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.created_on, NEW.created_by, now(),
                NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function teilnehmer_audit() owner to ibosng;


drop table teilnehmer_titles;
drop table teilnehmer_titles_history;
drop function teilnehmer_titles_audit();
