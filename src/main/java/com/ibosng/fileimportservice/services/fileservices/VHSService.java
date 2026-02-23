package com.ibosng.fileimportservice.services.fileservices;

import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.fileimportservice.dtos.ParticipantVHSDto;
import com.ibosng.fileimportservice.dtos.SeminarDTO;
import com.ibosng.fileimportservice.exceptions.ParserException;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;

import java.util.Optional;

public interface VHSService {
    void readFile(FileDetails file, Optional<String> filePassword) throws ParserException;

    TeilnehmerStaging getTeilnehmerStaging(String filePath, ParticipantVHSDto participantVHSDto, SeminarDTO seminarDTO);
}
