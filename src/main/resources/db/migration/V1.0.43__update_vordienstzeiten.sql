alter table vordienstzeiten drop column anrechenbar;
alter table vordienstzeiten_history drop column anrechenbar;

alter table vordienstzeiten add column anrechenbar boolean;
alter table vordienstzeiten_history add column anrechenbar boolean;