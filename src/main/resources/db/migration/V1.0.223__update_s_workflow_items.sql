update s_workflow_items
set name = 'Inform Lohnverrechnung for TN tritt aus'
where name = 'Inform Lohnverrechnung'
  and workflow = (select id from s_workflows where name = 'TN tritt sicherlich aus');