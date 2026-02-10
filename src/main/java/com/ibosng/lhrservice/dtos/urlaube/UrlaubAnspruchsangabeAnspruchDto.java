package com.ibosng.lhrservice.dtos.urlaube;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class UrlaubAnspruchsangabeAnspruchDto {
    private Integer nr;
    private String name;
}
