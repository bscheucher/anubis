alter table vertragsaenderung add column genehmigender integer references benutzer (id);
alter table vertragsaenderung_history add column genehmigender integer;

create or replace function vertragsaenderung_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO vertragsaenderung_history (vertragsaenderung_id, personalnummer, antragssteller, gueltig_ab,
                                               successor, predecessor, interne_anmerkung, offizielle_bemerkung,
                                               kommentar, status, genehmigender,
                                               created_on, created_by, changed_on, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (OLD.id, OLD.personalnummer, OLD.antragssteller, OLD.gueltig_ab, OLD.successor, OLD.predecessor,
                OLD.interne_anmerkung, OLD.offizielle_bemerkung, OLD.kommentar, OLD.status, OLD.genehmigender,
                OLD.created_on, OLD.created_by, OLD.changed_on, OLD.changed_by, 'D',
                now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO vertragsaenderung_history (vertragsaenderung_id, personalnummer, antragssteller, gueltig_ab,
                                               successor, predecessor, interne_anmerkung, offizielle_bemerkung,
                                               kommentar, status, genehmigender,
                                               created_on, created_by, changed_on, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.antragssteller, NEW.gueltig_ab, NEW.successor, NEW.predecessor,
                NEW.interne_anmerkung, NEW.offizielle_bemerkung, NEW.kommentar, NEW.status, NEW.genehmigender,
                NEW.created_on, NEW.created_by, NEW.changed_on, NEW.changed_by, 'U',
                now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO vertragsaenderung_history (vertragsaenderung_id, personalnummer, antragssteller, gueltig_ab,
                                               successor, predecessor, interne_anmerkung, offizielle_bemerkung,
                                               kommentar, status, genehmigender,
                                               created_on, created_by, changed_on, changed_by,
                                               action,
                                               action_timestamp)
        VALUES (NEW.id, NEW.personalnummer, NEW.antragssteller, NEW.gueltig_ab, NEW.successor, NEW.predecessor,
                NEW.interne_anmerkung, NEW.offizielle_bemerkung, NEW.kommentar, NEW.status, NEW.genehmigender,
                NEW.created_on, NEW.created_by, NEW.changed_on, NULL, 'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;
