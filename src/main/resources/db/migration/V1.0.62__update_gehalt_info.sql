alter table vertragsdaten rename column jobBezeichnung to job_bezeichnung;
alter table vertragsdaten_history rename column jobBezeichnung to job_bezeichnung;

alter table vertragsdaten rename column notizAllgemein to notiz_allgemein;
alter table vertragsdaten_history rename column notizAllgemein to notiz_allgemein;

alter table vertragsdaten rename column mobileWorking to mobile_working;
alter table vertragsdaten_history rename column mobileWorking to mobile_working;

alter table vertragsdaten rename column weitereAdressezuHauptwohnsitz to weitere_adresse_zu_hauptwohnsitz;
alter table vertragsdaten_history rename column weitereAdressezuHauptwohnsitz to weitere_adresse_zu_hauptwohnsitz;

create or replace function vertragsdaten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           job_bezeichnung, notiz_allgemein, mobile_working,
                                           weitere_adresse_zu_hauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.eintritt, OLD.is_befristet, OLD.befristung_bis,
                OLD.dienstort, OLD.kostenstelle, OLD.fuerhrungskraft, OLD.startcoach, OLD.kategorie,
                OLD.taetigkeit, OLD.job_bezeichnung, OLD.notiz_allgemein, OLD.mobile_working,
                OLD.weitere_adresse_zu_hauptwohnsitz, OLD.strasse, OLD.land, OLD.status, OLD.created_on,
                OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           job_bezeichnung, notiz_allgemein, mobile_working,
                                           weitere_adresse_zu_hauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.is_befristet, NEW.befristung_bis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.job_bezeichnung, NEW.notiz_allgemein, NEW.mobile_working,
                NEW.weitere_adresse_zu_hauptwohnsitz, NEW.strasse, NEW.land, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, is_befristet,
                                           befristung_bis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           job_bezeichnung, notiz_allgemein, mobile_working,
                                           weitere_adresse_zu_hauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.is_befristet, NEW.befristung_bis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.job_bezeichnung, NEW.notiz_allgemein, NEW.mobile_working,
                NEW.weitere_adresse_zu_hauptwohnsitz, NEW.strasse, NEW.land, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

alter function vertragsdaten_audit() owner to ibosng;





alter table arbeitszeiten_info rename column arbeitszeitmodellVon to arbeitszeitmodell_von;
alter table arbeitszeiten_info_history rename column arbeitszeitmodellVon to arbeitszeitmodell_von;

alter table arbeitszeiten_info rename column arbeitszeitmodellBis to arbeitszeitmodell_bis;
alter table arbeitszeiten_info_history rename column arbeitszeitmodellBis to arbeitszeitmodell_bis;

alter table arbeitszeiten_info rename column auswahlBegruendungFuerDurchrechner to auswahl_begruendung_fuer_durchrechner;
alter table arbeitszeiten_info_history rename column auswahlBegruendungFuerDurchrechner to auswahl_begruendung_fuer_durchrechner;

alter table arbeitszeiten_info rename column spezielleMittagspausenregelung to spezielle_mittagspausenregelung;
alter table arbeitszeiten_info_history rename column spezielleMittagspausenregelung to spezielle_mittagspausenregelung;

alter table arbeitszeiten_info rename column urlaubVorabVereinbart to urlaub_vorab_vereinbart;
alter table arbeitszeiten_info_history rename column urlaubVorabVereinbart to urlaub_vorab_vereinbart;

alter table arbeitszeiten_info rename column notizArbeitszeit to notiz_arbeitszeit;
alter table arbeitszeiten_info_history rename column notizArbeitszeit to notiz_arbeitszeit;



