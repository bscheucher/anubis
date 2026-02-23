package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TnAusbildungDto {
    private Integer id;
    private String ausbildungstyp;
    private boolean hoechsterAbschluss;
    private boolean erkanntInAt;
    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
}
