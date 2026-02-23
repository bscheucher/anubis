create table if not exists unterhaltsberechtigte
(
    id               integer generated always as identity primary key,
    vertragsdaten_id INTEGER REFERENCES vertragsdaten (id),
    vorname          text,
    nachname         text,
    svn              bigint,
    geburtsdatum     date,
    status           smallint  not null,
    created_on       timestamp not null,
    created_by       text      not null,
    changed_on       timestamp,
    changed_by       text
);

create table if not exists unterhaltsberechtigte_history
(
    id                       integer generated always as identity primary key,
    unterhaltsberechtigte_id integer   not null,
    vertragsdaten_id         INTEGER,
    vorname                  text,
    nachname                 text,
    svn                      bigint,
    geburtsdatum             date,
    status                   smallint  not null,
    created_on               timestamp not null,
    created_by               text      not null,
    changed_on               timestamp,
    changed_by               text,
    action                   char,
    action_timestamp         timestamp not null
);

create or replace function unterhaltsberechtigte_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO unterhaltsberechtigte_history (unterhaltsberechtigte_id, vertragsdaten_id, vorname, nachname, svn,
                                                   geburtsdatum, status, created_on,
                                                   created_by,
                                                   changed_by,
                                                   action,
                                                   action_timestamp)
        VALUES (OLD.id, OLD.vertragsdaten_id, NEW.vorname, NEW.nachname, NEW.svn, NEW.geburtsdatum, OLD.status, OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO unterhaltsberechtigte_history (unterhaltsberechtigte_id, vertragsdaten_id, vorname, nachname, svn,
                                                   geburtsdatum, status, status,
                                                   created_on,
                                                   created_by,
                                                   changed_by,
                                                   action,
                                                   action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vorname, NEW.nachname, NEW.svn, NEW.geburtsdatum,NEW.status, NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO unterhaltsberechtigte_history (unterhaltsberechtigte_id, vertragsdaten_id, vorname, nachname, svn,
                                                   geburtsdatum, status, status,
                                                   created_on,
                                                   created_by,
                                                   changed_by,
                                                   action,
                                                   action_timestamp)
        VALUES (NEW.id, NEW.vertragsdaten_id, NEW.vorname, NEW.nachname, NEW.svn, NEW.geburtsdatum, NEW.status, NEW.created_on, NEW.created_by, NULL,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


