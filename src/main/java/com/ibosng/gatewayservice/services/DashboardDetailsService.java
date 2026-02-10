package com.ibosng.gatewayservice.services;

import com.ibosng.gatewayservice.dtos.dashboards.AllDashboardsDto;
import com.ibosng.gatewayservice.dtos.dashboards.DashboardDto;

public interface DashboardDetailsService {
    AllDashboardsDto getAllDashboards(String token);
    DashboardDto saveDashboard(String token, DashboardDto dashboardDto);
    String deleteDashboard(Integer id);
    String setFavouriteDashboard(String token, Integer dashboardId);
}
