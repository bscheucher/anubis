package com.ibosng.personalverwaltung.utils;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;

import java.time.LocalDateTime;

public class BenutzerFactory {

    public static Benutzer createFor(Personalnummer personalnummer, String upn) {
        var benutzer = new Benutzer();
        benutzer.setPersonalnummer(personalnummer);
        benutzer.setUpn(upn);
        benutzer.setStatus(Status.ACTIVE);
        benutzer.setCreatedBy("");
        benutzer.setCreatedOn(LocalDateTime.now());
        return benutzer;
    }

    public static Benutzer createForUpn(String upn) {
        var benutzer = new Benutzer();
        benutzer.setUpn(upn);
        benutzer.setStatus(Status.ACTIVE);
        benutzer.setCreatedBy("");
        benutzer.setCreatedOn(LocalDateTime.now());
        return benutzer;
    }

    public static Benutzer createForEmail(String email) {
        var benutzer = new Benutzer();
        benutzer.setEmail(email);
        benutzer.setStatus(Status.ACTIVE);
        benutzer.setCreatedBy("");
        benutzer.setCreatedOn(LocalDateTime.now());
        return benutzer;
    }
}
