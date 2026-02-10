ALTER TABLE arbeitszeiten ALTER COLUMN montag_von TYPE time USING montag_von::time;
ALTER TABLE arbeitszeiten ALTER COLUMN montag_bis TYPE time USING montag_bis::time;
ALTER TABLE arbeitszeiten ALTER COLUMN dienstag_von TYPE time USING dienstag_von::time;
ALTER TABLE arbeitszeiten ALTER COLUMN dienstag_bis TYPE time USING dienstag_bis::time;
ALTER TABLE arbeitszeiten ALTER COLUMN mittwoch_von TYPE time USING mittwoch_von::time;
ALTER TABLE arbeitszeiten ALTER COLUMN mittwoch_bis TYPE time USING mittwoch_bis::time;
ALTER TABLE arbeitszeiten ALTER COLUMN donnerstag_von TYPE time USING donnerstag_von::time;
ALTER TABLE arbeitszeiten ALTER COLUMN donnerstag_bis TYPE time USING donnerstag_bis::time;
ALTER TABLE arbeitszeiten ALTER COLUMN freitag_von TYPE time USING freitag_von::time;
ALTER TABLE arbeitszeiten ALTER COLUMN freitag_bis TYPE time USING freitag_bis::time;
ALTER TABLE arbeitszeiten ALTER COLUMN samstag_von TYPE time USING samstag_von::time;
ALTER TABLE arbeitszeiten ALTER COLUMN samstag_bis TYPE time USING samstag_bis::time;



ALTER TABLE arbeitszeiten_history ALTER COLUMN montag_von TYPE time USING montag_von::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN montag_bis TYPE time USING montag_bis::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN dienstag_von TYPE time USING dienstag_von::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN dienstag_bis TYPE time USING dienstag_bis::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN mittwoch_von TYPE time USING mittwoch_von::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN mittwoch_bis TYPE time USING mittwoch_bis::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN donnerstag_von TYPE time USING donnerstag_von::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN donnerstag_bis TYPE time USING donnerstag_bis::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN freitag_von TYPE time USING freitag_von::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN freitag_bis TYPE time USING freitag_bis::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN samstag_von TYPE time USING samstag_von::time;
ALTER TABLE arbeitszeiten_history ALTER COLUMN samstag_bis TYPE time USING samstag_bis::time;
