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
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.amontagNetto', 'Montag Nettoarbeitszeit', language_id_de, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.adienstagNetto', 'Dienstag Nettoarbeitszeit', language_id_de, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.amittwochNetto', 'Mittwoch Nettoarbeitszeit', language_id_de, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.adonnerstagNetto', 'Donnerstag Nettoarbeitszeit', language_id_de, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.afreitagNetto', 'Freitag Nettoarbeitszeit', language_id_de, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.asamstagNetto', 'Samstag Nettoarbeitszeit', language_id_de, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.asonntagNetto', 'Sonntag Nettoarbeitszeit', language_id_de, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.asonntagVon', 'Sonntag von', language_id_de, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.asonntagBis', 'Sonntag bis', language_id_de, 'vertragsdaten', current_user, current_timestamp);

        -- Insert English translations
        INSERT INTO labels (label_key, label_text, language_id, section_name, created_by, created_on) VALUES
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.amontagNetto', 'Monday net working hours', language_id_en, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.adienstagNetto', 'Tuesday net working hours', language_id_en, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.amittwochNetto', 'Wednesday net working hours', language_id_en, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.adonnerstagNetto', 'Thursday net working hours', language_id_en, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.afreitagNetto', 'Friday net working hours', language_id_en, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.asamstagNetto', 'Saturday net working hours', language_id_en, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.asonntagNetto', 'Sunday net working hours', language_id_en, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.asonntagVon', 'Sunday from', language_id_en, 'vertragsdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.vertragsdaten.asonntagBis', 'Sunday until', language_id_en, 'vertragsdaten', current_user, current_timestamp);
    END $$;
