INSERT INTO public.file_import_headers(
	file_type, active_headers, inactive_headers ,"version", created_by)
	VALUES (4,'ID,Nachname,Vorname,SVnr,Gerburtsdatum',
	    'Ursprungsland,Gerburtsort,Erläuterung ziel,Vermittelbar ab,Notiz','1',current_user);
