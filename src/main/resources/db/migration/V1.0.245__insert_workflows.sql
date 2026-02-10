-- Workflow Group
insert into public.s_workflow_groups(name, status, created_on, created_by, changed_on, changed_by)
values ('Vertragsaenderung starten und vervollstaendigen', 1, current_date, current_user, null, null);

--  Workflows
insert into public.s_workflows(name, status, workflow_group, created_on, created_by, changed_on, changed_by)
values ('Vertragsaenderung Starten', 1,
        (select id from s_workflow_groups where name = 'Vertragsaenderung starten und vervollstaendigen'),
        current_date, current_user, null, null);

insert into public.s_workflows(name, status, workflow_group, created_on, created_by, changed_on, changed_by)
values ('Vertragsdaten an People informieren und ueberpruefen', 1,
        (select id from s_workflow_groups where name = 'Vertragsaenderung starten und vervollstaendigen'),
        current_date, current_user, null, null);

insert into public.s_workflows(name, status, workflow_group, created_on, created_by, changed_on, changed_by)
values ('Vertragsdaten an Lohnverrechnung informieren und ueberpruefen', 1,
        (select id from s_workflow_groups where name = 'Vertragsaenderung starten und vervollstaendigen'),
        current_date, current_user, null, null);

insert into public.s_workflows(name, status, workflow_group, created_on, created_by, changed_on, changed_by)
values ('Vertragsdaten an Gehnehmiger informieren und ueberpruefen', 1,
        (select id from s_workflow_groups where name = 'Vertragsaenderung starten und vervollstaendigen'),
        current_date, current_user, null, null);


insert into public.s_workflows(name, status, workflow_group, created_on, created_by, changed_on, changed_by)
values ('Unterschriftenlauf fuer Vertragsaenderung', 1,
        (select id from s_workflow_groups where name = 'Vertragsaenderung starten und vervollstaendigen'),
        current_date, current_user, null, null);

insert into public.s_workflows(name, status, workflow_group, created_on, created_by, changed_on, changed_by)
values ('Vertragsaenderung finalisieren', 1,
        (select id from s_workflow_groups where name = 'Vertragsaenderung starten und vervollstaendigen'),
        current_date, current_user, null, null);


-- Workflow items 'Vertragsdaten Starten'
insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsaenderung Daten vervollstaendigen', (select id from s_workflows where name = 'Vertragsaenderung Starten'),
        1, null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsaenderung Zusatzdokument erstellen', (select id from s_workflows where name = 'Vertragsaenderung Starten'),
        1, null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsaenderung zur Pruefung vorlegen', (select id from s_workflows where name = 'Vertragsaenderung Starten'),
        1, null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsaenderung people informieren', (select id from s_workflows where name = 'Vertragsdaten an People informieren und ueberpruefen'),
        1, null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsaenderung people prueft', (select id from s_workflows where name = 'Vertragsdaten an People informieren und ueberpruefen'),
        1, null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsaenderung Lohnverrechnung informieren', (select id from s_workflows where name = 'Vertragsdaten an Lohnverrechnung informieren und ueberpruefen'),
        1, null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsaenderung Lohnverrechnung prueft', (select id from s_workflows where name = 'Vertragsdaten an Lohnverrechnung informieren und ueberpruefen'),
        1, null, null, current_date, current_user, null, null);


insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsaenderung Genehmiger informieren', (select id from s_workflows where name = 'Vertragsdaten an Gehnehmiger informieren und ueberpruefen'),
        1, null, null, current_date, current_user, null, null);

insert into public.s_workflow_items (name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                     changed_by)
values ('Vertragsaenderung Genehmiger prueft', (select id from s_workflows where name = 'Vertragsdaten an Gehnehmiger informieren und ueberpruefen'),
        1, null, null, current_date, current_user, null, null);


-- Add Workflow Items for 'Unterschriftenlauf fuer Vertragsaenderung'
insert into public.s_workflow_items(name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                    changed_by)
values ('Vertragsaenderung Unterschriftenlauf starten', (select id from s_workflows where name = 'Unterschriftenlauf fuer Vertragsaenderung'), 1,
        null, null, current_date, current_user, null, null);


insert into public.s_workflow_items(name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                    changed_by)
values ('Vertragsaenderung Unterschriftenlauf durchfuehren', (select id from s_workflows where name = 'Unterschriftenlauf fuer Vertragsaenderung'), 1,
        null, null, current_date, current_user, null, null);

insert into public.s_workflow_items(name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                    changed_by)
values ('Vertragsaenderung Unterschriebenes Dokument speichern',
        (select id from s_workflows where name = 'Unterschriftenlauf fuer Vertragsaenderung'), 1, null, null, current_date,
        current_user, null, null);

-- Workflow Items for Post-Processing
insert into public.s_workflow_items(name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                    changed_by)
values ('Vertragsaenderung Zusatz im System eintragen', (select id from s_workflows where name = 'Vertragsaenderung finalisieren'), 1,
        null, null, current_date, current_user, null, null);

insert into public.s_workflow_items(name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                    changed_by)
values ('Vertragsaenderung Daten an LHR uebermitteln',
        (select id from s_workflows where name = 'Vertragsaenderung finalisieren'), 1, null, null, current_date,
        current_user, null, null);

insert into public.s_workflow_items(name, workflow, status, successor, predecessor, created_on, created_by, changed_on,
                                    changed_by)