create or replace function arbeitszeiten_info_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO arbeitszeiten_info_history (arbeitszeiten_info_id, vertragsdaten_id, stundenaenderung,
                                                verwendungsbereichsaenderung,
                                                stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung,
                                                beschaeftigungsausmass,
                                                beschaeftigungsstatus, wochenstunden, arbeitszeitmodell,
                                                arbeitszeitmodell_von,
                                                arbeitszeitmodell_bis, auswahl_begruendung_fuer_durchrechner,
                                                spezielle_mittagspausenregelung,
                                                urlaub_vorab_vereinbart, notiz_arbeitszeit, status, created_on, created_by,
                                                changed_on,
                                                changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.stundenaenderung, OLD.verwendungsbereichsaenderung,
                OLD.stufenwechsel, OLD.geschaeftsbereichsaenderung, OLD.kvErhoehung, OLD.beschaeftigungsausmass,
                OLD.beschaeftigungsstatus, OLD.wochenstunden, OLD.arbeitszeitmodell, OLD.arbeitszeitmodell_von,
                OLD.arbeitszeitmodell_bis, OLD.auswahl_begruendung_fuer_durchrechner, OLD.spezielle_mittagspausenregelung,
                OLD.urlaub_vorab_vereinbart, OLD.notiz_arbeitszeit, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_on,
                OLD.changed_by, 'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO arbeitszeiten_info_history (arbeitszeiten_info_id, vertragsdaten_id, stundenaenderung,
                                                verwendungsbereichsaenderung,
                                                stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung,
                                                beschaeftigungsausmass,
                                                beschaeftigungsstatus, wochenstunden, arbeitszeitmodell,
                                                arbeitszeitmodell_von,
                                                arbeitszeitmodell_bis, auswahl_begruendung_fuer_durchrechner,
                                                spezielle_mittagspausenregelung,
                                                urlaub_vorab_vereinbart, notiz_arbeitszeit, status, created_on, created_by,
                                                changed_on,
                                                changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.stundenaenderung, NEW.verwendungsbereichsaenderung,
                NEW.stufenwechsel, NEW.geschaeftsbereichsaenderung, NEW.kvErhoehung, NEW.beschaeftigungsausmass,
                NEW.beschaeftigungsstatus, NEW.wochenstunden, NEW.arbeitszeitmodell, NEW.arbeitszeitmodell_von,
                NEW.arbeitszeitmodell_bis, NEW.auswahl_begruendung_fuer_durchrechner, NEW.spezielle_mittagspausenregelung,
                NEW.urlaub_vorab_vereinbart, NEW.notiz_arbeitszeit, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO arbeitszeiten_info_history (arbeitszeiten_info_id, vertragsdaten_id, stundenaenderung,
                                                verwendungsbereichsaenderung,
                                                stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung,
                                                beschaeftigungsausmass,
                                                beschaeftigungsstatus, wochenstunden, arbeitszeitmodell,
                                                arbeitszeitmodell_von,
                                                arbeitszeitmodell_bis, auswahl_begruendung_fuer_durchrechner,
                                                spezielle_mittagspausenregelung,
                                                urlaub_vorab_vereinbart, notiz_arbeitszeit, status, created_on, created_by,
                                                changed_on, changed_by,
                                                action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.stundenaenderung, NEW.verwendungsbereichsaenderung,
                NEW.stufenwechsel, NEW.geschaeftsbereichsaenderung, NEW.kvErhoehung, NEW.beschaeftigungsausmass,
                NEW.beschaeftigungsstatus, NEW.wochenstunden, NEW.arbeitszeitmodell, NEW.arbeitszeitmodell_von,
                NEW.arbeitszeitmodell_bis, NEW.auswahl_begruendung_fuer_durchrechner, NEW.spezielle_mittagspausenregelung,
                NEW.urlaub_vorab_vereinbart, NEW.notiz_arbeitszeit, NEW.status, NEW.created_on, NEW.created_by, NULL,
                NULL, 'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

alter function arbeitszeiten_info_audit() owner to ibosng;




alter table gehalt_info rename column facheinschlaegigeTaetigkeitenGeprueft to facheinschlaegige_taetigkeiten_geprueft;
alter table gehalt_info_history rename column facheinschlaegigeTaetigkeitenGeprueft to facheinschlaegige_taetigkeiten_geprueft;

alter table gehalt_info rename column angerechneteIbisJahre to angerechnete_ibis_jahre;
alter table gehalt_info_history rename column angerechneteIbisJahre to angerechnete_ibis_jahre;

alter table gehalt_info rename column angerechneteFacheinschlaegigeTaetigkeitenJahre to angerechnete_facheinschlaegige_taetigkeiten_monate;
alter table gehalt_info_history rename column angerechneteFacheinschlaegigeTaetigkeitenJahre to angerechnete_facheinschlaegige_taetigkeiten_monate;

alter table gehalt_info add column angerechnete_freie_taetigkeiten_monate INTEGER;
alter table gehalt_info_history add column angerechnete_freie_taetigkeiten_monate INTEGER;

alter table gehalt_info add column naechste_stufe_datum DATE;
alter table gehalt_info_history add column naechste_stufe_datum DATE;

alter table gehalt_info rename column kvGehaltBerechnet to kv_gehalt_berechnet;
alter table gehalt_info_history rename column kvGehaltBerechnet to kv_gehalt_berechnet;

alter table gehalt_info rename column gehaltVereinbart to gehalt_vereinbart;
alter table gehalt_info_history rename column gehaltVereinbart to gehalt_vereinbart;

alter table gehalt_info rename column zulageInEuro to zulage_in_euro;
alter table gehalt_info_history rename column zulageInEuro to zulage_in_euro;

alter table gehalt_info rename column artDerZulage to art_der_zulage;
alter table gehalt_info_history rename column artDerZulage to art_der_zulage;

alter table gehalt_info rename column gesamtBrutto to gesamt_brutto;
alter table gehalt_info_history rename column gesamtBrutto to gesamt_brutto;

alter table gehalt_info rename column vereinbarungUEberstunden to vereinbarung_ueberstunden;
alter table gehalt_info_history rename column vereinbarungUEberstunden to vereinbarung_ueberstunden;

alter table gehalt_info rename column uestPauschale to uest_pauschale;
alter table gehalt_info_history rename column uestPauschale to uest_pauschale;

alter table gehalt_info rename column notizGehalt to notiz_gehalt;
alter table gehalt_info_history rename column notizGehalt to notiz_gehalt;



create or replace function gehalt_info_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegige_taetigkeiten_geprueft, angerechnete_ibis_jahre,
                                         angerechnete_facheinschlaegige_taetigkeiten_monate,
                                         angerechnete_freie_taetigkeiten_monate, naechsteStufeDatum,
                                         kv_gehalt_berechnet, gehalt_vereinbart, ueberzahlung, zulage_in_euro,
                                         art_der_zulage, gesamt_brutto, vereinbarung_ueberstunden,
                                         uest_pauschale, deckungspruefung, jobticket, notiz_gehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.kollektivvertrag, OLD.verwendungsgruppe,
                OLD.stufe, OLD.facheinschlaegige_taetigkeiten_geprueft, OLD.angerechnete_ibis_jahre,
                OLD.angerechnete_facheinschlaegige_taetigkeiten_monate,
                OLD.angerechnete_freie_taetigkeiten_monate, OLD.naechsteStufeDatum,
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
                                         angerechnete_freie_taetigkeiten_monate, naechsteStufeDatum,
                                         kv_gehalt_berechnet, gehalt_vereinbart, ueberzahlung, zulage_in_euro,
                                         art_der_zulage, gesamt_brutto, vereinbarung_ueberstunden,
                                         uest_pauschale, deckungspruefung, jobticket, notiz_gehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegige_taetigkeiten_geprueft, NEW.angerechnete_ibis_jahre,
                NEW.angerechnete_facheinschlaegige_taetigkeiten_monate,
                NEW.angerechnete_freie_taetigkeiten_monate, NEW.naechsteStufeDatum,
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
                                         angerechnete_freie_taetigkeiten_monate, naechsteStufeDatum,
                                         kv_gehalt_berechnet, gehalt_vereinbart, ueberzahlung, zulage_in_euro,
                                         art_der_zulage, gesamt_brutto, vereinbarung_ueberstunden,
                                         uest_pauschale, deckungspruefung, jobticket, notiz_gehalt, status, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegige_taetigkeiten_geprueft, NEW.angerechnete_ibis_jahre,
                NEW.angerechnete_facheinschlaegige_taetigkeiten_monate,
                NEW.angerechnete_freie_taetigkeiten_monate, NEW.naechsteStufeDatum,
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

