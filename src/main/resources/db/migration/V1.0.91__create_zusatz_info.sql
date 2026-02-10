create table if not exists zusatz_info
(
    id                        integer generated always as identity primary key,
    burgenland                boolean,
    kaernten                  boolean,
    niederoesterreich         boolean,
    oberoesterreich           boolean,
    salzburg                  boolean,
    steiermark                boolean,
    tirol                     boolean,
    vorarlberg                boolean,
    wien                      boolean,
    arbeitsgenehmigung        text,
    gueltigBis                DATE,
    arbeitsgenehmigung_status TEXT,
    foto                      text,
    status                    smallint                            not null,
    created_on                timestamp default CURRENT_TIMESTAMP not null,
    created_by                text                                not null,
    changed_on                timestamp,
    changed_by                text
);

create table if not exists zusatz_info_history
(
    id                        integer generated always as identity primary key,
    zusatz_info_id            integer                             not null,
    burgenland                boolean,
    kaernten                  boolean,
    niederoesterreich         boolean,
    oberoesterreich           boolean,
    salzburg                  boolean,
    steiermark                boolean,
    tirol                     boolean,
    vorarlberg                boolean,
    wien                      boolean,
    arbeitsgenehmigung        text,
    gueltigBis                DATE,
    arbeitsgenehmigung_status TEXT,
    foto                      text,
    status                    smallint                            not null,
    created_on                timestamp default CURRENT_TIMESTAMP not null,
    created_by                text                                not null,
    changed_on                timestamp,
    changed_by                text,
    action                    char,
    action_timestamp          timestamp                           not null
);

create or replace function zusatz_info_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zusatz_info_history (zusatz_info_id,
                                         burgenland, kaernten, niederoesterreich, oberoesterreich, salzburg, steiermark,
                                         tirol, vorarlberg, wien, arbeitsgenehmigung, gueltigBis,
                                         arbeitsgenehmigung_status,
                                         foto,
                                         status, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.burgenland, OLD.kaernten, OLD.niederoesterreich, OLD.oberoesterreich, OLD.salzburg,
                OLD.steiermark, OLD.tirol, OLD.vorarlberg, OLD.wien, OLD.arbeitsgenehmigung, OLD.gueltigBis,
                OLD.arbeitsgenehmigung_status, OLD.foto, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zusatz_info_history (zusatz_info_id,
                                         burgenland, kaernten, niederoesterreich, oberoesterreich, salzburg, steiermark,
                                         tirol, vorarlberg, wien, arbeitsgenehmigung, gueltigBis,
                                         arbeitsgenehmigung_status,
                                         foto,
                                         status, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id,
                NEW.burgenland, NEW.kaernten, NEW.niederoesterreich, NEW.oberoesterreich, NEW.salzburg, NEW.steiermark,
                NEW.tirol, NEW.vorarlberg, NEW.wien, NEW.arbeitsgenehmigung, NEW.gueltigBis,
                NEW.arbeitsgenehmigung_status,
                NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zusatz_info_history (zusatz_info_id,
                                         burgenland, kaernten, niederoesterreich, oberoesterreich, salzburg, steiermark,
                                         tirol, vorarlberg, wien, arbeitsgenehmigung, gueltigBis,
                                         arbeitsgenehmigung_status,
                                         foto,
                                         status, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.burgenland, NEW.kaernten, NEW.niederoesterreich, NEW.oberoesterreich, NEW.salzburg,
                NEW.steiermark, NEW.tirol, NEW.vorarlberg, NEW.wien, NEW.arbeitsgenehmigung, NEW.gueltigBis,
                NEW.arbeitsgenehmigung_status, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function zusatz_info_audit() owner to ibosng;

create trigger zusatz_info_insert
    after insert
    on zusatz_info
    for each row
execute procedure zusatz_info_audit();

create trigger zusatz_info_update
    after update
    on zusatz_info
    for each row
execute procedure zusatz_info_audit();

create trigger zusatz_info_delete
    after delete
    on zusatz_info
    for each row
execute procedure zusatz_info_audit();