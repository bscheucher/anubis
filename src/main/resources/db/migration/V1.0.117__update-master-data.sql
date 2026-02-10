TRUNCATE TABLE arbeitsgenehmigung;

insert into arbeitsgenehmigung (name, created_by)
values ('Beschäftigungsbewilligung', current_user),
       ('Entsendebewilligung', current_user),
       ('Anzeigebestätigung', current_user),
       ('Rot-Weiß-Rot – Karte', current_user),
       ('Blaue Karte EU', current_user),
       ('Aufenthaltsbewilligung als unternehmensintern transferierter Arbeitnehmer („ICT“),', current_user),
       ('Aufenthaltsbewilligung als mobiler unternehmensintern transferierter Arbeitnehmer („mobile ICT“)', current_user),
       ('Aufenthaltsbewilligung „Familiengemeinschaft“ mit Zugang zum Arbeitsmarkt (§20f Abs.4)', current_user),
       ('Niederlassungsbewilligung – Künstler', current_user),
       ('Rot-Weiß-Rot – Karte plus', current_user),
       ('Aufenthaltsberechtigung plus', current_user),
       ('Befreiungsschein (§4c)', current_user),
       ('Aufenthaltstitel „Familienangehöriger', current_user),
       ('Daueraufenthalt-EU', current_user);

insert into titel (name, position, created_by)
values ('BA', 0, current_user),
       ('Bakk.', 0, current_user),
       ('BEd', 0, current_user);

insert into kategorie (name, created_by)
values ('Mitarbeiter', current_user),
       ('Lehrling', current_user);

insert into dienstnehmergruppe (abbreviation, bezeichnung, created_on, created_by)
values
    ('T_WIE', 'Wien', CURRENT_TIMESTAMP, current_user),
    ('T_NOE', 'Niederösterreich', CURRENT_TIMESTAMP, current_user),
    ('T_OOE', 'Oberösterreich', CURRENT_TIMESTAMP, current_user),
    ('T_TIR', 'Tirol', CURRENT_TIMESTAMP, current_user);

insert into abrechnungsgruppe (abbreviation, bezeichnung, created_on, created_by)
values
    ('T_NOE', 'TN NÖ', CURRENT_TIMESTAMP, current_user),
    ('T_OOE', 'TN NÖ', CURRENT_TIMESTAMP, current_user),
    ('T_T', 'Tn Tirol', CURRENT_TIMESTAMP, current_user),
    ('T_WG', 'TN Wien Geiselberg', CURRENT_TIMESTAMP, current_user),
    ('T_WO', 'TN Wien Oberlaa', CURRENT_TIMESTAMP, current_user);

insert into taetigkeit (name, created_by)
values ('AMS-Lehrteilnehmer', current_user);