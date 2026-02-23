create table if not exists lhr_outbox
(
    id integer generated always as identity primary key,
    operation text not null,
    status text not null,
    data jsonb not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    synced_at timestamp,
    error_message text
);

create index if not exists idx_lhr_outbox_new_id
    on lhr_outbox (id)
    where status = 'NEW';