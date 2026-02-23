ALTER TABLE vertragsdaten DROP CONSTRAINT vertragsdaten_dienstnehmergruppe_fkey;

ALTER TABLE vertragsdaten
    ADD CONSTRAINT vertragsdaten_dienstnehmergruppe_fkey
        FOREIGN KEY (dienstnehmergruppe)
            REFERENCES dienstnehmergruppe (id)
            ON DELETE CASCADE;

INSERT INTO abrechnungsgruppe (abbreviation, bezeichnung, created_on, created_by)
VALUES
    ('A_CON', 'GF Content Team', CURRENT_TIMESTAMP, current_user),
    ('A_SC', 'GF Service Center', CURRENT_TIMESTAMP, current_user),
    ('A_TL', 'GF Talent Link', CURRENT_TIMESTAMP, current_user),
    ('A_IT', 'IT', CURRENT_TIMESTAMP, current_user),
    ('KEINE', 'Keine Abrechnung', CURRENT_TIMESTAMP, current_user),
    ('A_P&Q', 'People & Quality', CURRENT_TIMESTAMP, current_user),
    ('A_P&F', 'Performance & Finance', CURRENT_TIMESTAMP, current_user),
    ('A_P&V', 'Produktentwicklung & Vertrieb', CURRENT_TIMESTAMP, current_user);

UPDATE vertragsdaten
SET abrechnungsgruppe = (select id from abrechnungsgruppe where abbreviation = 'KEINE')
WHERE abrechnungsgruppe IN (
    SELECT id
    FROM abrechnungsgruppe
    WHERE abbreviation IN ('T_NOE', 'T_OOE', 'T_T', 'T_WG', 'T_WO')
);

DELETE
FROM abrechnungsgruppe
WHERE abbreviation IN ('T_NOE', 'T_OOE', 'T_T', 'T_WG', 'T_WO');