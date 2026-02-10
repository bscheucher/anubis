alter table if exists benutzer add column if not exists moxis_classifier text;
alter table if exists benutzer_history add column if not exists moxis_classifier text;

create or replace function benutzer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO benutzer_history (benutzer_id, azure_id, first_name, last_name, email, status, moxis_classifier, created_on,
                                      created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.azure_id, OLD.first_name, OLD.last_name, OLD.email, OLD.status, OLD.moxis_classifier, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO benutzer_history (benutzer_id, azure_id, first_name, last_name, email, status, moxis_classifier, created_on,
                                      created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.azure_id, NEW.first_name, NEW.last_name, NEW.email, NEW.status, NEW.moxis_classifier, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO benutzer_history (benutzer_id, azure_id, first_name, last_name, email, status, moxis_classifier, created_on,
                                      created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.azure_id, NEW.first_name, NEW.last_name, NEW.email, NEW.status, NEW.moxis_classifier, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


create table if not exists signature_position
(
    id                          integer generated always as identity primary key,
    x                           text,
    y                           text,
    width                       text,
    height                      text,
    page_number                 integer,
    created_on                  timestamp default CURRENT_TIMESTAMP     not null,
    created_by                  text                                    not null,
    changed_on                  timestamp,
    changed_by                  text
);

alter table signature_position owner to ibosng;

create table if not exists signature_position_history
(
    id                          integer generated always as identity primary key,
    signature_position_id       integer                                 not null,
    x                           text,
    y                           text,
    width                       text,
    height                      text,
    page_number                 integer,
    created_on                  timestamp                               not null,
    created_by                  text                                    not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table signature_position_history owner to ibosng;

create or replace function signature_position_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO signature_position_history (signature_position_id, x, y, width, height, page_number, created_on,
                                                created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.x, OLD.y, OLD.width, OLD.height, OLD.page_number, OLD.created_on, OLD.created_by,
                CURRENT_USER, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO signature_position_history (signature_position_id, x, y, width, height, page_number, created_on,
                                                created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.x, NEW.y, NEW.width, NEW.height, NEW.page_number, NEW.created_on, NEW.created_by,
                CURRENT_USER, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO signature_position_history (signature_position_id, x, y, width, height, page_number, created_on,
                                                created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.x, NEW.y, NEW.width, NEW.height, NEW.page_number, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function signature_position_audit() owner to ibosng;

create trigger signature_position_insert
    after insert
    on signature_position
    for each row
execute procedure signature_position_audit();

create trigger signature_position_update
    after update
    on signature_position
    for each row
execute procedure signature_position_audit();

create trigger signature_position_delete
    after delete
    on signature_position
    for each row
execute procedure signature_position_audit();


create table if not exists moxis_job
(
    id                          integer generated always as identity primary key,
    constituent                 integer references benutzer             not null,
    position_type               text,
    description                 text,
    category                    text,
    expiration_date             timestamp,
    reference_id                text,
    created_on                  timestamp default CURRENT_TIMESTAMP     not null,
    created_by                  text                                    not null,
    changed_on                  timestamp,
    changed_by                  text
);

alter table moxis_job owner to ibosng;

create table if not exists moxis_job_history
(
    id                          integer generated always as identity primary key,
    moxis_job_id                integer                                 not null,
    constituent                 integer,
    position_type               text,
    description                 text,
    category                    text,
    expiration_date             timestamp,
    reference_id                text,
    created_on                  timestamp                               not null,
    created_by                  text                                    not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table moxis_job_history owner to ibosng;

create or replace function moxis_job_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO moxis_job_history (moxis_job_id, constituent, position_type, description, category, expiration_date, reference_id, created_on, created_by,
                                    changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.constituent, OLD.position_type, OLD.description, OLD.category, OLD.expiration_date, OLD.reference_id, OLD.created_on, OLD.created_by,
                CURRENT_USER, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO moxis_job_history (moxis_job_id, constituent, position_type, description, category, expiration_date, reference_id, created_on, created_by,
                                    changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.constituent, NEW.position_type, NEW.description, NEW.category, NEW.expiration_date, NEW.reference_id, NEW.created_on, NEW.created_by,
                CURRENT_USER, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO moxis_job_history (moxis_job_id, constituent, position_type, description, category, expiration_date, reference_id, created_on, created_by,
                                    changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.constituent, NEW.position_type, NEW.description, NEW.category, NEW.expiration_date, NEW.reference_id, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function moxis_job_audit() owner to ibosng;

create trigger moxis_job_insert
    after insert
    on moxis_job
    for each row
execute procedure moxis_job_audit();

create trigger moxis_job_update
    after update
    on moxis_job
    for each row
execute procedure moxis_job_audit();

create trigger moxis_job_delete
    after delete
    on moxis_job
    for each row
execute procedure moxis_job_audit();


create table if not exists moxis_iteration_data
(
    id                          integer generated always as identity primary key,
    job_id                      integer references moxis_job            not null,
    category                    text,
    iteration_number            integer,
    created_on                  timestamp default CURRENT_TIMESTAMP     not null,
    created_by                  text                                    not null,
    changed_on                  timestamp,
    changed_by                  text
);

alter table moxis_iteration_data owner to ibosng;

create table if not exists moxis_iteration_data_history
(
    id                          integer generated always as identity primary key,
    iteration_data_id           integer                                 not null,
    job_id                      integer,
    category                    text,
    iteration_number            integer,
    created_on                  timestamp                               not null,
    created_by                  text                                    not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table moxis_iteration_data_history owner to ibosng;

create or replace function moxis_iteration_data_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO moxis_iteration_data_history (iteration_data_id, job_id, category, iteration_number, created_on, created_by,
                                    changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.job_id, OLD.category, OLD.iteration_number, OLD.created_on, OLD.created_by, CURRENT_USER, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO moxis_iteration_data_history (iteration_data_id, job_id, category, iteration_number, created_on, created_by,
                                    changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.job_id, NEW.category, NEW.iteration_number, NEW.created_on, NEW.created_by, CURRENT_USER, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO moxis_iteration_data_history (iteration_data_id, job_id, category, iteration_number, created_on, created_by,
                                    changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.job_id, NEW.category, NEW.iteration_number, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function moxis_iteration_data_audit() owner to ibosng;

create trigger moxis_iteration_data_insert
    after insert
    on moxis_iteration_data
    for each row
execute procedure moxis_iteration_data_audit();

create trigger moxis_iteration_data_update
    after update
    on moxis_iteration_data
    for each row
execute procedure moxis_iteration_data_audit();

create trigger moxis_iteration_data_delete
    after delete
    on moxis_iteration_data
    for each row
execute procedure moxis_iteration_data_audit();


create table if not exists moxis_invitee
(
    id                          integer generated always as identity primary key,
    iteration_data              integer references moxis_iteration_data not null,
    class                       text                                    not null,
    classifier                  text                                    not null,
    name                        text,
    role_name                   text,
    signature_position          integer references signature_position,
    created_on                  timestamp default CURRENT_TIMESTAMP     not null,
    created_by                  text                                    not null,
    changed_on                  timestamp,
    changed_by                  text
);

alter table moxis_invitee owner to ibosng;

create table if not exists moxis_invitee_history
(
    id                          integer generated always as identity primary key,
    moxis_invitee_id            integer                                 not null,
    class                       text                                    not null,
    classifier                  text                                    not null,
    name                        text,
    role_name                   text,
    signature_position          integer,
    created_on                  timestamp                               not null,
    created_by                  text                                    not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table moxis_invitee_history owner to ibosng;

create or replace function moxis_invitee_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO moxis_invitee_history (moxis_invitee_id, classifier, name, class, role_name, signature_position, created_on, created_by,
                                    changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.classifier, OLD.name, OLD.class, OLD.role_name, OLD.signature_position, OLD.created_on, OLD.created_by,
                CURRENT_USER, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO moxis_invitee_history (moxis_invitee_id, classifier, name, class, role_name, signature_position, created_on, created_by,
                                    changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.classifier, NEW.name, NEW.class, NEW.role_name, NEW.signature_position, NEW.created_on, NEW.created_by,
                CURRENT_USER, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO moxis_invitee_history (moxis_invitee_id, classifier, name, class, role_name, signature_position, created_on, created_by,
                                    changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.classifier, NEW.name, NEW.class, NEW.role_name, NEW.signature_position, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function moxis_invitee_audit() owner to ibosng;

create trigger moxis_invitee_insert
    after insert
    on moxis_invitee
    for each row
execute procedure moxis_invitee_audit();

create trigger moxis_invitee_update
    after update
    on moxis_invitee
    for each row
execute procedure moxis_invitee_audit();

create trigger moxis_invitee_delete
    after delete
    on moxis_invitee
    for each row
execute procedure moxis_invitee_audit();
