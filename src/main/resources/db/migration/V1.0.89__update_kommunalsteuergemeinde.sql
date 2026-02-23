alter table kommunalsteuergemeinde drop column ort;
alter table kommunalsteuergemeinde_history drop column ort;

alter table kommunalsteuergemeinde add column adresse integer references adresse(id);
alter table kommunalsteuergemeinde_history add column adresse integer;

create or replace function kommunalsteuergemeinde_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO kommunalsteuergemeinde_history (kommunalsteuergemeinde_id, lhr_id, adresse, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.lhr_id, OLD.adresse, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO kommunalsteuergemeinde_history (kommunalsteuergemeinde_id, lhr_id, adresse, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.lhr_id, NEW.adresse, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO kommunalsteuergemeinde_history (kommunalsteuergemeinde_id, lhr_id, adresse, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.lhr_id, NEW.adresse, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;