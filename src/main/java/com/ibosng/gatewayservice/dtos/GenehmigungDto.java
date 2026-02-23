package com.ibosng.gatewayservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude()
public class GenehmigungDto {
    private Boolean isAccepted;
    private String comment;
}
