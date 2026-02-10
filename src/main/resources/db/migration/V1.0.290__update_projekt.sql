alter table projekt add column kostentraeger_display_name text;
alter table projekt_history add column kostentraeger_display_name text;

create or replace function projekt_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO projekt_history (projekt_id, projekt_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, projekt_type, kostentraeger, kostentraeger_display_name, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (OLD.id, OLD.projekt_nummer, OLD.auftrag_nummer, OLD.bezeichnung, OLD.start_date, OLD.end_date, OLD.kostenstelle_gruppe, OLD.kostenstelle, OLD.projekt_type, OLD.kostentraeger, OLD.kostentraeger_display_name, OLD.created_on, OLD.changed_on, OLD.created_by, OLD.changed_by, OLD.ausschreibung_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO projekt_history (projekt_id, projekt_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, projekt_type, kostentraeger, kostentraeger_display_name, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (NEW.id, NEW.projekt_nummer, NEW.auftrag_nummer, NEW.bezeichnung, NEW.start_date, NEW.end_date, NEW.kostenstelle_gruppe, NEW.kostenstelle, NEW.projekt_type, NEW.kostentraeger, NEW.kostentraeger_display_name, NEW.created_on, NEW.changed_on, NEW.created_by, NEW.changed_by, NEW.ausschreibung_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO projekt_history (projekt_id, projekt_nummer, auftrag_nummer, bezeichnung, start_date, end_date, kostenstelle_gruppe, kostenstelle, projekt_type, kostentraeger, kostentraeger_display_name, created_on, changed_on, created_by, changed_by, ausschreibung_id, action, action_timestamp)
        VALUES (NEW.id, NEW.projekt_nummer, NEW.auftrag_nummer, NEW.bezeichnung, NEW.start_date, NEW.end_date, NEW.kostenstelle_gruppe, NEW.kostenstelle, NEW.projekt_type, NEW.kostentraeger, NEW.kostentraeger_display_name, NEW.created_on, NEW.changed_on, NEW.created_by, NULL, NEW.ausschreibung_id, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function projekt_audit() owner to ibosng;

grant execute on function projekt_audit() to ibosng;

UPDATE projekt
SET kostentraeger_display_name = CAST(
        LPAD(kostenstelle_gruppe::text, 2, '0') ||
        LPAD(kostenstelle::text, 2, '0') ||
        LPAD(kostentraeger::text, 3, '0')
    AS TEXT)
WHERE kostenstelle_gruppe IS NOT NULL
  AND kostenstelle IS NOT NULL
  AND kostentraeger IS NOT NULL;

