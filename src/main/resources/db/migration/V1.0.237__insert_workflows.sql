insert into public.s_workflow_groups(name, status, created_on, created_by, changed_on, changed_by)
values ('Vereinbarung zwischen MA und FK', 1, current_date, current_user,
        null, null);



insert into public.s_workflows(name, status, workflow_group, created_on, created_by, changed_on, changed_by)
values ('Neue Vereinbarung zwischen MA und FK', 1, (select id from s_workflow_groups where name = 'Vereinbarung zwischen MA und FK'), current_date, current_user, null, null);

insert into public.s_workflows(name, status, workflow_group, created_on, created_by, changed_on, changed_by)
values ('Unterschriftenlauf fuer Vereinbarung zwischen MA und FK', 1, (select id from s_workflow_groups where name = 'Vereinbarung zwischen MA und FK'), current_date, current_user, null, null);



insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Neue Vereinbarung erstellen zwischen MA und FK', (select id from s_workflows where name = 'Neue Vereinbarung zwischen MA und FK'), 1,
        null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vereinbarung vervollstaendigen zwischen MA und FK', (select id from s_workflows where name = 'Neue Vereinbarung zwischen MA und FK'), 1,
        null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Dokument erstellen fuer Vereinbarung zwischen MA und FK', (select id from s_workflows where name = 'Neue Vereinbarung zwischen MA und FK'), 1,
        null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Unterschriftenlauf starten fuer Vereinbarung zwischen MA und FK', (select id from s_workflows where name = 'Unterschriftenlauf fuer Vereinbarung zwischen MA und FK'), 1, null, null,
        current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Unterschriftenlauf durchfuehren fuer Vereinbarung zwischen MA und FK', (select id from s_workflows where name = 'Unterschriftenlauf fuer Vereinbarung zwischen MA und FK'), 1, null, null,
        current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Unterschriebenes Dokument speichern fuer Vereinbarung zwischen MA und FK', (select id from s_workflows where name = 'Unterschriftenlauf fuer Vereinbarung zwischen MA und FK'), 1, null, null,
        current_date, current_user, null, null);


update s_workflows
set successor = (select id from s_workflows where name = 'Unterschriftenlauf fuer Vereinbarung zwischen MA und FK')
where name = 'Neue Vereinbarung zwischen MA und FK';


update s_workflows
set predecessor = (select id from s_workflows where name = 'Neue Vereinbarung zwischen MA und FK')
where name = 'Unterschriftenlauf fuer Vereinbarung zwischen MA und FK';





update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vereinbarung vervollstaendigen zwischen MA und FK')
where name = 'Neue Vereinbarung erstellen zwischen MA und FK';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Dokument erstellen fuer Vereinbarung zwischen MA und FK')
where name = 'Vereinbarung vervollstaendigen zwischen MA und FK';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Unterschriftenlauf starten fuer Vereinbarung zwischen MA und FK')
where name = 'Dokument erstellen fuer Vereinbarung zwischen MA und FK';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Unterschriftenlauf durchfuehren fuer Vereinbarung zwischen MA und FK')
where name = 'Unterschriftenlauf starten fuer Vereinbarung zwischen MA und FK';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Unterschriebenes Dokument speichern fuer Vereinbarung zwischen MA und FK')
where name = 'Unterschriftenlauf durchfuehren fuer Vereinbarung zwischen MA und FK';



update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Neue Vereinbarung erstellen zwischen MA und FK')
where name = 'Vereinbarung vervollstaendigen zwischen MA und FK';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vereinbarung vervollstaendigen zwischen MA und FK')
where name = 'Dokument erstellen fuer Vereinbarung zwischen MA und FK';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Dokument erstellen fuer Vereinbarung zwischen MA und FK')
where name = 'Unterschriftenlauf starten fuer Vereinbarung zwischen MA und FK';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Unterschriftenlauf starten fuer Vereinbarung zwischen MA und FK')
where name = 'Unterschriftenlauf durchfuehren fuer Vereinbarung zwischen MA und FK';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Unterschriftenlauf durchfuehren fuer Vereinbarung zwischen MA und FK')
where name = 'Unterschriebenes Dokument speichern fuer Vereinbarung zwischen MA und FK';