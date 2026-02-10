package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.lhrservice.dtos.dokumente.DnDokumenteDto;
import com.ibosng.lhrservice.dtos.dokumente.DokumentRubrikenDto;
import com.ibosng.lhrservice.enums.LhrDocuments;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public interface LHRDokumenteService {

    DokumentRubrikenDto findRubrik(Personalnummer personalnummer, String regex);

    DnDokumenteDto findDocument(Personalnummer personalnummer, String date, String rubrikRegex);

    ResponseEntity<byte[]> downloadZeitnachweisForPersonalnummer(Integer personalnummerId, String date);

    List<File> getFiles(String personalnummer, String date, String regex);

    void processAndUploadFiles(String regex, String documentType, LhrDocuments docEnum, String identifier, String personalnummer, LocalDate lastSyncOfDocuments);

    boolean uploadToFileShare(File file, String personalnummer, String identifier, LhrDocuments type);

    boolean uploadToFileShare(File file, String personalnummer, String fullName, LhrDocuments type, String typeName);
}
