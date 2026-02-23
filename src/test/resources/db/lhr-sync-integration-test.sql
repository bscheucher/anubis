-- create 2 NEW abwesenheiten and associated outbox entries (1st of which with associated fuehrungskraft)
---------------------------------------------------------

INSERT INTO ibis_firma (id, status, created_by, lhr_nr, lhr_kz)
    OVERRIDING SYSTEM VALUE
    VALUES ( 100, 1, 'somebody', 10, 'abc');

INSERT INTO personalnummer ( id, personalnummer, status, created_by, firma, created_on)
    OVERRIDING SYSTEM VALUE
    VALUES  ( 200, '123456789', 1, 'somebody', 100, current_timestamp);

INSERT INTO benutzer (id, first_name, last_name, email, upn, status, created_by, created_on, personalnummer)
    OVERRIDING SYSTEM VALUE
    VALUES ( 100, 'John', 'Employee', 'employee@example.com', 'employee@example.com', 1, 'somebody', current_timestamp, 200);

INSERT INTO benutzer (id, first_name, last_name, email, upn, status, created_by, created_on)
    OVERRIDING SYSTEM VALUE
    VALUES ( 101, 'Max', 'Manager', 'manager@example.com', 'manager@example.com', 1, 'somebody', current_timestamp);


INSERT INTO abwesenheit ( id, personalnummer, typ, kommentar, status, von, bis)
    OVERRIDING SYSTEM VALUE
    VALUES ( 300, 200, 'Urlaub', 'I miss my family', 0, current_date - interval '5 days', current_date + interval '5 days');


INSERT INTO abwesenheit_fuehrungskraft (abwesenheit_id, benutzer_id)
VALUES (300, 101);

INSERT INTO lhr_outbox (id, operation, status, data, created_at)
    OVERRIDING SYSTEM VALUE
    VALUES ( 1 ,'CREATE_ABWESENHEIT_REQUEST', 'NEW', '{"entityId": "300"}', current_timestamp);

----
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


-- SETUP relevant for LHR call failure test
-- create 1 NEW personalnummer, abwesenheit and associated outbox entry
---------------------------------------------------------

INSERT INTO personalnummer ( id, personalnummer, status, created_by, firma, created_on)
    OVERRIDING SYSTEM VALUE
VALUES  ( 202, '999999999', 1, 'somebody', 100, current_timestamp);

INSERT INTO abwesenheit ( id, personalnummer, typ, kommentar, status, von, bis)
    OVERRIDING SYSTEM VALUE
VALUES ( 304, 202, 'Urlaub', 'test comment for failure', 1, current_date - interval '1 day', current_date + interval '1 day');

INSERT INTO lhr_outbox (id, operation, status, data, created_at)
    OVERRIDING SYSTEM VALUE
VALUES ( 5 ,'CREATE_ABWESENHEIT_REQUEST', 'NEW', '{"entityId": "304"}', current_timestamp);