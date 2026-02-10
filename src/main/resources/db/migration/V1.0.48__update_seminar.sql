alter table seminar drop column project;
alter table seminar add column project integer references projekt(id);