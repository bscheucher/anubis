package com.ibosng.dbibosservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DienstortDto {
    private Long id;
    private String name;
    private String strasse;
    private String land;
    private String plz;
    private String ort;
    private String telefon;
    private String email;
}
