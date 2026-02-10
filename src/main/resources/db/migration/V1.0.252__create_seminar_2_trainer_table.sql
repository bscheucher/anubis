create table if not exists seminar_2_trainer
(
    id                      integer generated always as identity
        primary key,
    seminar_id integer                                          not null
        references seminar(id),
    trainer_id           integer                             not null
        references benutzer(id),
    dienstvertrag_nr        integer                             not null,
    role                    varchar(40),
    trainer_type            integer,
    start_date              date,
    end_date                date,
    status                  integer,
    created_on              timestamp,
    action                  char,
    action_timestamp        timestamp default CURRENT_TIMESTAMP not null
);

ALTER TABLE seminar_2_trainer
    OWNER TO ibosng;

CREATE TABLE IF NOT EXISTS seminar_2_trainer_history
(
    id                      integer generated always as identity
        primary key,
    seminar_2_trainer_id    integer                             not null,
    seminar_id              integer                             not null,
    trainer_id              integer                             not null,
    dienstvertrag_nr        integer                             not null,
    role                    varchar(40),
    trainer_type            integer,
    start_date              date,
    end_date                date,
    status                  integer,
    created_on              timestamp,
    action                  char(1) not null ,
    action_timestamp        timestamp default CURRENT_TIMESTAMP not null
);

ALTER TABLE seminar_2_trainer_history
    OWNER TO ibosng;

CREATE OR REPLACE FUNCTION seminar_2_trainer_audit() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO seminar_2_trainer_history (seminar_2_trainer_id, seminar_id, trainer_id, dienstvertrag_nr, role,
                                               trainer_type, start_date, end_date, status, created_on,
                                               action, action_timestamp)
        VALUES (OLD.seminar_2_trainer_id, OLD.seminar_id, OLD.trainer_id, OLD.dienstvertrag_nr, OLD.role,
                OLD.trainer_type, OLD.start_date, OLD.end_date, OLD.status, OLD.created_on, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO seminar_2_trainer_history (seminar_2_trainer_id, seminar_id, trainer_id, dienstvertrag_nr, role,
                                               trainer_type, start_date, end_date, status, created_on,
                                               action, action_timestamp)
        VALUES (NEW.seminar_2_trainer_id, NEW.seminar_id, NEW.trainer_id, NEW.dienstvertrag_nr, NEW.role,
                NEW.trainer_type, NEW.start_date, NEW.end_date, NEW.status, NEW.created_on, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO seminar_2_trainer_history (seminar_2_trainer_id, seminar_id, trainer_id, dienstvertrag_nr, role,
                                               trainer_type, start_date, end_date, status, created_on,
                                               action, action_timestamp)
        VALUES (NEW.seminar_2_trainer_id, NEW.seminar_id, NEW.trainer_id, NEW.dienstvertrag_nr, NEW.role,
                NEW.trainer_type, NEW.start_date, NEW.end_date, NEW.status, NEW.created_on, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- Result is ignored since this is an AFTER trigger
END;
$$;

alter function seminar_2_trainer_audit() owner to ibosng;

create trigger seminar_2_trainer_insert
    after insert
    on seminar_2_trainer
    for each row
execute procedure seminar_2_trainer_audit();

create trigger seminar_2_trainer_update
    after update
    on seminar_2_trainer
    for each row
execute procedure seminar_2_trainer_audit();

create trigger seminar_2_trainer_delete
    after delete
    on seminar_2_trainer
    for each row
execute procedure seminar_2_trainer_audit();
