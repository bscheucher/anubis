package com.ibosng.lhrservice.dtos.mitversicherte;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MitversicherteDataDto {
    private Integer nr;
    private List<MitversicherteStammdatenDto> stammdaten;
}
