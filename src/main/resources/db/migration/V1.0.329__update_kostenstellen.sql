INSERT INTO kostenstelle (nummer, bezeichnung, status, created_on, created_by)
VALUES (300, 'GWL - Gemeinsam Wohnen & Leben', 1, CURRENT_TIMESTAMP, current_user);

update kostenstelle set bezeichnung = 'KAOS Bildungsservice' where nummer = 50;
update kostenstelle set bezeichnung = 'PSC (Produkt Service Center)' where nummer = 80;
update kostenstelle set bezeichnung = 'GF' where nummer = 90;
update kostenstelle set bezeichnung = 'Performance&Finance' where nummer = 93;
update kostenstelle set bezeichnung = 'Aspire Beteiligungs Gmb' where nummer = 94;
update kostenstelle set bezeichnung = 'IDC' where nummer = 191;

update kostenstelle set status = 2 where nummer = 11;
update kostenstelle set status = 2 where nummer = 12;
update kostenstelle set status = 2 where nummer = 13;
update kostenstelle set status = 2 where nummer = 14;
update kostenstelle set status = 2 where nummer = 15;
update kostenstelle set status = 2 where nummer = 16;
update kostenstelle set status = 2 where nummer = 17;
update kostenstelle set status = 2 where nummer = 18;
update kostenstelle set status = 2 where nummer = 19;
update kostenstelle set status = 2 where nummer = 20;
update kostenstelle set status = 2 where nummer = 25;
update kostenstelle set status = 2 where nummer = 28;
update kostenstelle set status = 2 where nummer = 29;
update kostenstelle set status = 2 where nummer = 95;
