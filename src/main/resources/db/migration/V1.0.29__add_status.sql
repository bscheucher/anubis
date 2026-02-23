ALTER TABLE arbeitszeiten_info
    add column status smallint not null default 0;
ALTER TABLE arbeitszeiten_info_history
    add column status smallint not null default 0;

CREATE OR REPLACE FUNCTION arbeitszeiten_info_audit()
    RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO arbeitszeiten_info_history (arbeitszeiten_info_id, vertragsdaten_id, stundenaenderung,
                                                verwendungsbereichsaenderung,
                                                stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung,
                                                beschaeftigungsausmass,
                                                beschaeftigungsstatus, wochenstunden, arbeitszeitmodell,
                                                arbeitszeitmodellVon,
                                                arbeitszeitmodellBis, auswahlBegruendungFuerDurchrechner,
                                                spezielleMittagspausenregelung,
                                                urlaubVorabVereinbart, notizArbeitszeit, status, created_on, created_by,
                                                changed_on,
                                                changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.stundenaenderung, OLD.verwendungsbereichsaenderung,
                OLD.stufenwechsel, OLD.geschaeftsbereichsaenderung, OLD.kvErhoehung, OLD.beschaeftigungsausmass,
                OLD.beschaeftigungsstatus, OLD.wochenstunden, OLD.arbeitszeitmodell, OLD.arbeitszeitmodellVon,
                OLD.arbeitszeitmodellBis, OLD.auswahlBegruendungFuerDurchrechner, OLD.spezielleMittagspausenregelung,
                OLD.urlaubVorabVereinbart, OLD.notizArbeitszeit, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_on,
                OLD.changed_by, 'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO arbeitszeiten_info_history (arbeitszeiten_info_id, vertragsdaten_id, stundenaenderung,
                                                verwendungsbereichsaenderung,
                                                stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung,
                                                beschaeftigungsausmass,
                                                beschaeftigungsstatus, wochenstunden, arbeitszeitmodell,
                                                arbeitszeitmodellVon,
                                                arbeitszeitmodellBis, auswahlBegruendungFuerDurchrechner,
                                                spezielleMittagspausenregelung,
                                                urlaubVorabVereinbart, notizArbeitszeit, status, created_on, created_by,
                                                changed_on,
                                                changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.stundenaenderung, NEW.verwendungsbereichsaenderung,
                NEW.stufenwechsel, NEW.geschaeftsbereichsaenderung, NEW.kvErhoehung, NEW.beschaeftigungsausmass,
                NEW.beschaeftigungsstatus, NEW.wochenstunden, NEW.arbeitszeitmodell, NEW.arbeitszeitmodellVon,
                NEW.arbeitszeitmodellBis, NEW.auswahlBegruendungFuerDurchrechner, NEW.spezielleMittagspausenregelung,
                NEW.urlaubVorabVereinbart, NEW.notizArbeitszeit, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO arbeitszeiten_info_history (arbeitszeiten_info_id, vertragsdaten_id, stundenaenderung,
                                                verwendungsbereichsaenderung,
                                                stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung,
                                                beschaeftigungsausmass,
                                                beschaeftigungsstatus, wochenstunden, arbeitszeitmodell,
                                                arbeitszeitmodellVon,
                                                arbeitszeitmodellBis, auswahlBegruendungFuerDurchrechner,
                                                spezielleMittagspausenregelung,
                                                urlaubVorabVereinbart, notizArbeitszeit, status, created_on, created_by,
                                                changed_on, changed_by,
                                                action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.stundenaenderung, NEW.verwendungsbereichsaenderung,
                NEW.stufenwechsel, NEW.geschaeftsbereichsaenderung, NEW.kvErhoehung, NEW.beschaeftigungsausmass,
                NEW.beschaeftigungsstatus, NEW.wochenstunden, NEW.arbeitszeitmodell, NEW.arbeitszeitmodellVon,
                NEW.arbeitszeitmodellBis, NEW.auswahlBegruendungFuerDurchrechner, NEW.spezielleMittagspausenregelung,
                NEW.urlaubVorabVereinbart, NEW.notizArbeitszeit, NEW.status, NEW.created_on, NEW.created_by, NULL,
                NULL, 'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;



ALTER TABLE gehalt_info
    add column status smallint not null default 0;
ALTER TABLE gehalt_info_history
    add column status smallint not null default 0;



CREATE OR REPLACE FUNCTION gehalt_info_audit()
    RETURNS TRIGGER
    LANGUAGE plpgsql AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegigeTaetigkeitenGeprueft, angerechneteIbisJahre,
                                         angerechneteFacheinschlaegigeTaetigkeitenJahre,
                                         kvGehaltBerechnet, gehaltVereinbart, ueberzahlung, zulageInEuro,
                                         artDerZulage, gesamtBrutto, vereinbarungUEberstunden,
                                         uestPauschale, deckungspruefung, jobticket, notizGehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.kollektivvertrag, OLD.verwendungsgruppe,
                OLD.stufe, OLD.facheinschlaegigeTaetigkeitenGeprueft, OLD.angerechneteIbisJahre,
                OLD.angerechneteFacheinschlaegigeTaetigkeitenJahre,
                OLD.kvGehaltBerechnet, OLD.gehaltVereinbart, OLD.ueberzahlung, OLD.zulageInEuro,
                OLD.artDerZulage, OLD.gesamtBrutto, OLD.vereinbarungUEberstunden,
                OLD.uestPauschale, OLD.deckungspruefung, OLD.jobticket, OLD.notizGehalt, OLD.status, OLD.created_on,
                OLD.created_by,
                OLD.changed_on,
                OLD.changed_by, 'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegigeTaetigkeitenGeprueft, angerechneteIbisJahre,
                                         angerechneteFacheinschlaegigeTaetigkeitenJahre,
                                         kvGehaltBerechnet, gehaltVereinbart, ueberzahlung, zulageInEuro,
                                         artDerZulage, gesamtBrutto, vereinbarungUEberstunden,
                                         uestPauschale, deckungspruefung, jobticket, notizGehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegigeTaetigkeitenGeprueft, NEW.angerechneteIbisJahre,
                NEW.angerechneteFacheinschlaegigeTaetigkeitenJahre,
                NEW.kvGehaltBerechnet, NEW.gehaltVereinbart, NEW.ueberzahlung, NEW.zulageInEuro,
                NEW.artDerZulage, NEW.gesamtBrutto, NEW.vereinbarungUEberstunden,
                NEW.uestPauschale, NEW.deckungspruefung, NEW.jobticket, NEW.notizGehalt, NEW.status, NEW.created_on,
                NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegigeTaetigkeitenGeprueft, angerechneteIbisJahre,
                                         angerechneteFacheinschlaegigeTaetigkeitenJahre,
                                         kvGehaltBerechnet, gehaltVereinbart, ueberzahlung, zulageInEuro,
                                         artDerZulage, gesamtBrutto, vereinbarungUEberstunden,
                                         uestPauschale, deckungspruefung, jobticket, notizGehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegigeTaetigkeitenGeprueft, NEW.angerechneteIbisJahre,
                NEW.angerechneteFacheinschlaegigeTaetigkeitenJahre,
                NEW.kvGehaltBerechnet, NEW.gehaltVereinbart, NEW.ueberzahlung, NEW.zulageInEuro,
                NEW.artDerZulage, NEW.gesamtBrutto, NEW.vereinbarungUEberstunden,
                NEW.uestPauschale, NEW.deckungspruefung, NEW.jobticket, NEW.notizGehalt, NEW.status, NEW.created_on,
                NEW.created_by,
                NULL,
                NULL, 'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;
