alter table zeitbuchuntyp rename to zeitbuchungstyp;
alter table zeitbuchuntyp_history rename to zeitbuchungstyp_history;

alter table zeitbuchungstyp
    add column lhr_kz TEXT;
alter table zeitbuchungstyp_history
    add column lhr_kz TEXT;

create or replace function zeitbuchuntyp_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeitbuchungstyp_history (zeitbuchuntyp_id, type, lhr_kz,
                                           created_on, created_by, changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (OLD.id, OLD.type, OLD.lhr_kz,
                OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeitbuchungstyp_history (zeitbuchuntyp_id, type, lhr_kz,
                                           created_on, created_by, changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.lhr_kz,
                NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', NOW());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeitbuchungstyp_history (zeitbuchuntyp_id, type, lhr_kz,
                                           created_on, created_by, changed_on, changed_by,
                                           action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.lhr_kz,
                NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function zeitbuchuntyp_audit owner to ibosng;


truncate table zeitbuchungstyp cascade;

insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Seminar',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Ersatzruhe',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Seminar-Vertretung',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'unterstützendes Personal',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Büro',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Weiterbildung',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Bildungsfreistellung', 'BIKAR', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Aufbau',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Externer Einsatz (Support vor Ort)',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Arztbesuch', 'ARZT', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Behördenweg und sonstiges', 'BEHOE', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Krankenstand', 'KRANK', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Sonderurlaub', 'SURL', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Urlaub', 'URLAU', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Zeitausgleich',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Pflegefreistellung', 'PFLG1', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Mittagspause',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Administration',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Dienstfreistellung', 'DFREI', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'unbezahlter Urlaub', 'UNURL', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'ibis Event',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Lernstudio',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'ibis Event Konsum',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Dienstfreistellung Betriebsversammlung', 'DFBET', current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Streik',null, current_user);
insert into zeitbuchungstyp (type, lhr_kz, created_by) values ( 'Arbeitsunfall', 'UNFAL', current_user);
