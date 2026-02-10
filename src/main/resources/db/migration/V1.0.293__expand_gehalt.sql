ALTER TABLE gehalt
    ADD COLUMN IF NOT EXISTS gueltig_ab date;

ALTER TABLE gehalt_history
    ADD COLUMN IF NOT EXISTS gueltig_ab date;

ALTER TABLE gehalt
    ADD COLUMN IF NOT EXISTS gueltig_bis date;

ALTER TABLE gehalt_history
    ADD COLUMN IF NOT EXISTS gueltig_bis date;

create or replace function gehalt_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehalt_history (gehalt_id, gehalt, kv_stufe, verwendungsgruppe, status, gueltig_ab, gueltig_bis,
                                    created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (OLD.id, OLD.gehalt, OLD.kv_stufe, OLD.verwendungsgruppe, OLD.status, OLD.gueltig_ab, OLD.gueltig_bis,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO gehalt_history (gehalt_id, gehalt, kv_stufe, verwendungsgruppe, status, gueltig_ab, gueltig_bis,
                                    created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.gehalt, NEW.kv_stufe, NEW.verwendungsgruppe, NEW.status, NEW.gueltig_ab, NEW.gueltig_bis,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO gehalt_history (gehalt_id, gehalt, kv_stufe, verwendungsgruppe, status, gueltig_ab, gueltig_bis,
                                    created_on, created_by, changed_by,
                                    action,
                                    action_timestamp)
        VALUES (NEW.id, NEW.gehalt, NEW.kv_stufe, NEW.verwendungsgruppe, NEW.status, NEW.gueltig_ab, NEW.gueltig_bis,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function gehalt_audit() owner to ibosng;

grant execute on function gehalt_audit() to ibosng;



-- Update existing entries
UPDATE gehalt
SET status = 2, gueltig_ab = '2024-08-01', gueltig_bis = '2025-04-30';

-- Insert new entries with corrected date format
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 1'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 1'),
           2376.80,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 1'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 2'),
           2534.73,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 1'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 3'),
           2744.96,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 1'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4'),
           2977.95,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 1'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4a'),
           3052.51,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 1'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 5'),
           3297.33,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 1'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 6'),
           3473.15,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 1'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 7'),
           4096.09,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 1'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 8'),
           4831.09,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 2'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 1'),
           2459.86,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 2'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 2'),
           2633.86,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 2'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 3'),
           2861.07,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 2'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4'),
           3127.24,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 2'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4a'),
           3204.94,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 2'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 5'),
           3503.30,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 2'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 6'),
           3716.71,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 2'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 7'),
           4377.33,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 2'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 8'),
           5161.01,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 3'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 1'),
           2542.94,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 3'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 2'),
           2733.35,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 3'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 3'),
           2977.15,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 3'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4'),
           3287.08,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 3'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4a'),
           3368.79,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 3'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 5'),
           3709.42,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 3'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 6'),
           3960.48,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 3'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 7'),
           4659.65,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 3'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 8'),
           5490.96,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 4'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 1'),
           2625.83,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 4'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 2'),
           2832.64,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 4'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 3'),
           3093.23,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 4'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4'),
           3455.61,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 4'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4a'),
           3537.35,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 4'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 5'),
           3915.56,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 4'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 6'),
           4204.21,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 4'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 7'),
           4942.44,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 4'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 8'),
           5820.89,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 5'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 1'),
           2708.89,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 5'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 2'),
           2931.97,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 5'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 3'),
           3213.48,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 5'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4'),
           3624.31,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 5'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4a'),
           3706.05,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 5'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 5'),
           4121.88,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 5'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 6'),
           4447.95,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 5'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 7'),
           5225.65,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 5'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 8'),
           6150.85,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 6'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 1'),
           2791.96,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 6'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 2'),
           3031.25,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 6'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 3'),
           3342.52,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 6'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4'),
           3792.84,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 6'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4a'),
           3874.57,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 6'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 5'),
           4328.00,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 6'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 6'),
           4692.89,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 6'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 7'),
           5508.47,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 6'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 8'),
           6480.78,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 7'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 1'),
           2874.85,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 7'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 2'),
           3130.53,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 7'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 3'),
           3473.57,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 7'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4'),
           3961.39,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 7'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4a'),
           4043.12,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 7'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 5'),
           4534.41,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 7'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 6'),
           4938.09,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 7'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 7'),
           5791.49,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 7'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 8'),
           6810.56,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 8'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 1'),
           2958.06,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 8'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 2'),
           3235.65,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 8'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 3'),
           3604.83,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 8'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4'),
           4129.92,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 8'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 4a'),
           4211.66,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 8'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 5'),
           4741.76,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 8'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 6'),
           5183.07,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 8'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 7'),
           6074.30,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
INSERT INTO gehalt (kv_stufe, verwendungsgruppe, gehalt, created_by, status, gueltig_ab, gueltig_bis)
VALUES (
           (SELECT id FROM kv_stufe WHERE name = 'Stufe 8'),
           (SELECT id FROM verwendungsgruppe WHERE name = 'VB 8'),
           7140.65,
           current_user,
           1,
           '2025-05-01',
           '2026-04-30'
       );
