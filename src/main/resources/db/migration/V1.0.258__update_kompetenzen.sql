ALTER TABLE kompetenz
    ADD COLUMN pin_code VARCHAR(255);

ALTER TABLE kompetenz
    ADD COLUMN confidence_pin_code DOUBLE PRECISION;

ALTER TABLE kompetenz
    ADD COLUMN tagesdatum DATE;

ALTER TABLE kompetenz
    ADD COLUMN confidence_tagesdatum DOUBLE PRECISION;

ALTER TABLE kompetenz_history
    ADD COLUMN pin_code VARCHAR(255);

ALTER TABLE kompetenz_history
    ADD COLUMN confidence_pin_code DOUBLE PRECISION;

ALTER TABLE kompetenz_history
    ADD COLUMN tagesdatum DATE;

ALTER TABLE kompetenz_history
    ADD COLUMN confidence_tagesdatum DOUBLE PRECISION;


CREATE
    OR REPLACE FUNCTION kompetenz_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
        TG_OP = 'DELETE' THEN
        INSERT INTO kompetenz_history (kompetenz_id, teilnehmer_id, art, name, confidence_name, score, confidence_score,
                                       pin_code, confidence_pin_code,tagesdatum,
                                       confidence_tagesdatum,
                                       created_on,created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (OLD.id,
                OLD.teilnehmer_id,
                OLD.art,
                OLD.name,
                OLD.confidence_name,
                OLD.score,
                OLD.confidence_score,
                OLD.pin_code,
                OLD.confidence_pin_code,
                OLD.tagesdatum,
                OLD.confidence_tagesdatum,
                OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF
        TG_OP = 'UPDATE' THEN
        INSERT INTO kompetenz_history (kompetenz_id, teilnehmer_id, art, name, confidence_name, score, confidence_score,
                                       pin_code, confidence_pin_code,tagesdatum,
                                       confidence_tagesdatum,
                                       created_on,created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.id,
                NEW.teilnehmer_id,
                NEW.art,
                NEW.name,
                NEW.confidence_name,
                NEW.score,
                NEW.confidence_score,
                NEW.pin_code,
                NEW.confidence_pin_code,
                NEW.tagesdatum,
                NEW.confidence_tagesdatum,
                NEW.created_on, NEW.created_by, NEW.changed_on,
                NEW.changed_by, 'U', NOW());
        RETURN NEW;
    ELSIF
        TG_OP = 'INSERT' THEN
        INSERT INTO kompetenz_history (kompetenz_id, teilnehmer_id, art, name, confidence_name, score, confidence_score,
                                       pin_code, confidence_pin_code,tagesdatum,
                                       confidence_tagesdatum, created_on,created_by, changed_on, changed_by,
                                       action, action_timestamp)
        VALUES (NEW.id,
                NEW.teilnehmer_id,
                NEW.art,
                NEW.name,
                NEW.confidence_name,
                NEW.score,
                NEW.confidence_score,
                NEW.pin_code,
                NEW.confidence_pin_code,
                NEW.tagesdatum,
                NEW.confidence_tagesdatum,
                NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', NOW());

        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

ALTER FUNCTION kompetenz_audit() OWNER TO ibosng;



