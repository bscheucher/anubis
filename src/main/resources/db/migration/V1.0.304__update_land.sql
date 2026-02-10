ALTER TABLE land ADD COLUMN pal_kz TEXT;
ALTER TABLE land_history ADD COLUMN pal_kz TEXT;

CREATE OR REPLACE FUNCTION land_audit() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO land_history (
            land_id, land_name, land_code, elda_code, lhr_kz, pal_kz, telefonvorwahl, is_in_eu_eea_ch,
            created_on, created_by, changed_by,
            action, action_timestamp
        )
        VALUES (
            OLD.id, OLD.land_name, OLD.land_code, OLD.elda_code, OLD.lhr_kz, OLD.pal_kz, OLD.telefonvorwahl, OLD.is_in_eu_eea_ch,
            OLD.created_on, OLD.created_by, OLD.changed_by,
            'D', now()
        );
RETURN OLD;

ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO land_history (
            land_id, land_name, land_code, elda_code, lhr_kz, pal_kz, telefonvorwahl, is_in_eu_eea_ch,
            created_on, created_by, changed_by,
            action, action_timestamp
        )
        VALUES (
            NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.lhr_kz, NEW.pal_kz, NEW.telefonvorwahl, NEW.is_in_eu_eea_ch,
            NEW.created_on, NEW.created_by, NEW.changed_by,
            'U', now()
        );
RETURN NEW;

ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO land_history (
            land_id, land_name, land_code, elda_code, lhr_kz, pal_kz, telefonvorwahl, is_in_eu_eea_ch,
            created_on, created_by, changed_by,
            action, action_timestamp
        )
        VALUES (
            NEW.id, NEW.land_name, NEW.land_code, NEW.elda_code, NEW.lhr_kz, NEW.pal_kz, NEW.telefonvorwahl, NEW.is_in_eu_eea_ch,
            NEW.created_on, NEW.created_by, NULL,
            'I', now()
        );
RETURN NEW;
END IF;

RETURN NULL;
END;
$$;


ALTER FUNCTION land_audit() OWNER TO ibosng;


