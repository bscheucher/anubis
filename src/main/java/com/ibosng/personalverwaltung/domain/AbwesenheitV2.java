package com.ibosng.personalverwaltung.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Constants.ISO_DATE_PATTERN;

@AllArgsConstructor
@Setter
@Getter
public class AbwesenheitV2 {

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
