UPDATE s_workflow_items
SET successor = (select id from s_workflow_items where name = 'Daten an Moxis übermitteln')
WHERE name = 'Dienstvertrag erstellen';

UPDATE s_workflow_items
SET predecessor = (select id from s_workflow_items where name = 'Dienstvertrag erstellen')
WHERE name = 'Daten an Moxis übermitteln';

update w_workflow_items
set workflow_item = null
where workflow_item = (select id
                       from s_workflow_items
                       WHERE name = 'Unterschriftenlauf starten');

DELETE
FROM s_workflow_items
WHERE name = 'Unterschriftenlauf starten';

UPDATE s_workflow_items
SET name = 'Unterschriftenlauf starten'
WHERE name = 'Daten an Moxis übermitteln';

