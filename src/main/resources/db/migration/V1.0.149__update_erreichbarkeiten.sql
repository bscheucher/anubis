alter table erreichbarkeit
    rename column wert to erreichbarkeitsart;
alter table erreichbarkeit_history
    rename column wert to erreichbarkeitsart;

alter table erreichbarkeit
    alter column created_on set default CURRENT_TIMESTAMP;
alter table erreichbarkeit_history
    alter column created_on set default CURRENT_TIMESTAMP;

alter table erreichbarkeit
    drop column dienstnehmer;
alter table erreichbarkeit_history
    drop column dienstnehmer;

create or replace function erreichbarkeit_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO erreichbarkeit_history (erreichbarkeit_id, kz, erreichbarkeitsart, created_on,
                                            created_by, changed_by,
                                            action, action_timestamp)
        VALUES (OLD.id, OLD.kz, OLD.erreichbarkeitsart, OLD.created_on, OLD.created_by,
                OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO erreichbarkeit_history (erreichbarkeit_id, kz, erreichbarkeitsart, created_on,
                                            created_by, changed_by,
                                            action, action_timestamp)
        VALUES (NEW.id, NEW.kz, NEW.erreichbarkeitsart, NEW.created_on, NEW.created_by,
                NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO erreichbarkeit_history (erreichbarkeit_id, kz, erreichbarkeitsart, created_on,
                                            created_by, changed_by,
                                            action, action_timestamp)
        VALUES (NEW.id, NEW.kz, NEW.erreichbarkeitsart, NEW.created_on, NEW.created_by, NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function erreichbarkeit_audit() owner to ibosng;



INSERT INTO erreichbarkeit (erreichbarkeitsart, kz, created_by)
VALUES ('E-Mail-Adresse', 'EMAIL', current_user),
       ('E-Mail-Adresse alternativ für Versand', 'EMAILAV', current_user),
       ('E-Mail-Adresse privat', 'EMAILPRIV', current_user),
       ('Telefaxnummer', 'FAX', current_user),
       ('Telefaxnummer privat', 'FAXPRIV', current_user),
       ('Mobiltelefonnummer', 'MOBIL', current_user),
       ('Mobiltelefonnummer privat', 'MOBILPRIV', current_user),
       ('Telefonnummer', 'TEL', current_user),
       ('Telefonnummer privat', 'TELPRIV', current_user),
       ('2. Adresse', '2ADRESS', current_user);

