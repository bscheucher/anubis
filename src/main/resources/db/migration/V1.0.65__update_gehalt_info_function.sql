create or replace function gehalt_info_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegige_taetigkeiten_geprueft, angerechnete_ibis_jahre,
                                         angerechnete_facheinschlaegige_taetigkeiten_monate,
                                         angerechnete_freie_taetigkeiten_monate, naechste_stufe_datum,
                                         kv_gehalt_berechnet, gehalt_vereinbart, ueberzahlung, zulage_in_euro,
                                         art_der_zulage, gesamt_brutto, vereinbarung_ueberstunden,
                                         uest_pauschale, deckungspruefung, jobticket, notiz_gehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.kollektivvertrag, OLD.verwendungsgruppe,
                OLD.stufe, OLD.facheinschlaegige_taetigkeiten_geprueft, OLD.angerechnete_ibis_jahre,
                OLD.angerechnete_facheinschlaegige_taetigkeiten_monate,
                OLD.angerechnete_freie_taetigkeiten_monate, OLD.naechste_stufe_datum,
                OLD.kv_gehalt_berechnet, OLD.gehalt_vereinbart, OLD.ueberzahlung, OLD.zulage_in_euro,
                OLD.art_der_zulage, OLD.gesamt_brutto, OLD.vereinbarung_ueberstunden,
                OLD.uest_pauschale, OLD.deckungspruefung, OLD.jobticket, OLD.notiz_gehalt, OLD.status, OLD.created_on,
                OLD.created_by,
                OLD.changed_on,
                OLD.changed_by, 'D', CURRENT_TIMESTAMP);
RETURN OLD;
ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegige_taetigkeiten_geprueft, angerechnete_ibis_jahre,
                                         angerechnete_facheinschlaegige_taetigkeiten_monate,
                                         angerechnete_freie_taetigkeiten_monate, naechste_stufe_datum,
                                         kv_gehalt_berechnet, gehalt_vereinbart, ueberzahlung, zulage_in_euro,
                                         art_der_zulage, gesamt_brutto, vereinbarung_ueberstunden,
                                         uest_pauschale, deckungspruefung, jobticket, notiz_gehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegige_taetigkeiten_geprueft, NEW.angerechnete_ibis_jahre,
                NEW.angerechnete_facheinschlaegige_taetigkeiten_monate,
                NEW.angerechnete_freie_taetigkeiten_monate, NEW.naechste_stufe_datum,
                NEW.kv_gehalt_berechnet, NEW.gehalt_vereinbart, NEW.ueberzahlung, NEW.zulage_in_euro,
                NEW.art_der_zulage, NEW.gesamt_brutto, NEW.vereinbarung_ueberstunden,
                NEW.uest_pauschale, NEW.deckungspruefung, NEW.jobticket, NEW.notiz_gehalt, NEW.status, NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'U', CURRENT_TIMESTAMP);
RETURN NEW;
ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegige_taetigkeiten_geprueft, angerechnete_ibis_jahre,
                                         angerechnete_facheinschlaegige_taetigkeiten_monate,
                                         angerechnete_freie_taetigkeiten_monate, naechste_stufe_datum,
                                         kv_gehalt_berechnet, gehalt_vereinbart, ueberzahlung, zulage_in_euro,
                                         art_der_zulage, gesamt_brutto, vereinbarung_ueberstunden,
                                         uest_pauschale, deckungspruefung, jobticket, notiz_gehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegige_taetigkeiten_geprueft, NEW.angerechnete_ibis_jahre,
                NEW.angerechnete_facheinschlaegige_taetigkeiten_monate,
                NEW.angerechnete_freie_taetigkeiten_monate, NEW.naechste_stufe_datum,
                NEW.kv_gehalt_berechnet, NEW.gehalt_vereinbart, NEW.ueberzahlung, NEW.zulage_in_euro,
                NEW.art_der_zulage, NEW.gesamt_brutto, NEW.vereinbarung_ueberstunden,
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
