package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class UnterhaltsberechtigteDto {

    private Integer id;
    private String personalnummer;
    private String uVorname;
    private String uNachname;
    private String uSvnr;
    private String uGeburtsdatum;
    private String uVerwandtschaft;
    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
}
