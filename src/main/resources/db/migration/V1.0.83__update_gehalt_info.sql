alter table gehalt_info drop column verwendungsgruppe;
alter table gehalt_info_history drop column verwendungsgruppe;

alter table gehalt_info drop column stufe;
alter table gehalt_info_history drop column stufe;

alter table gehalt_info add column verwendungsgruppe integer references verwendungsgruppe(id);
alter table gehalt_info_history add column verwendungsgruppe integer;

alter table gehalt_info add column stufe integer references kv_stufe(id);
alter table gehalt_info_history add column stufe integer;