UPDATE land SET pal_kz = 'AFG' WHERE land_name = 'Afghanistan';
UPDATE land SET pal_kz = 'ET' WHERE land_name = 'Ägypten';
UPDATE land SET pal_kz = 'AL' WHERE land_name = 'Albanien';
UPDATE land SET pal_kz = 'DZ' WHERE land_name = 'Algerien';
UPDATE land SET pal_kz = 'ASM' WHERE land_name = 'Amerikanisch-Samoa';
UPDATE land SET pal_kz = 'VIR' WHERE land_name = 'Amerikanisch Jungferninseln';
UPDATE land SET pal_kz = 'AND' WHERE land_name = 'Andorra';
UPDATE land SET pal_kz = 'AGO' WHERE land_name = 'Angola';
UPDATE land SET pal_kz = 'AIA' WHERE land_name = 'Anguilla';
UPDATE land SET pal_kz = 'ATA' WHERE land_name = 'Antarktis';
UPDATE land SET pal_kz = 'ATG' WHERE land_name = 'Antigua und Barbuda';
UPDATE land SET pal_kz = 'GNQ' WHERE land_name = 'Äquatorialguinea';
UPDATE land SET pal_kz = 'RA' WHERE land_name = 'Argentinien';
UPDATE land SET pal_kz = 'AM' WHERE land_name = 'Armenien';
UPDATE land SET pal_kz = 'ABW' WHERE land_name = 'Aruba';
UPDATE land SET pal_kz = 'AZ' WHERE land_name = 'Aserbaidschan';
UPDATE land SET pal_kz = 'ETH' WHERE land_name = 'Äthiopien';
UPDATE land SET pal_kz = 'AUS' WHERE land_name = 'Australien';
UPDATE land SET pal_kz = 'BS' WHERE land_name = 'Bahamas';
UPDATE land SET pal_kz = 'BRN' WHERE land_name = 'Bahrain';
UPDATE land SET pal_kz = 'BD' WHERE land_name = 'Bangladesch';
UPDATE land SET pal_kz = 'BDS' WHERE land_name = 'Barbados';
UPDATE land SET pal_kz = 'BY' WHERE land_name = 'Belarus';
UPDATE land SET pal_kz = 'B' WHERE land_name = 'Belgien';
UPDATE land SET pal_kz = 'BH' WHERE land_name = 'Belize';
UPDATE land SET pal_kz = 'DY' WHERE land_name = 'Benin';
UPDATE land SET pal_kz = 'BMU' WHERE land_name = 'Bermuda';
UPDATE land SET pal_kz = 'BTN' WHERE land_name = 'Bhutan';
UPDATE land SET pal_kz = 'BOL' WHERE land_name = 'Bolivien';
UPDATE land SET pal_kz = 'BIH' WHERE land_name = 'Bosnien & Herzegowina';
UPDATE land SET pal_kz = 'BW' WHERE land_name = 'Botswana';
UPDATE land SET pal_kz = 'BR' WHERE land_name = 'Brasilien';
UPDATE land SET pal_kz = 'BVI' WHERE land_name = 'Br. Jungferninseln';
UPDATE land SET pal_kz = 'IOT' WHERE land_name = 'Br. Territ./Ind. Ozean';
UPDATE land SET pal_kz = 'BRU' WHERE land_name = 'Brunei';
UPDATE land SET pal_kz = 'BG' WHERE land_name = 'Bulgarien';
UPDATE land SET pal_kz = 'BF' WHERE land_name = 'Burkina Faso';
UPDATE land SET pal_kz = 'RU' WHERE land_name = 'Burundi';
UPDATE land SET pal_kz = 'RCH' WHERE land_name = 'Chile';
UPDATE land SET pal_kz = 'RC' WHERE land_name = 'China';
UPDATE land SET pal_kz = 'COK' WHERE land_name = 'Cookinseln';
UPDATE land SET pal_kz = 'CR' WHERE land_name = 'Costa Rica';
UPDATE land SET pal_kz = 'DK' WHERE land_name = 'Dänemark';
UPDATE land SET pal_kz = 'D' WHERE land_name = 'Deutschland';
UPDATE land SET pal_kz = 'WD' WHERE land_name = 'Dominica';
UPDATE land SET pal_kz = 'DOM' WHERE land_name = 'Dominikanische Republik';
UPDATE land SET pal_kz = 'DJI' WHERE land_name = 'Dschibuti';
UPDATE land SET pal_kz = 'EC' WHERE land_name = 'Ecuador';
UPDATE land SET pal_kz = 'CIV' WHERE land_name = 'Elfenbeinküste';
UPDATE land SET pal_kz = 'ES' WHERE land_name = 'El Salvador';
UPDATE land SET pal_kz = 'ER' WHERE land_name = 'Eritrea';
UPDATE land SET pal_kz = 'EST' WHERE land_name = 'Estland';
UPDATE land SET pal_kz = 'FLK' WHERE land_name = 'Falklandinseln';
UPDATE land SET pal_kz = 'FRO' WHERE land_name = 'Färöer';
UPDATE land SET pal_kz = 'FJI' WHERE land_name = 'Fidschi';
UPDATE land SET pal_kz = 'FIN' WHERE land_name = 'Finnland';
UPDATE land SET pal_kz = 'F' WHERE land_name = 'Frankreich';
UPDATE land SET pal_kz = 'GUF' WHERE land_name = 'Französisch-Guayana';
UPDATE land SET pal_kz = 'PYF' WHERE land_name = 'Französisch-Polynesien';
UPDATE land SET pal_kz = 'G' WHERE land_name = 'Gabun';
UPDATE land SET pal_kz = 'WAG' WHERE land_name = 'Gambia';
UPDATE land SET pal_kz = 'GE' WHERE land_name = 'Georgien (ohne Abchasien)';
UPDATE land SET pal_kz = 'GH' WHERE land_name = 'Ghana';
UPDATE land SET pal_kz = 'GIB' WHERE land_name = 'Gibraltar';
UPDATE land SET pal_kz = 'WG' WHERE land_name = 'Grenada';
UPDATE land SET pal_kz = 'GR' WHERE land_name = 'Griechenland';
UPDATE land SET pal_kz = 'GRL' WHERE land_name = 'Grönland';
UPDATE land SET pal_kz = 'GB' WHERE land_name = 'Großbritannien';
UPDATE land SET pal_kz = 'GLP' WHERE land_name = 'Guadeloupe';
UPDATE land SET pal_kz = 'GUM' WHERE land_name = 'Guam';
UPDATE land SET pal_kz = 'GCA' WHERE land_name = 'Guatemala';
UPDATE land SET pal_kz = 'RG' WHERE land_name = 'Guinea';
UPDATE land SET pal_kz = 'GNB' WHERE land_name = 'Guinea-Bissau';
UPDATE land SET pal_kz = 'GUY' WHERE land_name = 'Guyana';
UPDATE land SET pal_kz = 'RH' WHERE land_name = 'Haiti';
UPDATE land SET pal_kz = 'HND' WHERE land_name = 'Honduras';
UPDATE land SET pal_kz = 'HKG' WHERE land_name = 'Hongkong';
UPDATE land SET pal_kz = 'IND' WHERE land_name = 'Indien';
UPDATE land SET pal_kz = 'RI' WHERE land_name = 'Indonesien';
UPDATE land SET pal_kz = 'IRQ' WHERE land_name = 'Irak';
UPDATE land SET pal_kz = 'IR' WHERE land_name = 'Iran';
UPDATE land SET pal_kz = 'IRL' WHERE land_name = 'Irland';
UPDATE land SET pal_kz = 'IS' WHERE land_name = 'Island';
UPDATE land SET pal_kz = 'IL' WHERE land_name = 'Israel';
UPDATE land SET pal_kz = 'I' WHERE land_name = 'Italien';
UPDATE land SET pal_kz = 'JA' WHERE land_name = 'Jamaika';
UPDATE land SET pal_kz = 'J' WHERE land_name = 'Japan';
UPDATE land SET pal_kz = 'YAR' WHERE land_name = 'Jemen';
UPDATE land SET pal_kz = 'HKJ' WHERE land_name = 'Jordanien';
UPDATE land SET pal_kz = 'RCA' WHERE land_name = 'Kaimaninseln';
UPDATE land SET pal_kz = 'K' WHERE land_name = 'Kambodscha';
UPDATE land SET pal_kz = 'CMR' WHERE land_name = 'Kamerun';
UPDATE land SET pal_kz = 'CDN' WHERE land_name = 'Kanada';
UPDATE land SET pal_kz = 'CPV' WHERE land_name = 'Kap Verde';
UPDATE land SET pal_kz = 'KZ' WHERE land_name = 'Kasachstan';
UPDATE land SET pal_kz = 'Q' WHERE land_name = 'Katar';
UPDATE land SET pal_kz = 'EAK' WHERE land_name = 'Kenia';
UPDATE land SET pal_kz = 'KS' WHERE land_name = 'Kirgisistan';
UPDATE land SET pal_kz = 'KIR' WHERE land_name = 'Kiribati';
UPDATE land SET pal_kz = 'CCK' WHERE land_name = 'Kokosinseln';
UPDATE land SET pal_kz = 'CO' WHERE land_name = 'Kolumbien';
UPDATE land SET pal_kz = 'COM' WHERE land_name = 'Komoren';
UPDATE land SET pal_kz = 'ZRE' WHERE land_name = 'Kongo (Demokratische Republik)';
UPDATE land SET pal_kz = 'KGO' WHERE land_name = 'Kongo (Republik)';
UPDATE land SET pal_kz = 'KSV' WHERE land_name = 'Kosovo';
UPDATE land SET pal_kz = 'HR' WHERE land_name = 'Kroatien';
UPDATE land SET pal_kz = 'CUB' WHERE land_name = 'Kuba';
UPDATE land SET pal_kz = 'KWT' WHERE land_name = 'Kuwait';
UPDATE land SET pal_kz = 'LAO' WHERE land_name = 'Laos';
UPDATE land SET pal_kz = 'LS' WHERE land_name = 'Lesotho';
UPDATE land SET pal_kz = 'LV' WHERE land_name = 'Lettland';
UPDATE land SET pal_kz = 'RL' WHERE land_name = 'Libanon';
UPDATE land SET pal_kz = 'LB' WHERE land_name = 'Liberia';
UPDATE land SET pal_kz = 'LAR' WHERE land_name = 'Libyen';
UPDATE land SET pal_kz = 'FL' WHERE land_name = 'Liechtenstein';
UPDATE land SET pal_kz = 'LT' WHERE land_name = 'Litauen';
UPDATE land SET pal_kz = 'L' WHERE land_name = 'Luxemburg';
UPDATE land SET pal_kz = 'MAC' WHERE land_name = 'Macau';
UPDATE land SET pal_kz = 'RM' WHERE land_name = 'Madagaskar';
UPDATE land SET pal_kz = 'MW' WHERE land_name = 'Malawi';
UPDATE land SET pal_kz = 'MAL' WHERE land_name = 'Malaysia';
UPDATE land SET pal_kz = 'MDV' WHERE land_name = 'Malediven';
UPDATE land SET pal_kz = 'RMM' WHERE land_name = 'Mali';
UPDATE land SET pal_kz = 'MLT' WHERE land_name = 'Malta';
UPDATE land SET pal_kz = 'MNP' WHERE land_name = 'Marianen (zu USA)';
UPDATE land SET pal_kz = 'MA' WHERE land_name = 'Marokko';
UPDATE land SET pal_kz = 'MHL' WHERE land_name = 'Marshallinseln';
UPDATE land SET pal_kz = 'MTQ' WHERE land_name = 'Martinique';
UPDATE land SET pal_kz = 'RIM' WHERE land_name = 'Mauretanien';
UPDATE land SET pal_kz = 'MS' WHERE land_name = 'Mauritius';
UPDATE land SET pal_kz = 'MYT' WHERE land_name = 'Mayotte';
UPDATE land SET pal_kz = 'MEX' WHERE land_name = 'Mexiko';
UPDATE land SET pal_kz = 'FSM' WHERE land_name = 'Mikronesien';
UPDATE land SET pal_kz = 'MD' WHERE land_name = 'Moldau';
UPDATE land SET pal_kz = 'MC' WHERE land_name = 'Monaco';
UPDATE land SET pal_kz = 'MGL' WHERE land_name = 'Mongolei';
UPDATE land SET pal_kz = 'MNE' WHERE land_name = 'Montenegro';
UPDATE land SET pal_kz = 'MSR' WHERE land_name = 'Montserrat';
UPDATE land SET pal_kz = 'MOC' WHERE land_name = 'Mosambik';
UPDATE land SET pal_kz = 'BUR' WHERE land_name = 'Myanmar';
UPDATE land SET pal_kz = 'NAM' WHERE land_name = 'Namibia';
UPDATE land SET pal_kz = 'NAU' WHERE land_name = 'Nauru';
UPDATE land SET pal_kz = 'NEP' WHERE land_name = 'Nepal';
UPDATE land SET pal_kz = 'NCL' WHERE land_name = 'Neukaledonien';
UPDATE land SET pal_kz = 'NZ' WHERE land_name = 'Neuseeland';
UPDATE land SET pal_kz = 'NIC' WHERE land_name = 'Nicaragua';
UPDATE land SET pal_kz = 'NL' WHERE land_name = 'Niederlande';
UPDATE land SET pal_kz = 'RN' WHERE land_name = 'Niger';
UPDATE land SET pal_kz = 'WAN' WHERE land_name = 'Nigeria';
UPDATE land SET pal_kz = 'NIU' WHERE land_name = 'Niue';
UPDATE land SET pal_kz = 'PRK' WHERE land_name = 'Nordkorea';
UPDATE land SET pal_kz = 'MK' WHERE land_name = 'Nordmazedonien';
UPDATE land SET pal_kz = 'NFK' WHERE land_name = 'Norfolkinsel';
UPDATE land SET pal_kz = 'N' WHERE land_name = 'Norwegen';
UPDATE land SET pal_kz = 'OMN' WHERE land_name = 'Oman';
UPDATE land SET pal_kz = 'A' WHERE land_name = 'Österreich';
UPDATE land SET pal_kz = 'PK' WHERE land_name = 'Pakistan';
UPDATE land SET pal_kz = 'PLW' WHERE land_name = 'Palau';
UPDATE land SET pal_kz = 'PA' WHERE land_name = 'Panama';
UPDATE land SET pal_kz = 'PNG' WHERE land_name = 'Papua-Neuguinea';
UPDATE land SET pal_kz = 'PY' WHERE land_name = 'Paraguay';
UPDATE land SET pal_kz = 'PE' WHERE land_name = 'Peru';
UPDATE land SET pal_kz = 'RP' WHERE land_name = 'Philippinen';
UPDATE land SET pal_kz = 'PCN' WHERE land_name = 'Pitcairninseln';
UPDATE land SET pal_kz = 'PL' WHERE land_name = 'Polen';
UPDATE land SET pal_kz = 'P' WHERE land_name = 'Portugal';
UPDATE land SET pal_kz = 'PRI' WHERE land_name = 'Puerto Rico';
UPDATE land SET pal_kz = 'REU' WHERE land_name = 'Réunion';
UPDATE land SET pal_kz = 'RWA' WHERE land_name = 'Ruanda';
UPDATE land SET pal_kz = 'RO' WHERE land_name = 'Rumänien';
UPDATE land SET pal_kz = 'RUS' WHERE land_name = 'Russland';
UPDATE land SET pal_kz = 'SLB' WHERE land_name = 'Salomonen';
UPDATE land SET pal_kz = 'RNR' WHERE land_name = 'Sambia';
UPDATE land SET pal_kz = 'WS' WHERE land_name = 'Samoa';
UPDATE land SET pal_kz = 'RSM' WHERE land_name = 'San Marino';
UPDATE land SET pal_kz = 'STP' WHERE land_name = 'São Tomé & Príncipe';
UPDATE land SET pal_kz = 'SA' WHERE land_name = 'Saudi-Arabien';
UPDATE land SET pal_kz = 'S' WHERE land_name = 'Schweden';
UPDATE land SET pal_kz = 'CH' WHERE land_name = 'Schweiz';
UPDATE land SET pal_kz = 'SN' WHERE land_name = 'Senegal';
UPDATE land SET pal_kz = 'SRB' WHERE land_name = 'Serbien';
UPDATE land SET pal_kz = 'SY' WHERE land_name = 'Seychellen';
UPDATE land SET pal_kz = 'WAL' WHERE land_name = 'Sierra Leone';
UPDATE land SET pal_kz = 'ZW' WHERE land_name = 'Simbabwe';
UPDATE land SET pal_kz = 'SGP' WHERE land_name = 'Singapur';
UPDATE land SET pal_kz = 'SK' WHERE land_name = 'Slowakei';
UPDATE land SET pal_kz = 'SLO' WHERE land_name = 'Slowenien';
UPDATE land SET pal_kz = 'SO' WHERE land_name = 'Somalia';
UPDATE land SET pal_kz = 'E' WHERE land_name = 'Spanien';
UPDATE land SET pal_kz = 'CL' WHERE land_name = 'Sri Lanka';
UPDATE land SET pal_kz = 'SHN' WHERE land_name = 'St. Helena';
UPDATE land SET pal_kz = 'KNA' WHERE land_name = 'St. Kitts und Nevis';
UPDATE land SET pal_kz = 'WL' WHERE land_name = 'St. Lucia';
UPDATE land SET pal_kz = 'WV' WHERE land_name = 'St. Vincent und die Grenadinen';
UPDATE land SET pal_kz = 'ZA' WHERE land_name = 'Südafrika';
UPDATE land SET pal_kz = 'SUD' WHERE land_name = 'Sudan';
UPDATE land SET pal_kz = 'ROK' WHERE land_name = 'Südkorea';
UPDATE land SET pal_kz = 'SME' WHERE land_name = 'Suriname';
UPDATE land SET pal_kz = 'SD' WHERE land_name = 'Swasiland';
UPDATE land SET pal_kz = 'SYR' WHERE land_name = 'Syrien';
UPDATE land SET pal_kz = 'TJ' WHERE land_name = 'Tadschikistan';
UPDATE land SET pal_kz = 'TWN' WHERE land_name = 'Taiwan (Republik China)';
UPDATE land SET pal_kz = 'EAT' WHERE land_name = 'Tansania';
UPDATE land SET pal_kz = 'T' WHERE land_name = 'Thailand';
UPDATE land SET pal_kz = 'TG' WHERE land_name = 'Togo';
UPDATE land SET pal_kz = 'TKL' WHERE land_name = 'Tokelau';
UPDATE land SET pal_kz = 'TON' WHERE land_name = 'Tonga';
UPDATE land SET pal_kz = 'TT' WHERE land_name = 'Trinidad und Tobago';
UPDATE land SET pal_kz = 'TD' WHERE land_name = 'Tschad';
UPDATE land SET pal_kz = 'CZ' WHERE land_name = 'Tschechien';
UPDATE land SET pal_kz = 'TN' WHERE land_name = 'Tunesien';
UPDATE land SET pal_kz = 'TR' WHERE land_name = 'Türkei';
UPDATE land SET pal_kz = 'TM' WHERE land_name = 'Turkmenistan';
UPDATE land SET pal_kz = 'TCA' WHERE land_name = 'Turks- und Caicosinseln';
UPDATE land SET pal_kz = 'TUV' WHERE land_name = 'Tuvalu';
UPDATE land SET pal_kz = 'EAU' WHERE land_name = 'Uganda';
UPDATE land SET pal_kz = 'UA' WHERE land_name = 'Ukraine';
UPDATE land SET pal_kz = 'H' WHERE land_name = 'Ungarn';
UPDATE land SET pal_kz = 'ROU' WHERE land_name = 'Uruguay';
UPDATE land SET pal_kz = 'USA' WHERE land_name = 'USA';
UPDATE land SET pal_kz = 'ZU' WHERE land_name = 'Usbekistan';
UPDATE land SET pal_kz = 'VUT' WHERE land_name = 'Vanuatu';
UPDATE land SET pal_kz = 'VAT' WHERE land_name = 'Vatikan';
UPDATE land SET pal_kz = 'YV' WHERE land_name = 'Venezuela';
UPDATE land SET pal_kz = 'UAE' WHERE land_name = 'Vereinigte Arabische Emirate';
UPDATE land SET pal_kz = 'VN' WHERE land_name = 'Vietnam';
UPDATE land SET pal_kz = 'WLF' WHERE land_name = 'Wallis und Futuna';
UPDATE land SET pal_kz = 'CAF' WHERE land_name = 'Zentralafrikanische Republik';
UPDATE land SET pal_kz = 'CY' WHERE land_name = 'Zypern';
UPDATE land SET pal_kz = 'VIR' WHERE land_name = 'Amerik. Jungferninseln';
UPDATE land SET pal_kz = 'SPM' WHERE land_name = 'St.Pierre';
UPDATE land SET pal_kz = 'NA' WHERE land_name = 'Niederländische Antillen';
