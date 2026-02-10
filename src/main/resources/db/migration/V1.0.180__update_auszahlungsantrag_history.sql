DO $$ 
DECLARE
constraint_rec RECORD;
BEGIN
    -- Loop through each foreign key constraint in zeitspeicher_history
FOR constraint_rec IN
SELECT constraint_name
FROM information_schema.table_constraints
WHERE table_name = 'zeitspeicher_history' AND constraint_type = 'FOREIGN KEY'
    LOOP
        EXECUTE format('ALTER TABLE zeitspeicher_history DROP CONSTRAINT %I', constraint_rec.constraint_name);
END LOOP;

    -- Loop through each foreign key constraint in auszahlungsantrag_history
FOR constraint_rec IN
SELECT constraint_name
FROM information_schema.table_constraints
WHERE table_name = 'auszahlungsantrag_history' AND constraint_type = 'FOREIGN KEY'
    LOOP
        EXECUTE format('ALTER TABLE auszahlungsantrag_history DROP CONSTRAINT %I', constraint_rec.constraint_name);
END LOOP;
END $$;