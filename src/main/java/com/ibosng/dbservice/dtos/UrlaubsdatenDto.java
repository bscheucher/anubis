package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UrlaubsdatenDto {
    private Integer id;
    private String personalnummer;
    private String anspuruch;
    private String month;
    private String from;
    private String nextAnspruch;
    private Double kuerzung;
    private Double verjaehrung;
    private Double anspruch;
    private Double konsum;
    private Double rest;
}
