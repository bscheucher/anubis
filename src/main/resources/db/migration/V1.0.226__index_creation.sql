DO
$$
    DECLARE
        fk_record RECORD;
        create_index_sql TEXT;
    BEGIN
        -- Iterate over all foreign keys that do not have an index
        FOR fk_record IN
            WITH foreign_keys AS (
                SELECT
                    conname AS constraint_name,
                    conrelid::regclass::text AS table_name,
                    a.attname AS column_name
                FROM
                    pg_constraint c
                        JOIN
                    pg_attribute a ON a.attnum = ANY(c.conkey)
                        AND a.attrelid = c.conrelid
                WHERE
                    c.contype = 'f'
            ),
                 indexes AS (
                     SELECT
                         t.relname AS table_name,
                         a.attname AS column_name
                     FROM
                         pg_class t
                             JOIN
                         pg_index ix ON t.oid = ix.indrelid
                             JOIN
                         pg_attribute a ON a.attnum = ANY(ix.indkey)
                             AND a.attrelid = t.oid
                 ),
                 missing_indexes AS (
                     SELECT
                         fk.table_name,
                         fk.column_name
                     FROM
                         foreign_keys fk
                             LEFT JOIN
                         indexes idx ON fk.table_name = idx.table_name AND fk.column_name = idx.column_name
                     WHERE
                         idx.column_name IS NULL
                 )
            SELECT
                table_name,
                column_name
            FROM
                missing_indexes
            LOOP
                -- Construct the CREATE INDEX statement
                create_index_sql := 'CREATE INDEX idx_' || fk_record.table_name || '_' || fk_record.column_name ||
                                    ' ON ' || fk_record.table_name || ' (' || fk_record.column_name || ');';

                -- Execute the CREATE INDEX statement
                EXECUTE create_index_sql;

                -- Log the created index
                RAISE NOTICE 'Created index: %', create_index_sql;
            END LOOP;
    END
$$;