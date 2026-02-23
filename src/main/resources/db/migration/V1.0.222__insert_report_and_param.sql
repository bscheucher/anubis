INSERT INTO report(report_name, source_path, main_report_file, created_on, created_by, changed_on, changed_by) VALUES ('Dienstvertrag', 'dienstvertrag', 'Dienstverstrag_final.jrxml', current_date, current_user, null, null);



INSERT INTO report_parameter(name, type, description, report_id, required, label) VALUES ('is_befristung', 'BOOLEAN', null, (select id from report where report_name = 'Dienstvertrag'), null, null);
INSERT INTO report_parameter(name, type, description, report_id, required, label) VALUES ('is_Drittstaat', 'BOOLEAN', null, (select id from report where report_name = 'Dienstvertrag'), null, null);
INSERT INTO report_parameter(name, type, description, report_id, required, label) VALUES ('is_Betriebskontakt', 'BOOLEAN', null, (select id from report where report_name = 'Dienstvertrag'), null, null);
INSERT INTO report_parameter(name, type, description, report_id, required, label) VALUES ('is_Zulagen', 'BOOLEAN', null, (select id from report where report_name = 'Dienstvertrag'), null, null);
INSERT INTO report_parameter(name, type, description, report_id, required, label) VALUES ('is_all_in', 'BOOLEAN', null, (select id from report where report_name = 'Dienstvertrag'), null, null);
INSERT INTO report_parameter(name, type, description, report_id, required, label) VALUES ('is_urlaub_vorab_vereinbart', 'BOOLEAN', null, (select id from report where report_name = 'Dienstvertrag'), null, null);
INSERT INTO report_parameter(name, type, description, report_id, required, label) VALUES ('is_konkurrenzklausel', 'BOOLEAN', null, (select id from report where report_name = 'Dienstvertrag'), null, null);
INSERT INTO report_parameter(name, type, description, report_id, required, label) VALUES ('is_erfinde', 'BOOLEAN', null, (select id from report where report_name = 'Dienstvertrag'), null, null);
INSERT INTO report_parameter(name, type, description, report_id, required, label) VALUES ('AZModel_id_variant', 'INTEGER', null, (select id from report where report_name = 'Dienstvertrag'), null, null);
