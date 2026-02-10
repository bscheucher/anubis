package com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VertragsaenderungMultiRequestDto {
    @NotNull(message = "Vertragsaenderung cannot be null")
    @Valid
    private VertragsaenderungDto vertragsaenderungDto;
    @NotNull(message = "Vertragsdaten cannot be null")
    private VertragsdatenDto vertragsdatenDto;
}
