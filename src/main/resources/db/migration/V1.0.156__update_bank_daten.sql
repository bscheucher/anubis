UPDATE bank_daten SET bar_bei_austritt = false WHERE bar_bei_austritt IS NULL;
ALTER TABLE bank_daten ALTER COLUMN bar_bei_austritt SET NOT NULL;
ALTER TABLE bank_daten ALTER COLUMN bar_bei_austritt SET DEFAULT false;