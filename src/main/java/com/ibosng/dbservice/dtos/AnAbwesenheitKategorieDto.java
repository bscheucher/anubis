package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnAbwesenheitKategorieDto {
    private int id;
    private String short_bezeichnung;
    private String bezeichnung;
}
