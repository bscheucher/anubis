ALTER TABLE arbeitszeiten_info
    drop COLUMN beschaeftigungsausmass;
ALTER TABLE arbeitszeiten_info_history
    drop COLUMN beschaeftigungsausmass;

ALTER TABLE arbeitszeiten_info
    add COLUMN beschaeftigungsausmass INTEGER references beschaeftigungsausmass(id);
ALTER TABLE arbeitszeiten_info_history
    add COLUMN beschaeftigungsausmass INTEGER;

ALTER TABLE arbeitszeiten_info
    drop COLUMN beschaeftigungsstatus;
ALTER TABLE arbeitszeiten_info_history
    drop COLUMN beschaeftigungsstatus;

ALTER TABLE arbeitszeiten_info
    add COLUMN beschaeftigungsstatus INTEGER references beschaeftigungsstatus(id);
ALTER TABLE arbeitszeiten_info_history
    add COLUMN beschaeftigungsstatus INTEGER;