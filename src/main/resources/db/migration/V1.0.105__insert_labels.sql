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
                                                                                                          ('teilnehmer.bearbeiten.error.teilnehmerExistiertBereits', 'Teilnehmer mit dieser SV-Nr. existiert bereits', language_id_de, 'teilnehmer', current_user, current_timestamp);

        -- Insert English translations
        INSERT INTO labels (label_key, label_text, language_id, section_name, created_by, created_on) VALUES
                                                                                                          ('teilnehmer.bearbeiten.error.teilnehmerExistiertBereits', 'Participant with this SV number already exists', language_id_en, 'teilnehmer', current_user, current_timestamp);
    END $$;
