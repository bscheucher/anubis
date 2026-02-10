insert into public.s_workflow_groups (name, status, created_on, created_by, changed_on, changed_by)
values ('Teilnehmer Onboarding', 1, current_date, current_user, null, null);


insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Collect Data for TN onboarding', (select id from s_workflow_groups where name = 'Teilnehmer Onboarding'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Inform the Lohnverrechnung for TN onboarding', (select id from s_workflow_groups where name = 'Teilnehmer Onboarding'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Send data to LHR for TN onboarding', (select id from s_workflow_groups where name = 'Teilnehmer Onboarding'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Create new user in AD and DB for TN onboarding', (select id from s_workflow_groups where name = 'Teilnehmer Onboarding'), 1, null, null,
        current_date, current_user, null, null);


insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Stammdaten erfassen for TN onboarding', (select id from s_workflows where name = 'Collect Data for TN onboarding'), 1, null, null, current_date,
        current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsdaten erfassen for TN onboarding', (select id from s_workflows where name = 'Collect Data for TN onboarding'), 1, null, null,
        current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Lohnverrechnung informieren for TN onboarding', (select id from s_workflows where name = 'Inform the Lohnverrechnung for TN onboarding'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Mitarbeiterdaten prüfen for TN onboarding', (select id from s_workflows where name = 'Inform the Lohnverrechnung for TN onboarding'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Daten an LHR übergeben for TN onboarding', (select id from s_workflows where name = 'Send data to LHR for TN onboarding'), 1, null,
        null, current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Neuen MA anlegen for TN onboarding', (select id from s_workflows where name = 'Create new user in AD and DB for TN onboarding'), 1, null,
        null, current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('User Anlage beauftragen for TN onboarding', (select id from s_workflows where name = 'Create new user in AD and DB for TN onboarding'), 1, null,
        null, current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('AD & ibos User anlegen for TN onboarding', (select id from s_workflows where name = 'Create new user in AD and DB for TN onboarding'), 1, null,
        null, current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('IbosNG User anlege for TN onboarding', (select id from s_workflows where name = 'Create new user in AD and DB for TN onboarding'), 1, null,
        null, current_date, current_user, null, null);




update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsdaten erfassen for TN onboarding')
where name = 'Stammdaten erfassen for TN onboarding';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Lohnverrechnung informieren for TN onboarding')
where name = 'Vertragsdaten erfassen for TN onboarding';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Mitarbeiterdaten prüfen for TN onboarding')
where name = 'Lohnverrechnung informieren for TN onboarding';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Daten an LHR übergeben for TN onboarding')
where name = 'Mitarbeiterdaten prüfen for TN onboarding';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Neuen MA anlegen for TN onboarding')
where name = 'Daten an LHR übergeben for TN onboarding';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'User Anlage beauftragen for TN onboarding')
where name = 'Neuen MA anlegen for TN onboarding';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'AD & ibos User anlegen for TN onboarding')
where name = 'User Anlage beauftragen for TN onboarding';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'IbosNG User anlege for TN onboarding')
where name = 'AD & ibos User anlegen for TN onboarding';


update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Stammdaten erfassen for TN onboarding')
where name = 'Vertragsdaten erfassen for TN onboarding';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsdaten erfassen for TN onboarding')
where name = 'Lohnverrechnung informieren for TN onboarding';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Lohnverrechnung informieren for TN onboarding')
where name = 'Mitarbeiterdaten prüfen for TN onboarding';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Mitarbeiterdaten prüfen for TN onboarding')
where name = 'Daten an LHR übergeben for TN onboarding';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Daten an LHR übergeben for TN onboarding')
where name = 'Neuen MA anlegen for TN onboarding';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Neuen MA anlegen for TN onboarding')
where name = 'User Anlage beauftragen for TN onboarding';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'User Anlage beauftragen for TN onboarding')
where name = 'AD & ibos User anlegen for TN onboarding';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'AD & ibos User anlegen for TN onboarding')
where name = 'IbosNG User anlege for TN onboarding';
