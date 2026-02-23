insert into public.s_workflow_groups (name, status, created_on, created_by, changed_on, changed_by)
values ('TN tritt aus', 1, current_date, current_user, null, null);


insert into public.s_workflows (name, status, created_on, created_by, changed_on, changed_by)
values ('TN tritt sicherlich aus', 1, current_date, current_user, null, null);




insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on, changed_by)
values('Inform Lohnverrechnung', (select id from s_workflows where name = 'TN tritt sicherlich aus'), 1, null, null,
       current_date, current_user, null, null);


insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on, changed_by)
values('Send Austritt to LHR', (select id from s_workflows where name = 'TN tritt sicherlich aus'), 1, null, null,
       current_date, current_user, null, null);


insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on, changed_by)
values('Change status to Abgemeldet', (select id from s_workflows where name = 'TN tritt sicherlich aus'), 1, null, null,
       current_date, current_user, null, null);


update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Send Austritt to LHR')
where name = 'Inform Lohnverrechnung';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Change status to Abgemeldet')
where name = 'Send Austritt to LHR';


update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Inform Lohnverrechnung')
where name = 'Send Austritt to LHR';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Send Austritt to LHR')
where name = 'Change status to Abgemeldet';



