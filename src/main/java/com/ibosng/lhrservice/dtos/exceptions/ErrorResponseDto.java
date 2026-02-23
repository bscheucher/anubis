package com.ibosng.lhrservice.dtos.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unnecessary fields
public class ErrorResponseDto {
    private int code;
    private String message;
    private List<ErrorDetailDto> errors;
}
