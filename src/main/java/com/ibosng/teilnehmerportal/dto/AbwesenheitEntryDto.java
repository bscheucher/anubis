package com.ibosng.teilnehmerportal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AbwesenheitEntryDto(
        Integer id,
        String vorname,
        String nachname,
        @JsonProperty("sv_nummer") String svNummer,
        String url,
        @JsonProperty("start_datum") String startDatum,
        @JsonProperty("end_datum") String endDatum,
        @JsonProperty("created_at") String createdAt
) {}
