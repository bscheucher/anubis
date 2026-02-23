alter table gehalt_info drop column jobticket;
alter table gehalt_info add column jobticket integer references jobticket(id);
alter table gehalt_info_history drop column jobticket;
alter table gehalt_info_history add column jobticket integer;