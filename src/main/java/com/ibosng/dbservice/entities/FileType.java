package com.ibosng.dbservice.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {

    EAMS_STANDALONE(0), EAMS(1), VHS(2), OEIF(3), TEILNEHMER_CSV(4);

    private final int code;
}
