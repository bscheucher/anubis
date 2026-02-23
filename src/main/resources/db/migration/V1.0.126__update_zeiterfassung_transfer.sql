ALTER TABLE zeiterfassung_transfer
    ALTER COLUMN datum_sent
        SET DATA TYPE TIMESTAMP;

ALTER TABLE zeiterfassung_transfer_history
    ALTER COLUMN datum_sent
        SET DATA TYPE TIMESTAMP;