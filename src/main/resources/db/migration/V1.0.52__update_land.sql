alter table land add column lhr_kz text;
alter table land_history add column lhr_kz text;

create or replace function land_audit() returns trigger
    language plpgsql
as
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, lhr_kz, telefonvorwahl, is_in_eu_eea_ch,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (OLD.id, OLD.land_name, OLD.land_code, OLD.elda_code, OLD.lhr_kz, OLD.telefonvorwahl, OLD.is_in_eu_eea_ch,
                OLD.created_on, OLD.created_by, OLD.changed_by,
                'D', now());
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, lhr_kz, telefonvorwahl, is_in_eu_eea_ch,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.lhr_kz, NEW.telefonvorwahl, NEW.is_in_eu_eea_ch,
                NEW.created_on, NEW.created_by, NEW.changed_by,
                'U', now());
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO land_history (land_id, land_name, land_code, elda_code, lhr_kz, telefonvorwahl, is_in_eu_eea_ch,
                                  created_on, created_by, changed_by,
                                  action, action_timestamp)
        VALUES (NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.lhr_kz, NEW.telefonvorwahl, NEW.is_in_eu_eea_ch, NEW.created_on, NEW.created_by,
                NULL,
                'I', now());
        RETURN NEW;
    END IF;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$;

alter function land_audit() owner to ibosng;


