create or replace function teilnehmer_staging_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_staging_history (teilnehmer_staging_id, titel, vorname, nachname, geschlecht, sv_nummer,
                                                geburtsdatum,
                                                buchungsstatus, anmerkung, zubuchung, geplant, eintritt, austritt,
                                                teilnahme_von, teilnahme_bis, rgs,
                                                massnahmennummer,
                                                veranstaltungsnummer, email, import_filename, betreuer_titel,
                                                betreuer_vorname,
                                                betreuer_nachname, telefon, status, plz, ort, strasse, nation, info,
                                                seminar_identifier,
                                                seminar_start_date, seminar_end_date, seminar_start_time, seminar_type,
                                                trainer,
                                                landesvorwahl, vorwahl, telefon_nummer, teilnahmebeginn,
                                                anmeldestatus, ams, abmeldegrund, anwesenheit_in_ue,
                                                entschuldigte_abwesenheit_in_ue, unentschuldigte_abwesenheit_in_ue,
                                                summe_erfasste_an_und_abwesenheit,
                                                anwesenheit_erfasst_bis, absolute_anwesenheit,
                                                relative_anwesenheit, source, teilnehmer_id, created_on, created_by,
                                                changed_by,
                                                action,
                                                action_timestamp, anrede)
        VALUES (OLD.id, OLD.titel, OLD.vorname, OLD.nachname, OLD.geschlecht, OLD.sv_nummer, OLD.geburtsdatum,
                OLD.buchungsstatus, OLD.anmerkung, OLD.zubuchung, OLD.geplant, OLD.eintritt, OLD.austritt,
                OLD.teilnahme_von, OLD.teilnahme_bis, OLD.rgs,
                OLD.massnahmennummer, OLD.veranstaltungsnummer, OLD.email, OLD.import_filename, OLD.betreuer_titel,
                OLD.betreuer_vorname, OLD.betreuer_nachname, OLD.telefon, OLD.status, OLD.plz, OLD.ort, OLD.strasse,
                OLD.nation, OLD.info, OLD.seminar_identifier, OLD.seminar_start_date, OLD.seminar_end_date,
                OLD.seminar_start_time,
                OLD.seminar_type, OLD.trainer, OLD.landesvorwahl, OLD.vorwahl, OLD.telefon_nummer,
                OLD.teilnahmebeginn, OLD.anmeldestatus, OLD.ams, OLD.abmeldegrund,
                OLD.anwesenheit_in_ue, OLD.entschuldigte_abwesenheit_in_ue, OLD.unentschuldigte_abwesenheit_in_ue,
                OLD.summe_erfasste_an_und_abwesenheit,
                OLD.anwesenheit_erfasst_bis, OLD.absolute_anwesenheit, OLD.relative_anwesenheit, OLD.source,
                OLD.teilnehmer_id,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now(), OLD.anrede);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_staging_history (teilnehmer_staging_id, titel, vorname, nachname, geschlecht, sv_nummer,
                                                geburtsdatum,
                                                buchungsstatus, anmerkung, zubuchung, geplant, eintritt, austritt,
                                                teilnahme_von, teilnahme_bis, rgs,
                                                massnahmennummer,
                                                veranstaltungsnummer, email, import_filename, betreuer_titel,
                                                betreuer_vorname,
                                                betreuer_nachname, telefon, status, plz, ort, strasse, nation, info,
                                                seminar_identifier,
                                                seminar_start_date, seminar_end_date, seminar_start_time, seminar_type,
                                                trainer,
                                                landesvorwahl, vorwahl, telefon_nummer, teilnahmebeginn,
                                                anmeldestatus, ams, abmeldegrund, anwesenheit_in_ue,
                                                entschuldigte_abwesenheit_in_ue, unentschuldigte_abwesenheit_in_ue,
                                                summe_erfasste_an_und_abwesenheit,
                                                anwesenheit_erfasst_bis, absolute_anwesenheit,
                                                relative_anwesenheit, source, teilnehmer_id, created_on, created_by,
                                                changed_by,
                                                action,
                                                action_timestamp, anrede)
        VALUES (NEW.id, NEW.titel, NEW.vorname, NEW.nachname, NEW.geschlecht, NEW.sv_nummer, NEW.geburtsdatum,
                NEW.buchungsstatus, NEW.anmerkung, NEW.zubuchung, NEW.geplant, NEW.eintritt, NEW.austritt,
                NEW.teilnahme_von, NEW.teilnahme_bis, NEW.rgs,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.email, NEW.import_filename, NEW.betreuer_titel,
                NEW.betreuer_vorname, NEW.betreuer_nachname, NEW.telefon, NEW.status, NEW.plz, NEW.ort, NEW.strasse,
                NEW.nation, NEW.info, NEW.seminar_identifier, NEW.seminar_start_date, NEW.seminar_end_date,
                NEW.seminar_start_time,
                NEW.seminar_type, NEW.trainer, NEW.landesvorwahl, NEW.vorwahl, NEW.telefon_nummer,
                NEW.teilnahmebeginn, NEW.anmeldestatus, NEW.ams, NEW.abmeldegrund,
                NEW.anwesenheit_in_ue, NEW.entschuldigte_abwesenheit_in_ue, NEW.unentschuldigte_abwesenheit_in_ue,
                NEW.summe_erfasste_an_und_abwesenheit,
                NEW.anwesenheit_erfasst_bis, NEW.absolute_anwesenheit, NEW.relative_anwesenheit, NEW.source,
                NEW.teilnehmer_id,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now(), NEW.anrede);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_staging_history (teilnehmer_staging_id, titel, vorname, nachname, geschlecht, sv_nummer,
                                                geburtsdatum,
                                                buchungsstatus, anmerkung, zubuchung, geplant, eintritt, austritt,
                                                teilnahme_von, teilnahme_bis, rgs,
                                                massnahmennummer,
                                                veranstaltungsnummer, email, import_filename, betreuer_titel,
                                                betreuer_vorname,
                                                betreuer_nachname, telefon, status, plz, ort, strasse, nation, info,
                                                seminar_identifier,
                                                seminar_start_date, seminar_end_date, seminar_start_time, seminar_type,
                                                trainer,
                                                landesvorwahl, vorwahl, telefon_nummer, teilnahmebeginn,
                                                anmeldestatus, ams, abmeldegrund, anwesenheit_in_ue,
                                                entschuldigte_abwesenheit_in_ue, unentschuldigte_abwesenheit_in_ue,
                                                summe_erfasste_an_und_abwesenheit,
                                                anwesenheit_erfasst_bis, absolute_anwesenheit,
                                                relative_anwesenheit, source, teilnehmer_id, created_on, created_by,
                                                changed_by,
                                                action,
                                                action_timestamp, anrede)
        VALUES (NEW.id, NEW.titel, NEW.vorname, NEW.nachname, NEW.geschlecht, NEW.sv_nummer, NEW.geburtsdatum,
                NEW.buchungsstatus, NEW.anmerkung, NEW.zubuchung, NEW.geplant, NEW.eintritt, NEW.austritt,
                NEW.teilnahme_von, NEW.teilnahme_bis, NEW.rgs,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.email, NEW.import_filename, NEW.betreuer_titel,
                NEW.betreuer_vorname, NEW.betreuer_nachname, NEW.telefon, NEW.status, NEW.plz, NEW.ort, NEW.strasse,
                NEW.nation, NEW.info, NEW.seminar_identifier, NEW.seminar_start_date, NEW.seminar_end_date,
                NEW.seminar_start_time,
                NEW.seminar_type, NEW.trainer, NEW.landesvorwahl, NEW.vorwahl, NEW.telefon_nummer,
                NEW.teilnahmebeginn, NEW.anmeldestatus, NEW.ams, NEW.abmeldegrund,
                NEW.anwesenheit_in_ue, NEW.entschuldigte_abwesenheit_in_ue, NEW.unentschuldigte_abwesenheit_in_ue,
                NEW.summe_erfasste_an_und_abwesenheit,
                NEW.anwesenheit_erfasst_bis, NEW.absolute_anwesenheit, NEW.relative_anwesenheit, NEW.source,
                NEW.teilnehmer_id,
                NEW.created_on, NEW.created_by, NULL, 'I', now(), NEW.anrede);
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function teilnehmer_staging_audit() owner to ibosng;


