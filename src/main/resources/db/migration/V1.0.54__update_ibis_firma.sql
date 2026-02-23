alter table ibis_firma add column lhr_nr integer;
alter table ibis_firma add column lhr_kz text;
alter table ibis_firma_history add column lhr_nr integer;
alter table ibis_firma_history add column lhr_kz text;

create or replace function ibis_firma_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ibis_firma_history (ibis_firma_id, name, short_name, status, bmd_client, lhr_nr, lhr_kz, footer_text,
                                        created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.short_name, OLD.status, OLD.bmd_client, OLD.lhr_nr, OLD.lhr_kz, OLD.footer_text,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO ibis_firma_history (ibis_firma_id, name, short_name, status, bmd_client, lhr_nr, lhr_kz, footer_text,
                                        created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.short_name, NEW.status, NEW.bmd_client, NEW.lhr_nr, NEW.lhr_kz, NEW.footer_text,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO ibis_firma_history (ibis_firma_id, name, short_name, status, bmd_client, lhr_nr, lhr_kz, footer_text,
                                        created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.short_name, NEW.status, NEW.bmd_client, NEW.lhr_nr, NEW.lhr_kz, NEW.footer_text,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

update ibis_firma set bmd_client = 1, status = 1, lhr_nr = 1, lhr_kz = 'IA' where name = 'ibis acam Bildungs GmbH';
update ibis_firma set bmd_client = 8, status = 1, lhr_nr = 10, lhr_kz = 'IA' where name = 'Aspire Beteiligungs GmbH';
update ibis_firma set bmd_client = 9, status = 1, lhr_nr = 2, lhr_kz = 'IA' where name = 'ibis acam gemeinnützige Bildungs GmbH';
update ibis_firma set bmd_client = 11, status = 1, lhr_nr = 3, lhr_kz = 'IA' where name = 'KAOS gemeinnützige Bildungsservice GmbH';
update ibis_firma set bmd_client = 13, status = 1, lhr_nr = 11, lhr_kz = 'IA' where name = 'Aspire Operations GmbH';

update ibis_firma set bmd_client = null, status = 2, lhr_nr = null where name = 'Aspire Invest Holding GmbH';
update ibis_firma set bmd_client = 7, status = 2, lhr_nr = null where name = 'Aspire Education GmbH';
update ibis_firma set bmd_client = null, status = 2, lhr_nr = null where name = 'Aspire Education Group Holding GmbH';