UPDATE land SET elda_code = 'AUT', land_code = 'AT', lhr_kz = 'A' WHERE land_name = 'Österreich';
UPDATE land SET elda_code = 'AFG', land_code = 'AF', lhr_kz = 'AFG' WHERE land_name = 'Afghanistan';
UPDATE land SET elda_code = 'AIA', land_code = 'AI', lhr_kz = 'AI' WHERE land_name = 'Anguilla';
UPDATE land SET elda_code = 'ALB', land_code = 'AL', lhr_kz = 'AL' WHERE land_name = 'Albanien';
UPDATE land SET elda_code = 'AND', land_code = 'AD', lhr_kz = 'AND' WHERE land_name = 'Andorra';
UPDATE land SET elda_code = 'AGO', land_code = 'AO', lhr_kz = 'ANG' WHERE land_name = 'Angola';
UPDATE land SET elda_code = 'ATG', land_code = 'AG', lhr_kz = 'ANT' WHERE land_name = 'Antigua und Barbuda';
UPDATE land SET elda_code = 'ATA', land_code = 'AQ', lhr_kz = 'AQ' WHERE land_name = 'Antarktis';
UPDATE land SET elda_code = 'ARM', land_code = 'AM', lhr_kz = 'ARM' WHERE land_name = 'Armenien';
UPDATE land SET elda_code = 'ASM', land_code = 'AS', lhr_kz = 'AS' WHERE land_name = 'Amerikanisch-Samoa';
UPDATE land SET elda_code = 'AUS', land_code = 'AU', lhr_kz = 'AUS' WHERE land_name = 'Australien';
UPDATE land SET elda_code = 'ABW', land_code = 'AW', lhr_kz = 'AW' WHERE land_name = 'Aruba';
UPDATE land SET elda_code = 'AZE', land_code = 'AZ', lhr_kz = 'AZ' WHERE land_name = 'Aserbaidschan';
UPDATE land SET elda_code = 'BEL', land_code = 'BE', lhr_kz = 'B' WHERE land_name = 'Belgien';
UPDATE land SET elda_code = 'BGD', land_code = 'BD', lhr_kz = 'BD' WHERE land_name = 'Bangladesch';
UPDATE land SET elda_code = 'BRB', land_code = 'BB', lhr_kz = 'BDS' WHERE land_name = 'Barbados';
UPDATE land SET elda_code = 'BFA', land_code = 'BF', lhr_kz = 'BF' WHERE land_name = 'Burkina Faso';
UPDATE land SET elda_code = 'BGR', land_code = 'BG', lhr_kz = 'BG' WHERE land_name = 'Bulgarien';
UPDATE land SET elda_code = 'BLZ', land_code = 'BZ', lhr_kz = 'BH' WHERE land_name = 'Belize';
UPDATE land SET elda_code = 'BIH', land_code = 'BA', lhr_kz = 'BIH' WHERE land_name = 'Bosnien & Herzegowina';
UPDATE land SET elda_code = 'BMU', land_code = 'BM', lhr_kz = 'BMU' WHERE land_name = 'Bermuda';
UPDATE land SET elda_code = 'BOL', land_code = 'BO', lhr_kz = 'BOL' WHERE land_name = 'Bolivien';
UPDATE land SET elda_code = 'BRA', land_code = 'BR', lhr_kz = 'BR' WHERE land_name = 'Brasilien';
UPDATE land SET elda_code = 'BHR', land_code = 'BH', lhr_kz = 'BRN' WHERE land_name = 'Bahrain';
UPDATE land SET elda_code = 'BRN', land_code = 'BN', lhr_kz = 'BRU' WHERE land_name = 'Brunei';
UPDATE land SET elda_code = 'BHS', land_code = 'BS', lhr_kz = 'BS' WHERE land_name = 'Bahamas';
UPDATE land SET elda_code = 'BTN', land_code = 'BT', lhr_kz = 'BTN' WHERE land_name = 'Bhutan';
UPDATE land SET elda_code = 'MMR', land_code = 'MM', lhr_kz = 'BUR' WHERE land_name = 'Myanmar';
UPDATE land SET elda_code = 'BLR', land_code = 'BY', lhr_kz = 'BY' WHERE land_name = 'Belarus';
UPDATE land SET elda_code = 'CUB', land_code = 'CU', lhr_kz = 'C' WHERE land_name = 'Kuba';
UPDATE land SET elda_code = 'CMR', land_code = 'CM', lhr_kz = 'CAM' WHERE land_name = 'Kamerun';
UPDATE land SET elda_code = 'CCK', land_code = 'CC', lhr_kz = 'CC' WHERE land_name = 'Kokosinseln';
UPDATE land SET elda_code = 'CAN', land_code = 'CA', lhr_kz = 'CDN' WHERE land_name = 'Kanada';
UPDATE land SET elda_code = 'CHE', land_code = 'CH', lhr_kz = 'CH' WHERE land_name = 'Schweiz';
UPDATE land SET elda_code = 'CIV', land_code = 'CI', lhr_kz = 'CI' WHERE land_name = 'Elfenbeinküste';
UPDATE land SET elda_code = 'COK', land_code = 'CK', lhr_kz = 'CK' WHERE land_name = 'Cookinseln';
UPDATE land SET elda_code = 'LKA', land_code = 'LK', lhr_kz = 'CL' WHERE land_name = 'Sri Lanka';
UPDATE land SET elda_code = 'COL', land_code = 'CO', lhr_kz = 'CO' WHERE land_name = 'Kolumbien';
UPDATE land SET elda_code = 'COM', land_code = 'KM', lhr_kz = 'COM' WHERE land_name = 'Komoren';
UPDATE land SET elda_code = 'CRI', land_code = 'CR', lhr_kz = 'CR' WHERE land_name = 'Costa Rica';
UPDATE land SET elda_code = 'CPV', land_code = 'CV', lhr_kz = 'CV' WHERE land_name = 'Kap Verde';
UPDATE land SET elda_code = 'CXR', land_code = 'CX', lhr_kz = 'CX' WHERE land_name = 'Weihnachtsinsel';
UPDATE land SET elda_code = 'CYP', land_code = 'CY', lhr_kz = 'CY' WHERE land_name = 'Zypern';
UPDATE land SET elda_code = 'CZE', land_code = 'CZ', lhr_kz = 'CZ' WHERE land_name = 'Tschechien';
UPDATE land SET elda_code = 'DEU', land_code = 'DE', lhr_kz = 'D' WHERE land_name = 'Deutschland';
UPDATE land SET elda_code = 'DJI', land_code = 'DJ', lhr_kz = 'DJI' WHERE land_name = 'Dschibuti';
UPDATE land SET elda_code = 'DNK', land_code = 'DK', lhr_kz = 'DK' WHERE land_name = 'Dänemark';
UPDATE land SET elda_code = 'DOM', land_code = 'DO', lhr_kz = 'DOM' WHERE land_name = 'Dominikanische Republik';
UPDATE land SET elda_code = 'BEN', land_code = 'BJ', lhr_kz = 'DY' WHERE land_name = 'Benin';
UPDATE land SET elda_code = 'DZA', land_code = 'DZ', lhr_kz = 'DZ' WHERE land_name = 'Algerien';
UPDATE land SET elda_code = 'ESP', land_code = 'ES', lhr_kz = 'E' WHERE land_name = 'Spanien';
UPDATE land SET elda_code = 'KEN', land_code = 'KE', lhr_kz = 'EAK' WHERE land_name = 'Kenia';
UPDATE land SET elda_code = 'TZA', land_code = 'TZ', lhr_kz = 'EAT' WHERE land_name = 'Tansania';
UPDATE land SET elda_code = 'UGA', land_code = 'UG', lhr_kz = 'EAU' WHERE land_name = 'Uganda';
UPDATE land SET elda_code = 'ECU', land_code = 'EC', lhr_kz = 'EC' WHERE land_name = 'Ecuador';
UPDATE land SET elda_code = 'ERI', land_code = 'ER', lhr_kz = 'ER' WHERE land_name = 'Eritrea';
UPDATE land SET elda_code = 'SLV', land_code = 'SV', lhr_kz = 'ES' WHERE land_name = 'El Salvador';
UPDATE land SET elda_code = 'EGY', land_code = 'EG', lhr_kz = 'ET' WHERE land_name = 'Ägypten';
UPDATE land SET elda_code = 'ETH', land_code = 'ET', lhr_kz = 'ETH' WHERE land_name = 'Äthiopien';
UPDATE land SET elda_code = 'EST', land_code = 'EE', lhr_kz = 'EW' WHERE land_name = 'Estland';
UPDATE land SET elda_code = 'FRA', land_code = 'FR', lhr_kz = 'F' WHERE land_name = 'Frankreich';
UPDATE land SET elda_code = 'FIN', land_code = 'FI', lhr_kz = 'FIN' WHERE land_name = 'Finnland';
UPDATE land SET elda_code = 'FJI', land_code = 'FJ', lhr_kz = 'FJI' WHERE land_name = 'Fidschi';
UPDATE land SET elda_code = 'LIE', land_code = 'LI', lhr_kz = 'FL' WHERE land_name = 'Liechtenstein';
UPDATE land SET elda_code = 'FLK', land_code = 'FK', lhr_kz = 'FLK' WHERE land_name = 'Falklandinseln';
UPDATE land SET elda_code = 'FRO', land_code = 'FO', lhr_kz = 'FR' WHERE land_name = 'Färöer';
UPDATE land SET elda_code = 'FSM', land_code = 'FM', lhr_kz = 'FSM' WHERE land_name = 'Mikronesien';
UPDATE land SET elda_code = 'GAB', land_code = 'GA', lhr_kz = 'GAB' WHERE land_name = 'Gabun';
UPDATE land SET elda_code = 'GBR', land_code = 'GB', lhr_kz = 'GB' WHERE land_name = 'Großbritannien';
UPDATE land SET elda_code = 'GIB', land_code = 'GI', lhr_kz = 'GBZ' WHERE land_name = 'Gibraltar';
UPDATE land SET elda_code = 'GTM', land_code = 'GT', lhr_kz = 'GCA' WHERE land_name = 'Guatemala';
UPDATE land SET elda_code = 'GEO', land_code = 'GE', lhr_kz = 'GE' WHERE land_name = 'Georgien (ohne Abchasien)';
UPDATE land SET elda_code = 'GUF', land_code = 'GF', lhr_kz = 'GF' WHERE land_name = 'Französisch-Guayana';
UPDATE land SET elda_code = 'GHA', land_code = 'GH', lhr_kz = 'GH' WHERE land_name = 'Ghana';
UPDATE land SET elda_code = 'GRL', land_code = 'GL', lhr_kz = 'GL' WHERE land_name = 'Grönland';
UPDATE land SET elda_code = 'GLP', land_code = 'GP', lhr_kz = 'GLP' WHERE land_name = 'Guadeloupe';
UPDATE land SET elda_code = 'GIN', land_code = 'GN', lhr_kz = 'GN' WHERE land_name = 'Guinea';
UPDATE land SET elda_code = 'GNB', land_code = 'GW', lhr_kz = 'GNB' WHERE land_name = 'Guinea-Bissau';
UPDATE land SET elda_code = 'GNQ', land_code = 'GQ', lhr_kz = 'GQ' WHERE land_name = 'Äquatorialguinea';
UPDATE land SET elda_code = 'GRC', land_code = 'GR', lhr_kz = 'GR' WHERE land_name = 'Griechenland';
UPDATE land SET elda_code = 'GUM', land_code = 'GU', lhr_kz = 'GU' WHERE land_name = 'Guam';
UPDATE land SET elda_code = 'GUY', land_code = 'GY', lhr_kz = 'GUY' WHERE land_name = 'Guyana';
UPDATE land SET elda_code = 'HUN', land_code = 'HU', lhr_kz = 'H' WHERE land_name = 'Ungarn';
UPDATE land SET elda_code = 'HND', land_code = 'HN', lhr_kz = 'HD' WHERE land_name = 'Honduras';
UPDATE land SET elda_code = 'HKG', land_code = 'HK', lhr_kz = 'HK' WHERE land_name = 'Hongkong';
UPDATE land SET elda_code = 'JOR', land_code = 'JO', lhr_kz = 'HKJ' WHERE land_name = 'Jordanien';
UPDATE land SET elda_code = 'HRV', land_code = 'HR', lhr_kz = 'HR' WHERE land_name = 'Kroatien';
UPDATE land SET elda_code = 'ITA', land_code = 'IT', lhr_kz = 'I' WHERE land_name = 'Italien';
UPDATE land SET elda_code = 'ISR', land_code = 'IL', lhr_kz = 'IL' WHERE land_name = 'Israel';
UPDATE land SET elda_code = 'IND', land_code = 'IN', lhr_kz = 'IND' WHERE land_name = 'Indien';
UPDATE land SET elda_code = 'IOT', land_code = 'IO', lhr_kz = 'IO' WHERE land_name = 'Br. Territ./Ind. Ozean';
UPDATE land SET elda_code = 'IRN', land_code = 'IR', lhr_kz = 'IR' WHERE land_name = 'Iran';
UPDATE land SET elda_code = 'IRL', land_code = 'IE', lhr_kz = 'IRL' WHERE land_name = 'Irland';
UPDATE land SET elda_code = 'IRQ', land_code = 'IQ', lhr_kz = 'IRQ' WHERE land_name = 'Irak';
UPDATE land SET elda_code = 'ISL', land_code = 'IS', lhr_kz = 'IS' WHERE land_name = 'Island';
UPDATE land SET elda_code = 'JPN', land_code = 'JP', lhr_kz = 'J' WHERE land_name = 'Japan';
UPDATE land SET elda_code = 'JAM', land_code = 'JM', lhr_kz = 'JA' WHERE land_name = 'Jamaika';
UPDATE land SET elda_code = 'KHM', land_code = 'KH', lhr_kz = 'K' WHERE land_name = 'Kambodscha';
UPDATE land SET elda_code = 'KIR', land_code = 'KI', lhr_kz = 'KIR' WHERE land_name = 'Kiribati';
UPDATE land SET elda_code = 'KNA', land_code = 'KN', lhr_kz = 'KN' WHERE land_name = 'St. Kitts und Nevis';
UPDATE land SET elda_code = 'KGZ', land_code = 'KG', lhr_kz = 'KS' WHERE land_name = 'Kirgisistan';
UPDATE land SET elda_code = 'KWT', land_code = 'KW', lhr_kz = 'KWT' WHERE land_name = 'Kuwait';
UPDATE land SET elda_code = 'CYM', land_code = 'KY', lhr_kz = 'KY' WHERE land_name = 'Kaimaninseln';
UPDATE land SET elda_code = 'KAZ', land_code = 'KZ', lhr_kz = 'KZ' WHERE land_name = 'Kasachstan';
UPDATE land SET elda_code = 'LUX', land_code = 'LU', lhr_kz = 'L' WHERE land_name = 'Luxemburg';
UPDATE land SET elda_code = 'LAO', land_code = 'LA', lhr_kz = 'LAO' WHERE land_name = 'Laos';
UPDATE land SET elda_code = 'LBY', land_code = 'LY', lhr_kz = 'LAR' WHERE land_name = 'Libyen';
UPDATE land SET elda_code = 'LBR', land_code = 'LR', lhr_kz = 'LB' WHERE land_name = 'Liberia';
UPDATE land SET elda_code = 'LSO', land_code = 'LS', lhr_kz = 'LS' WHERE land_name = 'Lesotho';
UPDATE land SET elda_code = 'LTU', land_code = 'LT', lhr_kz = 'LT' WHERE land_name = 'Litauen';
UPDATE land SET elda_code = 'LVA', land_code = 'LV', lhr_kz = 'LV' WHERE land_name = 'Lettland';
UPDATE land SET elda_code = 'MLT', land_code = 'MT', lhr_kz = 'M' WHERE land_name = 'Malta';
UPDATE land SET elda_code = 'MAR', land_code = 'MA', lhr_kz = 'MA' WHERE land_name = 'Marokko';
UPDATE land SET elda_code = 'MAC', land_code = 'MO', lhr_kz = 'MAC' WHERE land_name = 'Macau';
UPDATE land SET elda_code = 'MYS', land_code = 'MY', lhr_kz = 'MAL' WHERE land_name = 'Malaysia';
UPDATE land SET elda_code = 'MCO', land_code = 'MC', lhr_kz = 'MC' WHERE land_name = 'Monaco';
UPDATE land SET elda_code = 'MYT', land_code = 'YT', lhr_kz = 'ME' WHERE land_name = 'Mayotte';
UPDATE land SET elda_code = 'MEX', land_code = 'MX', lhr_kz = 'MEX' WHERE land_name = 'Mexiko';
UPDATE land SET elda_code = 'MHL', land_code = 'MH', lhr_kz = 'MH' WHERE land_name = 'Marshallinseln';
UPDATE land SET elda_code = 'MKD', land_code = 'MK', lhr_kz = 'MK' WHERE land_name = 'Nordmazedonien';
UPDATE land SET elda_code = 'MNE', land_code = 'ME', lhr_kz = 'MNE' WHERE land_name = 'Montenegro';
UPDATE land SET elda_code = 'MNG', land_code = 'MN', lhr_kz = 'MNL' WHERE land_name = 'Mongolei';
UPDATE land SET elda_code = 'MNP', land_code = 'MP', lhr_kz = 'MNP' WHERE land_name = 'Marianen (zu USA)';
UPDATE land SET elda_code = 'MOZ', land_code = 'MZ', lhr_kz = 'MOC' WHERE land_name = 'Mosambik';
UPDATE land SET elda_code = 'MUS', land_code = 'MU', lhr_kz = 'MS' WHERE land_name = 'Mauritius';
UPDATE land SET elda_code = 'MSR', land_code = 'MS', lhr_kz = 'MSR' WHERE land_name = 'Montserrat';
UPDATE land SET elda_code = 'MTQ', land_code = 'MQ', lhr_kz = 'MTQ' WHERE land_name = 'Martinique';
UPDATE land SET elda_code = 'MDV', land_code = 'MV', lhr_kz = 'MV' WHERE land_name = 'Malediven';
UPDATE land SET elda_code = 'MWI', land_code = 'MW', lhr_kz = 'MW' WHERE land_name = 'Malawi';
UPDATE land SET elda_code = 'NOR', land_code = 'NO', lhr_kz = 'N' WHERE land_name = 'Norwegen';
UPDATE land SET elda_code = 'ANT', land_code = 'AN', lhr_kz = 'NA' WHERE land_name = 'Niederländische Antillen';
UPDATE land SET elda_code = 'NAM', land_code = 'NA', lhr_kz = 'NAM' WHERE land_name = 'Namibia';
UPDATE land SET elda_code = 'NCL', land_code = 'NC', lhr_kz = 'NCL' WHERE land_name = 'Neukaledonien';
UPDATE land SET elda_code = 'NPL', land_code = 'NP', lhr_kz = 'NEP' WHERE land_name = 'Nepal';
UPDATE land SET elda_code = 'NFK', land_code = 'NF', lhr_kz = 'NF' WHERE land_name = 'Norfolkinsel';
UPDATE land SET elda_code = 'NIC', land_code = 'NI', lhr_kz = 'NIC' WHERE land_name = 'Nicaragua';
UPDATE land SET elda_code = 'NLD', land_code = 'NL', lhr_kz = 'NL' WHERE land_name = 'Niederlande';
UPDATE land SET elda_code = 'NIU', land_code = 'NU', lhr_kz = 'NU' WHERE land_name = 'Niue';
UPDATE land SET elda_code = 'NZL', land_code = 'NZ', lhr_kz = 'NZ' WHERE land_name = 'Neuseeland';
UPDATE land SET elda_code = 'OMN', land_code = 'OM', lhr_kz = 'OM' WHERE land_name = 'Oman';
UPDATE land SET elda_code = 'PRT', land_code = 'PT', lhr_kz = 'P' WHERE land_name = 'Portugal';
UPDATE land SET elda_code = 'PAN', land_code = 'PA', lhr_kz = 'PA' WHERE land_name = 'Panama';
UPDATE land SET elda_code = 'PSE', land_code = 'PS', lhr_kz = 'PAL' WHERE land_name = 'Palästina';
UPDATE land SET elda_code = 'PER', land_code = 'PE', lhr_kz = 'PE' WHERE land_name = 'Peru';
UPDATE land SET elda_code = 'PYF', land_code = 'PF', lhr_kz = 'PF' WHERE land_name = 'Französisch-Polynesien';
UPDATE land SET elda_code = 'PAK', land_code = 'PK', lhr_kz = 'PK' WHERE land_name = 'Pakistan';
UPDATE land SET elda_code = 'POL', land_code = 'PL', lhr_kz = 'PL' WHERE land_name = 'Polen';
UPDATE land SET elda_code = 'SPM', land_code = 'PM', lhr_kz = 'PM' WHERE land_name = 'St.Pierre';
UPDATE land SET elda_code = 'PCN', land_code = 'PN', lhr_kz = 'PN' WHERE land_name = 'Pitcairninseln';
UPDATE land SET elda_code = 'PNG', land_code = 'PG', lhr_kz = 'PNG' WHERE land_name = 'Papua-Neuguinea';
UPDATE land SET elda_code = 'PRI', land_code = 'PR', lhr_kz = 'PRI' WHERE land_name = 'Puerto Rico';
UPDATE land SET elda_code = 'PRK', land_code = 'KP', lhr_kz = 'PRK' WHERE land_name = 'Nordkorea';
UPDATE land SET elda_code = 'PLW', land_code = 'PW', lhr_kz = 'PW' WHERE land_name = 'Palau';
UPDATE land SET elda_code = 'PRY', land_code = 'PY', lhr_kz = 'PY' WHERE land_name = 'Paraguay';
UPDATE land SET elda_code = 'QAT', land_code = 'QA', lhr_kz = 'Q' WHERE land_name = 'Katar';
UPDATE land SET elda_code = 'ARG', land_code = 'AR', lhr_kz = 'RA' WHERE land_name = 'Argentinien';
UPDATE land SET elda_code = 'CAF', land_code = 'CF', lhr_kz = 'RCA' WHERE land_name = 'Zentralafrikanische Republik';
UPDATE land SET elda_code = 'COG', land_code = 'CG', lhr_kz = 'RCB' WHERE land_name = 'Kongo (Republik)';
UPDATE land SET elda_code = 'CHL', land_code = 'CL', lhr_kz = 'RCH' WHERE land_name = 'Chile';
UPDATE land SET elda_code = 'REU', land_code = 'RE', lhr_kz = 'RE' WHERE land_name = 'Réunion';
UPDATE land SET elda_code = 'HTI', land_code = 'HT', lhr_kz = 'RH' WHERE land_name = 'Haiti';
UPDATE land SET elda_code = 'IDN', land_code = 'ID', lhr_kz = 'RI' WHERE land_name = 'Indonesien';
UPDATE land SET elda_code = 'MRT', land_code = 'MR', lhr_kz = 'RIM' WHERE land_name = 'Mauretanien';
UPDATE land SET elda_code = 'LBN', land_code = 'LB', lhr_kz = 'RL' WHERE land_name = 'Libanon';
UPDATE land SET elda_code = 'MDG', land_code = 'MG', lhr_kz = 'RM' WHERE land_name = 'Madagaskar';
UPDATE land SET elda_code = 'MLI', land_code = 'ML', lhr_kz = 'RMM' WHERE land_name = 'Mali';
UPDATE land SET elda_code = 'NER', land_code = 'NE', lhr_kz = 'RN' WHERE land_name = 'Niger';
UPDATE land SET elda_code = 'ROM', land_code = 'RO', lhr_kz = 'RO' WHERE land_name = 'Rumänien';
UPDATE land SET elda_code = 'KOR', land_code = 'KR', lhr_kz = 'ROK' WHERE land_name = 'Südkorea';
UPDATE land SET elda_code = 'URY', land_code = 'UY', lhr_kz = 'ROU' WHERE land_name = 'Uruguay';
UPDATE land SET elda_code = 'PHL', land_code = 'PH', lhr_kz = 'RP' WHERE land_name = 'Philippinen';
UPDATE land SET elda_code = 'SMR', land_code = 'SM', lhr_kz = 'RSM' WHERE land_name = 'San Marino';
UPDATE land SET elda_code = 'BDI', land_code = 'BI', lhr_kz = 'RU' WHERE land_name = 'Burundi';
UPDATE land SET elda_code = 'RUS', land_code = 'RU', lhr_kz = 'RUS' WHERE land_name = 'Russland';
UPDATE land SET elda_code = 'RWA', land_code = 'RW', lhr_kz = 'RWA' WHERE land_name = 'Ruanda';
UPDATE land SET elda_code = 'SWE', land_code = 'SE', lhr_kz = 'S' WHERE land_name = 'Schweden';
UPDATE land SET elda_code = 'SAU', land_code = 'SA', lhr_kz = 'SA' WHERE land_name = 'Saudi-Arabien';
UPDATE land SET elda_code = 'SWZ', land_code = 'SZ', lhr_kz = 'SD' WHERE land_name = 'Swasiland';
UPDATE land SET elda_code = 'SGP', land_code = 'SG', lhr_kz = 'SGP' WHERE land_name = 'Singapur';
UPDATE land SET elda_code = 'SHN', land_code = 'SH', lhr_kz = 'SH' WHERE land_name = 'St. Helena';
UPDATE land SET elda_code = 'SJM', land_code = 'SJ', lhr_kz = 'SJ' WHERE land_name = 'Svalbard';
UPDATE land SET elda_code = 'SVK', land_code = 'SK', lhr_kz = 'SK' WHERE land_name = 'Slowakei';
UPDATE land SET elda_code = 'SLB', land_code = 'SB', lhr_kz = 'SLB' WHERE land_name = 'Salomonen';
UPDATE land SET elda_code = 'SVN', land_code = 'SI', lhr_kz = 'SLO' WHERE land_name = 'Slowenien';
UPDATE land SET elda_code = 'SUR', land_code = 'SR', lhr_kz = 'SME' WHERE land_name = 'Suriname';
UPDATE land SET elda_code = 'SEN', land_code = 'SN', lhr_kz = 'SN' WHERE land_name = 'Senegal';
UPDATE land SET elda_code = 'SOM', land_code = 'SO', lhr_kz = 'SO' WHERE land_name = 'Somalia';
UPDATE land SET elda_code = 'SRB', land_code = 'RS', lhr_kz = 'SRB' WHERE land_name = 'Serbien';
UPDATE land SET elda_code = 'SSD', land_code = 'SS', lhr_kz = 'SS' WHERE land_name = 'Südsudan';
UPDATE land SET elda_code = 'SDN', land_code = 'SD', lhr_kz = 'SUD' WHERE land_name = 'Sudan';
UPDATE land SET elda_code = 'SYC', land_code = 'SC', lhr_kz = 'SY' WHERE land_name = 'Seychellen';
UPDATE land SET elda_code = 'SYR', land_code = 'SY', lhr_kz = 'SYR' WHERE land_name = 'Syrien';
UPDATE land SET elda_code = 'THA', land_code = 'TH', lhr_kz = 'T' WHERE land_name = 'Thailand';
UPDATE land SET elda_code = 'TWN', land_code = 'TW', lhr_kz = 'TA' WHERE land_name = 'Taiwan (Republik China)';
UPDATE land SET elda_code = 'TCA', land_code = 'TC', lhr_kz = 'TC' WHERE land_name = 'Turks- und Caicosinseln';
UPDATE land SET elda_code = 'TCD', land_code = 'TD', lhr_kz = 'TCH' WHERE land_name = 'Tschad';
UPDATE land SET elda_code = 'TGO', land_code = 'TG', lhr_kz = 'TG' WHERE land_name = 'Togo';
UPDATE land SET elda_code = 'TJK', land_code = 'TJ', lhr_kz = 'TJ' WHERE land_name = 'Tadschikistan';
UPDATE land SET elda_code = 'TKL', land_code = 'TK', lhr_kz = 'TK' WHERE land_name = 'Tokelau';
UPDATE land SET elda_code = 'TKM', land_code = 'TM', lhr_kz = 'TM' WHERE land_name = 'Turkmenistan';
UPDATE land SET elda_code = 'TUN', land_code = 'TN', lhr_kz = 'TN' WHERE land_name = 'Tunesien';
UPDATE land SET elda_code = 'TON', land_code = 'TO', lhr_kz = 'TO' WHERE land_name = 'Tonga';
UPDATE land SET elda_code = 'TLS', land_code = 'TL', lhr_kz = 'TP' WHERE land_name = 'Osttimor';
UPDATE land SET elda_code = 'TUR', land_code = 'TR', lhr_kz = 'TR' WHERE land_name = 'Türkei';
UPDATE land SET elda_code = 'TTO', land_code = 'TT', lhr_kz = 'TT' WHERE land_name = 'Trinidad und Tobago';
UPDATE land SET elda_code = 'TUV', land_code = 'TV', lhr_kz = 'TUV' WHERE land_name = 'Tuvalu';
UPDATE land SET elda_code = 'UKR', land_code = 'UA', lhr_kz = 'UA' WHERE land_name = 'Ukraine';
UPDATE land SET elda_code = 'ARE', land_code = 'AE', lhr_kz = 'UAE' WHERE land_name = 'Vereinigte Arabische Emirate';
UPDATE land SET elda_code = 'USA', land_code = 'US', lhr_kz = 'USA' WHERE land_name = 'USA';
UPDATE land SET elda_code = 'UZB', land_code = 'UZ', lhr_kz = 'UZ' WHERE land_name = 'Usbekistan';
UPDATE land SET elda_code = 'VAT', land_code = 'VA', lhr_kz = 'V' WHERE land_name = 'Vatikan';
UPDATE land SET elda_code = 'VGB', land_code = 'VG', lhr_kz = 'VG' WHERE land_name = 'Br. Jungferninseln';
UPDATE land SET elda_code = 'VIR', land_code = 'VI', lhr_kz = 'VI' WHERE land_name = 'Amerik. Jungferninseln';
UPDATE land SET elda_code = 'VNM', land_code = 'VN', lhr_kz = 'VN' WHERE land_name = 'Vietnam';
UPDATE land SET elda_code = 'CHN', land_code = 'CN', lhr_kz = 'VRC' WHERE land_name = 'China';
UPDATE land SET elda_code = 'VUT', land_code = 'VU', lhr_kz = 'VU' WHERE land_name = 'Vanuatu';
UPDATE land SET elda_code = 'GMB', land_code = 'GM', lhr_kz = 'WAG' WHERE land_name = 'Gambia';
UPDATE land SET elda_code = 'NRU', land_code = 'NR', lhr_kz = 'WAI' WHERE land_name = 'Nauru';
UPDATE land SET elda_code = 'SLE', land_code = 'SL', lhr_kz = 'WAL' WHERE land_name = 'Sierra Leone';
UPDATE land SET elda_code = 'NGA', land_code = 'NG', lhr_kz = 'WAN' WHERE land_name = 'Nigeria';
UPDATE land SET elda_code = 'DMA', land_code = 'DM', lhr_kz = 'WD' WHERE land_name = 'Dominica';
UPDATE land SET elda_code = 'WLF', land_code = 'WF', lhr_kz = 'WF' WHERE land_name = 'Wallis und Futuna';
UPDATE land SET elda_code = 'GRD', land_code = 'GD', lhr_kz = 'WG' WHERE land_name = 'Grenada';
UPDATE land SET elda_code = 'LCA', land_code = 'LC', lhr_kz = 'WL' WHERE land_name = 'St. Lucia';
UPDATE land SET elda_code = 'WSM', land_code = 'WS', lhr_kz = 'WS' WHERE land_name = 'Samoa';
UPDATE land SET elda_code = 'VCT', land_code = 'VC', lhr_kz = 'WV' WHERE land_name = 'St. Vincent und die Grenadinen';
UPDATE land SET elda_code = 'YEM', land_code = 'YE', lhr_kz = 'Y' WHERE land_name = 'Jemen';
UPDATE land SET elda_code = 'VEN', land_code = 'VE', lhr_kz = 'YV' WHERE land_name = 'Venezuela';
UPDATE land SET elda_code = 'ZMB', land_code = 'ZM', lhr_kz = 'Z' WHERE land_name = 'Sambia';
UPDATE land SET elda_code = 'ZAF', land_code = 'ZA', lhr_kz = 'ZA' WHERE land_name = 'Südafrika';
UPDATE land SET elda_code = 'COD', land_code = 'CD', lhr_kz = 'ZRE' WHERE land_name = 'Kongo (Demokratische Republik)';
UPDATE land SET elda_code = 'ZWE', land_code = 'ZW', lhr_kz = 'ZW' WHERE land_name = 'Simbabwe';
