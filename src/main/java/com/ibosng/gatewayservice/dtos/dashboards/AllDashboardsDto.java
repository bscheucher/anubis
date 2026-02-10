package com.ibosng.gatewayservice.dtos.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllDashboardsDto {

    private List<DashboardDto> dashboards;

    private Integer favouriteDashboard;
}
