alter table personalnummer drop column kostenstelle;
alter table personalnummer_history drop column kostenstelle;

create or replace function personalnummer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO personalnummer_history (personalnummer_id, firma, nummer, personalnummer, status, created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (OLD.id, OLD.firma, OLD.nummer, OLD.personalnummer, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO personalnummer_history (personalnummer_id, firma, nummer, personalnummer, status, created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.firma, NEW.nummer, NEW.personalnummer, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO personalnummer_history (personalnummer_id, firma, nummer, personalnummer, status, created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.firma, NEW.nummer, NEW.personalnummer, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter table vertragsdaten drop column kostenstelle;
alter table vertragsdaten_history drop column kostenstelle;

alter table vertragsdaten add column kostenstelle integer references kostenstelle(id);
alter table vertragsdaten_history add column kostenstelle integer references kostenstelle(id);