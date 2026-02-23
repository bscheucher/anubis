DELETE FROM gehalt_info WHERE id NOT IN (SELECT DISTINCT ON (vertragsdaten_id) id
FROM gehalt_info
ORDER BY vertragsdaten_id,
    CASE status
        WHEN 1 THEN 1
        WHEN 4 THEN 2
        WHEN 0 THEN 3
        WHEN 3 THEN 4
        WHEN 2 THEN 5
        ELSE 6
    END);

ALTER TABLE gehalt_info ADD CONSTRAINT vertragsdaten_id_unique UNIQUE(vertragsdaten_id);
