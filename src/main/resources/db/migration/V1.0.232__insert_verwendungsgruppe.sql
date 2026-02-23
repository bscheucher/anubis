insert into verwendungsgruppe (name, created_by, kollektivvertrag)
values ('Lehrlinge', current_user, (select id from kollektivvertrag where kollektivvertrag.name = 'AMS-Lehrteilnehmer'));