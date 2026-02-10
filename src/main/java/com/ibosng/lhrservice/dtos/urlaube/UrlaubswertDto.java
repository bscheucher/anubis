package com.ibosng.lhrservice.dtos.urlaube;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude
public class UrlaubswertDto {
    private UrlaubAnspruchsangabeDto anspruchsangaben;
    private UrlaubswertAnspruchsverlaufDto anspruchsverlauf;
}
