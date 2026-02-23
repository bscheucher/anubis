package com.ibosng.dbibosservice.dtos.revenue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProjectRevenueDataDto extends ProjectSollPlanData {
    @JsonIgnore
    private Integer projectNumber;

    @JsonIgnore
    private LocalDate von;

    @JsonIgnore
    private LocalDate bis;

    private BigDecimal istUmsatz;

    private BigDecimal istStunden;

    private BigDecimal abweichungSollPlan;

    private BigDecimal abweichungSollIst;

    private BigDecimal abweichungPlanIst;

    @JsonIgnore
    private BigDecimal stundenSatz;
}
