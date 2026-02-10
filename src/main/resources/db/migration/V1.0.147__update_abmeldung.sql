ALTER TABLE abmeldung
    ADD CONSTRAINT uc_abmeldung_personalnummer UNIQUE (personalnummer);

ALTER TABLE abmeldung ALTER COLUMN austrittsgrund TYPE INTEGER USING austrittsgrund::integer;

ALTER TABLE abmeldung
    ADD CONSTRAINT FK_ABMELDUNG_ON_AUSTRITTSGRUND FOREIGN KEY (austrittsgrund) REFERENCES austrittsgrund (id);

ALTER TABLE abmeldung
    ADD CONSTRAINT FK_ABMELDUNG_ON_PERSONALNUMMER FOREIGN KEY (personalnummer) REFERENCES personalnummer (id);

ALTER TABLE abmeldung_history ALTER COLUMN austrittsgrund TYPE INTEGER USING austrittsgrund::integer;

ALTER TABLE abmeldung_history ADD CONSTRAINT pk_abmeldung_history PRIMARY KEY (id);
