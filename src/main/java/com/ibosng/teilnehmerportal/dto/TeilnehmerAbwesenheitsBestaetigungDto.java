package com.ibosng.teilnehmerportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TeilnehmerAbwesenheitsbestaetigungDto(
        Integer id,
        @NotBlank String vorname,
        @NotBlank String nachname,
        @NotBlank @Size(max = 10) String svNummer,
        @NotNull LocalDate startDatum,
        @NotNull LocalDate endDatum
) {
    @Override
    public String toString() {
        return "TnAbsenceConfirmationDto{" +
                "id=" + id +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", svNummer='***'" +
                ", startDatum=" + startDatum +
                ", endDatum=" + endDatum +
                '}';
    }
}