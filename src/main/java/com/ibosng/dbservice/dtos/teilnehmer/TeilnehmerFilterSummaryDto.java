package com.ibosng.dbservice.dtos.teilnehmer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeilnehmerFilterSummaryDto {
    private Integer id;
    private String vorname;
    private String nachname;
    private List<String> massnahmennummern = new ArrayList<>();
    private List<String> seminarNamen = new ArrayList<>();
    private String svn;
    private String land;
    private String ort;
    private String plz;
    private List<String> angemeldetIn = new ArrayList<>();
    private boolean isUeba;
    private String status;
    private List<String> errors = new ArrayList<>();
}
