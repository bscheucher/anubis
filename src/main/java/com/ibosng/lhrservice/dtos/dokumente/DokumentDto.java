package com.ibosng.lhrservice.dtos.dokumente;

import lombok.Data;

@Data
public class DokumentDto {
    private Integer id;
    private String name;
    private String fileName;
    private String created;
}
