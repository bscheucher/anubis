ALTER TABLE teilnehmer_staging
ALTER COLUMN teilnahme_von TYPE text;

ALTER TABLE teilnehmer_staging_history
ALTER COLUMN teilnahme_von TYPE text;

ALTER TABLE teilnehmer_staging
ALTER COLUMN teilnahme_bis TYPE text;

ALTER TABLE teilnehmer_staging_history
ALTER COLUMN teilnahme_bis TYPE text;