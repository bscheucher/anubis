package com.ibosng.lhrservice.dtos.sondertage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class SondertageDto {
    private Integer matchCode;
    private List<SondertagDatesDto> dates;

}
