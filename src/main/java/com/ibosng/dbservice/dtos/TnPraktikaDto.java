package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TnPraktikaDto {
    private int id;
    private String von;
    private String bis;
    private String erprobung;
    private String ergebnis;
    private String notiz;
}
