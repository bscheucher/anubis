package com.ibosng.lhrservice.dtos.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetailDto {
    private String representation;
    private String reason;
}
