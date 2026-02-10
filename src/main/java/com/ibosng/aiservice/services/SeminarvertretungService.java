package com.ibosng.aiservice.services;

public interface SeminarvertretungService {
    /**
     * Gibt eine paginierte Liste aller verfügbaren Seminare mit Start- und Enddatum, Uhrzeit, zugehörigem Projekt zurück und alle notwendigen Details.
     *
     * @param page Die Seitennummer, beginnend bei 0
     * @return Liste von SeminarScheduleDto mit allen relevanten Seminarinformationen.
     */
   /* @MethodDescription("Gibt eine paginierte Liste aller verfügbaren Seminare mit Start- und Enddatum, Uhrzeit, zugehörigem Projekt zurück, paginiert, beginnend bei Page 0.")
    List<SeminarScheduleDto> findAllSeminarsWithDetails(int page, int size);

    *//**
     * Gibt eine paginierte Liste aller verfügbaren Trainer mit allen Details innerhalb eines bestimmten Zeitraums zurück.
     *
     * @param fromDate Startdatum im Format "yyyy-MM-dd"
     * @param toDate   Enddatum im Format "yyyy-MM-dd"
     * @param page Die Seitennummer, beginnend bei 0
     * @return Liste von TrainerScheduleDto mit allen relevanten Informationen zu Trainern.
     *//*
    @MethodDescription("Gibt eine paginierte Liste aller verfügbaren Trainer mit allen Details innerhalb eines bestimmten Zeitraums, paginiert, beginnend bei Page 0.")
    List<TrainerScheduleDto> findTrainersWithSchedule(String fromDate, String toDate, int page, int size);

    *//**
     * Findet die Verfügbarkeit einer Trainer-Vertrettung basierend auf Vorname und Nachname innerhalb eines bestimmten Zeitraums, paginiert.
     *
     * @param from     Startdatum im Format "yyyy-MM-dd"
     * @param to       Enddatum im Format "yyyy-MM-dd"
     * @param vorname  Vorname des Trainers
     * @param nachname Nachname des Trainers
     * @param page     Die Seitennummer, beginnend bei 0
     * @return Liste von TrainerScheduleDto mit den Seminarplan-Details des Trainers.
     *//*
    @MethodDescription("Findet die Verfügbarkeit einer Trainer-Vertrettung basierend auf Vorname und Nachname innerhalb eines bestimmten Zeitraums, paginiert, beginnend bei Page 0")
    List<TrainerScheduleDto> findTrainersScheduleByFirstNameAndLastName(String from, String to, String vorname, String nachname, int page, int size);

    *//**
     * Findet alle Trainer, die bestimmte Voraussetzungen erfüllen, paginiert.
     *
     * @param requirements Liste von Voraussetzungen (z. B. "Deutsch", "DaF/Daz").
     * @param available_from Startdatum des Verfügbarkeitszeitraums im Format "yyyy-MM-dd"
     * @param available_to Enddatum des Verfügbarkeitszeitraums im Format "yyyy-MM-dd"
     * @param page Die Seitennummer, beginnend bei 0
     * @return Liste von Trainern, die die angegebenen Voraussetzungen haben.
     *//*
    @MethodDescription("Findet alle Trainer, die bestimmte Voraussetzungen erfüllen, paginiert, beginnend bei Page 0.")
    List<Trainer> findTrainersWithVoraussetzung(List<String> beschreibungen, int page, int size);*/

