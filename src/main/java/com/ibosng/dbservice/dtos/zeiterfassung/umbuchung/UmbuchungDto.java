package com.ibosng.dbservice.dtos.zeiterfassung.umbuchung;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@JsonInclude()
@NoArgsConstructor
@AllArgsConstructor
public class UmbuchungDto {
    private UmbuchungMetadataDto metadata;

    @Valid
    @NotEmpty(message = "zeitspeicher can`t be empty")
    private List<Zeitspeicher2ValueDto> zeitspeicher;
}
