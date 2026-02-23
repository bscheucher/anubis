package com.ibosng.lhrservice.dtos.sondertage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

import static com.ibosng.lhrservice.utils.Constants.LOCAL_DATE_FORMAT;

@Data
@JsonInclude()
public class SondertagDatesDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_FORMAT)
    private LocalDate day;
    private String note;
}
