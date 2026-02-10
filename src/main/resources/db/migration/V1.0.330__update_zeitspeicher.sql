update zeitspeicher set name = 'UeSt 50% Zuschlag (LSt-begünstigt)', comment = 'Auszahlung und/oder Umbuchung auf Zeitguthaben erfolgt jeweils mit 50% Zuschlag.' where name = 'UeSt 50% frei';
update zeitspeicher set name = 'UeSt 50% Zuschlag (LSt-pflichtig)', comment = 'Auszahlung und/oder Umbuchung auf Zeitguthaben erfolgt jeweils mit 50% Zuschlag.' where name = 'UeSt 50% pflichtig';
update zeitspeicher set name = 'UeSt 100% Zuschlag (LSt-begünstigt)', comment = 'Auszahlung und/oder Umbuchung auf Zeitguthaben erfolgt jeweils mit 100% Zuschlag.' where name = 'UeSt 100% frei';
update zeitspeicher set name = 'UeSt 100% Zuschlag (LSt-pflichtig)', comment = 'Auszahlung und/oder Umbuchung auf Zeitguthaben erfolgt jeweils mit 100% Zuschlag.' where name = 'UeSt 100% pflichtig';
update zeitspeicher set name = 'UeSt 25% Zuschlag (LSt-pflichtig)', comment = 'Auszahlung und/oder Umbuchung auf Zeitguthaben erfolgt jeweils mit 25% Zuschlag.' where name = 'UeSt 25% pflichtig';
