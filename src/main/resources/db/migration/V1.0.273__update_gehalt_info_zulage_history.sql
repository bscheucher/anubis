alter table gehalt_info_zulage_history
    add column gehalt_info_zulage_id integer;

create or replace function gehalt_info_zulage_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehalt_info_zulage_history (gehalt_info_zulage_id, gehalt_info_id, zulage_in_euro, art_der_zulage,
                                                created_on, created_by, changed_on, changed_by, action,
                                                action_timestamp)
        VALUES (OLD.id, OLD.gehalt_info_id, OLD.zulage_in_euro, OLD.art_der_zulage, OLD.created_on, OLD.created_by,
                OLD.changed_on, OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO gehalt_info_zulage_history (gehalt_info_zulage_id, gehalt_info_id, zulage_in_euro, art_der_zulage,
                                                created_on, created_by, changed_on, changed_by, action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.gehalt_info_id, NEW.zulage_in_euro, NEW.art_der_zulage, NEW.created_on, NEW.created_by,
                NEW.changed_on, NEW.changed_by, 'U', NOW());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO gehalt_info_zulage_history (gehalt_info_zulage_id, gehalt_info_id, zulage_in_euro, art_der_zulage,
                                                created_on, created_by, changed_on, changed_by, action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.gehalt_info_id, NEW.zulage_in_euro, NEW.art_der_zulage, NEW.created_on, NEW.created_by,
                NULL, NULL, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function gehalt_info_zulage_audit() owner to ibosng;

grant execute on function gehalt_info_zulage_audit() to ibosng;

