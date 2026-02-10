-- Step 1: Alter the column type from BIGINT to TEXT
ALTER TABLE ibosng.public.stammdaten
    ALTER COLUMN svnr TYPE TEXT
        USING ibosng.public.stammdaten.svnr::TEXT;

-- Step 1: Alter the column type from BIGINT to TEXT
ALTER TABLE ibosng.public.unterhaltsberechtigte
    ALTER COLUMN svn TYPE TEXT
        USING ibosng.public.unterhaltsberechtigte.svn::TEXT;