create table if not exists benutzer
(
    id         integer generated always as identity
        primary key,
    azure_id   text,
    status     smallint  default 0                 not null,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_by text,
    first_name text,
    last_name  text,
    email      text
);

alter table benutzer
    owner to ibosng;


create table if not exists benutzer_history
(
    id               integer generated always as identity
        primary key,
    benutzer_id      integer   not null,
    azure_id         text,
    status           smallint  not null,
    created_on       timestamp,
    created_by       text,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null,
    first_name       text,
    last_name        text,
    email            text
);

alter table benutzer_history
    owner to ibosng;

create table if not exists betreuer
(
    id         integer generated always as identity
        primary key,
    titel      text,
    nachname   text,
    vorname    text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_by text,
    status     smallint  default 0                 not null
);

alter table betreuer
    owner to ibosng;


create table if not exists teilnehmer_history
(
    id                   integer generated always as identity
        primary key,
    titel                text,
    nachname             text,
    vorname              text,
    geschlecht           text,
    sv_nummer            bigint,
    geburtsdatum         date,
    adresse              integer,
    buchungsstatus       text,
    anmerkung            text,
    geplant              date,
    eintritt             date,
    austritt             date,
    betreuer             integer,
    massnahmennummer     text,
    veranstaltungsnummer text,
    email                text,
    status               smallint  not null,
    created_on           timestamp,
    created_by           text,
    changed_by           text,
    action               char,
    action_timestamp     timestamp not null,
    teilnehmer_id        integer   not null,
    import_filename      text,
    zubuchung            date,
    rgs                  integer,
    info                 text,
    nation               text,
    changed_on           timestamp
);

alter table teilnehmer_history
    owner to ibosng;


create table if not exists telefon_history
(
    id               integer generated always as identity
        primary key,
    telefon_id       integer   not null,
    land_id          integer   not null,
    telefonnummer    bigint    not null,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_by       text,
    action           char      not null,
    action_timestamp timestamp not null,
    status           smallint  not null,
    owner            text
);

alter table telefon_history
    owner to ibosng;

create table if not exists land
(
    id             integer generated always as identity
        primary key,
    land_name      varchar(255)                        not null,
    land_code      varchar(2),
    elda_code      varchar(10),
    telefonvorwahl varchar(10)                         not null,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_by     text
);

alter table land
    owner to ibosng;

create table if not exists telefon
(
    id            integer generated always as identity
        primary key,
    land_id       integer                             not null
        references land,
    telefonnummer bigint                              not null,
    created_on    timestamp default CURRENT_TIMESTAMP not null,
    created_by    text                                not null,
    changed_by    text,
    status        smallint  default 0                 not null,
    owner         text
);

alter table telefon
    owner to ibosng;


create table if not exists land_history
(
    id               integer generated always as identity
        primary key,
    land_id          integer      not null,
    land_name        varchar(255) not null,
    land_code        varchar(2),
    elda_code        varchar(10),
    telefonvorwahl   varchar(10)  not null,
    created_on       timestamp,
    created_by       text,
    changed_by       text,
    action           char,
    action_timestamp timestamp    not null
);

alter table land_history
    owner to ibosng;

create table if not exists betreuer_history
(
    id               integer generated always as identity,
    betreuer_id      integer   not null,
    titel            text,
    nachname         text,
    vorname          text,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_by       text,
    status           smallint  not null,
    action           char,
    action_timestamp timestamp not null
);

alter table betreuer_history
    owner to ibosng;

create table if not exists widgets_in_dashboards_history
(
    id                       integer generated always as identity
        primary key,
    widgets_in_dashboards_id integer   not null,
    dashboard_id             integer   not null,
    widget_id                integer   not null,
    position_x               integer   not null,
    position_y               integer   not null,
    status                   smallint  not null,
    created_on               timestamp,
    created_by               text,
    changed_by               text,
    action                   char,
    action_timestamp         timestamp not null
);

alter table widgets_in_dashboards_history
    owner to ibosng;

create table if not exists dashboards
(
    id             integer generated always as identity
        primary key,
    benutzer_id    integer                             not null
        references benutzer,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_by     text,
    dashboard_name text,
    status         smallint  default 0                 not null,
    is_favourite   boolean   default false             not null
);

alter table dashboards
    owner to ibosng;

create table if not exists dashboards_history
(
    id               integer generated always as identity
        primary key,
    dashboards_id    integer   not null,
    benutzer_id      integer   not null,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_by       text,
    action           char      not null,
    action_timestamp timestamp not null,
    dashboard_name   text,
    status           smallint  not null,
    is_favourite     boolean
);

alter table dashboards_history
    owner to ibosng;

create table if not exists validations
(
    id         integer generated always as identity
        primary key,
    type       smallint  default 0                 not null,
    status     smallint  default 0                 not null,
    message    text,
    identifier text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_by text,
    entity_id  integer
);

alter table validations
    owner to ibosng;

create table if not exists validations_history
(
    id               integer generated always as identity
        primary key,
    validations_id   integer                             not null,
    type             smallint  default 0                 not null,
    status           smallint  default 0                 not null,
    message          text,
    identifier       text,
    created_on       timestamp                           not null,
    created_by       text                                not null,
    changed_by       text,
    action           char                                not null,
    action_timestamp timestamp default CURRENT_TIMESTAMP not null,
    entity_id        integer
);

alter table validations_history
    owner to ibosng;

