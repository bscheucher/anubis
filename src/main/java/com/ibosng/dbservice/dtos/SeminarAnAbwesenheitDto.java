package com.ibosng.dbservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeminarAnAbwesenheitDto {
    private Integer seminarId;
    private String seminar;
    private String project;
    private String standort;
    private LocalDate von;
    private LocalDate bis;
    private Double verzoegerung;
    private List<TrainerDto> trainers;
    private LocalDateTime changedOn;

    //as of 12.03.2025 Paul wants to have it returned to the FE as a LocalDate
    public LocalDate getChangedOn() {
        return changedOn != null ? changedOn.toLocalDate() : null;
    }
}
