-- Update workflow item names
DO $$
    BEGIN
        -- Entry 1
        UPDATE s_workflow_items
        SET name = 'Mitarbeitendedaten prüfen'
        WHERE name = 'Mitarbeiterdaten prüfen';

        -- Entry 2
        UPDATE s_workflow_items
        SET name = 'Stammdaten erfassen (Teilnehmende)'
        WHERE name = 'Stammdaten erfassen for TN onboarding';

        -- Entry 3
        UPDATE s_workflow_items
        SET name = 'Vertragsdaten erfassen (Teilnehmende)'
        WHERE name = 'Vertragsdaten erfassen for TN onboarding';

        -- Entry 4
        UPDATE s_workflow_items
        SET name = 'Lohnverrechnung informieren (Teilnehmende)'
        WHERE name = 'Lohnverrechnung informieren for TN onboarding';

        -- Entry 5
        UPDATE s_workflow_items
        SET name = 'Mitarbeitendedaten prüfen (Teilnehmende)'
        WHERE name = 'Mitarbeiterdaten prüfen for TN onboarding';

        -- Entry 6
        UPDATE s_workflow_items
        SET name = 'Daten an LHR übergeben (Teilnehmende)'
        WHERE name = 'Daten an LHR übergeben for TN onboarding';

        -- Entry 7
        UPDATE s_workflow_items
        SET name = 'Stakeholder informieren (Teilnehmende)'
        WHERE name = 'Stakeholder informieren';
    END $$;