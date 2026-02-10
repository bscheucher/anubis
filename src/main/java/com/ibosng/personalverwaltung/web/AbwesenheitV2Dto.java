package com.ibosng.personalverwaltung.web;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class AbwesenheitV2Dto {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_PATTERN)
    private LocalDate startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_PATTERN)
    private LocalDate endDate;

    @NotNull
    private AbwesenheitType type;

    private String comment;
}
