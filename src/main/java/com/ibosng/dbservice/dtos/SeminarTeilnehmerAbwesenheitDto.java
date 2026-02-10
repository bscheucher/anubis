package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeminarTeilnehmerAbwesenheitDto {
    private int id;
    private AnAbwesenheitKategorieDto abwesenheitKategorie;
    private String datum;
    private String bemerkung;
}
