ALTER TABLE land ADD CONSTRAINT uk_land_name UNIQUE (land_name);

ALTER TABLE auftraggeber_bank ADD CONSTRAINT uk_firmenbank_name UNIQUE (firmenbank_name);
ALTER TABLE auftraggeber_bank ADD CONSTRAINT uk_firmenbank_nummer UNIQUE (firmenbank_nummer);
ALTER TABLE auftraggeber_bank ADD CONSTRAINT uk_bankleitzahl UNIQUE (bankleitzahl);

ALTER TABLE bankverbindung ADD CONSTRAINT uk_iban_or_kto_nummer UNIQUE (iban_or_kto_nummer);

ALTER TABLE dienstnehmer ADD CONSTRAINT uk_dn_nr UNIQUE (dn_nr);
ALTER TABLE dienstnehmer ADD CONSTRAINT uk_sv_nummer UNIQUE (sv_nummer);

ALTER TABLE religion ADD CONSTRAINT uk_designator UNIQUE (designator);
ALTER TABLE religion ADD CONSTRAINT uk_name UNIQUE (name);

--ALTER TABLE beruf ADD CONSTRAINT uk_name UNIQUE (name);

--ALTER TABLE telefon ADD CONSTRAINT uk_telefonnummer UNIQUE (telefonnummer);

ALTER TABLE vorwahl ADD CONSTRAINT uk_vorwahl UNIQUE (vorwahl);
