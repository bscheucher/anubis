-- Remove old Zulage
ALTER TABLE gehalt_info
DROP COLUMN zulage_in_euro,
DROP COLUMN art_der_zulage;

ALTER TABLE gehalt_info_history
DROP COLUMN zulage_in_euro,
DROP COLUMN art_der_zulage;


-- Create new tables and triggers
create table if not exists gehalt_info_zulage
(
    id             integer generated always as identity primary key,
    gehalt_info_id       integer references gehalt_info(id),
    zulage_in_euro NUMERIC,
    art_der_zulage text,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_on     timestamp,
    changed_by     text
    );

create table if not exists gehalt_info_zulage_history
(
    id             integer generated always as identity primary key,
    gehalt_info_id       integer not null,
    zulage_in_euro NUMERIC,
    art_der_zulage text,
    created_on     timestamp  not null,
    created_by     text       not null,
    changed_on     timestamp,
    changed_by     text,
    action           char,
    action_timestamp timestamp not null
    );

CREATE OR REPLACE FUNCTION gehalt_info_zulage_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehalt_info_zulage_history (gehalt_info_id, zulage_in_euro, art_der_zulage, created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (OLD.gehalt_info_id, OLD.zulage_in_euro, OLD.art_der_zulage, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', NOW());
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO gehalt_info_zulage_history (gehalt_info_id, zulage_in_euro, art_der_zulage, created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.gehalt_info_id, NEW.zulage_in_euro, NEW.art_der_zulage, NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', NOW());
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO gehalt_info_zulage_history (gehalt_info_id, zulage_in_euro, art_der_zulage, created_on, created_by, changed_on, changed_by, action, action_timestamp)
        VALUES (NEW.gehalt_info_id, NEW.zulage_in_euro, NEW.art_der_zulage, NEW.created_on, NEW.created_by, NULL, NULL, 'I', NOW());
RETURN NEW;
END IF;
RETURN NULL;
END;
$$;

ALTER FUNCTION gehalt_info_zulage_audit() OWNER TO ibosng;

CREATE TRIGGER gehalt_info_zulage_insert
    AFTER INSERT
    ON gehalt_info_zulage
    FOR EACH ROW
    EXECUTE PROCEDURE gehalt_info_zulage_audit();

CREATE TRIGGER gehalt_info_zulage_update
    AFTER UPDATE
    ON gehalt_info_zulage
    FOR EACH ROW
    EXECUTE PROCEDURE gehalt_info_zulage_audit();

CREATE TRIGGER gehalt_info_zulage_delete
    AFTER DELETE
    ON gehalt_info_zulage
    FOR EACH ROW
    EXECUTE PROCEDURE gehalt_info_zulage_audit();


create or replace function gehalt_info_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegige_taetigkeiten_geprueft, angerechnete_ibis_monate,
                                         angerechnete_facheinschlaegige_taetigkeiten_monate,
                                         angerechnete_freie_taetigkeiten_monate, naechste_stufe_datum,
                                         kv_gehalt_berechnet, gehalt_vereinbart, ueberzahlung,
                                         gesamt_brutto, vereinbarung_ueberstunden,
                                         uest_pauschale, deckungspruefung, jobticket, notiz_gehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.kollektivvertrag, OLD.verwendungsgruppe,
                OLD.stufe, OLD.facheinschlaegige_taetigkeiten_geprueft, OLD.angerechnete_ibis_monate,
                OLD.angerechnete_facheinschlaegige_taetigkeiten_monate,
                OLD.angerechnete_freie_taetigkeiten_monate, OLD.naechste_stufe_datum,
                OLD.kv_gehalt_berechnet, OLD.gehalt_vereinbart, OLD.ueberzahlung,
                 OLD.gesamt_brutto, OLD.vereinbarung_ueberstunden,
                OLD.uest_pauschale, OLD.deckungspruefung, OLD.jobticket, OLD.notiz_gehalt, OLD.status, OLD.created_on,
                OLD.created_by,
                OLD.changed_on,
                OLD.changed_by, 'D', CURRENT_TIMESTAMP);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegige_taetigkeiten_geprueft, angerechnete_ibis_monate,
                                         angerechnete_facheinschlaegige_taetigkeiten_monate,
                                         angerechnete_freie_taetigkeiten_monate, naechste_stufe_datum,
                                         kv_gehalt_berechnet, gehalt_vereinbart, ueberzahlung,
                                         gesamt_brutto, vereinbarung_ueberstunden,
                                         uest_pauschale, deckungspruefung, jobticket, notiz_gehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegige_taetigkeiten_geprueft, NEW.angerechnete_ibis_monate,
                NEW.angerechnete_facheinschlaegige_taetigkeiten_monate,
                NEW.angerechnete_freie_taetigkeiten_monate, NEW.naechste_stufe_datum,
                NEW.kv_gehalt_berechnet, NEW.gehalt_vereinbart, NEW.ueberzahlung,
                 NEW.gesamt_brutto, NEW.vereinbarung_ueberstunden,
                NEW.uest_pauschale, NEW.deckungspruefung, NEW.jobticket, NEW.notiz_gehalt, NEW.status, NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'U', CURRENT_TIMESTAMP);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegige_taetigkeiten_geprueft, angerechnete_ibis_monate,
                                         angerechnete_facheinschlaegige_taetigkeiten_monate,
                                         angerechnete_freie_taetigkeiten_monate, naechste_stufe_datum,
                                         kv_gehalt_berechnet, gehalt_vereinbart, ueberzahlung,
                                         gesamt_brutto, vereinbarung_ueberstunden,
                                         uest_pauschale, deckungspruefung, jobticket, notiz_gehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegige_taetigkeiten_geprueft, NEW.angerechnete_ibis_monate,
                NEW.angerechnete_facheinschlaegige_taetigkeiten_monate,
                NEW.angerechnete_freie_taetigkeiten_monate, NEW.naechste_stufe_datum,
                NEW.kv_gehalt_berechnet, NEW.gehalt_vereinbart, NEW.ueberzahlung,
                 NEW.gesamt_brutto, NEW.vereinbarung_ueberstunden,
                NEW.uest_pauschale, NEW.deckungspruefung, NEW.jobticket, NEW.notiz_gehalt, NEW.status, NEW.created_on,
                NEW.created_by,
                NULL,
                NULL, 'I', CURRENT_TIMESTAMP);
RETURN NEW;
END IF;
RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

alter function gehalt_info_audit() owner to ibosng;