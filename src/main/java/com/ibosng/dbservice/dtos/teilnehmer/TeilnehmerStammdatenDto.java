package com.ibosng.dbservice.dtos.teilnehmer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeilnehmerStammdatenDto {
    private int id;
    private String bank;
    private String iban;
    private String bic;
    private String bankcard;
    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
}
