package com.ibosng.lhrservice.dtos.dokumente;

import lombok.Data;

@Data
public class DokumentRubrikDto {
    private Integer id;
    private String location;
    private String name;
    private Integer parent;
    private Integer docCount;
}
