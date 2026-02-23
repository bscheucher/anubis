-- Step 1: Alter the column type from BIGINT to TEXT
ALTER TABLE ibosng.public.teilnehmer_history
    ALTER COLUMN sv_nummer TYPE TEXT
        USING ibosng.public.teilnehmer_history.sv_nummer::TEXT;

-- Step 1: Alter the column type from BIGINT to TEXT
ALTER TABLE ibosng.public.abmeldung_history
    ALTER COLUMN sv_nummer TYPE TEXT
        USING ibosng.public.abmeldung_history.sv_nummer::TEXT;

-- Step 1: Alter the column type from BIGINT to TEXT
ALTER TABLE ibosng.public.stammdaten_history
    ALTER COLUMN svnr TYPE TEXT
        USING ibosng.public.stammdaten_history.svnr::TEXT;

-- Step 1: Alter the column type from BIGINT to TEXT
ALTER TABLE ibosng.public.unterhaltsberechtigte_history
    ALTER COLUMN svn TYPE TEXT
        USING ibosng.public.unterhaltsberechtigte_history.svn::TEXT;