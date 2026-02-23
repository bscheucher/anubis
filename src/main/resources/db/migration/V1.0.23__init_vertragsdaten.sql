--VERTRAGSDATEN!!!!!!!!!!!!!!!!!!!!!!!!!
CREATE TABLE IF NOT EXISTS vertragsdaten
(
    id                            integer generated always as identity primary key,
    personalNummer                TEXT,
    eintritt                      DATE,
    befristungVon                 DATE,
    befristungBis                 DATE,
    dienstort                     TEXT,
    kostenstelle                  TEXT,
    fuerhrungskraft               BOOLEAN,
    startcoach                    BOOLEAN,
    kategorie                     integer references kategorie (id),
    taetigkeit                    integer references taetigkeit (id),
    jobBezeichnung                integer references jobbeschreibung (id),
    notizAllgemein                TEXT,
    mobileWorking                 BOOLEAN,
    weitereAdressezuHauptwohnsitz BOOLEAN,
    strasse                       TEXT,
    land                          integer references land (id),
    status                smallint  default 0                 not null,
    created_on                    timestamp not null,
    created_by                    text      not null,
    changed_on                    timestamp,
    changed_by                    text
);


create table if not exists vertragsdaten_history
(
    id                            integer generated always as identity primary key,
    vertragsdaten_id              integer   not null,
    personalNummer                TEXT,
    eintritt                      DATE,
    befristungVon                 DATE,
    befristungBis                 DATE,
    dienstort                     TEXT,
    kostenstelle                  TEXT,
    fuerhrungskraft               BOOLEAN,
    startcoach                    BOOLEAN,
    kategorie                     integer,
    taetigkeit                    integer,
    jobBezeichnung                integer,
    notizAllgemein                TEXT,
    mobileWorking                 BOOLEAN,
    weitereAdressezuHauptwohnsitz BOOLEAN,
    strasse                       TEXT,
    land                          integer,
    status                smallint  not null,
    created_on                    timestamp not null,
    created_by                    text      not null,
    changed_on                    timestamp,
    changed_by                    text,
    action                        char,
    action_timestamp              timestamp not null
);

CREATE OR REPLACE FUNCTION vertragsdaten_audit()
    RETURNS TRIGGER LANGUAGE plpgsql AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalNummer, eintritt, befristungVon,
                                           befristungBis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           jobBezeichnung, notizAllgemein, mobileWorking,
                                           weitereAdressezuHauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.personalNummer, OLD.eintritt, OLD.befristungVon, OLD.befristungBis,
                OLD.dienstort, OLD.kostenstelle, OLD.fuerhrungskraft, OLD.startcoach, OLD.kategorie,
                OLD.taetigkeit, OLD.jobBezeichnung, OLD.notizAllgemein, OLD.mobileWorking,
                OLD.weitereAdressezuHauptwohnsitz, OLD.strasse, OLD.land, OLD.status, OLD.created_on,
                OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalNummer, eintritt, befristungVon,
                                           befristungBis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           jobBezeichnung, notizAllgemein, mobileWorking,
                                           weitereAdressezuHauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalNummer, NEW.eintritt, NEW.befristungVon, NEW.befristungBis,
                NEW.dienstort, NEW.kostenstelle, NEW.fuerhrungskraft, NEW.startcoach, NEW.kategorie,
                NEW.taetigkeit, NEW.jobBezeichnung, NEW.notizAllgemein, NEW.mobileWorking,
                NEW.weitereAdressezuHauptwohnsitz, NEW.strasse, NEW.land, OLD.status, NEW.created_on,
                NEW.created_by, NEW.changed_on, NEW.changed_by,
                'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsdaten_history (vertragsdaten_id, personalNummer, eintritt, befristungVon,
                                           befristungBis,
                                           dienstort, kostenstelle, fuerhrungskraft, startcoach, kategorie,
                                           taetigkeit,
                                           jobBezeichnung, notizAllgemein, mobileWorking,
                                           weitereAdressezuHauptwohnsitz, strasse, land, status, created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.personalNummer, NEW.eintritt, NEW.befristungVon, NEW.befristungBis,
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

alter function vertragsdaten_audit() owner to ibosng;

create trigger vertragsdaten_insert
    after insert
    on vertragsdaten
    for each row
execute procedure vertragsdaten_audit();

create trigger vertragsdaten_update
    after update
    on vertragsdaten
    for each row
execute procedure vertragsdaten_audit();

create trigger vertragsdaten_delete
    after delete
    on vertragsdaten
    for each row
execute procedure vertragsdaten_audit();



