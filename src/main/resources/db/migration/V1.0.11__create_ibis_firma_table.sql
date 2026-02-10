create table if not exists ibis_firma
(
    id                          integer generated always as identity primary key,
    name                        text,
    short_name                  text,
    status                      smallint  default 0                 not null,
    bmd_client                  text,
    footer_text                 text,
    created_on                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  text                                not null,
    changed_on                  timestamp default CURRENT_TIMESTAMP,
    changed_by                  text
);

create table if not exists ibis_firma_history
(
    id                          integer generated always as identity primary key,
    ibis_firma_id               integer   not null,
    name                        text,
    short_name                  text,
    status                      smallint                            not null,
    bmd_client                  text,
    footer_text                 text,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table ibis_firma_history owner to ibosng;

create or replace function ibis_firma_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ibis_firma_history (ibis_firma_id, name, short_name, status, bmd_client, footer_text,
        created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.short_name, OLD.status, OLD.bmd_client, OLD.footer_text,
        OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO ibis_firma_history (ibis_firma_id, name, short_name, status, bmd_client, footer_text,
        created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.short_name, NEW.status, NEW.bmd_client, NEW.footer_text,
        NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO ibis_firma_history (ibis_firma_id, name, short_name, status, bmd_client, footer_text,
        created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.short_name, NEW.status, NEW.bmd_client, NEW.footer_text,
        NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function ibis_firma_audit() owner to ibosng;

create trigger ibis_firma_insert
    after insert
    on ibis_firma
    for each row
execute procedure ibis_firma_audit();

create trigger ibis_firma_update
    after update
    on ibis_firma
    for each row
execute procedure ibis_firma_audit();

create trigger ibis_firma_delete
    after delete
    on ibis_firma
    for each row
execute procedure ibis_firma_audit();
