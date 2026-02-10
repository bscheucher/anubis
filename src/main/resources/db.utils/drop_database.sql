drop table flyway_schema_history;

drop table benutzer_history;

drop table teilnehmer_history;

drop table telefon_history;

drop table land_history;

drop table betreuer_history;

drop table widgets_in_dashboards_history;

drop table dashboards_history;

drop table validations;

drop table validations_history;

drop table adresse_history;

drop table data_status_history;

drop table teilnehmer_staging;

drop table data_status;

drop table rgs_history;

drop table plz;

drop table plz_history;

drop table vorwahl;

drop table vorwahl_history;

drop table teilnehmer_telefon;

drop table teilnehmer_telefon_history;

drop table teilnehmer_staging_history;

drop table widgets_in_dashboards;

drop table dashboards;

drop table widget;

drop table widget_history;

drop table seminar_history;

drop table teilnehmer_2_seminar;

drop table teilnehmer;

drop table betreuer;

drop table rgs;

drop table seminar;

drop table teilnehmer_2_seminar_history;

drop table rollen_gruppen_history;

drop table rollen_history;

drop table funktionen_history;

drop table benutzer_2_rolle;

drop table benutzer_2_rolle_history;

drop table benutzer_2_rollengruppe;

drop table benutzer_2_rollengruppe_history;

drop table benutzer_2_funktion;

drop table benutzer;

drop table benutzer_2_funktion_history;

drop table rollengruppe_2_rollen;

drop table rollen_gruppen;

drop table rollengruppe_2_rollen_history;

drop table rollen_2_funktionen;

drop table rollen;

drop table funktionen;

drop table rollen_2_funktionen_history;

drop table beruf_history;

drop table abteilung_history;

drop table religion_history;

drop table gehaltsinfo_history;

drop table auftraggeber_bank_history;

drop table bankverbindung_history;

drop table dienstnehmer_history;

drop table erreichbarkeit;

drop table dienstnehmer;

drop table telefon;

drop table adresse;

drop table beruf;

drop table abteilung;

drop table religion;

drop table gehaltsinfo;

drop table bankverbindung;

drop table land;

drop table auftraggeber_bank;

drop table erreichbarkeit_history;

drop table ibis_firma;

drop table ibis_firma_history;

drop table s_workflow_groups_history;

drop table s_workflows_history;

drop table s_workflow_items_history;

drop table w_workflow_groups_history;

drop table w_workflows_history;

drop table w_workflow_items;

drop table s_workflow_items;

drop table w_workflows;

drop table s_workflows;

drop table w_workflow_groups;

drop table s_workflow_groups;

drop table w_workflow_items_history;

drop table bundesland;

drop table bundesland_history;



drop function betreuer_audit();

drop function land_audit();

drop function telefon_audit();

drop function benutzer_audit();

drop function dashboards_audit();

drop function widgets_in_dashboards_audit();

drop function teilnehmer_audit();

drop function validations_audit();

drop function adresse_audit();

drop function data_status_audit();

drop function rgs_audit();

drop function plz_audit();

drop function vorwahl_audit();

drop function teilnehmer_telefon_audit();

drop function teilnehmer_staging_audit();

drop function widget_audit();

drop function seminar_audit();

drop function teilnehmer_2_seminar_audit();

drop function rollen_gruppen_audit();

drop function rollen_audit();

drop function funktionen_audit();

drop function benutzer_2_rolle_audit();

drop function benutzer_2_rollengruppe_audit();

drop function benutzer_2_funktion_audit();

drop function rollengruppe_2_rollen_audit();

drop function rollen_2_funktionen_audit();

drop function abteilung_audit();

drop function bankverbindung_audit();

drop function auftraggeber_bank_audit();

drop function beruf_audit();

drop function dienstnehmer_audit();

drop function erreichbarkeit_audit();

drop function gehaltsinfo_audit();

drop function religion_audit();

drop function ibis_firma_audit();

drop function s_workflow_groups_audit();

drop function s_workflows_audit();

drop function s_workflow_items_audit();

drop function w_workflow_groups_audit();

drop function w_workflows_audit();

drop function w_workflow_items_audit();

drop function bundesland_audit();