    /*    *//**
     * Findet alle Seminare basierend auf dem Vor- und Nachnamen eines Trainers, paginiert.
     *
     * @param vorname Vorname des Trainers
     * @param nachname Nachname des Trainers
     * @param page Die Seitennummer, beginnend bei 0
     * @return Liste von Seminaren, die von diesem Trainer geleitet werden.
     *//*
    @MethodDescription("Findet alle Seminare basierend auf dem Vor- und Nachnamen eines Trainers")
    @ParameterDescription({
        @Param(name = "vorname", description = "Vorname des Trainers"),
        @Param(name = "nachname", description = "Nachname des Trainers"),
        @Param(name = "page", description = "Seitennummer für die Paginierung, beginnend bei 0"),
        @Param(name = "size", description = "Anzahl der Elemente pro Seite")
    })
    ResponseDto<SeminarDto> findAllSeminarsByTrainerVornameAndTrainerNachname(
        String vorname, 
        String nachname, 
        int page, 
        int size
    );

    *//**
     * Ruft die Details eines Seminars anhand der Seminarbezeichnung ab, paginiert.
     *
     * @param bezeichnung Name des Seminars
     * @param page Die Seitennummer, beginnend bei 0
     * @return SeminarScheduleDto mit den Details des Seminars.
     *//*
    @MethodDescription("Ruft die Details eines Seminars anhand der Seminarbezeichnung ab")
    @ParameterDescription({
        @Param(name = "bezeichnung", description = "Name oder Bezeichnung des Seminars"),
        @Param(name = "page", description = "Seitennummer für die Paginierung, beginnend bei 0"),
        @Param(name = "size", description = "Anzahl der Elemente pro Seite")
    })
    Page<SeminarScheduleDto> findSeminarDetailsBySeminarBezeichnung(
        String bezeichnung, 
        int page, 
        int size
    );

    *//**
     * Findet die Voraussetzungen für eine Liste von Seminarbezeichnungen, paginiert.
     *
     * @param seminarNames Liste der Seminarbezeichnungen
     * @param page Die Seitennummer, beginnend bei 0
     * @return Liste von Voraussetzungen für die angegebenen Seminare.
     *//*
    @MethodDescription("Findet die Voraussetzungen für eine Liste von Seminarbezeichnungen")
    @ParameterDescription({
        @Param(name = "seminarNames", description = "Liste der Seminarbezeichnungen"),
        @Param(name = "page", description = "Seitennummer für die Paginierung, beginnend bei 0"),
        @Param(name = "size", description = "Anzahl der Elemente pro Seite")
    })
    Page<Voraussetzung> findVoraussetzungenBySeminarBezeichnungen(
        List<String> seminarNames, 
        int page, 
        int size
    );

  *//*  *//**//**
     * Findet die Verfügbarkeit einer Trainer-Vertrettung basierend auf Vorname und Nachname innerhalb eines bestimmten Zeitraums, paginiert.
     *
     * @param from     Startdatum im Format "yyyy-MM-dd"
     * @param to       Enddatum im Format "yyyy-MM-dd"
     * @param vorname  Vorname des Trainers
     * @param nachname Nachname des Trainers
     * @param page     Die Seitennummer, beginnend bei 0
     * @return Liste von TrainerScheduleDto mit den Seminarplan-Details des Trainers.
     *//**//*
    @MethodDescription("Findet die Verfügbarkeit einer Trainer-Vertretung basierend auf Zeitraum, Name, Voraussetzungen und Präsenztyp (ONLINE oder PRAESENZ), paginiert ab Seite 0.")
    List<TrainerScheduleDto> findTrainerScheduleWithFilters(String from, String to, String vorname, String nachname, List<String> voraussetzungen, String praesenzType, int page, int size);*//*

     *//**
     * Findet die Verfügbarkeit einer Trainer-Vertrettung basierend auf Vorname und Nachname innerhalb eines bestimmten Zeitraums, paginiert.
     *
     * @param from     Startdatum im Format "yyyy-MM-dd"
     * @param to       Enddatum im Format "yyyy-MM-dd"
     * @param page     Die Seitennummer, beginnend bei 0
     * @return Liste von TrainerScheduleDto mit den Seminarplan-Details des Trainers.
     *//*
    @MethodDescription("Findet alle Trainer zu den angegebenen Filterkriterien")
    @ParameterDescription({
        @Param(name = "from", description = "Startdatum im Format 'yyyy-MM-dd'"),
        @Param(name = "to", description = "Enddatum im Format 'yyyy-MM-dd'"),
        @Param(name = "voraussetzungen", description = "Liste von Voraussetzungen"),
        @Param(name = "praesenzType", description = "Präsenztyp (ONLINE oder PRAESENZ)"),
        @Param(name = "page", description = "Seitennummer für die Paginierung, beginnend bei 0"),
        @Param(name = "size", description = "Anzahl der Elemente pro Seite")
    })
    ResponseDto<TrainerDto> findTrainers(
        String from, String to, 
        List<String> voraussetzungen, 
        String praesenzType, 
        int page,
        int size
    );*/
}
