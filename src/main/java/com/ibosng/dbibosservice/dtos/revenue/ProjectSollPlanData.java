package com.ibosng.dbibosservice.dtos.revenue;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectSollPlanData {
    private BigDecimal sollUmsatz;

    private BigDecimal sollStunden;

    private BigDecimal planUmsatz;

    private BigDecimal planStunden;
}
