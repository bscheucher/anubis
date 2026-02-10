CREATE TABLE IF NOT EXISTS languages
(
    id                              integer generated always as identity primary key,
    name                            TEXT,
    created_on                      timestamp                               not null,
    created_by                      text                                    not null,
    changed_on                      timestamp,
    changed_by                      text
);


create table if not exists languages_history
(
    id                              integer generated always as identity primary key,
    languages_id                    integer                                 not null,
    name                            TEXT,
    created_on                      timestamp                               not null,
    created_by                      text                                    not null,
    changed_on                      timestamp,
    changed_by                      text,
    action                          char,
    action_timestamp                timestamp                               not null
);

CREATE OR REPLACE FUNCTION languages_audit()
    RETURNS TRIGGER LANGUAGE plpgsql AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO languages_history (languages_id, name, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO languages_history (languages_id, name, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO languages_history (languages_id, name, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

alter function languages_audit() owner to ibosng;

create trigger languages_insert
    after insert
    on languages
    for each row
execute procedure languages_audit();

create trigger languages_update
    after update
    on languages
    for each row
execute procedure languages_audit();

create trigger languages_delete
    after delete
    on languages
    for each row
execute procedure languages_audit();


CREATE TABLE IF NOT EXISTS labels
(
    id                              integer generated always as identity primary key,
    label_key                       TEXT,
    label_text                      TEXT,
    language_id                     integer                 references languages(id),
    section_name                    TEXT,
    created_on                      timestamp                               not null,
    created_by                      text                                    not null,
    changed_on                      timestamp,
    changed_by                      text
);


create table if not exists labels_history
(
    id                              integer generated always as identity primary key,
    labels_id                       integer                                 not null,
    label_key                       TEXT,
    label_text                      TEXT,
    language_id                     integer                 references languages(id),
    section_name                    TEXT,
    created_on                      timestamp                               not null,
    created_by                      text                                    not null,
    changed_on                      timestamp,
    changed_by                      text,
    action                          char,
    action_timestamp                timestamp                               not null
);

CREATE OR REPLACE FUNCTION labels_audit()
    RETURNS TRIGGER LANGUAGE plpgsql AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO labels_history (labels_id, label_key, label_text, language_id, section_name, created_on, created_by,
                                    changed_on, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.label_key, OLD.label_text, OLD.language_id, OLD.section_name, OLD.created_on, OLD.created_by,
                OLD.changed_on, OLD.changed_by, 'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO labels_history (labels_id, label_key, label_text, language_id, section_name, created_on, created_by,
                                    changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.label_key, NEW.label_text, NEW.language_id, NEW.section_name, NEW.created_on, NEW.created_by,
                NEW.changed_on, NEW.changed_by, 'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO labels_history (labels_id, label_key, label_text, language_id, section_name, created_on, created_by,
                                    changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.label_key, NEW.label_text, NEW.language_id, NEW.section_name, NEW.created_on, NEW.created_by,
                NEW.changed_on, NEW.changed_by, 'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

alter function labels_audit() owner to ibosng;

create trigger labels_insert
    after insert
    on labels
    for each row
execute procedure labels_audit();

create trigger labels_update
    after update
    on labels
    for each row
execute procedure labels_audit();

create trigger labels_delete
    after delete
    on labels
    for each row
execute procedure labels_audit();
