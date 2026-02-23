alter function teilnehmer_titles_audit() owner to ibosng;

create trigger teilnehmer_titles_insert
    after insert
    on teilnehmer_titles
    for each row
execute procedure teilnehmer_titles_audit();

create trigger teilnehmer_titles_update
    after update
    on teilnehmer_titles
    for each row
execute procedure teilnehmer_titles_audit();

create trigger teilnehmer_titles_delete
    after delete
    on teilnehmer_titles
    for each row
execute procedure teilnehmer_titles_audit();



alter table teilnehmer
    drop column nation;
alter table teilnehmer_history
    drop column nation;


create or replace function teilnehmer_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_history (teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info,  created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (OLD.id, OLD.nachname, OLD.vorname, OLD.geschlecht,
                OLD.sv_nummer, OLD.geburtsdatum, OLD.adresse,
                OLD.email, OLD.status, OLD.import_filename, OLD.info,  OLD.created_on, OLD.created_by, now(),
                OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_history (teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info,  created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id, NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info,  NEW.created_on, NEW.created_by, now(),
                NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_history (teilnehmer_id, nachname, vorname, geschlecht, sv_nummer, geburtsdatum,
                                        adresse,
                                        email, status, import_filename, info, created_on, created_by,
                                        changed_on, changed_by,
                                        action, action_timestamp)
        VALUES (NEW.id,  NEW.nachname, NEW.vorname, NEW.geschlecht,
                NEW.sv_nummer, NEW.geburtsdatum, NEW.adresse,
                NEW.email, NEW.status, NEW.import_filename, NEW.info, NEW.created_on, NEW.created_by, now(),
                NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;




create table if not exists teilnehmer_nations
(
    id         integer generated always as identity primary key,
    teilnehmer_id       integer references teilnehmer(id),
    land_id    integer references land(id)
);

create table if not exists teilnehmer_nations_history
(
    id               integer generated always as identity primary key,
    teilnehmer_nations_id     integer   not null,
    teilnehmer_id       integer,
    land_id    integer,
    action           char,
    action_timestamp timestamp not null
);

create or replace function teilnehmer_nations_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO teilnehmer_nations_history (teilnehmer_nations_id, teilnehmer_id, land_id,
                                                action,
                                                action_timestamp)
        VALUES (OLD.id, OLD.teilnehmer_id, OLD.land_id, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO teilnehmer_nations_history (teilnehmer_nations_id, teilnehmer_id, land_id,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.land_id, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO teilnehmer_nations_history (teilnehmer_nations_id, teilnehmer_id, land_id,
                                                action,
                                                action_timestamp)
        VALUES (NEW.id, NEW.teilnehmer_id, NEW.land_id,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


alter function teilnehmer_nations_audit() owner to ibosng;

create trigger teilnehmer_nations_insert
    after insert
    on teilnehmer_nations
    for each row
execute procedure teilnehmer_nations_audit();

create trigger teilnehmer_nations_update
    after update
    on teilnehmer_nations
    for each row
execute procedure teilnehmer_nations_audit();

create trigger teilnehmer_nations_delete
    after delete
    on teilnehmer_nations
    for each row
execute procedure teilnehmer_nations_audit();