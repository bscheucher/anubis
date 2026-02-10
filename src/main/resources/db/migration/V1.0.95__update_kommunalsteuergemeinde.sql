alter table kommunalsteuergemeinde drop column adresse;
alter table kommunalsteuergemeinde_history drop column adresse;

alter table kommunalsteuergemeinde drop column lhr_id;
alter table kommunalsteuergemeinde_history drop column lhr_id;

alter table kommunalsteuergemeinde add column dienstort_plz integer;
alter table kommunalsteuergemeinde_history add column dienstort_plz integer;

alter table kommunalsteuergemeinde add column lhr_land integer references land(id);
alter table kommunalsteuergemeinde_history add column lhr_land integer;

alter table kommunalsteuergemeinde add column lhr_plz integer;
alter table kommunalsteuergemeinde_history add column lhr_plz integer;

alter table kommunalsteuergemeinde add column lhr_name text;
alter table kommunalsteuergemeinde_history add column lhr_name text;

create or replace function kommunalsteuergemeinde_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO kommunalsteuergemeinde_history (kommunalsteuergemeinde_id, dienstort_plz, lhr_land, lhr_plz, lhr_name, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (OLD.id, OLD.dienstort_plz, OLD.lhr_land, OLD.lhr_plz, OLD.lhr_name, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO kommunalsteuergemeinde_history (kommunalsteuergemeinde_id, dienstort_plz, lhr_land, lhr_plz, lhr_name, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.dienstort_plz, NEW.lhr_land, NEW.lhr_plz, NEW.lhr_name, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO kommunalsteuergemeinde_history (kommunalsteuergemeinde_id, dienstort_plz, lhr_land, lhr_plz, lhr_name, created_on, created_by, changed_by, action, action_timestamp)
        VALUES (NEW.id, NEW.dienstort_plz, NEW.lhr_land, NEW.lhr_plz, NEW.lhr_name, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1010, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1020, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1030, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1040, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1050, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1060, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1070, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1080, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1090, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1100, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1110, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1120, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1130, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1140, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1150, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1160, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1170, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1180, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1190, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1200, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1210, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1220, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (1230, (select id from land where land_code = 'AT'), 1010, 'Wien', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (2020, (select id from land where land_code = 'AT'), 2020, 'Hollabrunn', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (2320, (select id from land where land_code = 'AT'), 2320, 'Schwechat', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (2340, (select id from land where land_code = 'AT'), 2340, 'Mödling', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (2410, (select id from land where land_code = 'AT'), 2410, 'Hainburg a. d. Donau', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (2460, (select id from land where land_code = 'AT'), 2460, 'Bruck an der Leitha', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (2500, (select id from land where land_code = 'AT'), 2500, 'Baden', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (2620, (select id from land where land_code = 'AT'), 2620, 'Neunkirchen', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (2680, (select id from land where land_code = 'AT'), 2680, 'Semmering', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (2700, (select id from land where land_code = 'AT'), 2700, 'Wr. Neustadt', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3100, (select id from land where land_code = 'AT'), 3100, 'St. Pölten', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3270, (select id from land where land_code = 'AT'), 3270, 'Scheibbs', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3300, (select id from land where land_code = 'AT'), 3300, 'Amstetten', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3390, (select id from land where land_code = 'AT'), 3390, 'Melk', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3430, (select id from land where land_code = 'AT'), 3430, 'Tulln', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3500, (select id from land where land_code = 'AT'), 3500, 'Krems an der Donau', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3580, (select id from land where land_code = 'AT'), 3580, 'Horn', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3830, (select id from land where land_code = 'AT'), 3830, 'Waidhofen an der Thaya', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3910, (select id from land where land_code = 'AT'), 3910, 'Zwettl', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (3950, (select id from land where land_code = 'AT'), 3950, 'Gmünd', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4020, (select id from land where land_code = 'AT'), 4020, 'Linz', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4240, (select id from land where land_code = 'AT'), 4240, 'Freistadt', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4560, (select id from land where land_code = 'AT'), 4560, 'Kirchdorf an der Krems', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4600, (select id from land where land_code = 'AT'), 4600, 'Wels', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4642, (select id from land where land_code = 'AT'), 4642, 'Sattledt', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4710, (select id from land where land_code = 'AT'), 4710, 'Grieskirchen', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4780, (select id from land where land_code = 'AT'), 4780, 'Schärding', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4810, (select id from land where land_code = 'AT'), 4810, 'Gmunden', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4840, (select id from land where land_code = 'AT'), 4840, 'Vöcklabruck', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (4910, (select id from land where land_code = 'AT'), 4910, 'Ried im Innkreis', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (5020, (select id from land where land_code = 'AT'), 5020, 'Salzburg', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (5121, (select id from land where land_code = 'AT'), 5121, 'Ostermiething', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (5280, (select id from land where land_code = 'AT'), 5280, 'Branau am Inn', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (5400, (select id from land where land_code = 'AT'), 5400, 'Hallein', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (5500, (select id from land where land_code = 'AT'), 5500, 'Bischofshofen', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (5580, (select id from land where land_code = 'AT'), 5580, 'Tamsweg', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (5600, (select id from land where land_code = 'AT'), 5600, 'Sankt Johann im Pongau', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (5700, (select id from land where land_code = 'AT'), 5700, 'Zell am See', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (5730, (select id from land where land_code = 'AT'), 5730, 'Mittersill', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6020, (select id from land where land_code = 'AT'), 6020, 'Innsbruck', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6200, (select id from land where land_code = 'AT'), 6200, 'Jenbach', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6300, (select id from land where land_code = 'AT'), 6300, 'Wörgl', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6370, (select id from land where land_code = 'AT'), 6370, 'Kitzbühel', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6460, (select id from land where land_code = 'AT'), 6460, 'Imst', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6500, (select id from land where land_code = 'AT'), 6500, 'Landeck', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6600, (select id from land where land_code = 'AT'), 6600, 'Reutte', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6700, (select id from land where land_code = 'AT'), 6700, 'Bludenz', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6800, (select id from land where land_code = 'AT'), 6800, 'Feldkirch', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6850, (select id from land where land_code = 'AT'), 6850, 'Dornbirn', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (6900, (select id from land where land_code = 'AT'), 6900, 'Bregenz', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (7000, (select id from land where land_code = 'AT'), 7000, 'Eisenstadt', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (7100, (select id from land where land_code = 'AT'), 7100, 'Neusiedl am See', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (7210, (select id from land where land_code = 'AT'), 7210, 'Mattersburg', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (7400, (select id from land where land_code = 'AT'), 7400, 'Oberwart', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (7540, (select id from land where land_code = 'AT'), 7540, 'Güssing', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (8011, (select id from land where land_code = 'AT'), 8011, 'Graz', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (9020, (select id from land where land_code = 'AT'), 9020, 'Klagenfurt am Wörthersee', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (9500, (select id from land where land_code = 'AT'), 9500, 'Villach', current_user);
insert into kommunalsteuergemeinde (dienstort_plz, lhr_land, lhr_plz, lhr_name, created_by) values (9900, (select id from land where land_code = 'AT'), 9900, 'Lienz', current_user);
