UPDATE s_workflows
SET name = 'Inform the stakeholder for TN onboarding'
WHERE name = 'Create new user in AD and DB for TN onboarding';

UPDATE s_workflow_items
SET name = 'Inform the stakeholder for TN onboarding'
WHERE name = 'Neuen MA anlegen for TN onboarding';

UPDATE s_workflow_items
SET successor = null
WHERE name = 'Inform the stakeholder for TN onboarding';

update w_workflow_items
set workflow_item = null
where workflow_item in (select id
                        FROM s_workflow_items
                        WHERE name in ('User Anlage beauftragen for TN onboarding',
                                       'AD & ibos User anlegen for TN onboarding',
                                       'IbosNG User anlege for TN onboarding'));

DELETE
FROM s_workflow_items
WHERE name in ('User Anlage beauftragen for TN onboarding',
               'AD & ibos User anlegen for TN onboarding',
               'IbosNG User anlege for TN onboarding');



