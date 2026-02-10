DELETE FROM titel t1
    USING titel t2
WHERE t1.name = t2.name
  AND t1.id > t2.id;