create table if not exists plz
(
    id         integer generated always as identity primary key,
    plz        integer                             not null,
    ort        text                                not null,
    bundesland text                                not null,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_by text
);

alter table plz
    owner to ibosng;

create table if not exists plz_history
(
    id               integer generated always as identity primary key ,
    plz_id           integer                             not null,
    plz              integer                             not null,
    ort              text                                not null,
    bundesland       text                                not null,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_by       text,
    action           char                                not null,
    action_timestamp timestamp default CURRENT_TIMESTAMP not null
);

alter table plz_history
    owner to ibosng;

create table if not exists adresse
(
    id         integer generated always as identity
        primary key,
    ort        text,
    plz        integer references plz(id),
    strasse    text,
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_by text
);

alter table adresse
    owner to ibosng;

create table if not exists adresse_history
(
    id               integer generated always as identity
        primary key,
    adresse_id       integer                             not null,
    ort              text,
    plz              integer,
    strasse          text,
    created_on       timestamp                           not null,
    created_by       text                                not null,
    changed_by       text,
    action           char                                not null,
    action_timestamp timestamp default CURRENT_TIMESTAMP not null
);

alter table adresse_history
    owner to ibosng;

create table if not exists data_status_history
(
    id                integer generated always as identity
        primary key,
    data_status_id    integer                             not null,
    data_status_value smallint                            not null,
    teilnehmer_id     integer                             not null,
    action            char                                not null,
    action_timestamp  timestamp default CURRENT_TIMESTAMP not null
);

alter table data_status_history
    owner to ibosng;


create table if not exists teilnehmer_staging
(
    id                                integer generated always as identity
        primary key,
    titel                             text,
    vorname                           text,
    nachname                          text,
    geschlecht                        text,
    sv_nummer                         text,
    geburtsdatum                      text,
    buchungsstatus                    text,
    anmerkung                         text,
    zubuchung                         text,
    geplant                           text,
    eintritt                          text,
    austritt                          text,
    rgs                               text,
    massnahmennummer                  text,
    veranstaltungsnummer              text,
    email                             text,
    import_filename                   text,
    betreuer_titel                    text,
    betreuer_vorname                  text,
    betreuer_nachname                 text,
    telefon                           text,
    status                            smallint,
    plz                               text,
    ort                               text,
    strasse                           text,
    nation                            text,
    info                              text,
    seminar_identifier                text,
    seminar_start_date                text,
    seminar_end_date                  text,
    seminar_start_time                text,
    seminar_type                      text,
    created_on                        timestamp default CURRENT_TIMESTAMP not null,
    created_by                        text                                not null,
    changed_by                        text,
    landesvorwahl                     text,
    vorwahl                           text,
    telefon_nummer                    text,
    trainer                           text,
    teilnahmebeginn                   text,
    anmeldestatus                     text,
    ams                               text,
    abmeldegrund                      text,
    anwesenheit_in_ue                 text,
    entschuldigte_abwesenheit_in_ue   text,
    unentschuldigte_abwesenheit_in_ue text,
    summe_erfasste_an_und_abwesenheit text,
    anwesenheit_erfasst_bis           text,
    absolute_anwesenheit              text,
    relative_anwesenheit              text,
    source                            smallint,
    teilnehmer_id integer
);

alter table teilnehmer_staging
    owner to ibosng;

create table if not exists rgs
(
    id          integer generated always as identity
        primary key,
    rgs         integer                             not null,
    bezeichnung text                                not null,
    adresse_id  integer
        references adresse,
    created_on  timestamp default CURRENT_TIMESTAMP not null,
    created_by  text                                not null,
    changed_by  text
);

alter table rgs
    owner to ibosng;

create table if not exists teilnehmer
(
    id                   integer generated always as identity
        primary key,
    titel                text,
    nachname             text,
    vorname              text,
    geschlecht           text,
    sv_nummer            bigint,
    geburtsdatum         date,
    adresse              integer
        references adresse,
    buchungsstatus       text,
    anmerkung            text,
    geplant              date,
    eintritt             date,
    austritt             date,
    betreuer             integer
        references betreuer,
    massnahmennummer     text,
    veranstaltungsnummer text,
    email                text,
    status               smallint  default 0                 not null,
    created_on           timestamp default CURRENT_TIMESTAMP not null,
    created_by           text                                not null,
    changed_by           text,
    import_filename      text,
    zubuchung            date,
    rgs                  integer
        references rgs,
    info                 text,
    nation               text,
    changed_on           timestamp default CURRENT_TIMESTAMP
);

alter table teilnehmer
    owner to ibosng;


create table if not exists data_status
(
    id                integer generated always as identity
        primary key,
    data_status_value smallint not null,
    teilnehmer_id     integer  not null
        references teilnehmer
);

alter table data_status
    owner to ibosng;

create table if not exists rgs_history
(
    id               integer generated always as identity,
    rgs_id           integer                             not null,
    bezeichnung      text                                not null,
    adresse_id       integer,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_by       text,
    action           char                                not null,
    action_timestamp timestamp default CURRENT_TIMESTAMP not null,
    rgs              text
);

alter table rgs_history
    owner to ibosng;

create table if not exists vorwahl
(
    id           integer generated always as identity,
    vorwahl      integer                             not null,
    land_vorwahl integer                             not null
        references land,
    created_on   timestamp default CURRENT_TIMESTAMP not null,
    created_by   text                                not null,
    changed_by   text
);

alter table vorwahl
    owner to ibosng;

