package com.ibosng.lhrservice.enums;

public enum PersoenlicheSatze {
    WOCHENSTUNDEN(1),
    ARBEITSTAGE(2),
    GEHALT(10);

    private final int satzNr;

    // Constructor
    PersoenlicheSatze(int satzNr) {
        this.satzNr = satzNr;
    }

    // Getter method to retrieve the integer value
    public int getSatzNr() {
        return satzNr;
    }
}
