insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Inform the Lohnverrechnung for new Mitarbeiter', (select id from s_workflow_groups where name = 'Create New Mitarbeiter'), 1,
        null, null, current_date, current_user, null, null);


insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Inform the Lohnverrechnung', (select id from s_workflows where name = 'Inform the Lohnverrechnung for new Mitarbeiter'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Lohnverrechnung control', (select id from s_workflows where name = 'Inform the Lohnverrechnung for new Mitarbeiter'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Creating DV for new Mitarbeiter', (select id from s_workflows where name = 'Inform the Lohnverrechnung for new Mitarbeiter'), 1, null,
        null, current_date, current_user, null, null);


update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Lohnverrechnung control')
where name = 'Inform the Lohnverrechnung';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Creating DV for new Mitarbeiter')
where name = 'Lohnverrechnung control';


update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Inform the Lohnverrechnung')
where name = 'Lohnverrechnung control';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Lohnverrechnung control')
where name = 'Creating DV for new Mitarbeiter';






insert into public.s_workflows (name, workflow_group, status, successor, predecessor, created_on, created_by,
                                changed_on, changed_by)
values ('Start the signing process of the contract for new Mitarbeiter', (select id from s_workflow_groups where name = 'Create New Mitarbeiter'), 1,
        null, null, current_date, current_user, null, null);


insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Start the signing process', (select id from s_workflows where name = 'Start the signing process of the contract for new Mitarbeiter'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('New Mitarbeiter had signed', (select id from s_workflows where name = 'Start the signing process of the contract for new Mitarbeiter'), 1, null, null,
        current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('The internal persons had signed', (select id from s_workflows where name = 'Start the signing process of the contract for new Mitarbeiter'), 1, null,
        null, current_date, current_user, null, null);
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Contract saved', (select id from s_workflows where name = 'Start the signing process of the contract for new Mitarbeiter'), 1, null,
        null, current_date, current_user, null, null);



update s_workflow_items
set successor = (select id from s_workflow_items where name = 'New Mitarbeiter had signed')
where name = 'Start the signing process';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'The internal persons had signed')
where name = 'New Mitarbeiter had signed';
update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Contract saved')
where name = 'The internal persons had signed';


update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Start the signing process')
where name = 'New Mitarbeiter had signed';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'New Mitarbeiter had signed')
where name = 'The internal persons had signed';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'The internal persons had signed')
where name = 'Contract saved';