CREATE TABLE IF NOT EXISTS arbeitszeiten_info
(
    id                                 SERIAL PRIMARY KEY,
    vertragsdaten_id                   INTEGER REFERENCES vertragsdaten (id),
    stundenaenderung                   VARCHAR(255),
    verwendungsbereichsaenderung       VARCHAR(255),
    stufenwechsel                      VARCHAR(255),
    geschaeftsbereichsaenderung        VARCHAR(255),
    kvErhoehung                        BOOLEAN,
    beschaeftigungsausmass             VARCHAR(255),
    beschaeftigungsstatus              VARCHAR(255),
    wochenstunden                      VARCHAR(255),
    arbeitszeitmodell                  VARCHAR(255),
    arbeitszeitmodellVon               DATE,
    arbeitszeitmodellBis               DATE,
    auswahlBegruendungFuerDurchrechner TEXT,
    spezielleMittagspausenregelung     TEXT,
    urlaubVorabVereinbart              BOOLEAN,
    notizArbeitszeit                   TEXT,
    created_on                         timestamp not null,
    created_by                         text      not null,
    changed_on                         timestamp,
    changed_by                         text
);


create table if not exists arbeitszeiten_info_history
(
    id                                 integer generated always as identity primary key,
    arbeitszeiten_info_id              integer   not null,
    vertragsdaten_id                   INTEGER,
    stundenaenderung                   VARCHAR(255),
    verwendungsbereichsaenderung       VARCHAR(255),
    stufenwechsel                      VARCHAR(255),
    geschaeftsbereichsaenderung        VARCHAR(255),
    kvErhoehung                        BOOLEAN,
    beschaeftigungsausmass             VARCHAR(255),
    beschaeftigungsstatus              VARCHAR(255),
    wochenstunden                      VARCHAR(255),
    arbeitszeitmodell                  VARCHAR(255),
    arbeitszeitmodellVon               DATE,
    arbeitszeitmodellBis               DATE,
    auswahlBegruendungFuerDurchrechner TEXT,
    spezielleMittagspausenregelung     TEXT,
    urlaubVorabVereinbart              BOOLEAN,
    notizArbeitszeit                   TEXT,
    created_on                         timestamp not null,
    created_by                         text      not null,
    changed_on                         timestamp,
    changed_by                         text,
    action                             char,
    action_timestamp                   timestamp not null
);