create table if not exists vorwahl_history
(
    id               integer generated always as identity,
    vorwahl_id       integer                             not null,
    vorwahl          integer                             not null,
    land_vorwahl     integer                             not null,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_by       text,
    action           char                                not null,
    action_timestamp timestamp default CURRENT_TIMESTAMP not null
);

alter table vorwahl_history
    owner to ibosng;

create table if not exists teilnehmer_telefon
(
    id            integer generated always as identity
        primary key,
    teilnehmer_id integer                             not null
        references teilnehmer,
    telefon_id    integer                             not null
        references telefon,
    created_on    timestamp default CURRENT_TIMESTAMP not null
);

alter table teilnehmer_telefon
    owner to ibosng;

create table if not exists teilnehmer_telefon_history
(
    id                    integer generated always as identity
        primary key,
    teilnehmer_telefon_id integer                             not null,
    teilnehmer_id         integer                             not null,
    telefon_id            integer                             not null,
    created_on            timestamp default CURRENT_TIMESTAMP not null,
    action                char                                not null,
    action_timestamp      timestamp default CURRENT_TIMESTAMP not null
);

alter table teilnehmer_telefon_history
    owner to ibosng;

create table if not exists teilnehmer_staging_history
(
    id                                integer generated always as identity
        primary key,
    teilnehmer_staging_id             integer                             not null,
    titel                             text,
    vorname                           text,
    nachname                          text,
    geschlecht                        text,
    sv_nummer                         text,
    geburtsdatum                      text,
    buchungsstatus                    text,
    anmerkung                         text,
    zubuchung                         text,
    geplant                           text,
    eintritt                          text,
    austritt                          text,
    rgs                               text,
    massnahmennummer                  text,
    veranstaltungsnummer              text,
    email                             text,
    import_filename                   text,
    betreuer_titel                    text,
    betreuer_vorname                  text,
    betreuer_nachname                 text,
    telefon                           text,
    status                            smallint,
    plz                               text,
    ort                               text,
    strasse                           text,
    nation                            text,
    info                              text,
    seminar_identifier                text,
    seminar_start_date                text,
    seminar_end_date                  text,
    seminar_start_time                text,
    seminar_type                      text,
    created_on                        timestamp default CURRENT_TIMESTAMP not null,
    created_by                        text                                not null,
    changed_by                        text,
    action                            char,
    action_timestamp                  timestamp default CURRENT_TIMESTAMP not null,
    landesvorwahl                     text,
    vorwahl                           text,
    telefon_nummer                    text,
    trainer                           text,
    teilnahmebeginn                   text,
    anmeldestatus                     text,
    ams                               text,
    abmeldegrund                      text,
    anwesenheit_in_ue                 text,
    entschuldigte_abwesenheit_in_ue   text,
    unentschuldigte_abwesenheit_in_ue text,
    summe_erfasste_an_und_abwesenheit text,
    anwesenheit_erfasst_bis           text,
    absolute_anwesenheit              text,
    relative_anwesenheit              text,
    source                            smallint,
    teilnehmer_id integer
);

alter table teilnehmer_staging_history
    owner to ibosng;

create table if not exists widget
(
    id          integer generated always as identity
        primary key,
    name        text                                not null,
    description text,
    status      smallint  default 0                 not null,
    created_on  timestamp default CURRENT_TIMESTAMP not null,
    created_by  text                                not null,
    changed_by  text
);

alter table widget
    owner to ibosng;

create table if not exists widgets_in_dashboards
(
    id           integer generated always as identity
        primary key,
    dashboard_id integer                             not null
        references dashboards,
    widget_id    integer                             not null
        references widget,
    position_x   integer                             not null,
    position_y   integer                             not null,
    status       smallint  default 0                 not null,
    created_on   timestamp default CURRENT_TIMESTAMP not null,
    created_by   text                                not null,
    changed_by   text
);

alter table widgets_in_dashboards
    owner to ibosng;

create table if not exists widget_history
(
    id               integer generated always as identity
        primary key,
    widget_id        integer                             not null,
    name             text                                not null,
    description      text,
    status           smallint  default 0                 not null,
    created_on       timestamp,
    created_by       text,
    changed_by       text,
    action           char,
    action_timestamp timestamp default CURRENT_TIMESTAMP not null
);

alter table widget_history
    owner to ibosng;

create table if not exists seminar
(
    id             integer generated always as identity
        primary key,
    identifier     text                                not null,
    type           text,
    start_date     date,
    end_date       date,
    start_time     time,
    status         smallint  default 0                 not null,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_on     timestamp default CURRENT_TIMESTAMP not null,
    changed_by     text,
    end_time       time,
    seminar_nummer integer,
    project        integer,
    bezeichnung    text
);

alter table seminar
    owner to ibosng;

create table if not exists seminar_history
(
    id               integer generated always as identity
        constraint course_history_pkey
            primary key,
    seminar_id       integer                             not null,
    identifier       text                                not null,
    type             text,
    start_date       date,
    end_date         date,
    start_time       time,
    status           smallint  default 0                 not null,
    created_on       timestamp,
    changed_on       timestamp,
    created_by       text,
    changed_by       text,
    action           char,
    action_timestamp timestamp default CURRENT_TIMESTAMP not null,
    end_time         time,
    seminar_nummer   integer,
    project          integer,
    bezeichnung      text
);

alter table seminar_history
    owner to ibosng;

