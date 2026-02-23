-- Step 1: Alter the column type from BIGINT to TEXT
ALTER TABLE ibosng.public.teilnehmer
    ALTER COLUMN sv_nummer TYPE TEXT
        USING ibosng.public.teilnehmer.sv_nummer::TEXT;

-- Step 1: Alter the column type from BIGINT to TEXT
ALTER TABLE ibosng.public.abmeldung
    ALTER COLUMN sv_nummer TYPE TEXT
        USING ibosng.public.abmeldung.sv_nummer::TEXT;