CREATE OR REPLACE FUNCTION arbeitszeiten_info_audit()
    RETURNS TRIGGER LANGUAGE plpgsql AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO arbeitszeiten_info_history (arbeitszeiten_info_id, vertragsdaten_id, stundenaenderung,
                                                verwendungsbereichsaenderung,
                                                stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung,
                                                beschaeftigungsausmass,
                                                beschaeftigungsstatus, wochenstunden, arbeitszeitmodell,
                                                arbeitszeitmodellVon,
                                                arbeitszeitmodellBis, auswahlBegruendungFuerDurchrechner,
                                                spezielleMittagspausenregelung,
                                                urlaubVorabVereinbart, notizArbeitszeit, created_on, created_by,
                                                changed_on,
                                                changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.stundenaenderung, OLD.verwendungsbereichsaenderung,
                OLD.stufenwechsel, OLD.geschaeftsbereichsaenderung, OLD.kvErhoehung, OLD.beschaeftigungsausmass,
                OLD.beschaeftigungsstatus, OLD.wochenstunden, OLD.arbeitszeitmodell, OLD.arbeitszeitmodellVon,
                OLD.arbeitszeitmodellBis, OLD.auswahlBegruendungFuerDurchrechner, OLD.spezielleMittagspausenregelung,
                OLD.urlaubVorabVereinbart, OLD.notizArbeitszeit, OLD.created_on, OLD.created_by, OLD.changed_on,
                OLD.changed_by, 'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO arbeitszeiten_info_history (arbeitszeiten_info_id, vertragsdaten_id, stundenaenderung,
                                                verwendungsbereichsaenderung,
                                                stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung,
                                                beschaeftigungsausmass,
                                                beschaeftigungsstatus, wochenstunden, arbeitszeitmodell,
                                                arbeitszeitmodellVon,
                                                arbeitszeitmodellBis, auswahlBegruendungFuerDurchrechner,
                                                spezielleMittagspausenregelung,
                                                urlaubVorabVereinbart, notizArbeitszeit, created_on, created_by,
                                                changed_on,
                                                changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.stundenaenderung, NEW.verwendungsbereichsaenderung,
                NEW.stufenwechsel, NEW.geschaeftsbereichsaenderung, NEW.kvErhoehung, NEW.beschaeftigungsausmass,
                NEW.beschaeftigungsstatus, NEW.wochenstunden, NEW.arbeitszeitmodell, NEW.arbeitszeitmodellVon,
                NEW.arbeitszeitmodellBis, NEW.auswahlBegruendungFuerDurchrechner, NEW.spezielleMittagspausenregelung,
                NEW.urlaubVorabVereinbart, NEW.notizArbeitszeit, NEW.created_on, NEW.created_by, NEW.changed_on,
                NEW.changed_by, 'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO arbeitszeiten_info_history (arbeitszeiten_info_id, vertragsdaten_id, stundenaenderung,
                                                verwendungsbereichsaenderung,
                                                stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung,
                                                beschaeftigungsausmass,
                                                beschaeftigungsstatus, wochenstunden, arbeitszeitmodell,
                                                arbeitszeitmodellVon,
                                                arbeitszeitmodellBis, auswahlBegruendungFuerDurchrechner,
                                                spezielleMittagspausenregelung,
                                                urlaubVorabVereinbart, notizArbeitszeit, created_on, created_by,
                                                changed_on, changed_by,
                                                action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.stundenaenderung, NEW.verwendungsbereichsaenderung,
                NEW.stufenwechsel, NEW.geschaeftsbereichsaenderung, NEW.kvErhoehung, NEW.beschaeftigungsausmass,
                NEW.beschaeftigungsstatus, NEW.wochenstunden, NEW.arbeitszeitmodell, NEW.arbeitszeitmodellVon,
                NEW.arbeitszeitmodellBis, NEW.auswahlBegruendungFuerDurchrechner, NEW.spezielleMittagspausenregelung,
                NEW.urlaubVorabVereinbart, NEW.notizArbeitszeit, NEW.created_on, NEW.created_by, NULL,
                NULL, 'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;


alter function arbeitszeiten_info_audit() owner to ibosng;

create trigger arbeitszeiten_info_insert
    after insert
    on arbeitszeiten_info
    for each row
execute procedure arbeitszeiten_info_audit();

create trigger arbeitszeiten_info_update
    after update
    on arbeitszeiten_info
    for each row
execute procedure arbeitszeiten_info_audit();

create trigger arbeitszeiten_info_delete
    after delete
    on arbeitszeiten_info
    for each row
execute procedure arbeitszeiten_info_audit();



create table if not exists arbeitszeiten
(
    id                    integer generated always as identity primary key,
    type                  integer references arbeitszeitmodell (id),
    arbeitszeiten_info_id integer references arbeitszeiten_info (id),
    montag_von            text,
    montag_bis            text,
    dienstag_von          text,
    dienstag_bis          text,
    mittwoch_von          text,
    mittwoch_bis          text,
    donnerstag_von        text,
    donnerstag_bis        text,
    freitag_von           text,
    freitag_bis           text,
    samstag_von           text,
    samstag_bis           text,
    created_on            timestamp not null,
    created_by            text      not null,
    changed_on            timestamp,
    changed_by            text
);

create table if not exists arbeitszeiten_history
(
    id                    integer generated always as identity primary key,
    arbeitszeiten_id      integer   not null,
    type                  integer,
    arbeitszeiten_info_id integer,
    montag_von            text,
    montag_bis            text,
    dienstag_von          text,
    dienstag_bis          text,
    mittwoch_von          text,
    mittwoch_bis          text,
    donnerstag_von        text,
    donnerstag_bis        text,
    freitag_von           text,
    freitag_bis           text,
    samstag_von           text,
    samstag_bis           text,
    created_on            timestamp not null,
    created_by            text      not null,
    changed_on            timestamp,
    changed_by            text,
    action                char,
    action_timestamp      timestamp not null
);

CREATE OR REPLACE FUNCTION arbeitszeiten_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO arbeitszeiten_history (arbeitszeiten_id, type, arbeitszeiten_info_id,
                                           montag_von, montag_bis,
                                           dienstag_von, dienstag_bis,
                                           mittwoch_von, mittwoch_bis,
                                           donnerstag_von, donnerstag_bis,
                                           freitag_von, freitag_bis,
                                           samstag_von, samstag_bis,
                                           created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.type, OLD.arbeitszeiten_info_id,
                OLD.montag_von, OLD.montag_bis,
                OLD.dienstag_von, OLD.dienstag_bis,
                OLD.mittwoch_von, OLD.mittwoch_bis,
                OLD.donnerstag_von, OLD.donnerstag_bis,
                OLD.freitag_von, OLD.freitag_bis,
                OLD.samstag_von, OLD.samstag_bis,
                OLD.created_on, OLD.created_by,
                OLD.changed_on, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO arbeitszeiten_history (arbeitszeiten_id, type, arbeitszeiten_info_id,
                                           montag_von, montag_bis,
                                           dienstag_von, dienstag_bis,
                                           mittwoch_von, mittwoch_bis,
                                           donnerstag_von, donnerstag_bis,
                                           freitag_von, freitag_bis,
                                           samstag_von, samstag_bis,
                                           created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.arbeitszeiten_info_id,
                NEW.montag_von, NEW.montag_bis,
                NEW.dienstag_von, NEW.dienstag_bis,
                NEW.mittwoch_von, NEW.mittwoch_bis,
                NEW.donnerstag_von, NEW.donnerstag_bis,
                NEW.freitag_von, NEW.freitag_bis,
                NEW.samstag_von, NEW.samstag_bis,
                NEW.created_on, NEW.created_by,
                now(), NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO arbeitszeiten_history (arbeitszeiten_id, type, arbeitszeiten_info_id,
                                           montag_von, montag_bis,
                                           dienstag_von, dienstag_bis,
                                           mittwoch_von, mittwoch_bis,
                                           donnerstag_von, donnerstag_bis,
                                           freitag_von, freitag_bis,
                                           samstag_von, samstag_bis,
                                           created_on, created_by,
                                           changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.arbeitszeiten_info_id,
                NEW.montag_von, NEW.montag_bis,
                NEW.dienstag_von, NEW.dienstag_bis,
                NEW.mittwoch_von, NEW.mittwoch_bis,
                NEW.donnerstag_von, NEW.donnerstag_bis,
                NEW.freitag_von, NEW.freitag_bis,
                NEW.samstag_von, NEW.samstag_bis,
                NEW.created_on, NEW.created_by,
                NULL, NULL, -- assuming no change info for new records
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;


alter function arbeitszeiten_audit() owner to ibosng;

create trigger arbeitszeiten_insert
    after insert
    on arbeitszeiten
    for each row
execute procedure arbeitszeiten_audit();

create trigger arbeitszeiten_update
    after update
    on arbeitszeiten
    for each row
execute procedure arbeitszeiten_audit();

create trigger arbeitszeiten_delete
    after delete
    on arbeitszeiten
    for each row
execute procedure arbeitszeiten_audit();



CREATE TABLE IF NOT EXISTS gehalt_info
(
    id                                             integer generated always as identity primary key,
    vertragsdaten_id                               INTEGER REFERENCES vertragsdaten (id),
    kollektivvertrag                               VARCHAR(255),
    verwendungsgruppe                              VARCHAR(255),
    stufe                                          VARCHAR(255),
    facheinschlaegigeTaetigkeitenGeprueft          BOOLEAN,
    angerechneteIbisJahre                          INTEGER,
    angerechneteFacheinschlaegigeTaetigkeitenJahre INTEGER,
    kvGehaltBerechnet                              NUMERIC,
    gehaltVereinbart                               NUMERIC,
    ueberzahlung                                   NUMERIC,
    zulageInEuro                                   NUMERIC,
    artDerZulage                                   VARCHAR(255),
    gesamtBrutto                                   NUMERIC,
    vereinbarungUEberstunden                       VARCHAR(255),
    uestPauschale                                  NUMERIC,
    deckungspruefung                               TEXT,
    jobticket                                      BOOLEAN,
    notizGehalt                                    TEXT,
    created_on                                     timestamp not null,
    created_by                                     text      not null,
    changed_on                                     timestamp,
    changed_by                                     text
);


create table if not exists gehalt_info_history
(
    id                                             integer generated always as identity primary key,
    gehalt_info_id                                 integer   not null,
    vertragsdaten_id                               integer,
    kollektivvertrag                               VARCHAR(255),
    verwendungsgruppe                              VARCHAR(255),
    stufe                                          VARCHAR(255),
    facheinschlaegigeTaetigkeitenGeprueft          BOOLEAN,
    angerechneteIbisJahre                          INTEGER,
    angerechneteFacheinschlaegigeTaetigkeitenJahre INTEGER,
    kvGehaltBerechnet                              NUMERIC,
    gehaltVereinbart                               NUMERIC,
    ueberzahlung                                   NUMERIC,
    zulageInEuro                                   NUMERIC,
    artDerZulage                                   VARCHAR(255),
    gesamtBrutto                                   NUMERIC,
    vereinbarungUEberstunden                       VARCHAR(255),
    uestPauschale                                  NUMERIC,
    deckungspruefung                               TEXT,
    jobticket                                      BOOLEAN,
    notizGehalt                                    TEXT,
    created_on                                     timestamp not null,
    created_by                                     text      not null,
    changed_on                                     timestamp,
    changed_by                                     text,
    action                                         char,
    action_timestamp                               timestamp not null
);



CREATE OR REPLACE FUNCTION gehalt_info_audit()
    RETURNS TRIGGER LANGUAGE plpgsql AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegigeTaetigkeitenGeprueft, angerechneteIbisJahre,
                                         angerechneteFacheinschlaegigeTaetigkeitenJahre,
                                         kvGehaltBerechnet, gehaltVereinbart, ueberzahlung, zulageInEuro,
                                         artDerZulage, gesamtBrutto, vereinbarungUEberstunden,
                                         uestPauschale, deckungspruefung, jobticket, notizGehalt, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, OLD.kollektivvertrag, OLD.verwendungsgruppe,
                OLD.stufe, OLD.facheinschlaegigeTaetigkeitenGeprueft, OLD.angerechneteIbisJahre,
                OLD.angerechneteFacheinschlaegigeTaetigkeitenJahre,
                OLD.kvGehaltBerechnet, OLD.gehaltVereinbart, OLD.ueberzahlung, OLD.zulageInEuro,
                OLD.artDerZulage, OLD.gesamtBrutto, OLD.vereinbarungUEberstunden,
                OLD.uestPauschale, OLD.deckungspruefung, OLD.jobticket, OLD.notizGehalt, OLD.created_on, OLD.created_by,
                OLD.changed_on,
                OLD.changed_by, 'D', CURRENT_TIMESTAMP);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegigeTaetigkeitenGeprueft, angerechneteIbisJahre,
                                         angerechneteFacheinschlaegigeTaetigkeitenJahre,
                                         kvGehaltBerechnet, gehaltVereinbart, ueberzahlung, zulageInEuro,
                                         artDerZulage, gesamtBrutto, vereinbarungUEberstunden,
                                         uestPauschale, deckungspruefung, jobticket, notizGehalt, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegigeTaetigkeitenGeprueft, NEW.angerechneteIbisJahre,
                NEW.angerechneteFacheinschlaegigeTaetigkeitenJahre,
                NEW.kvGehaltBerechnet, NEW.gehaltVereinbart, NEW.ueberzahlung, NEW.zulageInEuro,
                NEW.artDerZulage, NEW.gesamtBrutto, NEW.vereinbarungUEberstunden,
                NEW.uestPauschale, NEW.deckungspruefung, NEW.jobticket, NEW.notizGehalt, NEW.created_on, NEW.created_by,
                NEW.changed_on,
                NEW.changed_by, 'U', CURRENT_TIMESTAMP);
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO gehalt_info_history (gehalt_info_id, vertragsdaten_id, kollektivvertrag, verwendungsgruppe,
                                         stufe, facheinschlaegigeTaetigkeitenGeprueft, angerechneteIbisJahre,
                                         angerechneteFacheinschlaegigeTaetigkeitenJahre,
                                         kvGehaltBerechnet, gehaltVereinbart, ueberzahlung, zulageInEuro,
                                         artDerZulage, gesamtBrutto, vereinbarungUEberstunden,
                                         uestPauschale, deckungspruefung, jobticket, notizGehalt, created_on,
                                         created_by, changed_on,
                                         changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.kollektivvertrag, NEW.verwendungsgruppe,
                NEW.stufe, NEW.facheinschlaegigeTaetigkeitenGeprueft, NEW.angerechneteIbisJahre,
                NEW.angerechneteFacheinschlaegigeTaetigkeitenJahre,
                NEW.kvGehaltBerechnet, NEW.gehaltVereinbart, NEW.ueberzahlung, NEW.zulageInEuro,
                NEW.artDerZulage, NEW.gesamtBrutto, NEW.vereinbarungUEberstunden,
                NEW.uestPauschale, NEW.deckungspruefung, NEW.jobticket, NEW.notizGehalt, NEW.created_on, NEW.created_by,
                NULL,
                NULL, 'I', CURRENT_TIMESTAMP);
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;


alter function gehalt_info_audit() owner to ibosng;

create trigger gehalt_info_insert
    after insert
    on gehalt_info
    for each row
execute procedure gehalt_info_audit();

create trigger gehalt_info_update
    after update
    on gehalt_info
    for each row
execute procedure gehalt_info_audit();

create trigger gehalt_info_delete
    after delete
    on gehalt_info
    for each row
execute procedure gehalt_info_audit();