package com.ibosng.lhrservice.dtos.dokumente;

import com.ibosng.lhrservice.dtos.FirmaRefDto;
import lombok.Data;

import java.util.List;

@Data
public class DokumentRubrikenDto {
    private FirmaRefDto firma;
    private List<DokumentRubrikDto> rubriken;
}