create table if not exists teilnehmer_2_seminar
(
    id            integer generated always as identity
        primary key,
    teilnehmer_id integer                             not null
        references teilnehmer,
    seminar_id    integer                             not null
        references seminar,
    created_on    timestamp default CURRENT_TIMESTAMP not null
);

alter table teilnehmer_2_seminar
    owner to ibosng;

create table if not exists teilnehmer_2_seminar_history
(
    id                      integer generated always as identity
        primary key,
    teilnehmer_2_seminar_id integer                             not null,
    teilnehmer_id           integer                             not null,
    course_id               integer                             not null,
    created_on              timestamp,
    action                  char,
    action_timestamp        timestamp default CURRENT_TIMESTAMP not null
);

alter table teilnehmer_2_seminar_history
    owner to ibosng;


create or replace function betreuer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO betreuer_history (betreuer_id, titel, nachname, vorname, status, created_on, created_by, changed_by,
                                      action, action_timestamp)
        VALUES (OLD.id, OLD.titel, OLD.nachname, OLD.vorname, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO betreuer_history (betreuer_id, titel, nachname, vorname, status, created_on, created_by, changed_by,
                                      action, action_timestamp)
        VALUES (NEW.id, NEW.titel, NEW.nachname, NEW.vorname, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO betreuer_history (betreuer_id, titel, nachname, vorname, status, created_on, created_by, changed_by,
                                      action, action_timestamp)
        VALUES (NEW.id, NEW.titel, NEW.nachname, NEW.vorname, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function betreuer_audit() owner to ibosng;

create trigger betreuer_insert
    after insert
    on betreuer
    for each row
execute procedure betreuer_audit();

create trigger betreuer_update
    after update
    on betreuer
    for each row
execute procedure betreuer_audit();

create trigger betreuer_delete
    after delete
    on betreuer
    for each row
execute procedure betreuer_audit();

create or replace function land_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, telefonvorwahl,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (OLD.id, OLD.land_name, OLD.land_code, OLD.elda_code, OLD.telefonvorwahl,
                OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, telefonvorwahl,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.telefonvorwahl,
                NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, telefonvorwahl,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.telefonvorwahl, NEW.created_on, NEW.created_by,
                NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function land_audit() owner to ibosng;

create trigger land_insert
    after insert
    on land
    for each row
execute procedure land_audit();

create trigger land_update
    after update
    on land
    for each row
execute procedure land_audit();

create trigger land_delete
    after delete
    on land
    for each row
execute procedure land_audit();

create or replace function telefon_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO telefon_history (telefon_id, land_id, telefonnummer, owner, status,
                                     created_on, created_by, changed_by,
                                     action, action_timestamp)
        VALUES (OLD.id, OLD.land_id, OLD.telefonnummer, OLD.owner, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO telefon_history (telefon_id, land_id, telefonnummer, owner, status,
                                     created_on, created_by, changed_by,
                                     action, action_timestamp)
        VALUES (NEW.id, NEW.land_id, NEW.telefonnummer, NEW.owner, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO telefon_history (telefon_id, land_id, telefonnummer, owner, status,
                                     created_on, created_by, changed_by,
                                     action, action_timestamp)
        VALUES (NEW.id, NEW.land_id, NEW.telefonnummer, NEW.owner, NEW.status,
                NEW.created_on, NEW.created_by, NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function telefon_audit() owner to ibosng;

create trigger telefon_insert
    after insert
    on telefon
    for each row
execute procedure telefon_audit();

create trigger telefon_update
    after update
    on telefon
    for each row
execute procedure telefon_audit();

create trigger telefon_delete
    after delete
    on telefon
    for each row
execute procedure telefon_audit();

create or replace function benutzer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO benutzer_history (benutzer_id, azure_id, first_name, last_name, email, status, created_on,
                                      created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.azure_id, OLD.first_name, OLD.last_name, OLD.email, OLD.status, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO benutzer_history (benutzer_id, azure_id, first_name, last_name, email, status, created_on,
                                      created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.azure_id, NEW.first_name, NEW.last_name, NEW.email, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO benutzer_history (benutzer_id, azure_id, first_name, last_name, email, status, created_on,
                                      created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.azure_id, NEW.first_name, NEW.last_name, NEW.email, NEW.status, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function benutzer_audit() owner to ibosng;

create trigger benutzer_insert
    after insert
    on benutzer
    for each row
execute procedure benutzer_audit();

create trigger benutzer_update
    after update
    on benutzer
    for each row
execute procedure benutzer_audit();

create trigger benutzer_delete
    after delete
    on benutzer
    for each row
execute procedure benutzer_audit();

create or replace function dashboards_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO dashboards_history (dashboards_id, benutzer_id, dashboard_name, status, is_favourite, created_on,
                                        created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.benutzer_id, OLD.dashboard_name, OLD.status, OLD.is_favourite, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO dashboards_history (dashboards_id, benutzer_id, dashboard_name, status, is_favourite, created_on,
                                        created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.dashboard_name, NEW.status, NEW.is_favourite, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO dashboards_history (dashboards_id, benutzer_id, dashboard_name, status, is_favourite, created_on,
                                        created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.benutzer_id, NEW.dashboard_name, NEW.status, NEW.is_favourite, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function dashboards_audit() owner to ibosng;

create trigger dashboards_insert
    after insert
    on dashboards
    for each row
execute procedure dashboards_audit();

create trigger dashboards_update
    after update
    on dashboards
    for each row
execute procedure dashboards_audit();

create trigger dashboards_delete
    after delete
    on dashboards
    for each row
execute procedure dashboards_audit();

create or replace function widgets_in_dashboards_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO Widgets_in_Dashboards_history (widgets_in_dashboards_id, dashboard_id, widget_id, position_x,
                                                   position_y, status, created_on, created_by, changed_by, action,
                                                   action_timestamp)
        VALUES (OLD.id, OLD.dashboard_id, OLD.widget_id, OLD.position_x, OLD.position_y, OLD.status, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO Widgets_in_Dashboards_history (widgets_in_dashboards_id, dashboard_id, widget_id, position_x,
                                                   position_y, status, created_on, created_by, changed_by, action,
                                                   action_timestamp)
        VALUES (NEW.id, NEW.dashboard_id, NEW.widget_id, NEW.position_x, NEW.position_y, NEW.status, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO Widgets_in_Dashboards_history (widgets_in_dashboards_id, dashboard_id, widget_id, position_x,
                                                   position_y, status, created_on, created_by, changed_by, action,
                                                   action_timestamp)
        VALUES (NEW.id, NEW.dashboard_id, NEW.widget_id, NEW.position_x, NEW.position_y, NEW.status, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function widgets_in_dashboards_audit() owner to ibosng;

create trigger widgets_in_dashboards_insert
    after insert
    on widgets_in_dashboards
    for each row
execute procedure widgets_in_dashboards_audit();

create trigger widgets_in_dashboards_update
    after update
    on widgets_in_dashboards
    for each row
execute procedure widgets_in_dashboards_audit();

create trigger widgets_in_dashboards_delete
    after delete
    on widgets_in_dashboards
    for each row
execute procedure widgets_in_dashboards_audit();

create or replace function teilnehmer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_history (teilnehmer_id, titel, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse, buchungsstatus,
                                        anmerkung, zubuchung, geplant, eintritt, austritt, rgs, betreuer,
                                        massnahmennummer, veranstaltungsnummer,
                                        email, status, import_filename, info, nation, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (OLD.id, OLD.titel, OLD.nachname, OLD.vorname, OLD.geschlecht,
                OLD.sv_nummer, OLD.geburtsdatum, OLD.adresse, OLD.buchungsstatus, OLD.anmerkung, OLD.zubuchung,
                OLD.geplant, OLD.eintritt, OLD.austritt, OLD.rgs, OLD.betreuer, OLD.massnahmennummer,
                OLD.veranstaltungsnummer,
                OLD.email, OLD.status, OLD.import_filename, OLD.info, OLD.nation, OLD.created_on, OLD.created_by, now(),
                OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_history (teilnehmer_id, titel, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse, buchungsstatus,
                                        anmerkung, zubuchung, geplant, eintritt, austritt, rgs, betreuer,
                                        massnahmennummer, veranstaltungsnummer,
                                        email, status, import_filename, info, nation, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.titel, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse, NEW.buchungsstatus, NEW.anmerkung, NEW.zubuchung,
                NEW.geplant, NEW.eintritt, NEW.austritt, NEW.rgs, NEW.betreuer, NEW.massnahmennummer,
                NEW.veranstaltungsnummer,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.nation, NEW.created_on, NEW.created_by, now(),
                NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_history (teilnehmer_id, titel, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse, buchungsstatus,
                                        anmerkung, zubuchung, geplant, eintritt, austritt, rgs, betreuer,
                                        massnahmennummer, veranstaltungsnummer,
                                        email, status, import_filename, info, nation, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.titel, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse, NEW.buchungsstatus, NEW.anmerkung, NEW.zubuchung,
                NEW.geplant, NEW.eintritt, NEW.austritt, NEW.rgs, NEW.betreuer, NEW.massnahmennummer,
                NEW.veranstaltungsnummer,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, OLD.nation, NEW.created_on, NEW.created_by, now(),
                NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function teilnehmer_audit() owner to ibosng;

create trigger teilnehmer_insert
    after insert
    on teilnehmer
    for each row
execute procedure teilnehmer_audit();

create trigger teilnehmer_update
    after update
    on teilnehmer
    for each row
execute procedure teilnehmer_audit();

create trigger teilnehmer_delete
    after delete
    on teilnehmer
    for each row
execute procedure teilnehmer_audit();

create or replace function validations_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO validations_history (validations_id, type, status, message, identifier, entity_id, created_on,
                                         created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.type, OLD.status, OLD.message, OLD.identifier, OLD.entity_id, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO validations_history (validations_id, type, status, message, identifier, entity_id, created_on,
                                         created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.status, NEW.message, NEW.identifier, NEW.entity_id, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO validations_history (validations_id, type, status, message, identifier, entity_id, created_on,
                                         created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.status, NEW.message, NEW.identifier, OLD.entity_id, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function validations_audit() owner to ibosng;

create trigger validations_insert
    after insert
    on validations
    for each row
execute procedure validations_audit();

create trigger validations_update
    after update
    on validations
    for each row
execute procedure validations_audit();

create trigger validations_delete
    after delete
    on validations
    for each row
execute procedure validations_audit();

create or replace function adresse_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO adresse_history (adresse_id, ort, plz, strasse, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.ort, OLD.plz, OLD.strasse, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO adresse_history (adresse_id, ort, plz, strasse, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.ort, NEW.plz, NEW.strasse, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO adresse_history (adresse_id, ort, plz, strasse, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.ort, NEW.plz, NEW.strasse, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function adresse_audit() owner to ibosng;

create trigger adresse_insert
    after insert
    on adresse
    for each row
execute procedure adresse_audit();

create trigger adresse_update
    after update
    on adresse
    for each row
execute procedure adresse_audit();

create trigger adresse_delete
    after delete
    on adresse
    for each row
execute procedure adresse_audit();

create or replace function data_status_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO data_status_history (data_status_id, data_status_value, teilnehmer_id, action, action_timestamp)
        VALUES (OLD.id, OLD.data_status_value, OLD.teilnehmer_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO data_status_history (data_status_id, data_status_value, teilnehmer_id, action, action_timestamp)
        VALUES (NEW.id, NEW.data_status_value, NEW.teilnehmer_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO data_status_history (data_status_id, data_status_value, teilnehmer_id, action, action_timestamp)
        VALUES (NEW.id, NEW.data_status_value, NEW.teilnehmer_id, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function data_status_audit() owner to ibosng;

create trigger data_status_insert
    after insert
    on data_status
    for each row
execute procedure data_status_audit();

create trigger data_status_update
    after update
    on data_status
    for each row
execute procedure data_status_audit();

create trigger data_status_delete
    after delete
    on data_status
    for each row
execute procedure data_status_audit();

create or replace function rgs_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO rgs_history (rgs_id, rgs, bezeichnung, adresse_id, created_on, created_by, changed_by, action,
                                 action_timestamp)
        VALUES (OLD.id, OLD.rgs, OLD.bezeichnung, OLD.adresse_id, OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO rgs_history (rgs_id, rgs, bezeichnung, adresse_id, created_on, created_by, changed_by, action,
                                 action_timestamp)
        VALUES (NEW.id, NEW.rgs, NEW.bezeichnung, NEW.adresse_id, NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO rgs_history (rgs_id, rgs, bezeichnung, adresse_id, created_on, created_by, changed_by, action,
                                 action_timestamp)
        VALUES (NEW.id, NEW.rgs, NEW.bezeichnung, NEW.adresse_id, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function rgs_audit() owner to ibosng;

create or replace function plz_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO plz_history (plz_id, plz, ort, bundesland, created_on, created_by, changed_by, action,
                                 action_timestamp)
        VALUES (OLD.id, OLD.plz, OLD.ort, OLD.bundesland, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO plz_history (plz_id, plz, ort, bundesland, created_on, created_by, changed_by, action,
                                 action_timestamp)
        VALUES (NEW.id, NEW.plz, NEW.ort, NEW.bundesland, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO plz_history (plz_id, plz, ort, bundesland, created_on, created_by, changed_by, action,
                                 action_timestamp)
        VALUES (NEW.id, NEW.plz, NEW.ort, NEW.bundesland, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function plz_audit() owner to ibosng;

create trigger plz_insert
    after insert
    on plz
    for each row
execute procedure plz_audit();

create trigger plz_update
    after update
    on plz
    for each row
execute procedure plz_audit();

create trigger plz_delete
    after delete
    on plz
    for each row
execute procedure plz_audit();

create or replace function vorwahl_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vorwahl_history (vorwahl_id, vorwahl, land_vorwahl, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.vorwahl, OLD.land_vorwahl, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vorwahl_history (vorwahl_id, vorwahl, land_vorwahl, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.vorwahl, NEW.land_vorwahl, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vorwahl_history (vorwahl_id, vorwahl, land_vorwahl, created_on, created_by, changed_by, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.vorwahl, NEW.land_vorwahl, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function vorwahl_audit() owner to ibosng;

create trigger vorwahl_insert
    after insert
    on vorwahl
    for each row
execute procedure vorwahl_audit();

create trigger vorwahl_update
    after update
    on vorwahl
    for each row
execute procedure vorwahl_audit();

create trigger vorwahl_delete
    after delete
    on vorwahl
    for each row
execute procedure vorwahl_audit();

create or replace function teilnehmer_telefon_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_telefon_history (teilnehmer_telefon_id, teilnehmer_id, telefon_id, created_on, action,
                                                action_timestamp)
        VALUES (OLD.id, OLD.teilnehmer_id, OLD.telefon_id, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_telefon_history (teilnehmer_telefon_id, teilnehmer_id, telefon_id, created_on, action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.telefon_id, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_telefon_history (teilnehmer_telefon_id, teilnehmer_id, telefon_id, created_on, action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.telefon_id, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function teilnehmer_telefon_audit() owner to ibosng;

create trigger teilnehmer_telefon_insert
    after insert
    on teilnehmer_telefon
    for each row
execute procedure teilnehmer_telefon_audit();

create trigger teilnehmer_telefon_update
    after update
    on teilnehmer_telefon
    for each row
execute procedure teilnehmer_telefon_audit();

create trigger teilnehmer_telefon_delete
    after delete
    on teilnehmer_telefon
    for each row
execute procedure teilnehmer_telefon_audit();

create or replace function teilnehmer_staging_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_staging_history (teilnehmer_staging_id, titel, vorname, nachname, geschlecht, sv_nummer,
                                                geburtsdatum,
                                                buchungsstatus, anmerkung, zubuchung, geplant, eintritt, austritt, rgs,
                                                massnahmennummer,
                                                veranstaltungsnummer, email, import_filename, betreuer_titel,
                                                betreuer_vorname,
                                                betreuer_nachname, telefon, status, plz, ort, strasse, nation, info,
                                                seminar_identifier,
                                                seminar_start_date, seminar_end_date, seminar_start_time, seminar_type,
                                                trainer,
                                                landesvorwahl, vorwahl, telefon_nummer, teilnahmebeginn,
                                                anmeldestatus, ams, abmeldegrund, anwesenheit_in_ue,
                                                entschuldigte_abwesenheit_in_ue, unentschuldigte_abwesenheit_in_ue,
                                                summe_erfasste_an_und_abwesenheit,
                                                anwesenheit_erfasst_bis, absolute_anwesenheit,
                                                relative_anwesenheit, source, teilnehmer_id, created_on, created_by, changed_by,
                                                action,
                                                action_timestamp)
        VALUES (OLD.id, OLD.titel, OLD.vorname, OLD.nachname, OLD.geschlecht, OLD.sv_nummer, OLD.geburtsdatum,
                OLD.buchungsstatus, OLD.anmerkung, OLD.zubuchung, OLD.geplant, OLD.eintritt, OLD.austritt, OLD.rgs,
                OLD.massnahmennummer, OLD.veranstaltungsnummer, OLD.email, OLD.import_filename, OLD.betreuer_titel,
                OLD.betreuer_vorname, OLD.betreuer_nachname, OLD.telefon, OLD.status, OLD.plz, OLD.ort, OLD.strasse,
                OLD.nation, OLD.info, OLD.seminar_identifier, OLD.seminar_start_date, OLD.seminar_end_date,
                OLD.seminar_start_time,
                OLD.seminar_type, OLD.trainer, OLD.landesvorwahl, OLD.vorwahl, OLD.telefon_nummer,
                OLD.teilnahmebeginn, OLD.anmeldestatus, OLD.ams, OLD.abmeldegrund,
                OLD.anwesenheit_in_ue, OLD.entschuldigte_abwesenheit_in_ue, OLD.unentschuldigte_abwesenheit_in_ue,
                OLD.summe_erfasste_an_und_abwesenheit,
                OLD.anwesenheit_erfasst_bis, OLD.absolute_anwesenheit, OLD.relative_anwesenheit, OLD.source, OLD.teilnehmer_id,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_staging_history (teilnehmer_staging_id, titel, vorname, nachname, geschlecht, sv_nummer,
                                                geburtsdatum,
                                                buchungsstatus, anmerkung, zubuchung, geplant, eintritt, austritt, rgs,
                                                massnahmennummer,
                                                veranstaltungsnummer, email, import_filename, betreuer_titel,
                                                betreuer_vorname,
                                                betreuer_nachname, telefon, status, plz, ort, strasse, nation, info,
                                                seminar_identifier,
                                                seminar_start_date, seminar_end_date, seminar_start_time, seminar_type,
                                                trainer,
                                                landesvorwahl, vorwahl, telefon_nummer, teilnahmebeginn,
                                                anmeldestatus, ams, abmeldegrund, anwesenheit_in_ue,
                                                entschuldigte_abwesenheit_in_ue, unentschuldigte_abwesenheit_in_ue,
                                                summe_erfasste_an_und_abwesenheit,
                                                anwesenheit_erfasst_bis, absolute_anwesenheit,
                                                relative_anwesenheit, source, teilnehmer_id, created_on, created_by, changed_by,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.titel, NEW.vorname, NEW.nachname, NEW.geschlecht, NEW.sv_nummer, NEW.geburtsdatum,
                NEW.buchungsstatus, NEW.anmerkung, NEW.zubuchung, NEW.geplant, NEW.eintritt, NEW.austritt, NEW.rgs,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.email, NEW.import_filename, NEW.betreuer_titel,
                NEW.betreuer_vorname, NEW.betreuer_nachname, NEW.telefon, NEW.status, NEW.plz, NEW.ort, NEW.strasse,
                NEW.nation, NEW.info, NEW.seminar_identifier, NEW.seminar_start_date, NEW.seminar_end_date,
                NEW.seminar_start_time,
                NEW.seminar_type, NEW.trainer, NEW.landesvorwahl, NEW.vorwahl, NEW.telefon_nummer,
                NEW.teilnahmebeginn, NEW.anmeldestatus, NEW.ams, NEW.abmeldegrund,
                NEW.anwesenheit_in_ue, NEW.entschuldigte_abwesenheit_in_ue, NEW.unentschuldigte_abwesenheit_in_ue,
                NEW.summe_erfasste_an_und_abwesenheit,
                NEW.anwesenheit_erfasst_bis, NEW.absolute_anwesenheit, NEW.relative_anwesenheit, NEW.source, NEW.teilnehmer_id,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_staging_history (teilnehmer_staging_id, titel, vorname, nachname, geschlecht, sv_nummer,
                                                geburtsdatum,
                                                buchungsstatus, anmerkung, zubuchung, geplant, eintritt, austritt, rgs,
                                                massnahmennummer,
                                                veranstaltungsnummer, email, import_filename, betreuer_titel,
                                                betreuer_vorname,
                                                betreuer_nachname, telefon, status, plz, ort, strasse, nation, info,
                                                seminar_identifier,
                                                seminar_start_date, seminar_end_date, seminar_start_time, seminar_type,
                                                trainer,
                                                landesvorwahl, vorwahl, telefon_nummer, teilnahmebeginn,
                                                anmeldestatus, ams, abmeldegrund, anwesenheit_in_ue,
                                                entschuldigte_abwesenheit_in_ue, unentschuldigte_abwesenheit_in_ue,
                                                summe_erfasste_an_und_abwesenheit,
                                                anwesenheit_erfasst_bis, absolute_anwesenheit,
                                                relative_anwesenheit, source, teilnehmer_id, created_on, created_by, changed_by,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.titel, NEW.vorname, NEW.nachname, NEW.geschlecht, NEW.sv_nummer, NEW.geburtsdatum,
                NEW.buchungsstatus, NEW.anmerkung, NEW.zubuchung, NEW.geplant, NEW.eintritt, NEW.austritt, NEW.rgs,
                NEW.massnahmennummer, NEW.veranstaltungsnummer, NEW.email, NEW.import_filename, NEW.betreuer_titel,
                NEW.betreuer_vorname, NEW.betreuer_nachname, NEW.telefon, NEW.status, NEW.plz, NEW.ort, NEW.strasse,
                NEW.nation, NEW.info, NEW.seminar_identifier, NEW.seminar_start_date, NEW.seminar_end_date,
                NEW.seminar_start_time,
                NEW.seminar_type, NEW.trainer, NEW.landesvorwahl, NEW.vorwahl, NEW.telefon_nummer,
                NEW.teilnahmebeginn, NEW.anmeldestatus, NEW.ams, NEW.abmeldegrund,
                NEW.anwesenheit_in_ue, NEW.entschuldigte_abwesenheit_in_ue, NEW.unentschuldigte_abwesenheit_in_ue,
                NEW.summe_erfasste_an_und_abwesenheit,
                NEW.anwesenheit_erfasst_bis, NEW.absolute_anwesenheit, NEW.relative_anwesenheit, NEW.source, NEW.teilnehmer_id,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function teilnehmer_staging_audit() owner to ibosng;

create trigger teilnehmer_staging_insert
    after insert
    on teilnehmer_staging
    for each row
execute procedure teilnehmer_staging_audit();

create trigger teilnehmer_staging_update
    after update
    on teilnehmer_staging
    for each row
execute procedure teilnehmer_staging_audit();

create trigger teilnehmer_staging_delete
    after delete
    on teilnehmer_staging
    for each row
execute procedure teilnehmer_staging_audit();

create or replace function widget_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO widget_history (widget_id, name, description, status, created_on, created_by, changed_by, action,
                                    action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.description, OLD.status, OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO widget_history (widget_id, name, description, status, created_on, created_by, changed_by, action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.description, NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO widget_history (widget_id, name, description, status, created_on, created_by, changed_by, action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.description, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function widget_audit() owner to ibosng;

create trigger widget_insert
    after insert
    on widget
    for each row
execute procedure widget_audit();

create trigger widget_update
    after update
    on widget
    for each row
execute procedure widget_audit();

create trigger widget_delete
    after delete
    on widget
    for each row
execute procedure widget_audit();

create or replace function seminar_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO seminar_history (seminar_id, seminar_nummer, project, identifier, type, bezeichnung, start_date,
                                     end_date, start_time, end_time, status, created_on, created_by, changed_by,
                                     changed_on, action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.seminar_nummer, OLD.project, OLD.identifier, OLD.type, OLD.bezeichnung, OLD.start_date,
                OLD.end_date, OLD.start_time, OLD.end_time, OLD.status, OLD.created_on, OLD.created_by, OLD.changed_by,
                now(),
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO seminar_history (seminar_id, seminar_nummer, project, identifier, type, bezeichnung, start_date,
                                     end_date, start_time, end_time, status, created_on, created_by, changed_by,
                                     changed_on, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.seminar_nummer, NEW.project, NEW.identifier, NEW.type, NEW.bezeichnung, NEW.start_date,
                NEW.end_date, NEW.start_time, NEW.end_time, NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by,
                now(),
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO seminar_history (seminar_id, seminar_nummer, project, identifier, type, bezeichnung, start_date,
                                     end_date, start_time, end_time, status, created_on, created_by, changed_by,
                                     changed_on, action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.seminar_nummer, NEW.project, NEW.identifier, NEW.type, NEW.bezeichnung, NEW.start_date,
                NEW.end_date, NEW.start_time, OLD.end_time, NEW.status, NEW.created_on, NEW.created_by, NULL, now(),
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function seminar_audit() owner to ibosng;

create trigger seminar_insert
    after insert
    on seminar
    for each row
execute procedure seminar_audit();

create trigger seminar_update
    after update
    on seminar
    for each row
execute procedure seminar_audit();

create trigger seminar_delete
    after delete
    on seminar
    for each row
execute procedure seminar_audit();

create or replace function teilnehmer_2_seminar_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, course_id, created_on, action,
                                                  action_timestamp)
        VALUES (OLD.id, OLD.teilnehmer_id, OLD.seminar_id, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, course_id, created_on, action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_2_seminar_history (teilnehmer_2_seminar_id, teilnehmer_id, course_id, created_on, action,
                                                  action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.seminar_id, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function teilnehmer_2_seminar_audit() owner to ibosng;

create trigger teilnehmer_2_seminar_insert
    after insert
    on teilnehmer_2_seminar
    for each row
execute procedure teilnehmer_2_seminar_audit();

create trigger teilnehmer_2_seminar_update
    after update
    on teilnehmer_2_seminar
    for each row
execute procedure teilnehmer_2_seminar_audit();

create trigger teilnehmer_2_seminar_delete
    after delete
    on teilnehmer_2_seminar
    for each row
execute procedure teilnehmer_2_seminar_audit();

