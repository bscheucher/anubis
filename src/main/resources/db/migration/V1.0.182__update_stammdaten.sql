ALTER TABLE stammdaten
DROP COLUMN IF EXISTS muttersprache;

ALTER TABLE stammdaten
    ADD COLUMN muttersprache INT references muttersprache(id);

ALTER TABLE stammdaten_history
DROP COLUMN IF EXISTS muttersprache;

ALTER TABLE stammdaten_history
    ADD COLUMN muttersprache INT;