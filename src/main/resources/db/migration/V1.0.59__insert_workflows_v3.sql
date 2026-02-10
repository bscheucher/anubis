update s_workflow_items set name = 'Stammdaten erfassen' where name = 'Stammdaten';
update s_workflow_items set name = 'Vertragsdaten erfassen' where name = 'Vertragsdaten';
update s_workflow_items set name = 'KV-Einstufung berechnen' where name = 'Calculate DV Einstufung';

update s_workflow_items set name = 'Lohnverrechnung informieren' where name = 'Inform the Lohnverrechnung';
update s_workflow_items set name = 'Mitarbeiterdaten prüfen' where name = 'Lohnverrechnung control';
update s_workflow_items set name = 'Dienstvertrag erstellen' where name = 'Creating DV for new Mitarbeiter';

update s_workflow_items set name = 'Unterschriftenlauf starten' where name = 'Start the signing process';
update s_workflow_items set name = 'Daten an Moxis übermitteln' where name = 'New Mitarbeiter had signed';
update s_workflow_items set name = 'Unterschriftenlauf dürchführen' where name = 'The internal persons had signed';
update s_workflow_items set name = 'Unterschriebene Dokumente speichern' where name = 'Contract saved';




insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Send data to LHR for the new MA', (select id from s_workflow_groups where name = 'Create New Mitarbeiter'), 1,
        null, null, current_date, current_user, null, null);


insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Daten an LHR übergeben', (select id from s_workflows where name = 'Send data to LHR for the new MA'), 1, null, null,
        current_date, current_user, null, null);





insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Create new user in AD and DB', (select id from s_workflow_groups where name = 'Create New Mitarbeiter'), 1,
        null, null, current_date, current_user, null, null);


insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Neuen MA anlegen', (select id from s_workflows where name = 'Create new user in AD and DB'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('User Anlage beauftragen', (select id from s_workflows where name = 'Create new user in AD and DB'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('AD & ibos User anlegen', (select id from s_workflows where name = 'Create new user in AD and DB'), 1, null,
        null, current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('IbosNG User anlegen', (select id from s_workflows where name = 'Create new user in AD and DB'), 1, null,
        null, current_date, current_user, null, null);


update s_workflow_items
set successor = (select id from s_workflow_items where name = 'User Anlage beauftragen')
where name = 'Neuen MA anlegen';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'AD & ibos User anlegen')
where name = 'User Anlage beauftragen';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'IbosNG User anlegen')
where name = 'AD & ibos User anlegen';


update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Neuen MA anlegen')
where name = 'User Anlage beauftragen';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'User Anlage beauftragen')
where name = 'AD & ibos User anlegen';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'AD & ibos User anlegen')
where name = 'IbosNG User anlegen';