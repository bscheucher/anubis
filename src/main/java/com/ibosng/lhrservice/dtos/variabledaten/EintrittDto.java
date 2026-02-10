package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude()
@Builder(toBuilder = true)
public class EintrittDto {
    private String grund;
    private String beschreibung;
    private String kommentar;
    private String art;
    private Integer id;
    private Integer reference;
    private ZeitangabeDto zeitangabe;
    private Integer units;
    private String source;
}
