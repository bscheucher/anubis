package com.ibosng.lhrservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Zeitspeicherrefs {
    UEST_50_FREI("9"),
    UEST_50_PFLICHTIG("25"),
    UEST_100_FREI("29"),
    UEST_100_PFLICHTIG("30"),
    UEST_25_FREI("36"),
    UEST_25_PFLICHTIG("38");

    private final String ref;
}
