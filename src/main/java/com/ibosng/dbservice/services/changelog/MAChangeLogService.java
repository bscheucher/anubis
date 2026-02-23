package com.ibosng.dbservice.services.changelog;

import com.ibosng.dbservice.dtos.changelog.FieldChangeDto;
import com.ibosng.dbservice.dtos.changelog.MitarbeiterChangeLogDto;

import java.util.List;

public interface MAChangeLogService {
    MitarbeiterChangeLogDto getMAChangeLog(String personalnummerString);

    List<FieldChangeDto> getVertragsaenderungenPredecessorAndSuccessor(Integer predecessor, Integer successor);
}
