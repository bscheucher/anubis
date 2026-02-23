package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TnAbschlussDto {
    private int id;
    private String abschlussAm;
    private String notiz;
}
