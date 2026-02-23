alter table zusatz_info
    add column religion integer references religion (id);
alter table zusatz_info_history
    add column religion integer;

alter table zusatz_info
    add column erreichbarkeit integer references erreichbarkeit (id);
alter table zusatz_info_history
    add column erreichbarkeit integer;


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
                                         foto, religion, erreichbarkeit,
                                         status, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.burgenland, OLD.kaernten, OLD.niederoesterreich, OLD.oberoesterreich, OLD.salzburg,
                OLD.steiermark, OLD.tirol, OLD.vorarlberg, OLD.wien, OLD.arbeitsgenehmigung, OLD.gueltigBis,
                OLD.arbeitsgenehmigung_status, OLD.foto, OLD.religion, OLD.erreichbarkeit, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zusatz_info_history (zusatz_info_id,
                                         burgenland, kaernten, niederoesterreich, oberoesterreich, salzburg, steiermark,
                                         tirol, vorarlberg, wien, arbeitsgenehmigung, gueltigBis,
                                         arbeitsgenehmigung_status,
                                         foto, religion, erreichbarkeit,
                                         status, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id,
                NEW.burgenland, NEW.kaernten, NEW.niederoesterreich, NEW.oberoesterreich, NEW.salzburg, NEW.steiermark,
                NEW.tirol, NEW.vorarlberg, NEW.wien, NEW.arbeitsgenehmigung, NEW.gueltigBis,
                NEW.arbeitsgenehmigung_status,
                NEW.foto, NEW.religion, NEW.erreichbarkeit,
                NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zusatz_info_history (zusatz_info_id,
                                         burgenland, kaernten, niederoesterreich, oberoesterreich, salzburg, steiermark,
                                         tirol, vorarlberg, wien, arbeitsgenehmigung, gueltigBis,
                                         arbeitsgenehmigung_status,
                                         foto, religion, erreichbarkeit,
                                         status, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.burgenland, NEW.kaernten, NEW.niederoesterreich, NEW.oberoesterreich, NEW.salzburg,
                NEW.steiermark, NEW.tirol, NEW.vorarlberg, NEW.wien, NEW.arbeitsgenehmigung, NEW.gueltigBis,
                NEW.arbeitsgenehmigung_status, NEW.foto, NEW.religion, NEW.erreichbarkeit,
                NEW.status, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function zusatz_info_audit() owner to ibosng;

