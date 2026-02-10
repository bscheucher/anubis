insert into public.s_workflow_groups(name, status, created_on, created_by, changed_on, changed_by)
values ('MA Zeiterfassung zum Monatsende', 1, current_date, current_user,
        null, null);



insert into public.s_workflows(name, status, workflow_group, created_on, created_by, changed_on, changed_by)
values ('MA Zeiterfassung', 1, (select id from s_workflow_groups where name = 'MA Zeiterfassung zum Monatsende'), current_date, current_user, null, null);



insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Get Mehrstunden from LHR', (select id from s_workflows where name = 'MA Zeiterfassung'), 1,
        null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Mehrstunden und Ueberstunden ueberpruefen', (select id from s_workflows where name = 'MA Zeiterfassung'), 1,
        null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Auszahlungsantrag an LHR uebermitteln', (select id from s_workflows where name = 'MA Zeiterfassung'), 1,
        null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Auszahlungsantrag archivieren', (select id from s_workflows where name = 'MA Zeiterfassung'), 1, null, null,
        current_date, current_user, null, null);



update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Mehrstunden und Ueberstunden ueberpruefen')
where name = 'Get Mehrstunden from LHR';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Auszahlungsantrag an LHR uebermitteln')
where name = 'Mehrstunden und Ueberstunden ueberpruefen';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Auszahlungsantrag archivieren')
where name = 'Auszahlungsantrag an LHR uebermitteln';



update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Get Mehrstunden from LHR')
where name = 'Mehrstunden und Ueberstunden ueberpruefen';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Mehrstunden und Ueberstunden ueberpruefen')
where name = 'Auszahlungsantrag an LHR uebermitteln';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Auszahlungsantrag an LHR uebermitteln')
where name = 'Auszahlungsantrag archivieren';