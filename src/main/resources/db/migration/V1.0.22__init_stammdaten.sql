create table if not exists bank_daten
(
    id         integer generated always as identity primary key,
    bank       text,
    iban       VARCHAR(34), -- IBAN lengths vary by country but do not exceed 34 characters
    bic        VARCHAR(11), -- The BIC is generally 8 or 11 characters long
    card       text,
    status     smallint  not null,
    created_on timestamp not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists bank_daten_history
(
    id               integer generated always as identity primary key,
    bank_daten_id    integer   not null,
    bank             text,
    iban             VARCHAR(34), -- IBAN lengths vary by country but do not exceed 34 characters
    bic              VARCHAR(11), -- The BIC is generally 8 or 11 characters long
    card             text,
    status           smallint  not null,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function bank_daten_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, card, status, created_on, created_by,
                                        changed_by,
                                        action,
                                        action_timestamp)
        VALUES (OLD.id, OLD.bank, OLD.iban, OLD.bic, OLD.card, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, card, status, created_on, created_by,
                                        changed_by,
                                        action,
                                        action_timestamp)
        VALUES (NEW.id, NEW.bank, NEW.iban, NEW.bic, NEW.card, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO bank_daten_history (bank_daten_id, bank, iban, bic, card, status, created_on, created_by,
                                        changed_by,
                                        action,
                                        action_timestamp)
        VALUES (NEW.id, NEW.bank, NEW.iban, NEW.bic, NEW.card, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function bank_daten_audit() owner to ibosng;

create trigger bank_daten_insert
    after insert
    on bank_daten
    for each row
execute procedure bank_daten_audit();

create trigger bank_daten_update
    after update
    on bank_daten
    for each row
execute procedure bank_daten_audit();

create trigger bank_daten_delete
    after delete
    on bank_daten
    for each row
execute procedure bank_daten_audit();



create table if not exists stammdaten
(
    id                    integer generated always as identity
        primary key,
    anrede                integer references anrede (id),
    titel                 integer references titel (id),
    titel2                integer references titel (id),
    nachname              text,
    vorname               text,
    geburtsname           text,
    svnr                  bigint,
    ecard                 text,
    geschlecht            integer references geschlecht (id),
    familienstand         integer references familienstand (id),
    geburtsdatum          date,
    lebensalter           integer,
    staatsbuergerschaft   integer references land (id),
    muttersprache         text,
    adresse               integer
        references adresse,
    email                 text,
    mobilnummer           integer references telefon (id),
    bank                  integer references bank_daten,
    arbeitsgenehmigung    text,
    gueltigBis            DATE,
    arbeitsgenehmigungDok TEXT,
    foto                  text,
    status                smallint  default 0                 not null,
    created_on            timestamp default CURRENT_TIMESTAMP not null,
    created_by            text                                not null,
    changed_by            text,
    changed_on            timestamp default CURRENT_TIMESTAMP
);

create table if not exists stammdaten_history
(
    id                    integer generated always as identity
        primary key,
    stammdaten_id         integer,
    anrede                text,
    titel                 integer,
    titel2                integer,
    nachname              text,
    vorname               text,
    geburtsname           text,
    svnr                  bigint,
    ecard                 text,
    geschlecht            text,
    familienstand         integer,
    geburtsdatum          date,
    lebensalter           integer,
    staatsbuergerschaft   integer,
    muttersprache         text,
    adresse               integer,
    email                 text,
    mobilnummer           integer,
    bank                  integer,
    arbeitsgenehmigung    text,
    gueltigBis            DATE,
    arbeitsgenehmigungDok TEXT,
    foto                  text,
    status                smallint  not null,
    created_on            timestamp,
    created_by            text,
    changed_by            text,
    changed_on            timestamp,
    action                char,
    action_timestamp      timestamp not null
);


CREATE OR REPLACE FUNCTION stammdaten_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, bank,
                                        arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (OLD.id, OLD.anrede, OLD.titel, OLD.titel2, OLD.nachname, OLD.vorname,
                OLD.geburtsname, OLD.svnr, OLD.ecard, OLD.geschlecht, OLD.familienstand,
                OLD.geburtsdatum, OLD.lebensalter, OLD.staatsbuergerschaft, OLD.muttersprache,
                OLD.adresse, OLD.email, OLD.mobilnummer, OLD.bank,
                OLD.arbeitsgenehmigung, OLD.gueltigBis, OLD.arbeitsgenehmigungDok, OLD.foto,
                OLD.status, OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO stammdaten_history (stammdaten_id, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, bank,
                                        arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
                NEW.geburtsname, NEW.svnr, NEW.ecard, NEW.geschlecht, NEW.familienstand,
                NEW.geburtsdatum, NEW.lebensalter, NEW.staatsbuergerschaft, NEW.muttersprache,
                NEW.adresse, NEW.email, NEW.mobilnummer, NEW.bank,
                NEW.arbeitsgenehmigung, NEW.gueltigBis, NEW.arbeitsgenehmigungDok, NEW.foto,
                NEW.status, NEW.created_on, NEW.created_by, now(), NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO stammdaten_history (stammdaten_id, anrede, titel, titel2, nachname, vorname,
                                        geburtsname, svnr, ecard, geschlecht, familienstand,
                                        geburtsdatum, lebensalter, staatsbuergerschaft, muttersprache,
                                        adresse, email, mobilnummer, bank,
                                        arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungDok, foto,
                                        status, created_on, created_by, changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.anrede, NEW.titel, NEW.titel2, NEW.nachname, NEW.vorname,
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

alter function stammdaten_audit() owner to ibosng;

create trigger stammdaten_insert
    after insert
    on stammdaten
    for each row
execute procedure stammdaten_audit();

create trigger stammdaten_update
    after update
    on stammdaten
    for each row
execute procedure stammdaten_audit();

create trigger stammdaten_delete
    after delete
    on stammdaten
    for each row
execute procedure stammdaten_audit();