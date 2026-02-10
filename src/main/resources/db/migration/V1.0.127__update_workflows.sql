update public.s_workflows set workflow_group = (select id from s_workflow_groups where name = 'TN An und Abwesenheiten Transfer') where name = 'Transfer AN und Abwesenheiten for TN on LHR';
update public.s_workflow_items set workflow = (select id from s_workflows where name = 'Transfer AN und Abwesenheiten for TN on LHR') where name = 'Transfer An und Abwesenheiten to LHR';
update public.s_workflow_items set workflow = (select id from s_workflows where name = 'Transfer AN und Abwesenheiten for TN on LHR') where name = 'Lock time booking period';
update public.s_workflow_items set workflow = (select id from s_workflows where name = 'Transfer AN und Abwesenheiten for TN on LHR') where name = 'Inform LV and Jugend Admin';
