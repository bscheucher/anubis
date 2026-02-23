create schema if not exists sync_to_ibosng;

create table if not exists sync_to_ibosng.error
(
    id                  integer generated always as identity primary key,
    aggregate_id        integer not null,
    aggregate_name      text not null,
    entity_name         text not null,
    entity              jsonb not null,
    message             text,
    created_at          timestamp default CURRENT_TIMESTAMP not null
);