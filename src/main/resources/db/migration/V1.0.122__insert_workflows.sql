insert into public.s_workflow_groups (name, status, created_on, created_by, changed_on, changed_by)
values ('TN An und Abwesenheiten Transfer', 1, current_date, current_user, null, null);



insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Transfer AN und Abwesenheiten for TN on LHR', (select id from s_workflow_groups where name = 'Teilnehmer Onboarding'), 1, null, null,
        current_date, current_user, null, null);



insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on, changed_by)
values ('Transfer An und Abwesenheiten to LHR',
        (select id from s_workflows where name = 'Anwesenheits- und Abwesenheitsaufzeichnung'), 1, null, null,
        current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on, changed_by)
values ('Lock time booking period',
        (select id from s_workflows where name = 'Anwesenheits- und Abwesenheitsaufzeichnung'), 1, null, null,
        current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on, changed_by)
values ('Inform LV and Jugend Admin',
        (select id from s_workflows where name = 'Anwesenheits- und Abwesenheitsaufzeichnung'), 1, null, null,
        current_date, current_user, null, null);



update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Lock time booking period')
where name = 'Transfer An und Abwesenheiten to LHR';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Inform LV and Jugend Admin')
where name = 'Lock time booking period';



update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Transfer An und Abwesenheiten to LHR')
where name = 'Lock time booking period';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Lock time booking period')
where name = 'Inform LV and Jugend Admin';
