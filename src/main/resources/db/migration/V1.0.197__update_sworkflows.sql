--MA ONBOARDING
update s_workflows
set successor = (select id from s_workflows where name = 'Inform the Lohnverrechnung for new Mitarbeiter')
where name = 'Collect Data for New Mitarbeiter';

update s_workflows
set successor = (select id from s_workflows where name = 'Start the signing process of the contract for new Mitarbeiter')
where name = 'Inform the Lohnverrechnung for new Mitarbeiter';

update s_workflows
set successor = (select id from s_workflows where name = 'Send data to LHR for the new MA')
where name = 'Start the signing process of the contract for new Mitarbeiter';

update s_workflows
set successor = (select id from s_workflows where name = 'Create new user in AD and DB')
where name = 'Send data to LHR for the new MA';


update s_workflows
set predecessor = (select id from s_workflows where name = 'Collect Data for New Mitarbeiter')
where name = 'Inform the Lohnverrechnung for new Mitarbeiter';

update s_workflows
set predecessor = (select id from s_workflows where name = 'Inform the Lohnverrechnung for new Mitarbeiter')
where name = 'Start the signing process of the contract for new Mitarbeiter';

update s_workflows
set predecessor = (select id from s_workflows where name = 'Start the signing process of the contract for new Mitarbeiter')
where name = 'Send data to LHR for the new MA';

update s_workflows
set predecessor = (select id from s_workflows where name = 'Send data to LHR for the new MA')
where name = 'Create new user in AD and DB';

--TN ONBOARDING
update s_workflows
set successor = (select id from s_workflows where name = 'Inform the Lohnverrechnung for TN onboarding')
where name = 'Collect Data for TN onboarding';

update s_workflows
set successor = (select id from s_workflows where name = 'Send data to LHR for TN onboarding')
where name = 'Inform the Lohnverrechnung for TN onboarding';

update s_workflows
set successor = (select id from s_workflows where name = 'Inform the Stakeholder for TN onboarding')
where name = 'Send data to LHR for TN onboarding';



update s_workflows
set predecessor = (select id from s_workflows where name = 'Collect Data for TN onboarding')
where name = 'Inform the Lohnverrechnung for TN onboarding';

update s_workflows
set predecessor = (select id from s_workflows where name = 'Inform the Lohnverrechnung for TN onboarding')
where name = 'Send data to LHR for TN onboarding';

update s_workflows
set predecessor = (select id from s_workflows where name = 'Send data to LHR for TN onboarding')
where name = 'Inform the Stakeholder for TN onboarding';
