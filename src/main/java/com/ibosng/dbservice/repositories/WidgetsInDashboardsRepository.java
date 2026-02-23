package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.WidgetsInDashboards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface WidgetsInDashboardsRepository extends JpaRepository<WidgetsInDashboards, Integer> {
    String GET_ALL_WIDGETS_BY_DASHBOARD_ID = "select wid from WidgetsInDashboards wid where wid.dashboard.id = :dashboardId";
    String FIND_ALL_BY_BENUTZER_ID = "select wid from WidgetsInDashboards wid where wid.dashboard.benutzer.azureId = :azureId";
    String GET_ALL_WIDGETS_BY_DASHBOARD_ID_AND_WIDGET_ID = "select wid from WidgetsInDashboards wid where wid.dashboard.id = :dashboardId and wid.widgetId.id = :widgetId";
    String DELETE_BY_ID = "delete from WidgetsInDashboards wid where wid.id in :ids";

    @Query(value = GET_ALL_WIDGETS_BY_DASHBOARD_ID)
    List<WidgetsInDashboards> getAllWidgetsByDashboardId(Integer dashboardId);

    @Query(value = FIND_ALL_BY_BENUTZER_ID)
    List<WidgetsInDashboards> getAllWidgetsInDashboardsByAzureId(Integer azureId);

    @Query(value = GET_ALL_WIDGETS_BY_DASHBOARD_ID_AND_WIDGET_ID)
    List<WidgetsInDashboards> getAllWidgetsByDashboardIdAndWidgetId(Integer dashboardId, Integer widgetId);

    @Modifying
    @Query(value = DELETE_BY_ID)
    void deleteAllById(List<Integer> ids);
}
