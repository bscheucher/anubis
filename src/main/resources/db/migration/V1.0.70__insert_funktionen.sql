insert into funktionen (name , description, created_by)
values ('FN_MA_ONBOARDING', 'Mitarbeiter Onboarding.', current_user),
       ('FN_TEILNEHMERINNEN_LESEN', 'TeilnehmerInnen lesen.', current_user),
       ('FN_TEILNEHMERINNEN_BEARBEITEN', 'TeilnehmerInnen bearbeiten.', current_user),
       ('FN_TEILNEHMERINNEN_ANLEGEN', 'TeilnehmerInnen anlegen.', current_user);

ALTER TABLE user_session_history
    DROP CONSTRAINT fk_user_session_history;
