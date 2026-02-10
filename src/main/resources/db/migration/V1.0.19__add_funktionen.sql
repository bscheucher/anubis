--7531, 7532,
DO
$$
    DECLARE
        benutzer_ids integer[];
        fun_ids      integer[] := '{}'; -- Initialize as an empty array
        b_id         integer;
        f_id         integer;
    BEGIN
        -- Collect user IDs
        SELECT array_agg(ben.id)
        INTO benutzer_ids
        FROM benutzer ben
                 JOIN benutzer_2_funktion b2f ON ben.id = b2f.benutzer_id
                 JOIN funktionen fun ON b2f.funktion_id = fun.id
        WHERE fun.name = 'FN_MA_ERFASSEN';

        -- Delete existing function relations for these users
        IF benutzer_ids IS NOT NULL THEN
            DELETE
            FROM benutzer_2_funktion
            WHERE funktion_id IN (SELECT id FROM funktionen WHERE name = 'FN_MA_ERFASSEN')
              AND benutzer_id = ANY (benutzer_ids);
        END IF;

        -- Insert new functions and collect their IDs
        WITH inserted AS (
            INSERT INTO funktionen (name, description, created_by)
                VALUES ('FN_STAMMDATEN_ERFASSEN', 'Stammdaten erfassen', current_user),
                       ('FN_VERTRAGSDATEN_ERFASSEN', 'Vertragsdaten erfassen', current_user)
                RETURNING id)
        SELECT array_agg(id)
        INTO fun_ids
        FROM inserted;

        -- Create entries for all combinations of user IDs and function IDs
        IF benutzer_ids IS NOT NULL AND fun_ids IS NOT NULL THEN
            FOR i IN 1 .. array_length(benutzer_ids, 1)
                LOOP
                    FOR j IN 1 .. array_length(fun_ids, 1)
                        LOOP
                            INSERT INTO benutzer_2_funktion(benutzer_id, funktion_id)
                            VALUES (benutzer_ids[i], fun_ids[j]);
                        END LOOP;
                END LOOP;
        END IF;
    END
$$;
