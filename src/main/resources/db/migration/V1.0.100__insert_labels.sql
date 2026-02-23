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
                                                                                                          ('mitarbeiter.erfassen.placeholder.stammdaten.email', 'Mobilnummer', language_id_de, 'stammdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragdaten.title', 'Vertragsdaten', language_id_de, 'vertragdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.navigator.step.optional.sonstiger', 'Wartet', language_id_de, 'sonstiger', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.placeholder.stammdaten.email', 'mobile phone', language_id_en, 'stammdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.vertragdaten.title', 'Contract data', language_id_en, 'vertragdaten', current_user, current_timestamp),
                                                                                                          ('mitarbeiter.erfassen.navigator.step.optional.sonstiger', 'Pending', language_id_en, 'sonstiger', current_user, current_timestamp);
    END $$;


update labels set label_key = 'navigation.teilnehmer.korrigieren.label' where label_key = 'navigation.teilnehmer.korrigieren';
update labels set label_key = 'navigation.teilnehmer.label' where label_key = 'navigation.teilnehmer';
update labels set label_key = 'login.button.anmelden.label' where label_key = 'login.button.anmelden';


update labels set label_text = 'Teilnehmer Anlegen' where label_key = 'teilnehmer.verwalten.button.anlegen' and language_id = (SELECT id language_id_de FROM languages WHERE name = 'german');
update labels set label_text = 'Create Participant' where label_key = 'teilnehmer.verwalten.button.anlegen' and language_id = (SELECT id language_id_de FROM languages WHERE name = 'english');

update labels set label_text = 'Warte auf Eingabe' where label_key = 'mitarbeiter.erfassen.navigator.step.optional.stammdatenVertragsdaten.new' and language_id = (SELECT id language_id_de FROM languages WHERE name = 'german');
update labels set label_text = 'Waiting for input' where label_key = 'mitarbeiter.erfassen.navigator.step.optional.stammdatenVertragsdaten.new' and language_id = (SELECT id language_id_de FROM languages WHERE name = 'english');

update labels set label_text = 'In Arbeit' where label_key = 'mitarbeiter.erfassen.navigator.step.optional.stammdatenVertragsdaten.inProgress' and language_id = (SELECT id language_id_de FROM languages WHERE name = 'german');
update labels set label_text = 'In progress' where label_key = 'mitarbeiter.erfassen.navigator.step.optional.stammdatenVertragsdaten.inProgress' and language_id = (SELECT id language_id_de FROM languages WHERE name = 'english');

update labels set label_text = 'Angefordert' where label_key = 'mitarbeiter.erfassen.navigator.step.optional.pruefungLv.inProgress' and language_id = (SELECT id language_id_de FROM languages WHERE name = 'german');
update labels set label_text = 'Requested' where label_key = 'mitarbeiter.erfassen.navigator.step.optional.pruefungLv.inProgress' and language_id = (SELECT id language_id_de FROM languages WHERE name = 'english');
