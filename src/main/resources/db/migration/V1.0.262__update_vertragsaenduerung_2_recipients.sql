alter table vertragsaenderung_2_recipients
    drop column email_id;
alter table vertragsaenderung_2_recipients_history
    drop column email_id;

alter table vertragsaenderung_2_recipients
    drop column recipient_type;
alter table vertragsaenderung_2_recipients_history
    drop column recipient_type;

alter table vertragsaenderung_2_recipients
    add column benutzer_id integer references benutzer (id);
alter table vertragsaenderung_2_recipients_history
    add column benutzer_id integer;


CREATE OR REPLACE FUNCTION vertragsaenderung_2_recipients_audit() RETURNS TRIGGER AS
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO vertragsaenderung_2_recipients_history (vertragsaenderung_2_recipients_id, benutzer_id, vd_id,
                                                            action, action_timestamp)
        VALUES (OLD.id, OLD.benutzer_id, OLD.vd_id, 'D', NOW());
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO vertragsaenderung_2_recipients_history (vertragsaenderung_2_recipients_id, benutzer_id, vd_id,
                                                            action, action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.vd_id, 'U', NOW());
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO vertragsaenderung_2_recipients_history (vertragsaenderung_2_recipients_id, benutzer_id, vd_id,
                                                            action, action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.vd_id, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;