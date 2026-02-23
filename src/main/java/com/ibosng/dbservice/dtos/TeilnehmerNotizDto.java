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
public class TeilnehmerNotizDto {
    private Integer id;
    private String notiz;
    private String kategorie;
    private String type;
    private String createdOn;
    private String createdBy;
    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
}

