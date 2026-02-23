INSERT INTO zeiterfassung_reason (short_bezeichnung, bezeichnung, lhr_kz, created_by)
VALUES ('KS', 'Krankenstand', 'K', current_user),
       ('U', 'Unentschuldigt', 'NE', current_user),
       ('U', 'Unentschuldigt Abwesend', 'NE', current_user),
       ('K', 'Krank', 'K', current_user),
       ('NE', 'nicht entschuldigt Abwesend', 'NE', current_user),
       ('UE', 'unentschuldigt abwesend', 'NE', current_user),
       ('U', 'Unentschuldigte Fehlzeit', 'NE', current_user);

update zeiterfassung_reason
set created_by = current_user;