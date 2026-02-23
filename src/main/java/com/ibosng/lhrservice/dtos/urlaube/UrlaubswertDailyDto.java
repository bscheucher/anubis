package com.ibosng.lhrservice.dtos.urlaube;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude
public class UrlaubswertDailyDto {
    private UrlaubAnspruchsangabeDto anspruchsangaben;
    private List<UrlaubswertDailyDaysDto> days;
}
