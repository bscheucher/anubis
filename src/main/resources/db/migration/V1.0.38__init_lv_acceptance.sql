create table if not exists lv_acceptance
(
    id                          integer generated always as identity primary key,
    personalnummer           integer references personalnummer (id),
    bankcard                    boolean,
    bankcardReason              text,
    ecard                       boolean,
    ecardReason                 text,
    arbeitsgenehmigungDok       boolean,
    arbeitsgenehmigungDokReason text,
    gehaltEinstufung            boolean,
    gehaltEinstufungReason      text,
    status                      smallint  not null,
    created_on                  timestamp not null,
    created_by                  text      not null,
    changed_on                  timestamp,
    changed_by                  text
);

create table if not exists lv_acceptance_history
(
    id                          integer generated always as identity primary key,
    lv_acceptance_id            integer   not null,
    personalnummer           integer,
    bankcard                    boolean,
    bankcardReason              text,
    ecard                       boolean,
    ecardReason                 text,
    arbeitsgenehmigungDok       boolean,
    arbeitsgenehmigungDokReason text,
    gehaltEinstufung            boolean,
    gehaltEinstufungReason      text,
    status                      smallint  not null,
    created_on                  timestamp not null,
    created_by                  text      not null,
    changed_on                  timestamp,
    changed_by                  text,
    action                      char,
    action_timestamp            timestamp not null
);

create or replace function lv_acceptance_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO lv_acceptance_history (lv_acceptance_id, personalnummer, bankcard, bankcardReason, ecard,
                                           ecardReason, arbeitsgenehmigungDok,
                                           arbeitsgenehmigungDokReason, gehaltEinstufung, gehaltEinstufungReason,
                                           status, created_on,
                                           created_by,
                                           changed_by,
                                           action,
                                           action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.bankcard, OLD.bankcardReason, OLD.ecard, OLD.ecardReason,
                OLD.arbeitsgenehmigungDok,
                OLD.arbeitsgenehmigungDokReason, OLD.gehaltEinstufung, OLD.gehaltEinstufungReason, OLD.status,
                OLD.created_on, OLD.created_by,
                OLD.changed_by, 'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO lv_acceptance_history (lv_acceptance_id, personalnummer, bankcard, bankcardReason, ecard,
                                           ecardReason, arbeitsgenehmigungDok,
                                           arbeitsgenehmigungDokReason, gehaltEinstufung, gehaltEinstufungReason,
                                           status, status, created_on,
                                           created_by,
                                           changed_by,
                                           action,
                                           action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.bankcard, NEW.bankcardReason, NEW.ecard, NEW.ecardReason,
                NEW.arbeitsgenehmigungDok,
                NEW.arbeitsgenehmigungDokReason, NEW.gehaltEinstufung, NEW.gehaltEinstufungReason, NEW.status,
                NEW.created_on, NEW.created_by,
                NEW.changed_by, 'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO lv_acceptance_history (lv_acceptance_id, personalnummer, bankcard, bankcardReason, ecard,
                                           ecardReason, arbeitsgenehmigungDok,
                                           arbeitsgenehmigungDokReason, gehaltEinstufung, gehaltEinstufungReason,
                                           status, status, created_on,
                                           created_by,
                                           changed_by,
                                           action,
                                           action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.bankcard, NEW.bankcardReason, NEW.ecard, NEW.ecardReason,
                NEW.arbeitsgenehmigungDok,
                NEW.arbeitsgenehmigungDokReason, NEW.gehaltEinstufung, NEW.gehaltEinstufungReason, NEW.status,
                NEW.created_on, NEW.created_by, NULL,
                'I',
                now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;


