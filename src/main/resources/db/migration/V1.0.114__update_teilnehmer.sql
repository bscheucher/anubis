insert into kollektivvertrag (name, created_by) values ('AMS-Lehrteilnehmer', current_user);

alter table teilnehmer add column personalnummer integer references personalnummer (id);
alter table teilnehmer_history add column personalnummer integer;

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
                                        action, action_timestamp, anrede, personalnummer)
        VALUES (OLD.titel, OLD.id, OLD.nachname, OLD.vorname, OLD.geschlecht,
                OLD.sv_nummer, OLD.geburtsdatum, OLD.adresse,
                OLD.email, OLD.status, OLD.import_filename, OLD.info,  OLD.created_on, OLD.created_by, now(),
                OLD.changed_by,
                'D', now(), OLD.anrede, OLD.personalnummer);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info,  created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp, anrede, personalnummer)
        VALUES (NEW.titel, NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info,  NEW.created_on, NEW.created_by, now(),
                NEW.changed_by,
                'U', now(), NEW.anrede, NEW.personalnummer);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_history (titel, teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp, anrede, personalnummer)
        VALUES (NEW.titel, NEW.id,  NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.created_on, NEW.created_by, now(),
                NULL,
                'I', now(), NEW.anrede, NEW.personalnummer);
RETURN NEW;
END IF;
RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function teilnehmer_audit() owner to ibosng;