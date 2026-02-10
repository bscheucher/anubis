package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDto {
    private int id;
    private String name;
    private String email;
    private String funktion;
    private String telefon;
    private String bezugsdauerStart;
    private String bezugsdauerEnde;
}
