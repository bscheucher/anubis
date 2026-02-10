package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Dashboards;

import java.util.List;

public interface DashboardsService extends BaseService<Dashboards> {
    List<Integer> getAllDashboardsIdsByAzureId(String azureId);
    void setFavouriteDashboard(Integer dashboardId, String azureId);
    Dashboards findDashboardByIdAndUserAzureId(Integer dashboardId, String azureId);
    List<Dashboards> getAllDashboardsByAzureId(String azureId);
    Dashboards saveUpdateDashboardWithWidgets(Dashboards dashboard);
}
