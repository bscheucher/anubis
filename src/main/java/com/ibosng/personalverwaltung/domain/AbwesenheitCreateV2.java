package com.ibosng.personalverwaltung.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Constants.ISO_DATE_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbwesenheitCreateV2 {
    @NotNull
    private Personalnummer personalnummer;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_PATTERN)
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_PATTERN)
    private LocalDate endDate;
    private AbwesenheitType type;
    private String comment;
}
