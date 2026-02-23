-- Step 1: Create a new sequence with an arbitrary start value
CREATE SEQUENCE base_plz_id_seq;

-- Step 2: Synchronize the new sequence with the current value of plz_id_seq
SELECT setval('base_plz_id_seq', (SELECT last_value FROM plz_id_seq), true);

-- Step 3: Assign the new sequence to base_plz.id
ALTER TABLE base_plz ALTER COLUMN id SET DEFAULT nextval('base_plz_id_seq');

-- Step 4: Drop the identity property from plz.id
ALTER TABLE plz ALTER COLUMN id DROP IDENTITY;

-- Step 5: Ensure all IDs from plz exist in base_plz
INSERT INTO base_plz(id)
SELECT DISTINCT id FROM plz
ON CONFLICT DO NOTHING;

-- Step 6: Drop the existing primary key constraint from plz (if necessary)
DO $$
    DECLARE
        constraint_name text;
    BEGIN
        -- Find the primary key constraint name
        SELECT conname
        INTO constraint_name
        FROM pg_constraint
        WHERE conrelid = 'plz'::regclass AND contype = 'p';

        -- If a primary key exists, drop it
        IF constraint_name IS NOT NULL THEN
            EXECUTE format('ALTER TABLE plz DROP CONSTRAINT %I', constraint_name);
        END IF;
    END
$$;

-- Step 7: Add a foreign key constraint to plz.id
ALTER TABLE plz
    ADD CONSTRAINT plz_id_fkey FOREIGN KEY (id) REFERENCES base_plz (id);

-- Step 8: Add the primary key constraint to plz
ALTER TABLE plz ADD PRIMARY KEY (id);