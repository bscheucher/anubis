update s_workflows
set workflow_group = (select id from s_workflow_groups where name = 'TN tritt aus')
where name = 'TN tritt sicherlich aus';