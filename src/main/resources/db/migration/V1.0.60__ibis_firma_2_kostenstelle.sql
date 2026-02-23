

create table if not exists ibis_firma_2_kostenstelle
(
    id         integer generated always as identity primary key,
    ibis_firma       integer references ibis_firma(id),
    kostenstelle      integer references kostenstelle(id),
    created_on timestamp default CURRENT_TIMESTAMP not null,
    created_by text                                not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists ibis_firma_2_kostenstelle_history
(
    id               integer generated always as identity primary key,
    ibis_firma_2_kostenstelle_id        integer   not null,
    ibis_firma       integer references ibis_firma(id),
    kostenstelle      integer references kostenstelle(id),
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function ibis_firma_2_kostenstelle_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO ibis_firma_2_kostenstelle_history (ibis_firma_2_kostenstelle_id, ibis_firma, kostenstelle, created_on, created_by, changed_by,
                                                       action,
                                                       action_timestamp)
        VALUES (OLD.id, OLD.ibis_firma, OLD.kostenstelle, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO ibis_firma_2_kostenstelle_history (ibis_firma_2_kostenstelle_id, ibis_firma, kostenstelle, created_on, created_by, changed_by,
                                                       action,
                                                       action_timestamp)
        VALUES (NEW.id, NEW.ibis_firma, NEW.kostenstelle, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO ibis_firma_2_kostenstelle_history (ibis_firma_2_kostenstelle_id, ibis_firma, kostenstelle, created_on, created_by, changed_by,
                                                       action,
                                                       action_timestamp)
        VALUES (NEW.id, NEW.ibis_firma, NEW.kostenstelle, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function ibis_firma_2_kostenstelle_audit() owner to ibosng;

create trigger ibis_firma_2_kostenstelle_insert
    after insert
    on ibis_firma_2_kostenstelle
    for each row
execute procedure ibis_firma_2_kostenstelle_audit();

create trigger ibis_firma_2_kostenstelle_update
    after update
    on ibis_firma_2_kostenstelle
    for each row
execute procedure ibis_firma_2_kostenstelle_audit();

create trigger ibis_firma_2_kostenstelle_delete
    after delete
    on ibis_firma_2_kostenstelle
    for each row
execute procedure ibis_firma_2_kostenstelle_audit();


truncate table kostenstelle cascade;

INSERT INTO kostenstelle (nummer, bezeichnung, status, created_on, created_by)
VALUES
    (10, 'MBÖ– Leitung', 1, CURRENT_TIMESTAMP, current_user),
    (11, 'Vorarlberg', 1, CURRENT_TIMESTAMP, current_user),
    (12, 'Tirol', 1, CURRENT_TIMESTAMP, current_user),
    (13, 'Salzburg', 1, CURRENT_TIMESTAMP, current_user),
    (14, 'Kärnten', 1, CURRENT_TIMESTAMP, current_user),
    (15, 'Steiermark', 1, CURRENT_TIMESTAMP, current_user),
    (16, 'Burgenland', 1, CURRENT_TIMESTAMP, current_user),
    (17, 'Oberösterreich', 1, CURRENT_TIMESTAMP, current_user),
    (18, 'Niederösterreich', 1, CURRENT_TIMESTAMP, current_user),
    (19, 'Wien', 1, CURRENT_TIMESTAMP, current_user),
    (20, 'ÜR Projekte', 1, CURRENT_TIMESTAMP, current_user),
    (21, 'IGG TIROL', 1, CURRENT_TIMESTAMP, current_user),
    (22, 'ServiceCenter', 1, CURRENT_TIMESTAMP, current_user),
    (23, 'Online Marketing', 1, CURRENT_TIMESTAMP, current_user),
    (24, 'WorkLifeLearn', 1, CURRENT_TIMESTAMP, current_user),
    (25, 'Aspidoo', 1, CURRENT_TIMESTAMP, current_user),
    (26, 'Content Team', 1, CURRENT_TIMESTAMP, current_user),
    (28, 'Wien - Jugend', 1, CURRENT_TIMESTAMP, current_user),
    (29, 'Wien - Sprachen', 1, CURRENT_TIMESTAMP, current_user),
    (31, 'TrainerCommunity', 1, CURRENT_TIMESTAMP, current_user),
    (40, 'Jugend', 1, CURRENT_TIMESTAMP, current_user),
    (41, 'Sprachen & Integration', 1, CURRENT_TIMESTAMP, current_user),
    (42, 'Erwachsene ', 1, CURRENT_TIMESTAMP, current_user),
    (43, 'Frauen', 1, CURRENT_TIMESTAMP, current_user),
    (45, 'Beratung & Coaching', 1, CURRENT_TIMESTAMP, current_user),
    (50, 'KAOS Bildungsservice', 1, CURRENT_TIMESTAMP, current_user),
    (51, 'MBU', 1, CURRENT_TIMESTAMP, current_user),
    (61, 'Future Lab', 1, CURRENT_TIMESTAMP, current_user),
    (80, 'PSC (Produkt Service Center)', 1, CURRENT_TIMESTAMP, current_user),
    (81, 'IT Services', 1, CURRENT_TIMESTAMP, current_user),
    (82, 'Application Management', 1, CURRENT_TIMESTAMP, current_user),
    (83, 'Standortmanagement', 1, CURRENT_TIMESTAMP, current_user),
    (90, 'GF', 1, CURRENT_TIMESTAMP, current_user),
    (91, 'IDC', 1, CURRENT_TIMESTAMP, current_user),
    (92, 'Betriebsrat', 1, CURRENT_TIMESTAMP, current_user),
    (93, 'Performance&Finance', 1, CURRENT_TIMESTAMP, current_user),
    (94, 'Aspire Beteiligungs Gmb', 1, CURRENT_TIMESTAMP, current_user),
    (95, 'Aspire Education GmbH', 1, CURRENT_TIMESTAMP, current_user),
    (96, 'Aspire Education GmbH', 1, CURRENT_TIMESTAMP, current_user),
    (111, 'Brandenburg', 1, CURRENT_TIMESTAMP, current_user),
    (112, 'Brandenburg nicht auswÃ¤hlen', 1, CURRENT_TIMESTAMP, current_user),
    (113, 'Mecklenburg-Vorpommern Ost', 1, CURRENT_TIMESTAMP, current_user),
    (115, 'Berlin', 1, CURRENT_TIMESTAMP, current_user),
    (116, 'Bayern', 1, CURRENT_TIMESTAMP, current_user),
    (117, 'Baden-Württemberg', 1, CURRENT_TIMESTAMP, current_user),
    (191, 'IDC', 1, CURRENT_TIMESTAMP, current_user),
    (290, 'AKÜ - GF', 1, CURRENT_TIMESTAMP, current_user),
    (300, 'GWL - Gemeinsam Wohnen & Leben', 1, CURRENT_TIMESTAMP, current_user),
    (312, 'iGG - Tirol', 1, CURRENT_TIMESTAMP, current_user),
    (313, 'iGG - Salzburg', 1, CURRENT_TIMESTAMP, current_user),
    (317, 'iGG Oberösterreich ', 1, CURRENT_TIMESTAMP, current_user);



insert into ibis_firma_2_kostenstelle (ibis_firma, kostenstelle, created_by) values
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 14 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 313 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 15 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 92 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 45 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 16 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 22 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 40 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 17 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 25 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 41 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 18 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 23 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 42 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 19 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 43 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 31 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 28 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 93 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 29 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 61 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 80 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 81 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 82 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 24 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 10 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 83 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 26 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 11 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 90 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 12 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 91 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 317 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 13 ), current_user),
     ((select id from ibis_firma where lhr_nr = 1 ), (select id from kostenstelle where nummer = 20 ), current_user),
     ((select id from ibis_firma where lhr_nr = 2 ), (select id from kostenstelle where nummer = 300 ), current_user),
     ((select id from ibis_firma where lhr_nr = 2 ), (select id from kostenstelle where nummer = 312 ), current_user),
     ((select id from ibis_firma where lhr_nr = 2 ), (select id from kostenstelle where nummer = 317 ), current_user),
     ((select id from ibis_firma where lhr_nr = 3 ), (select id from kostenstelle where nummer = 51 ), current_user),
     ((select id from ibis_firma where lhr_nr = 3 ), (select id from kostenstelle where nummer = 50 ), current_user),
     ((select id from ibis_firma where lhr_nr = 10 ), (select id from kostenstelle where nummer = 90 ), current_user),
     ((select id from ibis_firma where lhr_nr = 10 ), (select id from kostenstelle where nummer = 94 ), current_user),
     ((select id from ibis_firma where lhr_nr = 11 ), (select id from kostenstelle where nummer = 191 ), current_user);
