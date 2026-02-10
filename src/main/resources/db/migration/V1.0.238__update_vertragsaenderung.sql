alter table vertragsaenderung drop column status;
alter table vertragsaenderung_history drop column status;

alter table vertragsaenderung add column status text;
alter table vertragsaenderung_history add column status text;