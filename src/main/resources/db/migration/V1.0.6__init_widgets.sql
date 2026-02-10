insert into widget (id, name, description, status, created_on, created_by, changed_by)
    OVERRIDING SYSTEM VALUE
values  (1, 'Meine Seminare', 'Meine Seminare Beschreibung', 0, current_date, current_user, null),
        (2, 'Fehlerhafte Teilnehmerdaten', 'Fehlerhafte Teilnehmerdaten Beschreibung', 0, current_date, current_user, null),
        (3, 'KPI Controlling', 'KPI Controlling Beschreibung', 0, current_date, current_user, null),
        (4, 'Meine persönlichen Daten', 'Meine persönlichen Daten Beschreibung', 0, current_date, current_user, null);