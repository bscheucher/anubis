package com.ibosng.dbservice.dtos.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Constants.ISO_DATE_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AbwesenheitDto {
    private Integer id;
    private Integer personalnummerId;
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_PATTERN)
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_PATTERN)
    private LocalDate endDate;
    private AbwesenheitType type;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_PATTERN)
    private LocalDate changedOn;
    private String durationInDays;
    @Builder.Default
    private List<String> fuehrungskraefte = new ArrayList<>();
    private AbwesenheitStatus status;
    private String commentFuehrungskraft;
    private Double anspruch;
    private boolean isLhrCalculated;
    private Integer lhrHttpStatus;
}
