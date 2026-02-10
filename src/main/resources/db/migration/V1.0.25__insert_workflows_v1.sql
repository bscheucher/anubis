insert into public.s_workflow_groups (name, status, created_on, created_by, changed_on, changed_by)
values ('Import Teilnehmer Files', 1, current_date, current_user, null, null);
insert into public.s_workflow_groups (name, status, created_on, created_by, changed_on, changed_by)
values ('Create New Mitarbeiter', 1, current_date, current_user, null, null);



insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Import Teilnehmer', (select id from s_workflow_groups where name = 'Import Teilnehmer Files'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Collect Data for New Mitarbeiter', (select id from s_workflow_groups where name = 'Create New Mitarbeiter'), 1,
        null, null, current_date, current_user, null, null);


insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Importing Teilnehmer', (select id from s_workflows where name = 'Import Teilnehmer'), 1, null, null, current_date,
        current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Validating Imported Teilnehmer', (select id from s_workflows where name = 'Import Teilnehmer'), 1, null, null,
        current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Stammdaten', (select id from s_workflows where name = 'Collect Data for New Mitarbeiter'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsdaten', (select id from s_workflows where name = 'Collect Data for New Mitarbeiter'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Calculate DV Einstufung', (select id from s_workflows where name = 'Collect Data for New Mitarbeiter'), 1, null,
        null, current_date, current_user, null, null);

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Validating Imported Teilnehmer')
where name = 'Importing Teilnehmer';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsdaten')
where name = 'Stammdaten';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Calculate DV Einstufung')
where name = 'Vertragsdaten';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Importing Teilnehmer')
where name = 'Validating Imported Teilnehmer';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Stammdaten')
where name = 'Vertragsdaten';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsdaten')
where name = 'Calculate DV Einstufung';