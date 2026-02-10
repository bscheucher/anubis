package com.ibosng.dbservice.entities.teilnehmer;

import lombok.Getter;

@Getter
public enum TeilnehmerSource {
    VHS(0),
    EAMS(1),
    VHS_EAMS(2),
    OEIF(3),
    EAMS_STANDALONE(4),
    MANUAL(5),
    SYNC_SERVICE(6),
    TN_ONBOARDING(7),
    TEILNEHMER_CSV(8);


    private final int code;

    TeilnehmerSource(int code) {
        this.code = code;
    }
}
