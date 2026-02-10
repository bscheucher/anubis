ALTER TABLE zeitausgleich ALTER COLUMN id DROP IDENTITY IF EXISTS;
ALTER TABLE zeitausgleich ALTER COLUMN id SET DEFAULT nextval('abwesenheit_id_seq');
UPDATE zeitausgleich SET id = nextval('abwesenheit_id_seq');

