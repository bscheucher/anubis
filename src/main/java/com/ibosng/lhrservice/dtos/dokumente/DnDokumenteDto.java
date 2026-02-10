package com.ibosng.lhrservice.dtos.dokumente;

import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

import java.util.List;

@Data
public class DnDokumenteDto {
    private DienstnehmerRefDto dienstnehmer;
    private DocumentRubrikRefDto rubrik;
    private List<DokumentDto> documents;
}
