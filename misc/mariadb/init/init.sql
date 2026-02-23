CREATE DATABASE  IF NOT EXISTS `ibos` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_german2_ci */;
USE `ibos`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 172.30.59.12    Database: ibos
-- ------------------------------------------------------
-- Server version	5.5.5-10.5.29-MariaDB-0+deb11u1-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ABGLEICHSLISTE`
--

DROP TABLE IF EXISTS `ABGLEICHSLISTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ABGLEICHSLISTE` (
                                  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                  `PJnr` int(6) unsigned DEFAULT NULL,
                                  `ASnr` int(6) unsigned DEFAULT NULL COMMENT 'AUSSCHREIBUNG.ASnr',
                                  `datum` date DEFAULT NULL,
                                  `datum_zahlungseingang` date DEFAULT NULL COMMENT 'Datum Zahlungseingang',
                                  `betrag` decimal(15,2) DEFAULT NULL,
                                  `betrag_eingang` decimal(15,2) DEFAULT NULL COMMENT 'Tatsächlich eingegangener Betrag',
                                  `zahlungspflichtiger` enum('ams','aqua','berater','bfi','bund','caritas','comino','ibis','ipc-center','land','mentor','ministerium','waff','weidinger','wifi','fab','best','bit') DEFAULT NULL,
                                  `empfaenger1` enum('ibis','subunternehmer') DEFAULT NULL,
                                  `empfaenger2` enum('comino','caritas','aqua','ibis','weidinger','waff','wifi','bfi','berater','kapsch','murad','ipcenter','jaw','ams','mentor','fab','best','bit') DEFAULT NULL,
                                  `bemerkung` text DEFAULT NULL,
                                  `status` enum('offen','bezahlt') DEFAULT NULL,
                                  `geplant` int(1) unsigned NOT NULL DEFAULT 0,
                                  `zahlungstyp` enum('teilzahlung','akontierung','restzahlung') DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10177 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ABTEILUNG`
--

DROP TABLE IF EXISTS `ABTEILUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ABTEILUNG` (
                             `ABnr` int(3) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Abteilungs-Nummer',
                             `ABname` varchar(40) NOT NULL DEFAULT '' COMMENT 'Abteilungs-Name',
                             `ABstatus` enum('a','i') NOT NULL DEFAULT 'a' COMMENT 'Status',
                             `ABaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                             `ABaeuser` char(35) DEFAULT NULL COMMENT 'Änderungsbenutzer',
                             `ABerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                             `ABeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                             PRIMARY KEY (`ABnr`),
                             UNIQUE KEY `ABname` (`ABname`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AB_MAPPING`
--

DROP TABLE IF EXISTS `AB_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AB_MAPPING` (
                              `id_alt` int(6) unsigned DEFAULT NULL,
                              `bez_alt` varchar(100) DEFAULT NULL,
                              `id_neu` int(6) unsigned DEFAULT NULL,
                              `bez_neu` varchar(100) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ADRESSE`
--

DROP TABLE IF EXISTS `ADRESSE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ADRESSE` (
                           `ADadnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Adress-Nummer',
                           `ADtyp` enum('tr','tn') DEFAULT NULL COMMENT 'tr => Trainer, tn => Teilnehmer',
                           `ADfadnr` int(6) unsigned DEFAULT 0 COMMENT 'Adressnummer der Firma (=Referenz auf Firma)',
                           `ADibis_gs` int(6) unsigned DEFAULT NULL COMMENT 'ibis Geschäftsstelle',
                           `ADibis_gseinsatz` varchar(20) DEFAULT NULL,
                           `ADadrtype` enum('p','f') NOT NULL DEFAULT 'p' COMMENT 'Person oder Firma',
                           `ADanrede` int(6) unsigned DEFAULT NULL COMMENT 'Anrede-Code',
                           `ADgeschlecht` enum('m','w','d','i','o') DEFAULT NULL COMMENT 'm => männlich, w => weiblich , d => divers , i => inter, o => offen ',
                           `ADtitel_old` int(6) unsigned DEFAULT NULL,
                           `ADtitel` int(6) unsigned DEFAULT NULL,
                           `ADtitelv` int(6) unsigned DEFAULT NULL COMMENT 'Titel-Code verliehen',
                           `ADznf1` varchar(50) DEFAULT NULL COMMENT 'Zuname, Firmenname1',
                           `ADvnf2` varchar(50) DEFAULT NULL COMMENT 'Vorname, Firmenname 2',
                           `ADsube` text DEFAULT NULL COMMENT 'Eingabe von Suchbegriffen leerzeichngetrennt z.B. ehemals Meier vip',
                           `ADstrasse` varchar(50) DEFAULT NULL COMMENT 'Anschrift 1, Strasse',
                           `ADstrobjekt` varchar(50) DEFAULT NULL COMMENT 'Anschrift 2, Objekt',
                           `ADfsb` int(3) unsigned zerofill DEFAULT NULL COMMENT 'Feststellbescheid (Grad der Behinderung)',
                           `ADfsb_kopie_uebergeben` tinyint(1) DEFAULT NULL,
                           `ADlkz` varchar(6) DEFAULT NULL COMMENT 'LKZ',
                           `ADplz` varchar(6) DEFAULT NULL COMMENT 'PLZ',
                           `ADort` varchar(50) DEFAULT NULL COMMENT 'Ort',
                           `ADplz2` varchar(6) DEFAULT NULL COMMENT 'PLZ Nebenwohnsitz',
                           `ADlkz2` varchar(6) DEFAULT NULL COMMENT 'LKZ Nebenwohnsitz',
                           `ADort2` varchar(50) DEFAULT NULL COMMENT 'Ort Nebenwohnsitz',
                           `ADstrasse2` varchar(50) DEFAULT NULL COMMENT 'Anschrift 1, Strasse Nebenwohnsitz',
                           `ADbundesland` int(6) unsigned DEFAULT NULL COMMENT 'Bundesland',
                           `ADzukz` enum('p','f','h','c') DEFAULT 'p' COMMENT 'Zusende-Kennzeichen (Privat, Firma, zHd, c/o)',
                           `ADtelp` varchar(20) DEFAULT NULL COMMENT 'Telefon Privat mit Vorwahl',
                           `ADfaxp` varchar(20) DEFAULT NULL COMMENT 'Fax Privat mit Vorwahl',
                           `ADtelf` varchar(20) DEFAULT NULL COMMENT 'Telefon Firma mit Vorwahl',
                           `ADfaxf` varchar(20) DEFAULT NULL COMMENT 'Fax Firma mit Vorwahl',
                           `ADsipnr` varchar(20) DEFAULT NULL COMMENT 'SIP-Nummer',
                           `ADmobil1` varchar(20) DEFAULT NULL COMMENT 'Mobil Nummer 1',
                           `ADmobil1Besitzer` varchar(35) DEFAULT NULL,
                           `ADmobil2` varchar(20) DEFAULT NULL COMMENT 'Mobil Nummer 2',
                           `ADmobil2Besitzer` varchar(35) DEFAULT NULL,
                           `ADemail1` varchar(80) DEFAULT NULL COMMENT 'EMail Adresse 1',
                           `ADemail2` varchar(80) DEFAULT NULL COMMENT 'EMail Adresse 2',
                           `ADinternet` varchar(150) DEFAULT NULL COMMENT 'Internet-Adresse / Homepage',
                           `ADgebdatum` date DEFAULT NULL COMMENT 'Geburtsdatum',
                           `ADgebdatumf` int(1) unsigned DEFAULT 0 COMMENT 'unbekannter Geburtstag',
                           `ADgebort` varchar(40) DEFAULT NULL COMMENT 'Geburtsort',
                           `ADgebland` varchar(40) DEFAULT NULL COMMENT 'Geburtsland',
                           `ADstaatsb` int(6) DEFAULT NULL,
                           `ADstaatenlos` enum('allgemein','schutzberechtigt','fluechtling') DEFAULT NULL COMMENT 'Allgemein staatenlose, subsidiär schutzberechtigt, Konventionsflüchtling',
                           `ADmuttersprache` int(6) DEFAULT NULL COMMENT 'MUTTERSPRACHE.id',
                           `ADmuttersprache_keine_Angabe` tinyint(1) DEFAULT NULL,
                           `ADfamstand` int(6) unsigned DEFAULT NULL COMMENT 'Familien-Stand',
                           `ADerstkontaktAm` date DEFAULT NULL,
                           `ADbewerbungAm` date DEFAULT NULL,
                           `ADersteintritt` date DEFAULT NULL,
                           `ADpersnr` varchar(10) DEFAULT NULL COMMENT 'Personal-Nummer',
                           `ADsvnr` varchar(50) DEFAULT NULL,
                           `ADsvnr_unbekannt` int(1) unsigned DEFAULT 0,
                           `ADversicherung` varchar(50) DEFAULT NULL COMMENT 'Versicherung',
                           `ADmitversichertbei` varchar(50) DEFAULT NULL COMMENT 'mitversichert bei',
                           `ADErzBe` varchar(50) DEFAULT NULL COMMENT 'ErziehungsberechtigteR',
                           `ADErzBeTel` varchar(20) DEFAULT NULL COMMENT 'Telefonnummer: Erziehungsberechtigter',
                           `ADErzBeMail` varchar(80) DEFAULT NULL COMMENT 'Email: Erziehungsberechtigter',
                           `ADVorM` varchar(50) DEFAULT NULL COMMENT 'Vormund',
                           `ADVorMTel` varchar(20) DEFAULT NULL COMMENT 'Telefonnummer: Vormund',
                           `ADVorMMail` varchar(80) DEFAULT NULL COMMENT 'Email: Vormund',
                           `ADarbeitsgenehmigung` varchar(40) DEFAULT NULL COMMENT 'Arbeitsgenehmigung',
                           `ADarbeitsgenbis` date DEFAULT NULL COMMENT 'Arbeitsgenehmigung bis',
                           `ADfuehrerschein` varchar(10) DEFAULT NULL COMMENT 'Führerscheine',
                           `ADpruefungen` varchar(10) DEFAULT NULL COMMENT 'Prüfungen',
                           `ADberuf` varchar(40) DEFAULT NULL COMMENT 'Beruf',
                           `ADfunktion` int(6) DEFAULT NULL,
                           `ADkreditornr` varchar(20) DEFAULT NULL COMMENT 'Kreditoren-Nummer',
                           `ADbank` varchar(40) DEFAULT NULL COMMENT 'Bankverbindung',
                           `ADbankblz` varchar(50) DEFAULT NULL,
                           `ADbankkonto` varchar(12) DEFAULT NULL COMMENT 'Bank-Konto',
                           `ADbankiban` varchar(34) DEFAULT NULL COMMENT 'Bank IBAN-Nummer',
                           `ADbankbic` varchar(11) DEFAULT NULL COMMENT 'Bank BIC',
                           `ADfbnr` varchar(20) DEFAULT NULL COMMENT 'Firmenbuch-Nummer',
                           `ADfbnrgericht` varchar(20) DEFAULT NULL COMMENT 'Firmenbuch Gericht',
                           `ADsteuernr` varchar(20) DEFAULT NULL COMMENT 'Steuernummer',
                           `ADfinanzamt` varchar(40) DEFAULT NULL COMMENT 'Finanzamt',
                           `ADuid` varchar(20) DEFAULT NULL COMMENT 'Umsatzsteuernummer',
                           `ADklientid` int(6) unsigned DEFAULT NULL COMMENT 'IBIS_FIRMA.id',
                           `ADklientid_old` tinyint(2) DEFAULT NULL,
                           `ADbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                           `ADnoml` enum('n','y') DEFAULT 'n' COMMENT 'No-Mail-Kennzeichen',
                           `ADfoto` varchar(60) DEFAULT NULL COMMENT 'Foto',
                           `ADbildLink` text DEFAULT NULL COMMENT 'Nur für Trainer und PKs. Link zu einem Bild, das auf der Hompage angezeigt wird',
                           `ADkategorie_old` int(6) unsigned DEFAULT NULL,
                           `ADkategorie` int(6) unsigned DEFAULT NULL COMMENT 'FK:MITARBEITER_KATEGORIE.id',
                           `ADgewerbeschein` int(1) DEFAULT NULL,
                           `ADowner` varchar(10) DEFAULT NULL COMMENT 'Eigentümer der Adresse',
                           `ADberecht` varchar(10) DEFAULT NULL COMMENT 'Berechtigungsliste',
                           `ADquelle` varchar(10) DEFAULT NULL COMMENT 'Adress-Quelle',
                           `ADprda` date DEFAULT NULL,
                           `ADpruser` varchar(15) DEFAULT NULL,
                           `ADstatus` char(1) NOT NULL DEFAULT 'a' COMMENT 'aktiv, inaktiv, Bewerbung zuruekgezogen',
                           `ADuserid` varchar(35) DEFAULT NULL,
                           `ADpwd` varchar(32) DEFAULT NULL,
                           `ADtcstatus` enum('aktiv','inaktiv') DEFAULT NULL,
                           `ADsettinglistlg` int(3) DEFAULT NULL,
                           `ADlastlogin` datetime DEFAULT NULL,
                           `ADmbbe_uebertritt` date DEFAULT NULL,
                           `ADbBcreate` char(1) DEFAULT NULL,
                           `ADloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichn',
                           `ADaeda` datetime DEFAULT NULL COMMENT 'Änderungsdatum',
                           `ADaeuser` char(35) DEFAULT NULL,
                           `ADerda` datetime DEFAULT NULL COMMENT 'Erstellungsdatum',
                           `ADeruser` char(35) DEFAULT NULL,
                           PRIMARY KEY (`ADadnr`),
                           UNIQUE KEY `ADuserid` (`ADuserid`),
                           KEY `ADtyp` (`ADtyp`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=423228 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ADRESSE_ACTIVE_DIRECTORY`
--

DROP TABLE IF EXISTS `ADRESSE_ACTIVE_DIRECTORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ADRESSE_ACTIVE_DIRECTORY` (
                                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                            `AdresseId` int(6) unsigned NOT NULL,
                                            `ActiveDirectoryGUID` varchar(36) NOT NULL,
                                            `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                            `ErstellungsBenutzer` char(35) NOT NULL,
                                            PRIMARY KEY (`id`),
                                            UNIQUE KEY `ADR_AD_UNIQUE_USER` (`AdresseId`,`ActiveDirectoryGUID`),
                                            KEY `ADR_AD_ActiveDirectoryGuid` (`ActiveDirectoryGUID`),
                                            CONSTRAINT `ADR_AD_AdresseId` FOREIGN KEY (`AdresseId`) REFERENCES `ADRESSE` (`ADadnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=91213 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Zuweisung von Active Directory Benutzern zu Adressdatensätzen';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ADRESSE_ACTIVE_DIRECTORY_PROJEKT`
--

DROP TABLE IF EXISTS `ADRESSE_ACTIVE_DIRECTORY_PROJEKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ADRESSE_ACTIVE_DIRECTORY_PROJEKT` (
                                                    `AdresseActiveDirectoryId` int(10) unsigned NOT NULL,
                                                    `ProjektId` int(6) NOT NULL,
                                                    `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                    `ErstellungsBenutzer` char(35) NOT NULL,
                                                    PRIMARY KEY (`AdresseActiveDirectoryId`,`ProjektId`),
                                                    KEY `FK_ADR_AD_PJ_ProjektId_idx` (`ProjektId`),
                                                    CONSTRAINT `ADR_AD_AdresseActiveDirectoryId` FOREIGN KEY (`AdresseActiveDirectoryId`) REFERENCES `ADRESSE_ACTIVE_DIRECTORY` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                                    CONSTRAINT `ADR_AD_PJ_ProjektId` FOREIGN KEY (`ProjektId`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Zuweisung einer Adresse AD Account Zuweisung zu einem Projekt';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AD_AUSTRITT`
--

DROP TABLE IF EXISTS `AD_AUSTRITT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AD_AUSTRITT` (
                               `ADRESSE_adnr` int(6) unsigned NOT NULL,
                               `ansprechpartner_id` int(10) unsigned DEFAULT NULL COMMENT 'ID aus ADRESSE',
                               `eams_delete` tinyint(4) DEFAULT NULL,
                               `austritt_am` date DEFAULT NULL,
                               `AD_AUSTRITT_GRUND_id` int(6) unsigned DEFAULT NULL,
                               `sonstiger_austrittsgrund` char(100) DEFAULT NULL,
                               `uebertritt_freies_dv` tinyint(1) DEFAULT NULL,
                               `resturlaub` tinyint(1) DEFAULT NULL,
                               `resturlaub_tage_konsumiert` int(6) unsigned DEFAULT NULL,
                               `resturlaub_tage_ausbezahlt` decimal(10,2) DEFAULT NULL,
                               `ueberstunden` tinyint(1) DEFAULT NULL,
                               `ueberstunden_konsumiert` decimal(10,2) DEFAULT NULL,
                               `ueberstunden_ausbezahlt` decimal(10,2) DEFAULT NULL,
                               `spesen` tinyint(1) DEFAULT NULL,
                               `spesen_euro` decimal(10,2) DEFAULT NULL,
                               `email_loeschen_bis` date DEFAULT NULL,
                               `email_weiterleitung` tinyint(1) DEFAULT NULL,
                               `email_weiterleitung_ab` date DEFAULT NULL,
                               `email_adresse` varchar(50) DEFAULT NULL,
                               `email_adresse_domain_id` int(6) unsigned DEFAULT NULL COMMENT 'AD_AUSTRITT_EMAIL_DOMAINS.id',
                               `email_weiterleitung_bis` date DEFAULT NULL,
                               `email_kopie_mitarbeiter_bis` date DEFAULT NULL,
                               `firmenschluessel` varchar(100) DEFAULT NULL,
                               `handy` varchar(100) DEFAULT NULL,
                               `laptop` varchar(100) DEFAULT NULL,
                               `vodafone_karte` varchar(100) DEFAULT NULL,
                               `bankomatkarte` varchar(100) DEFAULT NULL,
                               `rueckzahlung_weiterbildung` tinyint(1) DEFAULT NULL COMMENT 'Rückzahlungsvereinbarung Weiterbildung',
                               `wiedereintritt_am` date DEFAULT NULL COMMENT 'Wiedereintritt vorgesehen am',
                               `serverprofil_aktiv_bis` date DEFAULT NULL COMMENT 'Serverprofil aktiv bis',
                               `aktives_projekt` int(6) unsigned DEFAULT NULL COMMENT 'PROJEKT.PJnr, in dem Trainer aktiv war',
                               `datenaufbewahrung_monate` int(6) unsigned DEFAULT NULL COMMENT 'Gibt an, wie lange Trainerstammdaten nach dessen Austritt aufbewahrt werden dürfen',
                               `datenloeschung_info_mail_aktiv` tinyint(1) DEFAULT NULL COMMENT 'Info-Mail wird ein Monat vor Ende der Aufbewahrungsfrist verschickt',
                               `datenspeicherung_name` tinyint(1) DEFAULT NULL COMMENT 'Einverständniserklärung über speicherung von Titel, Vorname, Nachname',
                               `datenspeicherung_gebdatum` tinyint(1) DEFAULT NULL COMMENT 'Einverständniserklärung über speicherung von Geburtsdatum.',
                               `datenspeicherung_tel` tinyint(1) DEFAULT NULL COMMENT 'Einverständniserklärung über speicherung von Telefonnr.',
                               `datenspeicherung_email` tinyint(1) DEFAULT NULL COMMENT 'Einverständniserklärung über speicherung von E-Mail-Adresse',
                               `datenspeicherung_ausb_erf_kenntn` tinyint(1) DEFAULT NULL COMMENT 'Einverständniserklärung über speicherung von Ausbildungen/Erfahrungen/Kenntnisse',
                               `datenspeicherung_dokumente` tinyint(1) DEFAULT NULL COMMENT 'Einverständniserklärung über speicherung von Dokumenten',
                               PRIMARY KEY (`ADRESSE_adnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AD_AUSTRITT_EMAIL_DOMAINS`
--

DROP TABLE IF EXISTS `AD_AUSTRITT_EMAIL_DOMAINS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AD_AUSTRITT_EMAIL_DOMAINS` (
                                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                             `domain` char(255) DEFAULT NULL,
                                             `aktiv` tinyint(1) DEFAULT NULL COMMENT 'Domain ist eine aktive Domain',
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AD_AUSTRITT_GRUND`
--

DROP TABLE IF EXISTS `AD_AUSTRITT_GRUND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AD_AUSTRITT_GRUND` (
                                     `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                     `bezeichnung` char(100) DEFAULT NULL,
                                     `sort` int(3) unsigned DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AD_AUSTRITT_STANDORTE`
--

DROP TABLE IF EXISTS `AD_AUSTRITT_STANDORTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AD_AUSTRITT_STANDORTE` (
                                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                         `ADRESSE_adnr` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                         `STANDORT_id` int(6) unsigned DEFAULT NULL COMMENT 'STANDORT.SOstandortid',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2269 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AD_LERNPLATTFORM`
--

DROP TABLE IF EXISTS `AD_LERNPLATTFORM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AD_LERNPLATTFORM` (
                                    `LP_ADadnr` int(6) unsigned NOT NULL,
                                    `benutzername` varchar(35) DEFAULT NULL,
                                    `kategorie` enum('bewerber','mitarbeiter') DEFAULT NULL,
                                    `pw_aendern` int(1) unsigned DEFAULT NULL,
                                    `erstellt_am` datetime /* mariadb-5.3 */ NOT NULL,
                                    `deaktiviert_am` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `aeuser` char(35) DEFAULT NULL,
                                    `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `eruser` char(35) DEFAULT NULL,
                                    PRIMARY KEY (`LP_ADadnr`),
                                    UNIQUE KEY `benutzername_UNIQUE` (`benutzername`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Daten für Lernplattform	';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AD_MK`
--

DROP TABLE IF EXISTS `AD_MK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AD_MK` (
                         `ADMKnr` int(1) unsigned NOT NULL AUTO_INCREMENT,
                         `ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                         `ADMKtyp` char(12) DEFAULT NULL,
                         `ADMKaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `ADMKaeuser` char(35) DEFAULT NULL,
                         `ADMKerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `ADMKeruser` char(35) DEFAULT NULL,
                         PRIMARY KEY (`ADMKnr`,`ADRESSE_ADadnr`),
                         KEY `AD_MK_FKIndex1` (`ADRESSE_ADadnr`)
) ENGINE=InnoDB AUTO_INCREMENT=4237 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AD_ZU`
--

DROP TABLE IF EXISTS `AD_ZU`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AD_ZU` (
                         `ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                         `ADZUdatumeinst` date DEFAULT NULL,
                         `ADZUwostd` decimal(4,2) DEFAULT NULL,
                         `ADZUselbstbesch` text DEFAULT NULL,
                         `ADZUref` int(6) unsigned DEFAULT NULL,
                         `ADZUreftxt` char(128) DEFAULT NULL,
                         `ADZUsprachetxt` char(128) DEFAULT NULL,
                         `ADZUloek` enum('n','y') DEFAULT 'n',
                         `ADZUaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `ADZUaeuser` char(35) DEFAULT NULL,
                         `ADZUerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `ADZUeruser` char(35) DEFAULT NULL,
                         PRIMARY KEY (`ADRESSE_ADadnr`),
                         KEY `AD_ZU_FKIndex1` (`ADRESSE_ADadnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AKQUISITIONSBETRIEBE`
--

DROP TABLE IF EXISTS `AKQUISITIONSBETRIEBE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AKQUISITIONSBETRIEBE` (
                                        `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                        `polit_bez` int(6) unsigned DEFAULT NULL,
                                        `hauptverband_id` int(12) unsigned DEFAULT NULL,
                                        `herold_nr` int(6) unsigned DEFAULT NULL,
                                        `name` varchar(100) DEFAULT NULL,
                                        `plz` varchar(6) DEFAULT NULL,
                                        `strasse` varchar(50) DEFAULT NULL,
                                        `ort` varchar(50) DEFAULT NULL,
                                        `reihung` int(6) unsigned DEFAULT NULL,
                                        `qug_ug` decimal(14,13) DEFAULT NULL,
                                        `qug_og` decimal(14,13) DEFAULT NULL,
                                        PRIMARY KEY (`id`),
                                        KEY `qug_ug_qug_og` (`qug_ug`,`qug_og`)
) ENGINE=InnoDB AUTO_INCREMENT=14265 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG` (
                                  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                  `ADRESSE_adnr` int(6) unsigned DEFAULT NULL COMMENT 'ID des Trainers',
                                  `art` enum('fix','freio','freim') DEFAULT NULL COMMENT 'Art fix, frei ohne bzw. mit Gewerbeschein',
                                  `dienstgeber` int(6) unsigned DEFAULT NULL COMMENT 'IBIS_FIRMA.id',
                                  `agen` varchar(40) DEFAULT NULL COMMENT 'Arbeitsgenehmigung',
                                  `agen_bis` date DEFAULT NULL COMMENT 'Arbeitsgenehmigung gültig bis',
                                  `agenbh` varchar(40) DEFAULT NULL COMMENT 'Arbeitsgenehmigung ausstellende Behoerde',
                                  `agen_kopie_uebergeben` tinyint(1) DEFAULT NULL COMMENT 'Arbeitsgenehmigung Kopie übergeben',
                                  `agen_unbefristet` tinyint(1) DEFAULT 0,
                                  `geschaeftsbereich` int(6) unsigned DEFAULT NULL COMMENT 'Kostenstelle',
                                  `geringfuegig_karenz` tinyint(1) DEFAULT NULL COMMENT 'Beschäftigungsausmass geringfügig Karenz',
                                  `kurzarbeit` tinyint(1) DEFAULT NULL COMMENT 'Kurzarbeit',
                                  `status` enum('offen','zugewiesen','gesperrt') DEFAULT 'offen',
                                  `formblattA1` tinyint(1) DEFAULT NULL COMMENT 'Formblatt A1 der Krankenkassa',
                                  `DVnr_old` int(6) unsigned DEFAULT NULL COMMENT 'Alte Dienstvertrag-ID',
                                  `PBid_old` int(6) unsigned DEFAULT NULL COMMENT 'Alte PBid',
                                  `loek` enum('n','y') DEFAULT NULL,
                                  `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `aeuser` char(35) DEFAULT NULL,
                                  `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `eruser` char(35) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25233 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_CHANGELOG`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_CHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_CHANGELOG` (
                                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                            `ADRESSE_adnr` int(6) unsigned DEFAULT NULL,
                                            `ARBEITSVERTRAG_id` int(6) unsigned DEFAULT NULL,
                                            `datum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                            `telefon` tinyint(1) DEFAULT NULL,
                                            `mobil` tinyint(1) DEFAULT NULL,
                                            `email` tinyint(1) DEFAULT NULL,
                                            `bank` tinyint(1) DEFAULT NULL,
                                            `bankblz` tinyint(1) DEFAULT NULL,
                                            `bankkonto` tinyint(1) DEFAULT NULL,
                                            `bankiban` tinyint(1) DEFAULT NULL,
                                            `bankbic` tinyint(1) DEFAULT NULL,
                                            `avab` tinyint(1) DEFAULT NULL,
                                            `pendlerp` tinyint(1) DEFAULT NULL,
                                            `agen` tinyint(1) DEFAULT NULL,
                                            `agen_bis` tinyint(1) DEFAULT NULL,
                                            `agenbh` tinyint(1) DEFAULT NULL,
                                            `agen_kopie_uebergeben` tinyint(1) DEFAULT NULL,
                                            `fsb` tinyint(1) DEFAULT NULL,
                                            `fsb_kopie_ueberg` tinyint(1) DEFAULT NULL,
                                            `vdz_geprueft` tinyint(1) DEFAULT NULL,
                                            `kur` tinyint(1) DEFAULT NULL,
                                            `uid` tinyint(1) DEFAULT NULL,
                                            `finanzamt` tinyint(1) DEFAULT NULL,
                                            `projekt_id` tinyint(1) DEFAULT NULL,
                                            `seminar_id` tinyint(1) DEFAULT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=187926 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_DOKUMENTE`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_DOKUMENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_DOKUMENTE` (
                                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                            `titel` char(100) DEFAULT NULL,
                                            `datum` date DEFAULT NULL,
                                            `sySTORE_STnr` int(9) unsigned DEFAULT NULL,
                                            `ARBEITSVERTRAG_id` int(6) unsigned DEFAULT NULL,
                                            `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                            `eruser` char(35) DEFAULT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30334 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_FIX`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_FIX`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_FIX` (
                                      `ARBEITSVERTRAG_id` int(6) unsigned NOT NULL,
                                      `avab` tinyint(1) DEFAULT NULL COMMENT 'Alleinverdienerabsetzbetrag',
                                      `pendlerp` tinyint(1) DEFAULT NULL COMMENT 'Pendlerpauschale',
                                      `fsb_kopie_uebergeben` tinyint(1) DEFAULT NULL COMMENT 'Feststellbescheid Kopie übergeben',
                                      `betriebsvereinbarung` tinyint(1) DEFAULT NULL,
                                      `reisekostenvereinbarung` tinyint(1) DEFAULT NULL,
                                      `gleitzeitvereinbarung` tinyint(1) DEFAULT NULL,
                                      `gleitzeitvereinbarung_light` tinyint(1) DEFAULT NULL,
                                      `mobileworkingvereinbarung` tinyint(1) DEFAULT NULL,
                                      `unterweisung_arbeitssicherheit` tinyint(1) DEFAULT NULL COMMENT 'Unterweisung Arbeitssicherheit übergeben',
                                      `vdgeprueft` tinyint(1) DEFAULT NULL COMMENT 'Vordienstzeiten geprüft',
                                      `startcoach_id` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                      `vorgesetzter_id` int(6) unsigned DEFAULT NULL,
                                      `ansaessbesch` tinyint(1) DEFAULT NULL COMMENT 'Ansässigkeitsbescheinigung für Deutschland',
                                      `zulage_berechnen` tinyint(1) DEFAULT 1 COMMENT 'Gehaltszulage vom letzten Zusatz übernehmen',
                                      `vdz_monate_nichterfasst` decimal(6,2) DEFAULT NULL COMMENT 'Nicht erfasste Vordienstzeiten-Monate',
                                      PRIMARY KEY (`ARBEITSVERTRAG_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_FREI`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_FREI`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_FREI` (
                                       `ARBEITSVERTRAG_id` int(6) unsigned NOT NULL,
                                       `kur` tinyint(1) DEFAULT NULL COMMENT 'Kleinunternehmerregelung',
                                       `uid` varchar(40) DEFAULT NULL COMMENT 'Umsatzsteuer-ID',
                                       `finanzamt` varchar(40) DEFAULT NULL,
                                       `alg` tinyint(1) DEFAULT NULL COMMENT 'Arbeitslosengeld',
                                       `fba_kopie_uebergeben` tinyint(1) DEFAULT NULL COMMENT 'Firmenbuchauszug Kopie übergeben',
                                       `vtr` varchar(100) DEFAULT NULL COMMENT 'Versicherungsträger',
                                       `vnr` varchar(11) DEFAULT NULL COMMENT 'Versicherungsnr',
                                       `gewart` varchar(100) DEFAULT NULL COMMENT 'Gewerbeart',
                                       `gewab` date DEFAULT NULL COMMENT 'Gewerbeschein gültig ab',
                                       `gewbh` varchar(40) DEFAULT NULL COMMENT 'ausstellende Behörde',
                                       `gewnr` varchar(40) DEFAULT NULL COMMENT 'Gewerbescheinnr',
                                       `PROJEKT_id` int(6) unsigned DEFAULT 0,
                                       `SEMINAR_id` int(6) unsigned DEFAULT 0,
                                       PRIMARY KEY (`ARBEITSVERTRAG_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_NICHTLEISTUNGEN`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_NICHTLEISTUNGEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_NICHTLEISTUNGEN` (
                                                  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                  `ARBEITSVERTRAG_id` int(6) unsigned DEFAULT NULL,
                                                  `datum_von` date DEFAULT NULL,
                                                  `datum_bis` date DEFAULT NULL,
                                                  `wochenstd` decimal(5,2) DEFAULT NULL,
                                                  `art` enum('bildungskarenz','karenz','unbezahlter_urlaub','praesenzdienst','papamonat','hospizkarenz','pflegekarenz','mutterschutz') DEFAULT NULL,
                                                  `bemerkung` text DEFAULT NULL,
                                                  `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                  `aeuser` char(35) DEFAULT NULL,
                                                  `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                  `eruser` char(35) DEFAULT NULL,
                                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=839 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_ZUSATZ`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_ZUSATZ` (
                                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                         `ARBEITSVERTRAG_id` int(6) unsigned DEFAULT NULL,
                                         `datum_von` date DEFAULT NULL,
                                         `datum_bis` date DEFAULT NULL,
                                         `geschaeftsbereich` int(6) unsigned DEFAULT NULL COMMENT 'Kostenstellengruppe',
                                         `dienstgeber` int(6) unsigned DEFAULT NULL COMMENT 'IBIS_FIRMA.id',
                                         `persnr` varchar(10) DEFAULT NULL COMMENT 'Personal-Nummer',
                                         `taetbezeichnung` int(6) unsigned DEFAULT NULL COMMENT 'Berufsbezeichnung (Keytable)',
                                         `taetausmass` int(6) unsigned DEFAULT NULL COMMENT 'Beschäftigungsausmass (Keytable)',
                                         `dienstort` int(6) unsigned DEFAULT NULL COMMENT 'Dienstort/Beschäftigungsort (STANDORT.SOstandortid)',
                                         `fahrkostenpauschale` varchar(128) DEFAULT NULL,
                                         `bemerkung` text DEFAULT NULL,
                                         `bemerkung_drucken` tinyint(1) DEFAULT NULL COMMENT 'Bestimmt, ob Bemerkungsfeld auf Arbeitsvertrag angedruckt wird',
                                         `bemerkung_2` text DEFAULT NULL,
                                         `bemerkung_drucken_2` tinyint(1) DEFAULT 0 COMMENT 'Bestimmt, ob Bemerkungsfeld auf Arbeitsvertrag angedruckt wird',
                                         `status` enum('neu','freigegeben','unterschrieben','gesperrt') DEFAULT NULL,
                                         `unterschrieben_am` datetime DEFAULT NULL COMMENT 'Zeitpunkt der Unterschrift',
                                         `DZid_old` int(6) unsigned DEFAULT NULL,
                                         `loek` enum('n','y') DEFAULT NULL,
                                         `aeda` datetime DEFAULT NULL,
                                         `aeuser` char(35) DEFAULT NULL,
                                         `erda` datetime DEFAULT NULL,
                                         `eruser` char(35) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53982 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_ZUSATZ_FIX`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_ZUSATZ_FIX`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_ZUSATZ_FIX` (
                                             `ARBEITSVERTRAG_ZUSATZ_id` int(6) unsigned NOT NULL,
                                             `taetstatus` int(6) unsigned DEFAULT NULL COMMENT 'Beschäftigungsstatus (Keytable)',
                                             `stdmo` decimal(5,2) DEFAULT NULL COMMENT 'Stunden MO',
                                             `stdmovon` time DEFAULT '00:00:00',
                                             `stdmobis` time DEFAULT '00:00:00',
                                             `stddi` decimal(5,2) DEFAULT NULL COMMENT 'Stunden DI',
                                             `stddivon` time DEFAULT '00:00:00',
                                             `stddibis` time DEFAULT '00:00:00',
                                             `stdmi` decimal(5,2) DEFAULT NULL COMMENT 'Stunden MI',
                                             `stdmivon` time DEFAULT '00:00:00',
                                             `stdmibis` time DEFAULT '00:00:00',
                                             `stddo` decimal(5,2) DEFAULT NULL COMMENT 'Stunden DO',
                                             `stddovon` time DEFAULT '00:00:00',
                                             `stddobis` time DEFAULT '00:00:00',
                                             `stdfr` decimal(5,2) DEFAULT NULL COMMENT 'Stunden FR',
                                             `stdfrvon` time DEFAULT '00:00:00',
                                             `stdfrbis` time DEFAULT '00:00:00',
                                             `stufe` int(6) unsigned DEFAULT NULL COMMENT 'Gehaltsstufe KV_STUFE.id',
                                             `gehalt` decimal(9,2) DEFAULT NULL COMMENT 'Gehalt',
                                             `gehaltszulage` decimal(9,2) DEFAULT NULL,
                                             `gleitzeit` tinyint(1) DEFAULT NULL,
                                             `gleitzeit_light` tinyint(1) DEFAULT NULL,
                                             `mobile_working` tinyint(1) DEFAULT NULL,
                                             `gehaltskorrektur` tinyint(1) DEFAULT 0 COMMENT 'Gehaltskorrektur durch LV nötig',
                                             `mittagspause_ignorieren` tinyint(1) DEFAULT NULL COMMENT 'keine Mittagspause im PEP erforderlich',
                                             `aenderung_in_befristung` tinyint(1) DEFAULT NULL,
                                             `av_dauer` enum('befristung1','befristung2','unbefristet') DEFAULT NULL,
                                             `kategorie` int(6) unsigned DEFAULT NULL COMMENT 'ARBEITSVERTRAG_ZUSATZ_FIX_KATEGORIE.id',
                                             `sicherheitsunterweisung_uebergeben_am` date DEFAULT NULL,
                                             `Arbeitsplanmodell` int(11) DEFAULT NULL,
                                             `Umbuchungsmodell` int(11) DEFAULT NULL,
                                             `Plangruppe` int(11) DEFAULT NULL,
                                             PRIMARY KEY (`ARBEITSVERTRAG_ZUSATZ_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_ZUSATZ_FIX_KATEGORIE`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_ZUSATZ_FIX_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_ZUSATZ_FIX_KATEGORIE` (
                                                       `id` int(6) unsigned NOT NULL,
                                                       `bezeichnung` char(100) DEFAULT NULL,
                                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_ZUSATZ_FIX_KAT_CHECKED`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_ZUSATZ_FIX_KAT_CHECKED`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_ZUSATZ_FIX_KAT_CHECKED` (
                                                         `ARBEITSVERTRAG_ZUSATZ_id` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'ARBEITSVERTRAG_ZUSATZ.id',
                                                         `kategorie_id` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'ARBEITSVERTRAG_ZUSATZ_FIX_KATEGORIE.id',
                                                         PRIMARY KEY (`ARBEITSVERTRAG_ZUSATZ_id`,`kategorie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ARBEITSVERTRAG_ZUSATZ_FREI`
--

DROP TABLE IF EXISTS `ARBEITSVERTRAG_ZUSATZ_FREI`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARBEITSVERTRAG_ZUSATZ_FREI` (
                                              `ARBEITSVERTRAG_ZUSATZ_id` int(6) unsigned NOT NULL,
                                              `stdsatz` decimal(9,2) DEFAULT NULL COMMENT 'Stundensatz',
                                              `max_arbeitsstd` decimal(5,2) DEFAULT NULL COMMENT 'Max. Arbeitsstd pro Woche',
                                              `arbeitstage` int(6) unsigned DEFAULT NULL COMMENT 'Vorgesehene Arbeitstage pro Woche',
                                              PRIMARY KEY (`ARBEITSVERTRAG_ZUSATZ_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AS_HOMEPAGEDATEN`
--

DROP TABLE IF EXISTS `AS_HOMEPAGEDATEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AS_HOMEPAGEDATEN` (
                                    `ASnr` int(6) unsigned NOT NULL,
                                    `AHbildungsprogramm` enum('offen','geschlossen') DEFAULT NULL COMMENT 'offen: öffentliche, von jedermann buchbare Kurse',
                                    `AHbezeichnung` varchar(150) DEFAULT NULL,
                                    `AHzielsetzung` text DEFAULT NULL,
                                    `AHinhalt` text DEFAULT NULL,
                                    `AHonlineVon` date DEFAULT NULL,
                                    `AHonlineBis` date DEFAULT NULL,
                                    `AHtnpreisBrutto` decimal(9,2) DEFAULT NULL COMMENT 'Bruttopreis pro Teilnehmer',
                                    UNIQUE KEY `ASnr` (`ASnr`),
                                    CONSTRAINT `AS_HOMEPAGE_ASnr_AUSSCHREIBUNG` FOREIGN KEY (`ASnr`) REFERENCES `AUSSCHREIBUNG` (`ASnr`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Hompagerelevante Daten von AUSSCHREIBUNG';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AS_MT`
--

DROP TABLE IF EXISTS `AS_MT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AS_MT` (
                         `ASnr` int(6) unsigned NOT NULL,
                         `MTnr` int(6) unsigned NOT NULL,
                         PRIMARY KEY (`ASnr`,`MTnr`),
                         KEY `fk_AS_MT_MTnr_METHODIK` (`MTnr`),
                         CONSTRAINT `fk_AS_MT_ASnr_AUSSCHREIBUNG` FOREIGN KEY (`ASnr`) REFERENCES `AUSSCHREIBUNG` (`ASnr`) ON DELETE CASCADE,
                         CONSTRAINT `fk_AS_MT_MTnr_METHODIK` FOREIGN KEY (`MTnr`) REFERENCES `METHODIK` (`MTnr`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='junction table AS_HOMEPAGE <-> METHODIK';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AS_SK`
--

DROP TABLE IF EXISTS `AS_SK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AS_SK` (
                         `ASnr` int(6) unsigned NOT NULL,
                         `SKnr` int(6) unsigned NOT NULL,
                         PRIMARY KEY (`ASnr`,`SKnr`),
                         KEY `fk_AS_SK_SKnr_SMKATEGORIE` (`SKnr`),
                         CONSTRAINT `fk_AS_SK_ASnr_AUSSCHREIBUNG` FOREIGN KEY (`ASnr`) REFERENCES `AUSSCHREIBUNG` (`ASnr`) ON DELETE CASCADE,
                         CONSTRAINT `fk_AS_SK_SKnr_SMKATEGORIE` FOREIGN KEY (`SKnr`) REFERENCES `SMKATEGORIE` (`SKnr`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='junction table SMKATEGORIE <-> AUSSCHREIBUNG';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUFTRAGGEBER`
--

DROP TABLE IF EXISTS `AUFTRAGGEBER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUFTRAGGEBER` (
                                `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                `bezeichnung` char(255) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUSBILDUNG`
--

DROP TABLE IF EXISTS `AUSBILDUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUSBILDUNG` (
                              `BDnr` int(3) unsigned NOT NULL AUTO_INCREMENT,
                              `ADRESSE_ADadnr` int(6) unsigned NOT NULL DEFAULT 0,
                              `BDart` enum('a','b','t') DEFAULT NULL,
                              `BDbezeichnung` varchar(60) DEFAULT NULL,
                              `BDtyp1` int(6) unsigned DEFAULT NULL,
                              `BDtyp2` int(6) unsigned DEFAULT NULL,
                              `AUSBILDUNGSTYP_TRAINER_id` int(6) unsigned DEFAULT NULL,
                              `BDland` int(6) DEFAULT NULL,
                              `BDbundesland` int(6) unsigned DEFAULT NULL,
                              `BDinstitut` varchar(50) DEFAULT NULL,
                              `BDdatumvon` date DEFAULT NULL,
                              `BDdatumbis` date DEFAULT NULL,
                              `BDdauer_jahre` decimal(9,2) DEFAULT NULL,
                              `BDdauer` int(6) unsigned DEFAULT NULL,
                              `BDwochenstunden` int(6) unsigned DEFAULT NULL,
                              `BDabschluss` enum('y','n') DEFAULT 'n',
                              `BDabschlussdatum` date DEFAULT NULL,
                              `BDabschlussgueltigbis` date DEFAULT NULL,
                              `BDbeschart` enum('f','p','b','w','i') DEFAULT NULL COMMENT 'f => angestellt Vollzeit(fulltime); p => angestellt teiltzeit (parttime);  b => freiberuflich; w => Werkvertrag; i => Praktikum (internship)',
                              `BDzielgruppe` int(6) unsigned DEFAULT NULL,
                              `BDmassnahmentyp` enum('l','p') DEFAULT NULL,
                              `BDtaetigkeitstyp` int(6) unsigned DEFAULT NULL,
                              `BDformale_ausb_LOESCHEN` enum('n','y') DEFAULT 'n' COMMENT 'Umbenannt am 01.06.2011, Löschen am 12.01.2012',
                              `BDzusatz_LOESCHEN` enum('n','y') DEFAULT 'n' COMMENT 'Umbenannt am 01.06.2011, Löschen am 12.01.2012',
                              `BDpaedagog_LOESCHEN` enum('n','y') DEFAULT 'n' COMMENT 'Umbenannt am 01.06.2011, Löschen am 12.01.2012',
                              `BDtrainer_LOESCHEN` enum('n','y') DEFAULT 'n' COMMENT 'Umbenannt am 01.06.2011, Löschen am 12.01.2012',
                              `BDamperf` enum('n','y') DEFAULT 'n' COMMENT 'Ob Erfahrung ArbeitsMarktPolitische Relevanz hat',
                              `BDkat1` int(6) DEFAULT NULL,
                              `BDkat1_dauer` decimal(9,2) DEFAULT NULL,
                              `BDkat2` int(6) DEFAULT NULL,
                              `BDkat2_dauer` decimal(9,2) DEFAULT NULL,
                              `BDkat3` int(6) DEFAULT NULL,
                              `BDkat3_dauer` decimal(9,2) DEFAULT NULL,
                              `BDstichworte` text DEFAULT NULL,
                              `BDinhalt` text DEFAULT NULL,
                              `BDnachweis` enum('n','y') DEFAULT 'n',
                              `BDloek` enum('n','y') DEFAULT 'n',
                              `BDaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `BDaeuser` char(35) DEFAULT NULL,
                              `BDerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `BDeruser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`BDnr`,`ADRESSE_ADadnr`),
                              KEY `AUSBILDUNG_FKIndex1` (`ADRESSE_ADadnr`)
) ENGINE=MyISAM AUTO_INCREMENT=125169 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUSBILDUNGSTYP_TRAINER`
--

DROP TABLE IF EXISTS `AUSBILDUNGSTYP_TRAINER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUSBILDUNGSTYP_TRAINER` (
                                          `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                          `id_parent` int(10) unsigned DEFAULT NULL,
                                          `bezeichnung` varchar(100) DEFAULT NULL,
                                          `ablaufbar` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Manche Ausbildungen können ablaufen',
                                          `order_id` int(6) DEFAULT NULL,
                                          `KYnr` int(6) unsigned DEFAULT NULL COMMENT 'Sicherheitshalber wird hier die alte KYnr von KEYTABLE.BDtyp1 und KEYTABLE.BDtyp2 gespeichert',
                                          `ist_formal` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Formanle oder Zusatz-Ausbildung',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUSSCHREIBUNG`
--

DROP TABLE IF EXISTS `AUSSCHREIBUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUSSCHREIBUNG` (
                                 `ASnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Ausschreibungs-Nummer',
                                 `ASpnr` varchar(20) DEFAULT NULL COMMENT '(e)AMS Auftragsnummer',
                                 `ASbundesland` int(6) NOT NULL COMMENT 'Bundesland',
                                 `ASibis_gs` int(6) DEFAULT NULL COMMENT 'Geschaeftstelle',
                                 `ASbezeichnung1` char(80) DEFAULT NULL COMMENT 'Bezeichnung 1',
                                 `ASbezeichnung2` char(80) DEFAULT NULL COMMENT 'Bezeichnung 2',
                                 `ASmassnahmennr` char(20) DEFAULT NULL COMMENT 'Massnahmennummer',
                                 `ASverfahrensart` int(6) DEFAULT NULL COMMENT 'Verfahrensart',
                                 `ASauftraggeber` int(6) unsigned DEFAULT NULL COMMENT 'Auftraggeber',
                                 `AUFTRAGGEBER_id` int(6) unsigned DEFAULT NULL,
                                 `ASauftraggeber_old` int(6) unsigned DEFAULT NULL COMMENT 'Auftraggeber ALT',
                                 `ASansprech1` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'Ansprechperson 1',
                                 `ASansprech1_old` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'Ansprechperson 1 ALT',
                                 `ASansprechfunkt1` varchar(60) DEFAULT NULL,
                                 `ASansprech2` int(6) unsigned DEFAULT 0 COMMENT 'Ansprechperson 2',
                                 `ASansprech2_old` int(6) unsigned DEFAULT 0 COMMENT 'Ansprechperson 2 ALT',
                                 `ASansprechfunkt2` varchar(60) DEFAULT NULL,
                                 `ASansprechpartner` text DEFAULT NULL,
                                 `ASansprtelefon` char(40) DEFAULT NULL COMMENT 'Telefon',
                                 `ASansprtelefax` char(40) DEFAULT NULL COMMENT 'Telefax',
                                 `ASanspremail` char(60) DEFAULT NULL COMMENT 'EMail',
                                 `ASart` char(80) DEFAULT NULL COMMENT 'Massnahmen-Art',
                                 `ASmarktbereich` int(6) DEFAULT NULL COMMENT 'Marktbereich',
                                 `ASzielgruppe` int(6) DEFAULT NULL COMMENT 'Zielgruppe',
                                 `ASinhalt` int(6) DEFAULT NULL COMMENT 'Inhalt',
                                 `ASeingangsdatum` date DEFAULT NULL COMMENT 'Eingangsdatum',
                                 `ASabgabedatum` date DEFAULT NULL COMMENT 'Abgabe-Datum',
                                 `ASabgabezeit` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Abgabe-Zeit',
                                 `ASplz` char(6) DEFAULT NULL COMMENT 'PLZ',
                                 `ASort` char(40) DEFAULT NULL COMMENT 'Ort der Massnahme',
                                 `ASSOstandortid` int(6) unsigned DEFAULT NULL,
                                 `ASDatumVon` date DEFAULT NULL COMMENT 'Datum von',
                                 `ASDatumBis` date DEFAULT NULL COMMENT 'Datum bis',
                                 `ASzuschlag` text DEFAULT NULL COMMENT 'Zuschlag an',
                                 `ASauftragsbestand` date DEFAULT NULL,
                                 `ASbegruendung` text DEFAULT NULL COMMENT 'Begründung',
                                 `ASbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                                 `ASinhaltsbeschr` text DEFAULT NULL COMMENT 'Inhaltsbeschreibung',
                                 `ASibiscode` char(15) DEFAULT NULL COMMENT 'IBIS-Code berechnet sich aus ASmarktbereich, ASzielgruppe, ASinhalt',
                                 `ASdauer` int(6) DEFAULT NULL COMMENT 'Dauer der Maßnahme (Std)',
                                 `ASteilnehmeranz` int(6) DEFAULT NULL COMMENT 'TN-Anzahl',
                                 `ASpersonal_MNH` int(6) DEFAULT NULL COMMENT 'Personal-MNH',
                                 `ASpreis_ibis` decimal(10,2) DEFAULT NULL COMMENT 'Preis ibis',
                                 `ASdb2prozent` decimal(6,2) DEFAULT NULL COMMENT 'ibis DB2 in %',
                                 `ASdb2absolut` decimal(9,2) DEFAULT NULL COMMENT 'ibis DB2 absolut',
                                 `ASpreis_bestbieter` decimal(9,2) DEFAULT NULL COMMENT 'Preis Bestbieter',
                                 `ASmail_entscheidung` char(60) DEFAULT NULL COMMENT 'Entscheidung',
                                 `AStermin_entscheidung` date DEFAULT NULL COMMENT 'Termin Entscheidung',
                                 `ASmail_konzept` char(60) DEFAULT NULL COMMENT 'Konzept',
                                 `AStermin_konzept` date DEFAULT NULL COMMENT 'Termin Konzept',
                                 `ASmail_nachweise` char(60) DEFAULT NULL COMMENT 'Nachweise',
                                 `AStermin_nachweise` date DEFAULT NULL COMMENT 'Termin Nachweise',
                                 `ASmail_kalkulation` char(60) DEFAULT NULL COMMENT 'Kalkulation',
                                 `AStermin_kalkulation` date DEFAULT NULL COMMENT 'Termin Kalkulation',
                                 `ASmail_TC` char(60) DEFAULT NULL COMMENT 'TrainerInnen',
                                 `AStermin_TC` date DEFAULT NULL COMMENT 'Termin Trainer',
                                 `ASmail_ressourcen` char(60) DEFAULT NULL COMMENT 'Standorte/Ressourcen',
                                 `AStermin_ressourcen` date DEFAULT NULL COMMENT 'Termin Ressourcen',
                                 `ASmail_abgabefertig` char(60) DEFAULT NULL COMMENT 'Abgabe fertig',
                                 `AStermin_abgabefertig` date DEFAULT NULL COMMENT 'Termin Abgabe fertig',
                                 `ASmail_endkontrolle` char(60) DEFAULT NULL COMMENT 'End-Kontrolle',
                                 `AStermin_endkontrolle` date DEFAULT NULL COMMENT 'Termin Endkontrolle',
                                 `ASmail_bietergemeinschaft` char(60) DEFAULT NULL COMMENT 'Bietergemeinschaft',
                                 `AStermin_bietergemeinschaft` date DEFAULT NULL COMMENT 'Termin Bietergemeinschaft',
                                 `ASbew_qualifikaton` text DEFAULT NULL COMMENT 'Qualifikation',
                                 `ASbew_berufserfahrung` text DEFAULT NULL COMMENT 'Berufserfahrung',
                                 `ASbew_methodik` text DEFAULT NULL COMMENT 'Methodik',
                                 `ASbew_didaktik` text DEFAULT NULL COMMENT 'Didaktik',
                                 `ASbew_orgform` text DEFAULT NULL COMMENT 'Organisationsform',
                                 `ASbew_gleichstellung` text DEFAULT NULL COMMENT 'gleichstellungsfördernde Maßnahmen',
                                 `ASbew_frauenanteil` text DEFAULT NULL COMMENT 'Frauen-Anteil',
                                 `ASbew_gender` text DEFAULT NULL COMMENT 'Gender-Praxisnachweise',
                                 `ASbew_raumausstattung` text DEFAULT NULL COMMENT 'Räumliche Ausstattung',
                                 `ASbew_technausstattung` text DEFAULT NULL COMMENT 'Technische Ausstattung',
                                 `ASbew_verkehrsanbindung` text DEFAULT NULL COMMENT 'Verkehrsanbindung',
                                 `ASbew_kosten` text DEFAULT NULL COMMENT 'Kosten der Maßnahme',
                                 `ASrefasnr` int(6) DEFAULT NULL COMMENT 'Referenz auf eine andere Ausschreibung (Nachreichung)',
                                 `ASplanungsstufe` int(1) DEFAULT NULL COMMENT 'Planungsstufe',
                                 `ASesf_finanziert` enum('n','y') DEFAULT NULL COMMENT 'ESF-finanziert?',
                                 `ASams_finanziert` enum('n','y') DEFAULT NULL COMMENT 'AMS-finanziert?',
                                 `ASlandco_finanziert` enum('n','y') DEFAULT NULL COMMENT 'Land CO-finanziert?',
                                 `ASstatus` int(6) DEFAULT NULL COMMENT 'Status',
                                 `ASAbrechnungsmodus` int(6) DEFAULT NULL,
                                 `IndividuellesAnonymisierungsdatum` int(1) unsigned NOT NULL,
                                 `Anonymisierungsdatum` date DEFAULT NULL,
                                 `ASloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                                 `ASaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                 `ASaeuser` char(35) DEFAULT NULL,
                                 `ASerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                                 `ASeruser` char(35) DEFAULT NULL,
                                 `ASzuschlagam` date DEFAULT NULL,
                                 PRIMARY KEY (`ASnr`)
) ENGINE=InnoDB AUTO_INCREMENT=10927 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUSSCHREIBUNG_KETTE`
--

DROP TABLE IF EXISTS `AUSSCHREIBUNG_KETTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUSSCHREIBUNG_KETTE` (
                                       `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                       `AUSSCHREIBUNG_NachfolgerId` int(6) unsigned NOT NULL COMMENT 'Nachfolger AusschreibungsId',
                                       `AUSSCHREIBUNG_VorgaengerId` int(6) unsigned NOT NULL COMMENT 'Vorgänger AusschreibungsId',
                                       `AUSSCHREIBUNG_KETTE_TYP_id` int(10) unsigned NOT NULL COMMENT 'Um welchen Typ von Kettenglied handelt es sich',
                                       `TypZaehler` int(3) unsigned DEFAULT NULL COMMENT 'Zähler für den Typen - wie oft wurde dieser Typ in der Kette verwendet',
                                       `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `AenderungsBenutzer` char(35) DEFAULT NULL,
                                       `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                       `ErstellungsBenutzer` char(35) NOT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `AS_KETTE_AS_NachfolgerId_AS_VorgaengerId` (`AUSSCHREIBUNG_NachfolgerId`,`AUSSCHREIBUNG_VorgaengerId`),
                                       KEY `FK_AS_KETTE_AS_NachfolgerId` (`AUSSCHREIBUNG_NachfolgerId`),
                                       KEY `FK_AS_KETTE_AS_VorgaengerId` (`AUSSCHREIBUNG_VorgaengerId`),
                                       KEY `FK_AS_KETTE_AS_KETTE_TYP_id` (`AUSSCHREIBUNG_KETTE_TYP_id`),
                                       CONSTRAINT `AS_KETTEN_AS_KETTE_TYP_id` FOREIGN KEY (`AUSSCHREIBUNG_KETTE_TYP_id`) REFERENCES `AUSSCHREIBUNG_KETTE_TYP` (`id`),
                                       CONSTRAINT `AS_KETTEN_AS_NachfolgerId` FOREIGN KEY (`AUSSCHREIBUNG_NachfolgerId`) REFERENCES `AUSSCHREIBUNG` (`ASnr`) ON DELETE CASCADE ON UPDATE CASCADE,
                                       CONSTRAINT `AS_KETTEN_AS_VorgaengerId` FOREIGN KEY (`AUSSCHREIBUNG_VorgaengerId`) REFERENCES `AUSSCHREIBUNG` (`ASnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=438 DEFAULT CHARSET=latin1 COLLATE=latin1_german1_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUSSCHREIBUNG_KETTE_TYP`
--

DROP TABLE IF EXISTS `AUSSCHREIBUNG_KETTE_TYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUSSCHREIBUNG_KETTE_TYP` (
                                           `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                           `Name` varchar(50) NOT NULL,
                                           `Aktiv` int(1) unsigned NOT NULL DEFAULT 0,
                                           `Sortierung` int(3) unsigned DEFAULT NULL,
                                           `Zaehler` int(3) unsigned NOT NULL DEFAULT 0,
                                           `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                           `ErstellungsBenutzer` char(35) NOT NULL,
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_german1_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUSSCHREIBUNG_ZAHLUNGSDATEN`
--

DROP TABLE IF EXISTS `AUSSCHREIBUNG_ZAHLUNGSDATEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUSSCHREIBUNG_ZAHLUNGSDATEN` (
                                               `AUSSCHREIBUNG_ASnr` int(6) unsigned NOT NULL,
                                               `massnahmennebenkosten` decimal(15,2) DEFAULT NULL,
                                               `preis_ergaenzung` decimal(15,2) DEFAULT NULL,
                                               `subunternehmer` decimal(15,2) DEFAULT NULL,
                                               `endabrechnung_datum` date DEFAULT NULL,
                                               `endabrechnung_bemerkung` text DEFAULT NULL,
                                               `endabrechnung_betrag` decimal(15,2) DEFAULT NULL,
                                               `endabrechnung_projektpreis` decimal(15,2) DEFAULT NULL,
                                               `endabrechnung_mnk` decimal(15,2) DEFAULT NULL,
                                               `endabrechnung_betrag_bewilligt` decimal(15,2) DEFAULT NULL,
                                               `entwurf_faellig_am` date DEFAULT NULL,
                                               `abgabe_kunde` date DEFAULT NULL,
                                               `altern_abgabefrist` date DEFAULT NULL,
                                               PRIMARY KEY (`AUSSCHREIBUNG_ASnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Abrechnungsmodus`
--

DROP TABLE IF EXISTS `Abrechnungsmodus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Abrechnungsmodus` (
                                    `ABID` int(11) NOT NULL,
                                    `ABBezeichnung` varchar(45) NOT NULL,
                                    PRIMARY KEY (`ABID`),
                                    UNIQUE KEY `ABID_UNIQUE` (`ABID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='für Aufträge';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BB_Master_Kurse`
--

DROP TABLE IF EXISTS `BB_Master_Kurse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BB_Master_Kurse` (
                                   `BB_Master_Kurse_KEY` varchar(256) NOT NULL,
                                   `BB_Master_Kurse_Bezeichnung` varchar(256) NOT NULL,
                                   PRIMARY KEY (`BB_Master_Kurse_KEY`),
                                   UNIQUE KEY `BB_Master_Kurse_KEY_UNIQUE` (`BB_Master_Kurse_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BELEGUNG`
--

DROP TABLE IF EXISTS `BELEGUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BELEGUNG` (
                            `BKnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'lfd Nummer',
                            `BK_SMnr` int(6) DEFAULT NULL COMMENT 'Seminar-Nummer',
                            `BKtyp` enum('sm','tr','ra','rs') DEFAULT NULL COMMENT 'Belegungstyp (Seminar, Trainer, Raum, Ressource)',
                            `BKart` int(6) DEFAULT NULL COMMENT 'Belegungsart',
                            `BKdatumvon` date DEFAULT NULL COMMENT 'Datum von',
                            `BKdatumbis` date DEFAULT NULL COMMENT 'Datum bis',
                            `BKeinheiten` int(6) DEFAULT NULL COMMENT 'Einheiten in Std.',
                            `BKnormwoche1` int(6) DEFAULT NULL COMMENT 'Normwoche 1',
                            `BKnormwoche2` int(6) DEFAULT NULL COMMENT 'Normwoche 2',
                            `BKnormwochecheck` enum('n','y') DEFAULT NULL,
                            `BKbeschreibung` char(128) DEFAULT NULL COMMENT 'Beschreibung',
                            `BK_SMADnr` int(6) DEFAULT NULL,
                            `BK_SOstandortid` int(6) DEFAULT NULL COMMENT 'Standort-Nummer',
                            `BK_RAraumid` int(6) DEFAULT NULL COMMENT 'Raum-Nummer',
                            `BK_RSresid` int(6) DEFAULT NULL COMMENT 'Ressourcen-Nummer',
                            `BKanzahl` int(3) DEFAULT NULL COMMENT 'Anzahl',
                            `BKersetzt` text DEFAULT NULL COMMENT 'ersetzt Raum/Trainer',
                            `BKstatus` int(1) DEFAULT NULL COMMENT 'Buchungs-Status',
                            `BKloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                            `BKaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                            `BKaeuser` char(35) DEFAULT NULL,
                            `BKerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                            `BKeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`BKnr`)
) ENGINE=InnoDB AUTO_INCREMENT=168306 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BELEGUNG_POS`
--

DROP TABLE IF EXISTS `BELEGUNG_POS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BELEGUNG_POS` (
                                `BPnr` int(12) NOT NULL AUTO_INCREMENT COMMENT 'laufende Belegungsnr',
                                `BKnr` int(6) unsigned NOT NULL COMMENT 'Belegungs-Nummer',
                                `BPSMnr` int(6) NOT NULL COMMENT 'Seminar-Nummer',
                                `BPworkshop_id` int(6) unsigned DEFAULT NULL,
                                `BPtyp` enum('sm','tr','ra','rs') DEFAULT NULL COMMENT 'Belegungstyp',
                                `BPart` int(6) DEFAULT NULL COMMENT 'Belegungsart',
                                `BPdatum` date DEFAULT NULL COMMENT 'Datum',
                                `BPzeitvon` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Zeit von',
                                `BPzeitbis` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Zeit bis',
                                `BPeinheiten` decimal(4,2) DEFAULT NULL COMMENT 'Anzahl Einheiten',
                                `BPbeschreibung` char(20) DEFAULT NULL COMMENT 'Beschreibung',
                                `BPurlaub` enum('y','n') DEFAULT 'n' COMMENT 'Urlaubskennzeichnung',
                                `BP_SMADnr` int(6) DEFAULT NULL,
                                `BP_SOstandortid` int(6) DEFAULT NULL COMMENT 'Standort-Nummer',
                                `BP_RAraumid` int(6) DEFAULT NULL COMMENT 'Raum-Nummer',
                                `BP_RSresid` int(6) DEFAULT NULL COMMENT 'Ressourcen-Nummer',
                                `BPaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                `BPaeuser` char(35) DEFAULT NULL,
                                `BPerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                `BPeruser` char(35) DEFAULT NULL,
                                PRIMARY KEY (`BPnr`,`BKnr`),
                                KEY `BELEGUNG_POS_BKnr` (`BKnr`),
                                KEY `BELEGUNG_POS_WORKSHOP_id` (`BPworkshop_id`),
                                KEY `BELEGUNG_POS_Typ` (`BPtyp`),
                                KEY `BELEGUNG_POS_SMnr` (`BPSMnr`)
) ENGINE=MyISAM AUTO_INCREMENT=22589541 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BENUTZER`
--

DROP TABLE IF EXISTS `BENUTZER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BENUTZER` (
                            `BNid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Userid',
                            `BNadID` varchar(45) DEFAULT NULL,
                            `BNadSAMAN` varchar(45) DEFAULT NULL,
                            `BNupn` varchar(45) DEFAULT NULL,
                            `BNuserid` varchar(35) DEFAULT NULL COMMENT 'Userlogin',
                            `BNpwd` varchar(255) DEFAULT NULL COMMENT 'Benutzer-Passwort-Hash',
                            `BNpwd_mustchange` tinyint(1) unsigned NOT NULL DEFAULT 0,
                            `BNtyp` enum('IBOS','TC') NOT NULL DEFAULT 'IBOS' COMMENT 'Für welche Anwendung ist der Benutzer',
                            `BNaktivbis` date DEFAULT NULL COMMENT 'aktiv bis Datum',
                            `BNbemerk` text DEFAULT NULL COMMENT 'Interne Bemerkungen',
                            `BNlistlg` int(3) unsigned DEFAULT 3 COMMENT 'Listenlaenge Benutzereinstellung',
                            `BNreiteransicht` enum('einzeilig','mehrzeilig') DEFAULT 'mehrzeilig',
                            `BNlastlogin` datetime DEFAULT NULL,
                            `BNlastAktiv` datetime DEFAULT NULL COMMENT 'Letzte Aktivität',
                            `BNadnr` int(6) DEFAULT 0,
                            `BNmailsent` datetime DEFAULT NULL COMMENT 'Datum der automatischen E-Mail sendung',
                            `BNfailmessage` text DEFAULT NULL COMMENT 'Fehlermeldung falls vorhanden',
                            `BNloek` enum('n','y') DEFAULT 'n' COMMENT 'Loeschkennzeichen',
                            `BNaeda` datetime DEFAULT NULL,
                            `BNaeuser` char(35) DEFAULT NULL COMMENT 'Änderungsbenutzer',
                            `BNerda` datetime NOT NULL COMMENT 'Erstellungsdatum',
                            `BNeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                            PRIMARY KEY (`BNid`),
                            UNIQUE KEY `BNadSAMAN_UNIQUE` (`BNadSAMAN`)
) ENGINE=InnoDB AUTO_INCREMENT=13006 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BENUTZER2`
--

DROP TABLE IF EXISTS `BENUTZER2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BENUTZER2` (
                             `BNid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Userid',
                             `BNuserid` varchar(35) NOT NULL COMMENT 'Userlogin',
                             `BNpwd` varchar(32) NOT NULL DEFAULT '' COMMENT 'BENUTZER2-Passwort MD5-Verschluesselt',
                             `BNtyp` enum('IBOS','TC') NOT NULL DEFAULT 'IBOS' COMMENT 'Für welche Anwendung ist der BENUTZER2',
                             `BNaktivbis` date DEFAULT NULL COMMENT 'aktiv bis Datum',
                             `BNbemerk` text DEFAULT NULL COMMENT 'Interne Bemerkungen',
                             `BNlistlg` int(3) unsigned DEFAULT 3 COMMENT 'Listenlaenge BENUTZER2einstellung',
                             `BNreiteransicht` enum('einzeilig','mehrzeilig') DEFAULT 'mehrzeilig',
                             `BNlastlogin` datetime /* mariadb-5.3 */ DEFAULT NULL,
                             `BNlastAktiv` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Letzte Aktivität',
                             `BNadnr` int(6) DEFAULT NULL,
                             `BNmailsent` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum der automatischen E-Mail sendung',
                             `BNfailmessage` text DEFAULT NULL COMMENT 'Fehlermeldung falls vorhanden',
                             `BNloek` enum('n','y') DEFAULT 'n' COMMENT 'Loeschkennzeichen',
                             `BNaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                             `BNaeuser` char(35) DEFAULT NULL COMMENT 'ÄnderungsBENUTZER2',
                             `BNerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                             `BNeruser` char(35) NOT NULL COMMENT 'ErstellungsBENUTZER2',
                             PRIMARY KEY (`BNid`),
                             UNIQUE KEY `BNuserid` (`BNuserid`)
) ENGINE=InnoDB AUTO_INCREMENT=9231 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BENUTZER_BACKUP`
--

DROP TABLE IF EXISTS `BENUTZER_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BENUTZER_BACKUP` (
                                   `BNid` int(10) unsigned NOT NULL DEFAULT 0 COMMENT 'Userid',
                                   `BNuserid` varchar(35) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL COMMENT 'Userlogin'
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BERUFELISTE`
--

DROP TABLE IF EXISTS `BERUFELISTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BERUFELISTE` (
                               `ID` int(6) NOT NULL AUTO_INCREMENT,
                               `beruf` varchar(255) DEFAULT NULL,
                               `synonymvon` int(6) NOT NULL,
                               `isfavorit` tinyint(1) NOT NULL,
                               PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=2130 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BERUFE_NEU`
--

DROP TABLE IF EXISTS `BERUFE_NEU`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BERUFE_NEU` (
                              `id` int(5) NOT NULL AUTO_INCREMENT,
                              `beruf` varchar(255) DEFAULT NULL,
                              `found` int(1) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2090 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BN_AB`
--

DROP TABLE IF EXISTS `BN_AB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BN_AB` (
                         `BENUTZER_BNid` int(10) unsigned NOT NULL COMMENT 'Userid',
                         `BENUTZER_BNuserid` varchar(15) NOT NULL DEFAULT '',
                         `ABTEILUNG_ABnr` int(6) unsigned NOT NULL DEFAULT 0,
                         `BNABerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `BNABeruser` char(35) DEFAULT NULL,
                         KEY `BN_AB_FKIndex1` (`BENUTZER_BNuserid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BUNDESLAND`
--

DROP TABLE IF EXISTS `BUNDESLAND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BUNDESLAND` (
                              `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                              `bezeichnung` char(200) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CC_FAQ`
--

DROP TABLE IF EXISTS `CC_FAQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CC_FAQ` (
                          `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Eindeutige ID einer Frage',
                          `Frage` varchar(150) DEFAULT NULL COMMENT 'Die Frage selbst',
                          `loek` enum('n','y') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT 'n' COMMENT 'Löschkennzeichen',
                          `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                          `aeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Änderungsbenutzer',
                          `erda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                          `eruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL COMMENT 'Erstellungsbenutzer',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 COLLATE=latin1_german1_ci COMMENT='Tabelle für FAQ (Frequently Asked Questions';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CC_PROJEKT`
--

DROP TABLE IF EXISTS `CC_PROJEKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CC_PROJEKT` (
                              `PJnr` int(11) NOT NULL COMMENT 'PROJEKT.PJnr',
                              `Hinweis` text DEFAULT NULL COMMENT 'Hinweis zur Erreichbarkeit und wo wer aktiv ist',
                              `Beschreibung` text DEFAULT NULL COMMENT 'Grundinformation zur Erstbeauskunftung',
                              `Besonderheiten` text DEFAULT NULL COMMENT 'Besonderheiten die vom Call Center zu beachten sind\n',
                              `Zielgruppe` text DEFAULT NULL COMMENT 'Für welche Zielgruppe ist dieses Projekt gedacht',
                              `Infotag` varchar(150) DEFAULT NULL COMMENT 'Wann ist Infotag',
                              `Email` varchar(150) DEFAULT NULL COMMENT 'E-Mailadresse die zusätzlich zum kontaktieren verwendet wird.',
                              `Mo` text DEFAULT NULL COMMENT 'Wann läuft der Kurs Montags FORMAT: von-bis||von-bis||von-bis',
                              `Di` text DEFAULT NULL COMMENT 'Wann läuft der Kurs Dienstags FORMAT: von-bis||von-bis||von-bis',
                              `Mi` text DEFAULT NULL COMMENT 'Wann läuft der Kurs Mittwochs FORMAT: von-bis||von-bis||von-bis',
                              `Do` text DEFAULT NULL COMMENT 'Wann läuft der Kurs Donnerstags FORMAT: von-bis||von-bis||von-bis',
                              `Fr` text DEFAULT NULL COMMENT 'Wann läuft der Kurs Freitags FORMAT: von-bis||von-bis||von-bis',
                              `Sa` text DEFAULT NULL COMMENT 'Wann läuft der Kurs Samstags FORMAT: von-bis||von-bis||von-bis',
                              `So` text DEFAULT NULL COMMENT 'Wann läuft der Kurs Sonntags FORMAT: von-bis||von-bis||von-bis',
                              `Dateiname` text DEFAULT NULL COMMENT 'Dateinamen von der dieser Eintrag importiert wurde, wird nur intern benutzt',
                              `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `aeuser` char(35) DEFAULT NULL COMMENT 'Änderungsbenutzer',
                              `erda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                              `eruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                              PRIMARY KEY (`PJnr`),
                              UNIQUE KEY `PJnr_UNIQUE` (`PJnr`),
                              KEY `FOREIGN_PROJEKT_CC_PJnr` (`PJnr`),
                              CONSTRAINT `FOREIGN_CC_PROJEKT_PROJEKT_PJnr` FOREIGN KEY (`PJnr`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Tabelle für Call Center Informationen';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CC_PROJEKT_ANSPRECHPARTNER`
--

DROP TABLE IF EXISTS `CC_PROJEKT_ANSPRECHPARTNER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CC_PROJEKT_ANSPRECHPARTNER` (
                                              `PJnr` int(11) NOT NULL COMMENT 'PROJEKT.PJnr',
                                              `Ansprechpartner` int(10) unsigned NOT NULL COMMENT 'ADRESSE.ADadnr',
                                              `Ordnungsnummer` int(10) unsigned NOT NULL COMMENT 'An wievielter Stelle soll diese Person kontaktiert werden',
                                              `erda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                              `eruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                              PRIMARY KEY (`PJnr`,`Ansprechpartner`),
                                              KEY `FOREIGN_PROJEKT_CC_ANSPRECHPARTNER_PJnr` (`PJnr`),
                                              CONSTRAINT `FOREIGN_CC_PROJEKT_ANSPRECHPARTNER_PROJEKT_PJnr` FOREIGN KEY (`PJnr`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Tabelle mit Ansprechpartnern eines Projekts';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CC_PROJEKT_ANTWORT`
--

DROP TABLE IF EXISTS `CC_PROJEKT_ANTWORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CC_PROJEKT_ANTWORT` (
                                      `PJnr` int(11) NOT NULL COMMENT 'PROJEKT.PJnr',
                                      `FAQ_id` int(10) unsigned NOT NULL COMMENT 'CC_FAQ.id',
                                      `Antwort` text DEFAULT NULL COMMENT 'Antwort zur Frage',
                                      `Aktion` text DEFAULT NULL COMMENT 'Aktion die vom Call Center durchgeführt werden soll',
                                      `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                      `aeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Änderungsbenutzer',
                                      `erda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                      `eruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL COMMENT 'Erstellungsbenutzer',
                                      PRIMARY KEY (`PJnr`,`FAQ_id`),
                                      KEY `FOREIGN_CC_PROJEKT_ANTWORTEN_PROJEKT_PJnr` (`PJnr`),
                                      KEY `FOREIGN_CC_PROJEKT_ANTWORTEN_CC_FAQ_id` (`FAQ_id`),
                                      CONSTRAINT `FOREIGN_CC_PROJEKT_ANTWORTEN_CC_FAQ_id` FOREIGN KEY (`FAQ_id`) REFERENCES `CC_FAQ` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `FOREIGN_CC_PROJEKT_ANTWORTEN_PROJEKT_PJnr` FOREIGN KEY (`PJnr`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german1_ci COMMENT='Tabelle in der die Antworten zu den FAQ stehen';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CC_PROJEKT_FACHBEREICH`
--

DROP TABLE IF EXISTS `CC_PROJEKT_FACHBEREICH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CC_PROJEKT_FACHBEREICH` (
                                          `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Eindeutige ID eines Fachbereichs',
                                          `PJnr` int(11) NOT NULL COMMENT 'PROJEKT.PJnr',
                                          `Bezeichnung` varchar(150) DEFAULT NULL COMMENT 'Bezeichnung des Fachbereichs',
                                          `von` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Begindatum des Fachbereichs',
                                          `bis` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Enddatum des Fachbereichs',
                                          `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                          `aeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Änderungsbenutzer',
                                          `erda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                          `eruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL COMMENT 'Erstellungsbenutzer',
                                          PRIMARY KEY (`id`),
                                          KEY `FOREIGN_PROJEKT_CC_FACHBEREICH_PJnr` (`PJnr`),
                                          CONSTRAINT `FOREIGN_CC_PROJEKT_FACHBEREICH_PROJEKT_PJnr` FOREIGN KEY (`PJnr`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=437 DEFAULT CHARSET=latin1 COLLATE=latin1_german1_ci COMMENT='Tabelle in der die Fachbereiche des Seminar gespeichert werd';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_BERUF`
--

DROP TABLE IF EXISTS `CRM_BERUF`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_BERUF` (
                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                             `KYnr` int(6) unsigned DEFAULT NULL COMMENT 'KEYTABLE.KYnr where KYname=TZberufsbereich1: Die alten ids der bisher verwendeten Branchen (nicht verwendet, existiert nur sicherheitshalber)',
                             `bezeichnung` varchar(100) NOT NULL,
                             `parentKYnr` int(10) DEFAULT NULL COMMENT 'KEYTABLE.KYvaluenum1 where KYname = TZberufsberich2: Oberbranche gespeichert in KEYTABLE TZberufsbereich1 (nicht verwendet, existiert nur sicherheitshalber',
                             PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=219 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Eine Branche, die eine CRM_PRAKTIKUM betrifft';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_CF_CN`
--

DROP TABLE IF EXISTS `CRM_CF_CN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_CF_CN` (
                             `CRM_FIRMA_id` int(6) unsigned DEFAULT NULL COMMENT 'CRM_FIRMA.CFnr',
                             `CRM_NOTIZ_id` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'CRM_NOTIZ.CNnr',
                             PRIMARY KEY (`CRM_NOTIZ_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Notizen, die Firmen und|oder Mitarbeiter betreffen';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_CM_CN`
--

DROP TABLE IF EXISTS `CRM_CM_CN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_CM_CN` (
                             `CRM_MITARBEITER_id` int(6) unsigned DEFAULT NULL COMMENT 'CRM_MITARBEITER.CMnr',
                             `CRM_NOTIZ_id` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'CRM_NOTIZ.CNnr',
                             PRIMARY KEY (`CRM_NOTIZ_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Notizen, die Firmen und|oder Mitarbeiter betreffen';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_FIRMA`
--

DROP TABLE IF EXISTS `CRM_FIRMA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_FIRMA` (
                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                             `idParent` int(6) unsigned DEFAULT NULL,
                             `adnr_old` int(6) unsigned DEFAULT NULL,
                             `typ` enum('u','f','g') NOT NULL COMMENT 'u: Unternehmen, f: Filiale, g: Geschäfststelle',
                             `herold_firma_id` int(6) unsigned DEFAULT NULL,
                             `name` text NOT NULL,
                             `strasse` varchar(50) NOT NULL,
                             `plz` varchar(6) NOT NULL,
                             `ort` varchar(50) NOT NULL,
                             `telefon1` varchar(20) NOT NULL,
                             `telefon2` varchar(20) DEFAULT NULL,
                             `faxnummer` varchar(20) DEFAULT NULL,
                             `email` varchar(80) DEFAULT NULL,
                             `CRM_KATEGORIE_id` int(6) unsigned NOT NULL COMMENT 'CRM_KATEGORIE.KATnr: Bestimmt die Zugriffsrecht auf diesen Eintrag',
                             `CRM_BRANCHE_id` int(6) unsigned NOT NULL COMMENT 'CRM_FIRMENBRANCHE.id: Die Branche, in der die ser Eintrag tätig',
                             `bankBezeichnung` varchar(40) DEFAULT NULL,
                             `bankBIC` varchar(11) DEFAULT NULL,
                             `bankIBAN` varchar(34) DEFAULT NULL,
                             `betreuerADadnr` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr: Derzeit verantworlicher für diesen Eintrag',
                             `bemerkung` text DEFAULT NULL,
                             `aufnahmekriterien` text DEFAULT NULL COMMENT 'Kriterien, die für eine Aufnahme als Praktikant zu tragen kommen',
                             `loek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                             `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                             `aeuser` char(35) DEFAULT NULL,
                             `erda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                             `eruser` char(35) DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             KEY `herold_nr` (`herold_firma_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30128 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Ein CRM-firmeneintrag. Kann Unternehmen, Filiale oder Geschä';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_FIRMA_COPY`
--

DROP TABLE IF EXISTS `CRM_FIRMA_COPY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_FIRMA_COPY` (
                                  `id` int(6) unsigned NOT NULL DEFAULT 0,
                                  `idParent` int(6) unsigned DEFAULT NULL,
                                  `typ` enum('u','f','g') CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL COMMENT 'u: Unternehmen, f: Filiale, g: Geschäfststelle',
                                  `herold_firma_id` int(6) unsigned DEFAULT NULL,
                                  `name` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL,
                                  `strasse` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL,
                                  `plz` varchar(6) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL,
                                  `ort` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL,
                                  `telefon1` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL,
                                  `telefon2` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                  `faxnummer` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                  `email` varchar(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                  `CRM_KATEGORIE_id` int(6) unsigned NOT NULL COMMENT 'CRM_KATEGORIE.KATnr: Bestimmt die Zugriffsrecht auf diesen Eintrag',
                                  `CRM_BRANCHE_id` int(6) unsigned NOT NULL COMMENT 'CRM_FIRMENBRANCHE.id: Die Branche, in der die ser Eintrag tätig',
                                  `bankBezeichnung` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                  `bankBIC` varchar(11) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                  `bankIBAN` varchar(34) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                  `betreuerADadnr` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr: Derzeit verantworlicher für diesen Eintrag',
                                  `bemerkung` text CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                  `aufnahmekriterien` text CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Kriterien, die für eine Aufnahme als Praktikant zu tragen kommen',
                                  `loek` enum('n','y') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT 'n' COMMENT 'Löschkennzeichen',
                                  `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                  `aeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                  `erda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                  `eruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_FIRMENBRANCHE`
--

DROP TABLE IF EXISTS `CRM_FIRMENBRANCHE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_FIRMENBRANCHE` (
                                     `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                     `bezeichnung` varchar(100) NOT NULL,
                                     `kategorieId` int(10) DEFAULT NULL COMMENT 'CRM_FIRMENBRANCHE_KATEGORIE.id',
                                     PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=120 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Eine Branche, die eine CRM_PRAKTIKUM betrifft';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_FIRMENBRANCHE_KATEGORIE`
--

DROP TABLE IF EXISTS `CRM_FIRMENBRANCHE_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_FIRMENBRANCHE_KATEGORIE` (
                                               `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                               `bezeichnung` varchar(100) NOT NULL,
                                               PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Eine Kategorier einer Branche, in der eine CRM_FIRMA tätig i';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_KATEGORIE`
--

DROP TABLE IF EXISTS `CRM_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_KATEGORIE` (
                                 `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                 `bezeichnung` varchar(250) NOT NULL,
                                 `rechteLevel` int(2) unsigned NOT NULL COMMENT 'Definert wie hoch diese Kategorie als Zugriffsberechtigung eingestuft ist',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `bezeichnung` (`bezeichnung`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Kategorien, die die Zugriffsrechte bestimmen';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_KONTAKT`
--

DROP TABLE IF EXISTS `CRM_KONTAKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_KONTAKT` (
                               `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                               `CRM_FIRMA_id` int(6) unsigned DEFAULT NULL,
                               `datum` date DEFAULT NULL,
                               `uhrzeit` time /* mariadb-5.3 */ DEFAULT NULL,
                               `kontaktperson` int(6) unsigned DEFAULT NULL COMMENT 'CRM_MITARBEITER.id',
                               `kontaktierende_person` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                               `altern_verantwortlicher` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                               `art` int(6) unsigned DEFAULT NULL COMMENT 'CRM_KONTAKT_ART.id',
                               `betreff` char(100) DEFAULT NULL,
                               `thema` text DEFAULT NULL,
                               `ansicht` enum('alle','bk') DEFAULT 'alle' COMMENT 'Ansicht Alle oder Betriebskontakter',
                               `loek` enum('n','y') DEFAULT 'n',
                               `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `eruser` char(35) DEFAULT NULL,
                               `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `aeuser` char(35) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1463 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_KONTAKT_ART`
--

DROP TABLE IF EXISTS `CRM_KONTAKT_ART`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_KONTAKT_ART` (
                                   `id` int(6) unsigned NOT NULL,
                                   `bezeichnung` char(100) DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_MITARBEITER`
--

DROP TABLE IF EXISTS `CRM_MITARBEITER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_MITARBEITER` (
                                   `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                   `idFirma` int(6) unsigned NOT NULL COMMENT 'Ein Mitarbeiter ist einer CRM_FIRMA zugewiesen',
                                   `idAnrede` int(6) unsigned DEFAULT NULL COMMENT 'KEYTABLE KYname ANREDE',
                                   `idTitel` int(6) unsigned DEFAULT NULL COMMENT 'KEYTABLE where KYname = ''TITEL''',
                                   `vorname` varchar(50) NOT NULL,
                                   `nachname` varchar(50) NOT NULL,
                                   `tel` varchar(20) DEFAULT NULL,
                                   `mobil` varchar(20) DEFAULT NULL,
                                   `position` varchar(200) DEFAULT NULL,
                                   `fax` varchar(20) DEFAULT NULL,
                                   `email` varchar(80) DEFAULT NULL,
                                   `betreuerADadnr` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr: Derzeit verantworlicher für diesen Eintrag',
                                   `loek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                                   `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                   `aeuser` char(35) DEFAULT NULL,
                                   `erda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                   `eruser` char(35) DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3468 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Mitarbeiter einer CRM_FIRMA, Kontaktperson';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_NOTIZ`
--

DROP TABLE IF EXISTS `CRM_NOTIZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_NOTIZ` (
                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                             `ueberschrift` varchar(250) NOT NULL,
                             `notiztext` text NOT NULL,
                             `CRM_KATEGORIE_id` int(6) unsigned NOT NULL DEFAULT 3 COMMENT 'FK CRM_KATEGORIE.id',
                             `loek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                             `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                             `aeuser` char(35) DEFAULT NULL,
                             `erda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                             `eruser` char(35) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1485 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Notizen, die Firmen und|oder Mitarbeiter betreffen';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_OLD`
--

DROP TABLE IF EXISTS `CRM_OLD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_OLD` (
                           `ADadnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Adress-Nummer',
                           `ADfadnr` int(6) unsigned DEFAULT 0 COMMENT 'Adressnummer der Firma (=Referenz auf Firma)',
                           `ADibis_gs` int(6) unsigned DEFAULT NULL COMMENT 'ibis Geschäftsstelle',
                           `ADibis_gseinsatz` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                           `ADadrtype` enum('p','f') CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL DEFAULT 'p' COMMENT 'Person oder Firma',
                           `ADanrede` int(6) unsigned DEFAULT NULL COMMENT 'Anrede-Code',
                           `ADtitel` int(6) unsigned DEFAULT NULL COMMENT 'Titel-Code',
                           `ADtitelv` int(6) unsigned DEFAULT NULL COMMENT 'Titel-Code verliehen',
                           `ADznf1` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Zuname, Firmenname1',
                           `ADvnf2` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Vorname, Firmenname 2',
                           `ADsube` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Suchbegriff',
                           `ADstrasse` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Anschrift 1, Strasse',
                           `ADstrobjekt` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Anschrift 2, Objekt',
                           `ADfsb` int(3) unsigned zerofill DEFAULT NULL COMMENT 'Feststellbescheid (Grad der Behinderung)',
                           `ADlkz` varchar(6) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'LKZ',
                           `ADplz` varchar(6) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'PLZ',
                           `ADort` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Ort',
                           `ADbundesland` int(6) unsigned DEFAULT NULL COMMENT 'Bundesland',
                           `ADzukz` enum('p','f','h','c') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT 'p' COMMENT 'Zusende-Kennzeichen (Privat, Firma, zHd, c/o)',
                           `ADtelp` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Telefon Privat mit Vorwahl',
                           `ADfaxp` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Fax Privat mit Vorwahl',
                           `ADtelf` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Telefon Firma mit Vorwahl',
                           `ADfaxf` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Fax Firma mit Vorwahl',
                           `ADsipnr` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'SIP-Nummer',
                           `ADmobil1` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Mobil Nummer 1',
                           `ADmobil1Besitzer` varchar(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                           `ADmobil2` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Mobil Nummer 2',
                           `ADmobil2Besitzer` varchar(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                           `ADemail1` varchar(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'EMail Adresse 1',
                           `ADemail2` varchar(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'EMail Adresse 2',
                           `ADinternet` varchar(150) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Internet-Adresse / Homepage',
                           `ADgebdatum` date DEFAULT NULL COMMENT 'Geburtsdatum',
                           `ADgebdatumf` int(1) unsigned DEFAULT 0 COMMENT 'unbekannter Geburtstag',
                           `ADgebort` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Geburtsort',
                           `ADgebland` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Geburtsland',
                           `ADstaatsb` int(6) DEFAULT NULL,
                           `ADfamstand` int(6) unsigned DEFAULT NULL COMMENT 'Familien-Stand',
                           `ADerstkontaktAm` date DEFAULT NULL,
                           `ADbewerbungAm` date DEFAULT NULL,
                           `ADpersnr` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Personal-Nummer',
                           `ADsvnr` varchar(11) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Sozialversicherungsnummer, 1234 ttmmjj',
                           `ADversicherung` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Versicherung',
                           `ADmitversichertbei` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'mitversichert bei',
                           `ADErzBe` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'ErziehungsberechtigteR',
                           `ADErzBeTel` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Telefonnummer: Erziehungsberechtigter',
                           `ADErzBeMail` varchar(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Email: Erziehungsberechtigter',
                           `ADVorM` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Vormund',
                           `ADVorMTel` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Telefonnummer: Vormund',
                           `ADVorMMail` varchar(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Email: Vormund',
                           `ADarbeitsgenehmigung` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Arbeitsgenehmigung',
                           `ADarbeitsgenbis` date DEFAULT NULL COMMENT 'Arbeitsgenehmigung bis',
                           `ADfuehrerschein` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Führerscheine',
                           `ADpruefungen` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Prüfungen',
                           `ADberuf` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Beruf',
                           `ADfunktion` int(6) DEFAULT NULL,
                           `ADkreditornr` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Kreditoren-Nummer',
                           `ADbank` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bankverbindung',
                           `ADbankblz` varchar(6) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bankleitzahl',
                           `ADbankkonto` varchar(12) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bank-Konto',
                           `ADbankiban` varchar(34) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bank IBAN-Nummer',
                           `ADbankbic` varchar(11) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bank BIC',
                           `ADfbnr` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Firmenbuch-Nummer',
                           `ADfbnrgericht` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Firmenbuch Gericht',
                           `ADsteuernr` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Steuernummer',
                           `ADfinanzamt` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Finanzamt',
                           `ADuid` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Umsatzsteuernummer',
                           `ADbemerkung` text CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bemerkung',
                           `ADnoml` enum('n','y') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT 'n' COMMENT 'No-Mail-Kennzeichen',
                           `ADfoto` varchar(60) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Foto',
                           `ADbildLink` text CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Nur für Trainer und PKs. Link zu einem Bild, das auf der Hompage angezeigt wird',
                           `ADkategorie` int(6) DEFAULT NULL,
                           `ADgewerbeschein` int(1) DEFAULT NULL,
                           `ADowner` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Eigentümer der Adresse',
                           `ADberecht` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Berechtigungsliste',
                           `ADquelle` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Adress-Quelle',
                           `ADprda` date DEFAULT NULL,
                           `ADpruser` varchar(15) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                           `ADstatus` char(1) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL DEFAULT 'a',
                           `ADuserid` varchar(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                           `ADpwd` varchar(32) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                           `ADtcstatus` enum('aktiv','inaktiv') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                           `ADsettinglistlg` int(3) DEFAULT NULL,
                           `ADlastlogin` datetime /* mariadb-5.3 */ DEFAULT NULL,
                           `ADloek` enum('n','y') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT 'n' COMMENT 'Löschkennzeichn',
                           `ADaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                           `ADaeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                           `ADerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                           `ADeruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                           PRIMARY KEY (`ADadnr`)
) ENGINE=MyISAM AUTO_INCREMENT=66256 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_OLD_COPY`
--

DROP TABLE IF EXISTS `CRM_OLD_COPY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_OLD_COPY` (
                                `ADadnr` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'Adress-Nummer',
                                `ADfadnr` int(6) unsigned DEFAULT 0 COMMENT 'Adressnummer der Firma (=Referenz auf Firma)',
                                `ADibis_gs` int(6) unsigned DEFAULT NULL COMMENT 'ibis Geschäftsstelle',
                                `ADibis_gseinsatz` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `ADadrtype` enum('p','f') CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL DEFAULT 'p' COMMENT 'Person oder Firma',
                                `ADanrede` int(6) unsigned DEFAULT NULL COMMENT 'Anrede-Code',
                                `ADtitel` int(6) unsigned DEFAULT NULL COMMENT 'Titel-Code',
                                `ADtitelv` int(6) unsigned DEFAULT NULL COMMENT 'Titel-Code verliehen',
                                `ADznf1` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Zuname, Firmenname1',
                                `ADvnf2` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Vorname, Firmenname 2',
                                `ADsube` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Suchbegriff',
                                `ADstrasse` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Anschrift 1, Strasse',
                                `ADstrobjekt` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Anschrift 2, Objekt',
                                `ADfsb` int(3) unsigned zerofill DEFAULT NULL COMMENT 'Feststellbescheid (Grad der Behinderung)',
                                `ADlkz` varchar(6) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'LKZ',
                                `ADplz` varchar(6) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'PLZ',
                                `ADort` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Ort',
                                `ADbundesland` int(6) unsigned DEFAULT NULL COMMENT 'Bundesland',
                                `ADzukz` enum('p','f','h','c') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT 'p' COMMENT 'Zusende-Kennzeichen (Privat, Firma, zHd, c/o)',
                                `ADtelp` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Telefon Privat mit Vorwahl',
                                `ADfaxp` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Fax Privat mit Vorwahl',
                                `ADtelf` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Telefon Firma mit Vorwahl',
                                `ADfaxf` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Fax Firma mit Vorwahl',
                                `ADsipnr` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'SIP-Nummer',
                                `ADmobil1` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Mobil Nummer 1',
                                `ADmobil1Besitzer` varchar(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `ADmobil2` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Mobil Nummer 2',
                                `ADmobil2Besitzer` varchar(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `ADemail1` varchar(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'EMail Adresse 1',
                                `ADemail2` varchar(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'EMail Adresse 2',
                                `ADinternet` varchar(150) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Internet-Adresse / Homepage',
                                `ADgebdatum` date DEFAULT NULL COMMENT 'Geburtsdatum',
                                `ADgebdatumf` int(1) unsigned DEFAULT 0 COMMENT 'unbekannter Geburtstag',
                                `ADgebort` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Geburtsort',
                                `ADgebland` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Geburtsland',
                                `ADstaatsb` int(6) DEFAULT NULL,
                                `ADfamstand` int(6) unsigned DEFAULT NULL COMMENT 'Familien-Stand',
                                `ADerstkontaktAm` date DEFAULT NULL,
                                `ADbewerbungAm` date DEFAULT NULL,
                                `ADpersnr` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Personal-Nummer',
                                `ADsvnr` varchar(11) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Sozialversicherungsnummer, 1234 ttmmjj',
                                `ADversicherung` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Versicherung',
                                `ADmitversichertbei` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'mitversichert bei',
                                `ADErzBe` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'ErziehungsberechtigteR',
                                `ADErzBeTel` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Telefonnummer: Erziehungsberechtigter',
                                `ADErzBeMail` varchar(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Email: Erziehungsberechtigter',
                                `ADVorM` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Vormund',
                                `ADVorMTel` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Telefonnummer: Vormund',
                                `ADVorMMail` varchar(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Email: Vormund',
                                `ADarbeitsgenehmigung` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Arbeitsgenehmigung',
                                `ADarbeitsgenbis` date DEFAULT NULL COMMENT 'Arbeitsgenehmigung bis',
                                `ADfuehrerschein` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Führerscheine',
                                `ADpruefungen` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Prüfungen',
                                `ADberuf` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Beruf',
                                `ADfunktion` int(6) DEFAULT NULL,
                                `ADkreditornr` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Kreditoren-Nummer',
                                `ADbank` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bankverbindung',
                                `ADbankblz` varchar(6) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bankleitzahl',
                                `ADbankkonto` varchar(12) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bank-Konto',
                                `ADbankiban` varchar(34) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bank IBAN-Nummer',
                                `ADbankbic` varchar(11) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bank BIC',
                                `ADfbnr` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Firmenbuch-Nummer',
                                `ADfbnrgericht` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Firmenbuch Gericht',
                                `ADsteuernr` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Steuernummer',
                                `ADfinanzamt` varchar(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Finanzamt',
                                `ADuid` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Umsatzsteuernummer',
                                `ADbemerkung` text CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Bemerkung',
                                `ADnoml` enum('n','y') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT 'n' COMMENT 'No-Mail-Kennzeichen',
                                `ADfoto` varchar(60) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Foto',
                                `ADbildLink` text CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Nur für Trainer und PKs. Link zu einem Bild, das auf der Hompage angezeigt wird',
                                `ADkategorie` int(6) DEFAULT NULL,
                                `ADgewerbeschein` int(1) DEFAULT NULL,
                                `ADowner` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Eigentümer der Adresse',
                                `ADberecht` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Berechtigungsliste',
                                `ADquelle` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Adress-Quelle',
                                `ADprda` date DEFAULT NULL,
                                `ADpruser` varchar(15) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `ADstatus` char(1) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL DEFAULT 'a',
                                `ADuserid` varchar(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `ADpwd` varchar(32) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `ADtcstatus` enum('aktiv','inaktiv') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `ADsettinglistlg` int(3) DEFAULT NULL,
                                `ADlastlogin` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `ADloek` enum('n','y') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT 'n' COMMENT 'Löschkennzeichn',
                                `ADaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                `ADaeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `ADerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                `ADeruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_OLD_IDS_201111`
--

DROP TABLE IF EXISTS `CRM_OLD_IDS_201111`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_OLD_IDS_201111` (
                                      `ADadnr` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'Adress-Nummer'
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='ADadnr von CRM_OLD Stand 201111. Tabelle wird 201203 gebrauc';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CRM_OLD_NOTIZEN`
--

DROP TABLE IF EXISTS `CRM_OLD_NOTIZEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CRM_OLD_NOTIZEN` (
                                   `NZid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                   `CRM_OLD_ADadnr` int(6) unsigned NOT NULL,
                                   `ABTEILUNG_ABnr` int(3) unsigned NOT NULL,
                                   `NZdatum` date DEFAULT NULL,
                                   `NZwiedervorlage` date DEFAULT NULL,
                                   `NZbenutzer` varchar(15) DEFAULT NULL,
                                   `NZbezeichnung` varchar(40) DEFAULT NULL,
                                   `NZtext` text DEFAULT NULL,
                                   `NZloek` enum('n','y') NOT NULL DEFAULT 'n',
                                   `NZaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `NZaeuser` char(35) DEFAULT NULL,
                                   `NZerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `NZeruser` char(35) DEFAULT NULL,
                                   PRIMARY KEY (`NZid`)
) ENGINE=InnoDB AUTO_INCREMENT=10833 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DIAGNOSETYP`
--

DROP TABLE IF EXISTS `DIAGNOSETYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DIAGNOSETYP` (
                               `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                               `ordnungsnummer` int(6) unsigned DEFAULT NULL,
                               `bezeichnung` varchar(255) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='derzeit nur verwendet in PJ des Typs fit2work';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DIENSTVERTRAG`
--

DROP TABLE IF EXISTS `DIENSTVERTRAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DIENSTVERTRAG` (
                                 `DVnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Dienstvertrags-Nummer',
                                 `ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                                 `PBid` int(6) unsigned DEFAULT NULL COMMENT 'ID des zugeoerigen Personalbogens',
                                 `ARBEITSVERTRAG_id` int(6) unsigned DEFAULT NULL,
                                 `DVkstgr` int(3) DEFAULT NULL,
                                 `DVtyp` enum('frei','fix') DEFAULT NULL,
                                 `DVfirma` varchar(100) DEFAULT NULL,
                                 `DVgewerbeschein` enum('y','n') DEFAULT NULL,
                                 `DVgewerbeart` char(40) DEFAULT NULL,
                                 `DVbehoerde` char(40) DEFAULT NULL,
                                 `DVgszahl` char(20) DEFAULT NULL,
                                 `DVgsgueltigab` date DEFAULT NULL COMMENT 'Gewerbeschein gültig ab',
                                 `DVlohngruppe` int(6) unsigned DEFAULT NULL COMMENT 'Lohngruppe',
                                 `DVlohnstufe` int(6) unsigned DEFAULT NULL COMMENT 'Lohnstufe',
                                 `DVdatumeintritt` date DEFAULT NULL COMMENT 'Datum Eintritt',
                                 `DVdatumaustritt` date DEFAULT NULL COMMENT 'Datum Austritt',
                                 `DVanmeldungdatum` date DEFAULT NULL,
                                 `DVanmeldungsachb` varchar(35) DEFAULT NULL,
                                 `DVabmeldungdatum` date DEFAULT NULL,
                                 `DVabmeldungsachb` varchar(35) DEFAULT NULL,
                                 `DVmitversichert` text DEFAULT NULL,
                                 `DVvordienst` char(40) DEFAULT NULL,
                                 `DVanstellungsart` int(6) DEFAULT NULL,
                                 `DVfunktion` int(6) DEFAULT NULL,
                                 `DVdienstgeber` int(6) unsigned DEFAULT NULL COMMENT 'IBIS_FIRMA.id',
                                 `DVfunktionstr` char(128) DEFAULT NULL,
                                 `DVstatus` int(1) DEFAULT NULL COMMENT 'Status',
                                 `DVkarenz` tinyint(1) DEFAULT NULL COMMENT 'Status geringfügig Karenz',
                                 `DVkurzarbeit` tinyint(1) DEFAULT NULL COMMENT 'Kurzarbeit',
                                 `DVgesperrt` tinyint(1) DEFAULT NULL COMMENT 'Status gesperrt',
                                 `DVloek` enum('n','y') DEFAULT 'n',
                                 `DVaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                                 `DVaeuser` char(35) DEFAULT NULL,
                                 `DVerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstelltungsdatum',
                                 `DVeruser` char(35) DEFAULT NULL,
                                 PRIMARY KEY (`DVnr`),
                                 KEY `DIENSTVERTRAG_ADadnr` (`ADadnr`)
) ENGINE=MyISAM AUTO_INCREMENT=18843 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DIENSTVERTRAG_UNTERHALTSPFLICHTIGE`
--

DROP TABLE IF EXISTS `DIENSTVERTRAG_UNTERHALTSPFLICHTIGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DIENSTVERTRAG_UNTERHALTSPFLICHTIGE` (
                                                      `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                      `DVnr` int(6) unsigned DEFAULT NULL,
                                                      `ADadnr` int(6) unsigned DEFAULT NULL,
                                                      `svnr` int(6) unsigned DEFAULT NULL,
                                                      `gebdatum` date DEFAULT NULL,
                                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DIENSTZEUGNIS`
--

DROP TABLE IF EXISTS `DIENSTZEUGNIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DIENSTZEUGNIS` (
                                 `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                 `ADRESSE_adnr` int(6) unsigned DEFAULT NULL,
                                 `DIENSTVERTRAG_dvnr` int(6) unsigned DEFAULT NULL,
                                 `taetigkeit` varchar(100) DEFAULT NULL,
                                 `aussteller` int(6) unsigned DEFAULT NULL,
                                 `aussteller_funktion` varchar(100) DEFAULT NULL,
                                 `erda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                 `eruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DIENSTZEUGNIS_AUFGABEN`
--

DROP TABLE IF EXISTS `DIENSTZEUGNIS_AUFGABEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DIENSTZEUGNIS_AUFGABEN` (
                                          `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                          `DIENSTZEUGNIS_id` int(6) unsigned DEFAULT NULL,
                                          `aufgabenbereich` varchar(100) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOC_STORE`
--

DROP TABLE IF EXISTS `DOC_STORE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOC_STORE` (
                             `DCid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Dokument-ID',
                             `DCdatum` int(10) unsigned DEFAULT NULL COMMENT 'Dokument-Datum',
                             `DCbezeichnung` varchar(80) DEFAULT NULL COMMENT 'Dokument-Bezeichnung',
                             `DCtyp` char(3) DEFAULT NULL COMMENT 'Dokument-Typ',
                             `DCgroesse` int(6) unsigned DEFAULT NULL COMMENT 'Dokument-Grösse',
                             `DCsource` varchar(10) DEFAULT NULL COMMENT 'Quelle',
                             `DCsourceID` int(6) unsigned DEFAULT NULL COMMENT 'Quellen-ID',
                             `DClocation` varchar(80) DEFAULT NULL COMMENT 'Lokation',
                             `DCaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                             `DCaeuser` char(35) DEFAULT NULL,
                             `DCerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                             `DCeruser` char(35) DEFAULT NULL,
                             PRIMARY KEY (`DCid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_AD`
--

DROP TABLE IF EXISTS `DOK_AD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_AD` (
                          `sySTORE_STnr` int(9) unsigned NOT NULL,
                          `ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                          `ABTEILUNG_ABnr` int(3) unsigned NOT NULL,
                          `DOK_ART_id` int(6) unsigned DEFAULT NULL COMMENT 'FK DOK_ART_id DOK_ART_KLASSE = Trainer allgemein !!! Doks für Teilnehmer noch nicht implementiert',
                          `DKloek` enum('n','y') NOT NULL DEFAULT 'n',
                          PRIMARY KEY (`sySTORE_STnr`,`ADRESSE_ADadnr`,`ABTEILUNG_ABnr`),
                          KEY `DOK_ADR_FKIndex2` (`sySTORE_STnr`),
                          KEY `DOK_AD_FKIndex2` (`ADRESSE_ADadnr`,`ABTEILUNG_ABnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_ART`
--

DROP TABLE IF EXISTS `DOK_ART`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_ART` (
                           `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                           `bezeichnung_einzahl` varchar(50) NOT NULL,
                           `bezeichnung_mehrzahl` varchar(50) NOT NULL,
                           `DOK_ART_KLASSE_id` int(6) unsigned NOT NULL COMMENT 'FK: DOK_ART_KLASSE_id',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `bezeichnung_mehrzahl` (`bezeichnung_mehrzahl`,`DOK_ART_KLASSE_id`),
                           UNIQUE KEY `bezeichnung_einzahl` (`bezeichnung_einzahl`,`DOK_ART_KLASSE_id`),
                           KEY `DOK_ART_KLASSE_id_index` (`DOK_ART_KLASSE_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_ART_KLASSE`
--

DROP TABLE IF EXISTS `DOK_ART_KLASSE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_ART_KLASSE` (
                                  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                  `bezeichnung` varchar(100) NOT NULL,
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `bezeichnung` (`bezeichnung`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Um verschiedene DOK_ARTs für die Views (CRM, Trainer usw) an';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_AS`
--

DROP TABLE IF EXISTS `DOK_AS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_AS` (
                          `AUSSCHREIBUNG_ASnr` int(6) unsigned NOT NULL DEFAULT 0,
                          `sySTORE_STnr` int(9) unsigned NOT NULL DEFAULT 0,
                          `DKbereich` char(10) NOT NULL DEFAULT '',
                          `DKloek` enum('n','y') NOT NULL DEFAULT 'n',
                          PRIMARY KEY (`AUSSCHREIBUNG_ASnr`,`sySTORE_STnr`),
                          KEY `DOK_AS_FKIndex1` (`AUSSCHREIBUNG_ASnr`),
                          KEY `DOK_AS_FKIndex2` (`sySTORE_STnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_BD`
--

DROP TABLE IF EXISTS `DOK_BD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_BD` (
                          `AUSBILDUNG_ADRESSE_ADadnr` int(6) unsigned NOT NULL DEFAULT 0,
                          `AUSBILDUNG_BDnr` int(3) unsigned NOT NULL DEFAULT 0,
                          `DOK_ART_id` int(6) unsigned DEFAULT NULL COMMENT 'FK DOK_ART_id DOK_ART_KLASSE = Trainer Ausbildung | Trainer Efahrung. Doks für Teilnehmer noch nicht implementiert',
                          `sySTORE_STnr` int(9) unsigned NOT NULL DEFAULT 0,
                          `DKbereich` char(10) NOT NULL DEFAULT '',
                          `DKloek` enum('n','y') NOT NULL DEFAULT 'n',
                          PRIMARY KEY (`AUSBILDUNG_ADRESSE_ADadnr`,`AUSBILDUNG_BDnr`,`sySTORE_STnr`),
                          KEY `DOK_BD_FKIndex1` (`AUSBILDUNG_BDnr`,`AUSBILDUNG_ADRESSE_ADadnr`),
                          KEY `DOK_BD_FKIndex2` (`sySTORE_STnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_CRM_CF`
--

DROP TABLE IF EXISTS `DOK_CRM_CF`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_CRM_CF` (
                              `sySTORE_STnr` int(9) unsigned NOT NULL,
                              `CRM_FIRMA_id` int(6) unsigned NOT NULL,
                              `CRM_KATEGORIE_id` int(6) unsigned NOT NULL DEFAULT 3 COMMENT 'FK CRM_KATEGORIE.id',
                              `DOK_ART_id` int(6) unsigned NOT NULL DEFAULT 999,
                              `loek` enum('n','y') NOT NULL DEFAULT 'n',
                              PRIMARY KEY (`sySTORE_STnr`,`CRM_FIRMA_id`),
                              KEY `DOK_CRM_CF_FKIndexSTnr` (`sySTORE_STnr`),
                              KEY `DOK_CRM_CF_FKIndexCFnr` (`CRM_FIRMA_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_CRM_CM`
--

DROP TABLE IF EXISTS `DOK_CRM_CM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_CRM_CM` (
                              `sySTORE_STnr` int(9) unsigned NOT NULL,
                              `CRM_MITARBEITER_id` int(6) unsigned NOT NULL,
                              `CRM_KATEGORIE_id` int(6) unsigned NOT NULL DEFAULT 3 COMMENT 'FK CRM_KATEGORIE.id',
                              `DOK_ART_id` int(6) unsigned NOT NULL DEFAULT 999 COMMENT 'FK DOK_ART.id',
                              `loek` enum('n','y') NOT NULL DEFAULT 'n',
                              PRIMARY KEY (`sySTORE_STnr`,`CRM_MITARBEITER_id`),
                              KEY `DOK_CRM_CM_FKIndexSTnr` (`sySTORE_STnr`),
                              KEY `DOK_CRM_CM_FKIndexCMnr` (`CRM_MITARBEITER_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_CRM_OLD`
--

DROP TABLE IF EXISTS `DOK_CRM_OLD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_CRM_OLD` (
                               `sySTORE_STnr` int(9) unsigned NOT NULL,
                               `CRM_OLD_ADadnr` int(6) unsigned NOT NULL,
                               `ABTEILUNG_ABnr` int(3) unsigned NOT NULL,
                               `DOK_ART_id` int(6) unsigned DEFAULT NULL COMMENT 'FK DOK_ART_id DOK_ART_KLASSE = Trainer allgemein !!! Doks für Teilnehmer noch nicht implementiert',
                               `DKloek` enum('n','y') NOT NULL DEFAULT 'n',
                               PRIMARY KEY (`sySTORE_STnr`,`CRM_OLD_ADadnr`,`ABTEILUNG_ABnr`),
                               KEY `DOK_ADR_FKIndex2` (`sySTORE_STnr`),
                               KEY `DOK_AD_FKIndex2` (`CRM_OLD_ADadnr`,`ABTEILUNG_ABnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_IK`
--

DROP TABLE IF EXISTS `DOK_IK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_IK` (
                          `IK_ASnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                          `IK_STnr` int(9) unsigned NOT NULL,
                          `IKloek` enum('y','n') NOT NULL DEFAULT 'n',
                          PRIMARY KEY (`IK_ASnr`,`IK_STnr`)
) ENGINE=MyISAM AUTO_INCREMENT=10923 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_JAG_AD`
--

DROP TABLE IF EXISTS `DOK_JAG_AD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_JAG_AD` (
                              `sySTORE_STnr` int(9) unsigned NOT NULL,
                              `ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                              `JAGgefvon` varchar(100) DEFAULT NULL,
                              `DOK_ART_id` int(6) unsigned DEFAULT NULL COMMENT 'FK DOK_ART_id DOK_ART_KLASSE = Trainer allgemein !!! Doks für Teilnehmer noch nicht implementiert',
                              `DKloek` enum('n','y') NOT NULL DEFAULT 'n',
                              `JAGgefam` date DEFAULT NULL,
                              PRIMARY KEY (`sySTORE_STnr`),
                              KEY `DOK_JAG_ADR_FKIndex` (`sySTORE_STnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_SO`
--

DROP TABLE IF EXISTS `DOK_SO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_SO` (
                          `STANDORT_SOstandortid` int(6) NOT NULL,
                          `sySTORE_STnr` int(9) unsigned NOT NULL,
                          `MVid` int(6) unsigned DEFAULT NULL COMMENT 'zugewiesen zu Mietvertrag Nummer',
                          `DOK_SO_KATEGORIE_id` int(6) unsigned DEFAULT 1,
                          `DKloek` enum('n','y') DEFAULT 'n',
                          PRIMARY KEY (`STANDORT_SOstandortid`,`sySTORE_STnr`),
                          KEY `DOC_SO_FKIndex1` (`sySTORE_STnr`),
                          KEY `DOC_SO_FKIndex2` (`STANDORT_SOstandortid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_SO_KATEGORIE`
--

DROP TABLE IF EXISTS `DOK_SO_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_SO_KATEGORIE` (
                                    `id` int(6) unsigned NOT NULL,
                                    `bezeichnung` char(100) DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOK_ST`
--

DROP TABLE IF EXISTS `DOK_ST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOK_ST` (
                          `STELLE_STnr` int(6) unsigned NOT NULL,
                          `sySTORE_STnr` int(9) unsigned NOT NULL,
                          `DKloek` enum('n','y') DEFAULT 'n',
                          PRIMARY KEY (`STELLE_STnr`,`sySTORE_STnr`),
                          KEY `DOK_ST_FKIndex1` (`STELLE_STnr`),
                          KEY `DOK_ST_FKIndex2` (`sySTORE_STnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DPW_ARBEITSPLANMODELL`
--

DROP TABLE IF EXISTS `DPW_ARBEITSPLANMODELL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DPW_ARBEITSPLANMODELL` (
                                         `id` int(11) NOT NULL,
                                         `Bezeichnung` varchar(45) NOT NULL,
                                         `Bezeichnung_dpw` varchar(45) NOT NULL,
                                         `aktiv` varchar(45) NOT NULL DEFAULT '0',
                                         UNIQUE KEY `id_UNIQUE` (`id`),
                                         UNIQUE KEY `Bezeichnung_dpw_UNIQUE` (`Bezeichnung_dpw`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DPW_LEISTUNG`
--

DROP TABLE IF EXISTS `DPW_LEISTUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DPW_LEISTUNG` (
                                `LeistungId` int(6) unsigned NOT NULL,
                                `AdresseId` int(6) unsigned NOT NULL,
                                `Datum` date NOT NULL,
                                `Von` time /* mariadb-5.3 */ DEFAULT NULL,
                                `Bis` time /* mariadb-5.3 */ DEFAULT NULL,
                                `PauseVon` time /* mariadb-5.3 */ DEFAULT NULL,
                                `PauseBis` time /* mariadb-5.3 */ DEFAULT NULL,
                                `TaetigkeitId` int(6) unsigned NOT NULL,
                                `ExportiertAm` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                PRIMARY KEY (`LeistungId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DPW_PLANGRUPPE`
--

DROP TABLE IF EXISTS `DPW_PLANGRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DPW_PLANGRUPPE` (
                                  `id` int(11) NOT NULL,
                                  `Bezeichnung` varchar(45) NOT NULL,
                                  `Bezeichnung_dpw` varchar(45) NOT NULL,
                                  `aktiv` varchar(45) NOT NULL DEFAULT '0',
                                  UNIQUE KEY `id_UNIQUE` (`id`),
                                  UNIQUE KEY `Bezeichnung_dpw_UNIQUE` (`Bezeichnung_dpw`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DPW_UMBUCHUNGSMODELL`
--

DROP TABLE IF EXISTS `DPW_UMBUCHUNGSMODELL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DPW_UMBUCHUNGSMODELL` (
                                        `id` int(11) NOT NULL,
                                        `Bezeichnung` varchar(45) NOT NULL,
                                        `Bezeichnung_dpw` varchar(45) NOT NULL,
                                        `aktiv` varchar(45) NOT NULL DEFAULT '0',
                                        UNIQUE KEY `id_UNIQUE` (`id`),
                                        UNIQUE KEY `Bezeichnung_dpw_UNIQUE` (`Bezeichnung_dpw`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DVZUSATZ`
--

DROP TABLE IF EXISTS `DVZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DVZUSATZ` (
                            `DZnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                            `ADadnr` int(6) unsigned NOT NULL,
                            `DVnr` int(6) unsigned NOT NULL,
                            `DZpersnr` char(10) DEFAULT NULL,
                            `DZdatumab` date DEFAULT NULL,
                            `DZlohn` decimal(9,2) DEFAULT NULL,
                            `DZpraemie` decimal(9,2) DEFAULT NULL,
                            `DZhonorar` char(40) DEFAULT NULL,
                            `DZfahrtkosten` decimal(9,2) DEFAULT NULL,
                            `DZfahrkostenvereinb` char(128) DEFAULT NULL,
                            `DZbemerkungen` text DEFAULT NULL,
                            `DZsteuerbefreit` enum('n','y') DEFAULT 'n',
                            `DZazmo` decimal(4,2) DEFAULT NULL,
                            `DZazdi` decimal(4,2) DEFAULT NULL,
                            `DZazmi` decimal(4,2) DEFAULT NULL,
                            `DZazdo` decimal(4,2) DEFAULT NULL,
                            `DZazfr` decimal(4,2) DEFAULT NULL,
                            `DZazsa` decimal(4,2) DEFAULT NULL,
                            `DZazso` decimal(4,2) DEFAULT NULL,
                            `DZwochenstd` int(3) DEFAULT 0 COMMENT 'Stunden pro Woche',
                            `DZurlaub` decimal(3,2) DEFAULT NULL,
                            `DZbebucht` enum('n','y') DEFAULT 'n',
                            `DZmeldepflicht` int(1) DEFAULT NULL,
                            `DZmeldungdatum` date DEFAULT NULL,
                            `DZmeldungsachb` varchar(35) DEFAULT NULL,
                            `DZmittagspause_ignorieren` tinyint(1) DEFAULT NULL COMMENT 'keine Mittagspause im PEP erforderlich',
                            `DZstatus` int(1) DEFAULT NULL,
                            `DZloek` enum('n','y') DEFAULT 'n',
                            `DZaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `DZaeuser` char(35) DEFAULT NULL,
                            `DZerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `DZeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`DZnr`),
                            KEY `DVZUSATZ_datumab_adnr` (`DZdatumab`,`ADadnr`),
                            KEY `DVZUSATZ_DVnr` (`DVnr`)
) ENGINE=InnoDB AUTO_INCREMENT=3808699 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EINZELCOACHINGS`
--

DROP TABLE IF EXISTS `EINZELCOACHINGS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EINZELCOACHINGS` (
                                   `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                   `SEMINAR_id` int(6) unsigned DEFAULT NULL,
                                   `TEILNEHMER_id` int(6) unsigned DEFAULT NULL,
                                   `einzelcoach_smad_nr` int(6) unsigned DEFAULT NULL COMMENT 'SM_AD.SMADnr des Einzelcoachs',
                                   `datum_geplant` date DEFAULT NULL,
                                   `zeit_von_geplant` time /* mariadb-5.3 */ DEFAULT NULL,
                                   `zeit_bis_geplant` time /* mariadb-5.3 */ DEFAULT NULL,
                                   `thema` text DEFAULT NULL,
                                   `gespraechsprotokoll` text DEFAULT NULL,
                                   `leistung_id` int(6) unsigned DEFAULT NULL,
                                   `ecHomeoffice` varchar(15) DEFAULT NULL,
                                   `storniert` tinyint(1) DEFAULT NULL,
                                   `storno_grund_id` int(6) unsigned DEFAULT NULL COMMENT 'EINZELCOACHINGS_STORNO_GRUENDE.id',
                                   `storno_verrechnen` tinyint(1) DEFAULT 1,
                                   `storno_verrechnung_schluessel` decimal(5,4) DEFAULT NULL,
                                   `einzelcoach_old` int(6) unsigned DEFAULT NULL,
                                   `absage_durch_id` int(6) DEFAULT NULL,
                                   `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `AenderungsBenutzer` char(35) DEFAULT NULL,
                                   `ErstellungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `ErstellungsBenutzer` char(35) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `EINZELCOACHING_leistung_id` (`leistung_id`),
                                   KEY `EINZELCOACHING_SEMINAR_id` (`SEMINAR_id`),
                                   KEY `EINZELCOACHING_TEILNEHMER_id` (`TEILNEHMER_id`),
                                   KEY `EINZELCOACHING_einzelcoach_smad_nr` (`einzelcoach_smad_nr`),
                                   KEY `EINZELCOACHING_storno_grund_id` (`storno_grund_id`)
) ENGINE=InnoDB AUTO_INCREMENT=257417 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EINZELCOACHINGS_ABSAGE_DURCH`
--

DROP TABLE IF EXISTS `EINZELCOACHINGS_ABSAGE_DURCH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EINZELCOACHINGS_ABSAGE_DURCH` (
                                                `Id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                `Name` text DEFAULT NULL,
                                                `Aktiv` int(1) DEFAULT 0,
                                                `Sortierung` int(6) DEFAULT NULL,
                                                `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                `AenderungsBenutzer` char(35) CHARACTER SET latin1 COLLATE latin1_german1_ci DEFAULT NULL,
                                                `ErstellungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                `ErstellungsBenutzer` char(35) CHARACTER SET latin1 COLLATE latin1_german1_ci DEFAULT NULL,
                                                PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EINZELCOACHINGS_STORNO_GRUENDE`
--

DROP TABLE IF EXISTS `EINZELCOACHINGS_STORNO_GRUENDE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EINZELCOACHINGS_STORNO_GRUENDE` (
                                                  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                  `bezeichnung` text NOT NULL,
                                                  `Aktiv` int(1) DEFAULT 0,
                                                  `Sortierung` int(6) DEFAULT NULL,
                                                  `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                  `AenderungsBenutzer` char(35) DEFAULT NULL,
                                                  `ErstellungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                  `ErstellungsBenutzer` char(35) DEFAULT NULL,
                                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ERKRANKUNGSTYP`
--

DROP TABLE IF EXISTS `ERKRANKUNGSTYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ERKRANKUNGSTYP` (
                                  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                  `ordnungsnummer` int(6) unsigned DEFAULT NULL,
                                  `bezeichnung` varchar(50) NOT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='derzeit nur verwendet in PJ des Typs fit2work';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FEIERTAG`
--

DROP TABLE IF EXISTS `FEIERTAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FEIERTAG` (
                            `FTnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                            `FTland` int(6) unsigned NOT NULL,
                            `FTbundesland` int(6) unsigned NOT NULL DEFAULT 0,
                            `FTdatum` date NOT NULL,
                            `FTdatumbis` date DEFAULT NULL,
                            `FTtyp` char(3) DEFAULT NULL,
                            `FTbezeichnung` char(20) DEFAULT NULL,
                            PRIMARY KEY (`FTnr`)
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIRMA_KOLLEKTIVVERTRAG`
--

DROP TABLE IF EXISTS `FIRMA_KOLLEKTIVVERTRAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIRMA_KOLLEKTIVVERTRAG` (
                                          `ibis_firma_id` int(6) unsigned NOT NULL DEFAULT 0,
                                          `kollektivvertrag_id` int(6) unsigned NOT NULL DEFAULT 0,
                                          `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                          `eruser` char(35) DEFAULT NULL,
                                          `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                          `aeuser` char(35) DEFAULT NULL,
                                          PRIMARY KEY (`ibis_firma_id`,`kollektivvertrag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_ABBRUCHLISTE`
--

DROP TABLE IF EXISTS `FIT2WORK_ABBRUCHLISTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_ABBRUCHLISTE` (
                                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                         `bezeichnung` char(250) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_AKTUELLER_STATUS`
--

DROP TABLE IF EXISTS `FIT2WORK_AKTUELLER_STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_AKTUELLER_STATUS` (
                                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                             `ordnungsnummer` int(6) unsigned DEFAULT NULL,
                                             `kuerzel` varchar(10) NOT NULL,
                                             `bezeichnung` varchar(50) NOT NULL,
                                             PRIMARY KEY (`id`),
                                             UNIQUE KEY `kuerzel` (`kuerzel`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Fuer TNs in PJs des Typs fit2work';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_ANLAUFSTELLE`
--

DROP TABLE IF EXISTS `FIT2WORK_ANLAUFSTELLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_ANLAUFSTELLE` (
                                         `id` int(6) unsigned NOT NULL,
                                         `bezeichnung` varchar(200) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_BERUFSAUSBILDUNG`
--

DROP TABLE IF EXISTS `FIT2WORK_BERUFSAUSBILDUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_BERUFSAUSBILDUNG` (
                                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                             `bezeichnung` varchar(200) DEFAULT NULL,
                                             `code` char(10) DEFAULT NULL,
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_BERUFSGRUPPE`
--

DROP TABLE IF EXISTS `FIT2WORK_BERUFSGRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_BERUFSGRUPPE` (
                                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                         `bezeichnung` varchar(200) DEFAULT NULL,
                                         `code` char(10) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_BUNDESLAND`
--

DROP TABLE IF EXISTS `FIT2WORK_BUNDESLAND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_BUNDESLAND` (
                                       `id` int(6) unsigned NOT NULL,
                                       `bezeichnung` varchar(100) DEFAULT NULL,
                                       `aktiv` tinyint(1) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_EMPFEHLUNG`
--

DROP TABLE IF EXISTS `FIT2WORK_EMPFEHLUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_EMPFEHLUNG` (
                                       `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                       `ordnungsnummer` int(6) unsigned DEFAULT NULL,
                                       `bezeichnung` varchar(50) NOT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Fuer TNs in PJs des Typs fit2work';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_ERGEBNIS_NACH_STATUS`
--

DROP TABLE IF EXISTS `FIT2WORK_ERGEBNIS_NACH_STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_ERGEBNIS_NACH_STATUS` (
                                                 `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                 `ordnungsnummer` int(6) unsigned DEFAULT NULL,
                                                 `bezeichnung` varchar(50) NOT NULL,
                                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Fuer TNs in PJs des Typs fit2work';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_ERKRANKUNG`
--

DROP TABLE IF EXISTS `FIT2WORK_ERKRANKUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_ERKRANKUNG` (
                                       `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                       `FIT2WORK_FALL_id` int(6) unsigned DEFAULT NULL,
                                       `ERKRANKUNG_id` int(6) unsigned DEFAULT NULL,
                                       `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `eruser` char(35) DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `ZUSATZ_ERKRANKUNG` (`FIT2WORK_FALL_id`,`ERKRANKUNG_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28445 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_FALL`
--

DROP TABLE IF EXISTS `FIT2WORK_FALL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_FALL` (
                                 `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                 `kstgr` int(3) DEFAULT NULL COMMENT 'Kostenstellengruppe',
                                 `FIT2WORK_BUNDESLAND_id` int(6) unsigned DEFAULT NULL,
                                 `bsa_id` int(6) unsigned DEFAULT NULL COMMENT 'Identifikationsnummer Fit2Work-Monitoring-Datenbank',
                                 `ADRESSE_adnr` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                 `zugang` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ZUGANG.id',
                                 `anlaufstelle` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ANLAUFSTELLE.id',
                                 `oesterr_staatsbuerger_seit` date DEFAULT NULL,
                                 `kostenfreier_therapieplatz` tinyint(1) DEFAULT NULL,
                                 `krankenstand` tinyint(1) DEFAULT NULL,
                                 `hoechste_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                 `letzte_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                 `berufsgruppe` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSGRUPPE.id',
                                 `arbeitsverhaeltnis` enum('arbeitslos','erwerbstaetig') DEFAULT NULL,
                                 `versicherungstraeger` int(6) unsigned DEFAULT NULL COMMENT 'VERSICHERUNGSTRAEGER.id',
                                 `behinderung` tinyint(1) DEFAULT NULL,
                                 `pension_old` tinyint(1) DEFAULT NULL,
                                 `pension` enum('ja','nein','rehageld','umschulungsgeld') DEFAULT NULL,
                                 `selbstversichert` tinyint(1) DEFAULT NULL,
                                 `kontaktaufnahme` enum('schriftlich','telefonisch','persoenlich') DEFAULT NULL,
                                 `erstberatung_abgeschlossen_am` date DEFAULT NULL,
                                 `erstberatung_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                 `basischeck_am` date DEFAULT NULL,
                                 `apam_am` date DEFAULT NULL,
                                 `entwicklungsplan_am` date DEFAULT NULL,
                                 `case_management_ruhend_seit` date DEFAULT NULL,
                                 `case_management_abgebrochen_am` date DEFAULT NULL,
                                 `case_management_abgeschlossen_am` date DEFAULT NULL,
                                 `case_management_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                 `anonym` tinyint(1) DEFAULT NULL,
                                 `staatsb_sonstige` tinyint(1) DEFAULT NULL,
                                 `geburtsland_sonstige` tinyint(1) DEFAULT NULL,
                                 `feedback_am` date DEFAULT NULL,
                                 `beschreibung` text DEFAULT NULL,
                                 `abbruch_id` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ABBRUCHLISTE.id',
                                 `abbruch_sonstiges` char(250) DEFAULT NULL COMMENT 'Textfeld eines sonstigen Abbruchs',
                                 `feedback_nicht_vorhanden` tinyint(1) DEFAULT NULL COMMENT 'kein Feedback einholbar',
                                 `feedback_status` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_FEEDBACK_STATUSLISTE.id',
                                 `feedback_status_krankenstand_planungen` text DEFAULT NULL COMMENT 'Konkrete Planungen zur Rückkehr aus Krankenstand, wenn Status "Krankenstand" ausgewählt ist',
                                 `verbesserung_gesundheit` enum('n','y') DEFAULT NULL COMMENT 'Verbesserung der Gesundheitssituation (aus Sicht des Kunden)',
                                 `einhaltung_vereinbarung` enum('n','y') DEFAULT NULL COMMENT 'Einhaltung der Vereinbarungen vom Entwicklungsplan',
                                 `verbesserung_gesundheit_durch_ep` enum('n','y') DEFAULT NULL COMMENT 'Maßnahmen des Entwicklungsplan haben zur Verbesserung der Gesundheit beigetragen - aus Sicht des Kunden',
                                 `unterstuetzung_notwendig` enum('n','y') DEFAULT NULL COMMENT 'neuer Unterstützungsbedarf durch fit2work notwendig - aus Sicht des Kunden ',
                                 `wiederaufnahme_fit2work` enum('n','y') DEFAULT NULL COMMENT 'Wiederaufnahme Fit2Work',
                                 `verb_med_sonstiges` char(250) DEFAULT NULL COMMENT 'Verbesserungsmassnahme Medizinische Maßnahme / Sonstiges',
                                 `verb_ams_sonstiges` char(250) DEFAULT NULL COMMENT 'Verbesserungsmassnahme Fallübergabe an / AMS / Sonstiges',
                                 `verb_pva_sonstiges` char(250) DEFAULT NULL COMMENT 'Verbesserungsmassnahme Fallübergabe an / PVA / Sonstiges',
                                 `verb_gkk_sonstiges` char(250) DEFAULT NULL COMMENT 'Verbesserungsmassnahme Fallübergabe an / GKK / Sonstiges',
                                 `verb_andere_einrichtungen_sonstiges` char(250) DEFAULT NULL COMMENT 'Verbesserungsmassnahme Fallübergabe an / sonstiges Einrichtungen / Sonstiges',
                                 `verb_sonstiges` char(250) DEFAULT NULL COMMENT 'Verbesserungsmassnahme Sonstiges',
                                 `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `aeuser` char(35) DEFAULT NULL,
                                 `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `eruser` char(35) DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `FIT2WORK_BUNDESLAND_id` (`FIT2WORK_BUNDESLAND_id`,`bsa_id`),
                                 KEY `FIT2WORK_TN_ZUSATZ_ADRESSE_adnr` (`ADRESSE_adnr`)
) ENGINE=InnoDB AUTO_INCREMENT=26932 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_FALL_TEMP`
--

DROP TABLE IF EXISTS `FIT2WORK_FALL_TEMP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_FALL_TEMP` (
                                      `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                      `kstgr` int(3) DEFAULT NULL COMMENT 'Kostenstellengruppe',
                                      `bsa_id` int(6) unsigned DEFAULT NULL COMMENT 'Identifikationsnummer Fit2Work-Monitoring-Datenbank',
                                      `ADRESSE_adnr` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                      `zugang` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ZUGANG.id',
                                      `anlaufstelle` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ANLAUFSTELLE.id',
                                      `oesterr_staatsbuerger_seit` date DEFAULT NULL,
                                      `kostenfreier_therapieplatz` tinyint(1) DEFAULT NULL,
                                      `krankenstand` tinyint(1) DEFAULT NULL,
                                      `hoechste_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                      `letzte_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                      `berufsgruppe` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSGRUPPE.id',
                                      `arbeitsverhaeltnis` enum('arbeitslos','erwerbstaetig') DEFAULT NULL,
                                      `versicherungstraeger` int(6) unsigned DEFAULT NULL COMMENT 'VERSICHERUNGSTRAEGER.id',
                                      `behinderung` tinyint(1) DEFAULT NULL,
                                      `pension` tinyint(1) DEFAULT NULL,
                                      `selbstversichert` tinyint(1) DEFAULT NULL,
                                      `kontaktaufnahme` enum('schriftlich','telefonisch','persoenlich') DEFAULT NULL,
                                      `erstberatung_abgeschlossen_am` date DEFAULT NULL,
                                      `erstberatung_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                      `basischeck_am` date DEFAULT NULL,
                                      `entwicklungsplan_am` date DEFAULT NULL,
                                      `case_management_ruhend_seit` date DEFAULT NULL,
                                      `case_management_abgebrochen_am` date DEFAULT NULL,
                                      `case_management_abgeschlossen_am` date DEFAULT NULL,
                                      `case_management_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                      `anonym` tinyint(1) DEFAULT NULL,
                                      `staatsb_sonstige` tinyint(1) DEFAULT NULL,
                                      `geburtsland_sonstige` tinyint(1) DEFAULT NULL,
                                      `feedback_am` date DEFAULT NULL,
                                      `beschreibung` text DEFAULT NULL,
                                      `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                      `aeuser` char(35) DEFAULT NULL,
                                      `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                      `eruser` char(35) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `kstgr` (`kstgr`,`bsa_id`),
                                      KEY `FIT2WORK_TN_ZUSATZ_ADRESSE_adnr` (`ADRESSE_adnr`)
) ENGINE=InnoDB AUTO_INCREMENT=16391 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_FALL_TMP`
--

DROP TABLE IF EXISTS `FIT2WORK_FALL_TMP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_FALL_TMP` (
                                     `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                     `kstgr` int(3) DEFAULT NULL COMMENT 'Kostenstellengruppe',
                                     `bsa_id` int(6) unsigned DEFAULT NULL COMMENT 'Identifikationsnummer Fit2Work-Monitoring-Datenbank',
                                     `ADRESSE_adnr` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                     `zugang` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ZUGANG.id',
                                     `anlaufstelle` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ANLAUFSTELLE.id',
                                     `oesterr_staatsbuerger_seit` date DEFAULT NULL,
                                     `kostenfreier_therapieplatz` tinyint(1) DEFAULT NULL,
                                     `krankenstand` tinyint(1) DEFAULT NULL,
                                     `hoechste_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                     `letzte_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                     `berufsgruppe` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSGRUPPE.id',
                                     `arbeitsverhaeltnis` enum('arbeitslos','erwerbstaetig') DEFAULT NULL,
                                     `versicherungstraeger` int(6) unsigned DEFAULT NULL COMMENT 'VERSICHERUNGSTRAEGER.id',
                                     `behinderung` tinyint(1) DEFAULT NULL,
                                     `pension` tinyint(1) DEFAULT NULL,
                                     `selbstversichert` tinyint(1) DEFAULT NULL,
                                     `kontaktaufnahme` enum('schriftlich','telefonisch','persoenlich') DEFAULT NULL,
                                     `erstberatung_abgeschlossen_am` date DEFAULT NULL,
                                     `erstberatung_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                     `basischeck_am` date DEFAULT NULL,
                                     `entwicklungsplan_am` date DEFAULT NULL,
                                     `case_management_ruhend_seit` date DEFAULT NULL,
                                     `case_management_abgebrochen_am` date DEFAULT NULL,
                                     `case_management_abgeschlossen_am` date DEFAULT NULL,
                                     `case_management_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                     `anonym` tinyint(1) DEFAULT NULL,
                                     `staatsb_sonstige` tinyint(1) DEFAULT NULL,
                                     `geburtsland_sonstige` tinyint(1) DEFAULT NULL,
                                     `feedback_am` date DEFAULT NULL,
                                     `beschreibung` text DEFAULT NULL,
                                     `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     `aeuser` char(35) DEFAULT NULL,
                                     `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     `eruser` char(35) DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `kstgr` (`kstgr`,`bsa_id`),
                                     KEY `FIT2WORK_TN_ZUSATZ_ADRESSE_adnr` (`ADRESSE_adnr`)
) ENGINE=InnoDB AUTO_INCREMENT=16391 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_FALL_TMP2`
--

DROP TABLE IF EXISTS `FIT2WORK_FALL_TMP2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_FALL_TMP2` (
                                      `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                      `kstgr` int(3) DEFAULT NULL COMMENT 'Kostenstellengruppe',
                                      `bsa_id` int(6) unsigned DEFAULT NULL COMMENT 'Identifikationsnummer Fit2Work-Monitoring-Datenbank',
                                      `ADRESSE_adnr` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                      `zugang` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ZUGANG.id',
                                      `anlaufstelle` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ANLAUFSTELLE.id',
                                      `oesterr_staatsbuerger_seit` date DEFAULT NULL,
                                      `kostenfreier_therapieplatz` tinyint(1) DEFAULT NULL,
                                      `krankenstand` tinyint(1) DEFAULT NULL,
                                      `hoechste_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                      `letzte_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                      `berufsgruppe` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSGRUPPE.id',
                                      `arbeitsverhaeltnis` enum('arbeitslos','erwerbstaetig') DEFAULT NULL,
                                      `versicherungstraeger` int(6) unsigned DEFAULT NULL COMMENT 'VERSICHERUNGSTRAEGER.id',
                                      `behinderung` tinyint(1) DEFAULT NULL,
                                      `pension` tinyint(1) DEFAULT NULL,
                                      `selbstversichert` tinyint(1) DEFAULT NULL,
                                      `kontaktaufnahme` enum('schriftlich','telefonisch','persoenlich') DEFAULT NULL,
                                      `erstberatung_abgeschlossen_am` date DEFAULT NULL,
                                      `erstberatung_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                      `basischeck_am` date DEFAULT NULL,
                                      `entwicklungsplan_am` date DEFAULT NULL,
                                      `case_management_ruhend_seit` date DEFAULT NULL,
                                      `case_management_abgebrochen_am` date DEFAULT NULL,
                                      `case_management_abgeschlossen_am` date DEFAULT NULL,
                                      `case_management_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                      `anonym` tinyint(1) DEFAULT NULL,
                                      `staatsb_sonstige` tinyint(1) DEFAULT NULL,
                                      `geburtsland_sonstige` tinyint(1) DEFAULT NULL,
                                      `feedback_am` date DEFAULT NULL,
                                      `beschreibung` text DEFAULT NULL,
                                      `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                      `aeuser` char(35) DEFAULT NULL,
                                      `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                      `eruser` char(35) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `FIT2WORK_TN_ZUSATZ_ADRESSE_adnr` (`ADRESSE_adnr`)
) ENGINE=InnoDB AUTO_INCREMENT=17477 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_FALL_VERBESSERUNGSMASSNAHMEN`
--

DROP TABLE IF EXISTS `FIT2WORK_FALL_VERBESSERUNGSMASSNAHMEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_FALL_VERBESSERUNGSMASSNAHMEN` (
                                                         `FIT2WORK_FALL_id` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'FIT2WORK_FALL.id',
                                                         `VERBESSERUNGSMASSNAHMEN_id` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'FIT2WORK_VERBESSERUNGSMASSNAHMEN.id',
                                                         PRIMARY KEY (`FIT2WORK_FALL_id`,`VERBESSERUNGSMASSNAHMEN_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_FEEDBACK_STATUSLISTE`
--

DROP TABLE IF EXISTS `FIT2WORK_FEEDBACK_STATUSLISTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_FEEDBACK_STATUSLISTE` (
                                                 `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                 `kategorie_id` int(6) unsigned DEFAULT NULL,
                                                 `bezeichnung` char(250) DEFAULT NULL,
                                                 `bsa_code` int(6) unsigned DEFAULT NULL,
                                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_FEEDBACK_STATUSLISTE_KATEGORIE`
--

DROP TABLE IF EXISTS `FIT2WORK_FEEDBACK_STATUSLISTE_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_FEEDBACK_STATUSLISTE_KATEGORIE` (
                                                           `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                           `bezeichnung` char(250) DEFAULT NULL,
                                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_SM_TN_ZUSATZ`
--

DROP TABLE IF EXISTS `FIT2WORK_SM_TN_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_SM_TN_ZUSATZ` (
                                         `SEMINAR_SMnr` int(6) unsigned NOT NULL COMMENT 'SM_TN.SEMINAR_SMnr',
                                         `ADRESSE_ADadnr` int(6) unsigned NOT NULL COMMENT 'SM_TN.ADRESSE_ADadnr',
                                         `aktuellerStatus` int(6) unsigned NOT NULL COMMENT 'FIT2WORK_AKTUELLER_STATUS.id',
                                         `behinderung` tinyint(1) DEFAULT NULL,
                                         `behinderungGrad` tinyint(4) DEFAULT NULL,
                                         `ergebnisNachStatus` int(6) unsigned NOT NULL COMMENT 'FIT2WORK_ERGEBNIS_NACH_STATUS.id',
                                         `empfehlung` int(6) unsigned NOT NULL COMMENT 'FIT2WORK_EMPFEHLUNG.id',
                                         `versicherungstraeger_id` int(3) NOT NULL,
                                         PRIMARY KEY (`SEMINAR_SMnr`,`ADRESSE_ADadnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Fuer SM_TNs in PJs des Typs fit2work';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_TN_DOKUMENTE`
--

DROP TABLE IF EXISTS `FIT2WORK_TN_DOKUMENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_TN_DOKUMENTE` (
                                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                         `FIT2WORK_FALL_id` int(6) unsigned DEFAULT NULL,
                                         `sySTORE_STnr` int(9) unsigned DEFAULT NULL,
                                         `DOK_ART_id` int(6) unsigned DEFAULT NULL,
                                         `loek` enum('n','y') DEFAULT 'n',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19506 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_TN_MISSING`
--

DROP TABLE IF EXISTS `FIT2WORK_TN_MISSING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_TN_MISSING` (
                                       `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                       `teilnehmer_id` int(6) unsigned DEFAULT NULL COMMENT 'Spalte identifikationsnummer aus CSV-File',
                                       `bundesland` varchar(100) DEFAULT NULL COMMENT 'Spalte bundesland aus CSV-File',
                                       `import_error` int(6) unsigned DEFAULT NULL COMMENT '$errorCodes in FIT2WORKCSVTeilnehmerImportFileErrorHandler',
                                       `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `eruser` char(35) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18851 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_TN_NOTIZ`
--

DROP TABLE IF EXISTS `FIT2WORK_TN_NOTIZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_TN_NOTIZ` (
                                     `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                     `FIT2WORK_FALL_id` int(6) unsigned DEFAULT NULL,
                                     `titel` char(40) DEFAULT NULL,
                                     `text` text DEFAULT NULL,
                                     `loek` enum('n','y') DEFAULT 'n',
                                     `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     `aeuser` char(35) DEFAULT NULL,
                                     `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     `eruser` char(35) DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `FIT2WORK_FALL_id` (`FIT2WORK_FALL_id`),
                                     CONSTRAINT `FIT2WORK_TN_NOTIZ_ibfk_1` FOREIGN KEY (`FIT2WORK_FALL_id`) REFERENCES `FIT2WORK_FALL` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5725 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_TN_NOTIZ_TMP`
--

DROP TABLE IF EXISTS `FIT2WORK_TN_NOTIZ_TMP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_TN_NOTIZ_TMP` (
                                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                         `FIT2WORK_FALL_id` int(6) unsigned DEFAULT NULL,
                                         `titel` char(40) DEFAULT NULL,
                                         `text` text DEFAULT NULL,
                                         `loek` enum('n','y') DEFAULT 'n',
                                         `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                         `aeuser` char(35) DEFAULT NULL,
                                         `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                         `eruser` char(35) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5085 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_TN_SEMINARDOK`
--

DROP TABLE IF EXISTS `FIT2WORK_TN_SEMINARDOK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_TN_SEMINARDOK` (
                                          `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                          `FIT2WORK_FALL_id` int(6) unsigned DEFAULT NULL,
                                          `sySTORE_STnr` int(9) unsigned DEFAULT NULL,
                                          `gesperrt` tinyint(1) DEFAULT NULL,
                                          `gesperrt_durch` char(35) DEFAULT NULL,
                                          `loek` enum('n','y') DEFAULT 'n',
                                          `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                          `aeuser` char(35) DEFAULT NULL,
                                          `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                          `eruser` char(35) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49121 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_TN_ZUSATZ`
--

DROP TABLE IF EXISTS `FIT2WORK_TN_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_TN_ZUSATZ` (
                                      `ADRESSE_adnr` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'ADRESSE.ADadnr',
                                      `kstgr` int(3) NOT NULL DEFAULT 0 COMMENT 'Kostenstellengruppe',
                                      `bsa_id` int(6) unsigned DEFAULT NULL COMMENT 'Identifikationsnummer Fit2Work-Monitoring-Datenbank',
                                      `zugang` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ZUGANG.id',
                                      `anlaufstelle` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_ANLAUFSTELLE.id',
                                      `oesterr_staatsbuerger_seit` date DEFAULT NULL,
                                      `kostenfreier_therapieplatz` tinyint(1) DEFAULT NULL,
                                      `krankenstand` tinyint(1) DEFAULT NULL,
                                      `hoechste_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                      `letzte_berufsausbildung` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSAUSBILDUNG.id',
                                      `berufsgruppe` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_BERUFSGRUPPE.id',
                                      `arbeitsverhaeltnis` enum('arbeitslos','erwerbstaetig') DEFAULT NULL,
                                      `versicherungstraeger` int(6) unsigned DEFAULT NULL COMMENT 'VERSICHERUNGSTRAEGER.id',
                                      `behinderung` tinyint(1) DEFAULT NULL,
                                      `pension` tinyint(1) DEFAULT NULL,
                                      `selbstversichert` tinyint(1) DEFAULT NULL,
                                      `kontaktaufnahme` enum('schriftlich','telefonisch','persoenlich') DEFAULT NULL,
                                      `erstberatung_abgeschlossen_am` date DEFAULT NULL,
                                      `erstberatung_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                      `basischeck_am` date DEFAULT NULL,
                                      `entwicklungsplan_am` date DEFAULT NULL,
                                      `case_management_ruhend_seit` date DEFAULT NULL,
                                      `case_management_abgebrochen_am` date DEFAULT NULL,
                                      `case_management_abgeschlossen_am` date DEFAULT NULL,
                                      `case_management_von` int(6) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                      `anonym` tinyint(1) DEFAULT NULL,
                                      `staatsb_sonstige` tinyint(1) DEFAULT NULL,
                                      `geburtsland_sonstige` tinyint(1) DEFAULT NULL,
                                      `feedback_am` date DEFAULT NULL,
                                      `beschreibung` text DEFAULT NULL,
                                      PRIMARY KEY (`ADRESSE_adnr`,`kstgr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_VERBESSERUNGSMASSNAHMEN`
--

DROP TABLE IF EXISTS `FIT2WORK_VERBESSERUNGSMASSNAHMEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_VERBESSERUNGSMASSNAHMEN` (
                                                    `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                    `parent_id` int(6) unsigned DEFAULT NULL,
                                                    `bezeichnung` char(250) DEFAULT NULL,
                                                    `bsa_code` int(6) unsigned DEFAULT NULL,
                                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FIT2WORK_ZUGANG`
--

DROP TABLE IF EXISTS `FIT2WORK_ZUGANG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FIT2WORK_ZUGANG` (
                                   `id` int(6) unsigned NOT NULL,
                                   `bezeichnung` varchar(200) DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FKOSTENSTELLE`
--

DROP TABLE IF EXISTS `FKOSTENSTELLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FKOSTENSTELLE` (
                                 `KSTKSTGR` int(3) unsigned NOT NULL,
                                 `KSTKSTNR` int(3) unsigned NOT NULL,
                                 `KSTKSTSUB` int(3) unsigned NOT NULL,
                                 `KSTbezeichnung` varchar(30) DEFAULT NULL,
                                 `KSTkurz` varchar(20) DEFAULT NULL,
                                 `KSTverantwortlich` varchar(50) DEFAULT NULL,
                                 `KSTbemerkung` text DEFAULT NULL,
                                 `KSTanschrift` text DEFAULT NULL,
                                 `KSTadrbez1` varchar(50) DEFAULT NULL,
                                 `KSTadrbez2` varchar(50) DEFAULT NULL,
                                 `KSTadrbez3` varchar(50) DEFAULT NULL,
                                 `KSTadrplz` varchar(6) DEFAULT NULL,
                                 `KSTadrort` varchar(50) DEFAULT NULL,
                                 `KSTadrstrasse` varchar(50) DEFAULT NULL,
                                 `KSTadrtel` varchar(20) DEFAULT NULL,
                                 `KSTadrfax` varchar(20) DEFAULT NULL,
                                 `KSTadremail` varchar(80) DEFAULT NULL,
                                 `KSTbereichsleiter` int(10) unsigned DEFAULT NULL COMMENT 'Verlinkung zur Trainertabelle für den Bereichsleiter',
                                 `KSTbuchsperre` enum('n','y') DEFAULT 'n',
                                 `KSTstatus` int(1) unsigned DEFAULT NULL,
                                 `KSTcognoscode` varchar(5) DEFAULT NULL,
                                 `KSTloek` enum('n','y') DEFAULT 'n',
                                 `KSTerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `KSTeruser` char(35) DEFAULT NULL,
                                 `KSTaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `KSTaeuser` char(35) DEFAULT NULL,
                                 PRIMARY KEY (`KSTKSTGR`,`KSTKSTNR`,`KSTKSTSUB`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FRAGEBOGEN`
--

DROP TABLE IF EXISTS `FRAGEBOGEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FRAGEBOGEN` (
                              `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                              `name` text DEFAULT NULL,
                              `calc_igws` tinyint(1) DEFAULT 0 COMMENT 'Berechne Integrationswahrscheinlichkeit anhand des Fragebogens',
                              `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `eruser` char(35) DEFAULT NULL,
                              `loek` enum('n','y') DEFAULT 'n',
                              `aktiv` tinyint(1) DEFAULT 1 COMMENT 'true=aktiv, false=inaktiv',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FRAGEBOGEN_ANTWORTEN`
--

DROP TABLE IF EXISTS `FRAGEBOGEN_ANTWORTEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FRAGEBOGEN_ANTWORTEN` (
                                        `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                        `teilnehmer_id` int(6) unsigned DEFAULT NULL,
                                        `projekt_id` int(6) unsigned DEFAULT NULL,
                                        `frage_id` int(6) unsigned DEFAULT NULL,
                                        `antwort` text DEFAULT NULL,
                                        `antwort_id` int(6) unsigned DEFAULT NULL,
                                        `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                        `eruser` char(35) DEFAULT NULL,
                                        `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                        `aeuser` char(35) DEFAULT NULL,
                                        PRIMARY KEY (`id`),
                                        KEY `teilnehmer_projekt_id` (`teilnehmer_id`,`projekt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=512102 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FRAGEBOGEN_FRAGE`
--

DROP TABLE IF EXISTS `FRAGEBOGEN_FRAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FRAGEBOGEN_FRAGE` (
                                    `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                    `fragebogen_id` int(6) unsigned DEFAULT NULL,
                                    `ueberfrage_id` int(6) unsigned DEFAULT NULL,
                                    `frage` text DEFAULT NULL,
                                    `typ` int(2) DEFAULT NULL COMMENT '0=Dropbox, 1=Text, 2=RGS',
                                    `ueberfrage` tinyint(1) DEFAULT 0 COMMENT 'Frage ist Überfrage',
                                    `sort` int(6) unsigned DEFAULT NULL COMMENT 'Sortierungsnummer',
                                    `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `eruser` char(35) DEFAULT NULL,
                                    `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `aeuser` char(35) DEFAULT NULL,
                                    `loek` enum('n','y') DEFAULT 'n',
                                    `validate` enum('numeric','alphaNumeric','date','email','none') DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FRAGEBOGEN_FRAGE_MOEGL_ANTWORTEN`
--

DROP TABLE IF EXISTS `FRAGEBOGEN_FRAGE_MOEGL_ANTWORTEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FRAGEBOGEN_FRAGE_MOEGL_ANTWORTEN` (
                                                    `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                    `frage_id` int(6) unsigned DEFAULT NULL,
                                                    `text` text DEFAULT NULL,
                                                    `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                    `eruser` char(35) DEFAULT NULL,
                                                    `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                    `aeuser` char(35) DEFAULT NULL,
                                                    `loek` enum('n','y') DEFAULT 'n',
                                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=343 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FRAGEBOGEN_PROJEKTZUWEISUNG`
--

DROP TABLE IF EXISTS `FRAGEBOGEN_PROJEKTZUWEISUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FRAGEBOGEN_PROJEKTZUWEISUNG` (
                                               `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                               `fragebogen_id` int(6) unsigned DEFAULT NULL,
                                               `projekt_id` int(6) unsigned DEFAULT NULL,
                                               `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                               `eruser` char(35) DEFAULT NULL,
                                               PRIMARY KEY (`id`),
                                               UNIQUE KEY `fragenbogen_projekt_id` (`fragebogen_id`,`projekt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=769 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GEHALT`
--

DROP TABLE IF EXISTS `GEHALT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GEHALT` (
                          `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                          `kv_stufe_id` int(6) unsigned DEFAULT NULL,
                          `gehalt` decimal(10,7) DEFAULT NULL,
                          `gueltig_von` date DEFAULT NULL,
                          `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                          `eruser` char(35) DEFAULT NULL,
                          `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                          `aeuser` char(35) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1480 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GEHALTSSCHEMA`
--

DROP TABLE IF EXISTS `GEHALTSSCHEMA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GEHALTSSCHEMA` (
                                 `DVtyp` enum('frei','fix') CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL DEFAULT 'frei',
                                 `DVlohngruppe` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'Lohngruppe',
                                 `DVlohnstufe` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'Lohnstufe',
                                 `GSgehalt` decimal(20,10) unsigned DEFAULT NULL COMMENT 'Gehaltsgrenze',
                                 `GSaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                                 `GSaeuser` varchar(35) DEFAULT NULL COMMENT 'Aenderungsbenutzer',
                                 `GSerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                 `GSeruser` varchar(35) NOT NULL,
                                 PRIMARY KEY (`DVtyp`,`DVlohngruppe`,`DVlohnstufe`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GERINGFUEGIGKEITSGRENZE`
--

DROP TABLE IF EXISTS `GERINGFUEGIGKEITSGRENZE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GERINGFUEGIGKEITSGRENZE` (
                                           `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                           `gueltig_ab` date DEFAULT NULL,
                                           `betrag` decimal(9,2) DEFAULT NULL,
                                           `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                           `eruser` char(35) DEFAULT NULL,
                                           `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                           `aeuser` char(35) DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `gueltig_ab` (`gueltig_ab`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GRUPPE`
--

DROP TABLE IF EXISTS `GRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GRUPPE` (
                          `GRnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'laufend Nummer der Gruppe',
                          `GRbez` char(50) DEFAULT NULL COMMENT 'Bezeichnung der Gruppe',
                          `GRtyp` enum('b','s') DEFAULT 'b' COMMENT 'Typ Benutzer/System',
                          `GRsystembez` enum('tr','tn','crm') DEFAULT NULL COMMENT 'Systembezeichnung',
                          `GRaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                          `GRaeuser` char(35) DEFAULT NULL,
                          `GRerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                          `GReruser` char(35) DEFAULT NULL,
                          PRIMARY KEY (`GRnr`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GR_AD`
--

DROP TABLE IF EXISTS `GR_AD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GR_AD` (
                         `GRUPPE_GRnr` int(6) unsigned NOT NULL COMMENT 'Gruppen-Nummer',
                         `ADRESSE_ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                         `GRADaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                         `GRADaeuser` char(35) DEFAULT NULL,
                         `GRADerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                         `GRADeruser` char(35) DEFAULT NULL,
                         PRIMARY KEY (`GRUPPE_GRnr`,`ADRESSE_ADadnr`),
                         KEY `GRUPPE_has_ADRESSE_FKIndex1` (`GRUPPE_GRnr`),
                         KEY `GRUPPE_has_ADRESSE_FKIndex2` (`ADRESSE_ADadnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GR_CRM_OLD`
--

DROP TABLE IF EXISTS `GR_CRM_OLD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GR_CRM_OLD` (
                              `GRUPPE_GRnr` int(6) unsigned NOT NULL COMMENT 'Gruppen-Nummer',
                              `CRM_OLD_ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                              `GRADaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                              `GRADaeuser` char(35) DEFAULT NULL,
                              `GRADerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                              `GRADeruser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`GRUPPE_GRnr`,`CRM_OLD_ADadnr`),
                              KEY `GRUPPE_has_CRM_OLD_FKIndex1` (`GRUPPE_GRnr`),
                              KEY `GRUPPE_has_CRM_OLD_FKIndex2` (`CRM_OLD_ADadnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HEROLD_ADRESSTYP`
--

DROP TABLE IF EXISTS `HEROLD_ADRESSTYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HEROLD_ADRESSTYP` (
                                    `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                    `bezeichnung` varchar(50) DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HEROLD_BRANCHE`
--

DROP TABLE IF EXISTS `HEROLD_BRANCHE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HEROLD_BRANCHE` (
                                  `id` int(6) unsigned NOT NULL,
                                  `branche_gruppe_id` int(6) unsigned DEFAULT NULL,
                                  `bezeichnung` varchar(100) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HEROLD_BRANCHE_GRUPPE`
--

DROP TABLE IF EXISTS `HEROLD_BRANCHE_GRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HEROLD_BRANCHE_GRUPPE` (
                                         `id` int(6) unsigned NOT NULL,
                                         `branche_kategorie_id` int(6) unsigned DEFAULT NULL,
                                         `bezeichnung` varchar(100) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HEROLD_BRANCHE_KATEGORIE`
--

DROP TABLE IF EXISTS `HEROLD_BRANCHE_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HEROLD_BRANCHE_KATEGORIE` (
                                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                            `bezeichnung` varchar(100) DEFAULT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HEROLD_FIRMA`
--

DROP TABLE IF EXISTS `HEROLD_FIRMA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HEROLD_FIRMA` (
                                `herold_nr` int(12) unsigned NOT NULL,
                                `ksv_nr` int(12) unsigned DEFAULT NULL,
                                `strasse` varchar(100) DEFAULT NULL,
                                `hausnr` varchar(50) DEFAULT NULL,
                                `plz` varchar(6) DEFAULT NULL,
                                `ort` varchar(100) DEFAULT NULL,
                                `bundesland_kuerzel` varchar(6) DEFAULT NULL,
                                `polit_bezirk` varchar(100) DEFAULT NULL,
                                `mitarbeiter_anz` int(12) unsigned DEFAULT NULL,
                                `rechtsform` varchar(100) DEFAULT NULL,
                                `fbnr` varchar(50) DEFAULT NULL,
                                `adresstyp` int(6) unsigned DEFAULT NULL,
                                `firmenname_mailings_1` varchar(100) DEFAULT NULL,
                                `firmenname_mailings_2` varchar(100) DEFAULT NULL,
                                `firmenname_mailings_3` varchar(100) DEFAULT NULL,
                                `firmenname_mailings_4` varchar(100) DEFAULT NULL,
                                `firmenname` varchar(100) DEFAULT NULL,
                                `firmenname_ksv` varchar(100) DEFAULT NULL,
                                `firmenanrede` varchar(100) DEFAULT NULL,
                                `tel_vorwahl_1` varchar(10) DEFAULT NULL,
                                `tel_vorwahl_2` varchar(10) DEFAULT NULL,
                                `tel_vorwahl_3` varchar(10) DEFAULT NULL,
                                `tel_vorwahl_4` varchar(10) DEFAULT NULL,
                                `tel_vorwahl_5` varchar(10) DEFAULT NULL,
                                `tel_1` varchar(30) DEFAULT NULL,
                                `tel_2` varchar(30) DEFAULT NULL,
                                `tel_3` varchar(30) DEFAULT NULL,
                                `tel_4` varchar(30) DEFAULT NULL,
                                `tel_5` varchar(30) DEFAULT NULL,
                                `fax_vorwahl_1` varchar(10) DEFAULT NULL,
                                `fax_vorwahl_2` varchar(10) DEFAULT NULL,
                                `fax_vorwahl_3` varchar(10) DEFAULT NULL,
                                `fax_vorwahl_4` varchar(10) DEFAULT NULL,
                                `fax_vorwahl_5` varchar(10) DEFAULT NULL,
                                `fax_1` varchar(30) DEFAULT NULL,
                                `fax_2` varchar(30) DEFAULT NULL,
                                `fax_3` varchar(30) DEFAULT NULL,
                                `fax_4` varchar(30) DEFAULT NULL,
                                `fax_5` varchar(30) DEFAULT NULL,
                                `email_1` varchar(100) DEFAULT NULL,
                                `email_2` varchar(100) DEFAULT NULL,
                                `email_3` varchar(100) DEFAULT NULL,
                                `email_4` varchar(100) DEFAULT NULL,
                                `email_5` varchar(100) DEFAULT NULL,
                                `internet_1` varchar(100) DEFAULT NULL,
                                `internet_2` varchar(100) DEFAULT NULL,
                                `internet_3` varchar(100) DEFAULT NULL,
                                `internet_4` varchar(100) DEFAULT NULL,
                                `internet_5` varchar(100) DEFAULT NULL,
                                `herold_branchenr_1` int(12) unsigned DEFAULT NULL,
                                `herold_branchenr_2` int(12) unsigned DEFAULT NULL,
                                `herold_branchenr_3` int(12) unsigned DEFAULT NULL,
                                `herold_branchenr_4` int(12) unsigned DEFAULT NULL,
                                `herold_branchenr_5` int(12) unsigned DEFAULT NULL,
                                PRIMARY KEY (`herold_nr`),
                                KEY `idx_firmenname` (`firmenname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HEROLD_FIRMA_BACKUP`
--

DROP TABLE IF EXISTS `HEROLD_FIRMA_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HEROLD_FIRMA_BACKUP` (
                                       `herold_nr` int(12) unsigned NOT NULL,
                                       `ksv_nr` int(12) unsigned DEFAULT NULL,
                                       `strasse` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `hausnr` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `plz` varchar(6) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `ort` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `bundesland_kuerzel` varchar(6) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `polit_bezirk` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `mitarbeiter_anz` int(12) unsigned DEFAULT NULL,
                                       `rechtsform` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fbnr` varchar(50) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `adresstyp` int(6) unsigned DEFAULT NULL,
                                       `firmenname_mailings_1` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `firmenname_mailings_2` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `firmenname_mailings_3` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `firmenname_mailings_4` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `firmenname` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `firmenname_ksv` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `firmenanrede` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_vorwahl_1` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_vorwahl_2` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_vorwahl_3` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_vorwahl_4` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_vorwahl_5` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_1` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_2` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_3` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_4` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `tel_5` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_vorwahl_1` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_vorwahl_2` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_vorwahl_3` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_vorwahl_4` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_vorwahl_5` varchar(10) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_1` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_2` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_3` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_4` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `fax_5` varchar(30) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `email_1` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `email_2` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `email_3` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `email_4` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `email_5` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `internet_1` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `internet_2` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `internet_3` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `internet_4` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `internet_5` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                       `herold_branchenr_1` int(12) unsigned DEFAULT NULL,
                                       `herold_branchenr_2` int(12) unsigned DEFAULT NULL,
                                       `herold_branchenr_3` int(12) unsigned DEFAULT NULL,
                                       `herold_branchenr_4` int(12) unsigned DEFAULT NULL,
                                       `herold_branchenr_5` int(12) unsigned DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IBIS_FIRMA`
--

DROP TABLE IF EXISTS `IBIS_FIRMA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IBIS_FIRMA` (
                              `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                              `name` char(50) DEFAULT NULL,
                              `kurzname` char(15) NOT NULL,
                              `cognos_code` varchar(20) DEFAULT NULL COMMENT 'Diese Kuerzel werden stammen aus NAVISION und werden in den Cognos Views verwendet',
                              `bmd_klient_id` int(6) unsigned DEFAULT NULL COMMENT 'Klient-Id in BMD',
                              `lhr_kz` char(15) DEFAULT 'IA' COMMENT 'LHR KZ Kennzahl \nprod: IA\ntest: AI',
                              `lhr_nr` int(6) DEFAULT NULL COMMENT 'LHR NR Firmen Nummer',
                              `saveable_for_new_items` tinyint(1) DEFAULT NULL COMMENT 'Gibt an, ob ein Eitrag für das Neuanlegen von Daten verendet werden darf. Unabhaengig davon, die Anzeige für alte Daten',
                              `fusszeiletxt` text DEFAULT NULL,
                              `reihung` int(2) unsigned DEFAULT NULL,
                              `erda` datetime DEFAULT NULL,
                              `eruser` char(35) DEFAULT NULL,
                              `aeda` datetime DEFAULT NULL,
                              `aeuser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IBIS_FIRMA_ZUGRIFFSRECHTE`
--

DROP TABLE IF EXISTS `IBIS_FIRMA_ZUGRIFFSRECHTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IBIS_FIRMA_ZUGRIFFSRECHTE` (
                                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                             `IBIS_FIRMA_id` int(6) unsigned DEFAULT NULL,
                                             `FKOSTENSTELLE_kstgr` int(3) unsigned DEFAULT NULL,
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IBIS_GS_EINSATZ_BINCODES`
--

DROP TABLE IF EXISTS `IBIS_GS_EINSATZ_BINCODES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IBIS_GS_EINSATZ_BINCODES` (
                                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                            `bezeichnung` varchar(50) NOT NULL,
                                            `kuerzel` varchar(10) NOT NULL,
                                            `bin_code` int(10) DEFAULT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Hilfstab f. Queries in DB. Codes siehe auch Klasse Adresse';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IBIS_KLIENT`
--

DROP TABLE IF EXISTS `IBIS_KLIENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IBIS_KLIENT` (
                               `ID` int(11) NOT NULL AUTO_INCREMENT,
                               `KLIENT_ID` tinyint(2) DEFAULT NULL COMMENT 'Klient-Id',
                               `KLIENT_NAME` varchar(100) DEFAULT NULL COMMENT 'Klient-Name',
                               PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JAG`
--

DROP TABLE IF EXISTS `JAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JAG` (
                       `id` int(11) NOT NULL AUTO_INCREMENT,
                       `ADadnr` int(11) DEFAULT NULL,
                       `kst` int(6) unsigned DEFAULT NULL,
                       `gefuehrt_durch` int(11) DEFAULT NULL,
                       `gefuehrt_am` date DEFAULT NULL,
                       `aktuelle_hauptfunktion_id` int(11) DEFAULT NULL,
                       `zusaetzliche_funktion_id` int(11) DEFAULT NULL COMMENT 'ist die ID aus job_function \njob_family inner join job_function',
                       `bei_fk_seit` datetime /* mariadb-5.3 */ DEFAULT NULL,
                       `zusaetzliche_aufgaben_ma` text DEFAULT NULL,
                       `taetigkeit_letztes_gespraech` tinyint(1) DEFAULT NULL,
                       `allgemines_kommentar` text DEFAULT NULL,
                       `zielvereinbarung_1` text DEFAULT NULL,
                       `zielvereinbarung_2` text DEFAULT NULL,
                       `zielvereinbarung_3` text DEFAULT NULL,
                       `thema_naechstes_monat_1` text DEFAULT NULL,
                       `thema_naechstes_monat_2` text DEFAULT NULL,
                       `thema_naechstes_monat_3` text DEFAULT NULL,
                       `ist_ziel_erreicht_1` int(11) DEFAULT NULL,
                       `ist_ziel_erreicht_2` int(11) DEFAULT NULL,
                       `ist_ziel_erreicht_3` int(11) DEFAULT NULL,
                       `situationsanalyse_1` text DEFAULT NULL,
                       `situationsanalyse_2` text DEFAULT NULL,
                       `situationsanalyse_3` text DEFAULT NULL,
                       `bis_wann_ziel_1` date DEFAULT NULL,
                       `bis_wann_ziel_2` date DEFAULT NULL,
                       `bis_wann_ziel_3` date DEFAULT NULL,
                       `unterstuetzende_massnahme_1` text DEFAULT NULL,
                       `unterstuetzende_massnahme_2` text DEFAULT NULL,
                       `unterstuetzende_massnahme_3` text DEFAULT NULL,
                       `branchenwissen` int(11) DEFAULT NULL,
                       `staerke_branchenwissen` text DEFAULT NULL,
                       `projektdesign_management` int(11) DEFAULT NULL,
                       `staerken_projektdesign_management` text DEFAULT NULL,
                       `bmk_verkauf_servicekompetenz` int(11) DEFAULT NULL,
                       `staerken_verkauf_servicekompetenz` text DEFAULT NULL,
                       `bmk_verbesserungsposition_1` text DEFAULT NULL,
                       `bmk_verbesserungsposition_2` text DEFAULT NULL,
                       `bmk_verbesserungsposition_3` text DEFAULT NULL,
                       `gewinn_orientierend` int(11) DEFAULT NULL,
                       `staerken_gewinn` text DEFAULT NULL,
                       `steuerungssouveraenitaet` int(11) DEFAULT NULL,
                       `staerken_steuerung` text DEFAULT NULL,
                       `strategisches_geschick` int(11) DEFAULT NULL,
                       `staerken_geschick` text DEFAULT NULL,
                       `wk_verbesserungspotenziale_1` text DEFAULT NULL,
                       `wk_verbesserungspotenziale_2` text DEFAULT NULL,
                       `wk_verbesserungspotenziale_3` text DEFAULT NULL,
                       `selbstmanagement` int(11) DEFAULT NULL,
                       `staerken_selbstmanagement` text DEFAULT NULL,
                       `komplexitaetsmanagement` int(11) DEFAULT NULL,
                       `staerken_komplexitaetsmanagement` text DEFAULT NULL,
                       `entscheidungsfaehigkeit` int(11) DEFAULT NULL,
                       `staerken_entscheidungsfaehigkeit` text DEFAULT NULL,
                       `fk_verbesserungspotenziale_1` text DEFAULT NULL,
                       `fk_verbesserungspotenziale_2` text DEFAULT NULL,
                       `fk_verbesserungspotenziale_3` text DEFAULT NULL,
                       `sicht_ma` text DEFAULT NULL,
                       `sicht_fk` text DEFAULT NULL,
                       `gemeinsame_sicht` text DEFAULT NULL,
                       `funktion_sicht_ma_id` int(11) DEFAULT NULL COMMENT '// diese id ist diejenige aus der jobfunktion\njob_family inner join job_function',
                       `funktion_sicht_fk_id` int(11) DEFAULT NULL,
                       `entwicklungsmassnahme_1` text DEFAULT NULL,
                       `entwicklungsmassnahme_2` text DEFAULT NULL,
                       `entwicklungsmassnahme_3` text DEFAULT NULL,
                       `durch_wen_1` varchar(255) DEFAULT NULL,
                       `durch_wen_2` varchar(255) DEFAULT NULL,
                       `durch_wen_3` varchar(255) DEFAULT NULL,
                       `bis_wann_1` date DEFAULT NULL,
                       `bis_wann_2` date DEFAULT NULL,
                       `bis_wann_3` date DEFAULT NULL,
                       `goal_bis_wann_1` date DEFAULT NULL,
                       `goal_bis_wann_2` date DEFAULT NULL,
                       `goal_bis_wann_3` date DEFAULT NULL,
                       `schluesselkraft` tinyint(4) DEFAULT NULL,
                       `aeuser` varchar(45) DEFAULT NULL,
                       `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                       `eruser` varchar(45) DEFAULT NULL,
                       `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                       PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=881 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JAG_BEARBEITER`
--

DROP TABLE IF EXISTS `JAG_BEARBEITER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JAG_BEARBEITER` (
                                  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                  `adnr` int(6) unsigned NOT NULL,
                                  `kstgr` int(3) unsigned NOT NULL,
                                  `zugriff_ab` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `zugriff_bis` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `ansehen_ab` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `ansehen_bis` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `aeuser` char(35) DEFAULT NULL,
                                  `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `eruser` char(35) DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `idx_kst_adnr` (`kstgr`,`adnr`),
                                  KEY `idx_kst` (`kstgr`),
                                  KEY `idx_adnr` (`adnr`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JAG_DOC`
--

DROP TABLE IF EXISTS `JAG_DOC`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JAG_DOC` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `jag_id` int(11) NOT NULL,
                           `sySTORE_STnr` int(10) NOT NULL,
                           `ADRESSE_ADadnr` int(10) DEFAULT NULL,
                           `doc_type` int(1) DEFAULT NULL,
                           `gefuehrt_von` varchar(100) DEFAULT NULL,
                           `gefuehrt_am` date DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=728 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Dokumentenupload der Jahresgespräche';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOB`
--

DROP TABLE IF EXISTS `JOB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOB` (
                       `JBnr` int(6) NOT NULL AUTO_INCREMENT COMMENT 'lfd. Job-Nummer',
                       `JBkey` varchar(30) DEFAULT NULL,
                       `JBbezeichnung` char(80) DEFAULT NULL COMMENT 'Job-Bezeichnung',
                       `JBbeschreibung` text DEFAULT NULL COMMENT 'Job-Beschreibung',
                       `JBphppgm` char(128) DEFAULT NULL COMMENT 'PHP-Programm',
                       `JBappcontrolname` char(128) DEFAULT NULL COMMENT 'AppControl-Name',
                       `JBappcontrolstoreid` int(9) DEFAULT NULL,
                       `JBdownloadTemplate` text NOT NULL,
                       `JBstatus` enum('a','i') DEFAULT NULL COMMENT 'Job-Status',
                       `JBerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                       `JBeruser` char(35) DEFAULT NULL COMMENT 'Erfasungsbenutzer',
                       `JBaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                       `JBaeuser` char(35) DEFAULT NULL COMMENT 'Änderungsbenutzer',
                       PRIMARY KEY (`JBnr`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOBHELP`
--

DROP TABLE IF EXISTS `JOBHELP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOBHELP` (
                           `ID` int(6) NOT NULL AUTO_INCREMENT,
                           `BERUF` varchar(255) DEFAULT NULL,
                           `berufeliste_id` int(6) NOT NULL,
                           `kynr_id` int(6) NOT NULL,
                           PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=214 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOBPOS`
--

DROP TABLE IF EXISTS `JOBPOS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOBPOS` (
                          `JOB_JBnr` int(6) NOT NULL COMMENT 'Job-Nummer',
                          `JPdockey` char(40) NOT NULL COMMENT 'Dokument/Vorlagen-Schlüssel',
                          `JPtyp` char(3) DEFAULT NULL COMMENT 'Typ',
                          `JPbezeichnung` char(128) DEFAULT NULL COMMENT 'Bezeichnung',
                          `JPfilename` char(128) DEFAULT NULL COMMENT 'Datei-Name',
                          `JPstoreid` int(9) DEFAULT NULL COMMENT 'Store-ID',
                          `JPstatus` enum('a','i') DEFAULT NULL COMMENT 'Status',
                          `JPerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                          `JPeruser` char(35) DEFAULT NULL COMMENT 'Erfassungsbenutzer',
                          `JPaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                          `JPaeuser` char(35) DEFAULT NULL COMMENT 'Änderungsbenutzer',
                          PRIMARY KEY (`JOB_JBnr`,`JPdockey`),
                          KEY `JOBPOS_FKIndex1` (`JOB_JBnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOB_FAMILY`
--

DROP TABLE IF EXISTS `JOB_FAMILY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOB_FAMILY` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `name` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOB_FAMILY_hasMany_Function`
--

DROP TABLE IF EXISTS `JOB_FAMILY_hasMany_Function`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOB_FAMILY_hasMany_Function` (
                                               `id` int(11) NOT NULL AUTO_INCREMENT,
                                               `family_id` int(11) NOT NULL,
                                               `function_id` int(11) NOT NULL,
                                               PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=38 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JOB_FUNCTION`
--

DROP TABLE IF EXISTS `JOB_FUNCTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JOB_FUNCTION` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `name` varchar(255) DEFAULT NULL,
                                `jag_schuetzen` tinyint(1) unsigned NOT NULL DEFAULT 0,
                                PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JUNET_FACHBEREICH`
--

DROP TABLE IF EXISTS `JUNET_FACHBEREICH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JUNET_FACHBEREICH` (
                                     `id` int(11) NOT NULL AUTO_INCREMENT,
                                     `bezeichnung` varchar(255) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JUNET_IBA_AUSB`
--

DROP TABLE IF EXISTS `JUNET_IBA_AUSB`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JUNET_IBA_AUSB` (
                                  `id` int(3) NOT NULL,
                                  `bezeichnung` varchar(100) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JUNET_IBA_TSTATUS`
--

DROP TABLE IF EXISTS `JUNET_IBA_TSTATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JUNET_IBA_TSTATUS` (
                                     `id` int(3) NOT NULL,
                                     `bezeichnung` varchar(100) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JUNET_SM_TN_ZUSATZ`
--

DROP TABLE IF EXISTS `JUNET_SM_TN_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JUNET_SM_TN_ZUSATZ` (
                                      `JNid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                      `JNTN_ADRESSE_ADadnr` int(6) unsigned DEFAULT NULL,
                                      `JNTN_SEMINAR_SMnr` int(6) DEFAULT NULL,
                                      `JNTN_Jobcoach_ADnr` int(11) DEFAULT NULL,
                                      `JNdatum` date DEFAULT NULL,
                                      `JN_IBAteilnahmestatus` int(3) DEFAULT 0,
                                      `JN_IBAausbverhaeltnis` int(3) DEFAULT NULL,
                                      `JN_Fachbereich` int(3) DEFAULT NULL,
                                      PRIMARY KEY (`JNid`)
) ENGINE=InnoDB AUTO_INCREMENT=1765 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KASSABUCH`
--

DROP TABLE IF EXISTS `KASSABUCH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KASSABUCH` (
                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                             `ibis_firma_id` int(6) unsigned NOT NULL,
                             `kassabuch_nr` int(6) unsigned NOT NULL,
                             `name` char(35) NOT NULL,
                             `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                             `eruser` char(35) DEFAULT NULL,
                             `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                             `aeuser` char(35) DEFAULT NULL,
                             `loek` enum('n','y') DEFAULT 'n',
                             PRIMARY KEY (`id`),
                             KEY `FOREIGN_IBIS_FIRMA_ID` (`ibis_firma_id`),
                             CONSTRAINT `KASSABUCH_ibfk_1` FOREIGN KEY (`ibis_firma_id`) REFERENCES `IBIS_FIRMA` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KASSABUCH_BARSTAND`
--

DROP TABLE IF EXISTS `KASSABUCH_BARSTAND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KASSABUCH_BARSTAND` (
                                      `kassabuch_monat_id` int(6) unsigned NOT NULL COMMENT 'Kassabuch_monat.id',
                                      `500er` int(3) unsigned DEFAULT NULL,
                                      `200er` int(3) unsigned DEFAULT NULL,
                                      `100er` int(3) unsigned DEFAULT NULL,
                                      `50er` int(3) unsigned DEFAULT NULL,
                                      `20er` int(3) unsigned DEFAULT NULL,
                                      `10er` int(3) unsigned DEFAULT NULL,
                                      `5er` int(3) unsigned DEFAULT NULL,
                                      `2er` int(3) unsigned DEFAULT NULL,
                                      `1er` int(3) unsigned DEFAULT NULL,
                                      `05er` int(3) unsigned DEFAULT NULL,
                                      `02er` int(3) unsigned DEFAULT NULL,
                                      `01er` int(3) unsigned DEFAULT NULL,
                                      `005er` int(3) unsigned DEFAULT NULL,
                                      `002er` int(3) unsigned DEFAULT NULL,
                                      `001er` int(3) unsigned DEFAULT NULL,
                                      `einnahmen_folgende` decimal(10,2) unsigned DEFAULT NULL COMMENT 'Einnahmen Folgemonat',
                                      `ausgaben_folgende` decimal(10,2) unsigned DEFAULT NULL COMMENT 'Ausgaben Folgemonat',
                                      PRIMARY KEY (`kassabuch_monat_id`),
                                      CONSTRAINT `KASSABUCH_BARSTAND_ibfk_1` FOREIGN KEY (`kassabuch_monat_id`) REFERENCES `KASSABUCH_MONAT` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KASSABUCH_BENUTZER`
--

DROP TABLE IF EXISTS `KASSABUCH_BENUTZER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KASSABUCH_BENUTZER` (
                                      `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                      `kassabuch_id` int(6) unsigned NOT NULL,
                                      `adresse_adnr` int(6) unsigned NOT NULL,
                                      `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                      `eruser` char(35) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `FOREIGN_KASSABUCH_ID` (`kassabuch_id`),
                                      CONSTRAINT `KASSABUCH_BENUTZER_ibfk_1` FOREIGN KEY (`kassabuch_id`) REFERENCES `KASSABUCH` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=977 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KASSABUCH_EINTRAEGE`
--

DROP TABLE IF EXISTS `KASSABUCH_EINTRAEGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KASSABUCH_EINTRAEGE` (
                                       `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                       `kassabuch_monat_id` int(6) unsigned DEFAULT NULL COMMENT 'Kassabuch_monat.id',
                                       `lfd_beleg_monat` int(3) unsigned DEFAULT NULL,
                                       `datum` date DEFAULT NULL,
                                       `betrag` decimal(7,2) DEFAULT NULL,
                                       `typ` enum('e','a','u') DEFAULT NULL,
                                       `kstgr` int(3) unsigned DEFAULT NULL,
                                       `kstnr` int(3) unsigned DEFAULT NULL,
                                       `kstkostentraeger` int(7) unsigned DEFAULT NULL,
                                       `beschreibung` char(50) DEFAULT NULL,
                                       `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `eruser` char(35) DEFAULT NULL,
                                       `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `aeuser` char(35) DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       KEY `FOREIGN_KASSABUCH_MONAT_ID` (`kassabuch_monat_id`),
                                       CONSTRAINT `KASSABUCH_EINTRAEGE_ibfk_1` FOREIGN KEY (`kassabuch_monat_id`) REFERENCES `KASSABUCH_MONAT` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=154037 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KASSABUCH_MONAT`
--

DROP TABLE IF EXISTS `KASSABUCH_MONAT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KASSABUCH_MONAT` (
                                   `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                   `monat` int(2) unsigned NOT NULL,
                                   `jahr` int(4) unsigned NOT NULL,
                                   `kassabuch_id` int(6) unsigned NOT NULL,
                                   `geprueft_durch` char(35) DEFAULT NULL COMMENT 'User, welcher Beleg unterschreiben muss',
                                   `abgeschlossen_am` datetime DEFAULT NULL,
                                   `abgeschlossen_durch` char(35) DEFAULT NULL,
                                   `saldo` decimal(7,2) DEFAULT NULL,
                                   `erda` datetime DEFAULT NULL,
                                   `eruser` char(35) DEFAULT NULL,
                                   `aeda` datetime DEFAULT NULL,
                                   `aeuser` char(35) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `FOREIGN_KASSABUCH_ID` (`kassabuch_id`),
                                   CONSTRAINT `KASSABUCH_MONAT_ibfk_1` FOREIGN KEY (`kassabuch_id`) REFERENCES `KASSABUCH` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4913 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KASSABUCH_PRUEFER`
--

DROP TABLE IF EXISTS `KASSABUCH_PRUEFER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KASSABUCH_PRUEFER` (
                                     `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                     `adresse_adnr` int(6) unsigned NOT NULL,
                                     `kassabuch_id` int(6) unsigned NOT NULL,
                                     `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     `eruser` char(35) DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `FOREIGN_KASSABUCH_ID` (`kassabuch_id`),
                                     CONSTRAINT `KASSABUCH_PRUEFER_ibfk_1` FOREIGN KEY (`kassabuch_id`) REFERENCES `KASSABUCH` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KENNTNISSE`
--

DROP TABLE IF EXISTS `KENNTNISSE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KENNTNISSE` (
                              `KEid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                              `ADRESSE_ADadnr` int(6) unsigned NOT NULL DEFAULT 0,
                              `KEbereich` char(1) DEFAULT NULL,
                              `KEbezeichnung` varchar(50) DEFAULT NULL,
                              `KElevel` varchar(30) DEFAULT NULL,
                              `KEloek` enum('n','y') NOT NULL DEFAULT 'n',
                              `KEaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `KEaeuser` char(35) DEFAULT NULL,
                              `KEerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `KEeruser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`KEid`,`ADRESSE_ADadnr`),
                              KEY `KENNTNISSE_FKIndex1` (`ADRESSE_ADadnr`)
) ENGINE=MyISAM AUTO_INCREMENT=50534 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KEYTABLE`
--

DROP TABLE IF EXISTS `KEYTABLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KEYTABLE` (
                            `KYnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Key-Nummer',
                            `KYname` varchar(20) DEFAULT NULL COMMENT 'z.B.: ort, funktion, referat, bereitart, land',
                            `KYindex` int(3) unsigned DEFAULT NULL COMMENT 'Key-Index, Sortierbegriff',
                            `KYvaluet1` varchar(100) DEFAULT NULL COMMENT 'Value Text 1',
                            `KYvaluet2` varchar(100) DEFAULT NULL COMMENT 'Value Text 2',
                            `KYvaluet3` varchar(100) DEFAULT NULL COMMENT 'Value Text 3',
                            `KYvaluet4` varchar(100) DEFAULT NULL COMMENT 'Value Text 4',
                            `KYvaluet5` varchar(100) DEFAULT NULL COMMENT 'DPW ABW Code',
                            `KYvaluem1` text DEFAULT NULL COMMENT 'Memo Text 1',
                            `KYvaluenum1` int(10) unsigned DEFAULT NULL COMMENT 'Value numerisch 1',
                            `KYvaluenum2` int(10) unsigned DEFAULT NULL COMMENT 'Value numerisch 2',
                            `KYvaluedate1` int(10) unsigned DEFAULT NULL COMMENT 'Value Datum 1',
                            `KYvaluedate2` int(10) unsigned DEFAULT NULL COMMENT 'Value Datum 2',
                            `KYvisible` tinyint(1) DEFAULT NULL COMMENT 'Key sichtbar',
                            `KYbemerkung` int(10) unsigned DEFAULT NULL COMMENT 'Key Bemerkung',
                            `KYcodeart` char(4) NOT NULL DEFAULT 'user',
                            `KYloek` enum('n','y') DEFAULT 'n',
                            `KYaeda` datetime DEFAULT NULL COMMENT 'Aenderungsdatum',
                            `KYaeuser` char(35) DEFAULT NULL,
                            `KYerda` datetime DEFAULT NULL COMMENT 'Erstellungsdatum',
                            `KYeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`KYnr`),
                            KEY `KEYTABLE_KYname_Primary` (`KYname`,`KYnr`),
                            FULLTEXT KEY `KEYTABLE_KYname` (`KYname`)
) ENGINE=MyISAM AUTO_INCREMENT=4616 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KEYTABLE_REGION`
--

DROP TABLE IF EXISTS `KEYTABLE_REGION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KEYTABLE_REGION` (
                                   `KYnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Key-Nummer',
                                   `KYname` varchar(20) DEFAULT NULL COMMENT 'z.B.: ort, funktion, referat, bereitart, land',
                                   `KYindex` int(3) unsigned DEFAULT NULL COMMENT 'Key-Index, Sortierbegriff',
                                   `KYvaluet1` varchar(100) DEFAULT NULL COMMENT 'Value Text 1',
                                   `KYvaluet2` varchar(100) DEFAULT NULL COMMENT 'Value Text 2',
                                   `KYvaluet3` varchar(100) DEFAULT NULL COMMENT 'Value Text 3',
                                   `KYvaluet4` varchar(100) DEFAULT NULL COMMENT 'Value Text 4',
                                   `KYvaluem1` text DEFAULT NULL COMMENT 'Memo Text 1',
                                   `KYvaluenum1` int(10) unsigned DEFAULT NULL COMMENT 'Value numerisch 1',
                                   `KYvaluenum2` int(10) unsigned DEFAULT NULL COMMENT 'Value numerisch 2',
                                   `KYvaluedate1` int(10) unsigned DEFAULT NULL COMMENT 'Value Datum 1',
                                   `KYvaluedate2` int(10) unsigned DEFAULT NULL COMMENT 'Value Datum 2',
                                   `KYvisible` tinyint(1) DEFAULT NULL COMMENT 'Key sichtbar',
                                   `KYbemerkung` int(10) unsigned DEFAULT NULL COMMENT 'Key Bemerkung',
                                   `KYloek` enum('n','y') DEFAULT NULL COMMENT 'Loeschkennzeichen',
                                   `KYaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                                   `KYaeuser` char(35) DEFAULT NULL,
                                   `KYerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                   `KYeruser` char(35) DEFAULT NULL,
                                   PRIMARY KEY (`KYnr`)
) ENGINE=MyISAM AUTO_INCREMENT=2819 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KINDER`
--

DROP TABLE IF EXISTS `KINDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KINDER` (
                          `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                          `ADRESSE_adnr` int(6) unsigned DEFAULT NULL,
                          `vorname` varchar(100) DEFAULT NULL,
                          `nachname` varchar(100) DEFAULT NULL,
                          `svnr` varchar(50) DEFAULT NULL,
                          `gebdatum` date DEFAULT NULL,
                          `volljaehrig` enum('n','y') DEFAULT 'n',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1655 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KK_DOKUMENTE`
--

DROP TABLE IF EXISTS `KK_DOKUMENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KK_DOKUMENTE` (
                                `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                `KUNDENKONTROLLE_id` int(6) unsigned DEFAULT NULL,
                                `sySTORE_STnr` int(9) unsigned DEFAULT NULL,
                                `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `aeuser` char(35) DEFAULT NULL,
                                `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `eruser` char(35) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KK_KATEGORIE`
--

DROP TABLE IF EXISTS `KK_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KK_KATEGORIE` (
                                `id` int(6) unsigned NOT NULL,
                                `bezeichnung` char(100) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KK_PERSONEN_ADRESSE`
--

DROP TABLE IF EXISTS `KK_PERSONEN_ADRESSE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KK_PERSONEN_ADRESSE` (
                                       `KUNDENKONTROLLE_id` int(6) unsigned NOT NULL DEFAULT 0,
                                       `ADRESSE_adnr` int(6) unsigned NOT NULL DEFAULT 0,
                                       PRIMARY KEY (`KUNDENKONTROLLE_id`,`ADRESSE_adnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KK_PERSONEN_CRM`
--

DROP TABLE IF EXISTS `KK_PERSONEN_CRM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KK_PERSONEN_CRM` (
                                   `KUNDENKONTROLLE_id` int(6) unsigned NOT NULL DEFAULT 0,
                                   `CRM_MITARBEITER_id` int(6) unsigned NOT NULL DEFAULT 0,
                                   PRIMARY KEY (`KUNDENKONTROLLE_id`,`CRM_MITARBEITER_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KK_PERSONEN_EXTERN`
--

DROP TABLE IF EXISTS `KK_PERSONEN_EXTERN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KK_PERSONEN_EXTERN` (
                                      `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                      `KUNDENKONTROLLE_id` int(6) unsigned DEFAULT NULL,
                                      `vorname` varchar(50) DEFAULT NULL,
                                      `nachname` varchar(50) DEFAULT NULL,
                                      `institution` text DEFAULT NULL,
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KOLLEKTIVVERTRAG`
--

DROP TABLE IF EXISTS `KOLLEKTIVVERTRAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KOLLEKTIVVERTRAG` (
                                    `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                    `bezeichnung` varchar(100) DEFAULT NULL,
                                    `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `eruser` char(35) DEFAULT NULL,
                                    `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `aeuser` char(35) DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KSTTNstatus`
--

DROP TABLE IF EXISTS `KSTTNstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KSTTNstatus` (
                               `KSTTNkynr` int(6) NOT NULL,
                               `TEILNAHMESTATUS_BEREICH_id` int(6) unsigned NOT NULL,
                               `KSTTNkyindex` int(3) unsigned DEFAULT NULL,
                               `KSTTNkuerzel` varchar(5) DEFAULT NULL,
                               `KSTTNbez` varchar(35) DEFAULT NULL,
                               `KSTTNloek` enum('n','y') DEFAULT 'n',
                               `KSTTNaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `KSTTNaeuser` char(35) DEFAULT NULL,
                               `KSTTNerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `KSTTNeruser` char(35) DEFAULT NULL,
                               `DPWCode` varchar(9) DEFAULT NULL,
                               PRIMARY KEY (`KSTTNkynr`,`TEILNAHMESTATUS_BEREICH_id`),
                               KEY `TEILNAHMESTATUS_BEREICH_id` (`TEILNAHMESTATUS_BEREICH_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KUNDENKONTROLLE`
--

DROP TABLE IF EXISTS `KUNDENKONTROLLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KUNDENKONTROLLE` (
                                   `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                   `AUSSCHREIBUNG_nr` int(6) unsigned DEFAULT NULL,
                                   `datum` date DEFAULT NULL,
                                   `KK_KATEGORIE_id` int(6) unsigned DEFAULT NULL,
                                   `beanstandung_inhalt` tinyint(1) DEFAULT NULL,
                                   `beanstandung_personal` tinyint(1) DEFAULT NULL,
                                   `beanstandung_ausstattung` tinyint(1) DEFAULT NULL,
                                   `beanstandung_sonstiges` char(100) DEFAULT NULL,
                                   `bemerkung` text DEFAULT NULL,
                                   `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `aeuser` char(35) DEFAULT NULL,
                                   `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `eruser` char(35) DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KV_STUFE`
--

DROP TABLE IF EXISTS `KV_STUFE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KV_STUFE` (
                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                            `kv_verwendungsgruppe_id` int(6) unsigned DEFAULT NULL,
                            `bezeichnung` varchar(100) DEFAULT NULL,
                            `jahre_von` int(6) unsigned DEFAULT NULL,
                            `jahre_bis` int(6) unsigned DEFAULT NULL,
                            `gueltig_von` date DEFAULT NULL,
                            `gueltig_bis` date DEFAULT NULL,
                            `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `eruser` char(35) DEFAULT NULL,
                            `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `aeuser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=319 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KV_STUFE_BAK`
--

DROP TABLE IF EXISTS `KV_STUFE_BAK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KV_STUFE_BAK` (
                                `id` int(6) unsigned NOT NULL DEFAULT 0,
                                `kv_verwendungsgruppe_id` int(6) unsigned DEFAULT NULL,
                                `bezeichnung` varchar(100) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `jahre_von` int(6) unsigned DEFAULT NULL,
                                `jahre_bis` int(6) unsigned DEFAULT NULL,
                                `gehalt` decimal(10,7) DEFAULT NULL,
                                `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `eruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `aeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `KV_VERWENDUNGSGRUPPE`
--

DROP TABLE IF EXISTS `KV_VERWENDUNGSGRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `KV_VERWENDUNGSGRUPPE` (
                                        `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                        `kollektivvertrag_id` int(6) unsigned DEFAULT NULL,
                                        `bezeichnung` varchar(100) DEFAULT NULL,
                                        `bmd_id` tinyint(3) unsigned DEFAULT NULL COMMENT 'ID im BMD abhängig vom KV',
                                        `gueltig_von` date DEFAULT NULL,
                                        `gueltig_bis` date DEFAULT NULL,
                                        `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                        `eruser` char(35) DEFAULT NULL,
                                        `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                        `aeuser` char(35) DEFAULT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LAGERSTAND`
--

DROP TABLE IF EXISTS `LAGERSTAND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LAGERSTAND` (
                              `RSresID` int(6) NOT NULL COMMENT 'Ressourcen-ID',
                              `RAUM_SOstandortid` int(6) NOT NULL COMMENT 'Standort-Nummer',
                              `RAUM_RAraumid` int(6) NOT NULL COMMENT 'Raum-Nummer',
                              `LBdatum` date DEFAULT NULL COMMENT 'Datum',
                              `LGanzahl` int(6) DEFAULT NULL COMMENT 'Anzahl',
                              `LGerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungs-Datum',
                              `LGeruser` char(35) DEFAULT NULL,
                              `LGaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungs-Datum',
                              `LGaeuser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`RSresID`,`RAUM_SOstandortid`,`RAUM_RAraumid`),
                              KEY `STANDORT_has_RESSOURCE_FKIndex2` (`RSresID`),
                              KEY `LAGERSTAND_FKIndex2` (`RAUM_RAraumid`,`RAUM_SOstandortid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LAGERSTAND_BACKUP`
--

DROP TABLE IF EXISTS `LAGERSTAND_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LAGERSTAND_BACKUP` (
                                     `RSresID` int(6) NOT NULL COMMENT 'Ressourcen-ID',
                                     `RAUM_SOstandortid` int(6) NOT NULL COMMENT 'Standort-Nummer',
                                     `RAUM_RAraumid` int(6) NOT NULL COMMENT 'Raum-Nummer',
                                     `LBdatum` date DEFAULT NULL COMMENT 'Datum',
                                     `LGanzahl` int(6) DEFAULT NULL COMMENT 'Anzahl',
                                     `LGerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungs-Datum',
                                     `LGeruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                     `LGaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungs-Datum',
                                     `LGaeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LIZENZ`
--

DROP TABLE IF EXISTS `LIZENZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LIZENZ` (
                          `orgid` varchar(9) NOT NULL DEFAULT '' COMMENT 'Organisations-ID',
                          `orgname` varchar(50) NOT NULL DEFAULT '' COMMENT 'Organisations-Name',
                          PRIMARY KEY (`orgid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LV_BENACHRICHTIGUNG`
--

DROP TABLE IF EXISTS `LV_BENACHRICHTIGUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LV_BENACHRICHTIGUNG` (
                                       `ADRESSE_adnr` int(6) unsigned NOT NULL,
                                       `titel_datum` date DEFAULT NULL,
                                       `tiel_aeuser` char(35) DEFAULT NULL,
                                       `titel_alt` int(6) unsigned DEFAULT NULL,
                                       `titel_neu` int(6) unsigned DEFAULT NULL,
                                       `vorname_datum` date DEFAULT NULL,
                                       `vorname_aeuser` char(35) DEFAULT NULL,
                                       `vorname_alt` varchar(50) DEFAULT NULL,
                                       `vorname_neu` varchar(50) DEFAULT NULL,
                                       `nachname_datum` date DEFAULT NULL,
                                       `nachname_aeuser` char(35) DEFAULT NULL,
                                       `nachname_alt` varchar(50) DEFAULT NULL,
                                       `nachname_neu` varchar(50) DEFAULT NULL,
                                       `lkz_datum` date DEFAULT NULL,
                                       `lkz_aeuser` char(35) DEFAULT NULL,
                                       `lkz_alt` varchar(6) DEFAULT NULL,
                                       `lkz_neu` varchar(6) DEFAULT NULL,
                                       `plz_datum` date DEFAULT NULL,
                                       `plz_aeuser` char(35) DEFAULT NULL,
                                       `plz_alt` varchar(6) DEFAULT NULL,
                                       `plz_neu` varchar(6) DEFAULT NULL,
                                       `ort_datum` date DEFAULT NULL,
                                       `ort_aeuser` char(35) DEFAULT NULL,
                                       `ort_alt` varchar(50) DEFAULT NULL,
                                       `ort_neu` varchar(50) DEFAULT NULL,
                                       `strasse_datum` date DEFAULT NULL,
                                       `strasse_aeuser` char(35) DEFAULT NULL,
                                       `strasse_alt` varchar(50) DEFAULT NULL,
                                       `strasse_neu` varchar(50) DEFAULT NULL,
                                       `famstand_datum` date DEFAULT NULL,
                                       `famstand_aeuser` char(35) DEFAULT NULL,
                                       `famstand_alt` int(6) unsigned DEFAULT NULL,
                                       `famstand_neu` int(6) unsigned DEFAULT NULL,
                                       `staatsb_datum` date DEFAULT NULL,
                                       `staatsb_aeuser` char(35) DEFAULT NULL,
                                       `staatsb_alt` int(6) DEFAULT NULL,
                                       `staatsb_neu` int(6) DEFAULT NULL,
                                       `bank_datum` date DEFAULT NULL,
                                       `bank_aeuser` char(35) DEFAULT NULL,
                                       `bank_alt` varchar(40) DEFAULT NULL,
                                       `bank_neu` varchar(40) DEFAULT NULL,
                                       `bic_datum` date DEFAULT NULL,
                                       `bic_aeuser` char(35) DEFAULT NULL,
                                       `bic_alt` varchar(11) DEFAULT NULL,
                                       `bic_neu` varchar(11) DEFAULT NULL,
                                       `iban_datum` date DEFAULT NULL,
                                       `iban_aeuser` char(35) DEFAULT NULL,
                                       `iban_alt` varchar(34) DEFAULT NULL,
                                       `iban_neu` varchar(34) DEFAULT NULL,
                                       `blz_datum` date DEFAULT NULL,
                                       `blz_aeuser` char(35) DEFAULT NULL,
                                       `blz_alt` varchar(50) DEFAULT NULL,
                                       `blz_neu` varchar(50) DEFAULT NULL,
                                       `bankkonto_datum` date DEFAULT NULL,
                                       `bankkonto_aeuser` char(35) DEFAULT NULL,
                                       `bankkonto_alt` varchar(12) DEFAULT NULL,
                                       `bankkonto_neu` varchar(12) DEFAULT NULL,
                                       `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `eruser` char(35) DEFAULT NULL,
                                       `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `aeuser` char(35) DEFAULT NULL,
                                       PRIMARY KEY (`ADRESSE_adnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LV_LINKVERWALTUNG`
--

DROP TABLE IF EXISTS `LV_LINKVERWALTUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LV_LINKVERWALTUNG` (
                                     `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                     `bezeichnung` char(255) DEFAULT NULL,
                                     `link` char(255) DEFAULT NULL,
                                     `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     `eruser` char(35) DEFAULT NULL,
                                     `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     `aeuser` char(35) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MAIL`
--

DROP TABLE IF EXISTS `MAIL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MAIL` (
                        `MAnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                        `MAfromuserid` varchar(15) DEFAULT NULL,
                        `MAfromadress` char(128) DEFAULT NULL,
                        `MAsubject` char(128) DEFAULT NULL,
                        `MAbodyTxt` text DEFAULT NULL,
                        `MAbodyHtm` text DEFAULT NULL,
                        `MAstatus` int(1) unsigned DEFAULT NULL,
                        `MAtype` int(1) unsigned DEFAULT 1,
                        `MAexectime` datetime /* mariadb-5.3 */ DEFAULT NULL,
                        `MAexecutor` char(15) DEFAULT NULL,
                        `MAloek` enum('n','y') DEFAULT 'n',
                        `MAaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                        `MAaeuser` char(35) DEFAULT NULL,
                        `MAerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                        `MAeruser` char(35) DEFAULT NULL,
                        PRIMARY KEY (`MAnr`)
) ENGINE=InnoDB AUTO_INCREMENT=7083 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MA_DC`
--

DROP TABLE IF EXISTS `MA_DC`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MA_DC` (
                         `MAIL_MAnr` int(6) unsigned NOT NULL DEFAULT 0,
                         `sySTORE_STnr` int(9) unsigned NOT NULL DEFAULT 0,
                         `MADCloek` enum('n','y') DEFAULT 'n',
                         `MADCaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `MADCaeuser` char(35) DEFAULT NULL,
                         `MADCerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `MADCeruser` char(35) DEFAULT NULL,
                         PRIMARY KEY (`MAIL_MAnr`,`sySTORE_STnr`),
                         KEY `MA_DC_FKIndex1` (`MAIL_MAnr`),
                         KEY `MA_DC_FKIndex2` (`sySTORE_STnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MA_RE`
--

DROP TABLE IF EXISTS `MA_RE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MA_RE` (
                         `MAREnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                         `MAIL_MAnr` int(6) unsigned NOT NULL,
                         `MAREtype` char(3) DEFAULT NULL,
                         `MAREadress` text DEFAULT NULL,
                         `MAREquelle` char(20) DEFAULT NULL,
                         `MAREquelleid` int(6) unsigned DEFAULT NULL,
                         `MAREquellestr` varchar(15) DEFAULT NULL,
                         `MAREstatus` int(1) unsigned DEFAULT NULL,
                         `MAREretry` int(1) unsigned DEFAULT NULL,
                         `MAREexectime` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `MAREexecutor` char(15) DEFAULT NULL,
                         `MAREaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `MAREaeuser` char(35) DEFAULT NULL,
                         `MAREerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `MAREeruser` char(35) DEFAULT NULL,
                         PRIMARY KEY (`MAREnr`,`MAIL_MAnr`),
                         KEY `MARECIV_FKIndex1` (`MAIL_MAnr`)
) ENGINE=InnoDB AUTO_INCREMENT=7343 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_ABSCHLUSS`
--

DROP TABLE IF EXISTS `MBBE_ABSCHLUSS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_ABSCHLUSS` (
                                  `SEMINAR_nr` int(6) unsigned NOT NULL DEFAULT 0,
                                  `ADRESSE_adnr` int(6) unsigned NOT NULL DEFAULT 0,
                                  `datum` date DEFAULT NULL,
                                  `MBBE_ABSCHLUSS_GRUND_id` int(6) unsigned DEFAULT NULL,
                                  `bemerkung` text DEFAULT NULL,
                                  PRIMARY KEY (`SEMINAR_nr`,`ADRESSE_adnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_ABSCHLUSS_GRUND`
--

DROP TABLE IF EXISTS `MBBE_ABSCHLUSS_GRUND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_ABSCHLUSS_GRUND` (
                                        `id` int(6) unsigned DEFAULT NULL,
                                        `code` char(5) DEFAULT NULL,
                                        `bezeichnung` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_ANWESENHEIT`
--

DROP TABLE IF EXISTS `MBBE_ANWESENHEIT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_ANWESENHEIT` (
                                    `id` int(6) unsigned NOT NULL,
                                    `bezeichnung` char(50) DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_ARBEITSMARKT`
--

DROP TABLE IF EXISTS `MBBE_ARBEITSMARKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_ARBEITSMARKT` (
                                     `id` int(6) unsigned NOT NULL,
                                     `kurzbezeichnung` varchar(20) DEFAULT NULL,
                                     `langbezeichnung` varchar(50) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_BETREUUNG`
--

DROP TABLE IF EXISTS `MBBE_BETREUUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_BETREUUNG` (
                                  `id` int(6) unsigned DEFAULT NULL,
                                  `kurzbezeichnung` char(20) DEFAULT NULL,
                                  `langbezeichnung` char(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_EINZELTERMIN`
--

DROP TABLE IF EXISTS `MBBE_EINZELTERMIN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_EINZELTERMIN` (
                                     `TERMIN_id` int(6) unsigned NOT NULL,
                                     `SMAD_id` int(6) unsigned DEFAULT NULL,
                                     `SMTN_adnr` int(6) unsigned DEFAULT NULL,
                                     `MBBE_ANWESENHEIT_id` int(6) unsigned DEFAULT NULL,
                                     `bemerkung` text DEFAULT NULL,
                                     `wichtig` tinyint(1) DEFAULT NULL,
                                     `MBBE_TERMINTYP_id` int(6) unsigned DEFAULT NULL,
                                     `MBBE_TERMINTHEMA_id` int(6) unsigned DEFAULT NULL,
                                     PRIMARY KEY (`TERMIN_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_EINZELTERMIN_THEMA`
--

DROP TABLE IF EXISTS `MBBE_EINZELTERMIN_THEMA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_EINZELTERMIN_THEMA` (
                                           `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                           `bezeichnung` varchar(50) DEFAULT NULL,
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_EINZELTERMIN_TYP`
--

DROP TABLE IF EXISTS `MBBE_EINZELTERMIN_TYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_EINZELTERMIN_TYP` (
                                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                         `bezeichnung` varchar(50) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_GRUPPENTERMIN`
--

DROP TABLE IF EXISTS `MBBE_GRUPPENTERMIN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_GRUPPENTERMIN` (
                                      `TERMIN_id` int(6) unsigned NOT NULL DEFAULT 0,
                                      `SMAD_id` int(6) unsigned NOT NULL DEFAULT 0,
                                      `max_tn_anzahl` int(6) unsigned DEFAULT NULL,
                                      `bemerkung` text DEFAULT NULL,
                                      `wichtig` tinyint(1) DEFAULT NULL,
                                      `MBBE_TERMINTYP_id` int(6) unsigned DEFAULT NULL,
                                      `MBBE_TERMINTHEMA_id` int(6) unsigned DEFAULT NULL,
                                      PRIMARY KEY (`TERMIN_id`,`SMAD_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_GRUPPENTERMIN_SMTN`
--

DROP TABLE IF EXISTS `MBBE_GRUPPENTERMIN_SMTN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_GRUPPENTERMIN_SMTN` (
                                           `TERMIN_id` int(6) unsigned DEFAULT NULL,
                                           `TEILNEHMER_id` int(6) unsigned DEFAULT NULL,
                                           `MBBE_ANWESENHEIT_id` int(6) unsigned DEFAULT NULL,
                                           `bemerkung` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_GRUPPENTERMIN_THEMA`
--

DROP TABLE IF EXISTS `MBBE_GRUPPENTERMIN_THEMA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_GRUPPENTERMIN_THEMA` (
                                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                            `bezeichnung` varchar(50) DEFAULT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_GRUPPENTERMIN_TYP`
--

DROP TABLE IF EXISTS `MBBE_GRUPPENTERMIN_TYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_GRUPPENTERMIN_TYP` (
                                          `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                          `bezeichnung` varchar(50) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_LEISTUNGSBEZUG`
--

DROP TABLE IF EXISTS `MBBE_LEISTUNGSBEZUG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_LEISTUNGSBEZUG` (
                                       `id` int(6) unsigned DEFAULT NULL,
                                       `kurzbezeichnung` char(20) DEFAULT NULL,
                                       `langbezeichnung` char(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_MASSNAHME`
--

DROP TABLE IF EXISTS `MBBE_MASSNAHME`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_MASSNAHME` (
                                  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                  `SMTN_ADRESSE_adnr` int(6) unsigned DEFAULT NULL,
                                  `SMTN_SEMINAR_nr` int(6) unsigned DEFAULT NULL,
                                  `MBBE_MASSNAHME_TYP_id` int(6) unsigned DEFAULT NULL,
                                  `bezeichnung` char(50) DEFAULT NULL,
                                  `datum_von` date DEFAULT NULL,
                                  `datum_bis` date DEFAULT NULL,
                                  `CRM_FIRMA_id` int(6) unsigned DEFAULT NULL,
                                  `abbruch_am` date DEFAULT NULL,
                                  `MBBE_ARBEITSMARKT_id` int(6) unsigned DEFAULT NULL,
                                  `bemerkung` text DEFAULT NULL,
                                  `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `aeuser` char(35) DEFAULT NULL,
                                  `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `eruser` char(35) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_MASSNAHME_TYP`
--

DROP TABLE IF EXISTS `MBBE_MASSNAHME_TYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_MASSNAHME_TYP` (
                                      `id` int(6) unsigned DEFAULT NULL,
                                      `bezeichnung` char(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_SEMINARTYPEN`
--

DROP TABLE IF EXISTS `MBBE_SEMINARTYPEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_SEMINARTYPEN` (
                                     `id` int(6) unsigned NOT NULL,
                                     `bezeichnung` char(100) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MBBE_VERMITTLUNG`
--

DROP TABLE IF EXISTS `MBBE_VERMITTLUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MBBE_VERMITTLUNG` (
                                    `SMTN_PRAKT_id` int(6) unsigned NOT NULL,
                                    `MBBE_ARBEITSMARKT_id` int(6) unsigned DEFAULT NULL,
                                    `nachhaltigkeit` tinyint(1) DEFAULT NULL,
                                    PRIMARY KEY (`SMTN_PRAKT_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `METHODIK`
--

DROP TABLE IF EXISTS `METHODIK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `METHODIK` (
                            `MTnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                            `MTbezeichnung` varchar(100) DEFAULT NULL,
                            `MThomepageId` int(3) unsigned NOT NULL COMMENT 'ID, die dem Export fuer die Homepage mitgegeben wird',
                            PRIMARY KEY (`MTnr`),
                            UNIQUE KEY `MThomepageId` (`MThomepageId`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Methodik für eine Ausschreibung (Seminar auf HP)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MIETKAUTION`
--

DROP TABLE IF EXISTS `MIETKAUTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MIETKAUTION` (
                               `MKid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                               `MKmvid` int(6) unsigned NOT NULL,
                               `MKbetrag` decimal(15,2) NOT NULL,
                               `MKart` int(10) unsigned DEFAULT 0 COMMENT 'Art der Kautionshinterlegung',
                               `MKbemerk` text DEFAULT NULL COMMENT 'Bemerkungen',
                               `MKvon` date DEFAULT NULL,
                               `MKbis` date DEFAULT NULL,
                               `MKloek` enum('y','n') DEFAULT 'n',
                               `MKaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `MKaeuser` char(35) DEFAULT NULL,
                               `MKerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `MKeruser` char(35) DEFAULT NULL,
                               PRIMARY KEY (`MKid`),
                               KEY `MVid` (`MKmvid`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Mietkaution';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MIETMONAT`
--

DROP TABLE IF EXISTS `MIETMONAT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MIETMONAT` (
                             `MMid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                             `MMmtid` int(6) unsigned NOT NULL,
                             `MMtyp` int(6) unsigned NOT NULL,
                             `MMmonat` int(2) unsigned NOT NULL,
                             `MMjahr` int(4) unsigned NOT NULL,
                             `MMflaeche` decimal(9,2) unsigned NOT NULL,
                             `MMbetrag` decimal(13,4) NOT NULL,
                             `MMmwst` int(6) unsigned NOT NULL,
                             `MMbemerk` text DEFAULT NULL COMMENT 'Bemerkungen',
                             `MMaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                             `MMaeuser` char(35) DEFAULT NULL,
                             `MMerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                             `MMeruser` char(35) DEFAULT NULL,
                             PRIMARY KEY (`MMid`),
                             KEY `MTid` (`MMmtid`),
                             KEY `typ` (`MMtyp`),
                             KEY `monat` (`MMmonat`),
                             KEY `jahr` (`MMjahr`),
                             CONSTRAINT `MM_Miettop_MTid` FOREIGN KEY (`MMmtid`) REFERENCES `MIETTOP` (`MTid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=98390 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Mietmonat';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MIETSCHLUESSEL`
--

DROP TABLE IF EXISTS `MIETSCHLUESSEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MIETSCHLUESSEL` (
                                  `SKid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                  `SKmvid` int(6) unsigned NOT NULL,
                                  `SKanzahl` int(4) unsigned DEFAULT NULL,
                                  `SKnummern` text DEFAULT NULL,
                                  `SKerzeuger` int(6) DEFAULT NULL COMMENT 'Erzeuger von CRM',
                                  `SKerzeuger_old` int(6) DEFAULT NULL COMMENT 'Erzeuger von CRM ALT',
                                  `SKinhaber` int(6) DEFAULT NULL COMMENT 'Diese Person hat die Schlüssel',
                                  `SKbemerk` text DEFAULT NULL COMMENT 'Bemerkungen',
                                  `SKloek` enum('y','n') DEFAULT 'n',
                                  `SKaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `SKaeuser` char(35) DEFAULT NULL,
                                  `SKerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `SKeruser` char(35) DEFAULT NULL,
                                  PRIMARY KEY (`SKid`),
                                  KEY `MVid` (`SKmvid`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Mietschluessel';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MIETTOP`
--

DROP TABLE IF EXISTS `MIETTOP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MIETTOP` (
                           `MTid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                           `MTmvid` int(6) unsigned NOT NULL,
                           `MTbezeichnung` char(60) DEFAULT NULL COMMENT 'Miettopbezeichnung',
                           `MTdatumvon` date DEFAULT NULL,
                           `MTdatumbis` date DEFAULT NULL,
                           `MTbemerk` text DEFAULT NULL COMMENT 'Bemerkungen',
                           `MTflaeche` decimal(9,2) unsigned DEFAULT NULL,
                           `MTloek` enum('y','n') DEFAULT 'n',
                           `MTaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                           `MTaeuser` char(35) DEFAULT NULL,
                           `MTerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                           `MTeruser` char(35) DEFAULT NULL,
                           PRIMARY KEY (`MTid`),
                           KEY `MVid` (`MTmvid`)
) ENGINE=InnoDB AUTO_INCREMENT=499 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Miettops';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MIETVERBRAUCH`
--

DROP TABLE IF EXISTS `MIETVERBRAUCH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MIETVERBRAUCH` (
                                 `MDid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                 `MDmvid` int(6) unsigned NOT NULL,
                                 `MDtype` enum('internet','telefon','strom','gas','wasser') DEFAULT NULL,
                                 `MDanbieter` int(6) DEFAULT NULL COMMENT 'Anbieter',
                                 `MDanbieter_old` int(6) DEFAULT NULL COMMENT 'Anbieter ALT',
                                 `MDansprech` int(6) DEFAULT NULL COMMENT 'Ansprechperson',
                                 `MDansprech_old` int(6) DEFAULT NULL COMMENT 'Ansprechperson ALT',
                                 `MDnummer1` varchar(20) DEFAULT NULL COMMENT 'Vertrags-, Ruf-, Zählernummer',
                                 `MDnummer2` varchar(20) DEFAULT NULL COMMENT 'Ruf-, Kundennummer',
                                 `MDbetrag` varchar(30) DEFAULT NULL COMMENT 'UP/Down, Anschlussart, Zählerstand',
                                 `MDkosten` decimal(13,2) unsigned DEFAULT NULL COMMENT 'Kosten',
                                 `MDabrech` int(2) DEFAULT 1 COMMENT 'Abrechnungszeitraum',
                                 `MDab` date DEFAULT NULL,
                                 `MDbis` date DEFAULT NULL,
                                 `MDbemerk` text DEFAULT NULL COMMENT 'Bemerkungen',
                                 `MDloek` enum('y','n') DEFAULT 'n',
                                 `MDaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `MDaeuser` char(35) DEFAULT NULL,
                                 `MDerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `MDeruser` char(35) DEFAULT NULL,
                                 PRIMARY KEY (`MDid`),
                                 KEY `MVid` (`MDmvid`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Mietverbrauchsdaten';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MIETVERBRAUCHTOP`
--

DROP TABLE IF EXISTS `MIETVERBRAUCHTOP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MIETVERBRAUCHTOP` (
                                    `DTmtid` int(6) unsigned NOT NULL,
                                    `DTmdid` int(6) unsigned NOT NULL,
                                    `DTerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `DTeruser` char(35) DEFAULT NULL,
                                    PRIMARY KEY (`DTmtid`,`DTmdid`),
                                    KEY `MD_Mietverbrauch_MDid` (`DTmdid`),
                                    CONSTRAINT `DT_Miettop_MTid` FOREIGN KEY (`DTmtid`) REFERENCES `MIETTOP` (`MTid`) ON DELETE CASCADE ON UPDATE NO ACTION,
                                    CONSTRAINT `MD_Mietverbrauch_MDid` FOREIGN KEY (`DTmdid`) REFERENCES `MIETVERBRAUCH` (`MDid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Mietverbrauchsdatenzuordnung zu Top';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MIETVERTRAG`
--

DROP TABLE IF EXISTS `MIETVERTRAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MIETVERTRAG` (
                               `MVid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                               `MVstandortid` int(6) unsigned NOT NULL,
                               `MVnummer` int(10) unsigned NOT NULL,
                               `MVbezeichnung` char(60) DEFAULT NULL COMMENT 'Mietvertragsbezeichnung',
                               `MVkuendfrist` int(3) unsigned DEFAULT NULL,
                               `MVvergeb` decimal(9,2) DEFAULT NULL COMMENT 'Vergebührungsbetrag',
                               `MVprovision` decimal(9,2) DEFAULT NULL COMMENT 'Provision',
                               `MVanmiet` decimal(9,2) DEFAULT NULL COMMENT 'Anmietkosten',
                               `MVabmiet` decimal(9,2) DEFAULT NULL COMMENT 'Abmietkosten',
                               `MVbemerk` text DEFAULT NULL COMMENT 'Bemerkungen',
                               `MVloek` enum('y','n') DEFAULT 'n',
                               `MVaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `MVaeuser` char(35) DEFAULT NULL,
                               `MVerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `MVeruser` char(35) DEFAULT NULL,
                               PRIMARY KEY (`MVid`),
                               KEY `SOstandortid` (`MVstandortid`)
) ENGINE=InnoDB AUTO_INCREMENT=298 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Mietverträge';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MITARBEITER_ACTIVE_DIRECTORY`
--

DROP TABLE IF EXISTS `MITARBEITER_ACTIVE_DIRECTORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MITARBEITER_ACTIVE_DIRECTORY` (
                                                `AdresseId` int(6) unsigned NOT NULL,
                                                `ActiveDirectoryUserLogonName` varchar(255) DEFAULT NULL,
                                                `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                `ErstellungsBenutzer` char(35) NOT NULL,
                                                `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                `AenderungsBenutzer` char(35) DEFAULT NULL,
                                                PRIMARY KEY (`AdresseId`),
                                                UNIQUE KEY `MAD_UNIQUE_ADULN` (`ActiveDirectoryUserLogonName`),
                                                CONSTRAINT `MAD_AdresseId` FOREIGN KEY (`AdresseId`) REFERENCES `ADRESSE` (`ADadnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Active Directory Informationen zu einem Mitarbeiter';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MITARBEITER_KATEGORIE`
--

DROP TABLE IF EXISTS `MITARBEITER_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MITARBEITER_KATEGORIE` (
                                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                         `bezeichnung` varchar(100) NOT NULL COMMENT 'Bezeichnung für iBOS',
                                         `tc_bezeichnung` varchar(20) NOT NULL COMMENT 'alternative Bezeichnung für TC',
                                         `order_id` int(6) unsigned DEFAULT NULL COMMENT 'Sortierreihenfolge',
                                         `geburtstagsmail` tinyint(1) DEFAULT 0,
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `bezeichnung` (`bezeichnung`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MITBETREUUNG_ART`
--

DROP TABLE IF EXISTS `MITBETREUUNG_ART`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MITBETREUUNG_ART` (
                                    `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                    `Name` varchar(255) DEFAULT NULL,
                                    `Sortierung` int(3) unsigned DEFAULT NULL,
                                    `Aktiv` tinyint(1) DEFAULT NULL,
                                    `ErstellungsBenutzer` char(35) NOT NULL,
                                    `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                    `AenderungsBenutzer` char(35) DEFAULT NULL,
                                    `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    PRIMARY KEY (`Id`),
                                    UNIQUE KEY `UN_Mitbetreuung_Art_Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Art der Mitbetreuung';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MUTTERSPRACHE`
--

DROP TABLE IF EXISTS `MUTTERSPRACHE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MUTTERSPRACHE` (
                                 `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                 `kennzeichen_iso_639_1` varchar(5) DEFAULT NULL COMMENT 'ISO 639-1 Code',
                                 `kennzeichen_iso_639_3` varchar(5) DEFAULT NULL COMMENT 'ISO 639-3 Code',
                                 `kennzeichen_iso_639_2` varchar(5) DEFAULT NULL COMMENT 'ISO 639-2 Code',
                                 `name` varchar(100) DEFAULT NULL COMMENT 'Angezeigter Name',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NACHRICHT`
--

DROP TABLE IF EXISTS `NACHRICHT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NACHRICHT` (
                             `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Eindeutige ID einer Nachricht',
                             `Ueberschrift` varchar(50) NOT NULL COMMENT 'Überschrift einer Nachricht',
                             `Text` text NOT NULL COMMENT 'Inhalt einer Nachricht',
                             `Typ` enum('b','i') NOT NULL DEFAULT 'i' COMMENT 'Beschwerde oder Information',
                             `Gesendetam` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Wann wurde die Nachricht verschickt',
                             `Erledigtam` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Wann wurde die Nachricht als erledigt markiert',
                             `Erledigtvon` int(10) unsigned DEFAULT NULL COMMENT 'Wer hat die Nachricht erledgit',
                             `Gesendetan` text DEFAULT NULL COMMENT 'An welche E-Mails wurde die Nachricht versandt',
                             `Fehlernachricht` text DEFAULT NULL COMMENT 'Eventuelle Fehlermeldung bei Mail Versand',
                             `loek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                             `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                             `aeuser` char(35) DEFAULT NULL COMMENT 'Änderungsbenutzer',
                             `erda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                             `eruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=163554 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Tabelle mit Nachrichten';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NACHRICHT_ZUWEISUNG`
--

DROP TABLE IF EXISTS `NACHRICHT_ZUWEISUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NACHRICHT_ZUWEISUNG` (
                                       `Nachricht_id` int(10) unsigned NOT NULL COMMENT 'NACHRICHT.id',
                                       `Teilnehmer_id` int(10) unsigned DEFAULT NULL,
                                       `Trainer_id` int(10) unsigned DEFAULT NULL,
                                       `Adresse_id` int(10) unsigned DEFAULT NULL COMMENT 'ADRESSE.ADadnr',
                                       `Projekt_id` int(10) unsigned DEFAULT NULL COMMENT 'PROJEKT.PJnr',
                                       `FKostenstelle_kstgr` int(10) unsigned DEFAULT NULL COMMENT 'Kostenstellengruppe',
                                       `FKostenstelle_kstnr` int(10) unsigned DEFAULT NULL COMMENT 'Kostenstellennummer',
                                       `FKostenstelle_kstsub` int(10) unsigned DEFAULT NULL COMMENT 'Kostenstellensubnummer',
                                       `erda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                       `eruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                       PRIMARY KEY (`Nachricht_id`),
                                       KEY `FOREIGN_NACHRICHT_ZUORDNUNG_NACHRICHT_id` (`Nachricht_id`),
                                       KEY `ADRESSE_ID` (`Adresse_id`),
                                       KEY `PROJEKT_ID` (`Projekt_id`),
                                       KEY `FKOSTENSTELLE_ID` (`FKostenstelle_kstgr`,`FKostenstelle_kstnr`,`FKostenstelle_kstsub`),
                                       CONSTRAINT `FOREIGN_NACHRICHT_ZUORDNUNG_NACHRICHT_id` FOREIGN KEY (`Nachricht_id`) REFERENCES `NACHRICHT` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Tabelle die die Nachrichten zu Modulen verbindet';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NORMWOCHE`
--

DROP TABLE IF EXISTS `NORMWOCHE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NORMWOCHE` (
                             `NWnr` int(6) NOT NULL AUTO_INCREMENT COMMENT 'lfd. Nummer',
                             `NWmo_v1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'MO-Vormittag von',
                             `NWmo_v2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'MO-Vormittag bis',
                             `NWmo_n1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'MO-Nachmittag von',
                             `NWmo_n2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'MO-Nachmittag bis',
                             `NWdi_v1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'DI-Vormittag von',
                             `NWdi_v2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'DI-Vormittag bis',
                             `NWdi_n1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'DI-Nachmittag von',
                             `NWdi_n2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'DI-Nachmittag bis',
                             `NWmi_v1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'MI-Vormittag von',
                             `NWmi_v2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'MI-Vormittag bis',
                             `NWmi_n1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'MI-Nachmittag von',
                             `NWmi_n2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'MI-Nachmittag bis',
                             `NWdo_v1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'DO-Vormittag von',
                             `NWdo_v2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'DO-Vormittag bis',
                             `NWdo_n1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'DO-Nachmittag von',
                             `NWdo_n2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'DO-Nachmittag bis',
                             `NWfr_v1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'FR-Vormittag von',
                             `NWfr_v2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'FR-Vormittag  bis',
                             `NWfr_n1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'FR-Nachmittag von',
                             `NWfr_n2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'FR-Nachmittag bis',
                             `NWsa_v1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'SA-Vormittag von',
                             `NWsa_v2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'SA-Vormittag bis',
                             `NWsa_n1` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'SA-Nachmittag von',
                             `NWsa_n2` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'SA-Nachmittag bis',
                             `NWaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                             `NWaeuser` char(35) DEFAULT NULL,
                             `NWerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                             `NWeruser` char(35) DEFAULT NULL,
                             PRIMARY KEY (`NWnr`)
) ENGINE=InnoDB AUTO_INCREMENT=165692 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NOTIZEN`
--

DROP TABLE IF EXISTS `NOTIZEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NOTIZEN` (
                           `NZid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                           `ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                           `ABTEILUNG_ABnr` int(3) unsigned NOT NULL,
                           `NZkstgr` int(6) DEFAULT NULL,
                           `NZdatum` date DEFAULT NULL,
                           `NZwiedervorlage` date DEFAULT NULL,
                           `NZbenutzer` varchar(15) DEFAULT NULL,
                           `NZbezeichnung` varchar(40) DEFAULT NULL,
                           `NZtext` text DEFAULT NULL,
                           `NZloek` enum('n','y') NOT NULL DEFAULT 'n',
                           `NZaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                           `NZaeuser` char(35) DEFAULT NULL,
                           `NZerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                           `NZeruser` char(35) DEFAULT NULL,
                           PRIMARY KEY (`NZid`,`ADRESSE_ADadnr`,`ABTEILUNG_ABnr`),
                           KEY `NOTIZEN_FKIndex1` (`ADRESSE_ADadnr`,`ABTEILUNG_ABnr`)
) ENGINE=InnoDB AUTO_INCREMENT=12328 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PCOMMENT`
--

DROP TABLE IF EXISTS `PCOMMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PCOMMENT` (
                            `CMid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                            `CMtext` text DEFAULT NULL,
                            `CMstatus` enum('a','i') DEFAULT NULL COMMENT 'Anzeigestatus',
                            `CMdatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `CMerip` varchar(15) DEFAULT NULL,
                            `CMerbrowser` varchar(250) DEFAULT NULL,
                            `CMloek` enum('n','y') DEFAULT 'n',
                            `CMaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `CMaeuser` char(35) DEFAULT NULL,
                            `CMerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `CMeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`CMid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PCONTENT`
--

DROP TABLE IF EXISTS `PCONTENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PCONTENT` (
                            `CTid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Content-ID',
                            `PSECTION_CSsectionID` int(6) unsigned NOT NULL DEFAULT 0,
                            `CTheadline` varchar(80) DEFAULT NULL COMMENT 'Headline',
                            `CTsubline1` varchar(80) DEFAULT NULL COMMENT 'Subline 1',
                            `CTsubline2` varchar(80) DEFAULT NULL COMMENT 'Subline 2',
                            `CTdatum` date DEFAULT NULL COMMENT 'Datum der Meldung',
                            `CTdatumshow` enum('y','n') DEFAULT 'y' COMMENT 'Anzeige des Datums',
                            `CTtext` text DEFAULT NULL COMMENT 'Text',
                            `CTdatumvon` date DEFAULT NULL COMMENT 'Anzeige von',
                            `CTdatumbis` date DEFAULT NULL COMMENT 'Anzeibe bis',
                            `CTlink_to` varchar(128) DEFAULT NULL COMMENT 'Link',
                            `CTauthor` varchar(40) DEFAULT NULL COMMENT 'Verfasser',
                            `CTauthorshow` enum('y','n') DEFAULT 'y' COMMENT 'Anzeige des Verfassers',
                            `CTloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                            `CTaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungs-Datum',
                            `CTaeuser` char(35) DEFAULT NULL,
                            `CTerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungs-Datum',
                            `CTeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`CTid`),
                            KEY `PCONTENT_FKIndex1` (`PSECTION_CSsectionID`)
) ENGINE=MyISAM AUTO_INCREMENT=201 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PEP_LOGGING`
--

DROP TABLE IF EXISTS `PEP_LOGGING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PEP_LOGGING` (
                               `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                               `ADRESSE_adnr` int(6) unsigned DEFAULT NULL,
                               `DIENSTVERTRAG_id` int(6) unsigned DEFAULT NULL,
                               `jahr` int(4) DEFAULT NULL,
                               `monat` int(2) DEFAULT NULL,
                               `aktion` enum('ausdruck','za_bearbeitet','statuswechsel') DEFAULT NULL,
                               `status_alt` int(1) DEFAULT NULL,
                               `status_aktuell` int(1) DEFAULT NULL,
                               `za_alt` decimal(7,2) DEFAULT NULL,
                               `za_aktuell` decimal(7,2) DEFAULT NULL,
                               `client` char(255) DEFAULT NULL,
                               `eruser` char(35) DEFAULT NULL,
                               `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1746400 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PERSONALBOGEN`
--

DROP TABLE IF EXISTS `PERSONALBOGEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PERSONALBOGEN` (
                                 `PBid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID des Personalbogens',
                                 `PB_ADadnr` int(6) unsigned NOT NULL COMMENT 'ID des Trainers (Aus ADRESSE)',
                                 `PBart` enum('fix','freio','freim') NOT NULL DEFAULT 'fix' COMMENT 'Welche Art Personalbogen',
                                 `PBtitel` int(6) unsigned DEFAULT NULL COMMENT 'Titelcode',
                                 `PBznf1` varchar(50) DEFAULT NULL COMMENT 'Zuname',
                                 `PBvnf1` varchar(50) DEFAULT NULL COMMENT 'Vorname',
                                 `PBfirma` varchar(100) DEFAULT NULL,
                                 `PBstrasse` varchar(50) DEFAULT NULL COMMENT 'StraÃŸenname',
                                 `PBplz` varchar(6) DEFAULT NULL COMMENT 'Postleitzahl',
                                 `PBort` varchar(50) DEFAULT NULL COMMENT 'Postleitzahl',
                                 `PBsvnr` varchar(11) DEFAULT NULL COMMENT 'Sozialversicherungsnummer',
                                 `PBgebdat` date DEFAULT NULL COMMENT 'Geburtsdatum',
                                 `PBgetdatf` int(1) unsigned DEFAULT 0 COMMENT 'unbekannter Geburtstag',
                                 `PBstaatsb` int(6) unsigned DEFAULT NULL COMMENT 'Staatsbuergerschaft',
                                 `PBfamstand` int(6) unsigned DEFAULT NULL COMMENT 'Familienstand',
                                 `PBkinder` int(2) unsigned DEFAULT NULL COMMENT 'Anzahl Kinder',
                                 `PBunterhaltspflichtige` int(6) DEFAULT 0,
                                 `PBemail` varchar(80) DEFAULT NULL COMMENT 'EmailAdresse',
                                 `PBkonto` int(1) DEFAULT 0 COMMENT 'Aus- oder Inländisches Konto',
                                 `PBbank` varchar(40) DEFAULT NULL COMMENT 'Name der Bank',
                                 `PBblz` varchar(11) DEFAULT NULL COMMENT 'Bankleitzahl/BIC',
                                 `PBkontonr` varchar(34) DEFAULT NULL,
                                 `PBiban` varchar(34) DEFAULT NULL COMMENT 'Kontonummer/IBAN',
                                 `PBbic` varchar(34) DEFAULT NULL,
                                 `PBdienstgeber` int(6) unsigned DEFAULT NULL,
                                 `PBavab` enum('y','n') DEFAULT 'n' COMMENT 'Alleinverdienerabsetzbetrag',
                                 `PBpendlerp` enum('y','n') DEFAULT 'n' COMMENT 'Pendlerpauschale',
                                 `PBkopie_ueberg` enum('n','y') DEFAULT 'n',
                                 `PBagen` varchar(40) DEFAULT NULL COMMENT 'Arbeitsgenehmigung',
                                 `PBagenbis` varchar(5) DEFAULT NULL COMMENT 'Arbeitsgenehmigung bis',
                                 `PBagenkopie_ueberg` enum('n','y') DEFAULT 'n',
                                 `PBagenbh` varchar(40) DEFAULT NULL COMMENT 'Arbeitsgenehmigung ausstellende Behoerde',
                                 `PBvtr` varchar(100) DEFAULT NULL,
                                 `PBvnr` varchar(11) DEFAULT NULL COMMENT 'Versicherungsnummer (frei mit Gewerbeschein)',
                                 `PBgewart` varchar(40) DEFAULT NULL COMMENT 'Gewerbeart',
                                 `PBgewab` date DEFAULT NULL COMMENT 'Gewerbeschein gueltig ab',
                                 `PBgewbh` varchar(40) DEFAULT NULL COMMENT 'Gewerbeschein ausgestellt von',
                                 `PBgewnr` varchar(40) DEFAULT NULL COMMENT 'Gewerbescheinnummer',
                                 `STANDORT_SOstandortid` int(6) DEFAULT NULL COMMENT 'Beschaeftigungsort (aus Standort)',
                                 `PBbereich` int(6) unsigned DEFAULT NULL COMMENT 'Geschaeftsbereich',
                                 `PBtaetart` int(6) unsigned DEFAULT NULL COMMENT 'Art der Taetigkeit/Berufsbezeichnung',
                                 `PBtaetmasz` int(6) unsigned DEFAULT NULL COMMENT 'BeschaeftigungsausmaÃŸ',
                                 `PBstatus` int(1) unsigned DEFAULT 0 COMMENT 'Status des Personalbogens',
                                 `PBsmorpj` int(1) unsigned DEFAULT 0 COMMENT 'Zuweisung Projekt oder Seminar',
                                 `PBzuweisung` int(6) unsigned DEFAULT NULL COMMENT 'Seminarnummer/Projektnummer',
                                 `PBbegin` date DEFAULT NULL COMMENT 'Vertragsbeginn/Eintrittsdatum',
                                 `PBende` date DEFAULT NULL COMMENT 'Vertragsende',
                                 `PBtaetstatus` int(6) unsigned DEFAULT NULL COMMENT 'Beschaeftigungsstatus',
                                 `PBtage` varchar(30) DEFAULT NULL,
                                 `PBwochenstd` decimal(5,2) unsigned DEFAULT NULL,
                                 `PBgleitzeit` enum('n','y') DEFAULT 'n',
                                 `PBgleitzeit_light` enum('n','y') DEFAULT 'n',
                                 `PBmobileworking` enum('n','y') DEFAULT 'n',
                                 `PBvdgeprueft` enum('y','n') DEFAULT 'n' COMMENT 'Vordienstzeiten geprüft',
                                 `PBvstufe` int(10) unsigned DEFAULT NULL COMMENT 'Verwendungsstufe',
                                 `PBvbereich_old` int(10) unsigned DEFAULT NULL,
                                 `PBvstufe_old` int(10) unsigned DEFAULT NULL,
                                 `PBbetriebsvereinbarung` enum('n','y') DEFAULT 'n',
                                 `PBreisekostenrichtlinie` enum('n','y') DEFAULT 'n',
                                 `PBgleitzeitvereinbarung` enum('n','y') DEFAULT 'n',
                                 `PBgleitzeitvereinbarung_light` enum('n','y') DEFAULT 'n',
                                 `PBmobileworkingvereinbarung` enum('n','y') DEFAULT 'n',
                                 `PBansaessbesch` tinyint(1) DEFAULT NULL COMMENT 'Ansässigkeitsbescheinigung für Deutschland',
                                 `PBformblattA1` tinyint(1) DEFAULT NULL COMMENT 'Formblatt A1 der Krankenkassa',
                                 `PBgehalt` decimal(9,2) unsigned DEFAULT NULL COMMENT 'Stundensatz oder Bruttogehalt',
                                 `PBzulage` decimal(9,2) unsigned DEFAULT NULL COMMENT 'Zulage',
                                 `PB_SCid` int(6) unsigned DEFAULT NULL COMMENT 'ID des Coaches',
                                 `PBalg` enum('y','n') DEFAULT 'n' COMMENT 'Arbeitslosengeld',
                                 `PBkur` enum('y','n') DEFAULT 'n' COMMENT 'Kleinunternehmerregelung',
                                 `PBuid` varchar(40) DEFAULT NULL COMMENT 'Umsatzsteueridentifikationsnummer',
                                 `PBfinanzamt` varchar(40) DEFAULT NULL COMMENT 'Finanzamt',
                                 `PBbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                                 `PBvdbemerk` text DEFAULT NULL COMMENT 'Bemerkung für Vordienstzeiten',
                                 `PBfahrp` char(128) DEFAULT NULL COMMENT 'Fahrkostenpauschale',
                                 `PBloek` enum('n','y') DEFAULT 'n' COMMENT 'Loeschkennzeichen',
                                 `PBaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'aenderungsdatum',
                                 `PBaeuser` char(35) DEFAULT NULL COMMENT 'aenderungsbenutzer',
                                 `PBerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                 `PBeruser` char(35) DEFAULT NULL COMMENT 'Erstellungsbenutzer',
                                 PRIMARY KEY (`PBid`)
) ENGINE=MyISAM AUTO_INCREMENT=8605 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PLANUNG`
--

DROP TABLE IF EXISTS `PLANUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PLANUNG` (
                           `PAJahr` int(4) NOT NULL,
                           `PAKostenstelle` int(3) NOT NULL,
                           `PALohnnebenkostenAngestellte` decimal(5,2) DEFAULT NULL,
                           `PALohnnebenkostenArbeiter` decimal(5,2) DEFAULT NULL,
                           `PALohnnebenkostenLehrling` decimal(5,2) DEFAULT NULL,
                           `PANebenkosten_freie_MAoG` decimal(5,2) DEFAULT NULL,
                           `PAKV_Erhoehung_berechnen_ab` date DEFAULT NULL,
                           `PAKV_Erhoehung` decimal(5,2) DEFAULT NULL,
                           `PAAFA_Gesamt_Vorjahre` decimal(9,2) DEFAULT NULL,
                           `PAstatus` int(1) DEFAULT NULL,
                           `PAaeda` date DEFAULT NULL,
                           `PAaeuser` char(35) DEFAULT NULL,
                           `PAerda` date DEFAULT NULL,
                           `PAeruser` char(35) DEFAULT NULL,
                           PRIMARY KEY (`PAJahr`,`PAKostenstelle`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT`
--

DROP TABLE IF EXISTS `PROJEKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT` (
                           `PJnr` int(6) NOT NULL AUTO_INCREMENT COMMENT 'Projekt-Nummer',
                           `PJmnr` varchar(20) DEFAULT NULL COMMENT '(e)AMS Masznahmennummer',
                           `ASnr` int(6) NOT NULL COMMENT 'Auftrags-Nummer',
                           `PJbezeichnung1` char(80) DEFAULT NULL COMMENT 'Projekt-Bezeichnung1',
                           `PJbezeichnung2` char(80) DEFAULT NULL COMMENT 'Projekt-Bezeichnung2',
                           `PJtyp` int(6) unsigned DEFAULT 1 COMMENT 'PROJEKTTYPEN.id',
                           `FIT2WORK_BUNDESLAND_id` int(6) unsigned DEFAULT NULL,
                           `PJdatumvon` date DEFAULT NULL COMMENT 'Beginn-Datum',
                           `PJdatumbis` date DEFAULT NULL COMMENT 'Ende-Datum',
                           `PJmonAbschlussJahr` int(4) unsigned DEFAULT NULL,
                           `PJmonAbschlussMonat` int(2) unsigned DEFAULT NULL,
                           `PJeinheiten` int(6) DEFAULT NULL COMMENT 'Projekt-Dauer (h)',
                           `PJSOstandortid` int(6) DEFAULT NULL COMMENT 'Ort',
                           `PJkontaktibis` int(6) DEFAULT NULL COMMENT 'Projektkoordinator ibis',
                           `PJkontaktauftraggeber` text DEFAULT NULL COMMENT 'Kontaktperson Auftraggeber',
                           `PJkontaktauftraggeber_old` int(6) DEFAULT NULL COMMENT 'Kontaktperson Auftraggeber Alt',
                           `PJTasNr` varchar(50) DEFAULT NULL,
                           `PJkstgr` int(3) DEFAULT NULL COMMENT 'Kostenstelle Gruppe',
                           `PJkstnr` int(3) DEFAULT NULL COMMENT 'Kostenstelle',
                           `PJkstsub` int(3) DEFAULT NULL COMMENT 'Kostenstelle Sub',
                           `PJkostentraeger` int(7) DEFAULT NULL COMMENT 'Kostenträger-Nummer',
                           `TEILNAHMESTATUS_BEREICH_id` int(6) unsigned DEFAULT NULL,
                           `PJvermittlungsquote` int(3) DEFAULT NULL COMMENT 'Vermittlungsquote in %',
                           `PJprojektpreis` decimal(10,2) DEFAULT NULL COMMENT 'Projekt-Preis',
                           `PJprojektMNH` decimal(10,2) DEFAULT NULL COMMENT 'Personal-MNH',
                           `PJteilnehmeranz` int(4) DEFAULT NULL COMMENT 'Teilnehmer-Anzahl',
                           `PJstundensatz` decimal(9,2) DEFAULT NULL,
                           `PJkurszeiten` text DEFAULT NULL COMMENT 'z.B.: mo - Fr 8 - 12 Uhr; wird auf Homepage veröffentlicht',
                           `PJanmeldeschluss` date DEFAULT NULL COMMENT 'Wird auf Homepage veröffntlicht',
                           `PJbemerk` text DEFAULT NULL,
                           `PJbezugsaenderung` char(1) NOT NULL DEFAULT 'n',
                           `PJteilnehmerdetail` char(1) NOT NULL DEFAULT 'n',
                           `PJinhaltsbeschr` text DEFAULT NULL,
                           `PJpublishvon` date DEFAULT NULL COMMENT 'Veröffentlichen von-Datum',
                           `PJpublishbis` date DEFAULT NULL COMMENT 'Veröffentlichen bis-Datum',
                           `PJpublishtxt` text DEFAULT NULL,
                           `PJstatus` int(6) DEFAULT NULL COMMENT 'Projekt-Status',
                           `PJif` enum('y','n') DEFAULT 'n' COMMENT 'IndividualfÃ¶rderung',
                           `PJik` enum('y','n') DEFAULT 'n' COMMENT 'Internatskosten',
                           `PJkb` enum('y','n') DEFAULT 'n' COMMENT 'Kinderbetreuung',
                           `PJmassnahmennebenkosten` decimal(15,2) DEFAULT NULL,
                           `PJpreis_ergaenzung` decimal(15,2) DEFAULT NULL,
                           `PJsubunternehmer` decimal(15,2) DEFAULT NULL,
                           `PJvertrag` enum('y','n') DEFAULT NULL,
                           `PJbearbeiten` tinyint(1) DEFAULT NULL,
                           `PJvertragszuordnung` varchar(100) DEFAULT NULL,
                           `PJendabrechnung` enum('y','n') DEFAULT NULL,
                           `PJendabrechnung_datum` date DEFAULT NULL,
                           `PJendabrechnung_bemerkung` varchar(100) DEFAULT NULL,
                           `PJendabrechnung_betrag` decimal(15,2) DEFAULT NULL COMMENT 'Endabrechnungsbetrag',
                           `PJendabrechnung_projektpreis` decimal(15,2) DEFAULT NULL,
                           `PJendabrechnung_mnk` decimal(15,2) DEFAULT NULL COMMENT 'Maßnahmennebenkosten',
                           `PJendabrechnung_betrag_bewilligt` decimal(15,2) DEFAULT NULL COMMENT 'Endabrechnungsbetrag bewilligt',
                           `PJentwurf_faellig_am` date DEFAULT NULL COMMENT 'Zahlungsliste Entwurf fällig am',
                           `PJabgabe_kunde` date DEFAULT NULL,
                           `PJaltern_abgabefrist` date DEFAULT NULL COMMENT 'Alternative Abgabefrist für dieses Projekt',
                           `PJec_storno_verr_schluessel` decimal(5,4) DEFAULT NULL COMMENT 'Verrechnungsschlüssen der stornierten Einzelcoaching-Leistungen in Prozent',
                           `PJprojektabrechnung_pdf_gruppierung` enum('month','week') DEFAULT 'month' COMMENT 'Projektweise Einstellung, nach welchem Zeitraum die Projektabrechnung eines Trainers gruppiert werden soll.',
                           `PJloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                           `PJaeda` datetime DEFAULT NULL COMMENT 'Änderungsdatum',
                           `PJaeuser` char(35) DEFAULT NULL,
                           `PJerda` datetime DEFAULT NULL COMMENT 'Erfassungsdatum',
                           `PJeruser` char(35) DEFAULT NULL,
                           PRIMARY KEY (`PJnr`),
                           KEY `PROJEKT_ASnr` (`ASnr`),
                           KEY `PROJEKT_Teilnahmestatus_Bereich_id` (`TEILNAHMESTATUS_BEREICH_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6143 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKTKATEGORIE`
--

DROP TABLE IF EXISTS `PROJEKTKATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKTKATEGORIE` (
                                    `id` int(8) unsigned NOT NULL AUTO_INCREMENT,
                                    `gruppe_id` int(8) unsigned DEFAULT NULL COMMENT 'ID von PROJEKTKATEGORIE_GRUPPE',
                                    `methode_id` int(8) unsigned DEFAULT NULL COMMENT 'ID von PROJEKTKATEGORIE_METHODE',
                                    `name` varchar(50) DEFAULT NULL,
                                    `loek` enum('y','n') DEFAULT 'n',
                                    `begleitend` tinyint(1) DEFAULT NULL,
                                    `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `eruser` char(35) DEFAULT NULL,
                                    `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `aeuser` char(35) DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `PROJEKTKATEGORIE_METHODE_id` (`methode_id`),
                                    KEY `PROJEKTKATEGORIE_GRUPPE_id` (`gruppe_id`),
                                    CONSTRAINT `PROJEKTKATEGORIE_GRUPPE_id` FOREIGN KEY (`gruppe_id`) REFERENCES `PROJEKTKATEGORIE_GRUPPE` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                    CONSTRAINT `PROJEKTKATEGORIE_METHODE_id` FOREIGN KEY (`methode_id`) REFERENCES `PROJEKTKATEGORIE_METHODE` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Kategorien der Projekte';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKTKATEGORIE_GRUPPE`
--

DROP TABLE IF EXISTS `PROJEKTKATEGORIE_GRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKTKATEGORIE_GRUPPE` (
                                           `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                           `name` varchar(50) NOT NULL,
                                           `loek` enum('y','n') DEFAULT 'n',
                                           `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                           `eruser` char(35) DEFAULT NULL,
                                           `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                           `aeuser` char(35) DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `name_uq` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Gruppen zur Projektkategorie';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKTKATEGORIE_METHODE`
--

DROP TABLE IF EXISTS `PROJEKTKATEGORIE_METHODE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKTKATEGORIE_METHODE` (
                                            `id` int(8) unsigned NOT NULL AUTO_INCREMENT,
                                            `name` varchar(50) NOT NULL,
                                            `loek` enum('y','n') DEFAULT 'n',
                                            `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                            `eruser` char(35) DEFAULT NULL,
                                            `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                            `aeuser` char(35) DEFAULT NULL,
                                            PRIMARY KEY (`id`),
                                            UNIQUE KEY `name_UQ` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Methoden einer Projektkategorie';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKTKATEGORIE_ZUWEISUNG`
--

DROP TABLE IF EXISTS `PROJEKTKATEGORIE_ZUWEISUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKTKATEGORIE_ZUWEISUNG` (
                                              `projekt_id` int(8) unsigned NOT NULL,
                                              `kategorie_id` int(8) unsigned NOT NULL,
                                              `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                              `eruser` char(35) DEFAULT NULL,
                                              PRIMARY KEY (`projekt_id`,`kategorie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Zuordnung der Projekten zu KAteogiren';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKTTYPEN`
--

DROP TABLE IF EXISTS `PROJEKTTYPEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKTTYPEN` (
                                `id` int(6) unsigned NOT NULL,
                                `bezeichnung` char(50) DEFAULT NULL,
                                `aktiv` tinyint(1) DEFAULT 1,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_ACTIVE_DIRECTORY_DATEN`
--

DROP TABLE IF EXISTS `PROJEKT_ACTIVE_DIRECTORY_DATEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_ACTIVE_DIRECTORY_DATEN` (
                                                  `ProjektId` int(11) NOT NULL,
                                                  `Terminal_Server_Teilnehmer_Feature_Aktiv` int(1) unsigned DEFAULT NULL,
                                                  `Terminal_Server_Teilnehmer_Software_Gruppe_GUID` varchar(36) CHARACTER SET latin1 COLLATE latin1_german1_ci DEFAULT NULL,
                                                  `Blackboard_Teilnehmer_Feature_Aktiv` int(1) unsigned DEFAULT NULL,
                                                  `Blackboard_Teilnehmer_Software_Gruppe_GUID` varchar(36) CHARACTER SET latin1 COLLATE latin1_german1_ci DEFAULT NULL,
                                                  `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                  `AenderungsBenutzer` char(35) DEFAULT NULL,
                                                  `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                  `ErstellungsBenutzer` char(35) CHARACTER SET latin1 COLLATE latin1_german1_ci NOT NULL,
                                                  PRIMARY KEY (`ProjektId`),
                                                  CONSTRAINT `ProjektADData_ProjektId` FOREIGN KEY (`ProjektId`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Daten für Active Directory auf Projektebene';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_MITBETREUUNG`
--

DROP TABLE IF EXISTS `PROJEKT_MITBETREUUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_MITBETREUUNG` (
                                        `ProjektId` int(6) NOT NULL,
                                        `Aktiv` int(1) unsigned NOT NULL,
                                        `Projektuebergreifend` int(1) unsigned DEFAULT 0,
                                        `NurPk` int(1) unsigned DEFAULT 0,
                                        `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                        `AenderungsBenutzer` char(35) DEFAULT NULL,
                                        `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                        `ErstellungsBenutzer` char(35) NOT NULL,
                                        PRIMARY KEY (`ProjektId`),
                                        CONSTRAINT `FK_PROJEKT_MITBETREUUNG_ProjektId` FOREIGN KEY (`ProjektId`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_MITBETREUUNG_PROJEKT`
--

DROP TABLE IF EXISTS `PROJEKT_MITBETREUUNG_PROJEKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_MITBETREUUNG_PROJEKT` (
                                                `MitbetreuungId` int(6) NOT NULL,
                                                `ProjektId` int(6) NOT NULL,
                                                `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                `ErstellungsBenutzer` char(35) NOT NULL,
                                                PRIMARY KEY (`MitbetreuungId`,`ProjektId`),
                                                KEY `PROJEKT_MITBETREUUNG_ProjektId` (`ProjektId`),
                                                CONSTRAINT `FK_PROJEKT_MITBETREUUNG_PROJEKT_MitbetreuungId` FOREIGN KEY (`MitbetreuungId`) REFERENCES `PROJEKT_MITBETREUUNG` (`ProjektId`) ON DELETE CASCADE ON UPDATE CASCADE,
                                                CONSTRAINT `FK_PROJEKT_MITBETREUUNG_PROJEKT_ProjektId` FOREIGN KEY (`ProjektId`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_MONATSABSCHLUSS`
--

DROP TABLE IF EXISTS `PROJEKT_MONATSABSCHLUSS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_MONATSABSCHLUSS` (
                                           `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                           `ProjektId` int(6) NOT NULL,
                                           `Jahr` int(4) unsigned NOT NULL,
                                           `Monat` int(2) unsigned NOT NULL,
                                           `ProjektMonatsabschlussStatusId` int(10) unsigned NOT NULL,
                                           `AdresseId` int(6) unsigned NOT NULL,
                                           `ErstellungsBenutzer` char(35) NOT NULL,
                                           `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                           `AenderungsBenutzer` char(35) DEFAULT NULL,
                                           `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                           PRIMARY KEY (`Id`),
                                           UNIQUE KEY `UQ_PROJEKT_MONATSABSCHLUSS_ProjektId_Jahr_Monat` (`ProjektId`,`Jahr`,`Monat`),
                                           KEY `FK_PROJEKT_MONATSABSCHLUSS_AdresseId` (`AdresseId`),
                                           KEY `FK_PROJEKT_MONATSABSCHLUSS_StatusId` (`Id`,`ProjektMonatsabschlussStatusId`),
                                           KEY `PROJEKT_MONATSABSCHLUSS_StatusId` (`ProjektMonatsabschlussStatusId`),
                                           CONSTRAINT `PROJEKT_MONATSABSCHLUSS_AdresseId` FOREIGN KEY (`AdresseId`) REFERENCES `ADRESSE` (`ADadnr`) ON UPDATE CASCADE,
                                           CONSTRAINT `PROJEKT_MONATSABSCHLUSS_ProjektId` FOREIGN KEY (`ProjektId`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE,
                                           CONSTRAINT `PROJEKT_MONATSABSCHLUSS_StatusId` FOREIGN KEY (`ProjektMonatsabschlussStatusId`) REFERENCES `PROJEKT_MONATSABSCHLUSS_STATUS` (`Id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17841 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_MONATSABSCHLUSS_LOG`
--

DROP TABLE IF EXISTS `PROJEKT_MONATSABSCHLUSS_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_MONATSABSCHLUSS_LOG` (
                                               `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                               `ProjektMonatsabschlussId` int(10) unsigned NOT NULL,
                                               `Datum` datetime /* mariadb-5.3 */ NOT NULL,
                                               `AdresseId` int(6) unsigned NOT NULL,
                                               `ProjektMonatsabschlussStatusIdVon` int(10) unsigned NOT NULL,
                                               `ProjektMonatsabschlussStatusIdZu` int(10) unsigned NOT NULL,
                                               `Funktion` varchar(50) NOT NULL,
                                               `Begruendung` text DEFAULT NULL,
                                               `ErstellungsBenutzer` char(35) NOT NULL,
                                               `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                               `AenderungsBenutzer` char(35) DEFAULT NULL,
                                               `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                               PRIMARY KEY (`Id`),
                                               KEY `PROJEKT_MONATSABSCHLUSS_LOG_Datum` (`Datum`),
                                               KEY `FK_PROJEKT_MONATSABSCHLUSS_LOG_ProjektMonatsabschlussStatusIdVon` (`ProjektMonatsabschlussStatusIdVon`),
                                               KEY `FK_PROJEKT_MONATSABSCHLUSS_LOG_ProjektMonatsabschlussStatusIdZu` (`ProjektMonatsabschlussStatusIdZu`),
                                               KEY `FK_PROJEKT_MONATSABSCHLUSS_LOG_ProjektMonatsabschlussId` (`ProjektMonatsabschlussId`),
                                               KEY `FK_PROJEKT_MONATSABSCHLUSS_LOG_AdresseId` (`AdresseId`),
                                               CONSTRAINT `PROJEKT_MONATSABSCHLUSS_LOG_AdresseId` FOREIGN KEY (`AdresseId`) REFERENCES `ADRESSE` (`ADadnr`) ON UPDATE CASCADE,
                                               CONSTRAINT `PROJEKT_MONATSABSCHLUSS_LOG_ProjektMonatsabschlussId` FOREIGN KEY (`ProjektMonatsabschlussId`) REFERENCES `PROJEKT_MONATSABSCHLUSS` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                               CONSTRAINT `PROJEKT_MONATSABSCHLUSS_LOG_ProjektMonatsabschlussStatusIdVon` FOREIGN KEY (`ProjektMonatsabschlussStatusIdVon`) REFERENCES `PROJEKT_MONATSABSCHLUSS_STATUS` (`Id`) ON UPDATE CASCADE,
                                               CONSTRAINT `PROJEKT_MONATSABSCHLUSS_LOG_ProjektMonatsabschlussStatusIdZu` FOREIGN KEY (`ProjektMonatsabschlussStatusIdZu`) REFERENCES `PROJEKT_MONATSABSCHLUSS_STATUS` (`Id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=49357 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_MONATSABSCHLUSS_STATUS`
--

DROP TABLE IF EXISTS `PROJEKT_MONATSABSCHLUSS_STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_MONATSABSCHLUSS_STATUS` (
                                                  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                                  `Name` varchar(50) NOT NULL,
                                                  `Kuerzel` char(1) NOT NULL,
                                                  `Aktiv` int(1) unsigned NOT NULL DEFAULT 0,
                                                  `Qualitativ` int(1) unsigned NOT NULL DEFAULT 0,
                                                  `Quantitativ` int(1) unsigned NOT NULL DEFAULT 0,
                                                  `Sperre` int(1) unsigned NOT NULL DEFAULT 0,
                                                  `Begruendung` int(1) unsigned NOT NULL DEFAULT 0,
                                                  PRIMARY KEY (`Id`),
                                                  UNIQUE KEY `UQ_PROJEKT_MONATSABSCHLUSS_STATUS_Name` (`Name`),
                                                  KEY `PROJEKT_MONATSABSCHLUSS_STATUS_Aktiv` (`Aktiv`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_MONATSABSCHLUSS_STATUS_NEXT`
--

DROP TABLE IF EXISTS `PROJEKT_MONATSABSCHLUSS_STATUS_NEXT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_MONATSABSCHLUSS_STATUS_NEXT` (
                                                       `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                                       `ProjektMonatsabschlussStatusNowId` int(10) unsigned NOT NULL,
                                                       `ProjektMonatsabschlussStatusThenId` int(10) unsigned NOT NULL,
                                                       `Ebene1` int(1) unsigned NOT NULL,
                                                       `Ebene2` int(1) unsigned NOT NULL,
                                                       `Ebene3` int(1) unsigned NOT NULL,
                                                       PRIMARY KEY (`Id`),
                                                       UNIQUE KEY `UQ_PROJEKT_MONATSABSCHLUSS_STATUS_NEXT_NowId_ThenId` (`ProjektMonatsabschlussStatusNowId`,`ProjektMonatsabschlussStatusThenId`),
                                                       KEY `FK_PROJEKT_MONATSABSCHLUSS_STATUS_NEXT_ThenId` (`ProjektMonatsabschlussStatusThenId`),
                                                       KEY `PROJEKT_MONATSABSCHLUSS_STATUS_NEXT_Ebene1` (`Ebene1`),
                                                       KEY `PROJEKT_MONATSABSCHLUSS_STATUS_NEXT_Ebene2` (`Ebene2`),
                                                       KEY `PROJEKT_MONATSABSCHLUSS_STATUS_NEXT_Ebene3` (`Ebene3`),
                                                       CONSTRAINT `PROJEKT_MONATSABSCHLUSS_STATUS_NEXT_NowID` FOREIGN KEY (`ProjektMonatsabschlussStatusNowId`) REFERENCES `PROJEKT_MONATSABSCHLUSS_STATUS` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                                       CONSTRAINT `PROJEKT_MONATSABSCHLUSS_STATUS_NEXT_ThenId` FOREIGN KEY (`ProjektMonatsabschlussStatusThenId`) REFERENCES `PROJEKT_MONATSABSCHLUSS_STATUS` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_PK`
--

DROP TABLE IF EXISTS `PROJEKT_PK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_PK` (
                              `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                              `PROJEKT_id` int(6) unsigned DEFAULT NULL,
                              `ADRESSE_id` int(6) unsigned DEFAULT NULL,
                              `PROJEKT_PK_FUNKTIONEN_id` int(6) unsigned DEFAULT NULL,
                              `beginn` date DEFAULT NULL,
                              `ende` date DEFAULT NULL,
                              `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `aeuser` char(35) DEFAULT NULL,
                              `erda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                              `eruser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8709 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_PK_FUNKTIONEN`
--

DROP TABLE IF EXISTS `PROJEKT_PK_FUNKTIONEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_PK_FUNKTIONEN` (
                                         `id` int(6) unsigned NOT NULL,
                                         `bezeichnung` char(100) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_SEMINARKATEGORIE`
--

DROP TABLE IF EXISTS `PROJEKT_SEMINARKATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_SEMINARKATEGORIE` (
                                            `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                            `ProjektSeminarkategorieGruppeId` int(10) unsigned NOT NULL,
                                            `Name` varchar(80) NOT NULL,
                                            `Active` int(1) unsigned NOT NULL DEFAULT 1,
                                            `ErstellungsBenutzer` char(35) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                            `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                            `AenderungsBenutzer` char(35) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                            `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                            PRIMARY KEY (`Id`),
                                            UNIQUE KEY `UQ_PJ_SMKATEGORIE_ProjektSeminarkategorieGruppeId_Name` (`ProjektSeminarkategorieGruppeId`,`Name`),
                                            KEY `PJ_SMKATEGORIE_Active` (`Active`),
                                            CONSTRAINT `PROJEKT_SEMINARKATEGORIE_ibfk_1` FOREIGN KEY (`ProjektSeminarkategorieGruppeId`) REFERENCES `PROJEKT_SEMINARKATEGORIE_GRUPPE` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_SEMINARKATEGORIE_GRUPPE`
--

DROP TABLE IF EXISTS `PROJEKT_SEMINARKATEGORIE_GRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_SEMINARKATEGORIE_GRUPPE` (
                                                   `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                                   `ProjektId` int(11) NOT NULL,
                                                   `Name` varchar(80) NOT NULL,
                                                   `SingleSelection` int(1) unsigned DEFAULT 0,
                                                   `ErstellungsBenutzer` char(35) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                                   `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                   `AenderungsBenutzer` char(35) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                                   `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                   PRIMARY KEY (`Id`),
                                                   UNIQUE KEY `PJ_SMKATEGORIE_GRUPPE_ProjektId_Name` (`ProjektId`,`Name`),
                                                   CONSTRAINT `PROJEKT_SEMINARKATEGORIE_GRUPPE_ibfk_1` FOREIGN KEY (`ProjektId`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_TAETIGKEITZUSATZ_GRUPPEN`
--

DROP TABLE IF EXISTS `PROJEKT_TAETIGKEITZUSATZ_GRUPPEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_TAETIGKEITZUSATZ_GRUPPEN` (
                                                    `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                    `kstgr` int(3) unsigned DEFAULT NULL,
                                                    `PROJEKTTYPEN_id` int(6) unsigned DEFAULT NULL,
                                                    `gruppe` int(6) unsigned DEFAULT NULL,
                                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_TEILNEHMERKATEGORIE`
--

DROP TABLE IF EXISTS `PROJEKT_TEILNEHMERKATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_TEILNEHMERKATEGORIE` (
                                               `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                               `ProjektTeilnehmerkategorieGruppeId` int(11) unsigned NOT NULL,
                                               `Name` varchar(80) NOT NULL,
                                               `Active` int(1) unsigned NOT NULL DEFAULT 1,
                                               `ErstellungsBenutzer` char(35) NOT NULL,
                                               `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                               `AenderungsBenutzer` char(35) DEFAULT NULL,
                                               `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                               PRIMARY KEY (`Id`),
                                               UNIQUE KEY `PJ_TNKATEGORIE_ProjektTeilnehmerkategorieGruppeId_Name` (`ProjektTeilnehmerkategorieGruppeId`,`Name`),
                                               KEY `PJ_TNKATEGORIE_Active` (`Active`),
                                               CONSTRAINT `PROJEKT_TEILNEHMERKATEGORIE_ibfk_1` FOREIGN KEY (`ProjektTeilnehmerkategorieGruppeId`) REFERENCES `PROJEKT_TEILNEHMERKATEGORIE_GRUPPE` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=617 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_TEILNEHMERKATEGORIE_GRUPPE`
--

DROP TABLE IF EXISTS `PROJEKT_TEILNEHMERKATEGORIE_GRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_TEILNEHMERKATEGORIE_GRUPPE` (
                                                      `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                                      `ProjektId` int(11) NOT NULL,
                                                      `Name` varchar(80) NOT NULL,
                                                      `SingleSelection` int(1) unsigned NOT NULL DEFAULT 0,
                                                      `ErstellungsBenutzer` char(35) NOT NULL,
                                                      `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                      `AenderungsBenutzer` char(35) DEFAULT NULL,
                                                      `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                      PRIMARY KEY (`Id`),
                                                      UNIQUE KEY `UQ_PJ_TNKATEGORIE_GRUPPE_ProjektId_Name` (`ProjektId`,`Name`),
                                                      CONSTRAINT `PROJEKT_TEILNEHMERKATEGORIE_GRUPPE_ibfk_1` FOREIGN KEY (`ProjektId`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=260 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROJEKT_TEILNEHMERKATEGORIE_OLD`
--

DROP TABLE IF EXISTS `PROJEKT_TEILNEHMERKATEGORIE_OLD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROJEKT_TEILNEHMERKATEGORIE_OLD` (
                                                   `PROJEKT_id` int(6) unsigned NOT NULL DEFAULT 0,
                                                   `TEILNEHMERKATEGORIE_id` int(6) unsigned NOT NULL DEFAULT 0,
                                                   `bezeichnung` char(100) DEFAULT NULL,
                                                   `aktiv` tinyint(1) DEFAULT NULL,
                                                   PRIMARY KEY (`PROJEKT_id`,`TEILNEHMERKATEGORIE_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRUEFUNG`
--

DROP TABLE IF EXISTS `PRUEFUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRUEFUNG` (
                            `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                            `PruefungUnterkategorieId` int(10) unsigned NOT NULL,
                            `ProjektId` int(6) NOT NULL,
                            `Name` varchar(80) NOT NULL,
                            `Statistikrelevant` int(1) unsigned NOT NULL DEFAULT 0,
                            `Beschreibung` text DEFAULT NULL,
                            `ErstellungsBenutzer` char(35) NOT NULL,
                            `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                            `AenderungsBenutzer` char(35) DEFAULT NULL,
                            `AenderungsDatum` char(35) DEFAULT NULL,
                            PRIMARY KEY (`Id`),
                            KEY `FK_PUEFUNG_PruefungUnterkategorieId` (`PruefungUnterkategorieId`),
                            KEY `FK_PRUEFUNG_ProjektId` (`ProjektId`),
                            CONSTRAINT `PRUEFUNG_ProjektId` FOREIGN KEY (`ProjektId`) REFERENCES `PROJEKT` (`PJnr`) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT `PRUEFUNG_PruefungUnterkategorieId` FOREIGN KEY (`PruefungUnterkategorieId`) REFERENCES `PRUEFUNG_UNTERKATEGORIE` (`Id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12327 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRUEFUNG_ART`
--

DROP TABLE IF EXISTS `PRUEFUNG_ART`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRUEFUNG_ART` (
                                `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                `Name` varchar(50) NOT NULL,
                                `Aktiv` int(1) unsigned NOT NULL DEFAULT 1,
                                `Reihenfolge` int(2) unsigned NOT NULL,
                                PRIMARY KEY (`Id`),
                                UNIQUE KEY `UQ_PRUEFUNG_ART_Name` (`Name`),
                                UNIQUE KEY `UQ_PRUEFUNG_ART_Reihenfolge` (`Reihenfolge`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRUEFUNG_ERGEBNIS`
--

DROP TABLE IF EXISTS `PRUEFUNG_ERGEBNIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRUEFUNG_ERGEBNIS` (
                                     `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                     `NAME` varchar(50) NOT NULL,
                                     PRIMARY KEY (`Id`),
                                     UNIQUE KEY `NAME_UNIQUE` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRUEFUNG_ERGEBNIS__PRUEFUNG_KATEGORIE`
--

DROP TABLE IF EXISTS `PRUEFUNG_ERGEBNIS__PRUEFUNG_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRUEFUNG_ERGEBNIS__PRUEFUNG_KATEGORIE` (
                                                         `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                                         `PruefungErgebnisId` int(10) unsigned NOT NULL,
                                                         `PruefungKategorieId` int(10) unsigned NOT NULL,
                                                         `Sortierung` int(2) unsigned NOT NULL,
                                                         PRIMARY KEY (`Id`),
                                                         UNIQUE KEY `UQ_P_ERGEBNIS__P_KATEGORIE_PruefungKategorieId_Sortierung` (`PruefungKategorieId`,`Sortierung`),
                                                         KEY `FK_P_ERGEBNIS__P_KATEGORIE_PruefungErgebnisId` (`PruefungErgebnisId`),
                                                         CONSTRAINT `P_ERGEBNIS__P_KATEGORIE_PruefungErgebnisId` FOREIGN KEY (`PruefungErgebnisId`) REFERENCES `PRUEFUNG_ERGEBNIS` (`Id`) ON UPDATE CASCADE,
                                                         CONSTRAINT `P_ERGEBNIS__P_KATEGORIE_PruefungKategorieId` FOREIGN KEY (`PruefungKategorieId`) REFERENCES `PRUEFUNG_KATEGORIE` (`Id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRUEFUNG_KATEGORIE`
--

DROP TABLE IF EXISTS `PRUEFUNG_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRUEFUNG_KATEGORIE` (
                                      `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                      `Kurzname` varchar(15) NOT NULL,
                                      `Name` varchar(50) NOT NULL,
                                      `Aktiv` int(1) unsigned NOT NULL DEFAULT 1,
                                      PRIMARY KEY (`Id`),
                                      UNIQUE KEY `UQ_PRUEFUNG_KAGEGORIE_Kurzname` (`Kurzname`),
                                      UNIQUE KEY `UQ_PREUFUNG_KATEGORIE_Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRUEFUNG_STUFE`
--

DROP TABLE IF EXISTS `PRUEFUNG_STUFE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRUEFUNG_STUFE` (
                                  `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                  `Name` varchar(50) NOT NULL,
                                  `Aktiv` int(1) unsigned NOT NULL DEFAULT 1,
                                  `Reihenfolge` int(2) unsigned NOT NULL,
                                  `PruefungKategorieId` int(10) unsigned NOT NULL,
                                  PRIMARY KEY (`Id`),
                                  UNIQUE KEY `UQ_PRUEFUNG_STUFE_Name` (`Name`),
                                  UNIQUE KEY `UQ_PRUEFUNG_STUFE_Reihenfolge_PruefungKategorieId` (`Reihenfolge`,`PruefungKategorieId`),
                                  KEY `FK_PRUEFUNG_STUFE_PruefungKategorieId` (`PruefungKategorieId`),
                                  CONSTRAINT `PRUEFUNG_STUFE_PruefungKategorieId` FOREIGN KEY (`PruefungKategorieId`) REFERENCES `PRUEFUNG_KATEGORIE` (`Id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRUEFUNG_UNTERKATEGORIE`
--

DROP TABLE IF EXISTS `PRUEFUNG_UNTERKATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRUEFUNG_UNTERKATEGORIE` (
                                           `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                           `Name` varchar(50) NOT NULL,
                                           `Aktiv` int(1) unsigned NOT NULL DEFAULT 1,
                                           `Reihenfolge` int(2) unsigned NOT NULL,
                                           `PruefungKategorieId` int(10) unsigned NOT NULL,
                                           PRIMARY KEY (`Id`),
                                           UNIQUE KEY `UQ_SP_UK_Name` (`Name`),
                                           UNIQUE KEY `UQ_PRUEFUNG_UNTERKATEGORIE_Reihenfolge_PruefungKategorieId` (`Reihenfolge`,`PruefungKategorieId`),
                                           KEY `FK_PRUEFUNG_UNTERKATEGORIE_PruefungKategorieI` (`PruefungKategorieId`),
                                           CONSTRAINT `PRUEFUNG_UNTERKATEGORIE_PruefungKategorieId` FOREIGN KEY (`PruefungKategorieId`) REFERENCES `PRUEFUNG_KATEGORIE` (`Id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRUEFUNG_ZUSATZDATEN`
--

DROP TABLE IF EXISTS `PRUEFUNG_ZUSATZDATEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRUEFUNG_ZUSATZDATEN` (
                                        `PruefungId` int(10) unsigned NOT NULL,
                                        `PruefungStufeId` int(10) unsigned DEFAULT NULL,
                                        `ErstellungsBenutzer` char(35) NOT NULL,
                                        `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                        `AenderungsBenutzer` char(35) DEFAULT NULL,
                                        `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                        PRIMARY KEY (`PruefungId`),
                                        KEY `FK_PRUEFUNG_ZUSATZDATEN_PruefungStufeId` (`PruefungStufeId`),
                                        CONSTRAINT `PRUEFUNG_ZUSATZDATEN_PruefungId` FOREIGN KEY (`PruefungId`) REFERENCES `PRUEFUNG` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                        CONSTRAINT `PRUEFUNG_ZUSATZDATEN_PruefungStufeId` FOREIGN KEY (`PruefungStufeId`) REFERENCES `PRUEFUNG_STUFE` (`Id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRUEFUNG__SEMINAR_TEILNEHMER`
--

DROP TABLE IF EXISTS `PRUEFUNG__SEMINAR_TEILNEHMER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRUEFUNG__SEMINAR_TEILNEHMER` (
                                                `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                                `PruefungId` int(10) unsigned NOT NULL,
                                                `SeminarId` int(6) NOT NULL,
                                                `TeilnehmerId` int(6) unsigned NOT NULL,
                                                `PruefungArtId` int(10) unsigned NOT NULL,
                                                `PruefungErgebnisId` int(10) unsigned DEFAULT NULL,
                                                `Anmeldedatum` date DEFAULT NULL,
                                                `Pruefungsdatum` date DEFAULT NULL,
                                                `Bemerkung` text DEFAULT NULL,
                                                `ErstellungsBenutzer` char(35) NOT NULL,
                                                `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                `AenderungsBenutzer` char(35) DEFAULT NULL,
                                                `AenderungsDatum` char(35) DEFAULT NULL,
                                                PRIMARY KEY (`id`),
                                                KEY `PRUEFUNG__SEMINAR_TEILNEHMER_PruefungId_idx` (`PruefungId`),
                                                KEY `FK_PRUEFUNG__SEMINAR_TEILNEHMER_SeminarId_TeilnehmerId` (`TeilnehmerId`,`SeminarId`),
                                                KEY `FK_PRUEFUNG__SEMINAR_TEILNEHMER_PruefungArtId` (`PruefungArtId`),
                                                KEY `FK_PRUEFUNG__SEMINAR_TEILNEHMER_PruefungErgebnisId` (`PruefungErgebnisId`),
                                                CONSTRAINT `PRUEFUNG__SEMINAR_TEILNEHMER_PruefungArtId` FOREIGN KEY (`PruefungArtId`) REFERENCES `PRUEFUNG_ART` (`Id`) ON UPDATE CASCADE,
                                                CONSTRAINT `PRUEFUNG__SEMINAR_TEILNEHMER_PruefungErgebnisId` FOREIGN KEY (`PruefungErgebnisId`) REFERENCES `PRUEFUNG_ERGEBNIS` (`Id`) ON UPDATE CASCADE,
                                                CONSTRAINT `PRUEFUNG__SEMINAR_TEILNEHMER_PruefungId` FOREIGN KEY (`PruefungId`) REFERENCES `PRUEFUNG` (`Id`) ON UPDATE CASCADE,
                                                CONSTRAINT `PRUEFUNG__SEMINAR_TEILNEHMER_SeminarId_TeilnehmerId` FOREIGN KEY (`TeilnehmerId`, `SeminarId`) REFERENCES `SM_TN` (`ADRESSE_ADadnr`, `SEMINAR_SMnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=104207 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PSECTION`
--

DROP TABLE IF EXISTS `PSECTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PSECTION` (
                            `CSsectionID` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Section-ID',
                            `CSsection` varchar(20) DEFAULT NULL COMMENT 'Sektion',
                            `CSbezeichnung` varchar(80) DEFAULT NULL COMMENT 'Bezeichnung',
                            `CSstatus` enum('a','i') DEFAULT NULL COMMENT 'Status',
                            `CSdatumshow` enum('y','n') DEFAULT 'y' COMMENT 'Anzeige des Datums der Meldung',
                            `CSauthorshow` enum('y','n') DEFAULT 'y' COMMENT 'Anzeige des Verfassers',
                            `CSkommentar` enum('y','n') DEFAULT 'y' COMMENT 'möglichkeit zur eingabe von Kommentaren',
                            `CSloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschekennzeichen',
                            `CSaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungs-Datum',
                            `CSaeuser` char(35) DEFAULT NULL,
                            `CSerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungs-Datum',
                            `CSeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`CSsectionID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZKONTO`
--

DROP TABLE IF EXISTS `PZKONTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZKONTO` (
                           `DVnr` int(6) unsigned NOT NULL COMMENT 'Dienstvertrag-Nummer',
                           `ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                           `KTtyp` enum('U','ZA') NOT NULL COMMENT 'Konto-Typ (Urlaub, Zeitausgleich)',
                           `KTsaldo` decimal(7,2) DEFAULT NULL COMMENT 'Konto Saldo',
                           `KTdatumsaldo` date DEFAULT NULL COMMENT 'Datum Saldo',
                           `KTysaldo` decimal(7,2) DEFAULT NULL COMMENT 'Jahressaldo',
                           `KTydatumsaldo` date DEFAULT NULL COMMENT 'Datum Jahressaldo',
                           `KTaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                           `KTaeuser` char(35) DEFAULT NULL,
                           `KTerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                           `KTeruser` char(35) DEFAULT NULL,
                           PRIMARY KEY (`DVnr`,`ADadnr`,`KTtyp`),
                           KEY `PZKONTO_FKIndex1` (`DVnr`,`ADadnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZLEISTUNG`
--

DROP TABLE IF EXISTS `PZLEISTUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZLEISTUNG` (
                              `LZnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Laufende Nummer',
                              `ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                              `PMjahr` int(4) NOT NULL COMMENT 'Jahr',
                              `PMmonat` int(2) NOT NULL COMMENT 'Monat',
                              `LZtyp` enum('l','s') DEFAULT 'l' COMMENT 'Typ Leistung/Spesen',
                              `PZRECHNUNG_REnr` int(6) NOT NULL DEFAULT 0,
                              `LZdatum` date DEFAULT NULL COMMENT 'Datum von',
                              `LZdatumt` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Zeit von',
                              `LZbis` date DEFAULT NULL COMMENT 'Datum bis',
                              `LZbist` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Zeit bis',
                              `LZpauseVon` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Mittagspause von',
                              `LZpauseBis` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Mittagspause bis',
                              `LZdauer` decimal(4,2) DEFAULT NULL COMMENT 'Dauer in Std.',
                              `LZtaetnr` int(6) unsigned DEFAULT NULL COMMENT 'Tätigkeit',
                              `LZtaettyp` enum('anw','abw') DEFAULT NULL COMMENT 'Tätigkeitstyp (anwesend/abwesend)',
                              `PZ_TAETIGKEIT_ZUSATZ_id` int(6) unsigned DEFAULT NULL,
                              `WORKSHOP_id` int(6) unsigned DEFAULT NULL,
                              `LZstundensatz` decimal(9,2) DEFAULT NULL COMMENT 'Stundensatz/Honorar',
                              `SM_AD_SMADnr` int(6) DEFAULT NULL COMMENT 'SMAD laufende Nummer',
                              `LZSMnr` int(6) DEFAULT NULL COMMENT 'Seminar-Nummer',
                              `LZKSTGR` int(3) unsigned DEFAULT NULL COMMENT 'Kostenstelle Gruppe',
                              `LZDVnr` int(6) DEFAULT NULL COMMENT 'Dienstvertrags-Nummer',
                              `LZfahrtvonnach` text DEFAULT NULL COMMENT 'Fahrtvonnach',
                              `LZkfz_kennzeichen` char(20) DEFAULT NULL COMMENT 'KFZ-Kennzeichen für KM-Geld',
                              `LZkm` decimal(9,2) DEFAULT NULL COMMENT 'Km',
                              `LZkmgeld` decimal(9,2) DEFAULT NULL COMMENT 'Kmgeld',
                              `LZspesen` decimal(9,2) DEFAULT NULL COMMENT 'Spesen',
                              `LZtagesdiaeten` decimal(9,2) DEFAULT NULL COMMENT 'Tagesdiaeten',
                              `LZtagesdiaetensatz` int(6) DEFAULT NULL COMMENT 'Tagesdiaetensatz',
                              `LZnaechtigung` decimal(9,2) DEFAULT NULL COMMENT 'Naechtigung',
                              `LZspesensonstig` decimal(9,2) DEFAULT NULL COMMENT 'Spesensonstig',
                              `LZbemerk` text DEFAULT NULL COMMENT 'Bemerkung',
                              `LZhomeoffice` varchar(15) DEFAULT NULL,
                              `LZstatus` int(1) unsigned DEFAULT NULL COMMENT 'Status (0..offen, 1..zur ueberarbeitung,2..zur best,3,best.)',
                              `LZloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                              `LZaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                              `LZaeuser` char(35) DEFAULT NULL,
                              `LZerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                              `LZeruser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`LZnr`,`ADadnr`,`PMjahr`,`PMmonat`),
                              KEY `PZLEISTUNG_FKIndex1` (`ADadnr`,`PMjahr`,`PMmonat`),
                              KEY `PZLEISTUNG_FKIndex2` (`SM_AD_SMADnr`,`LZSMnr`),
                              KEY `PZLEISTUNG_FKIndex3` (`PZRECHNUNG_REnr`),
                              KEY `PZLEISTUNG_LZdatum` (`LZdatum`) USING BTREE,
                              KEY `LEISTUNG_WORKSHOP` (`WORKSHOP_id`),
                              KEY `PZLEISTUNG_LZSMnr` (`LZSMnr`)
) ENGINE=InnoDB AUTO_INCREMENT=5836558 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZLEISTUNG_SEMINARBUCH_ZUSATZ`
--

DROP TABLE IF EXISTS `PZLEISTUNG_SEMINARBUCH_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZLEISTUNG_SEMINARBUCH_ZUSATZ` (
                                                 `pz_leistung_id` int(10) unsigned NOT NULL,
                                                 `seminarinhalt` text DEFAULT NULL,
                                                 `bemerkung` text DEFAULT NULL,
                                                 `AnzeigeFuerMitbetreuung` int(1) unsigned DEFAULT NULL,
                                                 `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                 `aeuser` char(35) DEFAULT NULL,
                                                 `erda` datetime /* mariadb-5.3 */ NOT NULL,
                                                 `eruser` char(35) NOT NULL,
                                                 PRIMARY KEY (`pz_leistung_id`),
                                                 KEY `PZLEISTUNG_SB_ZUSATZ_AnzeigeFuerMitbetreuung` (`AnzeigeFuerMitbetreuung`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZMONAT`
--

DROP TABLE IF EXISTS `PZMONAT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZMONAT` (
                           `ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                           `PMjahr` int(4) NOT NULL COMMENT 'Jahr',
                           `PMmonat` int(2) NOT NULL COMMENT 'Monat',
                           `PMDZnr` int(6) DEFAULT NULL COMMENT 'Dienstvertrag-Zusatz-Nummer',
                           `PMDVtyp` enum('frei','fix') DEFAULT NULL COMMENT 'DV-Typ frei/fix',
                           `PMstatus` int(1) DEFAULT NULL,
                           `PMabschlussdatum` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum Abschluss',
                           `PMabschlussIP` char(16) DEFAULT NULL COMMENT 'IP-Adresse Abschluss',
                           `PMabschlussbenutzer` char(35) DEFAULT NULL COMMENT 'Abschluss-Benutzer',
                           `PMsoll` decimal(5,2) DEFAULT NULL COMMENT 'Soll-Stunden',
                           `PMist` decimal(5,2) DEFAULT NULL COMMENT 'Ist-Stunden',
                           `PMurlaub` int(2) DEFAULT NULL COMMENT 'Summe Urlaub',
                           `PMzakonsum` decimal(7,2) DEFAULT NULL COMMENT 'Kontosaldo-ZA',
                           `PMaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                           `PMaeuser` char(35) DEFAULT NULL,
                           `PMerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                           `PMeruser` char(35) DEFAULT NULL,
                           PRIMARY KEY (`ADadnr`,`PMjahr`,`PMmonat`),
                           KEY `PZMONAT_FKIndex1` (`ADadnr`),
                           KEY `PZMONAT_PMDZnr` (`PMDZnr`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZMONAT_BACKUP`
--

DROP TABLE IF EXISTS `PZMONAT_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZMONAT_BACKUP` (
                                  `ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                                  `PMjahr` int(4) NOT NULL COMMENT 'Jahr',
                                  `PMmonat` int(2) NOT NULL COMMENT 'Monat',
                                  `PMDZnr` int(6) DEFAULT NULL COMMENT 'Dienstvertrag-Zusatz-Nummer',
                                  `PMDVtyp` enum('frei','fix') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'DV-Typ frei/fix',
                                  `PMstatus` int(1) DEFAULT NULL,
                                  `PMabschlussdatum` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum Abschluss',
                                  `PMabschlussIP` char(16) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'IP-Adresse Abschluss',
                                  `PMabschlussbenutzer` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Abschluss-Benutzer',
                                  `PMsoll` decimal(5,2) DEFAULT NULL COMMENT 'Soll-Stunden',
                                  `PMist` decimal(5,2) DEFAULT NULL COMMENT 'Ist-Stunden',
                                  `PMurlaub` int(2) DEFAULT NULL COMMENT 'Summe Urlaub',
                                  `PMzakonsum` decimal(7,2) DEFAULT NULL COMMENT 'Kontosaldo-ZA',
                                  `PMaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                  `PMaeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                  `PMerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                                  `PMeruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZMONAT_DELETED_RECORDS`
--

DROP TABLE IF EXISTS `PZMONAT_DELETED_RECORDS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZMONAT_DELETED_RECORDS` (
                                           `ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                                           `PMjahr` int(4) NOT NULL COMMENT 'Jahr',
                                           `PMmonat` int(2) NOT NULL COMMENT 'Monat',
                                           `PMDZnr` int(6) DEFAULT NULL COMMENT 'Dienstvertrag-Zusatz-Nummer',
                                           `PMDVtyp` enum('frei','fix') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'DV-Typ frei/fix',
                                           `PMstatus` int(1) DEFAULT NULL,
                                           `PMabschlussdatum` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum Abschluss',
                                           `PMabschlussIP` char(16) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'IP-Adresse Abschluss',
                                           `PMabschlussbenutzer` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Abschluss-Benutzer',
                                           `PMsoll` decimal(5,2) DEFAULT NULL COMMENT 'Soll-Stunden',
                                           `PMist` decimal(5,2) DEFAULT NULL COMMENT 'Ist-Stunden',
                                           `PMurlaub` int(2) DEFAULT NULL COMMENT 'Summe Urlaub',
                                           `PMzakonsum` decimal(7,2) DEFAULT NULL COMMENT 'Kontosaldo-ZA',
                                           `PMaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                           `PMaeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                           `PMerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                                           `PMeruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZMONAT_LOCAL_BACKUP`
--

DROP TABLE IF EXISTS `PZMONAT_LOCAL_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZMONAT_LOCAL_BACKUP` (
                                        `ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                                        `PMjahr` int(4) NOT NULL COMMENT 'Jahr',
                                        `PMmonat` int(2) NOT NULL COMMENT 'Monat',
                                        `PMDZnr` int(6) DEFAULT NULL COMMENT 'Dienstvertrag-Zusatz-Nummer',
                                        `PMDVtyp` enum('frei','fix') CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'DV-Typ frei/fix',
                                        `PMstatus` int(1) DEFAULT NULL,
                                        `PMabschlussdatum` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum Abschluss',
                                        `PMabschlussIP` char(16) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'IP-Adresse Abschluss',
                                        `PMabschlussbenutzer` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Abschluss-Benutzer',
                                        `PMsoll` decimal(5,2) DEFAULT NULL COMMENT 'Soll-Stunden',
                                        `PMist` decimal(5,2) DEFAULT NULL COMMENT 'Ist-Stunden',
                                        `PMurlaub` int(2) DEFAULT NULL COMMENT 'Summe Urlaub',
                                        `PMzakonsum` decimal(7,2) DEFAULT NULL COMMENT 'Kontosaldo-ZA',
                                        `PMaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                        `PMaeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                        `PMerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                                        `PMeruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZRECHNUNG`
--

DROP TABLE IF EXISTS `PZRECHNUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZRECHNUNG` (
                              `REnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'laufende Rechnungsnummer',
                              `PZMONAT_PMmonat` int(2) NOT NULL COMMENT 'Monat',
                              `PZMONAT_PMjahr` int(4) NOT NULL COMMENT 'Jahr',
                              `PZMONAT_ADadnr` int(6) unsigned NOT NULL COMMENT 'Adresse',
                              `RErechnungsnr` char(10) DEFAULT NULL COMMENT 'Rechnungsnummer',
                              `REdatum` date DEFAULT NULL COMMENT 'Datum',
                              `REsteuerbefreit` enum('n','y') DEFAULT 'n' COMMENT 'Steuerbefreit',
                              `REkstgr` int(3) DEFAULT NULL COMMENT 'Kostenstelle',
                              `REkstnr` int(3) DEFAULT NULL,
                              `REkostentraeger` int(7) DEFAULT NULL,
                              `REnettobetrag` decimal(9,2) DEFAULT NULL COMMENT 'Rechnungsbetrag Netto',
                              `REanschrift` text DEFAULT NULL COMMENT 'Anschrift',
                              `REstorno` enum('n','y') DEFAULT 'n' COMMENT 'Rechnung storniert',
                              `REloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                              `REaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                              `REaeuser` char(35) DEFAULT NULL COMMENT 'Änderungs-Benutzer',
                              `REerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                              `REeruser` char(35) DEFAULT NULL COMMENT 'Erstellungs-Benutzer',
                              PRIMARY KEY (`REnr`),
                              KEY `PZRECHNUNG_FKIndex1` (`PZMONAT_ADadnr`,`PZMONAT_PMjahr`,`PZMONAT_PMmonat`)
) ENGINE=InnoDB AUTO_INCREMENT=55283 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZ_TAETIGKEITZUSATZGRUPPEN_INFO`
--

DROP TABLE IF EXISTS `PZ_TAETIGKEITZUSATZGRUPPEN_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZ_TAETIGKEITZUSATZGRUPPEN_INFO` (
                                                   `id` int(6) unsigned NOT NULL,
                                                   `beschreibung` text DEFAULT NULL,
                                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZ_TAETIGKEITZUSATZ_GRUPPEN`
--

DROP TABLE IF EXISTS `PZ_TAETIGKEITZUSATZ_GRUPPEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZ_TAETIGKEITZUSATZ_GRUPPEN` (
                                               `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                               `gruppe` int(6) unsigned DEFAULT NULL,
                                               `taetigkeit_id` int(6) unsigned DEFAULT NULL COMMENT 'KEYTABLE.KYnr WHERE KYname = ''PZtaet''',
                                               `PZ_TAETIGKEIT_ZUSATZ_id` int(6) unsigned DEFAULT NULL,
                                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PZ_TAETIGKEIT_ZUSATZ`
--

DROP TABLE IF EXISTS `PZ_TAETIGKEIT_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PZ_TAETIGKEIT_ZUSATZ` (
                                        `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                        `bezeichnung` char(100) DEFAULT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_BUDGET`
--

DROP TABLE IF EXISTS `P_BUDGET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_BUDGET` (
                            `PAKostenstelle` int(3) NOT NULL,
                            `PAJahr` int(4) NOT NULL,
                            `PHguvpos` int(3) NOT NULL,
                            `PHkostenart` int(6) DEFAULT NULL,
                            `PHw01` decimal(9,2) DEFAULT NULL,
                            `PHw02` decimal(9,2) DEFAULT NULL,
                            `PHw03` decimal(9,2) DEFAULT NULL,
                            `PHw04` decimal(9,2) DEFAULT NULL,
                            `PHw05` decimal(9,2) DEFAULT NULL,
                            `PHw06` decimal(9,2) DEFAULT NULL,
                            `PHw07` decimal(9,2) DEFAULT NULL,
                            `PHw08` decimal(9,2) DEFAULT NULL,
                            `PHw09` decimal(9,2) DEFAULT NULL,
                            `PHw10` decimal(9,2) DEFAULT NULL,
                            `PHw11` decimal(9,2) DEFAULT NULL,
                            `PHw12` decimal(9,2) DEFAULT NULL,
                            `PHbemerkung` text CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                            `PHaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `PHaeuser` char(35) DEFAULT NULL,
                            `PHerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `PHeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`PAKostenstelle`,`PAJahr`,`PHguvpos`),
                            KEY `P_BUDGET_FKIndex2` (`PAJahr`,`PAKostenstelle`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_INVESTITION`
--

DROP TABLE IF EXISTS `P_INVESTITION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_INVESTITION` (
                                 `PGinvnr` int(6) NOT NULL AUTO_INCREMENT,
                                 `PAKostenstelle` int(3) NOT NULL,
                                 `PAJahr` int(4) NOT NULL,
                                 `PGtyp` enum('afa','gwg') NOT NULL DEFAULT 'afa',
                                 `PGstatus` enum('erfasst','beantragt','genehmigt','verbraucht','abgelehnt','tw_verbraucht','freigegeben','zurueckgezogen') NOT NULL,
                                 `PGnum` int(6) unsigned DEFAULT 0 COMMENT 'Laufende Nummer',
                                 `PGprioritaet` int(6) DEFAULT NULL,
                                 `PGinvestitionsart` int(6) DEFAULT NULL,
                                 `PGinvestitionsgrund` int(6) DEFAULT NULL,
                                 `PGbezeichnung` char(255) DEFAULT NULL,
                                 `PGbeschreibung` text DEFAULT NULL,
                                 `PGbegruendung` text DEFAULT NULL,
                                 `PGgenehmigt` date DEFAULT NULL,
                                 `PGinvestbetrag` decimal(9,2) DEFAULT NULL,
                                 `PGinvestabmonat` int(2) DEFAULT NULL,
                                 `PGnutzungsdauer` int(3) DEFAULT NULL,
                                 `PGw01` decimal(9,2) DEFAULT NULL,
                                 `PGw02` decimal(9,2) DEFAULT NULL,
                                 `PGw03` decimal(9,2) DEFAULT NULL,
                                 `PGw04` decimal(9,2) DEFAULT NULL,
                                 `PGw05` decimal(9,2) DEFAULT NULL,
                                 `PGw06` decimal(9,2) DEFAULT NULL,
                                 `PGw07` decimal(9,2) DEFAULT NULL,
                                 `PGw08` decimal(9,2) DEFAULT NULL,
                                 `PGw09` decimal(9,2) DEFAULT NULL,
                                 `PGw10` decimal(9,2) DEFAULT NULL,
                                 `PGw11` decimal(9,2) DEFAULT NULL,
                                 `PGw12` decimal(9,2) DEFAULT NULL,
                                 `PGloek` enum('n','y') DEFAULT 'n',
                                 `PGaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `PGaeuser` char(35) DEFAULT NULL,
                                 `PGerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `PGeruser` char(35) DEFAULT NULL,
                                 PRIMARY KEY (`PGinvnr`,`PAKostenstelle`,`PAJahr`),
                                 KEY `P_INVESTITION_FKIndex1` (`PAJahr`,`PAKostenstelle`)
) ENGINE=InnoDB AUTO_INCREMENT=3674 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_INVESTITION_LOG`
--

DROP TABLE IF EXISTS `P_INVESTITION_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_INVESTITION_LOG` (
                                     `PLid` int(10) NOT NULL AUTO_INCREMENT,
                                     `PGinvnr` int(10) NOT NULL DEFAULT 0,
                                     `PGinvestbetrag` decimal(9,2) DEFAULT NULL,
                                     `PGinvestabmonat` int(3) DEFAULT NULL,
                                     `PGw01` decimal(9,2) DEFAULT NULL,
                                     `PGw02` decimal(9,2) DEFAULT NULL,
                                     `PGw03` decimal(9,2) DEFAULT NULL,
                                     `PGw04` decimal(9,2) DEFAULT NULL,
                                     `PGw05` decimal(9,2) DEFAULT NULL,
                                     `PGw06` decimal(9,2) DEFAULT NULL,
                                     `PGw07` decimal(9,2) DEFAULT NULL,
                                     `PGw08` decimal(9,2) DEFAULT NULL,
                                     `PGw09` decimal(9,2) DEFAULT NULL,
                                     `PGw10` decimal(9,2) DEFAULT NULL,
                                     `PGw11` decimal(9,2) DEFAULT NULL,
                                     `PGw12` decimal(9,2) DEFAULT NULL,
                                     `PGloek` enum('n','y') DEFAULT 'n',
                                     `PGaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     `PGaeuser` varchar(35) DEFAULT NULL,
                                     PRIMARY KEY (`PLid`),
                                     KEY `PGinvnr` (`PGinvnr`,`PGinvestbetrag`,`PGinvestabmonat`,`PGw01`,`PGw02`,`PGw03`,`PGw04`,`PGw05`,`PGw06`,`PGw07`,`PGw08`,`PGw09`,`PGw10`,`PGw11`,`PGw12`)
) ENGINE=MyISAM AUTO_INCREMENT=2340 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_INVESTITION_VERBRAUCH`
--

DROP TABLE IF EXISTS `P_INVESTITION_VERBRAUCH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_INVESTITION_VERBRAUCH` (
                                           `id` int(10) NOT NULL AUTO_INCREMENT,
                                           `PGinvnr` int(10) NOT NULL,
                                           `datum` date DEFAULT NULL,
                                           `monat` int(3) DEFAULT NULL,
                                           `betrag` decimal(9,2) DEFAULT NULL,
                                           `lieferant` varchar(100) DEFAULT NULL,
                                           `re_nr` varchar(100) DEFAULT NULL,
                                           `loek` enum('n','y') DEFAULT 'n',
                                           `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                           `eruser` varchar(35) DEFAULT NULL,
                                           `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                           `aeuser` varchar(35) DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           KEY `PGinvnr` (`PGinvnr`),
                                           CONSTRAINT `P_INVESTITION_VERBRAUCH_ibfk_1` FOREIGN KEY (`PGinvnr`) REFERENCES `P_INVESTITION` (`PGinvnr`)
) ENGINE=InnoDB AUTO_INCREMENT=355 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_MIETEN`
--

DROP TABLE IF EXISTS `P_MIETEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_MIETEN` (
                            `PDmietnr` int(6) NOT NULL AUTO_INCREMENT,
                            `PAJahr` int(4) NOT NULL,
                            `PAKostenstelle` int(3) NOT NULL,
                            `PDsatzart` enum('i','b') DEFAULT NULL,
                            `STANDORT_SOnr` int(6) DEFAULT NULL,
                            `PDplanungsstandort` char(255) DEFAULT NULL COMMENT 'Bezeichnung des geplanten Standortes, wenn STANDORT noch nicht existiert',
                            `STANDORT_SOfinanzierung` enum('e','m','l','s') DEFAULT NULL,
                            `PDbezeichnung` char(50) DEFAULT NULL,
                            `PDbemerkung` text DEFAULT NULL,
                            `PDbundesland` int(6) DEFAULT NULL,
                            `PDstrasse` char(50) DEFAULT NULL,
                            `PDland` char(6) DEFAULT NULL,
                            `PDplz` char(6) DEFAULT NULL,
                            `PDort` char(50) DEFAULT NULL,
                            `PDvertragsstatus` enum('p','v') DEFAULT NULL,
                            `PDmietbeginn` date DEFAULT NULL,
                            `PDmietende` date DEFAULT NULL,
                            `PDaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `PDaeuser` char(35) DEFAULT NULL,
                            `PDerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `PDeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`PDmietnr`,`PAJahr`,`PAKostenstelle`),
                            KEY `P_MIETEN_FKIndex1` (`PAJahr`,`PAKostenstelle`)
) ENGINE=InnoDB AUTO_INCREMENT=3364 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_MIETEN_MON`
--

DROP TABLE IF EXISTS `P_MIETEN_MON`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_MIETEN_MON` (
                                `PEmonat` int(2) NOT NULL,
                                `PAJahr` int(4) NOT NULL,
                                `PAKostenstelle` int(3) NOT NULL,
                                `PDmietnr` int(6) NOT NULL,
                                `PEmiete` decimal(9,2) DEFAULT NULL,
                                `PEmiete_ust` decimal(9,2) DEFAULT NULL,
                                `PEnebenkosten` decimal(9,2) DEFAULT NULL,
                                `PEnebenkosten_ust` decimal(9,2) DEFAULT NULL,
                                `PEsonderzahlung` decimal(9,2) DEFAULT NULL,
                                `PEsonderzahlung_ust` decimal(9,2) DEFAULT NULL,
                                `PEgarage` decimal(9,2) DEFAULT NULL,
                                `PEgarage_ust` decimal(9,2) DEFAULT NULL,
                                `PEreinigungkosten` decimal(9,2) DEFAULT NULL,
                                `PEmieterloes` decimal(9,2) DEFAULT NULL,
                                `PEmieterloes_ust` decimal(9,2) DEFAULT NULL,
                                `PEweiterverrechnung1betrag` decimal(9,2) DEFAULT NULL,
                                `PEweiterverrechnung1kst` decimal(9,2) DEFAULT NULL,
                                `PEweiterverrechnung2betrag` decimal(9,2) DEFAULT NULL,
                                `PEweiterverrechnung2kst` decimal(9,2) DEFAULT NULL,
                                `PEaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `PEaeuser` char(35) DEFAULT NULL,
                                `PEerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `PEeruser` char(35) DEFAULT NULL,
                                PRIMARY KEY (`PEmonat`,`PAJahr`,`PAKostenstelle`,`PDmietnr`),
                                KEY `P_MIETEN_MON_FKIndex1` (`PAKostenstelle`,`PAJahr`,`PDmietnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_PERSONAL`
--

DROP TABLE IF EXISTS `P_PERSONAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_PERSONAL` (
                              `PBpersnr` int(6) NOT NULL AUTO_INCREMENT,
                              `PAJahr` int(4) NOT NULL,
                              `PAKostenstelle` int(3) NOT NULL,
                              `PBsatzart` enum('i','b') DEFAULT NULL,
                              `SEMINAR_SMnr` int(6) NOT NULL DEFAULT 0,
                              `ADRESSE_ADnr` int(6) DEFAULT NULL,
                              `DIENSTVERTRAG_DVnr` int(6) DEFAULT NULL,
                              `PBpersonalnummerRZL` char(10) NOT NULL DEFAULT '',
                              `PBsvnr` varchar(11) DEFAULT NULL,
                              `PBzuname` char(100) DEFAULT NULL,
                              `PBvorname` char(100) DEFAULT NULL,
                              `PBstrasse` char(100) DEFAULT NULL,
                              `PBland` char(6) DEFAULT NULL,
                              `PBplz` char(6) DEFAULT NULL,
                              `PBort` char(50) DEFAULT NULL,
                              `PBtelefon` char(50) DEFAULT NULL,
                              `PBgeburtsdatum` date DEFAULT NULL,
                              `PBtyp` enum('frei','fix') DEFAULT NULL,
                              `PBeintritt` date DEFAULT NULL,
                              `PBaustritt` date DEFAULT NULL,
                              `PBanstellungsart` int(6) DEFAULT NULL,
                              `PBgewerbeschein` enum('y','n') DEFAULT NULL,
                              `PBfunktion` int(6) DEFAULT NULL,
                              `PBfunktionstr` char(128) DEFAULT NULL,
                              `PBbemerkung` text DEFAULT NULL,
                              `PBvertragsstatus` enum('p','v') NOT NULL DEFAULT 'v',
                              `PBaeda` datetime DEFAULT NULL,
                              `PBaeuser` char(35) DEFAULT NULL,
                              `PBerda` datetime DEFAULT NULL,
                              `PBeruser` char(35) DEFAULT NULL,
                              `PBsmadnr` int(6) unsigned NOT NULL DEFAULT 0,
                              PRIMARY KEY (`PBpersnr`,`PAJahr`,`PAKostenstelle`),
                              KEY `P_PERSONAL_FKIndex1` (`PAJahr`,`PAKostenstelle`),
                              KEY `P_PErSONAL_DVnr` (`DIENSTVERTRAG_DVnr`),
                              KEY `P_PERSONAL_PBsmadnr` (`PBsmadnr`)
) ENGINE=InnoDB AUTO_INCREMENT=410668 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_PERSONAL_MON`
--

DROP TABLE IF EXISTS `P_PERSONAL_MON`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_PERSONAL_MON` (
                                  `PAJahr` int(4) NOT NULL,
                                  `PAKostenstelle` int(3) NOT NULL,
                                  `PCmonat` int(2) NOT NULL,
                                  `PBpersnr` int(6) NOT NULL,
                                  `PCsonderzahlung` enum('n','y') DEFAULT NULL,
                                  `PCgehalt` decimal(9,2) DEFAULT NULL,
                                  `PCfahrtkosten` decimal(9,2) DEFAULT NULL,
                                  `PCzulage` decimal(9,2) DEFAULT NULL,
                                  `PCzulagebegruendung` text DEFAULT NULL,
                                  `PCreisekosten` decimal(9,2) DEFAULT NULL,
                                  `PCweiterverrechnung1betrag` decimal(9,2) DEFAULT NULL,
                                  `PCweiterverrechnung1kst` int(3) DEFAULT NULL,
                                  `PCweiterverrechnung2betrag` decimal(9,2) DEFAULT NULL,
                                  `PCweiterverrechnung2kst` int(3) DEFAULT NULL,
                                  `PCaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `PCaeuser` char(35) DEFAULT NULL,
                                  `PCerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `PCeruser` char(35) DEFAULT NULL,
                                  PRIMARY KEY (`PAJahr`,`PAKostenstelle`,`PCmonat`,`PBpersnr`),
                                  KEY `P_PERSONAL_MON_FKIndex1` (`PAKostenstelle`,`PAJahr`,`PBpersnr`),
                                  KEY `P_PERSONAL_persnr` (`PBpersnr`),
                                  CONSTRAINT `P_PERSONAL_persnr` FOREIGN KEY (`PBpersnr`) REFERENCES `P_PERSONAL` (`PBpersnr`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_PROJEKT`
--

DROP TABLE IF EXISTS `P_PROJEKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_PROJEKT` (
                             `PFlfdsemnr` int(10) unsigned NOT NULL AUTO_INCREMENT,
                             `PAKostenstelle` int(3) NOT NULL,
                             `PAJahr` int(4) NOT NULL,
                             `PFsatzart` enum('i','b') DEFAULT NULL,
                             `PROJEKT_PJnr` int(6) DEFAULT NULL,
                             `PFauftragsstatus` int(6) DEFAULT NULL,
                             `PFprojektpreis` decimal(9,2) DEFAULT NULL,
                             `PFprojektMNH` decimal(9,2) DEFAULT NULL,
                             `PFbezeichnung` char(100) DEFAULT NULL,
                             `PFbeschreibung` text DEFAULT NULL,
                             `P_MIETEN_mietnr` int(6) DEFAULT NULL COMMENT 'P_MIETEN.PDmietnr',
                             `PFort` char(50) DEFAULT NULL,
                             `PFpersonalkosten` decimal(9,2) DEFAULT NULL,
                             `PFsachkosten` decimal(9,2) DEFAULT NULL,
                             `PFbeginn` date DEFAULT NULL,
                             `PFende` date DEFAULT NULL,
                             `PFw01` decimal(9,2) DEFAULT NULL,
                             `PFw02` decimal(9,2) DEFAULT NULL,
                             `PFw03` decimal(9,2) DEFAULT NULL,
                             `PFw04` decimal(9,2) DEFAULT NULL,
                             `PFw05` decimal(9,2) DEFAULT NULL,
                             `PFw06` decimal(9,2) DEFAULT NULL,
                             `PFw07` decimal(9,2) DEFAULT NULL,
                             `PFw08` decimal(9,2) DEFAULT NULL,
                             `PFw09` decimal(9,2) DEFAULT NULL,
                             `PFw10` decimal(9,2) DEFAULT NULL,
                             `PFw11` decimal(9,2) DEFAULT NULL,
                             `PFw12` decimal(9,2) DEFAULT NULL,
                             `PFaeda` datetime DEFAULT NULL,
                             `PFaeuser` char(35) DEFAULT NULL,
                             `PFerda` datetime DEFAULT NULL,
                             `PFeruser` char(35) DEFAULT NULL,
                             PRIMARY KEY (`PFlfdsemnr`,`PAKostenstelle`,`PAJahr`),
                             KEY `P_SEMINAR_FKIndex1` (`PAJahr`,`PAKostenstelle`)
) ENGINE=InnoDB AUTO_INCREMENT=97790 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `P_WORKTABLE`
--

DROP TABLE IF EXISTS `P_WORKTABLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `P_WORKTABLE` (
                               `PAJahr` int(4) unsigned NOT NULL,
                               `PAKostenstelle` int(3) unsigned NOT NULL,
                               `KEYTABLE_KYnr` int(6) unsigned NOT NULL,
                               `KEYTABLE_KYindex` int(3) unsigned NOT NULL,
                               `WTgruppe` char(20) DEFAULT NULL,
                               `WTbezeichnung` char(128) DEFAULT NULL,
                               `WTw01` decimal(9,2) DEFAULT NULL,
                               `WTw02` decimal(9,2) DEFAULT NULL,
                               `WTw03` decimal(9,2) DEFAULT NULL,
                               `WTw04` decimal(9,2) DEFAULT NULL,
                               `WTw05` decimal(9,2) DEFAULT NULL,
                               `WTw06` decimal(9,2) DEFAULT NULL,
                               `WTw07` decimal(9,2) DEFAULT NULL,
                               `WTw08` decimal(9,2) DEFAULT NULL,
                               `WTw09` decimal(9,2) DEFAULT NULL,
                               `WTw10` decimal(9,2) DEFAULT NULL,
                               `WTw11` decimal(9,2) DEFAULT NULL,
                               `WTw12` decimal(9,2) DEFAULT NULL,
                               `WTsumme` decimal(11,2) DEFAULT NULL,
                               PRIMARY KEY (`PAJahr`,`PAKostenstelle`,`KEYTABLE_KYnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RAUM`
--

DROP TABLE IF EXISTS `RAUM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RAUM` (
                        `RAraumid` int(6) NOT NULL AUTO_INCREMENT COMMENT 'Raum-ID',
                        `SOstandortid` int(6) NOT NULL COMMENT 'Standort-ID',
                        `RAbezeichnung` char(60) DEFAULT NULL COMMENT 'Allgemeiner Raumname',
                        `RAbezeichnungSTM` char(60) DEFAULT NULL COMMENT 'Raumname STM',
                        `RAbeschreibung` text DEFAULT NULL COMMENT 'Ressourcen-Beschreibung',
                        `RAraumart` int(6) DEFAULT NULL COMMENT 'Raum-Art',
                        `RAraumpers` int(6) DEFAULT NULL COMMENT 'Raumgrösse Personen',
                        `RArauminternet` char(1) DEFAULT NULL COMMENT 'Internet-Anschluss',
                        `RAbestuhlung` char(1) DEFAULT NULL COMMENT 'Bestuhlung',
                        `RAmasseL` decimal(9,2) DEFAULT NULL COMMENT 'Masse Länge (m)',
                        `RAmasseB` decimal(9,2) DEFAULT NULL COMMENT 'Masse Breite (m)',
                        `RAmasseH` decimal(9,2) DEFAULT NULL COMMENT 'Masse Höhe (m)',
                        `RAraumflaeche` int(6) DEFAULT NULL COMMENT 'Raum-Fläche in  m²',
                        `RAvermieternr` int(6) DEFAULT NULL COMMENT 'Vermieter-Nummer',
                        `RAvermieter` char(40) DEFAULT NULL COMMENT 'Vermieter-Name - LÖSCHEN',
                        `RAmietemonat` decimal(9,2) DEFAULT NULL COMMENT 'Miete monatlich - LÖSCHEN',
                        `RAfinanzierung` enum('e','m','l','s') DEFAULT NULL COMMENT 'Finanzierung (Eigentum, Miete, Leasing, sonstige) - LÖSCHEN',
                        `RAafa` int(3) DEFAULT NULL COMMENT 'Abschreibungsdauer (Monate) - LÖSCHEN',
                        `RAafaende` date DEFAULT NULL COMMENT 'Datum Ende Abschreibung - LÖSCHEN',
                        `RAverfuegbarvon` date DEFAULT NULL COMMENT 'Datum verfügbar von',
                        `RAverfuegbarbis` date DEFAULT NULL COMMENT 'Datum verfügbar bis',
                        `RApreisprostunde` decimal(9,2) DEFAULT NULL COMMENT 'Preis pro Stunde',
                        `RAstatus` int(6) DEFAULT NULL COMMENT 'Status',
                        `RAbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                        `RAloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                        `RAaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungs-Datum',
                        `RAaeuser` char(35) DEFAULT NULL,
                        `RAerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungs-Datum',
                        `RAeruser` char(35) DEFAULT NULL,
                        PRIMARY KEY (`RAraumid`,`SOstandortid`),
                        KEY `Raum_FKIndex1` (`SOstandortid`)
) ENGINE=InnoDB AUTO_INCREMENT=2421 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RECHT`
--

DROP TABLE IF EXISTS `RECHT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECHT` (
                         `Rid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Recht ID',
                         `Rname` varchar(30) NOT NULL COMMENT 'Name des Rechtes',
                         `Raeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                         `Raeuser` char(35) DEFAULT NULL COMMENT 'Aenderungsbenutzer',
                         `Rerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                         `Reruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                         PRIMARY KEY (`Rid`),
                         UNIQUE KEY `Rname` (`Rname`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Rechte';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RECHTBEREICH`
--

DROP TABLE IF EXISTS `RECHTBEREICH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECHTBEREICH` (
                                `RBid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Rechtbereichs ID',
                                `RBname` varchar(30) NOT NULL COMMENT 'Name des Bereichs',
                                `RBaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                                `RBaeuser` char(35) DEFAULT NULL COMMENT 'Aenderungsbenutzer',
                                `RBerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                `RBeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                PRIMARY KEY (`RBid`),
                                UNIQUE KEY `RBname` (`RBname`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Bereiche auf die Rechtfunktionen und Rechte zugewiesen werde';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RECHTFUNKTION`
--

DROP TABLE IF EXISTS `RECHTFUNKTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECHTFUNKTION` (
                                 `RFid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Rechtfunktions ID',
                                 `RFname` varchar(30) NOT NULL COMMENT 'Name der Funktion',
                                 `RFaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                                 `RFaeuser` char(35) DEFAULT NULL COMMENT 'Aenderungsbenutzer',
                                 `RFerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                 `RFeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                 PRIMARY KEY (`RFid`),
                                 UNIQUE KEY `RFname` (`RFname`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Funktionen auf die Rechte zugewiesen werden';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RECHTGRUPPE`
--

DROP TABLE IF EXISTS `RECHTGRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECHTGRUPPE` (
                               `RGid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Gruppe ID',
                               `RGname` varchar(30) NOT NULL COMMENT 'Gruppen Name',
                               `RGloek` enum('n','y') DEFAULT 'n' COMMENT 'Loeschkennzeichen',
                               `RGaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                               `RGaeuser` char(35) DEFAULT NULL COMMENT 'Aenderungsbenutzer',
                               `RGerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                               `RGeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                               PRIMARY KEY (`RGid`),
                               UNIQUE KEY `RGname` (`RGname`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Rechtegruppen';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RECHTGRUPPE_BENUTZER`
--

DROP TABLE IF EXISTS `RECHTGRUPPE_BENUTZER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECHTGRUPPE_BENUTZER` (
                                        `RGBid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Zuordnungs ID',
                                        `RGB_RGid` int(10) unsigned NOT NULL COMMENT 'Gruppen ID',
                                        `RGB_BNid` int(10) unsigned NOT NULL COMMENT 'Benutzer ID',
                                        `RGBerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                        `RGBeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                        PRIMARY KEY (`RGBid`),
                                        KEY `FK_RECHTGRUPPE_BENUTZER_RGid` (`RGB_RGid`),
                                        KEY `FK_RECHTGRUPPE_BENUTZER_BNid` (`RGB_BNid`),
                                        CONSTRAINT `RECHTGRUPPE_BENUTZER_BNid` FOREIGN KEY (`RGB_BNid`) REFERENCES `BENUTZER` (`BNid`) ON DELETE CASCADE ON UPDATE CASCADE,
                                        CONSTRAINT `RECHTGRUPPE_BENUTZER_RGid` FOREIGN KEY (`RGB_RGid`) REFERENCES `RECHTGRUPPE` (`RGid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2517 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Zuordnung der Benutzer zu GRUPPEN';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RECHTGRUPPE_BENUTZER_KST`
--

DROP TABLE IF EXISTS `RECHTGRUPPE_BENUTZER_KST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECHTGRUPPE_BENUTZER_KST` (
                                            `RGBK_RGBid` int(10) unsigned NOT NULL COMMENT 'Zuordnungs ID Benutzer -> Gruppe',
                                            `RGBK_KSTGR` int(3) unsigned NOT NULL DEFAULT 0 COMMENT 'Kostenstellen Gruppe',
                                            `RGBK_KSTNR` int(3) unsigned NOT NULL DEFAULT 0 COMMENT 'Kostenstellen Nummer',
                                            `RGBKerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                            `RGBKeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                            PRIMARY KEY (`RGBK_RGBid`,`RGBK_KSTGR`,`RGBK_KSTNR`),
                                            KEY `FK_RECHTGRUPPE_BENUTZER_KST_KSTID` (`RGBK_KSTGR`,`RGBK_KSTNR`),
                                            CONSTRAINT `RECHTGRUPPE_BENUTZER_KST_KSTID` FOREIGN KEY (`RGBK_KSTGR`, `RGBK_KSTNR`) REFERENCES `FKOSTENSTELLE` (`KSTKSTGR`, `KSTKSTNR`) ON DELETE CASCADE ON UPDATE CASCADE,
                                            CONSTRAINT `RECHTGRUPPE_BENUTZER_KST_RGBid` FOREIGN KEY (`RGBK_RGBid`) REFERENCES `RECHTGRUPPE_BENUTZER` (`RGBid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Zuordnung der Benutzergruppen zu einer Kostenstelle';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RESDOC`
--

DROP TABLE IF EXISTS `RESDOC`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESDOC` (
                          `RDnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                          `RS_RSresid` int(6) unsigned NOT NULL,
                          `sySTORE_STnr` int(9) unsigned NOT NULL COMMENT 'Nummer im Store',
                          `RDloek` enum('n','y') DEFAULT 'n',
                          `RDaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                          `RDaeuser` char(35) DEFAULT NULL,
                          `RDerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                          `RDeruser` char(35) DEFAULT NULL,
                          PRIMARY KEY (`RDnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RESKOSTEN`
--

DROP TABLE IF EXISTS `RESKOSTEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESKOSTEN` (
                             `RKid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id der Kosten',
                             `RKkategorie` int(6) unsigned DEFAULT NULL COMMENT 'Kategorie',
                             `RKdatumab` date DEFAULT NULL COMMENT 'Ab wann gültig',
                             `RKpreis` float(9,2) unsigned DEFAULT NULL COMMENT 'Kosten',
  `RKloek` enum('y','n') DEFAULT 'n',
  `RKaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
  `RKaeuser` char(35) DEFAULT NULL,
  `RKerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
  `RKeruser` char(35) DEFAULT NULL,
  PRIMARY KEY (`RKid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Preise für einzelne Kategorien';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RESSOURCE`
--

DROP TABLE IF EXISTS `RESSOURCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RESSOURCE` (
                             `RSresid` int(6) NOT NULL AUTO_INCREMENT COMMENT 'Ressourcen-ID',
                             `RSbezeichnung` char(60) DEFAULT NULL COMMENT 'Ressourcen-Bezeichnung',
                             `RSbeschreibung` text DEFAULT NULL COMMENT 'Ressourcen-Beschreibung',
                             `RSresart` int(6) DEFAULT NULL COMMENT 'Ressourcen-Art',
                             `RSrestyp` int(6) DEFAULT NULL COMMENT 'Ressourcen-Typ',
                             `RSkategorie` int(6) DEFAULT NULL COMMENT 'Ressourcen-Kategorie',
                             `RSresklasse` enum('e','m') DEFAULT NULL COMMENT 'Ressourcen-Klasse',
                             `RSinvnr` char(20) DEFAULT NULL COMMENT 'Inventar-Nummer',
                             `RSsrnr` char(20) DEFAULT NULL COMMENT 'Serien-Nummer',
                             `RSmac_lan` char(12) DEFAULT NULL COMMENT 'MAC-Adresse LAN',
                             `RSmac_wlan` char(12) DEFAULT NULL COMMENT 'MAC-Adresse WLAN',
                             `RSmasseL` decimal(9,2) DEFAULT NULL COMMENT 'Masse Länge (cm)',
                             `RSmasseB` decimal(9,2) DEFAULT NULL COMMENT 'Masse Breite (cm)',
                             `RSmasseH` decimal(9,2) DEFAULT NULL COMMENT 'Masse Höhe (cm)',
                             `RSgewicht` decimal(9,2) DEFAULT NULL COMMENT 'Gewicht (kg)',
                             `RSfarbe` char(10) DEFAULT NULL COMMENT 'Farbe',
                             `RSstapelbar` char(1) DEFAULT NULL COMMENT 'stapelbar',
                             `RSkaufdatum` date DEFAULT NULL COMMENT 'Kaufdatum',
                             `RSkaufpreis` decimal(9,2) DEFAULT NULL COMMENT 'Kaufpreis',
                             `RSlieferantnr` int(6) DEFAULT NULL COMMENT 'Lieferant',
                             `RSlieferantnr_old` int(6) DEFAULT NULL COMMENT 'Lieferant ALT',
                             `RSgarantiedauer` int(3) DEFAULT NULL COMMENT 'Garantiedauer (Monate)',
                             `RSgarantieende` date DEFAULT NULL COMMENT 'Datum Garantie-Ende',
                             `RSfinanzierung` enum('e','m','l','s') DEFAULT NULL COMMENT 'Finanzierung (Eigentum, Miete, Leasing, sonstige)',
                             `RSafa` int(3) DEFAULT NULL COMMENT 'Abschreibungsdauer (Monate)',
                             `RSafaende` date DEFAULT NULL COMMENT 'Datum Ende Abschreibung',
                             `RSverfuegbarvon` date DEFAULT NULL COMMENT 'Datum verfügbar von',
                             `RSverfuegbarbis` date DEFAULT NULL COMMENT 'Datum verfügbar bis',
                             `RSpreisprostunde` decimal(9,2) DEFAULT NULL COMMENT 'Preis pro Stunde',
                             `RSmietgrp` int(6) unsigned DEFAULT NULL COMMENT 'Mietgruppe/Kategorie',
                             `RSstatus` int(6) DEFAULT NULL COMMENT 'Status',
                             `RSbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                             `RScpu` char(15) DEFAULT NULL COMMENT 'PC CPU',
                             `RSghz` char(15) DEFAULT NULL COMMENT 'PC Taktfrequenz in GHz',
                             `RSram` char(15) DEFAULT NULL COMMENT 'PC RAM in MB',
                             `RSdisk1` char(10) DEFAULT NULL COMMENT 'PC Festplatte-1 in GB',
                             `RSdisk2` char(10) DEFAULT NULL COMMENT 'PC Festplatte-2 in GB',
                             `RSos` char(15) DEFAULT NULL COMMENT 'PC Betriebssystem',
                             `RSsonstiges` text DEFAULT NULL COMMENT 'PC sonstiges',
                             `RSFoto` varchar(60) DEFAULT NULL COMMENT 'Foto',
                             `RSloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                             `RSaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungs-Datum',
                             `RSaeuser` char(35) DEFAULT NULL,
                             `RSerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungs-Datum',
                             `RSeruser` char(35) DEFAULT NULL,
                             PRIMARY KEY (`RSresid`)
) ENGINE=InnoDB AUTO_INCREMENT=8013 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SACHKONTO`
--

DROP TABLE IF EXISTS `SACHKONTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SACHKONTO` (
                             `SK_id` int(6) NOT NULL AUTO_INCREMENT,
                             `SK_nummer` varchar(5) NOT NULL,
                             `SK_bezeichnung` text DEFAULT NULL,
                             `KT_KYnr` int(5) NOT NULL,
                             PRIMARY KEY (`SK_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SACHKONTO_NUTZUNGSDAUER`
--

DROP TABLE IF EXISTS `SACHKONTO_NUTZUNGSDAUER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SACHKONTO_NUTZUNGSDAUER` (
                                           `SN_id` int(3) NOT NULL AUTO_INCREMENT,
                                           `SN_SACHKONTO_id` int(6) NOT NULL,
                                           `SN_monate` int(4) NOT NULL,
                                           PRIMARY KEY (`SN_id`),
                                           KEY `SN_SACHKONTO_id` (`SN_SACHKONTO_id`),
                                           CONSTRAINT `SACHKONTO_NUTZUNGSDAUER_ibfk_1` FOREIGN KEY (`SN_SACHKONTO_id`) REFERENCES `SACHKONTO` (`SK_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SECFKT`
--

DROP TABLE IF EXISTS `SECFKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SECFKT` (
                          `SFnr` int(3) unsigned NOT NULL AUTO_INCREMENT,
                          `SFbez` varchar(20) DEFAULT NULL,
                          `SFmrefnr` int(3) unsigned DEFAULT NULL,
                          `SFmorder` int(2) unsigned DEFAULT NULL,
                          `SFmlink` varchar(150) DEFAULT NULL,
                          `SFmicon` varchar(50) DEFAULT NULL,
                          PRIMARY KEY (`SFnr`)
) ENGINE=MyISAM AUTO_INCREMENT=943 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SECGRP`
--

DROP TABLE IF EXISTS `SECGRP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SECGRP` (
                          `SGnr` int(3) unsigned NOT NULL AUTO_INCREMENT COMMENT 'laufende Gruppennummer',
                          `SGbez` varchar(20) DEFAULT NULL COMMENT 'Gruppenbezeichnung',
                          `SGtype` enum('u','g') DEFAULT NULL COMMENT 'Gruppen- Type',
                          `SGloek` enum('n','y') DEFAULT 'n' COMMENT 'Loeschkennzeichen',
                          `SGaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                          `SGaeuser` char(35) DEFAULT NULL,
                          `SGerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                          `SGeruser` char(35) DEFAULT NULL,
                          PRIMARY KEY (`SGnr`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINAR`
--

DROP TABLE IF EXISTS `SEMINAR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINAR` (
                           `SMnr` int(6) NOT NULL AUTO_INCREMENT COMMENT 'Seminar-Nummer',
                           `SMvnr` varchar(20) DEFAULT NULL COMMENT '(e)AMS Veranstalutngsummer',
                           `SMalternMnr` varchar(20) DEFAULT NULL COMMENT 'alternative Mnr überschreibt PJMnr',
                           `PJnr` int(6) NOT NULL COMMENT 'Projekt-Nummer',
                           `SMbezeichnung1` char(128) DEFAULT NULL COMMENT 'Seminar-Bezeichnung 1',
                           `SMbezeichnung2` char(128) DEFAULT NULL COMMENT 'Seminar-Bezeichnung 2',
                           `SMtype` int(6) DEFAULT NULL COMMENT 'Seminar-Typ',
                           `mbbe_seminartyp` int(6) unsigned DEFAULT NULL COMMENT 'MBBE_SEMINARTYPEN.id',
                           `infotag` tinyint(1) DEFAULT NULL COMMENT 'Ist Seminar ein Infotag?',
                           `SMdatumVon` date DEFAULT NULL COMMENT 'Seminar Datum von',
                           `SMzeitVon` time DEFAULT NULL COMMENT 'Seminar Zeit von',
                           `SMdatumBis` date DEFAULT NULL COMMENT 'Seminar Datum bis',
                           `SMzeitBis` time DEFAULT NULL COMMENT 'Seminar Zeit bis',
                           `SMeinheiten` int(6) DEFAULT NULL COMMENT 'Dauer in Std.',
                           `SMseminarform` text DEFAULT NULL COMMENT 'Seminarform',
                           `SMbundesland` int(6) DEFAULT NULL COMMENT 'Bundesland',
                           `SMSOstandortid` int(6) DEFAULT NULL COMMENT 'Seminar-Ort',
                           `SMkosten` decimal(9,2) DEFAULT NULL COMMENT 'Seminar-Kosten',
                           `SMkostenbem` text DEFAULT NULL COMMENT 'Seminar-Kosten Bemerkung',
                           `SMabschluss` text DEFAULT NULL COMMENT 'Abschluss',
                           `SMkurszeiten` text DEFAULT NULL COMMENT 'Kurszeiten',
                           `SMauftraggeber` int(6) DEFAULT NULL COMMENT 'Auftraggeber',
                           `SMauftraggeber_old` int(6) DEFAULT NULL COMMENT 'Auftraggeber ALT',
                           `SMziel` text DEFAULT NULL COMMENT 'Seminar-Ziel',
                           `SMinhalte` text DEFAULT NULL COMMENT 'Inhalte',
                           `SMvoraussetzung` text DEFAULT NULL COMMENT 'Voraussetzungen für Teilnahme',
                           `SMbemerk` text DEFAULT NULL COMMENT 'Bemerkung',
                           `SMvermquote` int(3) DEFAULT NULL COMMENT 'Vermittlungsquote',
                           `SMnachbetreuungbis` date DEFAULT NULL COMMENT 'Nachbetreuung bis Datum',
                           `SMnachbesetzungbis` date DEFAULT NULL COMMENT 'Nachbesetzung bis Datum',
                           `SMpraktikumvon` date DEFAULT NULL COMMENT 'Praktikum von Datum',
                           `SMpraktikumbis` date DEFAULT NULL COMMENT 'Praktikum bis Datum',
                           `SMteilnehmeranzahl` int(4) DEFAULT NULL COMMENT 'Teilnehmer-Anzahl',
                           `SMtnpreis` decimal(9,2) DEFAULT NULL,
                           `SMtnsteuersatz` decimal(4,2) DEFAULT NULL,
                           `SMferienweihnachten` enum('n','y') DEFAULT NULL COMMENT 'Ferien Weihnachten',
                           `SMferiensemester` enum('n','y') DEFAULT NULL COMMENT 'Ferien Semester',
                           `SMferienostern` enum('n','y') DEFAULT NULL COMMENT 'Ferien Ostern',
                           `SMferiensommer` enum('n','y') DEFAULT NULL COMMENT 'Ferien Sommer',
                           `SMtntyp` enum('t','h') DEFAULT NULL,
                           `SMmassnahmennr` char(20) DEFAULT NULL COMMENT 'Seminarnummer der Länder',
                           `SMpublishvon` date DEFAULT NULL COMMENT 'Veröffentlichen von-Datum',
                           `SMpublishbis` date DEFAULT NULL COMMENT 'Veröffentlichen bis-Datum',
                           `SMpublishtxt` text DEFAULT NULL,
                           `SMstatus` int(6) DEFAULT NULL COMMENT 'Veranstaltungs-Status',
                           `SMdefaulttrainertyp` int(6) unsigned DEFAULT NULL COMMENT 'SM_AD_TRAINERTYP.id, Defaulteinstellung für SM_AD.SMADtrainertyp',
                           `SMSourceBelegungen` enum('seminar','workshop') DEFAULT 'seminar' COMMENT 'Quelle, aus der Belegungen generiert werden sollen',
                           `SMmasterKursKey` varchar(256) DEFAULT NULL,
                           `SMbBId` varchar(100) DEFAULT NULL,
                           `SMbBaltName` varchar(333) DEFAULT NULL,
                           `SMbBaktiv` char(1) DEFAULT 'N',
                           `SMbBcreate` char(1) DEFAULT NULL,
                           `SMvorlageKey` varchar(256) DEFAULT NULL,
                           `SMteamsaltName` varchar(333) DEFAULT NULL,
                           `SMteamsaktiv` char(1) DEFAULT 'N',
                           `SMteamsId` varchar(100) DEFAULT NULL,
                           `SMteamscode` varchar(100) DEFAULT NULL,
                           `SMloek` enum('n','y') DEFAULT 'n' COMMENT 'Loeschkennzeichen',
                           `SMaeda` datetime DEFAULT NULL COMMENT 'Aenderungsdatum',
                           `SMaeuser` char(35) DEFAULT NULL,
                           `SMerda` datetime DEFAULT NULL COMMENT 'Erstellungsdatum',
                           `SMeruser` char(35) DEFAULT NULL,
                           PRIMARY KEY (`SMnr`),
                           KEY `SEMINAR_FKIndex1` (`PJnr`)
) ENGINE=InnoDB AUTO_INCREMENT=47571 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINARBUCH`
--

DROP TABLE IF EXISTS `SEMINARBUCH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINARBUCH` (
                               `SBnr` int(6) NOT NULL AUTO_INCREMENT COMMENT 'lfd. Nr.',
                               `ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                               `SMnr` int(6) NOT NULL COMMENT 'Seminar-Nummer',
                               `SBdatum` date DEFAULT NULL COMMENT 'Datum',
                               `SBvon` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Uhrzeit von (Optional)',
                               `SBbis` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Uhrzeit bis (Optional)',
                               `SBeinheiten` decimal(4,2) DEFAULT NULL COMMENT 'Einheiten',
                               `SBlehrstoff` text DEFAULT NULL COMMENT 'Lehrstoff',
                               `SBbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                               `SBstatus` int(1) DEFAULT NULL COMMENT 'Status',
                               `SBloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                               `SBaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                               `SBaeuser` char(35) DEFAULT NULL,
                               `SBerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                               `SBeruser` char(35) DEFAULT NULL,
                               PRIMARY KEY (`SBnr`),
                               KEY `SEMINARBUCH_FKIndex1` (`SMnr`),
                               KEY `SEMINARBUCH_FKIndex2` (`ADadnr`)
) ENGINE=InnoDB AUTO_INCREMENT=685146 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINARBUCH_TRAINER`
--

DROP TABLE IF EXISTS `SEMINARBUCH_TRAINER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINARBUCH_TRAINER` (
                                       `seminarbuch_id` int(6) unsigned NOT NULL,
                                       `trainer_id` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                                       `bezeichnung` varchar(50) DEFAULT NULL,
                                       `teilnehmerstunden` decimal(4,2) DEFAULT 0.00,
                                       `ecstunden` decimal(4,2) DEFAULT 0.00,
                                       `opstunden` decimal(4,2) DEFAULT 0.00,
                                       `spstunden` decimal(4,2) DEFAULT 0.00,
                                       `grstunden` decimal(4,2) DEFAULT 0.00,
                                       `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `eruser` char(35) DEFAULT NULL,
                                       `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `aeuser` char(35) DEFAULT NULL,
                                       PRIMARY KEY (`seminarbuch_id`,`trainer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINARDOKUMENTE_VORLAGEN`
--

DROP TABLE IF EXISTS `SEMINARDOKUMENTE_VORLAGEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINARDOKUMENTE_VORLAGEN` (
                                             `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                             `bezeichnung` char(100) DEFAULT NULL,
                                             `kstgr` int(3) unsigned DEFAULT NULL,
                                             `PROJEKTTYPEN_id` int(6) unsigned DEFAULT NULL,
                                             `typ` int(6) unsigned DEFAULT NULL,
                                             `filename` char(100) DEFAULT NULL,
                                             `systorenr` int(9) unsigned DEFAULT NULL,
                                             `loek` enum('n','y') DEFAULT 'n',
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINARDOKUMENTE_VORLAGEN_TYP`
--

DROP TABLE IF EXISTS `SEMINARDOKUMENTE_VORLAGEN_TYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINARDOKUMENTE_VORLAGEN_TYP` (
                                                 `id` int(6) unsigned NOT NULL,
                                                 `bezeichnung` char(100) DEFAULT NULL,
                                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINARTOOL`
--

DROP TABLE IF EXISTS `SEMINARTOOL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINARTOOL` (
                               `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                               `bezeichnung` char(100) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINARTOOL_SEMINAR`
--

DROP TABLE IF EXISTS `SEMINARTOOL_SEMINAR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINARTOOL_SEMINAR` (
                                       `SEMINAR_id` int(6) unsigned NOT NULL DEFAULT 0,
                                       `SEMINARTOOL_id` int(6) unsigned NOT NULL DEFAULT 0,
                                       `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `aeuser` char(35) DEFAULT NULL,
                                       `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `eruser` char(35) DEFAULT NULL,
                                       PRIMARY KEY (`SEMINAR_id`,`SEMINARTOOL_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINARZIELE`
--

DROP TABLE IF EXISTS `SEMINARZIELE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINARZIELE` (
                                `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                `bezeichnung` char(100) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINAR_KONTROLLE`
--

DROP TABLE IF EXISTS `SEMINAR_KONTROLLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINAR_KONTROLLE` (
                                     `SMKLnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                     `SEMINAR_SMnr` int(6) NOT NULL,
                                     `SMKLtyp` char(2) DEFAULT NULL,
                                     `SMKLdatum` date DEFAULT NULL,
                                     `SMKLzeit` time /* mariadb-5.3 */ DEFAULT NULL,
                                     `BENUTZER_BNuserid` char(35) DEFAULT NULL,
                                     `SMKLbemerkung` text DEFAULT NULL,
                                     `SMKLaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     `SMKLaeuser` char(35) DEFAULT NULL,
                                     `SMKLerda` date DEFAULT NULL,
                                     `SMKLeruser` char(35) DEFAULT NULL,
                                     PRIMARY KEY (`SMKLnr`,`SEMINAR_SMnr`),
                                     KEY `Table_58_FKIndex1` (`SEMINAR_SMnr`)
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINAR_LEHRJAHR`
--

DROP TABLE IF EXISTS `SEMINAR_LEHRJAHR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINAR_LEHRJAHR` (
                                    `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                    `SeminarId` int(11) NOT NULL,
                                    `Lehrjahr` int(1) unsigned NOT NULL DEFAULT 1,
                                    `Von` date NOT NULL,
                                    `Bis` date NOT NULL,
                                    `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `AenderungsBenutzer` char(35) DEFAULT NULL,
                                    `ErstellungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                    `ErstellungsBenutzer` char(35) DEFAULT NULL,
                                    PRIMARY KEY (`Id`),
                                    UNIQUE KEY `SeminarId_Lehrjahr_UNIQUE` (`SeminarId`,`Lehrjahr`),
                                    CONSTRAINT `FK_Seminar_Lehrjahr_Seminar_SeminarId` FOREIGN KEY (`SeminarId`) REFERENCES `SEMINAR` (`SMnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2608 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINAR_MITBETREUUNG`
--

DROP TABLE IF EXISTS `SEMINAR_MITBETREUUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINAR_MITBETREUUNG` (
                                        `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                        `SeminarId` int(6) NOT NULL,
                                        `TrainerId` int(6) unsigned NOT NULL,
                                        `Datum` date NOT NULL,
                                        `Von` time /* mariadb-5.3 */ NOT NULL,
                                        `Bis` time /* mariadb-5.3 */ NOT NULL,
                                        `MitbetreuungArtId` int(11) unsigned DEFAULT NULL,
                                        `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                        `AenderungsBenutzer` char(35) DEFAULT NULL,
                                        `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                        `ErstellungsBenutzer` char(35) NOT NULL,
                                        PRIMARY KEY (`Id`),
                                        KEY `SEMINAR_MITBETREUUNG_SeminarId` (`SeminarId`),
                                        KEY `SEMINAR_MITBETREUUNG_TrainerId` (`TrainerId`),
                                        KEY `SEMINAR_MITBETREUUNG_ArtId` (`MitbetreuungArtId`),
                                        CONSTRAINT `FK_SEMINAR_MITBETREUUNG_ArtId` FOREIGN KEY (`MitbetreuungArtId`) REFERENCES `MITBETREUUNG_ART` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                        CONSTRAINT `FK_SEMINAR_MITBETREUUNG_SeminarId` FOREIGN KEY (`SeminarId`) REFERENCES `SEMINAR` (`SMnr`) ON DELETE CASCADE ON UPDATE CASCADE,
                                        CONSTRAINT `FK_SEMINAR_MITBETREUUNG_TrainerId` FOREIGN KEY (`TrainerId`) REFERENCES `ADRESSE` (`ADadnr`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43824 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINAR_MITBETREUUNG_TEILNEHMER`
--

DROP TABLE IF EXISTS `SEMINAR_MITBETREUUNG_TEILNEHMER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINAR_MITBETREUUNG_TEILNEHMER` (
                                                   `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                                   `SeminarMitbetreuungId` int(11) unsigned NOT NULL,
                                                   `SMTNSeminarId` int(6) NOT NULL,
                                                   `SMTNTeilnehmerId` int(6) unsigned NOT NULL,
                                                   `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                   `ErstellungsBenutzer` char(35) NOT NULL,
                                                   PRIMARY KEY (`Id`),
                                                   UNIQUE KEY `SEMINAR_MITBETREUUNG_UNIQUE_IDs` (`SeminarMitbetreuungId`,`SMTNSeminarId`,`SMTNTeilnehmerId`),
                                                   KEY `SEMINAR_MITBETREUUNG_TEILNEHMER_SeminarMitbetreuungId` (`SeminarMitbetreuungId`),
                                                   KEY `SEMINAR_MITBETREUUNG_SMTNSeminarID_SMTNTeilnehmerId` (`SMTNSeminarId`,`SMTNTeilnehmerId`),
                                                   CONSTRAINT `FK_SEMINAR_MITBETREUUNG_SMTNSeminarID_SMTNTeilnehmerId` FOREIGN KEY (`SMTNSeminarId`, `SMTNTeilnehmerId`) REFERENCES `SM_TN` (`SEMINAR_SMnr`, `ADRESSE_ADadnr`) ON DELETE CASCADE ON UPDATE CASCADE,
                                                   CONSTRAINT `FK_SEMINAR_MITBETREUUNG_TEILNEHMER_SeminarMitbetreuungId` FOREIGN KEY (`SeminarMitbetreuungId`) REFERENCES `SEMINAR_MITBETREUUNG` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=232475 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEMINAR__PROJEKT_SEMINARKATEGORIE`
--

DROP TABLE IF EXISTS `SEMINAR__PROJEKT_SEMINARKATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEMINAR__PROJEKT_SEMINARKATEGORIE` (
                                                     `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                                     `SeminarId` int(6) NOT NULL,
                                                     `ProjektSeminarkategorieId` int(11) unsigned NOT NULL,
                                                     `ErstellungsBenutzer` char(35) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                                     `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                     PRIMARY KEY (`Id`),
                                                     UNIQUE KEY `SM__PJ_SMKATEGORIE_SeminarId_ProjektSeminarkategorieId` (`SeminarId`,`ProjektSeminarkategorieId`),
                                                     KEY `ProjektSeminarkategorieId` (`ProjektSeminarkategorieId`),
                                                     CONSTRAINT `SEMINAR__PROJEKT_SEMINARKATEGORIE_ibfk_1` FOREIGN KEY (`SeminarId`) REFERENCES `SEMINAR` (`SMnr`) ON DELETE CASCADE ON UPDATE CASCADE,
                                                     CONSTRAINT `SEMINAR__PROJEKT_SEMINARKATEGORIE_ibfk_2` FOREIGN KEY (`ProjektSeminarkategorieId`) REFERENCES `PROJEKT_SEMINARKATEGORIE` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1191 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SMKATEGORIE`
--

DROP TABLE IF EXISTS `SMKATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SMKATEGORIE` (
                               `SKnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                               `SOKnr` int(6) unsigned NOT NULL COMMENT 'Oberkategorie',
                               `SKbezeichnung` varchar(100) NOT NULL,
                               `SKhomepageId` int(3) unsigned NOT NULL,
                               PRIMARY KEY (`SKnr`),
                               UNIQUE KEY `SKhomepageId` (`SKhomepageId`),
                               KEY `fk_SMKATEGORIE_SOKnr_SMOBERKATEGORIE` (`SOKnr`),
                               CONSTRAINT `fk_SMKATEGORIE_SOKnr_SMOBERKATEGORIE` FOREIGN KEY (`SOKnr`) REFERENCES `SMOBERKATEGORIE` (`SOKnr`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Seminarkategorien für Ausschreibungen (Seminar auf Homepage)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SMOBERKATEGORIE`
--

DROP TABLE IF EXISTS `SMOBERKATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SMOBERKATEGORIE` (
                                   `SOKnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                   `SOKbezeichnung` varchar(100) NOT NULL,
                                   `SOKhomepageId` int(3) unsigned NOT NULL,
                                   PRIMARY KEY (`SOKnr`),
                                   UNIQUE KEY `SOKhomepageId` (`SOKhomepageId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Seminaroberkategorien für Ausschreibungen (Seminar auf Homep';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SMTN_PRAKT`
--

DROP TABLE IF EXISTS `SMTN_PRAKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SMTN_PRAKT` (
                              `id` int(6) unsigned DEFAULT NULL,
                              `SMTN_ADRESSE_adnr` int(6) unsigned DEFAULT NULL,
                              `SMTN_SEMINAR_smnr` int(6) unsigned DEFAULT NULL,
                              `CRM_FIRMA_id` int(6) unsigned DEFAULT NULL,
                              `CRM_BERUF_id` int(6) unsigned DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SMTN_UNTERBRECHUNG`
--

DROP TABLE IF EXISTS `SMTN_UNTERBRECHUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SMTN_UNTERBRECHUNG` (
                                      `TAUBnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                      `SMTN_SMnr` int(6) unsigned NOT NULL,
                                      `SMTN_ADadnr` int(6) unsigned NOT NULL,
                                      `TAUBdatumVon` date NOT NULL,
                                      `TAUBdatumBis` date NOT NULL,
                                      `TAUBabschlussgrundKYnr` int(6) NOT NULL COMMENT 'Begründung, warum Seminarbesuch zuvor abgebrochen/Abgeschlossen wurde',
                                      `TAUBWGnr` int(6) unsigned NOT NULL COMMENT 'WIEDEREINSTIEGSGRUND',
                                      `TAUBbemerkung` text DEFAULT NULL,
                                      PRIMARY KEY (`TAUBnr`),
                                      UNIQUE KEY `SMTN_SMnr` (`SMTN_SMnr`,`SMTN_ADadnr`,`TAUBdatumVon`,`TAUBdatumBis`),
                                      KEY `TAUBWGnr` (`TAUBWGnr`),
                                      CONSTRAINT `fk_smtn_unterbrechung_wiedereinstiegsgrund` FOREIGN KEY (`TAUBWGnr`) REFERENCES `WIEDEREINSTIEGSGRUND` (`WGnr`)
) ENGINE=InnoDB AUTO_INCREMENT=1438 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Seminarunterbrechung eines Teilnehmers mit Begründung für de';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_AD`
--

DROP TABLE IF EXISTS `SM_AD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_AD` (
                         `SMADnr` int(6) NOT NULL AUTO_INCREMENT COMMENT 'laufende Nummer',
                         `SEMINAR_SMnr` int(6) NOT NULL COMMENT 'Seminar-Nummer',
                         `ADRESSE_ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                         `DIENSTVERTRAG_DVnr` int(6) NOT NULL DEFAULT 0 COMMENT 'Dienstvertrags-Nummer',
                         `SMADdatumvon` date DEFAULT NULL COMMENT 'Datum von',
                         `SMADdatumbis` date DEFAULT NULL COMMENT 'Datum bis',
                         `SMADersetzt` int(6) DEFAULT NULL COMMENT 'ersetzt Trainer',
                         `SMADbezeichnung` char(80) DEFAULT NULL COMMENT 'Bezeichnung Unterrichtsgegenstand',
                         `SMADinhalte` text DEFAULT NULL COMMENT 'Inhalte',
                         `SMADtaetigkeit` char(40) DEFAULT NULL COMMENT 'Art der Tätigkeit',
                         `SMADtyp` int(6) DEFAULT NULL COMMENT 'Typ',
                         `SMADfunktion` int(6) DEFAULT NULL COMMENT 'Funktion',
                         `SMADbewertung` int(2) DEFAULT NULL,
                         `SMADeinheiten` int(6) DEFAULT NULL COMMENT 'Einheiten in Stunden',
                         `SMADeinheitenbem` char(40) DEFAULT NULL COMMENT 'Einheiten-Bemerkung',
                         `SMADstundensatz` decimal(9,2) DEFAULT NULL COMMENT 'Kalkulations-Stundensatz',
                         `SMADref` int(6) unsigned DEFAULT NULL,
                         `SMADbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                         `SMADanfdatum` datetime DEFAULT NULL COMMENT 'Anforderungsdatum',
                         `SMADanfuser` char(15) DEFAULT NULL COMMENT 'Anforderungsbenutzer',
                         `SMADzugdatum` datetime DEFAULT NULL COMMENT 'Zuweisungs-Datum',
                         `SMADzuguser` char(15) DEFAULT NULL COMMENT 'Zuweisungs-Benutzer',
                         `SMADstatus` int(6) DEFAULT NULL COMMENT 'Status',
                         `SMADseminarbuch` tinyint(1) unsigned DEFAULT 1 COMMENT 'Führt ein Seminarbuch',
                         `SMADtrainertyp` int(6) unsigned DEFAULT NULL COMMENT 'SM_AD_TRAINERTYP.id',
                         `SM_ADpktErf` varchar(2) DEFAULT '-' COMMENT 'AMS Trainer Erfahrungspunkte',
                         `SM_ADpktqual` varchar(2) DEFAULT '-' COMMENT 'AMS Trainer Qualitätspunkte',
                         `SMADbBcreate` char(1) DEFAULT NULL,
                         `SMADTeamscreate` char(1) DEFAULT NULL,
                         `SMADaeda` datetime DEFAULT NULL COMMENT 'Änderungsdatum',
                         `SMADaeuser` char(35) DEFAULT NULL,
                         `SMADerda` datetime DEFAULT NULL COMMENT 'Erstellungsdatum',
                         `SMADeruser` char(35) DEFAULT NULL,
                         `SMADcckontakt` enum('e','y','n') NOT NULL DEFAULT 'n',
                         PRIMARY KEY (`SMADnr`,`SEMINAR_SMnr`),
                         KEY `FA_AD_FKIndex2` (`ADRESSE_ADadnr`),
                         KEY `SEMINARVERTRAG_FKIndex2` (`SEMINAR_SMnr`),
                         KEY `SMAD_DVnr` (`DIENSTVERTRAG_DVnr`)
) ENGINE=InnoDB AUTO_INCREMENT=246001 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_AD_CCNOTIZ`
--

DROP TABLE IF EXISTS `SM_AD_CCNOTIZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_AD_CCNOTIZ` (
                                 `CCnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                 `SM_AD_SEMINAR_SMnr` int(6) NOT NULL,
                                 `SM_AD_SMADnr` int(6) NOT NULL,
                                 `ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                                 `CCemail1` char(80) DEFAULT NULL,
                                 `CCemail2` char(80) DEFAULT NULL,
                                 `CCtitel` char(80) DEFAULT NULL,
                                 `CCbemerkung` text DEFAULT NULL,
                                 `CCsachbearbeiter` char(35) DEFAULT NULL,
                                 `CCaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `CCaeuser` char(35) DEFAULT NULL,
                                 `CCerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `CCeruser` char(35) DEFAULT NULL,
                                 PRIMARY KEY (`CCnr`,`SM_AD_SEMINAR_SMnr`,`SM_AD_SMADnr`),
                                 KEY `SM_AD_CCNOTIZ_FKIndex1` (`SM_AD_SMADnr`,`SM_AD_SEMINAR_SMnr`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_AD_TRAINERTYP`
--

DROP TABLE IF EXISTS `SM_AD_TRAINERTYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_AD_TRAINERTYP` (
                                    `id` int(6) unsigned NOT NULL,
                                    `bezeichnung` char(100) DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_BEZAE`
--

DROP TABLE IF EXISTS `SM_BEZAE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_BEZAE` (
                            `SEMINAR_SMnr` int(6) NOT NULL,
                            `SMBZjahr` int(4) unsigned NOT NULL,
                            `SMBZkw` int(2) unsigned NOT NULL,
                            `ADRESSE_ADadnr` int(6) unsigned DEFAULT NULL,
                            `SMBZbemerkung` text DEFAULT NULL,
                            `SMBZaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `SMBZaeuser` char(35) DEFAULT NULL,
                            `SMBZerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `SMBZeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`SEMINAR_SMnr`,`SMBZjahr`,`SMBZkw`),
                            KEY `SM_BEZUGSAENDERUNG_FKIndex1` (`SEMINAR_SMnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_BEZAE_TN`
--

DROP TABLE IF EXISTS `SM_BEZAE_TN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_BEZAE_TN` (
                               `SM_BEZAE_SEMINAR_SMnr` int(6) NOT NULL,
                               `SM_BEZAE_SMBZjahr` int(4) unsigned NOT NULL,
                               `SM_BEZAE_SMBZkw` int(2) unsigned NOT NULL,
                               `ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                               `TNBZcode` char(10) DEFAULT NULL,
                               `TNBZ_KSTTNstatus` int(6) DEFAULT NULL,
                               `TNBZawvon` date DEFAULT NULL,
                               `TNBZawbis` date DEFAULT NULL,
                               `TNBZkstbestvon` date DEFAULT NULL,
                               `TNBZkstbestbis` date DEFAULT NULL,
                               `TNBZbemerkung` text DEFAULT NULL,
                               `TNBZaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `TNBZaeuser` char(35) DEFAULT NULL,
                               `TNBZerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `TNBZeruser` char(35) DEFAULT NULL,
                               PRIMARY KEY (`SM_BEZAE_SEMINAR_SMnr`,`SM_BEZAE_SMBZjahr`,`SM_BEZAE_SMBZkw`,`ADRESSE_ADadnr`),
                               KEY `SM_TN_BEZUGSAENDERUNG_FKIndex1` (`SM_BEZAE_SMBZjahr`,`SM_BEZAE_SMBZkw`,`SM_BEZAE_SEMINAR_SMnr`),
                               KEY `SM_BEZAE_TN_FKIndex2` (`ADRESSE_ADadnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_BF`
--

DROP TABLE IF EXISTS `SM_BF`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_BF` (
                         `SEMINAR_SMnr` int(6) NOT NULL COMMENT 'Seminar-Nummer',
                         `uBEFRAGUNG_BEid` int(10) unsigned NOT NULL COMMENT 'Befragungs-Nummer',
                         `SMBFtermin` date DEFAULT NULL COMMENT 'Datum',
                         `SMBFleiterADadnr` int(6) DEFAULT NULL,
                         `SMBFleiterSMADnr` int(6) unsigned DEFAULT NULL,
                         `SMBFaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungs-Datum',
                         `SMBFaeuser` char(35) DEFAULT NULL,
                         `SMBFerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungs-Datum',
                         `SMBFeruser` char(35) DEFAULT NULL,
                         PRIMARY KEY (`SEMINAR_SMnr`,`uBEFRAGUNG_BEid`),
                         KEY `BEid` (`uBEFRAGUNG_BEid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_FEIERTAG`
--

DROP TABLE IF EXISTS `SM_FEIERTAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_FEIERTAG` (
                               `SEMINAR_SMnr` int(6) NOT NULL AUTO_INCREMENT,
                               `SMFTdatumvon` date NOT NULL,
                               `SMFTdatumbis` date NOT NULL,
                               `SMFTbezeichnung` char(30) DEFAULT NULL,
                               `SMFTaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `SMFTaeuser` char(35) DEFAULT NULL,
                               `SMFTerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `SMFTeruser` char(35) DEFAULT NULL,
                               PRIMARY KEY (`SEMINAR_SMnr`,`SMFTdatumvon`,`SMFTdatumbis`),
                               KEY `SM_FEIERTAG_FKIndex1` (`SEMINAR_SMnr`)
) ENGINE=InnoDB AUTO_INCREMENT=47557 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_KTR_CHK`
--

DROP TABLE IF EXISTS `SM_KTR_CHK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_KTR_CHK` (
                              `SMKC_SMKLnr` int(6) unsigned NOT NULL,
                              `SMKC_ADnr` int(6) unsigned NOT NULL,
                              `SMKC_SMnr` int(6) NOT NULL,
                              PRIMARY KEY (`SMKC_SMKLnr`,`SMKC_ADnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_RA`
--

DROP TABLE IF EXISTS `SM_RA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_RA` (
                         `SEMINAR_SMnr` int(6) NOT NULL COMMENT 'Seminar',
                         `RAUM_SOstandortid` int(6) NOT NULL COMMENT 'Standort-Nummer',
                         `RAUM_RAraumid` int(6) NOT NULL COMMENT 'Raum-Nummer',
                         `SMRAvon` date DEFAULT NULL COMMENT 'Datum von',
                         `SMRAvont` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Zeit von',
                         `SMRAbis` date DEFAULT NULL COMMENT 'Datum bis',
                         `SMRAbist` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Zeit bis',
                         `SMRAeinheiten` int(6) DEFAULT NULL COMMENT 'Einheiten',
                         `SMRArefraumid` int(6) DEFAULT NULL COMMENT 'Referenz auf Raum (Nachreichungen)',
                         `SMRArefstandortid` int(10) unsigned DEFAULT NULL COMMENT 'Referenz auf Standort(Nachreichungen)',
                         `SMRAstatus` int(10) unsigned DEFAULT NULL COMMENT 'Status siehe SVstatus (Einreichung, Nachbesetzung, Aufstockung)',
                         `SMRAaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                         `SMRAaeuser` char(35) DEFAULT NULL,
                         `SMRAerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                         `SMRAeruser` char(35) DEFAULT NULL,
                         PRIMARY KEY (`SEMINAR_SMnr`,`RAUM_SOstandortid`,`RAUM_RAraumid`),
                         KEY `SEMINAR_has_RAUM_FKIndex1` (`SEMINAR_SMnr`),
                         KEY `SM_RA_FKIndex2` (`RAUM_RAraumid`,`RAUM_SOstandortid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_TN`
--

DROP TABLE IF EXISTS `SM_TN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_TN` (
                         `SEMINAR_SMnr` int(6) NOT NULL COMMENT 'Seminar-Nummer',
                         `ADRESSE_ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                         `TAanmeldedatum` date DEFAULT NULL COMMENT 'Anmelde-Datum',
                         `TAamsbetreuer` char(40) DEFAULT NULL COMMENT 'AMS-Betreuer',
                         `TArgsnr` int(6) DEFAULT NULL,
                         `TARgsKYnr` int(6) DEFAULT NULL COMMENT 'KEYTABLE where KYname = RGS',
                         `TAteilnahmevon` date DEFAULT NULL COMMENT 'Teilnahme von',
                         `TAteilnahmebis` date DEFAULT NULL COMMENT 'Teilnahme bis',
                         `TAvermittlungsart` int(6) DEFAULT NULL COMMENT 'Vermittlungsart (Arbeitsaufnahme, Zusage, Qualifizierung, sonstiges)',
                         `TAvermittlung` int(6) DEFAULT NULL COMMENT 'Arbeitsaufnahme (KT Dienstverhältnis, Lehrstelle)',
                         `TAdatumbeginn` date DEFAULT NULL COMMENT 'Beginn-Datum Vermittlung',
                         `TAdatumende` date DEFAULT NULL COMMENT 'Ende-Datum Vermittlung',
                         `TAaa_bei` char(80) DEFAULT NULL COMMENT 'Arbeitsaufnahme bei Firma/Institut',
                         `TAaa_als` char(80) DEFAULT NULL COMMENT 'Arbeitsaufnahme als',
                         `TAqual_ort` char(40) DEFAULT NULL COMMENT 'Qualifizierung Ort',
                         `TAqual_kursbez` char(80) DEFAULT NULL COMMENT 'Qualifizierung Kursbezeichnung',
                         `TAsonstiges` int(6) DEFAULT NULL COMMENT 'Sonstiger Abschluss (KT Pension, Karenz, Krankheit, sonstiges)',
                         `TApruefung` char(80) DEFAULT NULL COMMENT 'Prüfungsbezeichnung',
                         `TApruefungerg` enum('b','t','n') DEFAULT NULL COMMENT 'Prüfungsergebnis (bestanden, tlw. bestanden, nicht bestanden)',
                         `TApruefungwdh` date DEFAULT NULL COMMENT 'Prüfungswiederholung am (Datum)',
                         `TAkarriereplan` enum('n','y') DEFAULT NULL COMMENT 'Karriereplan vorhanden',
                         `TAabschluss` int(6) DEFAULT NULL COMMENT 'Abbruch',
                         `TAabschlussdatum` date DEFAULT NULL COMMENT 'Abbruch Datum',
                         `TAabschlussBeiCrmFirma` int(6) unsigned DEFAULT NULL COMMENT 'Wenn Abschluss Dienstverhaeltnis, wird hier die CRM_FIRMA.id angegeben',
                         `TAabschlussgrund` text DEFAULT NULL COMMENT 'Abbruch Begründung',
                         `TAinternebemerk` text DEFAULT NULL,
                         `TAarbeitsplatzsucheseit` char(7) DEFAULT NULL,
                         `TAletztearbeitsstelle` char(40) DEFAULT NULL,
                         `TAletztearbeitsstelleals` char(40) DEFAULT NULL,
                         `TAletztearbeitsstellevon` char(7) DEFAULT NULL,
                         `TAletztearbeitsstellebis` char(7) DEFAULT NULL,
                         `TAalternativeMNummer` varchar(50) DEFAULT NULL COMMENT 'Einzutragen, wenn M Nummer auf PJ-Ebene nicht stimmt',
                         `TAalternativeVNummer` varchar(50) DEFAULT NULL COMMENT 'Einzutragen, wenn V Nummer auf SM-Ebene nicht stimmt',
                         `TAtn_zufriedenheit_ausgefuellt` tinyint(1) DEFAULT NULL,
                         `TAmbbe_betreuung` int(6) unsigned DEFAULT NULL COMMENT 'MBBE_BETREUUNG.id',
                         `TAmbbe_leistungsbezug` int(6) unsigned DEFAULT NULL COMMENT 'MBBE_LEISTUNGSBEZUG.id',
                         `TAmbbe_uebertritt` date DEFAULT NULL,
                         `TAjobsuche_aktiv` tinyint(1) DEFAULT NULL COMMENT 'Für Jobsuche aktiv',
                         `TAteilnehmerkategorie_id` int(6) unsigned DEFAULT NULL,
                         `TAbBaktiv` char(1) DEFAULT 'N',
                         `TAbBcreate` char(1) DEFAULT NULL,
                         `TAteamsaktiv` char(1) DEFAULT 'N',
                         `TAteamscreate` char(1) DEFAULT NULL,
                         `TAaeda` date DEFAULT NULL COMMENT 'Änderungsdatum',
                         `TAaeuser` char(35) DEFAULT NULL,
                         `TAerda` date DEFAULT NULL COMMENT 'Erfassungsdatum',
                         `TAeruser` char(35) DEFAULT NULL,
                         PRIMARY KEY (`SEMINAR_SMnr`,`ADRESSE_ADadnr`),
                         KEY `SM_TN_ADadnr` (`ADRESSE_ADadnr`),
                         KEY `SM_TN_TeilnahmeVon` (`TAteilnahmevon`),
                         KEY `SM_TN_TeilnahmeBis` (`TAteilnahmebis`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_TN_DOKUMENTE`
--

DROP TABLE IF EXISTS `SM_TN_DOKUMENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_TN_DOKUMENTE` (
                                   `sySTORE_STnr` int(9) unsigned NOT NULL,
                                   `ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                                   `SEMINAR_SMnr` int(6) unsigned NOT NULL,
                                   `ABTEILUNG_ABnr` int(3) unsigned NOT NULL,
                                   `DOK_ART_id` int(6) unsigned DEFAULT NULL,
                                   `DKloek` enum('n','y') NOT NULL DEFAULT 'n',
                                   PRIMARY KEY (`sySTORE_STnr`,`ADRESSE_ADadnr`,`SEMINAR_SMnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_TN_ERKRANKUNG`
--

DROP TABLE IF EXISTS `SM_TN_ERKRANKUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_TN_ERKRANKUNG` (
                                    `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                    `SEMINAR_SMnr` int(6) unsigned NOT NULL COMMENT 'SM_TN.SEMINAR_SMnr',
                                    `ADRESSE_ADadnr` int(6) unsigned NOT NULL COMMENT 'SM_TN.ADRESSE_ADadnr',
                                    `ERKRANKUNGSTYP_id` int(6) unsigned NOT NULL COMMENT 'ERKRANKUNGSTYP.id',
                                    `DIAGNOSETYP_id` int(6) unsigned NOT NULL COMMENT 'DIAGNOSETYP.id',
                                    `bezeichnung` varchar(50) NOT NULL,
                                    `bemerkung` text DEFAULT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Fuer SM_TNs in PJs des Typs fit2work';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_TN_NOTIZ`
--

DROP TABLE IF EXISTS `SM_TN_NOTIZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_TN_NOTIZ` (
                               `TNNZnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                               `SM_TN_ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                               `SM_TN_SEMINAR_SMnr` int(6) NOT NULL,
                               `TNNZtyp` char(5) DEFAULT NULL,
                               `TNNZdatum` date DEFAULT NULL,
                               `TNNZbezeichnung` char(128) DEFAULT NULL,
                               `TNNZtext` text DEFAULT NULL,
                               `ADRESSE_ADadnr` int(6) unsigned DEFAULT NULL,
                               `BNuserid` char(35) DEFAULT NULL,
                               `sySTORE_STnr` int(9) unsigned DEFAULT NULL,
                               `TNNZgesperrt` tinyint(1) DEFAULT 0 COMMENT 'true=gesperrt, false=offen',
                               `TNNZloek` enum('n','y') DEFAULT 'n',
                               `TNNZaeda` datetime DEFAULT NULL,
                               `TNNZaeuser` char(35) DEFAULT NULL,
                               `TNNZerda` datetime DEFAULT NULL,
                               `TNNZeruser` char(35) DEFAULT NULL,
                               PRIMARY KEY (`TNNZnr`,`SM_TN_ADRESSE_ADadnr`,`SM_TN_SEMINAR_SMnr`),
                               KEY `SM_TN_NOTIZ_FKIndex1` (`SM_TN_SEMINAR_SMnr`,`SM_TN_ADRESSE_ADadnr`)
) ENGINE=InnoDB AUTO_INCREMENT=957043 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_TN_PRAKT`
--

DROP TABLE IF EXISTS `SM_TN_PRAKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_TN_PRAKT` (
                               `TNPRnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                               `SM_TN_ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                               `SM_TN_SEMINAR_SMnr` int(6) NOT NULL,
                               `CRM_FIRMA_id` int(6) unsigned DEFAULT NULL COMMENT 'CRM_FIRMA.CFnr: Unternehmen, Filiale, Geschäftstelle, in der das Praktikum stattfindet',
                               `CRM_BERUF_id` int(6) unsigned DEFAULT NULL COMMENT 'CRM_BERUF.id: Die Branche, die dem Praktikum zugeordnet wird',
                               `typ` enum('p','v') NOT NULL DEFAULT 'p' COMMENT 'Praktikum oder Vermittlung',
                               `TNPRvon` date DEFAULT NULL,
                               `TNPRbis` date DEFAULT NULL,
                               `TNPRbei` char(80) DEFAULT NULL,
                               `TNPRals` char(40) DEFAULT NULL,
                               `TNPRbemerkung` text DEFAULT NULL,
                               `loek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                               `TNPRaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `TNPRaeuser` char(35) DEFAULT NULL,
                               `TNPRerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                               `TNPReruser` char(35) DEFAULT NULL,
                               PRIMARY KEY (`TNPRnr`,`SM_TN_ADRESSE_ADadnr`,`SM_TN_SEMINAR_SMnr`),
                               KEY `Table_62_FKIndex1` (`SM_TN_SEMINAR_SMnr`,`SM_TN_ADRESSE_ADadnr`)
) ENGINE=InnoDB AUTO_INCREMENT=52040 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_TN_PRAKT_BACKUP`
--

DROP TABLE IF EXISTS `SM_TN_PRAKT_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_TN_PRAKT_BACKUP` (
                                      `TNPRnr` int(6) unsigned NOT NULL DEFAULT 0,
                                      `SM_TN_ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                                      `SM_TN_SEMINAR_SMnr` int(6) NOT NULL,
                                      `TNPRvon` date DEFAULT NULL,
                                      `TNPRbis` date DEFAULT NULL,
                                      `TNPRbei` char(80) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                      `TNPRals` char(40) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                      `TNPRaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                      `TNPRaeuser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                      `TNPRerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                      `TNPReruser` char(35) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL,
                                      PRIMARY KEY (`TNPRnr`,`SM_TN_ADRESSE_ADadnr`,`SM_TN_SEMINAR_SMnr`),
                                      KEY `Table_62_FKIndex1` (`SM_TN_SEMINAR_SMnr`,`SM_TN_ADRESSE_ADadnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SM_WS`
--

DROP TABLE IF EXISTS `SM_WS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SM_WS` (
                         `SEMINAR_id` int(6) unsigned NOT NULL DEFAULT 0,
                         `WORKSHOP_id` int(6) unsigned NOT NULL DEFAULT 0,
                         PRIMARY KEY (`SEMINAR_id`,`WORKSHOP_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STAATEN`
--

DROP TABLE IF EXISTS `STAATEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STAATEN` (
                           `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                           `bmd_id` int(6) unsigned DEFAULT NULL COMMENT 'id im BMD',
                           `reihung` int(6) unsigned NOT NULL COMMENT 'Reihung des Eintrages in der Liste',
                           `bezeichnung` varchar(60) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Name laut BMD',
                           `f2w_bezeichnung` varchar(60) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Name fuer Fit2Work',
                           `alpha2` char(2) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Alpha-2 Code laut ISO-3166-1',
                           `alpha3` char(3) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Alpha-3 Code laut ISO-3166-1',
                           `kennziffer` int(4) unsigned DEFAULT NULL COMMENT 'Kennziffer innerhalb von ISO-3166-1',
                           `TLD` char(2) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT NULL COMMENT 'Top Level Domain',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `reihung_UNIQUE` (`reihung`),
                           UNIQUE KEY `bmd_id_UNIQUE` (`bmd_id`),
                           UNIQUE KEY `alpha2_UNIQUE` (`alpha2`),
                           UNIQUE KEY `alpha3_UNIQUE` (`alpha3`),
                           UNIQUE KEY `kennziffer_UNIQUE` (`kennziffer`),
                           UNIQUE KEY `TLD_UNIQUE` (`TLD`)
) ENGINE=MyISAM AUTO_INCREMENT=258 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Staaten aus BMD bereichert um Daten der ISO-3166';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STANDORT`
--

DROP TABLE IF EXISTS `STANDORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STANDORT` (
                            `SOstandortid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Standort-ID',
                            `SObezeichnung` char(60) DEFAULT NULL COMMENT 'Standort-Bezeichnung',
                            `SObezkurz` char(20) DEFAULT NULL COMMENT 'Standort-Kurzbezeichnung',
                            `SObundesland` int(10) unsigned DEFAULT NULL COMMENT 'Bundesland',
                            `SOstrasse` char(50) DEFAULT NULL COMMENT 'Strasse',
                            `SOlkz` char(6) DEFAULT NULL COMMENT 'Land',
                            `SOplz` char(6) DEFAULT NULL COMMENT 'PLZ',
                            `SOort` char(50) DEFAULT NULL COMMENT 'Ort',
                            `SOtelefon` char(40) DEFAULT NULL COMMENT 'Telefon',
                            `SOtelefax` char(40) DEFAULT NULL COMMENT 'Telefax',
                            `SOemail` char(80) DEFAULT NULL COMMENT 'EMail-Adresse',
                            `SOgs` int(6) DEFAULT NULL COMMENT 'Geschäftsstellen-Zuordnung',
                            `SOvermieternr` int(6) DEFAULT NULL COMMENT 'Vermieter',
                            `SOvermieternr_old` int(6) DEFAULT NULL COMMENT 'Vermieter alt',
                            `SOhverw` int(6) DEFAULT NULL COMMENT 'Hausverwaltung ALT',
                            `SOhverw_old` int(6) DEFAULT NULL COMMENT 'Hausverwaltung ALT',
                            `SOverantwort` int(6) unsigned DEFAULT NULL COMMENT 'Standortverantwortlicher',
                            `SObemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                            `SOkstgrp` int(3) DEFAULT NULL COMMENT 'Kostenstelle-Gruppe',
                            `SOkstnr` int(3) DEFAULT NULL COMMENT 'Kostenstelle-Nummer',
                            `SOkstsub` int(3) DEFAULT NULL COMMENT 'Kostenstelle-Sub',
                            `SOraumflaeche` int(6) unsigned DEFAULT NULL COMMENT 'Raumfläche',
                            `SOfinanzierung` enum('e','m','l','s') DEFAULT NULL COMMENT 'Finanzierung',
                            `SOmietemonat` decimal(9,2) DEFAULT NULL COMMENT 'Miete monatlich',
                            `SObetriebskosten` decimal(9,2) DEFAULT NULL COMMENT 'Betriebskosten monatlich',
                            `SOreinigung` decimal(9,2) DEFAULT NULL COMMENT 'Reinigungskosten monatlich',
                            `SOheizung` decimal(6,2) DEFAULT NULL COMMENT 'Heizungskosten',
                            `SOkostensonstige` decimal(9,2) DEFAULT NULL COMMENT 'sonstige Kosten monatlich',
                            `SOsonstbemerk` char(20) DEFAULT NULL COMMENT 'BEMERKUNG zu SOkostensonstige',
                            `SOverfuegbarvon` date DEFAULT NULL COMMENT 'verfügbar von',
                            `SOverfuegbarbis` date DEFAULT NULL COMMENT 'verfügbar bis',
                            `SOpreisprostunde` decimal(9,2) DEFAULT NULL COMMENT 'Preis pro Stunde',
                            `SOinternet` char(1) DEFAULT NULL COMMENT 'Internet',
                            `SOkatgs` enum('n','y') DEFAULT 'n' COMMENT 'Kategorie Geschäftsstelle',
                            `SOkatsmort` enum('n','y') DEFAULT 'n' COMMENT 'Kategorie Seminarstandort',
                            `SOpublishvon` date DEFAULT NULL COMMENT 'Veröffentlichen Datum-von',
                            `SOpublishbis` date DEFAULT NULL COMMENT 'Veröffentlichen Datum-bis',
                            `SOmakler` int(6) DEFAULT NULL COMMENT 'Makler',
                            `SOmaklerprovision` decimal(9,2) DEFAULT NULL COMMENT 'Makler-Provision',
                            `SOprovisionsdatum` date DEFAULT NULL COMMENT 'Provisionsdatum',
                            `SOkaution` decimal(9,2) DEFAULT NULL COMMENT 'Kaution',
                            `SOkautionbeguenstigter` int(6) DEFAULT NULL COMMENT 'Kaution Begünstigter',
                            `SOkautionsart` int(6) DEFAULT NULL COMMENT 'Kautionsart',
                            `SOkautionablaufdatum` date DEFAULT NULL COMMENT 'Kaution Ablaufdatum',
                            `SOstromanmeldestand` char(12) DEFAULT NULL COMMENT 'Strom-Anmeldung Zählerstand',
                            `SOstromanmeldedatum` date DEFAULT NULL COMMENT 'Strom-Anmeldung Datum',
                            `SOstromanmeldungdurch` char(20) DEFAULT NULL COMMENT 'Strom-Anmeldung durch',
                            `SOstromabmeldestand` char(12) DEFAULT NULL COMMENT 'Strom-Abmeldung Zählerstand',
                            `SOstromabmeldedatum` date DEFAULT NULL COMMENT 'Strom-Abmeldung Datum',
                            `SOstromabmeldungdurch` char(20) DEFAULT NULL COMMENT 'Strom-Abmeldung durch',
                            `SOgasanmeldestand` char(12) DEFAULT NULL COMMENT 'Gas/Fernwärme Anmeldung Zählerstand',
                            `SOgasanmeldedatum` date DEFAULT NULL COMMENT 'Gas/Fernwärme Anmeldung Datum',
                            `SOgasanmeldungdurch` char(20) DEFAULT NULL COMMENT 'Gas/Fernwärme Anmeldung durch',
                            `SOgasabmeldestand` char(12) DEFAULT NULL COMMENT 'Gas/Fernwärme Abmeldung Zählerstand',
                            `SOgasabmeldedatum` date DEFAULT NULL COMMENT 'Gas/Fernwärme Abmeldung Datum',
                            `SOgasabmeldungdurch` char(20) DEFAULT NULL COMMENT 'Gas/Fernwärme Abmeldung durch',
                            `SOtelefonanmeldedatum` date DEFAULT NULL COMMENT 'Telefon-Anmeldung Datum',
                            `SOtelefonanmeldungdurch` char(20) DEFAULT NULL COMMENT 'Telefon-Anmeldung durch',
                            `SOtelefonabmeldedatum` date DEFAULT NULL COMMENT 'Telefon-Abmeldung Datum',
                            `SOtelefonabmeldungdurch` char(20) DEFAULT NULL COMMENT 'Telefon-Abmeldung durch',
                            `SOinternetanmeldedatum` date DEFAULT NULL COMMENT 'Internet-Anmeldung Datum',
                            `SOinternetanmeldungdurch` char(20) DEFAULT NULL COMMENT 'Internet-Anmeldung durch',
                            `SOinternetabmeldedatum` date DEFAULT NULL COMMENT 'Internet-Abmeldung Datum',
                            `SOinternetabmeldungdurch` char(20) DEFAULT NULL COMMENT 'Internet-Abmeldung durch',
                            `SOstatus` int(6) DEFAULT NULL COMMENT 'Status',
                            `SOpopup` text DEFAULT NULL COMMENT 'Popupmeldung',
                            `SObildLink` text DEFAULT NULL COMMENT 'Link eines Bildes, das auf der Hompage angezeigt wird',
                            `SOloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                            `SOaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                            `SOaeuser` char(35) DEFAULT NULL,
                            `SOerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                            `SOeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`SOstandortid`)
) ENGINE=InnoDB AUTO_INCREMENT=529 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STANDORT_EXPORT`
--

DROP TABLE IF EXISTS `STANDORT_EXPORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STANDORT_EXPORT` (
                                   `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                   `standort_id` int(6) unsigned DEFAULT NULL,
                                   `ibis_firma_id` int(6) unsigned DEFAULT NULL,
                                   `adresszusatz` char(50) DEFAULT NULL,
                                   `telefon` char(40) DEFAULT NULL,
                                   `telefax` char(40) DEFAULT NULL,
                                   `email` char(80) DEFAULT NULL,
                                   `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `eruser` char(35) DEFAULT NULL,
                                   `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `aeuser` char(35) DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STANDORT_ZERTIFIZIERUNGSPRODUKTE`
--

DROP TABLE IF EXISTS `STANDORT_ZERTIFIZIERUNGSPRODUKTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STANDORT_ZERTIFIZIERUNGSPRODUKTE` (
                                                    `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                    `STANDORT_id` int(6) unsigned DEFAULT NULL,
                                                    `ZERTIFZIZIERUNGSPRODUKTE_ID` int(6) unsigned DEFAULT NULL,
                                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1201 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STARTCOACH`
--

DROP TABLE IF EXISTS `STARTCOACH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STARTCOACH` (
                              `SCid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID des STARTCOACHES',
                              `SC_ADadnr` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'ID aus ADRESSTABELLE',
                              `SC_GSid` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'ID aus Geschaeftsbereichkeytable',
                              `SCLoek` enum('n','y') DEFAULT 'n' COMMENT 'Loeschkennzeichen',
                              `SCerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungdatum',
                              `SCeruser` char(35) DEFAULT NULL COMMENT 'Erstellungsbenutzer',
                              `SCaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                              `SCaeuser` char(35) DEFAULT NULL COMMENT 'Aenderungsbenutzer',
                              PRIMARY KEY (`SCid`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STELLE`
--

DROP TABLE IF EXISTS `STELLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STELLE` (
                          `STnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                          `STname` char(128) DEFAULT NULL,
                          `STvon` date DEFAULT NULL,
                          `STbis` date DEFAULT NULL,
                          `STbeschreibung` text DEFAULT NULL,
                          `STland` int(6) DEFAULT NULL,
                          `STbundesland` int(6) unsigned DEFAULT NULL,
                          `STrefkstgr` int(3) DEFAULT NULL,
                          `STrefnr` char(5) DEFAULT NULL,
                          `STzustaendig` int(6) unsigned DEFAULT NULL,
                          `STbemerkung` text DEFAULT NULL,
                          `STpublishvon` date DEFAULT NULL,
                          `STpublishbis` date DEFAULT NULL,
                          `STmehrfach` char(1) DEFAULT NULL,
                          `STtyp` int(6) DEFAULT NULL,
                          `STstatus` int(6) unsigned DEFAULT NULL,
                          `STloek` enum('n','y') DEFAULT 'n',
                          `STaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                          `STaeuser` char(35) DEFAULT NULL,
                          `STerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                          `STeruser` char(35) DEFAULT NULL,
                          PRIMARY KEY (`STnr`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STELLENLISTE`
--

DROP TABLE IF EXISTS `STELLENLISTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STELLENLISTE` (
                                `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                `bezeichnung` char(255) DEFAULT NULL,
                                `firmenname` char(255) DEFAULT NULL,
                                `ort` char(255) DEFAULT NULL,
                                `plz` char(20) DEFAULT NULL,
                                `STELLENLISTE_BRANCHE_id` int(6) unsigned DEFAULT NULL,
                                `bundesland` int(3) unsigned DEFAULT NULL,
                                `kurbeschreibung` text DEFAULT NULL,
                                `anforderungen` text DEFAULT NULL,
                                `STELLENLISTE_STUNDENAUSMASS_id` int(6) unsigned DEFAULT NULL,
                                `dokument_stnr` int(9) unsigned DEFAULT NULL,
                                `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `eruser` char(35) DEFAULT NULL,
                                `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `aeuser` char(35) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8736 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STELLENLISTE_BRANCHE`
--

DROP TABLE IF EXISTS `STELLENLISTE_BRANCHE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STELLENLISTE_BRANCHE` (
                                        `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                        `bezeichnung` char(255) DEFAULT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STELLENLISTE_STATISTIK`
--

DROP TABLE IF EXISTS `STELLENLISTE_STATISTIK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STELLENLISTE_STATISTIK` (
                                          `id` int(6) unsigned NOT NULL,
                                          `archivierungsdatum` date DEFAULT NULL,
                                          `bezeichnung` char(255) DEFAULT NULL,
                                          `firmenname` char(255) DEFAULT NULL,
                                          `ort` char(255) DEFAULT NULL,
                                          `plz` char(20) DEFAULT NULL,
                                          `STELLENLISTE_BRANCHE_id` int(6) unsigned DEFAULT NULL,
                                          `bundesland` int(3) unsigned DEFAULT NULL,
                                          `STELLENLISTE_STUNDENAUSMASS_id` int(6) unsigned DEFAULT NULL,
                                          `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                          `eruser` char(35) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STELLENLISTE_STATISTIK_del`
--

DROP TABLE IF EXISTS `STELLENLISTE_STATISTIK_del`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STELLENLISTE_STATISTIK_del` (
                                              `id` int(6) unsigned NOT NULL,
                                              `archivierungsdatum` date DEFAULT NULL,
                                              `bezeichnung` char(255) DEFAULT NULL,
                                              `firmenname` char(255) DEFAULT NULL,
                                              `ort` char(255) DEFAULT NULL,
                                              `plz` char(20) DEFAULT NULL,
                                              `STELLENLISTE_BRANCHE_id` int(6) unsigned DEFAULT NULL,
                                              `bundesland` int(3) unsigned DEFAULT NULL,
                                              `STELLENLISTE_STUNDENAUSMASS_id` int(6) unsigned DEFAULT NULL,
                                              `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                              `eruser` char(35) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STELLENLISTE_STUNDENAUSMASS`
--

DROP TABLE IF EXISTS `STELLENLISTE_STUNDENAUSMASS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STELLENLISTE_STUNDENAUSMASS` (
                                               `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                               `bezeichnung` char(255) DEFAULT NULL,
                                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STELLE_BEWERBER`
--

DROP TABLE IF EXISTS `STELLE_BEWERBER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STELLE_BEWERBER` (
                                   `STBnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                   `STELLE_STnr` int(6) unsigned NOT NULL,
                                   `ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                                   `STBWdatum` date DEFAULT NULL,
                                   `STBWmatch` int(3) unsigned DEFAULT NULL,
                                   `STBWbemerkung` text DEFAULT NULL,
                                   `STBWbemerkung2` text DEFAULT NULL,
                                   `STBWbearbeitung` date DEFAULT NULL,
                                   `STBWstatus` int(6) unsigned DEFAULT NULL,
                                   `STBWaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `STBWaeuser` char(35) DEFAULT NULL,
                                   `STBWerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                   `STBWeruser` char(35) DEFAULT NULL,
                                   PRIMARY KEY (`STBnr`,`STELLE_STnr`),
                                   KEY `STELLE_BEWERBER_FKIndex1` (`STELLE_STnr`),
                                   KEY `STELLE_BEWERBER_FKIndex2` (`ADRESSE_ADadnr`)
) ENGINE=InnoDB AUTO_INCREMENT=994 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STELLE_MEDIUM`
--

DROP TABLE IF EXISTS `STELLE_MEDIUM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STELLE_MEDIUM` (
                                 `STMEnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                 `STELLE_STnr` int(6) unsigned NOT NULL,
                                 `STMEname` int(6) DEFAULT NULL COMMENT 'Medium',
                                 `STMEname_old` int(6) DEFAULT NULL COMMENT 'Medium ALT',
                                 `STMEvon` date DEFAULT NULL,
                                 `STMEbis` date DEFAULT NULL,
                                 `STMEpreis` decimal(8,2) DEFAULT NULL,
                                 `STMEbundesland` int(6) unsigned DEFAULT NULL,
                                 `STMErefme` char(5) DEFAULT NULL,
                                 `STMEbemerkung` text DEFAULT NULL,
                                 `STMEaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `STMEaeuser` char(35) DEFAULT NULL,
                                 `STMEerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `STMEeruser` char(35) DEFAULT NULL,
                                 PRIMARY KEY (`STMEnr`,`STELLE_STnr`),
                                 KEY `STELLE_MEDIUM_FKIndex1` (`STELLE_STnr`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SYNTHESIS_FIT2WORK_SMTN_SENT`
--

DROP TABLE IF EXISTS `SYNTHESIS_FIT2WORK_SMTN_SENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SYNTHESIS_FIT2WORK_SMTN_SENT` (
                                                `PERS_ID` int(6) unsigned NOT NULL COMMENT 'SM_TN.ADRESSE_ADadnr',
                                                `SEM_ID` int(6) unsigned NOT NULL COMMENT 'SM_TN.SEMINAR_SMnr',
                                                `PJ_ID` int(6) unsigned DEFAULT NULL COMMENT 'PROJEKT.PJnr',
                                                `BUNDESLAND_ID` int(6) unsigned DEFAULT NULL COMMENT 'PROJEKT.PJkstgr',
                                                `BUNDESLAND_BEZ` varchar(50) DEFAULT NULL COMMENT 'KSTbezeichnung',
                                                `TEILN_VON` date NOT NULL COMMENT 'SM_TN.TAtailnahmevon',
                                                `TEILN_BIS` date NOT NULL COMMENT 'SM_TN-TAteilnahmebis',
                                                `ABSCHLUSS_AM` date DEFAULT NULL COMMENT 'SM_TN.TAabschlussdatum',
                                                `ABSCHLUSS_ID` int(6) unsigned DEFAULT NULL COMMENT 'SM_TN.TAabschluss',
                                                `ABSCHLUSS_BEZ` varchar(100) DEFAULT NULL COMMENT 'KEYTABLE.TAabschluss',
                                                `FIRMA_ID` int(6) unsigned DEFAULT NULL COMMENT 'SM_TN.TAabschlussBeiCrmFirma',
                                                `FIRMA_HEROLD_ID` int(6) unsigned DEFAULT NULL COMMENT 'CRM_FIRMA.herold_firma_id',
                                                `FIRMA_BEZ` varchar(50) DEFAULT NULL COMMENT 'CRM_FIRMA.name',
                                                `ABSCHLUSS_BEMERKUNG` text DEFAULT NULL COMMENT 'SM_TN.TAabschlussgrund (Frimen Infos?!?)',
                                                `UNTERBRECHUNG` int(1) NOT NULL DEFAULT 0,
                                                `AKTUELLER_STATUS_ID` int(6) unsigned NOT NULL COMMENT 'FIT2WORK_SM_TN_ZUSATZ.aktuellerStatus',
                                                `AKTUELLER_STATUS_BEZ` varchar(50) DEFAULT NULL COMMENT 'FIT2WORK_AKTUELLER_STATUS.bezeichnung',
                                                `IST_BEHINDERT` tinyint(1) DEFAULT NULL COMMENT 'FIT2WORK_SM_TN_ZUSATZ.behinderung',
                                                `BEHINDERUNGSGRAD` tinyint(4) DEFAULT NULL COMMENT 'FIT2WORK_SM_TN_ZUSATZ.behinderungGrad',
                                                `ERGEBNIS_NACH_STATUS_ID` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_SM_TN_ZUSATZ.ergebnisNachStatus',
                                                `ERGEBNIS_NACH_STATUS_BEZ` varchar(50) DEFAULT NULL COMMENT 'FIT2WORK_ERGEBNIS_NACH_STATUS.bezeichnung',
                                                `EMPFEHLUNG_ID` int(6) unsigned DEFAULT NULL COMMENT 'FIT2WORK_SM_TN_ZUSATZ.empfehlung',
                                                `EMPFEHLUNG_BEZ` varchar(50) DEFAULT NULL COMMENT 'FIT2WORK_EMPFEHLUNG.bezeichnung',
                                                `GESENDET_AM` date NOT NULL COMMENT 'aktuelles Datum',
                                                PRIMARY KEY (`PERS_ID`,`SEM_ID`),
                                                KEY `GESENDET_AM` (`GESENDET_AM`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Export der SMTN-Daten für Synthesis (ibisRUN)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SYNTHESIS_SMAD_SENT`
--

DROP TABLE IF EXISTS `SYNTHESIS_SMAD_SENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SYNTHESIS_SMAD_SENT` (
                                       `ID` int(6) unsigned NOT NULL COMMENT 'SMADnr, weil Merhfachzuweisungen vorhanden',
                                       `TRAINER_ID` int(6) unsigned NOT NULL COMMENT 'SM_AD.ADRESSE.ADadnr',
                                       `SEM_ID` int(6) unsigned NOT NULL COMMENT 'SM_AD.SEMINAR_SMnr',
                                       `DATUM_VON` date DEFAULT NULL COMMENT 'SM_AD.SMADdatumvon',
                                       `DATUM_BIS` date DEFAULT NULL COMMENT 'SM_AD.SMADdatumbis',
                                       `FUNKTION_ID` int(10) unsigned DEFAULT NULL COMMENT 'SM_AD.SMADfunktion',
                                       `FUNKTION_BEZ` varchar(100) DEFAULT NULL COMMENT 'KEYTABLE.SVfunktion.KYvaluet1',
                                       `GESENDET_AM` date NOT NULL COMMENT 'aktuelles Datum',
                                       PRIMARY KEY (`ID`,`TRAINER_ID`,`SEM_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='SMAD EXPORT';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SYNTHESIS_SMTN_SENT`
--

DROP TABLE IF EXISTS `SYNTHESIS_SMTN_SENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SYNTHESIS_SMTN_SENT` (
                                       `PERS_ID` int(6) unsigned NOT NULL COMMENT 'SM_TN.ADRESSE_ADadnr',
                                       `SEM_ID` int(6) unsigned NOT NULL COMMENT 'SM_TN.SEMINAR_SMnr',
                                       `PJ_ID` int(6) unsigned DEFAULT NULL COMMENT 'PROJEKT.PJnr',
                                       `BUNDESLAND_ID` int(6) unsigned DEFAULT NULL COMMENT 'PROJEKT.PJkstgr',
                                       `BUNDESLAND_BEZ` varchar(50) DEFAULT NULL COMMENT 'KSTbezeichnung',
                                       `TEILN_VON` date NOT NULL COMMENT 'SM_TN.TAtailnahmevon',
                                       `TEILN_BIS` date NOT NULL COMMENT 'SM_TN-TAteilnahmebis',
                                       `ABSCHLUSS_AM` date DEFAULT NULL COMMENT 'SM_TN.TAabschlussdatum',
                                       `ABSCHLUSS_ID` int(6) unsigned DEFAULT NULL COMMENT 'SM_TN.TAabschluss',
                                       `ABSCHLUSS_BEZ` varchar(100) DEFAULT NULL COMMENT 'KEYTABLE.TAabschluss',
                                       `FIRMA_ID` int(6) unsigned DEFAULT NULL COMMENT 'SM_TN.TAabschlussBeiCrmFirma',
                                       `FIRMA_HEROLD_ID` int(6) unsigned DEFAULT NULL COMMENT 'CRM_FIRMA.herold_firma_id',
                                       `FIRMA_BEZ` varchar(50) DEFAULT NULL COMMENT 'CRM_FIRMA.name',
                                       `ABSCHLUSS_BEMERKUNG` text DEFAULT NULL COMMENT 'SM_TN.TAabschlussgrund (Frimen Infos?!?)',
                                       `ZIELGR_ID` int(6) unsigned NOT NULL COMMENT 'ASUSSCHREIBUNG.ASzielgruppe',
                                       `ZIELGR_BEZ` varchar(100) NOT NULL COMMENT 'KEYTABLE.BDzielgruppe',
                                       `INHALT_ID` int(6) unsigned NOT NULL COMMENT 'AUSSCHREIBUNG.ASinhlalt',
                                       `INHALT_BEZ` varchar(100) NOT NULL COMMENT 'KEYTABLE.ASinhalt',
                                       `UNTERBRECHUNG` int(1) NOT NULL DEFAULT 0,
                                       `GESENDET_AM` date NOT NULL COMMENT 'aktuelles Datum',
                                       PRIMARY KEY (`PERS_ID`,`SEM_ID`),
                                       KEY `GESENDET_AM` (`GESENDET_AM`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Export der SMTN-Daten für Synthesis (ibisRUN)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SYNTHESIS_SMTN_SENT_20120313`
--

DROP TABLE IF EXISTS `SYNTHESIS_SMTN_SENT_20120313`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SYNTHESIS_SMTN_SENT_20120313` (
                                                `PERS_ID` int(6) unsigned NOT NULL COMMENT 'SM_TN.ADRESSE_ADadnr',
                                                `SEM_ID` int(6) unsigned NOT NULL COMMENT 'SM_TN.SEMINAR_SMnr',
                                                `TEILN_VON` date NOT NULL COMMENT 'SM_TN.TAtailnahmevon',
                                                `TEILN_BIS` date NOT NULL COMMENT 'SM_TN-TAteilnahmebis',
                                                `ABSCHLUSS_AM` date DEFAULT NULL COMMENT 'SM_TN.TAabschlussdatum',
                                                `ZIELGR_ID` int(6) unsigned NOT NULL COMMENT 'ASUSSCHREIBUNG.ASzielgruppe',
                                                `ZIELGR_BEZ` varchar(100) NOT NULL COMMENT 'KEYTABLE.BDzielgruppe',
                                                `INHALT_ID` int(6) unsigned NOT NULL COMMENT 'AUSSCHREIBUNG.ASinhlalt',
                                                `INHALT_BEZ` varchar(100) NOT NULL COMMENT 'KEYTABLE.ASinhalt',
                                                `UNTERBRECHUNG` int(1) NOT NULL DEFAULT 0,
                                                `GESENDET_AM` date NOT NULL COMMENT 'aktuelles Datum',
                                                PRIMARY KEY (`PERS_ID`,`SEM_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Export der SMTN-Daten für Synthesis (ibisRUN)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SYNTHESIS_SVNR_SENT`
--

DROP TABLE IF EXISTS `SYNTHESIS_SVNR_SENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SYNTHESIS_SVNR_SENT` (
                                       `PERS_ID` int(6) unsigned NOT NULL COMMENT 'ADRESSE.ADadnr',
                                       `SVNR` varchar(11) NOT NULL COMMENT 'ADRESSE.ADsvnr',
                                       `GEB_DATUM` date DEFAULT NULL COMMENT 'ADRESSE.ADgebdatum',
                                       `GESENDET_AM` date NOT NULL COMMENT 'aktuelles Datum',
                                       PRIMARY KEY (`PERS_ID`,`SVNR`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Export der ADadnrund SVnr für Synthesis (ibisRUN)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SYNTHESIS_SVNR_SENT_20120313`
--

DROP TABLE IF EXISTS `SYNTHESIS_SVNR_SENT_20120313`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SYNTHESIS_SVNR_SENT_20120313` (
                                                `PERS_ID` int(6) unsigned NOT NULL COMMENT 'ADRESSE.ADadnr',
                                                `SVNR` varchar(11) NOT NULL COMMENT 'ADRESSE.ADsvnr',
                                                `GEB_DATUM` date DEFAULT NULL COMMENT 'ADRESSE.ADgebdatum',
                                                `GESENDET_AM` date NOT NULL COMMENT 'aktuelles Datum',
                                                PRIMARY KEY (`PERS_ID`,`SVNR`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Export der ADadnrund SVnr für Synthesis (ibisRUN)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEILNAHME`
--

DROP TABLE IF EXISTS `TEILNAHME`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEILNAHME` (
                             `SM_TN_ADRESSE_ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                             `SM_TN_SEMINAR_SMnr` int(6) NOT NULL COMMENT 'Seminar-Nummer',
                             `TNdatum` date NOT NULL COMMENT 'Datum',
                             `TNdatumBis` date DEFAULT NULL COMMENT 'optionales Datum-Bis',
                             `TNletzterKurstag` date DEFAULT NULL COMMENT 'voraussichtlich letzter Kurstag. Nur fuer Abschluss, Abbruch, Kursende, Arbeitsaufnahme',
                             `TNstatusV` char(3) DEFAULT NULL,
                             `TNstatusN` char(3) DEFAULT NULL,
                             `KSTTNstatusV` int(6) DEFAULT NULL,
                             `KSTTNstatusN` int(6) DEFAULT NULL,
                             `TNSTATUS_id` int(6) unsigned DEFAULT NULL,
                             `TNbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                             `TNstatus` char(1) DEFAULT NULL COMMENT 'Buchungsstatus',
                             `TNaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                             `TNaeuser` char(35) DEFAULT NULL,
                             `TNerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                             `TNeruser` char(35) DEFAULT NULL,
                             PRIMARY KEY (`SM_TN_ADRESSE_ADadnr`,`SM_TN_SEMINAR_SMnr`,`TNdatum`),
                             KEY `TNdatum` (`TNdatum`),
                             KEY `SMnr_TNdatum` (`SM_TN_SEMINAR_SMnr`,`TNdatum`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEILNAHMESTATUS_BEREICH`
--

DROP TABLE IF EXISTS `TEILNAHMESTATUS_BEREICH`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEILNAHMESTATUS_BEREICH` (
                                           `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                           `bezeichnung` varchar(60) DEFAULT NULL,
                                           `kstgr` int(3) unsigned DEFAULT NULL,
                                           `order` int(6) unsigned DEFAULT NULL,
                                           `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                           `eruser` char(35) DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `bezeichnung_UNIQUE` (`bezeichnung`)
) ENGINE=MyISAM AUTO_INCREMENT=42 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEILNEHMERKATEGORIE`
--

DROP TABLE IF EXISTS `TEILNEHMERKATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEILNEHMERKATEGORIE` (
                                       `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                       `bezeichnung` char(100) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEILNEHMER_ARBEITSMODELL`
--

DROP TABLE IF EXISTS `TEILNEHMER_ARBEITSMODELL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEILNEHMER_ARBEITSMODELL` (
                                            `ADRESSE_adnr` int(6) unsigned NOT NULL DEFAULT 0,
                                            `VERMITTLUNG_ARBEITSMODELL_id` int(6) unsigned NOT NULL DEFAULT 0,
                                            PRIMARY KEY (`ADRESSE_adnr`,`VERMITTLUNG_ARBEITSMODELL_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEILNEHMER_BERUFSSCHULBLOCK`
--

DROP TABLE IF EXISTS `TEILNEHMER_BERUFSSCHULBLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEILNEHMER_BERUFSSCHULBLOCK` (
                                               `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                               `TeilnehmerId` int(10) unsigned NOT NULL,
                                               `Name` varchar(80) NOT NULL,
                                               `Von` date NOT NULL,
                                               `Bis` date NOT NULL,
                                               `MontagVon` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `MontagBis` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `DienstagVon` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `DienstagBis` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `MittwochVon` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `MittwochBis` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `DonnerstagVon` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `DonnerstagBis` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `FreitagVon` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `FreitagBis` time /* mariadb-5.3 */ DEFAULT NULL,
                                               `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                               `AenderungsBenutzer` char(35) DEFAULT NULL,
                                               `ErstellungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                               `ErstellungsBenutzer` char(35) DEFAULT NULL,
                                               PRIMARY KEY (`Id`),
                                               UNIQUE KEY `TeilnehmerId_Name_UNIQUE` (`TeilnehmerId`,`Name`),
                                               KEY `Von_Bis_INDEX` (`Von`,`Bis`),
                                               CONSTRAINT `FK_TeilnehmerId` FOREIGN KEY (`TeilnehmerId`) REFERENCES `ADRESSE` (`ADadnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16418 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEILNEHMER_LEHRJAHR`
--

DROP TABLE IF EXISTS `TEILNEHMER_LEHRJAHR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEILNEHMER_LEHRJAHR` (
                                       `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                       `TeilnehmerId` int(10) unsigned NOT NULL,
                                       `Lehrjahr` int(1) unsigned NOT NULL DEFAULT 1,
                                       `Von` date NOT NULL,
                                       `Bis` date NOT NULL,
                                       `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `AenderungsBenutzer` char(35) DEFAULT NULL,
                                       `ErstellungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       `ErstellungsBenutzer` char(35) DEFAULT NULL,
                                       PRIMARY KEY (`Id`),
                                       UNIQUE KEY `TeilnehmerId_Lehrjahr_UNIQUE` (`TeilnehmerId`,`Lehrjahr`),
                                       CONSTRAINT `FK_Teilnehmer_Lehrjahr_Teilnehmer_TeilnehmerId` FOREIGN KEY (`TeilnehmerId`) REFERENCES `ADRESSE` (`ADadnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20607 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEILNEHMER_SEMINARZIEL`
--

DROP TABLE IF EXISTS `TEILNEHMER_SEMINARZIEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEILNEHMER_SEMINARZIEL` (
                                          `ADRESSE_adnr` int(6) unsigned NOT NULL DEFAULT 0,
                                          `SEMINARZIELE_id` int(6) unsigned NOT NULL DEFAULT 0,
                                          PRIMARY KEY (`ADRESSE_adnr`,`SEMINARZIELE_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEILNEHMER_VORLEHRZEIT`
--

DROP TABLE IF EXISTS `TEILNEHMER_VORLEHRZEIT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEILNEHMER_VORLEHRZEIT` (
                                          `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                          `TeilnehmerId` int(6) unsigned NOT NULL,
                                          `Von` date NOT NULL,
                                          `Bis` date NOT NULL,
                                          `Bemerkung` text DEFAULT NULL,
                                          PRIMARY KEY (`Id`),
                                          UNIQUE KEY `UQ_TEILNEHMER_VORLEHRZEIT_TeilnehmerId_Von_Bis` (`TeilnehmerId`,`Von`,`Bis`),
                                          CONSTRAINT `TEILNEHMER_VORLEHRZEIT_TeilnehmerId` FOREIGN KEY (`TeilnehmerId`) REFERENCES `ADRESSE` (`ADadnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2346 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEILNEHMER__PROJEKT_TEILNEHMERKATEGORIE`
--

DROP TABLE IF EXISTS `TEILNEHMER__PROJEKT_TEILNEHMERKATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEILNEHMER__PROJEKT_TEILNEHMERKATEGORIE` (
                                                           `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                                           `TeilnehmerId` int(6) unsigned NOT NULL,
                                                           `ProjektTeilnehmerkategorieId` int(11) unsigned NOT NULL,
                                                           `SeminarId` int(6) NOT NULL,
                                                           `ErstellungsBenutzer` char(35) NOT NULL,
                                                           `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                           PRIMARY KEY (`Id`),
                                                           UNIQUE KEY `TN__PJ_TNKATEGORIE_TeilnehmerId_ProjektTeilnehmerkategorieId` (`TeilnehmerId`,`ProjektTeilnehmerkategorieId`),
                                                           KEY `ProjektTeilnehmerkategorieId` (`ProjektTeilnehmerkategorieId`),
                                                           KEY `SeminarId` (`SeminarId`),
                                                           CONSTRAINT `TEILNEHMER__PROJEKT_TEILNEHMERKATEGORIE_ibfk_1` FOREIGN KEY (`TeilnehmerId`) REFERENCES `ADRESSE` (`ADadnr`) ON DELETE CASCADE ON UPDATE CASCADE,
                                                           CONSTRAINT `TEILNEHMER__PROJEKT_TEILNEHMERKATEGORIE_ibfk_2` FOREIGN KEY (`ProjektTeilnehmerkategorieId`) REFERENCES `PROJEKT_TEILNEHMERKATEGORIE` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                                           CONSTRAINT `TEILNEHMER__PROJEKT_TEILNEHMERKATEGORIE_ibfk_3` FOREIGN KEY (`SeminarId`) REFERENCES `SEMINAR` (`SMnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17369 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TERMIN`
--

DROP TABLE IF EXISTS `TERMIN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TERMIN` (
                          `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                          `datum` date DEFAULT NULL,
                          `uhrzeit_von` time /* mariadb-5.3 */ DEFAULT NULL,
                          `uhrzeit_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20304 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TITEL`
--

DROP TABLE IF EXISTS `TITEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TITEL` (
                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                         `bez_lang` varchar(100) DEFAULT NULL,
                         `bez_kurz` varchar(20) NOT NULL,
                         `nachgestellt` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Ist der Titel dem Namen nachgestellt oder vorangestellt?',
                         `order_id` int(6) unsigned DEFAULT NULL COMMENT 'Sortierreihenfolge',
                         `bmd_bezeichnung` varchar(60) DEFAULT NULL COMMENT 'Bezeichnung im BMD',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `bez_kurz` (`bez_kurz`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TNSTATUS`
--

DROP TABLE IF EXISTS `TNSTATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TNSTATUS` (
                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                            `ordnungsnummer` int(6) unsigned DEFAULT NULL,
                            `grund` varchar(100) NOT NULL,
                            `code` varchar(10) NOT NULL,
                            `hatLetztenKurstag` tinyint(1) NOT NULL DEFAULT 0,
                            `hatDatumBis` tinyint(1) NOT NULL DEFAULT 0,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TN_AUSTRITTSGRUND`
--

DROP TABLE IF EXISTS `TN_AUSTRITTSGRUND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TN_AUSTRITTSGRUND` (
                                     `Id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                     `Name` varchar(255) NOT NULL,
                                     `ProjekttypenId` int(6) unsigned NOT NULL,
                                     `TNAustrittsgrundKategorieId` int(6) unsigned NOT NULL,
                                     `ExterneId` int(6) DEFAULT NULL,
                                     `Aktiv` tinyint(1) NOT NULL DEFAULT 0,
                                     `Sortierung` int(6) DEFAULT NULL,
                                     `ErstellungsBenutzer` char(35) NOT NULL,
                                     `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                     `AenderungsBenutzer` char(35) DEFAULT NULL,
                                     `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                     PRIMARY KEY (`Id`),
                                     UNIQUE KEY `UI_TN_AUSTRITTSGRUND_NAME_PROJEKTTYP_KATEGORIE` (`Name`,`ProjekttypenId`,`TNAustrittsgrundKategorieId`),
                                     KEY `FK_TN_AUSTRITTSGRUND_PROJEKTTYPEN_idx` (`ProjekttypenId`),
                                     KEY `FK_TN_AUSTRITTSGRUND_KATEGORIE_idx` (`TNAustrittsgrundKategorieId`),
                                     CONSTRAINT `FK_TN_AUSTRITTSGRUND_KATEGORIE` FOREIGN KEY (`TNAustrittsgrundKategorieId`) REFERENCES `TN_AUSTRITTSGRUND_KATEGORIE` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
                                     CONSTRAINT `FK_TN_AUSTRITTSGRUND_PROJEKTTYPEN` FOREIGN KEY (`ProjekttypenId`) REFERENCES `PROJEKTTYPEN` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5032 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Austrittsgründe';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TN_AUSTRITTSGRUND_KATEGORIE`
--

DROP TABLE IF EXISTS `TN_AUSTRITTSGRUND_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TN_AUSTRITTSGRUND_KATEGORIE` (
                                               `Id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                               `Name` varchar(255) NOT NULL,
                                               `Aktiv` tinyint(1) NOT NULL DEFAULT 0,
                                               `Sortierung` int(6) DEFAULT NULL,
                                               `ErstellungsBenutzer` char(35) NOT NULL,
                                               `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                               `AenderungsBenutzer` char(35) DEFAULT NULL,
                                               `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                               PRIMARY KEY (`Id`),
                                               UNIQUE KEY `UI_TN_AUSTRITTSGRUNDKATEGORIE_NAME` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Kategorie der Austrittsgründe';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TN_ZUSATZ`
--

DROP TABLE IF EXISTS `TN_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TN_ZUSATZ` (
                             `ADRESSE_ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                             `TZgeburtsname` varchar(80) DEFAULT NULL COMMENT 'fit2work: Mehrere Namen getrennt duch Beistiche, weil es nur ein Infofeld ist',
                             `TZkdnrageber` varchar(50) DEFAULT NULL,
                             `TZkinderanzahl` int(2) DEFAULT NULL COMMENT 'Anzahl Kinder',
                             `TZkinderalter` char(20) DEFAULT NULL COMMENT 'Alter der Kinder',
                             `TZeigenesfahrzeug` enum('n','y') DEFAULT NULL COMMENT 'eigenes Fahrzeug',
                             `TZschulbildung` char(80) DEFAULT NULL COMMENT 'Schulbildung (Schultyp, Abschluss, Studium)',
                             `TZschulbildungletztjahr` year(4) DEFAULT NULL COMMENT 'letztes Schuljahr',
                             `TZschulbildungabgeschlossen` enum('n','y') DEFAULT NULL COMMENT 'Schulbildung abgeschlossen',
                             `TZschulpflicht` enum('y','n') DEFAULT 'n' COMMENT 'Schulpflicht erfüllt oder nicht',
                             `TZberufsausbildung` char(80) DEFAULT NULL COMMENT 'Berufsausbildung',
                             `TZberufsausbildungvon` char(7) DEFAULT NULL COMMENT 'Berufsausbildung von Datum (MM.JJJJ)',
                             `TZberufsausbildungbis` char(7) DEFAULT NULL COMMENT 'Berufsausbildung bis Datum (MM.JJJJ)',
                             `TZberufsausbildungabschluss` enum('n','y') DEFAULT NULL COMMENT 'Berufsausbildung abgeschlossen',
                             `TZwehrdienst` enum('n','y','b') DEFAULT NULL COMMENT 'Wehrdienst abgeleistet / befreit',
                             `TZgesundeinschr` text DEFAULT NULL COMMENT 'Gesundheitliche Einschränkungen',
                             `TZzusatzausb` text DEFAULT NULL COMMENT 'Zusatzausbildung, bes. Fähigkeiten, Kenntnisse',
                             `TZbeswuensche` text DEFAULT NULL COMMENT 'Besondere Wünsche',
                             `TZBDtyp1` int(6) DEFAULT NULL,
                             `TZBDtyp2` int(6) DEFAULT NULL,
                             `TZBDtypBemerkung` varchar(100) DEFAULT NULL,
                             `TZatage` char(7) DEFAULT '1111100' COMMENT 'Arbeitstage',
                             `TZazvon` time /* mariadb-5.3 */ DEFAULT NULL,
                             `TZazbis` time /* mariadb-5.3 */ DEFAULT NULL,
                             `TZoeffverkehr` enum('n','y') DEFAULT NULL,
                             `TZarbeitswegkm` int(6) DEFAULT NULL,
                             `TZarbeitswegzeit` int(6) DEFAULT NULL,
                             `TZberufsbereich1` int(6) DEFAULT NULL,
                             `TZberufsbereich2` int(6) DEFAULT NULL,
                             `TZberuf1` int(6) unsigned DEFAULT NULL COMMENT 'Berufsfeld neu Ersatz für TZberufsbereich2',
                             `TZberufsbereich3` int(6) DEFAULT NULL,
                             `TZberufsbereich4` int(6) DEFAULT NULL,
                             `TZberuf2` int(6) unsigned DEFAULT NULL COMMENT 'Berufsfeld neu Ersatz für TZberufsbereich4',
                             `TZberufsbereich5` varchar(100) DEFAULT NULL,
                             `TZvermittlungab` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Vermittelbar ab',
                             `TZdeutsch` int(1) unsigned DEFAULT NULL COMMENT 'Deutsch Kenntnisse',
                             `TZzuverlaessig` int(1) unsigned DEFAULT NULL,
                             `TZmotivation` int(1) unsigned DEFAULT NULL,
                             `TZcooperation` int(1) unsigned DEFAULT NULL,
                             `TZamshilfe` enum('n','y') DEFAULT NULL COMMENT 'FÃ¶rderung durch AMS',
                             `TZzeitarbeit` enum('n','y') DEFAULT NULL COMMENT 'FÃ¶rderung durch AMS',
                             `TZbestaetam` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Weitergabeerlaubnisunterschrift bestÃ¤tigt am',
                             `TZbestaetvon` int(6) unsigned DEFAULT NULL COMMENT 'Weitergabeerlaubnisunterschrift bestaetigt von',
                             `TZaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                             `TZaeuser` char(35) DEFAULT NULL,
                             `TZerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                             `TZeruser` char(35) DEFAULT NULL,
                             PRIMARY KEY (`ADRESSE_ADadnr`),
                             KEY `TN_ZUSATZ_FKIndex1` (`ADRESSE_ADadnr`),
                             KEY `TN_ZUSATZ_TZBDtyp2` (`TZBDtyp2`),
                             KEY `TN_ZUSATZ_TZBDtyp1` (`TZBDtyp1`,`TZBDtyp2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Teams_Vorlage_Kurse`
--

DROP TABLE IF EXISTS `Teams_Vorlage_Kurse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Teams_Vorlage_Kurse` (
                                       `Teams_Vorlage_Kurse_KEY` varchar(256) NOT NULL,
                                       `Team_Bezeichnung` varchar(256) NOT NULL,
                                       PRIMARY KEY (`Teams_Vorlage_Kurse_KEY`),
                                       UNIQUE KEY `Teams_Vorlage_Kurse_KEY_UNIQUE` (`Teams_Vorlage_Kurse_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_AUSTRITTSGRUND`
--

DROP TABLE IF EXISTS `UEBA_AUSTRITTSGRUND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_AUSTRITTSGRUND` (
                                       `ag_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                       `ag_name` varchar(100) DEFAULT NULL,
                                       PRIMARY KEY (`ag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_BERUFSSCHULE_KLASSEN`
--

DROP TABLE IF EXISTS `UEBA_BERUFSSCHULE_KLASSEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_BERUFSSCHULE_KLASSEN` (
                                             `id` int(6) unsigned NOT NULL,
                                             `bezeichnung` char(50) DEFAULT NULL,
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_KALENDERWOCHE`
--

DROP TABLE IF EXISTS `UEBA_KALENDERWOCHE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_KALENDERWOCHE` (
                                      `kw_id` int(10) unsigned NOT NULL,
                                      `kw_name` varchar(50) DEFAULT NULL,
                                      `prj_id` int(10) unsigned DEFAULT NULL,
                                      `kw_kw` tinyint(3) DEFAULT NULL,
                                      PRIMARY KEY (`kw_id`),
                                      UNIQUE KEY `kw_id` (`kw_id`),
                                      KEY `kw_id_2` (`kw_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_KURS_PROJEKTE`
--

DROP TABLE IF EXISTS `UEBA_KURS_PROJEKTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_KURS_PROJEKTE` (
                                      `id` int(6) unsigned NOT NULL,
                                      `bezeichnung` char(50) DEFAULT NULL,
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_LEHRBERUFE`
--

DROP TABLE IF EXISTS `UEBA_LEHRBERUFE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_LEHRBERUFE` (
                                   `lehrberufe_id` int(10) NOT NULL AUTO_INCREMENT,
                                   `lehrberufe_m` varchar(100) DEFAULT NULL,
                                   `lehrberufe_w` varchar(100) DEFAULT NULL,
                                   `lehrberufe_aktiv` tinyint(4) DEFAULT NULL,
                                   `lehrberufe_laeuftaus` date DEFAULT NULL,
                                   `lehrberufe_tas_nr` varchar(100) DEFAULT NULL,
                                   `lehrberufe_wk_nr` varchar(100) DEFAULT NULL,
                                   PRIMARY KEY (`lehrberufe_id`)
) ENGINE=InnoDB AUTO_INCREMENT=433 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_LEHRJAHRE`
--

DROP TABLE IF EXISTS `UEBA_LEHRJAHRE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_LEHRJAHRE` (
                                  `id` int(6) unsigned NOT NULL,
                                  `bezeichnung` char(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_LOG`
--

DROP TABLE IF EXISTS `UEBA_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_LOG` (
                            `log_id` int(10) NOT NULL AUTO_INCREMENT,
                            `log_tnd_id` int(10) NOT NULL,
                            `log_art_id` int(10) NOT NULL,
                            `log_kw_id` int(10) DEFAULT NULL,
                            `log_status` varchar(100) DEFAULT NULL,
                            `log_beginn` date DEFAULT NULL,
                            `log_ende` date DEFAULT NULL,
                            `log_bs_name` varchar(100) DEFAULT NULL,
                            `log_fa_name` varchar(100) DEFAULT NULL,
                            `log_urlaubstage` int(10) DEFAULT NULL,
                            `log_memo` text DEFAULT NULL,
                            `log_update_datum` date DEFAULT NULL,
                            `log_erst_datum` date DEFAULT NULL,
                            `log_tr_id` int(10) DEFAULT NULL,
                            `log_bs_bsergebnis` int(10) DEFAULT NULL,
                            `log_lehrberufs_id` int(10) DEFAULT NULL,
                            `zentraldb_letztes_update` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `erda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `eruser` char(35) DEFAULT NULL,
                            `aeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `aeuser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50377 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_LOGARTEN`
--

DROP TABLE IF EXISTS `UEBA_LOGARTEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_LOGARTEN` (
                                 `logart_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `logarten_kategorie_id` int(6) unsigned DEFAULT NULL,
                                 `logart_name` varchar(50) DEFAULT NULL,
                                 `logart_brfs` tinyint(10) unsigned DEFAULT NULL,
                                 `logart_verwendung` tinyint(4) DEFAULT NULL,
                                 `logart_kommentar` text DEFAULT NULL,
                                 `logart_admin_sort` int(11) DEFAULT 0,
                                 `logart_zentraldb` tinyint(4) DEFAULT 0 COMMENT '1=Ja, 0=Nein',
                                 PRIMARY KEY (`logart_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_LOGARTEN_KATEGORIE`
--

DROP TABLE IF EXISTS `UEBA_LOGARTEN_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_LOGARTEN_KATEGORIE` (
                                           `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                           `bezeichnung` char(255) DEFAULT NULL,
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_LOG_BERUFSSCHULE_ZUSATZ`
--

DROP TABLE IF EXISTS `UEBA_LOG_BERUFSSCHULE_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_LOG_BERUFSSCHULE_ZUSATZ` (
                                                `UEBA_LOG_id` int(10) NOT NULL,
                                                `UEBA_BERUFSSCHULE_KLASSEN_id` int(6) unsigned DEFAULT NULL,
                                                PRIMARY KEY (`UEBA_LOG_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_LOG_ERGEBNIS`
--

DROP TABLE IF EXISTS `UEBA_LOG_ERGEBNIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_LOG_ERGEBNIS` (
                                     `log_erg_id` int(11) NOT NULL,
                                     `log_erg_bezeichnung` varchar(50) DEFAULT NULL,
                                     PRIMARY KEY (`log_erg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_LOG_LEHRBERUFSWECHSEL_ZUSATZ`
--

DROP TABLE IF EXISTS `UEBA_LOG_LEHRBERUFSWECHSEL_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_LOG_LEHRBERUFSWECHSEL_ZUSATZ` (
                                                     `UEBA_LOG_id` int(10) NOT NULL,
                                                     `UEBA_LEHRBERUFE_id` int(10) DEFAULT NULL,
                                                     `lv_nummer` char(100) DEFAULT NULL,
                                                     `old_lehrberuf_log_id` int(10) DEFAULT NULL,
                                                     `new_lehrberuf_log_id` int(10) DEFAULT NULL,
                                                     PRIMARY KEY (`UEBA_LOG_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_PROJEKT`
--

DROP TABLE IF EXISTS `UEBA_PROJEKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_PROJEKT` (
                                `prj_id` int(10) unsigned NOT NULL,
                                `prj_name` varchar(50) DEFAULT NULL,
                                `prj_jahr` int(6) unsigned DEFAULT NULL,
                                PRIMARY KEY (`prj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_PROJEKT_ZUSATZ`
--

DROP TABLE IF EXISTS `UEBA_PROJEKT_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_PROJEKT_ZUSATZ` (
                                       `PROJEKT_id` int(6) NOT NULL,
                                       `tasnr_ueba` varchar(50) DEFAULT NULL COMMENT 'TAS-Nummer für Teilnehmer, wenn aus ÜBA-Projekt',
                                       `tasnr_iba` varchar(50) DEFAULT NULL COMMENT 'TAS-Nummer für Teilnehmer, wenn aus IBA-Projekt',
                                       `UEBA_PROJEKT_id` int(10) unsigned DEFAULT NULL COMMENT 'UEBA_PROJEKT.prj_id',
                                       PRIMARY KEY (`PROJEKT_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_RGS`
--

DROP TABLE IF EXISTS `UEBA_RGS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_RGS` (
                            `rgs_id` int(11) NOT NULL,
                            `rgs_name` varchar(100) DEFAULT NULL,
                            PRIMARY KEY (`rgs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_SCHULABSCHLUSS`
--

DROP TABLE IF EXISTS `UEBA_SCHULABSCHLUSS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_SCHULABSCHLUSS` (
                                       `sa_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                       `sa_name` varchar(100) DEFAULT NULL,
                                       PRIMARY KEY (`sa_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_TN_DETAIL`
--

DROP TABLE IF EXISTS `UEBA_TN_DETAIL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_TN_DETAIL` (
                                  `tnd_id` int(11) NOT NULL AUTO_INCREMENT,
                                  `tnd_k_knr` int(11) NOT NULL,
                                  `tnd_projekt_id` int(11) DEFAULT NULL COMMENT 'PROJEKT.PJnr des laufenden Projektes',
                                  `tnd_projekt_id_old` int(11) DEFAULT NULL,
                                  `tnd_rgs_id` int(11) NOT NULL DEFAULT 0,
                                  `tnd_eintritt` date DEFAULT NULL,
                                  `tnd_austritt` date DEFAULT NULL,
                                  `tnd_ag_id` int(11) NOT NULL DEFAULT 0,
                                  `tnd_sa_id` int(11) NOT NULL DEFAULT 0,
                                  `tnd_lsab` date DEFAULT NULL,
                                  `tnd_lehrberuf_id` int(11) NOT NULL,
                                  `tnd_bisherige_ausbildung` varchar(100) NOT NULL,
                                  `tnd_lehrjahr_wechsel` date DEFAULT NULL,
                                  `tnd_lehrzeitende` date DEFAULT NULL,
                                  `tnd_verlaengerer` tinyint(4) DEFAULT NULL,
                                  `tnd_erz_name` varchar(80) DEFAULT NULL,
                                  `tnd_erz_anschrift` varchar(80) DEFAULT NULL,
                                  `tnd_erz_plz` varchar(80) DEFAULT NULL,
                                  `tnd_erz_ort` varchar(80) DEFAULT NULL,
                                  `tnd_erz_tel` varchar(80) DEFAULT NULL,
                                  `tnd_erz_handy` varchar(80) DEFAULT NULL,
                                  `tnd_erz_email` varchar(80) DEFAULT NULL,
                                  `tnd_erz_bemerkung` varchar(80) DEFAULT NULL,
                                  `tnd_trainername` varchar(80) DEFAULT NULL,
                                  `tnd_update_datum` date DEFAULT NULL,
                                  `tnd_erst_datum` date DEFAULT NULL,
                                  `tnd_tr_id` int(11) NOT NULL DEFAULT 0,
                                  `erda` datetime DEFAULT NULL,
                                  `eruser` char(35) DEFAULT NULL,
                                  `aeda` datetime DEFAULT NULL,
                                  `aeuser` char(35) DEFAULT NULL,
                                  PRIMARY KEY (`tnd_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1926 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_TN_DETAIL_DOKUMENTE`
--

DROP TABLE IF EXISTS `UEBA_TN_DETAIL_DOKUMENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_TN_DETAIL_DOKUMENTE` (
                                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                            `UEBA_TN_DETAIL_id` int(11) DEFAULT NULL,
                                            `sySTORE_STnr` int(9) unsigned DEFAULT NULL,
                                            `beschreibung` text DEFAULT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2261 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_TN_DETAIL_LEHRJAHR`
--

DROP TABLE IF EXISTS `UEBA_TN_DETAIL_LEHRJAHR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_TN_DETAIL_LEHRJAHR` (
                                           `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                           `UEBA_TN_DETAIL_id` int(11) DEFAULT NULL,
                                           `UEBA_LEHRJAHRE_id` int(6) unsigned DEFAULT NULL,
                                           `lehrjahr_von` date DEFAULT NULL,
                                           `lehrjahr_bis` date DEFAULT NULL,
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2369 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_TN_DETAIL_VORHERIGE_LEHRSTELLEN`
--

DROP TABLE IF EXISTS `UEBA_TN_DETAIL_VORHERIGE_LEHRSTELLEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_TN_DETAIL_VORHERIGE_LEHRSTELLEN` (
                                                        `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                        `UEBA_TN_DETAIL_id` int(11) DEFAULT NULL,
                                                        `UEBA_LEHRBERUFE_id` int(10) DEFAULT NULL,
                                                        `datum_von` date DEFAULT NULL,
                                                        `datum_bis` date DEFAULT NULL,
                                                        `vorhergehende_ueba_lehre` tinyint(1) DEFAULT NULL,
                                                        `firma` char(255) DEFAULT NULL,
                                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_TN_DETAIL_ZUSATZ`
--

DROP TABLE IF EXISTS `UEBA_TN_DETAIL_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_TN_DETAIL_ZUSATZ` (
                                         `UEBA_TN_DETAIL_id` int(11) NOT NULL,
                                         `kstgr` int(3) unsigned DEFAULT NULL COMMENT 'Kostenstellengruppe',
                                         `UEBA_KURS_PROJEKTE_id` int(6) unsigned DEFAULT NULL,
                                         `kurs_nr` int(6) unsigned DEFAULT NULL,
                                         `ueba_projekt_gestartet_in` int(10) unsigned DEFAULT NULL,
                                         `aktiv` tinyint(1) DEFAULT NULL,
                                         `aktiv_seit` date DEFAULT NULL,
                                         `wk_anmeldung` tinyint(1) DEFAULT NULL,
                                         `lehrberuf_lv_nummer` char(255) DEFAULT NULL,
                                         `abmeldung_gkk` date DEFAULT NULL,
                                         `bemerkung` text DEFAULT NULL,
                                         `VerlaengererVorgesehen` int(1) unsigned NOT NULL DEFAULT 0,
                                         `Nachbetreuung` int(1) unsigned DEFAULT 0,
                                         `zentraldb_letztes_update` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                         PRIMARY KEY (`UEBA_TN_DETAIL_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_TN_ZUSATZ`
--

DROP TABLE IF EXISTS `UEBA_TN_ZUSATZ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_TN_ZUSATZ` (
                                  `ADRESSE_id` int(6) unsigned NOT NULL,
                                  `ams_betreuer` char(255) DEFAULT NULL,
                                  `UEBA_RGS_id` int(11) DEFAULT NULL COMMENT 'UEBA_RGS.rgs_id',
                                  `zentraldb_letztes_update` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  PRIMARY KEY (`ADRESSE_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_WIEN_AUSBILDUNGSFORM`
--

DROP TABLE IF EXISTS `UEBA_WIEN_AUSBILDUNGSFORM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_WIEN_AUSBILDUNGSFORM` (
                                             `Id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                             `Name` varchar(200) DEFAULT NULL,
                                             `Aktiv` tinyint(1) NOT NULL DEFAULT 0,
                                             `Sortierung` int(6) DEFAULT NULL,
                                             `ExterneId` varchar(200) DEFAULT NULL,
                                             `ErstellungsBenutzer` char(35) NOT NULL,
                                             `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                             `AenderungsBenutzer` char(35) DEFAULT NULL,
                                             `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                             PRIMARY KEY (`Id`),
                                             UNIQUE KEY `UI_UEBA_WIEN_AUSBILDUNGSFORM_Name` (`Name`),
                                             UNIQUE KEY `UI_UEBA_WIEN_AUSBILDUNGSFORM_ExterneId` (`ExterneId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_WIEN_LEHRBERUF`
--

DROP TABLE IF EXISTS `UEBA_WIEN_LEHRBERUF`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_WIEN_LEHRBERUF` (
                                       `Id` int(10) unsigned NOT NULL,
                                       `Obergruppe` varchar(200) DEFAULT NULL,
                                       `Name` varchar(200) DEFAULT NULL,
                                       `Aktiv` tinyint(1) NOT NULL DEFAULT 1,
                                       `LauftAus` date DEFAULT NULL,
                                       `Lehrzeitdauer` decimal(18,2) DEFAULT NULL,
                                       `LehrzeitdauerLehrform` decimal(18,2) DEFAULT NULL,
                                       `AmsCode` varchar(200) DEFAULT NULL,
                                       `AmsBezeichnung` varchar(200) DEFAULT NULL,
                                       `ErstellungsBenutzer` char(35) NOT NULL,
                                       `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                       `AenderungsBenutzer` char(35) DEFAULT NULL,
                                       `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_WIEN_LOG`
--

DROP TABLE IF EXISTS `UEBA_WIEN_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_WIEN_LOG` (
                                 `Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `TeilnehmerId` int(6) unsigned NOT NULL,
                                 `SeminarId` int(6) NOT NULL,
                                 `UebaWienLogArtenId` int(6) unsigned NOT NULL,
                                 `Logbeginn` date NOT NULL,
                                 `Logende` date DEFAULT NULL,
                                 `Berufsschulname` varchar(100) DEFAULT NULL,
                                 `Tage` decimal(18,2) DEFAULT NULL,
                                 `Firmenname` varchar(200) DEFAULT NULL,
                                 `Firmenadresse` varchar(200) DEFAULT NULL,
                                 `PLZ` varchar(10) DEFAULT NULL,
                                 `Ort` varchar(200) DEFAULT NULL,
                                 `Memo` text DEFAULT NULL,
                                 `UebaWienLogErgebnisId` int(6) unsigned DEFAULT NULL,
                                 `UebaWienLehrberufId` int(10) unsigned DEFAULT NULL,
                                 `TransferDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `TransferHash` varchar(32) DEFAULT NULL,
                                 `ErstellungsBenutzer` char(35) NOT NULL,
                                 `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                 `AenderungsBenutzer` char(35) DEFAULT NULL,
                                 `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `GeloeschtDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                 `PraktikumID` int(10) DEFAULT NULL,
                                 PRIMARY KEY (`Id`),
                                 KEY `FK_UEBA_WIEN_LOG_TeilnehmerId_idx` (`TeilnehmerId`),
                                 KEY `FK_UEBA_WIEN_LOG_SeminarId_idx` (`SeminarId`),
                                 KEY `FK_UEBA_WIEN_LOG_LogartenId_idx` (`UebaWienLogArtenId`),
                                 KEY `FK_UEBA_WIEN_LOG_LogergebnisId_idx` (`UebaWienLogErgebnisId`),
                                 KEY `FK_UEBA_WIEN_LOG_LehrberufId_idx` (`UebaWienLehrberufId`),
                                 CONSTRAINT `FK_UEBA_WIEN_LOG_LehrberufId` FOREIGN KEY (`UebaWienLehrberufId`) REFERENCES `UEBA_WIEN_LEHRBERUF` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                 CONSTRAINT `FK_UEBA_WIEN_LOG_LogartenId` FOREIGN KEY (`UebaWienLogArtenId`) REFERENCES `UEBA_WIEN_LOG_ARTEN` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                 CONSTRAINT `FK_UEBA_WIEN_LOG_LogergebnisId` FOREIGN KEY (`UebaWienLogErgebnisId`) REFERENCES `UEBA_WIEN_LOG_ERGEBNIS` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                 CONSTRAINT `FK_UEBA_WIEN_LOG_SeminarId` FOREIGN KEY (`SeminarId`) REFERENCES `SEMINAR` (`SMnr`) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                 CONSTRAINT `FK_UEBA_WIEN_LOG_TeilnehmerId` FOREIGN KEY (`TeilnehmerId`) REFERENCES `ADRESSE` (`ADadnr`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12065 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_WIEN_LOG_ARTEN`
--

DROP TABLE IF EXISTS `UEBA_WIEN_LOG_ARTEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_WIEN_LOG_ARTEN` (
                                       `Id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                       `Name` varchar(255) DEFAULT NULL,
                                       `Aktiv` tinyint(1) NOT NULL DEFAULT 0,
                                       `Sortierung` int(6) DEFAULT NULL,
                                       `ExterneId` int(6) DEFAULT NULL,
                                       `ErstellungsBenutzer` char(35) NOT NULL,
                                       `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                       `AenderungsBenutzer` char(35) DEFAULT NULL,
                                       `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       PRIMARY KEY (`Id`),
                                       UNIQUE KEY `UI_UEBA_WIEN_LOG_ARTEN_Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_WIEN_LOG_ERGEBNIS`
--

DROP TABLE IF EXISTS `UEBA_WIEN_LOG_ERGEBNIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_WIEN_LOG_ERGEBNIS` (
                                          `Id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                          `Name` varchar(255) DEFAULT NULL,
                                          `Aktiv` tinyint(1) NOT NULL DEFAULT 0,
                                          `Sortierung` int(6) DEFAULT NULL,
                                          `ExterneId` varchar(200) DEFAULT NULL,
                                          `ErstellungsBenutzer` char(35) NOT NULL,
                                          `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                          `AenderungsBenutzer` char(35) DEFAULT NULL,
                                          `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                          PRIMARY KEY (`Id`),
                                          UNIQUE KEY `UI_UEBA_WIEN_LOG_ERGEBNIS_Name` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_WIEN_SEMINAR_ERWEITERUNG`
--

DROP TABLE IF EXISTS `UEBA_WIEN_SEMINAR_ERWEITERUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_WIEN_SEMINAR_ERWEITERUNG` (
                                                 `SeminarId` int(6) NOT NULL,
                                                 `UebaWienSendZentralDb` tinyint(1) NOT NULL DEFAULT 0,
                                                 `ErstellungsBenutzer` char(35) NOT NULL,
                                                 `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                                 `AenderungsBenutzer` char(35) DEFAULT NULL,
                                                 `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                 PRIMARY KEY (`SeminarId`),
                                                 CONSTRAINT `FK_UEBA_WIEN_SEMINAR_ERWEITERUNG_SeminarId` FOREIGN KEY (`SeminarId`) REFERENCES `SEMINAR` (`SMnr`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Erweiterungstabelle eine Üba-Wien Projektes Seminar betreffend';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_WIEN_SMTN_ERWEITERUNG`
--

DROP TABLE IF EXISTS `UEBA_WIEN_SMTN_ERWEITERUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_WIEN_SMTN_ERWEITERUNG` (
                                              `TeilnehmerId` int(6) unsigned NOT NULL,
                                              `SeminarId` int(6) NOT NULL,
                                              `UebaWienLehrberufId` int(10) unsigned NOT NULL,
                                              `UebaWienAusbildungsformId` int(6) unsigned NOT NULL,
                                              `Eintrittsdatum` date NOT NULL,
                                              `Lehrzeitende` date DEFAULT NULL,
                                              `TAS_M` int(6) unsigned NOT NULL,
                                              `TAS_V` int(2) unsigned NOT NULL DEFAULT 0,
                                              `StammTransferDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                              `StammTransferHash` varchar(32) DEFAULT NULL,
                                              `DetailTransferDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                              `DetailTransferHash` varchar(32) DEFAULT NULL,
                                              `Berufsschulname` varchar(100) DEFAULT NULL,
                                              `ErstellungsBenutzer` char(35) NOT NULL,
                                              `ErstellungsDatum` datetime /* mariadb-5.3 */ NOT NULL,
                                              `AenderungsBenutzer` char(35) DEFAULT NULL,
                                              `AenderungsDatum` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                              PRIMARY KEY (`TeilnehmerId`,`SeminarId`),
                                              KEY `FK_UEBA_WIEN_SMTN_ERWEITERUNG_TeilnehmerId_idx` (`TeilnehmerId`),
                                              KEY `FK_UEBA_WIEN_SMTN_ERWEITERUNG_SeminarId_idx` (`SeminarId`),
                                              KEY `FK_UEBA_WIEN_SMTN_ERWEITERUNG_LehrberufId_idx` (`UebaWienLehrberufId`),
                                              KEY `FK_UEBA_WIEN_SMTN_ERWEITERUNG_AusbildungsformId_idx` (`UebaWienAusbildungsformId`),
                                              CONSTRAINT `FK_UEBA_WIEN_SMTN_ERWEITERUNG_AusbildungsformId` FOREIGN KEY (`UebaWienAusbildungsformId`) REFERENCES `UEBA_WIEN_AUSBILDUNGSFORM` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
                                              CONSTRAINT `FK_UEBA_WIEN_SMTN_ERWEITERUNG_LehrberufId` FOREIGN KEY (`UebaWienLehrberufId`) REFERENCES `UEBA_WIEN_LEHRBERUF` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
                                              CONSTRAINT `FK_UEBA_WIEN_SMTN_ERWEITERUNG_SeminarId` FOREIGN KEY (`SeminarId`) REFERENCES `SEMINAR` (`SMnr`) ON DELETE NO ACTION ON UPDATE CASCADE,
                                              CONSTRAINT `FK_UEBA_WIEN_SMTN_ERWEITERUNG_TeilnehmerId` FOREIGN KEY (`TeilnehmerId`) REFERENCES `ADRESSE` (`ADadnr`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_WOCHENSTATUS`
--

DROP TABLE IF EXISTS `UEBA_WOCHENSTATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_WOCHENSTATUS` (
                                     `ws_id` int(11) NOT NULL,
                                     `ws_kuerzel` varchar(50) DEFAULT NULL,
                                     `ws_bezeichnung` varchar(50) DEFAULT NULL,
                                     PRIMARY KEY (`ws_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UEBA_ZENTRALDB_LOG`
--

DROP TABLE IF EXISTS `UEBA_ZENTRALDB_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UEBA_ZENTRALDB_LOG` (
                                      `id` int(10) NOT NULL AUTO_INCREMENT,
                                      `zeitpunkt` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                      `nachricht` text DEFAULT NULL,
                                      `objekt` text DEFAULT NULL COMMENT 'Serialized Objekt',
                                      `eruser` char(35) DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UMSATZLISTE`
--

DROP TABLE IF EXISTS `UMSATZLISTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UMSATZLISTE` (
                               `ULid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID der UL',
                               `ULJAHR` int(4) unsigned NOT NULL COMMENT 'Jahr der UL',
                               `ULMONAT` int(2) unsigned NOT NULL COMMENT 'MONAT der UL',
                               `ULKSTGR` int(3) unsigned NOT NULL COMMENT 'Kostenstelle der UL',
                               `ULstatus` int(1) unsigned DEFAULT 0 COMMENT 'Status der UL',
                               `ULdatum` date DEFAULT NULL COMMENT 'Datum der letzten Datenaktualisierung',
                               `ULuser` char(35) DEFAULT NULL COMMENT 'Benutzer der letzten Datenaktualisierung',
                               `ULloek` enum('n','y') DEFAULT 'n' COMMENT 'Loeschkennzeichen',
                               `ULaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Letztes Ã„nderungsdatum',
                               `ULaeuser` char(35) DEFAULT NULL COMMENT 'Letzter Ã„nderungsbenutzer',
                               `ULerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                               `ULeruser` char(35) DEFAULT NULL COMMENT 'Erstellungsbenutzer',
                               PRIMARY KEY (`ULid`)
) ENGINE=MyISAM AUTO_INCREMENT=1965 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UMSATZLISTE_ABWEICHUNGEN`
--

DROP TABLE IF EXISTS `UMSATZLISTE_ABWEICHUNGEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UMSATZLISTE_ABWEICHUNGEN` (
                                            `UAid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                            `UAbuchulid` int(6) unsigned NOT NULL COMMENT 'In welcher Umsatzliste erfolgt die Buchung',
                                            `UAwertulid` int(6) unsigned NOT NULL COMMENT 'Für welche Umsatzliste war die Buchung',
                                            `UApjnr` int(6) unsigned NOT NULL COMMENT 'Projektnummer der Anpassung',
                                            `UAdiffpreis` decimal(9,2) DEFAULT NULL COMMENT 'Differenz beim Preis',
                                            `UAdiffmnh` int(6) DEFAULT NULL COMMENT 'Differenz der MNH',
                                            `UAdiffpeph` decimal(9,2) DEFAULT NULL COMMENT 'Differenz der PEPH',
                                            `UAdiffumsatz` decimal(15,2) DEFAULT NULL COMMENT 'Umsatzdifferenz',
                                            `UAloek` enum('y','n') DEFAULT 'n',
                                            `UAaeuser` char(35) DEFAULT NULL,
                                            `UAaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                            `UAeruser` char(35) DEFAULT NULL,
                                            `UAerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                            PRIMARY KEY (`UAid`)
) ENGINE=InnoDB AUTO_INCREMENT=21714 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci COMMENT='Abweichungen in der Umsatzliste';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UMSATZLISTE_PJ`
--

DROP TABLE IF EXISTS `UMSATZLISTE_PJ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UMSATZLISTE_PJ` (
                                  `UPpjnr` int(6) unsigned NOT NULL COMMENT 'Projektnummer',
                                  `UPulid` int(6) unsigned NOT NULL COMMENT 'ID der erstellenden UL',
                                  `UPpreis` decimal(10,2) DEFAULT NULL COMMENT 'Gespeicherter Preis',
                                  `UPmnh` decimal(9,2) DEFAULT NULL COMMENT 'Gespeicherte MNH',
                                  `UPare` decimal(9,2) DEFAULT 0.00 COMMENT 'Ausgangs Rechnung',
                                  `UPdurch` decimal(9,2) DEFAULT 0.00 COMMENT 'DurchlÃ¤ufer',
                                  `UPzare` decimal(9,2) DEFAULT 0.00 COMMENT 'Zwischen Abrechnung',
                                  `UPjahrtrainerh` int(7) DEFAULT 0 COMMENT 'jÃ¤hrlich - aufgelaufene Trainerstunden',
                                  `UPjahrumsatz` decimal(9,2) DEFAULT 0.00 COMMENT 'jÃ¤hrlich - Umsatz Vorjahr',
                                  `UPaeda` datetime DEFAULT NULL,
                                  `UPaeuser` char(35) DEFAULT NULL,
                                  `UPerda` datetime DEFAULT NULL,
                                  `UPeruser` char(35) DEFAULT NULL,
                                  PRIMARY KEY (`UPulid`,`UPpjnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UMSATZLISTE_SM`
--

DROP TABLE IF EXISTS `UMSATZLISTE_SM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UMSATZLISTE_SM` (
                                  `USsmnr` int(6) unsigned NOT NULL COMMENT 'SEMINARNUMMER',
                                  `USpjnr` int(6) unsigned NOT NULL COMMENT 'Projektnummer',
                                  `USulid` int(6) unsigned NOT NULL COMMENT 'ID der erstellenden UL',
                                  `USpeph` decimal(9,2) DEFAULT NULL COMMENT 'Personalstunden',
                                  `USaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `USaeuser` char(35) DEFAULT NULL,
                                  `USerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                  `USeruser` char(35) DEFAULT NULL,
                                  PRIMARY KEY (`USsmnr`,`USulid`,`USpjnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USERRECHT`
--

DROP TABLE IF EXISTS `USERRECHT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERRECHT` (
                             `URid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Zuordnungs ID',
                             `UR_RBid` int(10) unsigned NOT NULL COMMENT 'Bereichs ID',
                             `UR_RFid` int(10) unsigned NOT NULL COMMENT 'Funktions ID',
                             `UR_Rid` int(10) unsigned NOT NULL COMMENT 'Recht ID',
                             `URerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                             `UReruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                             PRIMARY KEY (`URid`),
                             UNIQUE KEY `UR_RBid` (`UR_RBid`,`UR_RFid`,`UR_Rid`),
                             KEY `FK_USERRECHT_Rid` (`UR_Rid`),
                             KEY `FK_USERRECHT_RFid` (`UR_RFid`),
                             CONSTRAINT `USERRECHT_RBid` FOREIGN KEY (`UR_RBid`) REFERENCES `RECHTBEREICH` (`RBid`) ON DELETE CASCADE ON UPDATE CASCADE,
                             CONSTRAINT `USERRECHT_RFid` FOREIGN KEY (`UR_RFid`) REFERENCES `RECHTFUNKTION` (`RFid`) ON DELETE CASCADE ON UPDATE CASCADE,
                             CONSTRAINT `USERRECHT_Rid` FOREIGN KEY (`UR_Rid`) REFERENCES `RECHT` (`Rid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=402 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Zuordnung eines Rechtes zu einem Bereich';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USERRECHT_BENUTZER`
--

DROP TABLE IF EXISTS `USERRECHT_BENUTZER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERRECHT_BENUTZER` (
                                      `URBid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Zuordnungs ID',
                                      `URB_URid` int(10) unsigned NOT NULL COMMENT 'Rechtzuordnungs ID',
                                      `URB_BNid` int(10) unsigned NOT NULL COMMENT 'User ID',
                                      `URBerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                      `URBeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                      PRIMARY KEY (`URBid`),
                                      UNIQUE KEY `URB_URid` (`URB_URid`,`URB_BNid`),
                                      KEY `FK_USERRECHT_BENUTZER_BNid` (`URB_BNid`),
                                      CONSTRAINT `USERRECHT_BENUTZER_BNid` FOREIGN KEY (`URB_BNid`) REFERENCES `BENUTZER` (`BNid`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `USERRECHT_BENUTZER_URid` FOREIGN KEY (`URB_URid`) REFERENCES `USERRECHT` (`URid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=150581 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Zuordnung der Benutzer zu Rechten';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USERRECHT_BENUTZER_KST`
--

DROP TABLE IF EXISTS `USERRECHT_BENUTZER_KST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERRECHT_BENUTZER_KST` (
                                          `URBK_URBid` int(10) unsigned NOT NULL COMMENT 'Gruppen ID',
                                          `URBK_KSTGR` int(3) unsigned NOT NULL DEFAULT 0 COMMENT 'Kostenstellen Gruppe',
                                          `URBK_KSTNR` int(3) unsigned NOT NULL DEFAULT 0 COMMENT 'Kostenstellen Nummer',
                                          `URBKerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                          `URBKeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                          PRIMARY KEY (`URBK_URBid`,`URBK_KSTGR`,`URBK_KSTNR`),
                                          KEY `FK_USERRECHT_BENUTZER_KST_KSTID` (`URBK_KSTGR`,`URBK_KSTNR`),
                                          CONSTRAINT `USERRECHT_BENUTZER_KST_KSTID` FOREIGN KEY (`URBK_KSTGR`, `URBK_KSTNR`) REFERENCES `FKOSTENSTELLE` (`KSTKSTGR`, `KSTKSTNR`) ON DELETE CASCADE ON UPDATE CASCADE,
                                          CONSTRAINT `USERRECHT_BENUTZER_KST_URBid` FOREIGN KEY (`URBK_URBid`) REFERENCES `USERRECHT_BENUTZER` (`URBid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Zuordnung der Benutzerrechte zu einer Kostenstelle';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USERRECHT_GRUPPE`
--

DROP TABLE IF EXISTS `USERRECHT_GRUPPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERRECHT_GRUPPE` (
                                    `URG_URid` int(10) unsigned NOT NULL COMMENT 'Rechtzuordnungs ID',
                                    `URG_RGid` int(10) unsigned NOT NULL COMMENT 'Gruppe ID',
                                    `URGerda` datetime /* mariadb-5.3 */ NOT NULL COMMENT 'Erstellungsdatum',
                                    `URGeruser` char(35) NOT NULL COMMENT 'Erstellungsbenutzer',
                                    PRIMARY KEY (`URG_URid`,`URG_RGid`),
                                    KEY `FK_USERRECHT_GRUPPE_RGid` (`URG_RGid`),
                                    CONSTRAINT `USERRECHT_GRUPPE_RGid` FOREIGN KEY (`URG_RGid`) REFERENCES `RECHTGRUPPE` (`RGid`) ON DELETE CASCADE ON UPDATE CASCADE,
                                    CONSTRAINT `USERRECHT_GRUPPE_RZid` FOREIGN KEY (`URG_URid`) REFERENCES `USERRECHT` (`URid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Zuordnung der Gruppen zu Rechten';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_KL`
--

DROP TABLE IF EXISTS `VA_KL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VA_KL` (
                         `AUSSCHREIBUNG_ASnr` int(6) unsigned NOT NULL DEFAULT 0,
                         `ADRESSE_ADadnr` int(6) unsigned NOT NULL DEFAULT 0,
                         `VKDatumVon` date DEFAULT NULL,
                         `VKZeitVon` time /* mariadb-5.3 */ DEFAULT NULL,
                         `VKDatumBis` date DEFAULT NULL,
                         `VKZeitBis` time /* mariadb-5.3 */ DEFAULT NULL,
                         `VKeinheiten` int(10) unsigned DEFAULT NULL,
                         `VKeinheitenbem` text DEFAULT NULL,
                         `VKgegenstand` text DEFAULT NULL,
                         `VKfunktion` varchar(60) DEFAULT NULL,
                         `VKaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `VKaeuser` char(35) DEFAULT NULL,
                         `VKerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                         `VKeruser` char(35) DEFAULT NULL,
                         PRIMARY KEY (`AUSSCHREIBUNG_ASnr`,`ADRESSE_ADadnr`),
                         KEY `VA_KL_FKIndex1` (`ADRESSE_ADadnr`),
                         KEY `VA_KL_FKIndex2` (`AUSSCHREIBUNG_ASnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VERANSTALTUNG`
--

DROP TABLE IF EXISTS `VERANSTALTUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VERANSTALTUNG` (
                                 `VAnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Veranstaltungs-Nummer',
                                 `VAabtnr` int(3) unsigned DEFAULT NULL COMMENT 'Abteilungs-Nummer',
                                 `VAbezeichnung1` varchar(80) DEFAULT NULL COMMENT 'Veranstaltungs-Bezeichnung 1',
                                 `VAbezeichnung2` varchar(80) DEFAULT NULL COMMENT 'Veranstaltungs-Bezeichnung 2',
                                 `VAtype` int(3) unsigned DEFAULT NULL COMMENT 'Veranstaltungs-Type',
                                 `VAdatumVon` date DEFAULT NULL COMMENT 'Veranstaltung Datum von',
                                 `VAzeitVon` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Veranstaltung Zeit von',
                                 `VAdatumBis` date DEFAULT NULL COMMENT 'Veranstaltung Datum bis',
                                 `VAzeitBis` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Veranstaltung Zeit bis',
                                 `VAeinheiten` int(10) unsigned DEFAULT NULL COMMENT 'Einheiten',
                                 `VAeinheitenbem` text DEFAULT NULL COMMENT 'Einheiten Bemerkung',
                                 `VAseminarform` text DEFAULT NULL COMMENT 'Seminarform',
                                 `VAort` text DEFAULT NULL COMMENT 'Veranstaltungs-Ort',
                                 `VAkosten` decimal(7,2) DEFAULT NULL COMMENT 'Veranstaltungs-Kosten',
                                 `VAkostenbem` text DEFAULT NULL COMMENT 'Kosten Bemerkung',
                                 `VAabschluss` text DEFAULT NULL COMMENT 'Abschluss',
                                 `VAkurszeiten` text DEFAULT NULL COMMENT 'Kurszeiten',
                                 `VAauftraggeber` int(10) unsigned DEFAULT NULL COMMENT 'Auftraggeber',
                                 `VAinhalte` text DEFAULT NULL COMMENT 'Inhalte',
                                 `VAvoraussetzung` text DEFAULT NULL COMMENT 'Voraussetzungen für Teilnahme',
                                 `VAbemerk` text DEFAULT NULL COMMENT 'Bemerkung',
                                 `VAvorlage` enum('n','y') DEFAULT NULL COMMENT 'Vorlage',
                                 `VAstatus` int(3) unsigned DEFAULT NULL COMMENT 'Veranstaltungs-Status',
                                 `VAloek` enum('n','y') DEFAULT 'n' COMMENT 'Loeschkennzeichen',
                                 `VAaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Aenderungsdatum',
                                 `VAaeuser` char(35) DEFAULT NULL,
                                 `VAerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                 `VAeruser` char(35) DEFAULT NULL,
                                 PRIMARY KEY (`VAnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VERMITTLUNG_ARBEITSMODELLE`
--

DROP TABLE IF EXISTS `VERMITTLUNG_ARBEITSMODELLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VERMITTLUNG_ARBEITSMODELLE` (
                                              `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                              `bezeichnung` char(100) DEFAULT NULL,
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VERSICHERUNGSTRAEGER`
--

DROP TABLE IF EXISTS `VERSICHERUNGSTRAEGER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VERSICHERUNGSTRAEGER` (
                                        `ID` int(5) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `ordnungsnummer` int(3) NOT NULL COMMENT 'ordnungsnummer',
                                        `TYP_ID` int(3) NOT NULL COMMENT 'TYP_ID',
                                        `name_kurz` varchar(20) DEFAULT NULL COMMENT 'name_kurz',
                                        `name_lang` varchar(255) DEFAULT NULL COMMENT 'name_lang',
                                        PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VERSICHERUNGSTRAEGER_TYP`
--

DROP TABLE IF EXISTS `VERSICHERUNGSTRAEGER_TYP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VERSICHERUNGSTRAEGER_TYP` (
                                            `ID` int(3) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                            `name_kurz` varchar(20) DEFAULT NULL COMMENT 'name_kurz',
                                            `name_lang` varchar(255) DEFAULT NULL COMMENT 'name_lang',
                                            PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VORDIENSTZEITEN`
--

DROP TABLE IF EXISTS `VORDIENSTZEITEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VORDIENSTZEITEN` (
                                   `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                   `PERSONALBOGEN_id` int(6) unsigned NOT NULL DEFAULT 0,
                                   `ARBEITSVERTRAG_id` int(6) unsigned DEFAULT NULL,
                                   `firma` varchar(100) DEFAULT NULL,
                                   `taetigkeit` varchar(100) DEFAULT NULL,
                                   `nachweis` int(1) DEFAULT NULL,
                                   `wochenstd` decimal(10,2) DEFAULT NULL,
                                   `von` date DEFAULT NULL,
                                   `bis` date DEFAULT NULL,
                                   `anstellungsart` enum('fix','frei') DEFAULT NULL,
                                   `erfahrungsart` enum('facheinschl','allgemein') DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15074 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WARENBEWEGUNG`
--

DROP TABLE IF EXISTS `WARENBEWEGUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WARENBEWEGUNG` (
                                 `WEnummer` int(6) NOT NULL AUTO_INCREMENT COMMENT 'lfd. Nummer',
                                 `WEtyp` enum('e','a','u','b') DEFAULT NULL COMMENT 'Typ (Eingang, Ausgang, Umbuchung, Bedarfsmeldung)',
                                 `WEbezeichnung` char(80) DEFAULT NULL COMMENT 'Wareneingangs-Bezeichnung',
                                 `WEdatum` date DEFAULT NULL COMMENT 'Eingangs-Datum',
                                 `WElieferantnr` int(6) DEFAULT NULL COMMENT 'Lieferant',
                                 `WElieferantnr_old` int(6) DEFAULT NULL COMMENT 'Lieferant ALT',
                                 `WElieferantbez` char(80) DEFAULT NULL COMMENT 'Lieferanten-Bezeichnung',
                                 `WElsnummer` char(15) DEFAULT NULL COMMENT 'Lieferschein-Nummer',
                                 `WEstandortvon` int(6) DEFAULT NULL COMMENT 'Standort-ID von',
                                 `WEraumvon` int(6) DEFAULT NULL COMMENT 'Raum-ID von',
                                 `WEstandortnach` int(10) unsigned DEFAULT NULL COMMENT 'Standort-ID nach',
                                 `WEraumnach` int(10) unsigned DEFAULT NULL COMMENT 'Raum-ID nach',
                                 `WEtransportdurch` enum('it','ei') DEFAULT 'it',
                                 `WEtransportbemerkung` char(150) DEFAULT NULL COMMENT 'Transport Bemerkung',
                                 `SEMINAR_SMnr` int(9) DEFAULT NULL COMMENT 'Seminar-ID',
                                 `WEdatumvon` date DEFAULT NULL,
                                 `WEdatumbis` date DEFAULT NULL,
                                 `WEbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                                 `WEabgangsart` int(6) DEFAULT NULL COMMENT 'Abgangsart',
                                 `WEabgangsgrund` int(6) DEFAULT NULL COMMENT 'Abgangsgrund',
                                 `WEfreigabedurch` char(35) DEFAULT NULL COMMENT 'Freigabe durch',
                                 `WEfreigabedatum` date DEFAULT NULL,
                                 `WEbearbeitungsstatus` int(1) NOT NULL,
                                 `WEtransportscheinnr` varchar(20) NOT NULL COMMENT '@LÖSCHEN am 01.09.2012',
                                 `WEbuchungsnummer` int(10) unsigned NOT NULL COMMENT 'Buchungsnummer für Scanner',
                                 `WEstatus` int(1) DEFAULT 0,
                                 `WErefnr` int(6) DEFAULT NULL,
                                 `WEerfasser` char(35) DEFAULT NULL COMMENT 'Erfassung durch',
                                 `WEloek` enum('n','y') NOT NULL,
                                 `WEaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                 `WEaeuser` char(35) DEFAULT NULL COMMENT 'Änderungsbenutzer',
                                 `WEerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                                 `WEeruser` char(35) DEFAULT NULL COMMENT 'Erfassungsbenutzer',
                                 PRIMARY KEY (`WEnummer`)
) ENGINE=InnoDB AUTO_INCREMENT=5598 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='loek nicht löschen!';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WARENBEWEGUNG_POS`
--

DROP TABLE IF EXISTS `WARENBEWEGUNG_POS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WARENBEWEGUNG_POS` (
                                     `WPnr` int(6) NOT NULL AUTO_INCREMENT COMMENT 'Lfd Nummer',
                                     `WARENBEWEGUNG_WEnummer` int(6) NOT NULL COMMENT 'Warenkopf Nummer',
                                     `RESSOURCE_RSresid` int(6) NOT NULL COMMENT 'Ressourcen Resid',
                                     `WPstandortvon` int(6) DEFAULT NULL,
                                     `WPraumvon` int(6) DEFAULT NULL,
                                     `WPstandortnach` int(6) DEFAULT NULL,
                                     `WPraumnach` int(6) DEFAULT NULL,
                                     `WPinvnr` char(20) DEFAULT NULL,
                                     `WPresart` int(6) DEFAULT NULL,
                                     `WPrestyp` int(6) DEFAULT NULL,
                                     `WPreskategorie` int(6) DEFAULT NULL,
                                     `WPresklasse` enum('e','m') DEFAULT NULL,
                                     `WPanzahl` int(10) unsigned DEFAULT NULL COMMENT 'Anzahl',
                                     `WPanzahlbestellt` int(3) NOT NULL,
                                     `WPanzahlgeliefert` int(3) NOT NULL,
                                     `WPbestelltvon` varchar(35) NOT NULL,
                                     `WPgrund` char(180) DEFAULT NULL COMMENT 'Abgangsgrund',
                                     `WPbemerkung` text DEFAULT NULL,
                                     `WPdatumvon` date DEFAULT NULL COMMENT 'Datum von',
                                     `WPdatumbis` date DEFAULT NULL COMMENT 'Datum bis',
                                     `WPstatus` int(1) NOT NULL,
                                     `WPaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                     `WPaeuser` char(35) DEFAULT NULL COMMENT 'Änderungsbenutzer',
                                     `WPerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                     `WPeruser` char(35) DEFAULT NULL COMMENT 'Erstellungsbenutzer',
                                     PRIMARY KEY (`WPnr`),
                                     KEY `WAREN_POS_FKIndex1` (`RESSOURCE_RSresid`),
                                     KEY `WAREN_POS_FKIndex2` (`WARENBEWEGUNG_WEnummer`)
) ENGINE=InnoDB AUTO_INCREMENT=45496 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WD_VK`
--

DROP TABLE IF EXISTS `WD_VK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WD_VK` (
                         `WIEDERVORLAGE_WVnr` int(6) unsigned NOT NULL,
                         `NOTIZEN_NZid` int(6) unsigned NOT NULL,
                         `NOTIZEN_ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                         `NOTIZEN_ABTEILUNG_ABnr` int(3) unsigned NOT NULL,
                         `DOK_AD_sySTORE_STnr` int(9) unsigned NOT NULL,
                         `DOK_AD_ADRESSE_ADadnr` int(6) unsigned NOT NULL,
                         `DOK_AD_ABTEILUNG_ABnr` int(3) unsigned NOT NULL,
                         PRIMARY KEY (`WIEDERVORLAGE_WVnr`,`NOTIZEN_NZid`,`NOTIZEN_ADRESSE_ADadnr`,`NOTIZEN_ABTEILUNG_ABnr`,`DOK_AD_sySTORE_STnr`,`DOK_AD_ADRESSE_ADadnr`,`DOK_AD_ABTEILUNG_ABnr`),
                         KEY `Table_53_FKIndex1` (`NOTIZEN_NZid`,`NOTIZEN_ABTEILUNG_ABnr`,`NOTIZEN_ADRESSE_ADadnr`),
                         KEY `Table_53_FKIndex2` (`WIEDERVORLAGE_WVnr`),
                         KEY `WD_VK_FKIndex3` (`DOK_AD_sySTORE_STnr`,`DOK_AD_ABTEILUNG_ABnr`,`DOK_AD_ADRESSE_ADadnr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WIEDEREINSTIEGSGRUND`
--

DROP TABLE IF EXISTS `WIEDEREINSTIEGSGRUND`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WIEDEREINSTIEGSGRUND` (
                                        `WGnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                        `WGindex` int(3) unsigned DEFAULT NULL COMMENT 'Index um die Werte in einer Liste sortieren zu können',
                                        `WGbezeichnung` varchar(100) NOT NULL,
                                        `WGbeschreibung` text DEFAULT NULL,
                                        PRIMARY KEY (`WGnr`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Der Grund, warum ein Teilnehmer nach einer Unterbrechung ein';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WIEDERVORLAGE`
--

DROP TABLE IF EXISTS `WIEDERVORLAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WIEDERVORLAGE` (
                                 `WVnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                 `ABTEILUNG_ABnr` int(3) unsigned NOT NULL,
                                 `WVvon` date DEFAULT NULL,
                                 `WVvont` time /* mariadb-5.3 */ DEFAULT NULL,
                                 `WVfrist` date DEFAULT NULL,
                                 `WVfristt` time /* mariadb-5.3 */ DEFAULT NULL,
                                 `WVintervall` int(6) unsigned DEFAULT NULL,
                                 `WVuser` char(15) DEFAULT NULL,
                                 `WVstatus` int(1) unsigned DEFAULT NULL,
                                 `WVprioritaet` int(1) unsigned DEFAULT NULL,
                                 `WVtitel` char(60) DEFAULT NULL,
                                 `WVbeschreibung` text DEFAULT NULL,
                                 `WVemailinfo` int(1) unsigned DEFAULT NULL,
                                 `WVloek` enum('n','y') DEFAULT 'n',
                                 `WVerda` date DEFAULT NULL,
                                 `WVeruser` char(35) DEFAULT NULL,
                                 `WVaeda` date DEFAULT NULL,
                                 `WVaeuser` char(35) DEFAULT NULL,
                                 PRIMARY KEY (`WVnr`),
                                 KEY `WIEDERVORLAGE_FKIndex1` (`ABTEILUNG_ABnr`)
) ENGINE=InnoDB AUTO_INCREMENT=427 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WORKSHOP`
--

DROP TABLE IF EXISTS `WORKSHOP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WORKSHOP` (
                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                            `von` date DEFAULT NULL,
                            `bis` date DEFAULT NULL,
                            `titel` varchar(255) DEFAULT NULL,
                            `inhalt` varchar(255) DEFAULT NULL,
                            `buchungssperre` tinyint(1) DEFAULT NULL,
                            `max_anz_teilnehmer` int(6) unsigned DEFAULT NULL,
                            `vortragender_id` int(6) unsigned DEFAULT NULL,
                            `trainerbelegung_aktiv` tinyint(1) DEFAULT NULL COMMENT 'Trainer-Belegungen automatisch aus WS-Stundenplan generieren',
                            `mo_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `mo_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            `di_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `di_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            `mi_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `mi_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            `do_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `do_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            `fr_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `fr_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_mo_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_mo_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_di_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_di_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_mi_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_mi_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_do_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_do_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_fr_von` time /* mariadb-5.3 */ DEFAULT NULL,
                            `pause_fr_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WS_ABWESENHEIT`
--

DROP TABLE IF EXISTS `WS_ABWESENHEIT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WS_ABWESENHEIT` (
                                  `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                  `WS_TN_id` int(6) unsigned DEFAULT NULL,
                                  `TNSTATUS_id` int(6) unsigned DEFAULT NULL,
                                  `datum` date DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `WS_TN_id` (`WS_TN_id`,`datum`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WS_STUNDENPLANABWEICHUNG`
--

DROP TABLE IF EXISTS `WS_STUNDENPLANABWEICHUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WS_STUNDENPLANABWEICHUNG` (
                                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                            `WORKSHOP_id` int(6) unsigned DEFAULT NULL,
                                            `ist_frei` tinyint(1) DEFAULT NULL,
                                            `datum` date DEFAULT NULL,
                                            `von` time /* mariadb-5.3 */ DEFAULT NULL,
                                            `bis` time /* mariadb-5.3 */ DEFAULT NULL,
                                            `pause_von` time /* mariadb-5.3 */ DEFAULT NULL,
                                            `pause_bis` time /* mariadb-5.3 */ DEFAULT NULL,
                                            PRIMARY KEY (`id`),
                                            UNIQUE KEY `WORKSHOP_id` (`WORKSHOP_id`,`datum`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WS_TN`
--

DROP TABLE IF EXISTS `WS_TN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WS_TN` (
                         `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                         `WORKSHOP_id` int(6) unsigned DEFAULT NULL,
                         `TEILNEHMER_id` int(6) unsigned DEFAULT NULL,
                         `geplante_termine_aktiv` tinyint(1) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=182 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WS_TN_GEPLANTE_TERMINE`
--

DROP TABLE IF EXISTS `WS_TN_GEPLANTE_TERMINE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WS_TN_GEPLANTE_TERMINE` (
                                          `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                          `WS_TN_id` int(6) unsigned DEFAULT NULL,
                                          `datum` date DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `XLS_ADD`
--

DROP TABLE IF EXISTS `XLS_ADD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `XLS_ADD` (
                           `XAid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID des Wertes',
                           `XAtyp` enum('val','txt') DEFAULT 'txt' COMMENT 'Was soll eingefÃ¼gt werden',
                           `KYnr` int(6) unsigned NOT NULL COMMENT 'VorlagenID',
                           `XAws` int(4) unsigned DEFAULT 0 COMMENT 'Nummer des Worksheets',
                           `XArow` int(4) unsigned DEFAULT NULL COMMENT 'Nummer der Reihe',
                           `XAcol` int(4) unsigned DEFAULT NULL COMMENT 'Nummer der Spalte',
                           `XAval` varchar(100) DEFAULT '' COMMENT 'Wert der eingefÃ¼gt werden soll',
                           PRIMARY KEY (`XAid`)
) ENGINE=MyISAM AUTO_INCREMENT=52 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ZERTIFZIZIERUNGSPRODUKTE`
--

DROP TABLE IF EXISTS `ZERTIFZIZIERUNGSPRODUKTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ZERTIFZIZIERUNGSPRODUKTE` (
                                            `id` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                            `bezeichnung` char(255) DEFAULT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `laravel_failed_jobs`
--

DROP TABLE IF EXISTS `laravel_failed_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `laravel_failed_jobs` (
                                       `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                                       `connection` text NOT NULL,
                                       `queue` text NOT NULL,
                                       `payload` longtext NOT NULL,
                                       `exception` longtext NOT NULL,
                                       `failed_at` timestamp /* mariadb-5.3 */ NOT NULL DEFAULT current_timestamp(),
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1619 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `laravel_job_control`
--

DROP TABLE IF EXISTS `laravel_job_control`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `laravel_job_control` (
                                       `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                                       `job_name` varchar(255) NOT NULL,
                                       `payload` text DEFAULT NULL,
                                       `attachment` mediumblob DEFAULT NULL,
                                       `created_at` datetime /* mariadb-5.3 */ NOT NULL,
                                       `dispatched_at` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       KEY `laravel_job_control_dispatched_at_index` (`dispatched_at`)
) ENGINE=InnoDB AUTO_INCREMENT=149957 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `laravel_jobs`
--

DROP TABLE IF EXISTS `laravel_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `laravel_jobs` (
                                `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                                `queue` varchar(255) NOT NULL,
                                `payload` longtext NOT NULL,
                                `attempts` tinyint(3) unsigned NOT NULL,
                                `reserved_at` int(10) unsigned DEFAULT NULL,
                                `available_at` int(10) unsigned NOT NULL,
                                `created_at` int(10) unsigned NOT NULL,
                                PRIMARY KEY (`id`),
                                KEY `laravel_jobs_queue_index` (`queue`(191))
) ENGINE=InnoDB AUTO_INCREMENT=94198 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `laravel_moxis_leistung_processes`
--

DROP TABLE IF EXISTS `laravel_moxis_leistung_processes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `laravel_moxis_leistung_processes` (
                                                    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                                                    `moxis_process_status_id` bigint(20) unsigned NOT NULL,
                                                    `process_type` enum('spesen','pep') NOT NULL,
                                                    `constituent_adresse_id` int(10) unsigned NOT NULL,
                                                    `year_month` int(10) unsigned NOT NULL,
                                                    `kostenstellen_gruppe` varchar(4) DEFAULT NULL,
                                                    `is_schluesselkraft` tinyint(1) NOT NULL DEFAULT 0,
                                                    `constituent_approval_enabled` tinyint(1) NOT NULL DEFAULT 0,
                                                    `iteration_0_cfg_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `iteration_0_act_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `iteration_1_cfg_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `iteration_1_act_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `iteration_2_cfg_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `iteration_2_act_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `iteration_3_cfg_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `iteration_3_act_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `iteration_4_cfg_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `iteration_4_act_adresse_id` int(10) unsigned DEFAULT NULL,
                                                    `replaced_by_moxis_leistung_process_id` bigint(20) unsigned DEFAULT NULL,
                                                    `created_at` timestamp /* mariadb-5.3 */ NULL DEFAULT NULL,
                                                    `updated_at` timestamp /* mariadb-5.3 */ NULL DEFAULT NULL,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE KEY `laravel_moxis_leistung_processes_moxis_process_status_id_unique` (`moxis_process_status_id`),
                                                    UNIQUE KEY `laravel_moxis_leistung_processes_unq1` (`process_type`,`constituent_adresse_id`,`year_month`,`replaced_by_moxis_leistung_process_id`)
) ENGINE=InnoDB AUTO_INCREMENT=102265 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `laravel_moxis_process_status`
--

DROP TABLE IF EXISTS `laravel_moxis_process_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `laravel_moxis_process_status` (
                                                `id` bigint(20) unsigned NOT NULL,
                                                `job_control_id` bigint(20) unsigned NOT NULL,
                                                `state` varchar(255) DEFAULT NULL,
                                                `expires_at` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                `iterations` tinyint(3) unsigned DEFAULT NULL,
                                                `current_iteration` tinyint(3) unsigned DEFAULT NULL,
                                                `created_at` timestamp /* mariadb-5.3 */ NULL DEFAULT NULL,
                                                `updated_at` timestamp /* mariadb-5.3 */ NULL DEFAULT NULL,
                                                `ended_at` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                PRIMARY KEY (`id`),
                                                UNIQUE KEY `laravel_moxis_process_status_job_control_id_unique` (`job_control_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
                       `LGnr` bigint(11) NOT NULL AUTO_INCREMENT,
                       `LGanwendung` varchar(20) DEFAULT NULL,
                       `mtype` varchar(10) DEFAULT NULL,
                       `ctime` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum-Zeit Erstellung',
                       `session_username` varchar(35) DEFAULT NULL,
                       `url` varchar(200) DEFAULT NULL COMMENT 'URL',
                       `query` varchar(200) DEFAULT NULL COMMENT 'Query',
                       `queryPost` text DEFAULT NULL COMMENT 'Query-Post',
                       `clientip` varchar(50) DEFAULT NULL COMMENT 'Client-IP-Adresse',
                       `exectime` float DEFAULT NULL COMMENT 'Ausführungsdauer',
                       `user_agent` varchar(250) DEFAULT NULL COMMENT 'User-Agent',
                       `msg` text DEFAULT NULL COMMENT 'Message',
                       PRIMARY KEY (`LGnr`),
                       KEY `ctime` (`ctime`)
) ENGINE=MyISAM AUTO_INCREMENT=217598348 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logfile`
--

DROP TABLE IF EXISTS `logfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logfile` (
                           `LFnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'laufende Nummer',
                           `BNuserid` varchar(35) DEFAULT NULL,
                           `LFloginIP` varchar(16) DEFAULT NULL COMMENT 'Login - IP',
                           `LFlogin` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum-Zeit Log-in',
                           `LFlogout` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum-Zeit Log-out',
                           `LFlogoutok` char(1) NOT NULL DEFAULT 'n',
                           `LFpages` int(6) NOT NULL DEFAULT 0,
                           `LFBrowserID` varchar(255) DEFAULT NULL,
                           `LFsessionid` char(32) DEFAULT NULL,
                           PRIMARY KEY (`LFnr`),
                           KEY `userid_loginIP` (`BNuserid`,`LFloginIP`)
) ENGINE=MyISAM AUTO_INCREMENT=684635 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `migrations`
--

DROP TABLE IF EXISTS `migrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `migrations` (
                              `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                              `migration` varchar(255) NOT NULL,
                              `batch` int(11) NOT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reportingLog`
--

DROP TABLE IF EXISTS `reportingLog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reportingLog` (
                                `repnr` int(3) unsigned NOT NULL AUTO_INCREMENT,
                                `priority` enum('0','1','2','3') DEFAULT NULL,
                                `session_username` varchar(15) DEFAULT NULL,
                                `zuname` varchar(20) DEFAULT NULL,
                                `vorname` varchar(20) DEFAULT NULL,
                                `email` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL,
                                `rtype` enum('fehler','erweiterung','verbesserung') DEFAULT NULL,
                                `beschreibung` text DEFAULT NULL,
                                `rurl` varchar(200) DEFAULT NULL,
                                `rurltime` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `clientip` varchar(50) DEFAULT NULL,
                                `user_agent` varchar(250) DEFAULT NULL,
                                `connecttype` enum('modem','adslcable','andere') DEFAULT NULL,
                                `erledigt` enum('n','y') DEFAULT NULL,
                                `erledigtdate` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                `erledigtbemerk` text DEFAULT NULL,
                                PRIMARY KEY (`repnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `syDOCCTL`
--

DROP TABLE IF EXISTS `syDOCCTL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syDOCCTL` (
                            `DCnr` int(6) unsigned NOT NULL AUTO_INCREMENT,
                            `sySTORE_STnr` int(9) unsigned NOT NULL DEFAULT 0,
                            `DCdocname` char(128) DEFAULT NULL,
                            `DCsecid` char(32) DEFAULT NULL,
                            `DChash` char(32) DEFAULT NULL,
                            `DCmodus` char(1) DEFAULT NULL,
                            `DCbenutzer` char(15) DEFAULT NULL,
                            `DCbenutzerip` char(16) DEFAULT NULL,
                            `DCtransferart` char(5) DEFAULT NULL,
                            `DChostname` char(40) DEFAULT NULL,
                            `DChostbenutzer` char(20) DEFAULT NULL,
                            `DChostbenutzerpw` char(20) DEFAULT NULL,
                            `DCts_checkout` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `DCts_checkin` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `DCts_transbeginn` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `DCstatustrans` char(1) DEFAULT NULL,
                            `DCloek` enum('n','y') DEFAULT 'n',
                            PRIMARY KEY (`DCnr`)
) ENGINE=MyISAM AUTO_INCREMENT=53 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `syLOCATION`
--

DROP TABLE IF EXISTS `syLOCATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syLOCATION` (
                              `SLnr` int(3) unsigned NOT NULL AUTO_INCREMENT,
                              `SLdbname` char(60) DEFAULT NULL,
                              `SLtblname` char(60) DEFAULT NULL,
                              `SLlastupdate` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `SLanzahldocs` int(6) unsigned DEFAULT NULL,
                              `SLsize` decimal(10,2) NOT NULL DEFAULT 0.00,
                              `SLstatus` char(1) DEFAULT NULL,
                              `SLaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `SLaeuser` char(35) DEFAULT NULL,
                              `SLerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `SLeruser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`SLnr`)
) ENGINE=InnoDB AUTO_INCREMENT=55167 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sySTORE`
--

DROP TABLE IF EXISTS `sySTORE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sySTORE` (
                           `STnr` int(9) unsigned NOT NULL AUTO_INCREMENT,
                           `syLOCATION_SLnr` int(3) unsigned NOT NULL DEFAULT 0,
                           `STtitel` varchar(255) DEFAULT NULL,
                           `STdescription` text DEFAULT NULL,
                           `STdatum` date DEFAULT NULL,
                           `STfilename` varchar(256) DEFAULT NULL,
                           `STfilesize` int(6) unsigned DEFAULT NULL,
                           `STfiletype` varchar(10) DEFAULT NULL,
                           `SThash` varchar(32) DEFAULT NULL,
                           `STrevision` int(3) unsigned DEFAULT NULL,
                           `STcheckout` enum('n','y') DEFAULT 'n',
                           `STstoremode` enum('b','f') DEFAULT 'b',
                           `STloek` enum('n','y') DEFAULT 'n',
                           `STaeda` datetime DEFAULT NULL,
                           `STaeuser` char(35) DEFAULT NULL,
                           `STerda` datetime DEFAULT NULL,
                           `STeruser` char(35) DEFAULT NULL,
                           PRIMARY KEY (`STnr`),
                           KEY `sySTORE_FKIndex1` (`syLOCATION_SLnr`)
) ENGINE=MyISAM AUTO_INCREMENT=719696 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `syfSTORE`
--

DROP TABLE IF EXISTS `syfSTORE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syfSTORE` (
                            `STnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Store-ID',
                            `filename` varchar(128) DEFAULT NULL COMMENT 'File-Name',
                            `filesize` int(6) unsigned DEFAULT NULL COMMENT 'File-Grösse',
                            `filetype` varchar(10) DEFAULT NULL COMMENT 'File-Type',
                            `description` text DEFAULT NULL COMMENT 'File-Beschreibung',
                            `datablob` longblob DEFAULT NULL COMMENT 'BLOB',
                            `systemname` varchar(50) DEFAULT NULL COMMENT 'Datei-Name lokale Sicherung',
                            `STloek` enum('n','y') DEFAULT NULL COMMENT 'Löschkennzeichen',
                            `STaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                            `STaeuser` char(35) DEFAULT NULL,
                            `STerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erfassungsdatum',
                            `STeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`STnr`)
) ENGINE=MyISAM AUTO_INCREMENT=4263 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `syfTOKEN`
--

DROP TABLE IF EXISTS `syfTOKEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `syfTOKEN` (
                            `TKstr` char(15) NOT NULL,
                            `TKuser` char(35) NOT NULL DEFAULT '',
                            `TKcategory` char(10) DEFAULT NULL,
                            `TKexpiration` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `TKonetime` enum('n','y') DEFAULT NULL,
                            `TKused` enum('n','y') DEFAULT NULL,
                            `TKverified` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `TKverifiedip` char(15) DEFAULT NULL,
                            `TKaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `TKaeuser` char(35) DEFAULT NULL,
                            `TKerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                            `TKeruser` char(35) DEFAULT NULL,
                            PRIMARY KEY (`TKstr`,`TKuser`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tc_GARBAGE`
--

DROP TABLE IF EXISTS `tc_GARBAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_GARBAGE` (
                              `GCid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                              `GCtyp` char(3) DEFAULT NULL,
                              `GCuserid` varchar(15) DEFAULT NULL,
                              `GCfilepath` varchar(128) DEFAULT NULL,
                              `GCfilename` varchar(128) DEFAULT NULL,
                              `GCvaliduntil` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `GCloek` enum('n','y') DEFAULT 'n',
                              `GCerda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `GCeruser` char(35) DEFAULT NULL,
                              `GCaeda` datetime /* mariadb-5.3 */ DEFAULT NULL,
                              `GCaeuser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`GCid`)
) ENGINE=MyISAM AUTO_INCREMENT=263918 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tc_ONLINE`
--

DROP TABLE IF EXISTS `tc_ONLINE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_ONLINE` (
                             `session_id` varchar(40) NOT NULL DEFAULT '' COMMENT 'Session-ID',
                             `session_username` varchar(35) DEFAULT NULL,
                             `activity` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Letzt Aktivitaet',
                             PRIMARY KEY (`session_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tc_SECERG`
--

DROP TABLE IF EXISTS `tc_SECERG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_SECERG` (
                             `BNuserid` varchar(35) NOT NULL,
                             `SFnr` int(3) unsigned NOT NULL DEFAULT 0 COMMENT 'Funktions-Nr',
                             `SLrecht` varchar(15) DEFAULT NULL COMMENT 'Rechtestring',
                             PRIMARY KEY (`BNuserid`,`SFnr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tc_SECFKT`
--

DROP TABLE IF EXISTS `tc_SECFKT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_SECFKT` (
                             `SFnr` int(3) unsigned NOT NULL AUTO_INCREMENT,
                             `SFbez` varchar(20) DEFAULT NULL,
                             `SFmnur` enum('y','n') DEFAULT 'y',
                             `SFmrefnr` int(3) unsigned DEFAULT NULL,
                             `SFmorder` int(2) unsigned DEFAULT NULL,
                             `SFmlink` varchar(150) DEFAULT NULL,
                             `SFmicon` varchar(50) DEFAULT NULL,
                             `SFrstr1` varchar(20) DEFAULT NULL,
                             `SFrstr2` varchar(20) DEFAULT NULL,
                             `SFrstr3` varchar(20) DEFAULT NULL,
                             `SFrstr4` varchar(20) DEFAULT NULL,
                             `SFrstr5` varchar(20) DEFAULT NULL,
                             `SFrstr6` varchar(20) DEFAULT NULL,
                             `SFrstr7` varchar(20) DEFAULT NULL,
                             `SFrstr8` varchar(20) DEFAULT NULL,
                             `SFrstr9` varchar(20) DEFAULT NULL,
                             `SFrstr10` varchar(20) DEFAULT NULL,
                             `SFrstr11` varchar(20) DEFAULT NULL,
                             `SFrstr12` varchar(20) DEFAULT NULL,
                             `SFrstr13` varchar(20) DEFAULT NULL,
                             `SFrstr14` varchar(20) DEFAULT NULL,
                             `SFrstr15` varchar(20) DEFAULT NULL,
                             PRIMARY KEY (`SFnr`)
) ENGINE=MyISAM AUTO_INCREMENT=907 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tc_logfile`
--

DROP TABLE IF EXISTS `tc_logfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_logfile` (
                              `LFnr` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'laufende Nummer',
                              `BNuserid` varchar(35) DEFAULT NULL,
                              `LFloginIP` varchar(16) DEFAULT NULL COMMENT 'Login - IP',
                              `LFlogin` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum-Zeit Log-in',
                              `LFlogout` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum-Zeit Log-out',
                              `LFlogoutok` char(1) NOT NULL DEFAULT 'n',
                              `LFpages` int(6) NOT NULL DEFAULT 0,
                              `LFBrowserID` varchar(255) DEFAULT NULL,
                              `LFsessionid` char(32) DEFAULT NULL,
                              PRIMARY KEY (`LFnr`),
                              KEY `userid_loginIP` (`BNuserid`,`LFloginIP`)
) ENGINE=MyISAM AUTO_INCREMENT=2286981 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uAUSWERTUNG_WORKTABL20080116_121335`
--

DROP TABLE IF EXISTS `uAUSWERTUNG_WORKTABL20080116_121335`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uAUSWERTUNG_WORKTABL20080116_121335` (
                                                       `BFid` int(6) unsigned NOT NULL AUTO_INCREMENT,
                                                       `BEid` int(6) unsigned NOT NULL,
                                                       `BFip` char(20) DEFAULT NULL,
                                                       `BFverbraucht` enum('n','y') DEFAULT 'n',
                                                       `BFvon` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                       `BFbis` datetime /* mariadb-5.3 */ DEFAULT NULL,
                                                       `BFgeschlecht` enum('m','w') DEFAULT NULL,
                                                       `Kostenstelle` char(40) DEFAULT NULL,
                                                       `Projekt` char(40) DEFAULT NULL,
                                                       `Seminar` char(40) DEFAULT NULL,
                                                       `1_Fachwissen/Qualifikation_1` int(5) DEFAULT NULL,
                                                       `2_Fachwissen/Qualifikation_2` int(5) DEFAULT NULL,
                                                       `3_Fachwissen/Qualifikation_3` int(5) DEFAULT NULL,
                                                       `4_Fachwissen/Qualifikation_4` int(5) DEFAULT NULL,
                                                       `5_Fachwissen/Qualifikation_5` int(5) DEFAULT NULL,
                                                       `6_Fachwissen/Qualifikation_6` int(5) DEFAULT NULL,
                                                       `7_Eingehen auf TN_1` int(5) DEFAULT NULL,
                                                       `8_Eingehen auf TN_2` int(5) DEFAULT NULL,
                                                       `9_Eingehen auf TN_3` int(5) DEFAULT NULL,
                                                       `10_Eingehen auf TN_4` int(5) DEFAULT NULL,
                                                       `11_Eingehen auf TN_5` int(5) DEFAULT NULL,
                                                       `12_Eingehen auf TN_6` int(5) DEFAULT NULL,
                                                       `13_Gestaltung des Unterricht_1` int(5) DEFAULT NULL,
                                                       `14_Gestaltung des Unterricht_2` int(5) DEFAULT NULL,
                                                       `15_Gestaltung des Unterricht_3` int(5) DEFAULT NULL,
                                                       `16_Gestaltung des Unterricht_4` int(5) DEFAULT NULL,
                                                       `17_Gestaltung des Unterricht_5` int(5) DEFAULT NULL,
                                                       `18_Gestaltung des Unterricht_6` int(5) DEFAULT NULL,
                                                       `19_Tempo des Unterrichts_1` int(5) DEFAULT NULL,
                                                       `20_Tempo des Unterrichts_2` int(5) DEFAULT NULL,
                                                       `21_Tempo des Unterrichts_3` int(5) DEFAULT NULL,
                                                       `22_Tempo des Unterrichts_4` int(5) DEFAULT NULL,
                                                       `23_Tempo des Unterrichts_5` int(5) DEFAULT NULL,
                                                       `24_Tempo des Unterrichts_6` int(5) DEFAULT NULL,
                                                       `25_Umfang des Lehrstoffes_1` int(5) DEFAULT NULL,
                                                       `26_Umfang des Lehrstoffes_2` int(5) DEFAULT NULL,
                                                       `27_Umfang des Lehrstoffes_3` int(5) DEFAULT NULL,
                                                       `28_Umfang des Lehrstoffes_4` int(5) DEFAULT NULL,
                                                       `29_Umfang des Lehrstoffes_5` int(5) DEFAULT NULL,
                                                       `30_Umfang des Lehrstoffes_6` int(5) DEFAULT NULL,
                                                       `31_Förderung selbständiges u_1` int(5) DEFAULT NULL,
                                                       `32_Förderung selbständiges u_2` int(5) DEFAULT NULL,
                                                       `33_Förderung selbständiges u_3` int(5) DEFAULT NULL,
                                                       `34_Förderung selbständiges u_4` int(5) DEFAULT NULL,
                                                       `35_Förderung selbständiges u_5` int(5) DEFAULT NULL,
                                                       `36_Förderung selbständiges u_6` int(5) DEFAULT NULL,
                                                       `37_Praxisbezug der dargebote_1` int(5) DEFAULT NULL,
                                                       `38_Praxisbezug der dargebote_2` int(5) DEFAULT NULL,
                                                       `39_Praxisbezug der dargebote_3` int(5) DEFAULT NULL,
                                                       `40_Praxisbezug der dargebote_4` int(5) DEFAULT NULL,
                                                       `41_Praxisbezug der dargebote_5` int(5) DEFAULT NULL,
                                                       `42_Praxisbezug der dargebote_6` int(5) DEFAULT NULL,
                                                       `43_Gleichstellung von Frauen_1` int(5) DEFAULT NULL,
                                                       `44_Gleichstellung von Frauen_2` int(5) DEFAULT NULL,
                                                       `45_Gleichstellung von Frauen_3` int(5) DEFAULT NULL,
                                                       `46_Gleichstellung von Frauen_4` int(5) DEFAULT NULL,
                                                       `47_Gleichstellung von Frauen_5` int(5) DEFAULT NULL,
                                                       `48_Gleichstellung von Frauen_6` int(5) DEFAULT NULL,
                                                       `49_Kursmaterial schriftliche_1` int(5) DEFAULT NULL,
                                                       `50_Kursmaterial schriftliche_2` int(5) DEFAULT NULL,
                                                       `51_Kursmaterial schriftliche_3` int(5) DEFAULT NULL,
                                                       `52_Kursmaterial schriftliche_4` int(5) DEFAULT NULL,
                                                       `53_Kursmaterial schriftliche_5` int(5) DEFAULT NULL,
                                                       `54_Kursmaterial schriftliche_6` int(5) DEFAULT NULL,
                                                       `55_Maschinen/Geräte/Werkzeug_1` int(5) DEFAULT NULL,
                                                       `56_Maschinen/Geräte/Werkzeug_2` int(5) DEFAULT NULL,
                                                       `57_Maschinen/Geräte/Werkzeug_3` int(5) DEFAULT NULL,
                                                       `58_Maschinen/Geräte/Werkzeug_4` int(5) DEFAULT NULL,
                                                       `59_Maschinen/Geräte/Werkzeug_5` int(5) DEFAULT NULL,
                                                       `60_Maschinen/Geräte/Werkzeug_6` int(5) DEFAULT NULL,
                                                       `61_Gestaltung der Unterricht_1` int(5) DEFAULT NULL,
                                                       `62_Gestaltung der Unterricht_2` int(5) DEFAULT NULL,
                                                       `63_Gestaltung der Unterricht_3` int(5) DEFAULT NULL,
                                                       `64_Gestaltung der Unterricht_4` int(5) DEFAULT NULL,
                                                       `65_Gestaltung der Unterricht_5` int(5) DEFAULT NULL,
                                                       `66_Gestaltung der Unterricht_6` int(5) DEFAULT NULL,
                                                       `67_Gestaltung der Pausen-/Au_1` int(5) DEFAULT NULL,
                                                       `68_Gestaltung der Pausen-/Au_2` int(5) DEFAULT NULL,
                                                       `69_Gestaltung der Pausen-/Au_3` int(5) DEFAULT NULL,
                                                       `70_Gestaltung der Pausen-/Au_4` int(5) DEFAULT NULL,
                                                       `71_Gestaltung der Pausen-/Au_5` int(5) DEFAULT NULL,
                                                       `72_Gestaltung der Pausen-/Au_6` int(5) DEFAULT NULL,
                                                       `73_Übungsmöglichkeiten_1` int(5) DEFAULT NULL,
                                                       `74_Übungsmöglichkeiten_2` int(5) DEFAULT NULL,
                                                       `75_Übungsmöglichkeiten_3` int(5) DEFAULT NULL,
                                                       `76_Übungsmöglichkeiten_4` int(5) DEFAULT NULL,
                                                       `77_Übungsmöglichkeiten_5` int(5) DEFAULT NULL,
                                                       `78_Übungsmöglichkeiten_6` int(5) DEFAULT NULL,
                                                       `79_Angebot von Pausen_1` int(5) DEFAULT NULL,
                                                       `80_Angebot von Pausen_2` int(5) DEFAULT NULL,
                                                       `81_Angebot von Pausen_3` int(5) DEFAULT NULL,
                                                       `82_Angebot von Pausen_4` int(5) DEFAULT NULL,
                                                       `83_Angebot von Pausen_5` int(5) DEFAULT NULL,
                                                       `84_Angebot von Pausen_6` int(5) DEFAULT NULL,
                                                       `85_(Gesamt-) Maßnahmendauer_1` int(5) DEFAULT NULL,
                                                       `86_(Gesamt-) Maßnahmendauer_2` int(5) DEFAULT NULL,
                                                       `87_(Gesamt-) Maßnahmendauer_3` int(5) DEFAULT NULL,
                                                       `88_(Gesamt-) Maßnahmendauer_4` int(5) DEFAULT NULL,
                                                       `89_(Gesamt-) Maßnahmendauer_5` int(5) DEFAULT NULL,
                                                       `90_(Gesamt-) Maßnahmendauer_6` int(5) DEFAULT NULL,
                                                       `91_Kinderbetreuung_1` int(5) DEFAULT NULL,
                                                       `92_Kinderbetreuung_2` int(5) DEFAULT NULL,
                                                       `93_Kinderbetreuung_3` int(5) DEFAULT NULL,
                                                       `94_Kinderbetreuung_4` int(5) DEFAULT NULL,
                                                       `95_Kinderbetreuung_5` int(5) DEFAULT NULL,
                                                       `96_Kinderbetreuung_6` int(5) DEFAULT NULL,
                                                       `97_Nutzen für die Berufliche_1` int(5) DEFAULT NULL,
                                                       `98_Nutzen für die Berufliche_2` int(5) DEFAULT NULL,
                                                       `99_Nutzen für die Berufliche_3` int(5) DEFAULT NULL,
                                                       `100_Nutzen für die Berufliche_4` int(5) DEFAULT NULL,
                                                       `101_Nutzen für die Berufliche_5` int(5) DEFAULT NULL,
                                                       `102_Nutzen für die Berufliche_6` int(5) DEFAULT NULL,
                                                       `103_Maßnahmenbenotung insgesa_1` int(5) DEFAULT NULL,
                                                       `104_Maßnahmenbenotung insgesa_2` int(5) DEFAULT NULL,
                                                       `105_Maßnahmenbenotung insgesa_3` int(5) DEFAULT NULL,
                                                       `106_Maßnahmenbenotung insgesa_4` int(5) DEFAULT NULL,
                                                       `107_Maßnahmenbenotung insgesa_5` int(5) DEFAULT NULL,
                                                       `108_Maßnahmenbenotung insgesa_6` int(5) DEFAULT NULL,
                                                       `109_gut gefallen_freie Textantwort möglich` text DEFAULT NULL,
                                                       `110_was sollte besser gemacht_freie Texteingabe möglich` text DEFAULT NULL,
                                                       PRIMARY KEY (`BFid`)
) ENGINE=MyISAM AUTO_INCREMENT=6629 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci COMMENT='Worktable Auswertung Befragungen - Tabelle nur Tmp';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uBEFRAGUNG`
--

DROP TABLE IF EXISTS `uBEFRAGUNG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uBEFRAGUNG` (
                              `BEid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Befragung ID',
                              `FBid` int(3) unsigned NOT NULL COMMENT 'Fragebogen ID',
                              `BEbez` char(128) DEFAULT NULL,
                              `BEbeginn` date DEFAULT NULL COMMENT 'Start der Befragung',
                              `BEende` date DEFAULT NULL COMMENT 'Ende der Befragung',
                              `BEstatus` int(1) DEFAULT NULL,
                              `BEloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                              `BEerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                              `BEeruser` char(35) DEFAULT NULL,
                              `BEaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                              `BEaeuser` char(35) DEFAULT NULL,
                              PRIMARY KEY (`BEid`),
                              KEY `FBid` (`FBid`)
) ENGINE=InnoDB AUTO_INCREMENT=31697 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uBEFRAGUNG_ANTWORT`
--

DROP TABLE IF EXISTS `uBEFRAGUNG_ANTWORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uBEFRAGUNG_ANTWORT` (
                                      `ANid` int(12) unsigned NOT NULL AUTO_INCREMENT,
                                      `BFid` int(6) unsigned NOT NULL COMMENT 'Befragung ID',
                                      `POid` int(3) unsigned NOT NULL COMMENT 'Position ID',
                                      `FAid` int(6) unsigned DEFAULT NULL COMMENT 'FrageAntwort ID',
                                      `ANtyp` char(20) DEFAULT NULL COMMENT 'Fragetyp',
                                      `ANwert_text` text DEFAULT NULL COMMENT 'Antwort-Text',
                                      `ANwert_zeit` time /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Antwort-Zeit',
                                      `ANwert_datum` date DEFAULT NULL COMMENT 'Antwort-Datum',
                                      `ANwert_zahl` int(5) unsigned DEFAULT NULL COMMENT 'Antwort-Zahl',
                                      `ANerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                      `ANeruser` char(35) DEFAULT NULL,
                                      `ANaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                      `ANaeuser` char(35) DEFAULT NULL,
                                      PRIMARY KEY (`ANid`,`BFid`),
                                      KEY `BFid` (`BFid`),
                                      KEY `POid_FAid` (`POid`,`FAid`)
) ENGINE=InnoDB AUTO_INCREMENT=4355861 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uBEFRAGUNG_BEFRAGTE`
--

DROP TABLE IF EXISTS `uBEFRAGUNG_BEFRAGTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uBEFRAGUNG_BEFRAGTE` (
                                       `BFid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'laufende Nr',
                                       `BEid` int(6) unsigned NOT NULL COMMENT 'Befragung ID',
                                       `BFpin` char(4) DEFAULT NULL COMMENT 'PIN',
                                       `BFip` char(20) DEFAULT NULL COMMENT 'Benutzer IP',
                                       `BFverbraucht` enum('n','y') DEFAULT 'n' COMMENT 'Befragung durchgeführt',
                                       `BFvon` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Befragung von',
                                       `BFbis` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Befragung bis',
                                       `BFgeschlecht` enum('m','w') DEFAULT NULL,
                                       `BFerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                       `BFeruser` char(35) DEFAULT NULL,
                                       `BFaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                       `BFaeuser` char(35) DEFAULT NULL,
                                       PRIMARY KEY (`BFid`),
                                       KEY `BEid_verbraucht` (`BEid`,`BFverbraucht`)
) ENGINE=InnoDB AUTO_INCREMENT=241055 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uFRAGEBOGEN`
--

DROP TABLE IF EXISTS `uFRAGEBOGEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uFRAGEBOGEN` (
                               `FBid` int(3) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Fragebogen ID',
                               `FBbez` char(128) DEFAULT NULL COMMENT 'Bezeichnung',
                               `FBtitel` text DEFAULT NULL COMMENT 'Fragebogen-Überschrift',
                               `FBsubtitel` text DEFAULT NULL COMMENT 'Fragebogen-Subüberschrift',
                               `FBhinweis` text DEFAULT NULL COMMENT 'Hinweise / Hilfe',
                               `FBbemerkung` text DEFAULT NULL COMMENT 'Bemerkung',
                               `FBende` text NOT NULL COMMENT 'Text am Befragungsende',
                               `FBemailtxt` text DEFAULT NULL,
                               `FBstyle` text DEFAULT NULL COMMENT 'CSS',
                               `FBsySTORE_STnr` int(9) DEFAULT NULL COMMENT 'Referenz-Store für das Fragebogenlogo',
                               `FBstatus` enum('a','i') DEFAULT 'a' COMMENT 'Status Fragebogen Aktiv / Inaktiv',
                               `FBanzeigeart` enum('a','k','e') DEFAULT 'a' COMMENT 'Anzeigeart: alle, kategorie, einzel',
                               `FBauslassen` enum('n','y') DEFAULT 'n' COMMENT 'Fragen auslassen',
                               `FBzurueck` enum('n','y') DEFAULT 'n' COMMENT 'Zurück Button anzeigen',
                               `FBauswgeschlecht` enum('n','y') NOT NULL DEFAULT 'n',
                               `FBloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                               `FBerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                               `FBeruser` char(35) DEFAULT NULL,
                               `FBaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                               `FBaeuser` char(35) DEFAULT NULL,
                               PRIMARY KEY (`FBid`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uFRAGEBOGEN_ANTWORT`
--

DROP TABLE IF EXISTS `uFRAGEBOGEN_ANTWORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uFRAGEBOGEN_ANTWORT` (
                                       `FAid` int(6) unsigned NOT NULL AUTO_INCREMENT COMMENT 'FrageAntwort ID',
                                       `POid` int(3) unsigned NOT NULL COMMENT 'Position ID',
                                       `FBid` int(3) unsigned NOT NULL COMMENT 'Fragebogen ID',
                                       `FAtyp` char(20) DEFAULT NULL COMMENT 'Zusatzfrage',
                                       `FAbez` char(100) NOT NULL COMMENT 'Antwort',
                                       `FAsortierung` int(5) unsigned DEFAULT NULL COMMENT 'Sortierreihenfolge',
                                       `FAwert` int(5) unsigned DEFAULT NULL COMMENT 'Antwortwert',
                                       `FAloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                                       `FAerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                       `FAeruser` char(35) DEFAULT NULL,
                                       `FAaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                       `FAaeuser` char(35) DEFAULT NULL,
                                       PRIMARY KEY (`FAid`,`POid`,`FBid`),
                                       KEY `POid_FBid` (`POid`,`FBid`)
) ENGINE=InnoDB AUTO_INCREMENT=1802 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uFRAGEBOGEN_KATEGORIE`
--

DROP TABLE IF EXISTS `uFRAGEBOGEN_KATEGORIE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uFRAGEBOGEN_KATEGORIE` (
                                         `KAid` int(2) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Kategorie ID',
                                         `FBid` int(3) unsigned NOT NULL COMMENT 'Fragebogen ID',
                                         `KAtitel` char(128) DEFAULT NULL COMMENT 'Titel',
                                         `KAkurz` char(40) DEFAULT NULL COMMENT 'Kurzbezeichnung für Auswertung',
                                         `KAhinweis` text DEFAULT NULL COMMENT 'Hinweise',
                                         `KAsortierung` int(5) unsigned DEFAULT NULL COMMENT 'Sortierungsreihenfolge',
                                         `KAtrennstrich` enum('n','y') DEFAULT 'y' COMMENT 'Trennstriche',
                                         `KAanzeigen` enum('n','y') DEFAULT 'y' COMMENT 'Anzeigen',
                                         `KAloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                                         `KAerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                         `KAeruser` char(35) DEFAULT NULL,
                                         `KAaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                         `KAaeuser` char(35) DEFAULT NULL,
                                         PRIMARY KEY (`KAid`,`FBid`),
                                         KEY `FBid` (`FBid`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uFRAGEBOGEN_POSITION`
--

DROP TABLE IF EXISTS `uFRAGEBOGEN_POSITION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uFRAGEBOGEN_POSITION` (
                                        `POid` int(3) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Position ID',
                                        `FBid` int(3) unsigned NOT NULL COMMENT 'Fragebogen ID',
                                        `KAid` int(2) unsigned NOT NULL COMMENT 'Kategorie ID',
                                        `POidRef` int(3) unsigned DEFAULT NULL COMMENT 'Referenz auf die Frage',
                                        `POtyp` char(20) DEFAULT NULL COMMENT 'Fragetyp',
                                        `PObez` text DEFAULT NULL COMMENT 'Frage',
                                        `POkurz` char(40) DEFAULT NULL COMMENT 'Kurzbezeichnung (Auswertung)',
                                        `POsortierung` int(5) unsigned DEFAULT NULL COMMENT 'Sortierreihenfolge',
                                        `POgrenze1` decimal(4,2) DEFAULT NULL,
                                        `POgrenze2` decimal(4,2) DEFAULT NULL,
                                        `POvalueMin` int(12) unsigned DEFAULT NULL COMMENT 'Minimalwert Antwort',
                                        `POvalueMax` int(12) unsigned DEFAULT NULL COMMENT 'Maximalwert Antwort',
                                        `POlinks` char(15) DEFAULT NULL COMMENT 'Ausprägung Links',
                                        `POrechts` char(15) DEFAULT NULL COMMENT 'Ausprägung Rechts',
                                        `POtrennstrich` enum('y','n') DEFAULT 'y' COMMENT 'Trennstrich',
                                        `POerforderlich` enum('y','n') DEFAULT 'n' COMMENT 'Erforderlich',
                                        `POausrichtung` enum('v','h') DEFAULT 'v' COMMENT 'Ausrichtung',
                                        `POauswtyp` char(1) DEFAULT NULL,
                                        `POloek` enum('n','y') DEFAULT 'n' COMMENT 'Löschkennzeichen',
                                        `POerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungsdatum',
                                        `POeruser` char(35) DEFAULT NULL,
                                        `POaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungsdatum',
                                        `POaeuser` char(35) DEFAULT NULL,
                                        PRIMARY KEY (`POid`,`FBid`),
                                        KEY `KAid` (`KAid`),
                                        KEY `FBid, POidRef` (`FBid`,`POidRef`)
) ENGINE=InnoDB AUTO_INCREMENT=543 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uKOMMENTAR`
--

DROP TABLE IF EXISTS `uKOMMENTAR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uKOMMENTAR` (
                              `uBEFRAGUNG_BEid` int(6) unsigned NOT NULL COMMENT 'Befragung',
                              `uFRAGEBOGEN_POSITION_FBid` int(3) unsigned NOT NULL COMMENT 'Fragebogen',
                              `uFRAGEBOGEN_POSITION_POid` int(3) unsigned NOT NULL COMMENT 'Position',
                              `KMursache` text DEFAULT NULL COMMENT 'Mögliche Ursachen',
                              `KMloesung` text DEFAULT NULL COMMENT 'Lösungen',
                              `KMloesungbis` date DEFAULT NULL COMMENT 'Lösung bis',
                              `KMdatum` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Datum Kommentar',
                              `KMuserid` char(35) DEFAULT NULL COMMENT 'Benutzer Kommentar',
                              `KMdurchschnitt` decimal(4,2) DEFAULT NULL,
                              `KMstatus` int(1) unsigned DEFAULT NULL COMMENT 'Status 0=offen, 1=besprochen',
                              `KMloek` enum('n','y') DEFAULT 'n',
                              `KMerda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Erstellungs-Datum',
                              `KMeruser` char(35) DEFAULT NULL COMMENT 'Erstellungs-Benutzer',
                              `KMaeda` datetime /* mariadb-5.3 */ DEFAULT NULL COMMENT 'Änderungs-Datum',
                              `KMaeuser` char(35) DEFAULT NULL COMMENT 'Änderungs-Benutzer',
                              PRIMARY KEY (`uBEFRAGUNG_BEid`,`uFRAGEBOGEN_POSITION_FBid`,`uFRAGEBOGEN_POSITION_POid`),
                              KEY `uKOMMENTAR_FKIndex1` (`uBEFRAGUNG_BEid`),
                              KEY `uKOMMENTAR_FKIndex2` (`uFRAGEBOGEN_POSITION_POid`,`uFRAGEBOGEN_POSITION_FBid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ueba17_tmp`
--

DROP TABLE IF EXISTS `ueba17_tmp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ueba17_tmp` (
                              `SEMINAR_SMnr` int(6) NOT NULL COMMENT 'Seminar-Nummer',
                              `ADRESSE_ADadnr` int(6) unsigned NOT NULL COMMENT 'Adress-Nummer',
                              `TAanmeldedatum` date DEFAULT NULL COMMENT 'Anmelde-Datum',
                              `TAamsbetreuer` char(40) DEFAULT NULL COMMENT 'AMS-Betreuer',
                              `TArgsnr` int(6) DEFAULT NULL,
                              `TARgsKYnr` int(6) DEFAULT NULL COMMENT 'KEYTABLE where KYname = RGS',
                              `TAteilnahmevon` date DEFAULT NULL COMMENT 'Teilnahme von',
                              `TAteilnahmebis` date DEFAULT NULL COMMENT 'Teilnahme bis',
                              `TAvermittlungsart` int(6) DEFAULT NULL COMMENT 'Vermittlungsart (Arbeitsaufnahme, Zusage, Qualifizierung, sonstiges)',
                              `TAvermittlung` int(6) DEFAULT NULL COMMENT 'Arbeitsaufnahme (KT Dienstverhältnis, Lehrstelle)',
                              `TAdatumbeginn` date DEFAULT NULL COMMENT 'Beginn-Datum Vermittlung',
                              `TAdatumende` date DEFAULT NULL COMMENT 'Ende-Datum Vermittlung',
                              `TAaa_bei` char(80) DEFAULT NULL COMMENT 'Arbeitsaufnahme bei Firma/Institut',
                              `TAaa_als` char(80) DEFAULT NULL COMMENT 'Arbeitsaufnahme als',
                              `TAqual_ort` char(40) DEFAULT NULL COMMENT 'Qualifizierung Ort',
                              `TAqual_kursbez` char(80) DEFAULT NULL COMMENT 'Qualifizierung Kursbezeichnung',
                              `TAsonstiges` int(6) DEFAULT NULL COMMENT 'Sonstiger Abschluss (KT Pension, Karenz, Krankheit, sonstiges)',
                              `TApruefung` char(80) DEFAULT NULL COMMENT 'Prüfungsbezeichnung',
                              `TApruefungerg` enum('b','t','n') DEFAULT NULL COMMENT 'Prüfungsergebnis (bestanden, tlw. bestanden, nicht bestanden)',
                              `TApruefungwdh` date DEFAULT NULL COMMENT 'Prüfungswiederholung am (Datum)',
                              `TAkarriereplan` enum('n','y') DEFAULT NULL COMMENT 'Karriereplan vorhanden',
                              `TAabschluss` int(6) DEFAULT NULL COMMENT 'Abbruch',
                              `TAabschlussdatum` date DEFAULT NULL COMMENT 'Abbruch Datum',
                              `TAabschlussBeiCrmFirma` int(6) unsigned DEFAULT NULL COMMENT 'Wenn Abschluss Dienstverhaeltnis, wird hier die CRM_FIRMA.id angegeben',
                              `TAabschlussgrund` text DEFAULT NULL COMMENT 'Abbruch Begründung',
                              `TAinternebemerk` text DEFAULT NULL,
                              `TAarbeitsplatzsucheseit` char(7) DEFAULT NULL,
                              `TAletztearbeitsstelle` char(40) DEFAULT NULL,
                              `TAletztearbeitsstelleals` char(40) DEFAULT NULL,
                              `TAletztearbeitsstellevon` char(7) DEFAULT NULL,
                              `TAletztearbeitsstellebis` char(7) DEFAULT NULL,
                              `TAalternativeMNummer` varchar(50) DEFAULT NULL COMMENT 'Einzutragen, wenn M Nummer auf PJ-Ebene nicht stimmt',
                              `TAalternativeVNummer` varchar(50) DEFAULT NULL COMMENT 'Einzutragen, wenn V Nummer auf SM-Ebene nicht stimmt',
                              `TAtn_zufriedenheit_ausgefuellt` tinyint(1) DEFAULT NULL,
                              `TAmbbe_betreuung` int(6) unsigned DEFAULT NULL COMMENT 'MBBE_BETREUUNG.id',
                              `TAmbbe_leistungsbezug` int(6) unsigned DEFAULT NULL COMMENT 'MBBE_LEISTUNGSBEZUG.id',
                              `TAmbbe_uebertritt` date DEFAULT NULL,
                              `TAjobsuche_aktiv` tinyint(1) DEFAULT NULL COMMENT 'Für Jobsuche aktiv',
                              `TAteilnehmerkategorie_id` int(6) unsigned DEFAULT NULL,
                              `TAaeda` date DEFAULT NULL COMMENT 'Änderungsdatum',
                              `TAaeuser` char(35) DEFAULT NULL,
                              `TAerda` date DEFAULT NULL COMMENT 'Erfassungsdatum',
                              `TAeruser` char(35) DEFAULT NULL,
                              `ADadnr` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'Adress-Nummer',
                              `ADznf1` varchar(50) DEFAULT NULL COMMENT 'Zuname, Firmenname1',
                              `ADvnf2` varchar(50) DEFAULT NULL COMMENT 'Vorname, Firmenname 2',
                              `ADsvnr` varchar(50) DEFAULT NULL,
                              `ADgebdatum` date DEFAULT NULL COMMENT 'Geburtsdatum',
                              `SMnr` int(6) NOT NULL DEFAULT 0 COMMENT 'Seminar-Nummer',
                              `SMbezeichnung1` char(128) DEFAULT NULL COMMENT 'Seminar-Bezeichnung 1',
                              `PJnr` int(6) NOT NULL DEFAULT 0 COMMENT 'Projekt-Nummer',
                              `ASnr` int(6) unsigned NOT NULL DEFAULT 0 COMMENT 'Ausschreibungs-Nummer',
                              `zielseminar_smnr` int(6) unsigned DEFAULT NULL,
                              `zielseminar_teilnahme_von` date DEFAULT NULL,
                              `zielseminar_teilnahme_bis` date DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-17 15:14:17
