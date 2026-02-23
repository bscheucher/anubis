update jobbeschreibung set name = 'Trainer_in' where name = 'Trainerin';
update jobbeschreibung set name = 'Betriebskontakter_in' where name = 'Betriebskontakterin';
update jobbeschreibung set name = 'Case Manger_in' where name = 'Case Mangerin';
update jobbeschreibung set name = 'Logistiker_in' where name = 'Logistikerin';
update jobbeschreibung set name = 'Lohnverrechner_in' where name = 'Lohnverrechnerin';
update jobbeschreibung set name = 'Buchhalter_in' where name = 'Buchhalterin';
update jobbeschreibung set name = 'IT Techniker_in' where name = 'Technikerin';
delete from jobbeschreibung where name = 'uvm';

INSERT INTO public.jobbeschreibung (name, created_by)
VALUES
    ('Coach', 'ibosng'),
    ('Trainer_in und Betriebskontakter_in', 'ibosng'),
    ('Trainer_in und Standortleitung', 'ibosng'),
    ('Trainer_in und Sicherheitsvertrauensperson', 'ibosng'),
    ('Trainer_in und Ersthelfer_in', 'ibosng'),
    ('Trainer_in und Gleichstellungsbeauftragte_r', 'ibosng'),
    ('Betriebskontakter_in und Sicherheitsvertrauensperson', 'ibosng'),
    ('Betriebskontakter_in und Standortleitung', 'ibosng'),
    ('Betriebskontakter_in und Ersthelfer_in', 'ibosng'),
    ('Betriebskontakter_in und Gleichstellungsbeauftagte', 'ibosng'),
    ('Coach und Ersthefer_in', 'ibosng'),
    ('Coach und Sicherheitsvertrauensperson', 'ibosng'),
    ('Coach und Standortleitung', 'ibosng'),
    ('Coach und Gleichstellungsbeauftragte', 'ibosng'),
    ('Geschäftsfeldleiter_in', 'ibosng'),
    ('Stv. Geschäftsfeldleiter_in', 'ibosng'),
    ('Projektleiter_in', 'ibosng'),
    ('Stv. Projektleiter_in', 'ibosng'),
    ('Ausbildungskoordinator_in', 'ibosng'),
    ('Projektadministrator_in', 'ibosng'),
    ('Teamleiter_in ServicCenter', 'ibosng'),
    ('Immobilienmanager_in', 'ibosng'),
    ('Mitarbeiter_in Service Center', 'ibosng'),
    ('Key Account Manager_in', 'ibosng'),
    ('Bildungskonzeptentwickler_in', 'ibosng'),
    ('Assistent_in Bildungskonzeptentwicklung', 'ibosng'),
    ('Leiter_in Content Creation & Innovation', 'ibosng'),
    ('Expert_in LMS & Plattformen', 'ibosng'),
    ('LMS & Plattform Support', 'ibosng'),
    ('Qualitätssicherung & Angebotsmanagement', 'ibosng'),
    ('Leiter_in People', 'ibosng'),
    ('Leiter_in QM & interne Kommunikation', 'ibosng'),
    ('Personaladministrator_in', 'ibosng'),
    ('Recruiting Expert_in', 'ibosng'),
    ('Qualitätsmanager_in', 'ibosng'),
    ('Teamleiter_in Projektcontrolling', 'ibosng'),
    ('Projektcontroller_in', 'ibosng'),
    ('Teamleiter_in Finance', 'ibosng'),
    ('Controller_in', 'ibosng'),
    ('Teamleiter_in IT Services', 'ibosng'),
    ('Teamleiter_in Application Management', 'ibosng'),
    ('IT Service Desk Mitarbeiter_in', 'ibosng'),
    ('Software Entwickler_in', 'ibosng'),
    ('Systemadministrator_in', 'ibosng'),
    ('Legal Counsel', 'ibosng'),
    ('Marketing', 'ibosng');