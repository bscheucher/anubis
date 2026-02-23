update vertragsdaten set dienstort = null;

delete from dienstort;

ALTER TABLE dienstort add column lhr_nr integer;
ALTER TABLE dienstort_history add column lhr_nr integer;

ALTER TABLE dienstort add column lhr_kz text;
ALTER TABLE dienstort_history add column lhr_kz text;

ALTER TABLE dienstort add column short_name text;
ALTER TABLE dienstort_history add column short_name text;

create or replace function dienstort_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO dienstort_history (dienstort_id, name, short_name, lhr_kz, lhr_nr, adresse, telefon, email, status, created_on, created_by,
                                       changed_by,
                                       action,
                                       action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.short_name, OLD.lhr_kz, OLD.lhr_nr, OLD.adresse, OLD.telefon, OLD.email, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO dienstort_history (dienstort_id, name, short_name, lhr_kz, lhr_nr, adresse, telefon, email, status, created_on,
                                       created_by,
                                       changed_by,
                                       action,
                                       action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.short_name, NEW.lhr_kz, NEW.lhr_nr, NEW.adresse, NEW.telefon, NEW.email, NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO dienstort_history (dienstort_id, name, short_name, lhr_kz, lhr_nr, adresse, telefon, email, status, created_on,
                                       created_by,
                                       changed_by,
                                       action,
                                       action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.short_name, NEW.lhr_kz, NEW.lhr_nr, NEW.adresse, NEW.telefon, NEW.email, NEW.status, NEW.created_on, NEW.created_by, NULL,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function dienstort_audit() owner to ibosng;




insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Amstetten (Waidhofener Str. 42-42b)', 'Amstetten Waidhofnerstraße',233, 'Amstetten/Greinsfurt', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Baden (Goethegasse 14)', 'Baden Goethegasse',230, 'Baden', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Bischofshofen (Salzburger Straße 46)', 'Bischofshofen Salzburger Straße 45',144, 'B-Hofen Eigensinn', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Bludenz (Bahnhofplatz 1c -Rätikoncenter)', 'Bludenz Bahnhofplatz',250, 'Bludenz', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Bludenz (Untersteinstraße 8)', 'null',null, 'Bludenz', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Bregenz (Bachgasse 1a)', 'Bregenz Bachgasse 1a',129, 'Bregenz Bachgasse', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Bruck an der Leitha (Leithagürtel 2)', 'Bruckneudorf Leithagürtel',154, 'Bruck NEU', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Dornbirn (Bildgasse 18, Top 3)', 'Dornbirn Bildgasse',260, 'Dornbirn', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Dornbirn (Schulgasse 18)', 'Dornbirn Schulgasse',200, 'Schulgasse', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Eisenstadt (Esterhazystraße 22/4)', 'Eisenstadt Esterhazystraße 22',133, 'Eisenstadt', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Feldkirch (Albert-Schädler-Straße 12)', 'Feldkirch Albert-Schädler-Straße',80, 'Feldkirch', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Feldkirch (Beim Gräble 2 - Lamello)', 'null',null, 'Feldkirch', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Feldkirch (Schießstätte 12, Ganahl-Areal)', 'Feldkirch Schießstäte 12 (Ganahl-Areal)',37, 'Feldkirch', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Feldkirch (Schießstätte 16, Ganahl-Areal)', 'null',null, 'Feldkirch', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Freistadt (St. Peter Str. 7)', 'Freistadt St.Peter Straße 7',147, 'Freistadt (St. Peter', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Gmünd (Zweiländerstraße 8)', 'Gmünd Zweiländerstraße 8',12, 'Gmünd', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Grieskirchen (Trattnachtalstraße 5-7)', 'Grieskirchen Trattnachtalstraße',155, 'Grieskirchen', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Güssing Technologiezentrum', 'Güssing Europastraße 1',99, 'Güssing TZ', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Hall - Untere Lend 12a - KAOS', 'Hall in Tirol Untere Lend',995, 'HAL01', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Hallein (Löwensternstraße 18)', 'Hallein Löwensternstraße',175, 'Hallein-LÖW', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Hollabrunn (Hauptplatz12)', 'Grabern Hauptplatz',27, 'Hollabrunn', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Horn (Wiener Straße 2)', 'Horn Wiener Straße',201, 'Kunst Haus Horn', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Imst (Kramergasse 9-11)', 'Imst Kramergasse 11',65, 'Imst', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Imst - Kramerg.11 - KAOS', 'Imst Kramergasse',987, 'IMS01', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck (Eduard Bodem Gasse 8)', 'Innsbruck Eduard-Bodem-Gasse 8',149, 'Innsbruck Eduard Bod', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck (Fürstenweg 87)', 'Innsbruck Fürstenweg 87',56, 'Innsbruck', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck (J. Thomanstraße 12)', 'Innsbruck Josef-Thoman-Straße 12',165, '', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck (Kranebitter Allee 97)', 'Innsbruck Kranebitter Allee 97',227, 'Gärtnerei', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck (Leopoldstr.31a)', 'Innsbruck Leopoldstraße',121, 'ISB (Deliris)', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck (Leopoldstraße 24)', 'Innsbruck Leopoldstraße',121, 'Innsbruck IQ-Shop', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck (Resselstraße 18)', 'Innsbruck Resselstraße 18',64, 'Innsbruck', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck - Andreas-Hofer-Straße 44 - KAOS', 'Innsbruck Andreas-Hofer-Straße',997, 'INN05', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck - Mitterweg 25a - KAOS', 'Innsbruck Mitterweg',998, 'INN03', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck - Südtiroler Platz 14-16 - KAOS', 'Innsbruck Südtiroler Platz',999, 'INN01', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck Dörrstraße 85', 'Innsbruck Dörrstraße',401, 'Innsbruck Dörrstraße', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck Maria-Theresien-Straße 40', 'Innsbruck Maria-Theresien-Str.',146, 'MTS Innsbruck', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck, Hypo Passage 2', 'Innsbruck Hypo Passage 2',226, 'Innsbruck, Hypo Pass', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck, Sebastian-Kneipp-Weg 27', 'Innsbruck Sebastian-Kneipp-Weg',197, 'Innsbruck', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Innsbruck, Südtirolerplatz 14-16', 'Innsbruck Südtirolerplatz',228, 'Südtirolerplatz', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Jenbach (Austraße 21)', 'Jenbach Austraße 21',105, 'Jenbach FS', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Kitzbühel (Achenweg 22) KAOS', 'Kitzbühel Achenweg',988, 'KITZB', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Krems (Dr. Karl Dorrekstraße 20)', 'Krems an der Donau Dr. Karl Dorrekstraße',35, 'Krems', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Kufstein (Kreuzgasse 3) Top 2 - KAOS', 'Kufstein Kreuzgasse',989, 'Kufstein', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Kufstein - Kinkstr. 12 - KAOS', 'Kufstein Kinkstraße',990, 'KUF02', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Landeck (Bruggfeldstraße 5)', 'Landeck Bruggfeldstrasse 5',50, 'Landeck', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Landeck (Flirstraße 30a) -KAOS', 'null',null, 'Landeck - KAOS', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Landeck (Schrofensteinstraße 1)  - KAOS', 'Landeck Schrofensteinstraße',985, 'LAN 02', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Landeck - Römerstr. 14 - KAOS', 'Landeck Römerstraße',986, 'LAN01', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Lienz (Schweizergasse 18)', 'Lienz Schweizergasse',315, 'Lienz-Schweizergasse', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Lienz - Kärntner-Str. 42 - KAOS', 'Lienz Kärtner Straße',983, 'LIE01', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Linz (Hafferlstraße 7)', 'Linz Hafferlstraße',19, 'Linz', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Linz Paul-Hahn-Straße 1-3', 'Linz Paul-Hahn-Straße 1-3',318, 'Linz', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Melk (Wiener Straße 45)', 'Melk Wiener Straße 45',86, 'Melk', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Reutte (Lindenstr. 35) - KAOS', 'Reutte Lindenstraße',984, 'Reutte - KAOS', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Reutte (Lindenstraße 35)', 'Breitenwang Lindenstraße 35',43, 'Reutte - ibis', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Salzburg (Eichstraße 3)', 'Salzburg Eichstraße 3',100, 'Salzburg Eichstraße', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Salzburg (Münchner Bundesstr.158-160/2.OG)', 'Salzburg Münchner Bundesstraße',22, 'Münchner Bundesstr.', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Salzburg (Sterneckstr. 52/2.OG+)', 'Salzburg Sterneckstraße',187, 'Sterneckstr. 52/2.OG', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Sattledt (Hauptstr.41)', 'Sattledt Hauptstraße',156, 'Sattledt (Werkstatt)', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Sattledt (Hauptstraße 39a)', 'Sattledt Hauptstraße',156, 'Sattledt', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Schärding (Bahnhofstraße 57)', 'Schärding Bahnhofstraße 57',98, 'Schärding Molkerei', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Scheibbs (Neubruck 2, Schloss Neubruck)', 'Scheibbs Neubruck, Schloss Neubruck',192, 'Scheibbs Schloss Neu', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Schwaz (Münchnerstraße 48) KAOS', 'Schwaz Münchnerstraße',994, 'SCHW', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Schwechat (Himbergerstraße 66)', 'Rauchenwarth Himbergerstraße',88, 'Schwechat', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('St. Johann/P. (Liechtensteinklammstr. 11)', 'Sankt Johann im Pongau Lichtensteinklammstr.',265, 'St. Johann/P.', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('St. Johann/T. Bahnhofstraße 3 - KAOS', 'Innsbruck Bahnhofstraße',996, 'JOH01', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('St. Pölten (Heinrich Schneidmadl Straße 15)', 'St. Pölten Heinrich-Schneidmadl-Straße',148, 'St. Pölten BIZ', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('St. Pölten (Schießstattring 31-33)', 'St. Pölten Schießstattring',240, 'St. Pölten', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Tamsweg (Josef-Ehrenreich-Straße 1)', 'Tamsweg Josef-Ehrenreich-Straße 1',16, 'Tamsweg RK', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Tamsweg Q4 - Postplatz 4/3/22 & 23', 'Tamsweg Postplatz',325, 'Tamsweg Q4', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Tulln (Frauenhoferstraße 1-3)', 'Tulln an der Donau Frauenhoferstraße',89, 'Tulln', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Vöcklabruck (Gmundner Straße 47-49)', 'Vöcklabr Gmundner Straße 47-49',5, 'Vöcklabruck', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Waidhofen a.d.Thaya (Raiffeisenpromenade 2/9)', 'Waidhofen an der Thaya Raiffeisenpromenade 2/9',7, 'Waidhofen/Thaya', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wels (Bauernstraße 9, OG 03)', 'Wels Bauernstraße 9',410, 'Wels', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wels (Durisolstraße 7)', 'Wels Durisolstraße 7',400, 'GTZ', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wels (Lager-Puchnerstr. 54)', 'Wels Puchnerstraße',185, 'Lager Ö-Mitte', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wels (Maria-Theresia-Straße 53 WDZ 6)', 'Wels Maria-Theresia-Straße',179, 'WDZ 6', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wien (Geiselbergstraße 15-19)', 'Wien Geiselbergstraße 15-19',20, 'Geiselberg NEU', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wien (Gumpendorfer Gürtel 2B)', 'Wien Gumpendorfer Gürtel',6, 'Wien', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wien (Ignaz Köck Straße 17)', 'Wien Ignaz-Köck-Straße',13, 'FloDo', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wien (Oberlaaerstraße 276, Obj. 3)', 'Wien Oberlaaer Straße',178, 'Wien 23', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wien (Praterstraße 62)', 'Wien Praterstraße 62',36, 'Praterstraße JC', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wien Quellenstraße 2c, Gudrunstraße 11', 'Wien Quellenstraße 2 c',140, 'Wien Quellenstraße 2', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wiener Neustadt (Stadion-Straße 36)', 'Wiener Neustadt Stadionstraße 36',167, 'Wiener Neustadt', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wörgl (Giselastraße 3)', 'Wörgl Giselastraße 3',172, 'Wörgl(GIS)', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wörgl (Poststraße 6 a-c)', 'Wörgl Poststraße',153, 'Wörgl IQ', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wörgl (Rainerstraße 5-9)', 'null',null, 'Wörgl', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wörgl (Salzburgerstraße 53a) KAOS', 'Wörgl Salzburgerstraße',992, 'Wörgl', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wörgl - Angatherweg 5b - KAOS', 'Wörgl Angatherweg 2',993, 'WÖR01', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wörgl Josef Speckbacher-Straße 7+9', 'Wörgl Josef-Speckbacher-Straße',18, 'Wörgl Josef Speckbac', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wörgl Rainerstraße 4', 'Wörgl Rainerstraße 4',17, 'Wörgl', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wr. Neustadt (Badener Str.12/Josef Mohr-Gasse7)', 'Wiener Neustadt Badener Straße',300, 'Wr. Neustadt', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Wr. Neustadt (Stadionstraße 36)', 'Wiener Neustadt Stadionstraße 36',167, 'Wr. Neustadt Stadion', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Zell am See (Brucker Bundesstraße 92)', 'Zell am See Brucker Bundesstraße 92',51, 'Zell am See', current_user, 1, current_date);
insert into dienstort (name, lhr_kz, lhr_nr, short_name, created_by, status, created_on) values ('Zwettl (Landstr.52)', 'Zwettl-Niederösterreich Landstraße 52',174, 'Zwettl', current_user, 1, current_date);

