package com.ibosng.lhrservice.enums;


public enum LhrDocuments {
    L16("L16"),
    NETTOZETTEL("Nettozettel"),
    ELDA("Bestätigungen (ELDA)"),
    ZEITNACHWEISSE("Zeitnachweise");

    private final String lhrDocument;

    // Constructor
    LhrDocuments(String lhrDocument) {
        this.lhrDocument = lhrDocument;
    }

    // Getter method to retrieve the integer value
    public String getLhrDocument() {
        return lhrDocument;
    }
}