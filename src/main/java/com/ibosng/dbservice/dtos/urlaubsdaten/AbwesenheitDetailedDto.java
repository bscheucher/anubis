package com.ibosng.dbservice.dtos.urlaubsdaten;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude()
public class AbwesenheitDetailedDto {
    private String startDate;
    private String endDate;
    private Double days;
    private Double saldo;
    private String comment;
    private String urlaubType;
    private String status;
    private boolean isLhrCalculated;
}
