create table if not exists file_import_headers
(
    id               integer generated always as identity primary key,
    file_type        smallint  not null     unique,
    active_headers   text      not null,
    inactive_headers text,
    version          text,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_on       timestamp,
    changed_by       text
);

create table if not exists file_import_headers_history
(
    id                         integer generated always as identity primary key,
    file_import_headers_id     integer   not null,
    file_type                  smallint  not null,
    active_headers             text      not null,
    inactive_headers           text,
    version                    text,
    created_on                 timestamp default CURRENT_TIMESTAMP not null,
    created_by                 text                                not null,
    changed_on                 timestamp,
    changed_by                 text,
    action                     char,
    action_timestamp           timestamp not null
);

create or replace function file_import_headers_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO file_import_headers_history (file_import_headers_id, file_type, active_headers, inactive_headers, version,
                                                     created_on, created_by, changed_by,
                                                     action,
                                                     action_timestamp)
        VALUES (OLD.id, OLD.file_type, OLD.active_headers, OLD.inactive_headers, OLD.version,
                OLD.created_on, OLD.created_by, OLD.changed_by,'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO file_import_headers_history (file_import_headers_id, file_type, active_headers, inactive_headers, version,
                                                     created_on, created_by, changed_by,
                                                     action,
                                                     action_timestamp)
        VALUES (NEW.id, NEW.file_type, NEW.active_headers, NEW.inactive_headers, NEW.version,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO file_import_headers_history (file_import_headers_id, file_type, active_headers, inactive_headers, version,
                                                     created_on, created_by, changed_by,
                                                     action,
                                                     action_timestamp)
        VALUES (NEW.id, NEW.file_type, NEW.active_headers, NEW.inactive_headers, NEW.version,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function file_import_headers_audit() owner to ibosng;

create trigger file_import_headers_insert
    after insert
    on file_import_headers
    for each row
execute procedure file_import_headers_audit();

create trigger file_import_headers_update
    after update
    on file_import_headers
    for each row
execute procedure file_import_headers_audit();

create trigger file_import_headers_delete
    after delete
    on file_import_headers
    for each row
execute procedure file_import_headers_audit();

INSERT INTO public.file_import_headers(
	file_type, active_headers, "version", created_by)
	VALUES (0, 'Titel,Familien-/Nachname,Vorname,Geschlecht,SV-Nummer,Geburtsdatum,PLZ,Ort,Straße,Landesvorwahl,Vorwahl,Telefon-Nr,Buchungsstatus,Anmerkung,Zubuchung,geplant,Eintritt,Austritt,RGS,Titel Betreuer,Familien-/Nachname Betreuer,Vorname Betreuer,Maßnahmennummer,Veranstaltungsnummer,eMail-Adresse,Seminar ID','1','DB Service');

INSERT INTO public.file_import_headers(
	file_type, active_headers, "version", created_by)
	VALUES (1, 'Titel,Familien-/Nachname,Vorname,Geschlecht,SV-Nummer,Geburtsdatum,PLZ,Ort,Straße,Landesvorwahl,Vorwahl,Telefon-Nr,Buchungsstatus,Anmerkung,Zubuchung,geplant,Eintritt,Austritt,RGS,Titel Betreuer,Familien-/Nachname Betreuer,Vorname Betreuer,Maßnahmennummer,Veranstaltungsnummer,eMail-Adresse','1','DB Service');

INSERT INTO public.file_import_headers(
	file_type, active_headers, "version", created_by)
	VALUES (2, 'INFO,Vorname,Familienname,Geschlecht,SVNr,PLZ,Ort,Straße,Telefon,Nation,RGS,Anmerkung','1','DB Service');

INSERT INTO public.file_import_headers(
    file_type, active_headers, inactive_headers, "version", created_by)
    VALUES (3, 'Kennung,Nachname,Vorname,Geschlecht,Geburtsdatum,Teilnahmebeginn,Anmeldestatus,Individuelles Niveau,Email,Mobiltelefon,Sozialversicherungsnummer,AMS,Abmeldegrund,Anwesenheit in UE,Entschuldigte Abwesenheit in UE,Unentschuldigte Abwesenheit in UE,Summe erfasste An- und Abwesenheit,Anwesenheit erfasst bis (inkl.),Absolute Anwesenheit in %,Relative Anwesenheit in %,Seminar ID','KB 0 - 2 Jahre,KB 3 - 5 Jahre,KB ab 6 Jahren','1','DB Service');
