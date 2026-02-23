DO $$
    DECLARE
        constraint_name TEXT;
    BEGIN
        -- Loop through all foreign key constraints for the specified table
        FOR constraint_name IN
            SELECT conname
            FROM pg_constraint
            WHERE conrelid = 'public.ibis_firma_2_kostenstelle_history'::regclass
              AND contype = 'f'
            LOOP
                -- Dynamically drop each constraint
                EXECUTE format(
                        'ALTER TABLE public.ibis_firma_2_kostenstelle_history DROP CONSTRAINT %I',
                        constraint_name
                        );
                RAISE NOTICE 'Dropped constraint: %', constraint_name;
            END LOOP;
    END $$;

delete
from ibis_firma_2_kostenstelle
where kostenstelle in (select id
                       from kostenstelle
                       where nummer in (21, 31, 51, 61, 111, 112, 113, 115, 116, 117, 290, 300));

DELETE
from kostenstelle
where nummer in (21, 31, 51, 61, 111, 112, 113, 115, 116, 117, 290, 300, 96);

update kostenstelle
set bezeichnung = 'People'
where nummer = 10;
update kostenstelle
set bezeichnung = 'Wien-Migration'
where nummer = 29;
update kostenstelle
set bezeichnung = 'KAOS'
where nummer = 50;
update kostenstelle
set bezeichnung = 'Team Angebotslegung'
where nummer = 80;
update kostenstelle
set bezeichnung = 'Geschäftsführung'
where nummer = 90;
update kostenstelle
set bezeichnung = 'P&F'
where nummer = 93;
update kostenstelle
set bezeichnung = 'Aspire'
where nummer = 94;
update kostenstelle
set bezeichnung = 'Aspire education'
where nummer = 95;
update kostenstelle
set bezeichnung = 'Aspire Operations'
where nummer = 191;

update kostenstelle
set status = 2
where nummer = 14;
update kostenstelle
set status = 2
where nummer = 15;

INSERT INTO kostenstelle (nummer, bezeichnung, status, created_on, created_by)
VALUES (44, 'TalentLink', 1, CURRENT_TIMESTAMP, current_user);