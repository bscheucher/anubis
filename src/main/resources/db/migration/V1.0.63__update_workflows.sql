update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Lohnverrechnung informieren')
where name = 'KV-Einstufung berechnen';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'KV-Einstufung berechnen')
where name = 'Lohnverrechnung informieren';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Unterschriftenlauf starten')
where name = 'Dienstvertrag erstellen';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Dienstvertrag erstellen')
where name = 'Unterschriftenlauf starten';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Daten an LHR übergeben')
where name = 'Unterschriebene Dokumente speichern';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Unterschriebene Dokumente speichern')
where name = 'Daten an LHR übergeben';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Neuen MA anlegen')
where name = 'Daten an LHR übergeben';
update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Daten an LHR übergeben')
where name = 'Neuen MA anlegen';