alter table if exists adresse add column if not exists land integer references land (id);
alter table if exists adresse_history add column land integer;
update adresse set land = (select id from land where land_code = 'AT') where land is null;
--alter table adresse alter land set not null;

create or replace function adresse_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO adresse_history (adresse_id, ort, plz, strasse, land, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.ort, OLD.plz, OLD.strasse, OLD.land, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO adresse_history (adresse_id, ort, plz, strasse, land, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.ort, NEW.plz, NEW.strasse, NEW.land, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO adresse_history (adresse_id, ort, plz, strasse, land, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.ort, NEW.plz, NEW.strasse, NEW.land, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;



create table if not exists beruf
(
    id                          integer generated always as identity primary key,
    name                        text,
    nr                          integer,
    created_on                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  text                                not null,
    changed_on                  timestamp default CURRENT_TIMESTAMP,
    changed_by                  text
);

create table if not exists beruf_history
(
    id                          integer generated always as identity primary key,
    beruf_id                    integer   not null,
    name                        text,
    nr                          integer,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table beruf_history owner to ibosng;

create table if not exists abteilung
(
    id                          integer generated always as identity primary key,
    kz                          text,
    name                        text,
    nr                          integer,
    created_on                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  text                                not null,
    changed_on                  timestamp default CURRENT_TIMESTAMP,
    changed_by                  text
);

create table if not exists abteilung_history
(
    id                          integer generated always as identity primary key,
    abteilung_id                integer   not null,
    kz                          text,
    name                        text,
    nr                          integer,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table abteilung_history owner to ibosng;

create table if not exists religion
(
    id                          integer generated always as identity primary key,
    designator                  text,
    name                        text,
    created_on                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text
);

create table if not exists religion_history
(
    id                          integer generated always as identity primary key,
    religion_id                 integer   not null,
    designator                  text,
    name                        text,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table religion_history owner to ibosng;

create table if not exists gehaltsinfo
(
    id                          integer generated always as identity primary key,
    kz                          text,
    name                        text,
    passwort                    text,
    created_on                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  text                                not null,
    changed_on                  timestamp default CURRENT_TIMESTAMP,
    changed_by                  text
);

create table if not exists gehaltsinfo_history
(
    id                          integer generated always as identity primary key,
    gehaltsinfo_id              integer   not null,
    kz                          text,
    name                        text,
    passwort                    text,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table gehaltsinfo_history owner to ibosng;

create table if not exists auftraggeber_bank
(
    id                          integer generated always as identity primary key,
    firmenbank_name             text,
    firmenbank_nummer           bigint,
    bankleitzahl                text,
    zahlungsart                 text,
    created_on                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  text                                not null,
    changed_on                  timestamp default CURRENT_TIMESTAMP,
    changed_by                  text
);

create table if not exists auftraggeber_bank_history
(
    id                          integer generated always as identity primary key,
    auftraggeber_bank_id integer   not null,
    firmenbank_name             text,
    firmenbank_nummer           bigint,
    bankleitzahl                text,
    zahlungsart                 text,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table auftraggeber_bank_history owner to ibosng;

create table if not exists bankverbindung
(
    id                          integer generated always as identity primary key,
    bankbezeichnung             text,
    iban_or_kto_nummer          text,
    bar_bei_austritt            boolean default false,
    land                        integer references land(id),
    lautend_auf                 text,
    auftraggeber_bank           integer references auftraggeber_bank,
    created_on                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  text                                not null,
    changed_on                  timestamp default CURRENT_TIMESTAMP,
    changed_by                  text
);

create table if not exists bankverbindung_history
(
    id                          integer generated always as identity primary key,
    bankverbindung_id           integer   not null,
    bankbezeichnung             text,
    iban_or_kto_nummer          text,
    bar_bei_austritt            boolean default false,
    land                        integer references land(id),
    lautend_auf                 text,
    auftraggeber_bank           integer references auftraggeber_bank,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table bankverbindung_history owner to ibosng;

create table if not exists dienstnehmer
(
    id                          integer generated always as identity primary key,
    dn_nr                       integer,
    familienstand               text,
    geburtsdatum                date,
    geburtsname                 text,
    geschlecht                  text,
    legitimation                text,
    name                        text,
    staatsbuergerschaft         text,
    sv_nummer                   bigint,
    titel                       text,
    titel2                      text,
    titel_intern                text,
    vorname                     text,
    adresse                     integer references adresse,
    geburtsort                  integer references adresse,
    telefon                     integer references telefon,
    religion                    integer references religion,
    beruf                       integer references beruf,
    abteilung                   integer references abteilung,
    gehaltsinfo                 integer references gehaltsinfo,
    bankverbindung              integer references bankverbindung,
    created_on                  timestamp default CURRENT_TIMESTAMP not null,
    created_by                  text                                not null,
    changed_on                  timestamp default CURRENT_TIMESTAMP,
    changed_by                  text
);

create table if not exists dienstnehmer_history
(
    id                          integer generated always as identity primary key,
    dienstnehmer_id             integer   not null,
    dn_nr                       integer,
    familienstand               text,
    geburtsdatum                date,
    geburtsname                 text,
    geschlecht                  text,
    legitimation                text,
    name                        text,
    staatsbuergerschaft         text,
    sv_nummer                   bigint,
    titel                       text,
    titel2                      text,
    titel_intern                text,
    vorname                     text,
    adresse                     integer references adresse,
    geburtsort                  integer references adresse,
    telefon                     integer references telefon,
    religion                    integer references religion,
    beruf                       integer references beruf,
    abteilung                   integer references abteilung,
    gehaltsinfo                 integer references gehaltsinfo,
    bankverbindung              integer references bankverbindung,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table dienstnehmer_history owner to ibosng;

create table if not exists erreichbarkeit
(
    id                          integer generated always as identity primary key,
    kz                          text,
    wert                        text,
    dienstnehmer                integer not null references dienstnehmer,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text
);

create table if not exists erreichbarkeit_history
(
    id                          integer generated always as identity primary key,
    erreichbarkeit_id           integer   not null,
    kz                          text,
    wert                        text,
    dienstnehmer                integer references dienstnehmer,
    created_on                  timestamp                           not null,
    created_by                  text                                not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

alter table erreichbarkeit_history owner to ibosng;

create or replace function abteilung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO abteilung_history (abteilung_id, kz, name, nr, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.kz, OLD.name, OLD.nr, OLD.created_on, OLD.created_by, CURRENT_USER, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO abteilung_history (abteilung_id, kz, name, nr, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.kz, NEW.name, NEW.nr, NEW.created_on, NEW.created_by, CURRENT_USER, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO abteilung_history (abteilung_id, kz, name, nr, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.kz, NEW.name, NEW.nr, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function abteilung_audit() owner to ibosng;

create trigger abteilung_insert
    after insert
    on abteilung
    for each row
execute procedure abteilung_audit();

create trigger abteilung_update
    after update
    on abteilung
    for each row
execute procedure abteilung_audit();

create trigger abteilung_delete
    after delete
    on abteilung
    for each row
execute procedure abteilung_audit();

create or replace function bankverbindung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO bankverbindung_history (bankverbindung_id, bankbezeichnung, bar_bei_austritt,
                                            iban_or_kto_nummer, land, lautend_auf, auftraggeber_bank, created_on,
                                            created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.bankbezeichnung, OLD.bar_bei_austritt, OLD.iban_or_kto_nummer, OLD.land, OLD.lautend_auf,
                OLD.auftraggeber_bank, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO bankverbindung_history (bankverbindung_id, bankbezeichnung, bar_bei_austritt,
                                            iban_or_kto_nummer, land, lautend_auf, auftraggeber_bank, created_on,
                                            created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bankbezeichnung, NEW.bar_bei_austritt, NEW.iban_or_kto_nummer, NEW.land, NEW.lautend_auf,
                NEW.auftraggeber_bank, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO bankverbindung_history (bankverbindung_id, bankbezeichnung, bar_bei_austritt,
                                            iban_or_kto_nummer, land, lautend_auf, auftraggeber_bank, created_on,
                                            created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.bankbezeichnung, NEW.bar_bei_austritt, NEW.iban_or_kto_nummer, NEW.land, NEW.lautend_auf,
                NEW.auftraggeber_bank, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function bankverbindung_audit() owner to ibosng;

create trigger bankverbindung_insert
    after insert
    on bankverbindung
    for each row
execute procedure bankverbindung_audit();

create trigger bankverbindung_update
    after update
    on bankverbindung
    for each row
execute procedure bankverbindung_audit();

create trigger bankverbindung_delete
    after delete
    on bankverbindung
    for each row
execute procedure bankverbindung_audit();

create or replace function auftraggeber_bank_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO auftraggeber_bank_history (auftraggeber_bank_id, firmenbank_name, firmenbank_nummer, bankleitzahl,
                                                zahlungsart, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.firmenbank_name, OLD.firmenbank_nummer, OLD.bankleitzahl, OLD.zahlungsart,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO auftraggeber_bank_history (auftraggeber_bank_id, firmenbank_name, firmenbank_nummer, bankleitzahl,
                                                zahlungsart, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.firmenbank_name, NEW.firmenbank_nummer, NEW.bankleitzahl, NEW.zahlungsart,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO auftraggeber_bank_history (auftraggeber_bank_id, firmenbank_name, firmenbank_nummer, bankleitzahl,
                                                zahlungsart, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.firmenbank_name, NEW.firmenbank_nummer, NEW.bankleitzahl, NEW.zahlungsart,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function auftraggeber_bank_audit() owner to ibosng;

create trigger auftraggeber_bank_insert
    after insert
    on auftraggeber_bank
    for each row
execute procedure auftraggeber_bank_audit();

create trigger auftraggeber_bank_update
    after update
    on auftraggeber_bank
    for each row
execute procedure auftraggeber_bank_audit();

create trigger auftraggeber_bank_delete
    after delete
    on auftraggeber_bank
    for each row
execute procedure auftraggeber_bank_audit();

create or replace function beruf_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO beruf_history (beruf_id, name, nr, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.nr, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO beruf_history (beruf_id, name, nr, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.nr, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO beruf_history (beruf_id, name, nr, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.nr, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function beruf_audit() owner to ibosng;

create trigger beruf_insert
    after insert
    on beruf
    for each row
execute procedure beruf_audit();

create trigger beruf_update
    after update
    on beruf
    for each row
execute procedure beruf_audit();

create trigger beruf_delete
    after delete
    on beruf
    for each row
execute procedure beruf_audit();

create or replace function dienstnehmer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO dienstnehmer_history (dienstnehmer_id, dn_nr, familienstand, geburtsdatum, geburtsname, geschlecht,
                                          legitimation, name, staatsbuergerschaft, sv_nummer, titel, titel2, titel_intern, vorname, adresse, geburtsort,
                                          telefon, religion, beruf, abteilung, gehaltsinfo, bankverbindung, created_on, created_by, changed_by, action,
                                          action_timestamp)
        VALUES (OLD.id, OLD.dn_nr, OLD.familienstand, OLD.geburtsdatum, OLD.geburtsname, OLD.geschlecht,
                OLD.legitimation, OLD.name, OLD.staatsbuergerschaft, OLD.sv_nummer, OLD.titel, OLD.titel2, OLD.titel_intern,
                OLD.vorname, OLD.adresse, OLD.geburtsort, OLD.telefon, OLD.religion, OLD.beruf, OLD.abteilung, OLD.gehaltsinfo,
                OLD.bankverbindung, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO dienstnehmer_history (dienstnehmer_id, dn_nr, familienstand, geburtsdatum, geburtsname, geschlecht,
                                          legitimation, name, staatsbuergerschaft, sv_nummer, titel, titel2, titel_intern, vorname, adresse, geburtsort,
                                          telefon, religion, beruf, abteilung, gehaltsinfo, bankverbindung, created_on, created_by, changed_by, action,
                                          action_timestamp)
        VALUES (NEW.id, NEW.dn_nr, NEW.familienstand, NEW.geburtsdatum, NEW.geburtsname, NEW.geschlecht,
                NEW.legitimation, NEW.name, NEW.staatsbuergerschaft, NEW.sv_nummer, NEW.titel, NEW.titel2, NEW.titel_intern,
                NEW.vorname, NEW.adresse, NEW.geburtsort, NEW.telefon, NEW.religion, NEW.beruf, NEW.abteilung, NEW.gehaltsinfo,
                NEW.bankverbindung, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO dienstnehmer_history (dienstnehmer_id, dn_nr, familienstand, geburtsdatum, geburtsname, geschlecht,
                                          legitimation, name, staatsbuergerschaft, sv_nummer, titel, titel2, titel_intern, vorname, adresse, geburtsort,
                                          telefon, religion, beruf, abteilung, gehaltsinfo, bankverbindung, created_on, created_by, changed_by, action,
                                          action_timestamp)
        VALUES (NEW.id, NEW.dn_nr, NEW.familienstand, NEW.geburtsdatum, NEW.geburtsname, NEW.geschlecht,
                NEW.legitimation, NEW.name, NEW.staatsbuergerschaft, NEW.sv_nummer, NEW.titel, NEW.titel2, NEW.titel_intern,
                NEW.vorname, NEW.adresse, NEW.geburtsort, NEW.telefon, NEW.religion, NEW.beruf, NEW.abteilung, NEW.gehaltsinfo,
                NEW.bankverbindung, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function dienstnehmer_audit() owner to ibosng;

create trigger dienstnehmer_insert
    after insert
    on dienstnehmer
    for each row
execute procedure dienstnehmer_audit();

create trigger dienstnehmer_update
    after update
    on dienstnehmer
    for each row
execute procedure dienstnehmer_audit();

create trigger dienstnehmer_delete
    after delete
    on dienstnehmer
    for each row
execute procedure dienstnehmer_audit();

create or replace function erreichbarkeit_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO erreichbarkeit_history (erreichbarkeit_id, kz, wert, dienstnehmer, created_on, created_by, changed_by,
                                            action, action_timestamp)
        VALUES (OLD.id, OLD.kz, OLD.wert, OLD.dienstnehmer, OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO erreichbarkeit_history (erreichbarkeit_id, kz, wert, dienstnehmer, created_on, created_by, changed_by,
                                            action, action_timestamp)
        VALUES (NEW.id, NEW.kz, NEW.wert, NEW.dienstnehmer, NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO erreichbarkeit_history (erreichbarkeit_id, kz, wert, dienstnehmer, created_on, created_by, changed_by,
                                            action, action_timestamp)
        VALUES (NEW.id, NEW.kz, NEW.wert, NEW.dienstnehmer, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function erreichbarkeit_audit() owner to ibosng;

create trigger erreichbarkeit_insert
    after insert
    on erreichbarkeit
    for each row
execute procedure erreichbarkeit_audit();

create trigger erreichbarkeit_update
    after update
    on erreichbarkeit
    for each row
execute procedure erreichbarkeit_audit();

create trigger erreichbarkeit_delete
    after delete
    on erreichbarkeit
    for each row
execute procedure erreichbarkeit_audit();

create or replace function gehaltsinfo_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehaltsinfo_history (gehaltsinfo_id, kz, name, passwort, created_on, created_by, changed_by,
                                         action, action_timestamp)
        VALUES (OLD.id, OLD.kz, OLD.name, OLD.passwort, OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO gehaltsinfo_history (gehaltsinfo_id, kz, name, passwort, created_on, created_by, changed_by,
                                         action, action_timestamp)
        VALUES (NEW.id, NEW.kz, NEW.name, NEW.passwort, NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO gehaltsinfo_history (gehaltsinfo_id, kz, name, passwort, created_on, created_by, changed_by,
                                         action, action_timestamp)
        VALUES (NEW.id, NEW.kz, NEW.name, NEW.passwort, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function gehaltsinfo_audit() owner to ibosng;

create trigger gehaltsinfo_insert
    after insert
    on gehaltsinfo
    for each row
execute procedure gehaltsinfo_audit();

create trigger gehaltsinfo_update
    after update
    on gehaltsinfo
    for each row
execute procedure gehaltsinfo_audit();

create trigger gehaltsinfo_delete
    after delete
    on gehaltsinfo
    for each row
execute procedure gehaltsinfo_audit();

create or replace function religion_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO religion_history (religion_id, designator, name, created_on, created_by, changed_by,
                                      action, action_timestamp)
        VALUES (OLD.id, OLD.designator, OLD.name, OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO religion_history (religion_id, designator, name, created_on, created_by, changed_by,
                                      action, action_timestamp)
        VALUES (NEW.id, NEW.designator, NEW.name, NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO religion_history (religion_id, designator, name, created_on, created_by, changed_by,
                                      action, action_timestamp)
        VALUES (NEW.id, NEW.designator, NEW.name, NEW.created_on, NEW.created_by,
                NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function religion_audit() owner to ibosng;

create trigger religion_insert
    after insert
    on religion
    for each row
execute procedure religion_audit();

create trigger religion_update
    after update
    on religion
    for each row
execute procedure religion_audit();

create trigger religion_delete
    after delete
    on religion
    for each row
execute procedure religion_audit();
