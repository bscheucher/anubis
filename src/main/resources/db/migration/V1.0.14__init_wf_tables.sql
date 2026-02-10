create table if not exists s_workflow_groups
(
    id         integer generated always as identity primary key,
    name       text,
    status     smallint  not null,
    created_on timestamp not null,
    created_by text      not null,
    changed_on timestamp,
    changed_by text
);

create table if not exists s_workflow_groups_history
(
    id                 integer generated always as identity primary key,
    workflow_groups_id integer   not null,
    name               text,
    status             smallint  not null,
    created_on         timestamp not null,
    created_by         text      not null,
    changed_on         timestamp,
    changed_by         text,
    action             char,
    action_timestamp   timestamp not null
);

create or replace function s_workflow_groups_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO s_workflow_groups_history (workflow_groups_id, name, status, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.status, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO s_workflow_groups_history (workflow_groups_id, name, status, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO s_workflow_groups_history (workflow_groups_id, name, status, created_on, created_by, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function s_workflow_groups_audit() owner to ibosng;

create trigger s_workflow_groups_insert
    after insert
    on s_workflow_groups
    for each row
execute procedure s_workflow_groups_audit();

create trigger s_workflow_groups_update
    after update
    on s_workflow_groups
    for each row
execute procedure s_workflow_groups_audit();

create trigger s_workflow_groups_delete
    after delete
    on s_workflow_groups
    for each row
execute procedure s_workflow_groups_audit();



create table if not exists s_workflows
(
    id             integer generated always as identity primary key,
    name           text,
    workflow_group integer references s_workflow_groups (id),
    status         smallint  not null,
    successor      integer references s_workflows (id),
    predecessor    integer references s_workflows (id),
    created_on     timestamp not null,
    created_by     text      not null,
    changed_on     timestamp,
    changed_by     text
);

create table if not exists s_workflows_history
(
    id               integer generated always as identity primary key,
    workflows_id     integer   not null,
    name             text,
    workflow_group   integer,
    status           smallint  not null,
    successor        integer,
    predecessor      integer,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function s_workflows_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO s_workflows_history (workflows_id, name, workflow_group, status, successor, predecessor, created_on,
                                         created_by, changed_by,
                                         action,
                                         action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.workflow_group, OLD.status, OLD.successor, OLD.predecessor, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO s_workflows_history (workflows_id, name, workflow_group, status, successor, predecessor, created_on,
                                         created_by, changed_by,
                                         action,
                                         action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.workflow_group, NEW.status, NEW.successor, NEW.predecessor, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO s_workflows_history (workflows_id, name, workflow_group, status, successor, predecessor, created_on,
                                         created_by, changed_by,
                                         action,
                                         action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.workflow_group, NEW.status, NEW.successor, NEW.predecessor, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function s_workflows_audit() owner to ibosng;

create trigger s_workflows_insert
    after insert
    on s_workflows
    for each row
execute procedure s_workflows_audit();

create trigger s_workflows_update
    after update
    on s_workflows
    for each row
execute procedure s_workflows_audit();

create trigger s_workflows_delete
    after delete
    on s_workflows
    for each row
execute procedure s_workflows_audit();



create table if not exists s_workflow_items
(
    id          integer generated always as identity primary key,
    name        text,
    workflow    integer references s_workflows (id),
    status      smallint  not null,
    successor   integer references s_workflow_items (id),
    predecessor integer references s_workflow_items (id),
    created_on  timestamp not null,
    created_by  text      not null,
    changed_on  timestamp,
    changed_by  text
);

create table if not exists s_workflow_items_history
(
    id                integer generated always as identity primary key,
    workflow_items_id integer   not null,
    name              text,
    workflow          integer,
    status            smallint  not null,
    successor         integer,
    predecessor       integer,
    created_on        timestamp not null,
    created_by        text      not null,
    changed_on        timestamp,
    changed_by        text,
    action            char,
    action_timestamp  timestamp not null
);

create or replace function s_workflow_items_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO s_workflow_items_history (workflow_items_id, name, workflow, status, successor, predecessor,
                                              created_on, created_by,
                                              changed_by,
                                              action, action_timestamp)
        VALUES (OLD.id, OLD.name, OLD.workflow, OLD.status, OLD.successor, OLD.predecessor, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO s_workflow_items_history (workflow_items_id, name, workflow, status, successor, predecessor,
                                              created_on, created_by,
                                              changed_by,
                                              action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.workflow, NEW.status, NEW.successor, NEW.predecessor, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO s_workflow_items_history (workflow_items_id, name, workflow, status, successor, predecessor,
                                              created_on, created_by,
                                              changed_by,
                                              action, action_timestamp)
        VALUES (NEW.id, NEW.name, NEW.workflow, NEW.status, NEW.successor, NEW.predecessor, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function s_workflow_items_audit() owner to ibosng;

create trigger s_workflowitems_insert
    after insert
    on s_workflow_items
    for each row
execute procedure s_workflow_items_audit();

create trigger s_workflowitems_update
    after update
    on s_workflow_items
    for each row
execute procedure s_workflow_items_audit();

create trigger s_workflows_delete
    after delete
    on s_workflow_items
    for each row
execute procedure s_workflow_items_audit();


--WORK TABLES

create table if not exists w_workflow_groups
(
    id             integer generated always as identity primary key,
    workflow_group integer references s_workflow_groups (id),
    data        text,
    status         smallint  not null,
    created_on     timestamp not null,
    created_by     text      not null,
    changed_on     timestamp,
    changed_by     text
);

create table if not exists w_workflow_groups_history
(
    id                 integer generated always as identity primary key,
    workflow_groups_id integer   not null,
    workflow_group     integer,
    data        text,
    status             smallint  not null,
    created_on         timestamp not null,
    created_by         text      not null,
    changed_on         timestamp,
    changed_by         text,
    action             char,
    action_timestamp   timestamp not null
);

create or replace function w_workflow_groups_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO w_workflow_groups_history (workflow_groups_id, workflow_group, data, status, created_on, created_by,
                                               changed_by,
                                               action,
                                               action_timestamp)
        VALUES (OLD.id, OLD.workflow_group, OLD.data, OLD.status, OLD.created_on, OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO w_workflow_groups_history (workflow_groups_id, workflow_group, data, status, created_on, created_by,
                                               changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.workflow_group, NEW.data, NEW.status, NEW.created_on, NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO w_workflow_groups_history (workflow_groups_id, workflow_group, data, status, created_on, created_by,
                                               changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.workflow_group, NEW.data, NEW.status, NEW.created_on, NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function w_workflow_groups_audit() owner to ibosng;

create trigger w_workflow_groups_insert
    after insert
    on w_workflow_groups
    for each row
execute procedure w_workflow_groups_audit();

create trigger w_workflow_groups_update
    after update
    on w_workflow_groups
    for each row
execute procedure w_workflow_groups_audit();

create trigger w_workflow_groups_delete
    after delete
    on w_workflow_groups
    for each row
execute procedure w_workflow_groups_audit();



create table if not exists w_workflows
(
    id             integer generated always as identity primary key,
    workflow       integer references s_workflows (id),
    workflow_group integer references w_workflow_groups (id),
    data            text,
    status         smallint  not null,
    successor      integer references w_workflows (id),
    predecessor    integer references w_workflows (id),
    created_on     timestamp not null,
    created_by     text      not null,
    changed_on     timestamp,
    changed_by     text
);

create table if not exists w_workflows_history
(
    id               integer generated always as identity primary key,
    workflows_id     integer   not null,
    workflow         integer,
    workflow_group   integer,
    data            text,
    status           smallint  not null,
    successor        integer,
    predecessor      integer,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text,
    action           char,
    action_timestamp timestamp not null
);

create or replace function w_workflows_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO w_workflows_history (workflows_id, workflow, workflow_group, data, status, successor, predecessor,
                                         created_on, created_by, changed_by,
                                         action,
                                         action_timestamp)
        VALUES (OLD.id, OLD.workflow, OLD.workflow_group, OLD.status, OLD.status, OLD.successor, OLD.predecessor, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO w_workflows_history (workflows_id, workflow, workflow_group, data, status, successor, predecessor,
                                         created_on, created_by, changed_by,
                                         action,
                                         action_timestamp)
        VALUES (NEW.id, NEW.workflow, NEW.workflow_group, NEW.status, NEW.status, NEW.successor, NEW.predecessor, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO w_workflows_history (workflows_id, workflow, workflow_group, data, status, successor, predecessor,
                                         created_on, created_by, changed_by,
                                         action,
                                         action_timestamp)
        VALUES (NEW.id, NEW.workflow, NEW.workflow_group, NEW.status, NEW.status, NEW.successor, NEW.predecessor, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function w_workflows_audit() owner to ibosng;

create trigger w_workflows_insert
    after insert
    on w_workflows
    for each row
execute procedure w_workflows_audit();

create trigger w_workflows_update
    after update
    on w_workflows
    for each row
execute procedure w_workflows_audit();

create trigger w_workflows_delete
    after delete
    on w_workflows
    for each row
execute procedure w_workflows_audit();



create table if not exists w_workflow_items
(
    id            integer generated always as identity primary key,
    workflow_item integer references s_workflow_items (id),
    workflow      integer references w_workflows (id),
    data            text,
    status        smallint  not null,
    successor     integer references w_workflow_items (id),
    predecessor   integer references w_workflow_items (id),
    created_on    timestamp not null,
    created_by    text      not null,
    changed_on    timestamp,
    changed_by    text
);

create table if not exists w_workflow_items_history
(
    id                integer generated always as identity primary key,
    workflow_items_id integer   not null,
    workflow_item     integer,
    workflow          integer,
    data            text,
    status            smallint  not null,
    successor         integer,
    predecessor       integer,
    created_on        timestamp not null,
    created_by        text      not null,
    changed_on        timestamp,
    changed_by        text,
    action            char,
    action_timestamp  timestamp not null
);

create or replace function w_workflow_items_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO w_workflow_items_history (workflow_items_id, workflow_item, workflow, data, status, successor,
                                              predecessor, created_on, created_by,
                                              changed_by,
                                              action, action_timestamp)
        VALUES (OLD.id, OLD.workflow_item, OLD.workflow, OLD.status, OLD.status, OLD.successor, OLD.predecessor, OLD.created_on,
                OLD.created_by, OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO w_workflow_items_history (workflow_items_id, workflow_item, workflow, data, status, successor,
                                              predecessor, created_on, created_by,
                                              changed_by,
                                              action, action_timestamp)
        VALUES (NEW.id, NEW.workflow_item, NEW.workflow, NEW.status, NEW.status, NEW.successor, NEW.predecessor, NEW.created_on,
                NEW.created_by, NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO w_workflow_items_history (workflow_items_id, workflow_item, workflow, data, status, successor,
                                              predecessor, created_on, created_by,
                                              changed_by,
                                              action, action_timestamp)
        VALUES (NEW.id, NEW.workflow_item, NEW.workflow, NEW.status, NEW.status, NEW.successor, NEW.predecessor, NEW.created_on,
                NEW.created_by, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function w_workflow_items_audit() owner to ibosng;

create trigger w_workflow_items_insert
    after insert
    on w_workflow_items
    for each row
execute procedure w_workflow_items_audit();

create trigger w_workflow_items_update
    after update
    on w_workflow_items
    for each row
execute procedure w_workflow_items_audit();

create trigger w_workflow_items_delete
    after delete
    on w_workflow_items
    for each row
execute procedure w_workflow_items_audit();
