package com.ibosng.dbibosservice.dtos.revenue;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProjectForecastDataDto extends ProjectSollPlanData {

    private BigDecimal forecastStunden;

    private BigDecimal forecastUmsatz;

    private BigDecimal abweichungSollPlan;

    private BigDecimal abweichungSollForecast;

    private BigDecimal abweichungPlanForecast;
}
