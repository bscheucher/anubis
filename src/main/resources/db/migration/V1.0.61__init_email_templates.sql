create table if not exists email_template
(
    id         integer generated always as identity primary key,
    language   integer references languages (id),
    identifier text,
    subject    text,
    body       text,
    status     smallint  not null,
    created_on timestamp not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists email_template_history
(
    id                integer generated always as identity primary key,
    email_template_id integer   not null,
    language          integer,
    identifier        text,
    subject           text,
    body              text,
    status            smallint  not null,
    created_on        timestamp not null,
    created_by        text      not null,
    changed_on        timestamp,
    changed_by        text,
    action            char,
    action_timestamp  timestamp not null
);

create or replace function email_template_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO email_template_history (email_template_id, language, identifier, subject, body, status,
                                            created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (OLD.id, OLD.language, OLD.identifier, OLD.subject, OLD.body, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO email_template_history (email_template_id, language, identifier, subject, body, status,
                                            created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.language, NEW.identifier, NEW.subject, NEW.body, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO email_template_history (email_template_id, language, identifier, subject, body, status,
                                            created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.language, NEW.identifier, NEW.subject, NEW.body, NEW.status, NEW.created_on, NEW.created_by,
                NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function email_template_audit() owner to ibosng;

create trigger email_template_insert
    after insert
    on email_template
    for each row
execute procedure email_template_audit();

create trigger email_template_update
    after update
    on email_template
    for each row
execute procedure email_template_audit();

create trigger email_template_delete
    after delete
    on email_template
    for each row
execute procedure email_template_audit();

INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('gateway-service.lhr.neuer-ma',
        (select id from languages where name = 'german'),
        'Neue/r Mitarbeiter/in - Daten zur Überprüfung',
        'Liebes Lohnverrechnungsteam,<br><br>' ||
        'Für einen neuen Mitarbeiter wurden Stamm- und Vertragsdaten erfasst. ' ||
        'Bitte diese Daten in ibosNG überprüfen.<br><br>',
        1,
        current_user, current_timestamp);

-- Fehlerhaftes Excel-Dateiformat
INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('file-import-service.error.excel.invalid-format',
        (SELECT id FROM languages WHERE name = 'german'),
        'Fehlerhaftes Excel-Dateiformat: %s',
        'Die hochgeladene Excel-Datei %s weist ein fehlerhaftes Format auf und konnte nicht verarbeitet werden.<br><br>',
        1,
        current_user, current_timestamp);

-- Fehlerhaftes CSV-Dateiformat
INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('file-import-service.error.csv.invalid-format',
        (SELECT id FROM languages WHERE name = 'german'),
        'Fehlerhaftes CSV-Dateiformat: %s',
        'Die hochgeladene CSV-Datei %s weist ein fehlerhaftes Format auf und konnte nicht verarbeitet werden.<br><br>',
        1,
        current_user, current_timestamp);

-- Teilnehmerdaten nicht in AMS-Datei gefunden
INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('file-import-service.error.participant-data.not-found-in-ams',
        (SELECT id FROM languages WHERE name = 'german'),
        'Teilnehmerdaten nicht in AMS-Datei gefunden',
        'Die Teilnehmerdaten aus der Datei %s wurden nicht in der AMS-Datei %s gefunden. Bitte überprüfen Sie die Datensätze.<br><br>',
        1,
        current_user, current_timestamp);

-- Falscher Dateityp
INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('file-import-service.error.file.wrong-type',
        (SELECT id FROM languages WHERE name = 'german'),
        'Falscher Dateityp: %s',
        'Falscher Dateityp: Die Datei %s wurde in den Fehlerordner verschoben.<br><br>',
        1,
        current_user, current_timestamp);

-- Keine eAMS-Datei gefunden
INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('file-import-service.error.eams-file.not-found',
        (SELECT id FROM languages WHERE name = 'german'),
        'Keine eAMS-Datei gefunden: %s',
        'Für die VHS-Datei %s konnte keine eAMS-Datei gefunden werden.<br><br>',
        1,
        current_user, current_timestamp);

-- Teilnehmer mit fehlerhaften Daten
INSERT INTO email_template (identifier, language, subject, body, status, created_by, created_on)
VALUES ('validation-service.error.participant.invalid-data',
        (SELECT id FROM languages WHERE name = 'german'),
        'Teilnehmer mit fehlerhaften Daten',
        'Teilnehmer mit fehlerhaften Daten wurden im File %s importiert.<br><br>',
        1,
        current_user, current_timestamp);
