package com.ibosng.lhrservice.dtos.dokumente;

import com.ibosng.lhrservice.dtos.FirmaRefDto;
import lombok.Data;

@Data
public class DocumentRubrikRefDto {
    private FirmaRefDto firma;
    private Integer id;
    private String name;
}
