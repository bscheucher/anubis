alter table zeitbuchungstyp add column lhr_zeitspeicher integer;
alter table zeitbuchungstyp_history add column lhr_zeitspeicher integer;

alter table zeitbuchungstyp add column is_lhr_eintritt boolean default false;
alter table zeitbuchungstyp_history add column is_lhr_eintritt boolean;


create or replace function zeitbuchuntyp_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO zeitbuchungstyp_history (zeitbuchuntyp_id, type, lhr_kz, lhr_zeitspeicher, is_lhr_eintritt,
                                             created_on, created_by, changed_on, changed_by,
                                             action, action_timestamp)
        VALUES (OLD.id, OLD.type, OLD.lhr_kz, OLD.lhr_zeitspeicher, OLD.is_lhr_eintritt,
                OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D', NOW());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO zeitbuchungstyp_history (zeitbuchuntyp_id, type, lhr_kz, lhr_zeitspeicher, is_lhr_eintritt,
                                             created_on, created_by, changed_on, changed_by,
                                             action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.lhr_kz, NEW.lhr_zeitspeicher, NEW.is_lhr_eintritt,
                NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U', NOW());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO zeitbuchungstyp_history (zeitbuchuntyp_id, type, lhr_kz, lhr_zeitspeicher, is_lhr_eintritt,
                                             created_on, created_by, changed_on, changed_by,
                                             action, action_timestamp)
        VALUES (NEW.id, NEW.type, NEW.lhr_kz, NEW.lhr_zeitspeicher, NEW.is_lhr_eintritt,
                NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'I', NOW());
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$;

alter function zeitbuchuntyp_audit() owner to ibosng;

update zeitbuchungstyp set lhr_zeitspeicher = 20, is_lhr_eintritt = false where type = 'Behördenweg und sonstiges';


UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 28, is_lhr_eintritt = true WHERE type = 'Arbeitsunfall';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 20, is_lhr_eintritt = false WHERE type = 'Behördenweg und sonstiges';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 37, is_lhr_eintritt = true WHERE type = 'Dienstfreistellung';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 42, is_lhr_eintritt = false WHERE type = 'Ersatzruhe';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 15, is_lhr_eintritt = true WHERE type = 'Krankenstand';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 33, is_lhr_eintritt = false WHERE type = 'Mittagspause';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 21, is_lhr_eintritt = false WHERE type = 'Pflegefreistellung';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 52, is_lhr_eintritt = false WHERE type = 'Streik';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 16, is_lhr_eintritt = true WHERE type = 'Sonderurlaub';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 13, is_lhr_eintritt = true WHERE type = 'Urlaub';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 60, is_lhr_eintritt = true WHERE type = 'unbezahlter Urlaub';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 18, is_lhr_eintritt = false WHERE type = 'Weiterbildung';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 12, is_lhr_eintritt = false WHERE type = 'Zeitausgleich';

UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 48, is_lhr_eintritt = false WHERE type = 'ibis Event';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 49, is_lhr_eintritt = false WHERE type = 'ibis Event Konsum';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 14, is_lhr_eintritt = false WHERE type = 'Arztbesuch';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 39, is_lhr_eintritt = false WHERE type = 'Bildungsfreistellung';
UPDATE zeitbuchungstyp SET lhr_zeitspeicher = 92, is_lhr_eintritt = false WHERE type = 'Dienstfreistellung Betriebsversammlung';