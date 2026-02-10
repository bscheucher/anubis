-- create 2 NEW abwesenheiten and associated outbox entries
---------------------------------------------------------

INSERT INTO ibis_firma (id, status, created_by, lhr_nr, lhr_kz)
    OVERRIDING SYSTEM VALUE
    VALUES ( 100, 1, 'somebody', 10, 'abc');

INSERT INTO personalnummer ( id, personalnummer, status, created_by, firma, created_on)
    OVERRIDING SYSTEM VALUE
    VALUES  ( 200, '123456789', 1, 'somebody', 100, current_timestamp);

INSERT INTO abwesenheit ( id, personalnummer, typ, kommentar, status, von, bis)
    OVERRIDING SYSTEM VALUE
    VALUES ( 300, 200, 'Urlaub', 'I miss my family', 0, current_date - interval '5 days', current_date + interval '5 days');

INSERT INTO lhr_outbox (id, operation, status, data, created_at)
    OVERRIDING SYSTEM VALUE
    VALUES ( 1 ,'CREATE_ABWESENHEIT_REQUEST', 'NEW', '{"entityId": "300"}', current_timestamp);


INSERT INTO ibis_firma (id, status, created_by, lhr_nr, lhr_kz)
    OVERRIDING SYSTEM VALUE
VALUES ( 101, 1, 'somebody', 10, 'abc');

INSERT INTO personalnummer ( id, personalnummer, status, created_by, firma, created_on)
    OVERRIDING SYSTEM VALUE
VALUES  ( 201, '223456789', 1, 'somebody', 100, current_timestamp);

INSERT INTO abwesenheit ( id, personalnummer, typ, kommentar, status, von, bis)
    OVERRIDING SYSTEM VALUE
VALUES ( 301, 201, 'Urlaub', 'I miss my family more', 0, current_date - interval '5 days', current_date + interval '5 days');

INSERT INTO lhr_outbox (id, operation, status, data, created_at)
    OVERRIDING SYSTEM VALUE
VALUES ( 3 ,'CREATE_ABWESENHEIT_REQUEST', 'NEW', '{"entityId": "301"}', current_timestamp);


-- create 2 outbox entries WITHOUT the abwesenheiten entries that are referenced via the data field
---------------------------------------------------------

INSERT INTO lhr_outbox (id, operation, status, data, created_at)
    OVERRIDING SYSTEM VALUE
VALUES ( 2 ,'CREATE_ABWESENHEIT_REQUEST', 'NEW', '{"entityId": "302"}', current_timestamp);

INSERT INTO lhr_outbox (id, operation, status, data, created_at)
    OVERRIDING SYSTEM VALUE
VALUES ( 4 ,'CREATE_ABWESENHEIT_REQUEST', 'NEW', '{"entityId": "303"}', current_timestamp);