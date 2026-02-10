package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.dtos.zeiterfassung.umbuchung.UmbuchungDto;
import com.ibosng.dbservice.dtos.zeiterfassung.umbuchung.UmbuchungMetadataDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface LHRZeitdatenService {
    /**
     * <ul>
     *     <li>Get all the zeitdatem from LHR</li>
     *     <li>Parse Mitarbeiter with Mehr-/Ueberstunden</li>
     *     <li>Send Emails to those with Mehr-/Ueberstunden</li>
     * </ul>
     */
    void checkForAuszahlbareStunden();

    void closeMonaten(LocalDate monthForSend);

    ResponseEntity<UmbuchungDto> getPeriodensummen(Integer personalnummerId, String month);

    ResponseEntity<Void> postAuszahlungsanfrage(Integer personalnummerId, String month, UmbuchungDto umbuchungDto);

    UmbuchungMetadataDto formUmbuchungMetadata(String firmaKz, Integer firmaNr, Integer dnNr, Personalnummer personalnummer, String month);
}
