INSERT INTO jobbeschreibung ( name, created_on, created_by)
select  'Berufsausbildungsassistenz',now(),'ibosng'
    where not exists ( select 1 from jobbeschreibung where name = 'Berufsausbildungsassistenz');

INSERT INTO jobbeschreibung ( name, created_on, created_by)
select  'Servicekraft Leopold',now(),'ibosng'
    where not exists ( select 1 from jobbeschreibung where name = 'Servicekraft Leopold');

INSERT INTO jobbeschreibung ( name, created_on, created_by)
select  'Psychotherapeut_in',now(),'ibosng'
    where not exists ( select 1 from jobbeschreibung where name = 'Psychotherapeut_in');

INSERT INTO taetigkeit (name,created_on,created_by)
select  'Aushilfe' , now(), 'ibosng'
    where not exists (select 1 from taetigkeit where name = 'Aushilfe');

INSERT INTO jobbeschreibung (name, created_on, created_by)
VALUES ('Prüfer_in', now(), 'ibosng');

INSERT INTO jobbeschreibung (name, created_on, created_by)
VALUES ('Trainer_in und Prüfer_in', now(), 'ibosng');

INSERT INTO jobbeschreibung (name, created_on, created_by)
VALUES ('Trainer_in und Case Manager_in', now(), 'ibosng');
