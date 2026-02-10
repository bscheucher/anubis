ALTER TABLE vertragsdaten
    RENAME COLUMN fuehrungskraft TO fuehrungskraft_ref;

ALTER TABLE vertragsdaten_history
    RENAME COLUMN fuehrungskraft TO fuehrungskraft_ref;

ALTER TABLE vertragsdaten
    RENAME COLUMN startcoach TO startcoach_ref;

ALTER TABLE vertragsdaten_history
    RENAME COLUMN startcoach TO startcoach_ref;

ALTER TABLE vertragsdaten
    ADD COLUMN fuehrungskraft INTEGER references benutzer (id);
ALTER TABLE vertragsdaten_history
    ADD COLUMN fuehrungskraft INTEGER;

ALTER TABLE vertragsdaten
    ADD COLUMN startcoach INTEGER references benutzer (id);
ALTER TABLE vertragsdaten_history
    ADD COLUMN startcoach INTEGER;
