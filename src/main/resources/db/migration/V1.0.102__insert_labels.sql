DO $$
    DECLARE
        language_id_de INTEGER;
        language_id_en INTEGER;
    BEGIN
        -- Get the language_id for 'german'
        SELECT id INTO language_id_de FROM languages WHERE name = 'german';

        -- Get the language_id for 'english'
        SELECT id INTO language_id_en FROM languages WHERE name = 'english';

        -- Insert German translations
        INSERT INTO labels (label_key, label_text, language_id, section_name, created_by, created_on) VALUES
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.allgemein', 'Allgemein', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.vordienstzeiten', 'Vordienstzeiten', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.arbeitszeit', 'Arbeitszeit', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.gehalt', 'Gehalt', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.zusatzvereinbarung', 'Zusatzvereinbarung', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.unterhaltsberechtigte', 'Unterhaltsberechtigte', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.button.save.label', 'Speichern', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.text.herunterladen', 'Datei herunterladen', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.text.hochladen', 'Upload', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.checkbox.prefix', 'Nein', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.checkbox.suffix', 'Ja', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.text', 'Validierung fehlgeschlagen', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.dateiZugross.text', 'Datei überschreitet die Größe von 10 MB', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.ungueltig.text', 'Ungültige Eingabe', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.erforderlich.text', 'Dieses Feld ist erforderlich', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.vonVorBis.text', 'Von-Datum muss vor Bis-Datum sein', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.text.download', 'Download', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.text.hinzufuegen', 'Hinzufügen', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.arbeitszeiten', 'Arbeitszeiten', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.artDerZulage', 'Art der Zulage', language_id_de, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.kernzeit', 'Kernzeit', language_id_de, 'mitarbeiter', current_user, current_timestamp);

        -- Insert English translations
        INSERT INTO labels (label_key, label_text, language_id, section_name, created_by, created_on) VALUES
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.allgemein', 'General', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.vordienstzeiten', 'Previous Service Periods', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.arbeitszeit', 'Working Hours', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.gehalt', 'Salary', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.zusatzvereinbarung', 'Additional Agreement', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragsdaten.tab.unterhaltsberechtigte', 'Dependents', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.button.save.label', 'Save', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.text.hochladen', 'Upload', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.checkbox.prefix', 'No', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.checkbox.suffix', 'Yes', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.text', 'Validation failed', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.dateiZugross.text', 'File exceeds the size of 10 MB', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.ungueltig.text', 'Invalid input', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.erforderlich.text', 'This field is required', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.message.error.vonVorBis.text', 'From-date must be before To-date', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.text.download', 'Download', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.form.text.hinzufuegen', 'Add', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.arbeitszeiten', 'Working hours', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.artDerZulage', 'Type of allowance', language_id_en, 'mitarbeiter', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.kernzeit', 'Core time', language_id_en, 'mitarbeiter', current_user, current_timestamp);
    END $$;



update labels set label_key = 'teilnehmer.bearbeiten.required.vorname.text' where label_key = 'teilnehmer.bearbeiten.required.vorname';
update labels set label_key = 'teilnehmer.bearbeiten.required.nachname.text' where label_key = 'teilnehmer.bearbeiten.required.nachname';
update labels set label_key = 'teilnehmer.bearbeiten.required.svnr.text' where label_key = 'teilnehmer.bearbeiten.required.svnr';
update labels set label_key = 'teilnehmer.bearbeiten.required.geschlecht.text' where label_key = 'teilnehmer.bearbeiten.required.geschlecht';
update labels set label_key = 'teilnehmer.bearbeiten.required.geburtsdatum.text' where label_key = 'teilnehmer.bearbeiten.required.geburtsdatum';
update labels set label_key = 'teilnehmer.bearbeiten.required.staatsbuergerschaft.text' where label_key = 'teilnehmer.bearbeiten.required.staatsbuergerschaft';
update labels set label_key = 'teilnehmer.bearbeiten.required.telefonPrivat.text' where label_key = 'teilnehmer.bearbeiten.required.telefon';
update labels set label_key = 'teilnehmer.bearbeiten.required.email.text' where label_key = 'teilnehmer.bearbeiten.required.email';
update labels set label_key = 'teilnehmer.bearbeiten.required.strasse.text' where label_key = 'teilnehmer.bearbeiten.required.strasse';
update labels set label_key = 'teilnehmer.bearbeiten.required.plz.text' where label_key = 'teilnehmer.bearbeiten.required.plz';
update labels set label_key = 'teilnehmer.bearbeiten.required.plz.ort.text' where label_key = 'teilnehmer.bearbeiten.required.ort';

