package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.WidgetsInDashboards;

import java.util.List;

public interface WidgetsInDashboardsService extends BaseService<WidgetsInDashboards> {
    List<WidgetsInDashboards> getAllWidgetsByDashboardId(Integer dashboardId);

    List<WidgetsInDashboards> getAllWidgetsInDashboardsByAzureId(Integer azureId);

    List<WidgetsInDashboards> getAllWidgetsByDashboardIdAndWidgetId(Integer dashboardId, Integer widgetId);

    void deleteAll(List<WidgetsInDashboards> widgets);

    void deleteAllById(List<Integer> ids);
}
