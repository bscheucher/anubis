DO $$
    DECLARE
        constraint_name TEXT;
    BEGIN
        -- Loop through all foreign key constraints for the specified table
        FOR constraint_name IN
            SELECT conname
            FROM pg_constraint
            WHERE conrelid = 'public.vertragsdaten_history'::regclass
              AND contype = 'f'
            LOOP
                -- Dynamically drop each constraint
                EXECUTE format(
                        'ALTER TABLE public.vertragsdaten_history DROP CONSTRAINT %I',
                        constraint_name
                        );
                RAISE NOTICE 'Dropped constraint: %', constraint_name;
            END LOOP;
    END $$;