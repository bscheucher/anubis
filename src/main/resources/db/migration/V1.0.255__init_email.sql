create table if not exists email_recipients
(
    id                   integer generated always as identity primary key,
    email                varchar(255),
    created_on           timestamp default CURRENT_TIMESTAMP not null,
    created_by           text,
    changed_on           timestamp,
    changed_by           text
);

create table if not exists email_recipients_history
(
    id                   integer generated always as identity primary key,
    email_id             integer,
    email                varchar(255),
    created_on           timestamp default CURRENT_TIMESTAMP not null,
    created_by           text,
    changed_on           timestamp,
    changed_by           text,
    action               char,
    action_timestamp     timestamp not null
);

CREATE OR REPLACE FUNCTION email_recipients_audit() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO email_recipients_history (email_id, email, created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.email, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO email_recipients_history (email_id, email, created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.email, NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', NOW());
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO email_recipients_history (email_id, email, created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.email, NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER email_recipients_insert
    AFTER INSERT ON email_recipients
    FOR EACH ROW
    EXECUTE FUNCTION email_recipients_audit();

CREATE TRIGGER email_recipients_update
    AFTER UPDATE ON email_recipients
    FOR EACH ROW
    EXECUTE FUNCTION email_recipients_audit();

CREATE TRIGGER email_recipients_delete
    AFTER DELETE ON email_recipients
    FOR EACH ROW
    EXECUTE FUNCTION email_recipients_audit();

create table if not exists vertragsaenderung_2_recipients
(
    id                   integer generated always as identity primary key,
    email_id             integer references email_recipients (id),
    vd_id                integer references vertragsaenderung (id),
    recipient_type       varchar(255)
);

create table if not exists vertragsaenderung_2_recipients_history
(
    id                   integer generated always as identity primary key,
    vertragsaenderung_2_recipients_id          integer,
    email_id             integer references email_recipients (id),
    vd_id                integer references vertragsaenderung (id),
    recipient_type       varchar(255),
    action               char,
    action_timestamp     timestamp not null
);

CREATE OR REPLACE FUNCTION vertragsaenderung_2_recipients_audit() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO vertragsaenderung_2_recipients_history (vertragsaenderung_2_recipients_id, email_id, vd_id, recipient_type, action, action_timestamp)
        VALUES (OLD.id, OLD.email_id, OLD.vd_id, OLD.recipient_type, 'D', NOW());
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO vertragsaenderung_2_recipients_history (vertragsaenderung_2_recipients_id, email_id, vd_id, recipient_type, action, action_timestamp)
        VALUES (NEW.id, NEW.email_id, NEW.vd_id, NEW.recipient_type, 'U', NOW());
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO vertragsaenderung_2_recipients_history (vertragsaenderung_2_recipients_id, email_id, vd_id, recipient_type, action, action_timestamp)
        VALUES (NEW.id, NEW.email_id, NEW.vd_id, NEW.recipient_type, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER vertragsaenderung_2_recipients_insert
    AFTER INSERT ON vertragsaenderung_2_recipients
    FOR EACH ROW
    EXECUTE FUNCTION vertragsaenderung_2_recipients_audit();

CREATE TRIGGER vertragsaenderung_2_recipients_update
    AFTER UPDATE ON vertragsaenderung_2_recipients
    FOR EACH ROW
    EXECUTE FUNCTION vertragsaenderung_2_recipients_audit();

CREATE TRIGGER vertragsaenderung_2_recipients_delete
    AFTER DELETE ON vertragsaenderung_2_recipients
    FOR EACH ROW
    EXECUTE FUNCTION vertragsaenderung_2_recipients_audit();