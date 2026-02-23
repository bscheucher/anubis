package com.ibosng.dbservice.dtos.urlaubsdaten;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UrlaubTopLevelDto {
    private String nextAnspruch;
    private double kuerzung;
    private double verjaehrung;
    private double anspruch;
    private double konsum;
    private double rest;
}
