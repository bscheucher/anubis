package com.ibosng.dbservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ZeitausgleichDto {
    private Integer id;
    private String personalnummer;
    private String datum;
    private String timeVon;
    private String timeBis;
    private String comment;
    private Double durationHours;
    @Builder.Default
    private List<String> fuehrungskraefte = new ArrayList<>();
    private String createdOn;
}
