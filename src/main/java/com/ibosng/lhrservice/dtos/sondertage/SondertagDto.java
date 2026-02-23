package com.ibosng.lhrservice.dtos.sondertage;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class SondertagDto {
    private Integer matchCode;
    private SondertagDatesDto date;

}
