ALTER TABLE bankverbindung DROP CONSTRAINT uk_iban_or_kto_nummer;
ALTER TABLE auftraggeber_bank DROP CONSTRAINT uk_firmenbank_name;
ALTER TABLE auftraggeber_bank DROP CONSTRAINT uk_firmenbank_nummer;
ALTER TABLE dienstnehmer DROP CONSTRAINT uk_sv_nummer;