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
                                                                                                          ('mitarbeiter.erfassen.stammdaten.button.anlegen', 'Mitarbeiter anlegen', language_id_de, 'anlegen', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.stammdaten.button.anlegen', 'Create Participant', language_id_en, 'anlegen', current_user, current_timestamp);
    END $$;


update labels set label_key = 'mitarbeiter.erfassen.placeholder.stammdaten.mobilnummer' where label_key = 'mitarbeiter.erfassen.placeholder.stammdaten.email' and label_text = 'Mobilnummer';
update labels set label_key = 'mitarbeiter.erfassen.placeholder.stammdaten.mobilnummer' where label_key = 'mitarbeiter.erfassen.placeholder.stammdaten.email' and label_text = 'mobile phone';
update labels set label_key = 'navigation.mitarbeiter.label' where label_key = 'navigation.mitarbeiter';
update labels set label_key = 'navigation.abmelden.label' where label_key = 'teilnehmer.header.abmelden';

