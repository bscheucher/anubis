ALTER TABLE ibis_firma add column lhr_bank_nr integer;
ALTER TABLE ibis_firma_history add column lhr_bank_nr integer;

create or replace function ibis_firma_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ibis_firma_history (ibis_firma_id, name, short_name, status, bmd_client, lhr_nr, lhr_kz, lhr_bank_nr, footer_text,
                                        created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.short_name, OLD.status, OLD.bmd_client, OLD.lhr_nr, OLD.lhr_kz, OLD.lhr_bank_nr, OLD.footer_text,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO ibis_firma_history (ibis_firma_id, name, short_name, status, bmd_client, lhr_nr, lhr_kz, lhr_bank_nr, footer_text,
                                        created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.short_name, NEW.status, NEW.bmd_client, NEW.lhr_nr, NEW.lhr_kz, NEW.lhr_bank_nr, NEW.footer_text,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO ibis_firma_history (ibis_firma_id, name, short_name, status, bmd_client, lhr_nr, lhr_kz, lhr_bank_nr, footer_text,
                                        created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.short_name, NEW.status, NEW.bmd_client, NEW.lhr_nr, NEW.lhr_kz, NEW.lhr_bank_nr, NEW.footer_text,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function ibis_firma_audit() owner to ibosng;



update ibis_firma set lhr_bank_nr = 1 where status = 1;