values ('Vertragsaenderung Beteiligten informieren',
        (select id from s_workflows where name = 'Vertragsaenderung finalisieren'), 1, null, null, current_date,
        current_user, null, null);


-- successor workflows
update public.s_workflows
set successor = (select id from public.s_workflows where name = 'Vertragsdaten an People informieren und ueberpruefen')
where name = 'Vertragsaenderung Starten';

update public.s_workflows
set successor = (select id from public.s_workflows where name = 'Vertragsdaten an Lohnverrechnung informieren und ueberpruefen')
where name = 'Vertragsdaten an People informieren und ueberpruefen';

update public.s_workflows
set successor = (select id from public.s_workflows where name = 'Vertragsdaten an Gehnehmiger informieren und ueberpruefen')
where name = 'Vertragsdaten an Lohnverrechnung informieren und ueberpruefen';

update public.s_workflows
set successor = (select id from public.s_workflows where name = 'Unterschriftenlauf fuer Vertragsaenderung')
where name = 'Vertragsdaten an Gehnehmiger informieren und ueberpruefen';

update public.s_workflows
set successor = (select id from public.s_workflows where name = 'Vertragsaenderung finalisieren')
where name = 'Unterschriftenlauf fuer Vertragsaenderung';

-- predecessor workflows
update public.s_workflows
set predecessor = (select id from public.s_workflows where name = 'Vertragsaenderung Starten')
where name = 'Vertragsdaten an People informieren und ueberpruefen';

update public.s_workflows
set predecessor = (select id from public.s_workflows where name = 'Vertragsdaten an People informieren und ueberpruefen')
where name = 'Vertragsdaten an Lohnverrechnung informieren und ueberpruefen';

update public.s_workflows
set predecessor = (select id from public.s_workflows where name = 'Vertragsdaten an Lohnverrechnung informieren und ueberpruefen')
where name = 'Vertragsdaten an Gehnehmiger informieren und ueberpruefen';

update public.s_workflows
set predecessor = (select id from public.s_workflows where name = 'Vertragsdaten an Gehnehmiger informieren und ueberpruefen')
where name = 'Unterschriftenlauf fuer Vertragsaenderung';

update public.s_workflows
set predecessor = (select id from public.s_workflows where name = 'Unterschriftenlauf fuer Vertragsaenderung')
where name = 'Vertragsaenderung finalisieren';

-- successor workflow_items

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Zusatzdokument erstellen')
where name = 'Vertragsaenderung Daten vervollstaendigen';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung zur Pruefung vorlegen')
where name = 'Vertragsaenderung Zusatzdokument erstellen';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung people informieren')
where name = 'Vertragsaenderung zur Pruefung vorlegen';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung people prueft')
where name = 'Vertragsaenderung people informieren';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Lohnverrechnung informieren')
where name = 'Vertragsaenderung people prueft';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Lohnverrechnung prueft')
where name = 'Vertragsaenderung Lohnverrechnung informieren';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Genehmiger informieren')
where name = 'Vertragsaenderung Lohnverrechnung prueft';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Genehmiger prueft')
where name = 'Vertragsaenderung Genehmiger informieren';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Unterschriftenlauf starten')
where name = 'Vertragsaenderung Genehmiger prueft';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Unterschriftenlauf durchfuehren')
where name = 'Vertragsaenderung Unterschriftenlauf starten';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Unterschriebenes Dokument speichern')
where name = 'Vertragsaenderung Unterschriftenlauf durchfuehren';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Zusatz im System eintragen')
where name = 'Vertragsaenderung Unterschriebenes Dokument speichern';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Daten an LHR uebermitteln')
where name = 'Vertragsaenderung Zusatz im System eintragen';

update s_workflow_items
set successor = (select id from s_workflow_items where name = 'Vertragsaenderung Beteiligten informieren')
where name = 'Vertragsaenderung Daten an LHR uebermitteln';



-- predecessor workflow_items

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Daten vervollstaendigen')
where name = 'Vertragsaenderung Zusatzdokument erstellen';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Zusatzdokument erstellen')
where name = 'Vertragsaenderung zur Pruefung vorlegen';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung zur Pruefung vorlegen')
where name = 'Vertragsaenderung people informieren';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung people informieren')
where name = 'Vertragsaenderung people prueft';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung people prueft')
where name = 'Vertragsaenderung Lohnverrechnung informieren';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Lohnverrechnung informieren')
where name = 'Vertragsaenderung Lohnverrechnung prueft';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Lohnverrechnung prueft')
where name = 'Vertragsaenderung Genehmiger informieren';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Genehmiger informieren')
where name = 'Vertragsaenderung Genehmiger prueft';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Genehmiger prueft')
where name = 'Vertragsaenderung Unterschriftenlauf starten';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Unterschriftenlauf starten')
where name = 'Vertragsaenderung Unterschriftenlauf durchfuehren';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Unterschriftenlauf durchfuehren')
where name = 'Vertragsaenderung Unterschriebenes Dokument speichern';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Unterschriebenes Dokument speichern')
where name = 'Vertragsaenderung Zusatz im System eintragen';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Zusatz im System eintragen')
where name = 'Vertragsaenderung Daten an LHR uebermitteln';

update s_workflow_items
set predecessor = (select id from s_workflow_items where name = 'Vertragsaenderung Daten an LHR uebermitteln')
where name = 'Vertragsaenderung Beteiligten informieren';