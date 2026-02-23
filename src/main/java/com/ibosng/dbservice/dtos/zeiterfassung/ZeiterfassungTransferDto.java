package com.ibosng.dbservice.dtos.zeiterfassung;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ZeiterfassungTransferDto {
    private Integer id;
    private String datumBis;
    private String datumVon;
    private String datumSent;
    private String userName;
    private String status;
    private List<BasicSeminarDto> seminars = new ArrayList<>();
    private Integer teilnehmerNumber;
    private List<String> errors = new ArrayList();
    private Map<String, String> errorsMap = new HashMap();
}
