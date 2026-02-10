create table if not exists gehalt
(
    id             integer generated always as identity primary key,
    gehalt NUMERIC,
    kv_stufe       integer references kv_stufe(id),
    verwendungsgruppe integer references verwendungsgruppe (id),
    status         smallint                            not null,
    created_on     timestamp default CURRENT_TIMESTAMP not null,
    created_by     text                                not null,
    changed_on     timestamp,
    changed_by     text
);

create table if not exists gehalt_history
(
    id               integer generated always as identity primary key,
    gehalt_id       integer                             not null,
    gehalt NUMERIC,
    kv_stufe       integer references kv_stufe(id),
    verwendungsgruppe integer references verwendungsgruppe (id),
    status           smallint                            not null,
    created_on       timestamp default CURRENT_TIMESTAMP not null,
    created_by       text                                not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp                           not null
);

create or replace function gehalt_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO gehalt_history (gehalt_id, gehalt, kv_stufe, verwendungsgruppe, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (OLD.id, OLD.gehalt, OLD.kv_stufe, OLD.verwendungsgruppe, OLD.status,
                OLD.created_on, OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO gehalt_history (gehalt_id, gehalt, kv_stufe, verwendungsgruppe, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.gehalt, NEW.kv_stufe, NEW.verwendungsgruppe, NEW.status,
                NEW.created_on, NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO gehalt_history (gehalt_id, gehalt, kv_stufe, verwendungsgruppe, status,
                                     created_on, created_by, changed_by,
                                     action,
                                     action_timestamp)
        VALUES (NEW.id, NEW.gehalt, NEW.kv_stufe, NEW.verwendungsgruppe, NEW.status,
                NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function gehalt_audit() owner to ibosng;

create trigger gehalt_insert
    after insert
    on gehalt
    for each row
execute procedure gehalt_audit();

create trigger gehalt_update
    after update
    on gehalt
    for each row
execute procedure gehalt_audit();

create trigger gehalt_delete
    after delete
    on gehalt
    for each row
execute procedure gehalt_audit();



insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (60.6665789, (select id from kv_stufe where name = 'Stufe 1') , (select id from verwendungsgruppe where name = 'VB 1'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (64.6978947, (select id from kv_stufe where name = 'Stufe 1') , (select id from verwendungsgruppe where name = 'VB 2'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (70.0636842, (select id from kv_stufe where name = 'Stufe 1') , (select id from verwendungsgruppe where name = 'VB 3'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (76.0107895, (select id from kv_stufe where name = 'Stufe 1') , (select id from verwendungsgruppe where name = 'VB 4'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (77.9139474, (select id from kv_stufe where name = 'Stufe 1') , (select id from verwendungsgruppe where name = 'VB 4a'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (84.1628947, (select id from kv_stufe where name = 'Stufe 1') , (select id from verwendungsgruppe where name = 'VB 5'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (88.6505263, (select id from kv_stufe where name = 'Stufe 1') , (select id from verwendungsgruppe where name = 'VB 6'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (104.5507895, (select id from kv_stufe where name = 'Stufe 1') , (select id from verwendungsgruppe where name = 'VB 7'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (123.3113158, (select id from kv_stufe where name = 'Stufe 1') , (select id from verwendungsgruppe where name = 'VB 8'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (62.7868421, (select id from kv_stufe where name = 'Stufe 2') , (select id from verwendungsgruppe where name = 'VB 1'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (67.2281579, (select id from kv_stufe where name = 'Stufe 2') , (select id from verwendungsgruppe where name = 'VB 2'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (73.0273684, (select id from kv_stufe where name = 'Stufe 2') , (select id from verwendungsgruppe where name = 'VB 3'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (79.8213158, (select id from kv_stufe where name = 'Stufe 2') , (select id from verwendungsgruppe where name = 'VB 4'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (81.8044737, (select id from kv_stufe where name = 'Stufe 2') , (select id from verwendungsgruppe where name = 'VB 4a'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (89.42, (select id from kv_stufe where name = 'Stufe 2') , (select id from verwendungsgruppe where name = 'VB 5'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (94.8673684, (select id from kv_stufe where name = 'Stufe 2') , (select id from verwendungsgruppe where name = 'VB 6'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (111.7292105, (select id from kv_stufe where name = 'Stufe 2') , (select id from verwendungsgruppe where name = 'VB 7'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (131.7323684, (select id from kv_stufe where name = 'Stufe 2') , (select id from verwendungsgruppe where name = 'VB 8'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (64.9073684, (select id from kv_stufe where name = 'Stufe 3') , (select id from verwendungsgruppe where name = 'VB 1'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (69.7673684, (select id from kv_stufe where name = 'Stufe 3') , (select id from verwendungsgruppe where name = 'VB 2'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (75.9902632, (select id from kv_stufe where name = 'Stufe 3') , (select id from verwendungsgruppe where name = 'VB 3'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (83.9010526, (select id from kv_stufe where name = 'Stufe 3') , (select id from verwendungsgruppe where name = 'VB 4'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (85.9868421, (select id from kv_stufe where name = 'Stufe 3') , (select id from verwendungsgruppe where name = 'VB 4a'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (94.6813158, (select id from kv_stufe where name = 'Stufe 3') , (select id from verwendungsgruppe where name = 'VB 5'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (101.0894737, (select id from kv_stufe where name = 'Stufe 3') , (select id from verwendungsgruppe where name = 'VB 6'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (118.9352632, (select id from kv_stufe where name = 'Stufe 3') , (select id from verwendungsgruppe where name = 'VB 7'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (140.1542105, (select id from kv_stufe where name = 'Stufe 3') , (select id from verwendungsgruppe where name = 'VB 8'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (67.0231579, (select id from kv_stufe where name = 'Stufe 4') , (select id from verwendungsgruppe where name = 'VB 1'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (72.3018421, (select id from kv_stufe where name = 'Stufe 4') , (select id from verwendungsgruppe where name = 'VB 2'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (78.9531579, (select id from kv_stufe where name = 'Stufe 4') , (select id from verwendungsgruppe where name = 'VB 3'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (88.2028947, (select id from kv_stufe where name = 'Stufe 4') , (select id from verwendungsgruppe where name = 'VB 4'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (90.2892105, (select id from kv_stufe where name = 'Stufe 4') , (select id from verwendungsgruppe where name = 'VB 4a'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (99.9428947, (select id from kv_stufe where name = 'Stufe 4') , (select id from verwendungsgruppe where name = 'VB 5'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (107.3105263, (select id from kv_stufe where name = 'Stufe 4') , (select id from verwendungsgruppe where name = 'VB 6'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (126.1534211, (select id from kv_stufe where name = 'Stufe 4') , (select id from verwendungsgruppe where name = 'VB 7'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (148.5755263, (select id from kv_stufe where name = 'Stufe 4') , (select id from verwendungsgruppe where name = 'VB 8'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (69.1431579, (select id from kv_stufe where name = 'Stufe 5') , (select id from verwendungsgruppe where name = 'VB 1'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (74.8371053, (select id from kv_stufe where name = 'Stufe 5') , (select id from verwendungsgruppe where name = 'VB 2'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (82.0226316, (select id from kv_stufe where name = 'Stufe 5') , (select id from verwendungsgruppe where name = 'VB 3'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (92.5086842, (select id from kv_stufe where name = 'Stufe 5') , (select id from verwendungsgruppe where name = 'VB 4'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (94.5952632, (select id from kv_stufe where name = 'Stufe 5') , (select id from verwendungsgruppe where name = 'VB 4a'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (105.2089474, (select id from kv_stufe where name = 'Stufe 5') , (select id from verwendungsgruppe where name = 'VB 5'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (113.5318421, (select id from kv_stufe where name = 'Stufe 5') , (select id from verwendungsgruppe where name = 'VB 6'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (133.3823684, (select id from kv_stufe where name = 'Stufe 5') , (select id from verwendungsgruppe where name = 'VB 7'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (156.9976316, (select id from kv_stufe where name = 'Stufe 5') , (select id from verwendungsgruppe where name = 'VB 8'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (71.2634211, (select id from kv_stufe where name = 'Stufe 6') , (select id from verwendungsgruppe where name = 'VB 1'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (77.3713158, (select id from kv_stufe where name = 'Stufe 6') , (select id from verwendungsgruppe where name = 'VB 2'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (85.3163158, (select id from kv_stufe where name = 'Stufe 6') , (select id from verwendungsgruppe where name = 'VB 3'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (96.8105263, (select id from kv_stufe where name = 'Stufe 6') , (select id from verwendungsgruppe where name = 'VB 4'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (98.8965789, (select id from kv_stufe where name = 'Stufe 6') , (select id from verwendungsgruppe where name = 'VB 4a'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (110.4702632, (select id from kv_stufe where name = 'Stufe 6') , (select id from verwendungsgruppe where name = 'VB 5'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (119.7836842, (select id from kv_stufe where name = 'Stufe 6') , (select id from verwendungsgruppe where name = 'VB 6'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (140.6010526, (select id from kv_stufe where name = 'Stufe 6') , (select id from verwendungsgruppe where name = 'VB 7'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (165.4189474, (select id from kv_stufe where name = 'Stufe 6') , (select id from verwendungsgruppe where name = 'VB 8'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (73.3792105, (select id from kv_stufe where name = 'Stufe 7') , (select id from verwendungsgruppe where name = 'VB 1'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (79.9052632, (select id from kv_stufe where name = 'Stufe 7') , (select id from verwendungsgruppe where name = 'VB 2'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (88.6613158, (select id from kv_stufe where name = 'Stufe 7') , (select id from verwendungsgruppe where name = 'VB 3'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (101.1126316, (select id from kv_stufe where name = 'Stufe 7') , (select id from verwendungsgruppe where name = 'VB 4'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (103.1986842, (select id from kv_stufe where name = 'Stufe 7') , (select id from verwendungsgruppe where name = 'VB 4a'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (115.7386842, (select id from kv_stufe where name = 'Stufe 7') , (select id from verwendungsgruppe where name = 'VB 5'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (126.0423684, (select id from kv_stufe where name = 'Stufe 7') , (select id from verwendungsgruppe where name = 'VB 6'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (147.825, (select id from kv_stufe where name = 'Stufe 7') , (select id from verwendungsgruppe where name = 'VB 7'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (173.8363158, (select id from kv_stufe where name = 'Stufe 7') , (select id from verwendungsgruppe where name = 'VB 8'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (75.5031579, (select id from kv_stufe where name = 'Stufe 8') , (select id from verwendungsgruppe where name = 'VB 1'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (82.5884211, (select id from kv_stufe where name = 'Stufe 8') , (select id from verwendungsgruppe where name = 'VB 2'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (92.0115789, (select id from kv_stufe where name = 'Stufe 8') , (select id from verwendungsgruppe where name = 'VB 3'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (105.4142105, (select id from kv_stufe where name = 'Stufe 8') , (select id from verwendungsgruppe where name = 'VB 4'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (107.5005263, (select id from kv_stufe where name = 'Stufe 8') , (select id from verwendungsgruppe where name = 'VB 4a'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (121.0313158, (select id from kv_stufe where name = 'Stufe 8') , (select id from verwendungsgruppe where name = 'VB 5'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (132.2955263, (select id from kv_stufe where name = 'Stufe 8') , (select id from verwendungsgruppe where name = 'VB 6'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (155.0436842, (select id from kv_stufe where name = 'Stufe 8') , (select id from verwendungsgruppe where name = 'VB 7'), 1, current_user);
insert into gehalt (gehalt , kv_stufe, verwendungsgruppe, status, created_by) values (182.2618421, (select id from kv_stufe where name = 'Stufe 8') , (select id from verwendungsgruppe where name = 'VB 8'), 1, current_user);
