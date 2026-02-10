create trigger vordienstzeiten_insert
    after insert
    on vordienstzeiten
    for each row
    execute procedure vordienstzeiten_audit();

create trigger vordienstzeiten_update
    after update
    on vordienstzeiten
    for each row
    execute procedure vordienstzeiten_audit();

create trigger vordienstzeiten_delete
    after delete
    on vordienstzeiten
    for each row
    execute procedure vordienstzeiten_audit();