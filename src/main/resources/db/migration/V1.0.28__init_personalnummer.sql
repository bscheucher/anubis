create table if not exists personalnummer
(
    id         integer generated always as identity primary key,
    kostenstelle       integer references kostenstelle(id),
    firma      integer references ibis_firma(id),
    nummer		integer,
    personalnummer TEXT,
    status     smallint  not null,
    created_on timestamp not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists personalnummer_history
(
    id               integer generated always as identity primary key,
    personalnummer_id    integer   not null,
    kostenstelle             integer,
    firma       integer,
    nummer		integer,
    personalnummer TEXT,
    status           smallint  not null,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function personalnummer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO personalnummer_history (personalnummer_id, kostenstelle, firma, nummer, personalnummer, status, created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (OLD.id, OLD.kostenstelle, OLD.firma, OLD.nummer, OLD.personalnummer, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO personalnummer_history (personalnummer_id, kostenstelle, firma, nummer, personalnummer, status, created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.kostenstelle, NEW.firma, NEW.nummer, NEW.personalnummer, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO personalnummer_history (personalnummer_id, kostenstelle, firma, nummer, personalnummer, status, created_on, created_by,
                                            changed_by,
                                            action,
                                            action_timestamp)
        VALUES (NEW.id, NEW.kostenstelle, NEW.firma, NEW.nummer, NEW.personalnummer, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function personalnummer_audit() owner to ibosng;

create trigger personalnummer_insert
    after insert
    on personalnummer
    for each row
execute procedure personalnummer_audit();

create trigger personalnummer_update
    after update
    on personalnummer
    for each row
execute procedure personalnummer_audit();

create trigger personalnummer_delete
    after delete
    on personalnummer
    for each row
execute procedure personalnummer_audit();

ALTER TABLE stammdaten ADD COLUMN personalnummer INTEGER REFERENCES personalnummer(id);
ALTER TABLE stammdaten_history ADD COLUMN personalnummer INTEGER;


CREATE OR REPLACE FUNCTION stammdaten_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, bank,
                                        arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.anrede, OLD.titel, OLD.titel2, OLD.nachname, OLD.vorname,
                OLD.geburtsname, OLD.svnr, OLD.ecard, OLD.geschlecht, OLD.familienstand,
                OLD.geburtsdatum, OLD.lebensalter, OLD.staatsbuergerschaft, OLD.muttersprache,
                OLD.adresse, OLD.email, OLD.mobilnummer, OLD.bank,
                OLD.arbeitsgenehmigung, OLD.gueltigBis, OLD.arbeitsgenehmigungDok, OLD.foto,
                OLD.status, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, bank,
                                        arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.bank,
                NEW.arbeitsgenehmigung, NEW.gueltigBis, NEW.arbeitsgenehmigungDok, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, now(), NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO stammdaten_history (stammdaten_id, personalnummer, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, bank,
                                        arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.bank,
                NEW.arbeitsgenehmigung, NEW.gueltigBis, NEW.arbeitsgenehmigungDok, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, NULL, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;



ALTER TABLE vertragsdaten
    DROP COLUMN personalnummer;

ALTER TABLE vertragsdaten_history
    DROP COLUMN personalnummer;

ALTER TABLE vertragsdaten
    ADD COLUMN personalnummer INTEGER references personalnummer(id);

ALTER TABLE vertragsdaten_history
    ADD COLUMN personalnummer INTEGER;


CREATE OR REPLACE FUNCTION vertragsdaten_audit()
    RETURNS TRIGGER LANGUAGE plpgsql AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, befristungVon,
                                           befristungBis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           jobBezeichnung, notizAllgemein, mobileWorking,
                                           weitereAdressezuHauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.eintritt, OLD.befristungVon, OLD.befristungBis,
                OLD.dienstort, OLD.kostenstelle, OLD.fuerhrungskraft, OLD.startcoach, OLD.kategorie,
                OLD.taetigkeit, OLD.jobBezeichnung, OLD.notizAllgemein, OLD.mobileWorking,
                OLD.weitereAdressezuHauptwohnsitz, OLD.strasse, OLD.land, OLD.status, OLD.created_on,
                OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, befristungVon,
                                           befristungBis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           jobBezeichnung, notizAllgemein, mobileWorking,
                                           weitereAdressezuHauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.befristungVon, NEW.befristungBis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.jobBezeichnung, NEW.notizAllgemein, NEW.mobileWorking,
                NEW.weitereAdressezuHauptwohnsitz, NEW.strasse, NEW.land, OLD.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalnummer, eintritt, befristungVon,
                                           befristungBis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           jobBezeichnung, notizAllgemein, mobileWorking,
                                           weitereAdressezuHauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.eintritt, NEW.befristungVon, NEW.befristungBis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.jobBezeichnung, NEW.notizAllgemein, NEW.mobileWorking,
                NEW.weitereAdressezuHauptwohnsitz, NEW.strasse, NEW.land, OLD.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;