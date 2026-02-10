package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Dashboards;
import com.ibosng.dbservice.entities.WidgetsInDashboards;
import com.ibosng.dbservice.repositories.DashboardsRepository;
import com.ibosng.dbservice.services.DashboardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardsServiceImpl implements DashboardsService {

    private final DashboardsRepository dashboardsRepository;

    private final WidgetsInDashboardsServiceImpl widgetsInDashboardsService;

    @Autowired
    public DashboardsServiceImpl(DashboardsRepository dashboardsRepository, WidgetsInDashboardsServiceImpl widgetsInDashboardsService) {
        this.dashboardsRepository = dashboardsRepository;
        this.widgetsInDashboardsService = widgetsInDashboardsService;
    }

    @Override
    public Optional<Dashboards> findById(Integer id) {
        return dashboardsRepository.findById(id);
    }

    @Override
    public List<Dashboards> findAll() {
        return dashboardsRepository.findAll();
    }

    @Override
    public Dashboards save(Dashboards dashboards) {
        return dashboardsRepository.save(dashboards);
    }

    @Override
    public List<Dashboards> saveAll(List<Dashboards> dashboards) {
        return dashboardsRepository.saveAll(dashboards);
    }

    @Override
    @Transactional("postgresTransactionManager")
    public void deleteById(Integer id) {
        widgetsInDashboardsService.deleteAllById(widgetsInDashboardsService.getAllWidgetsByDashboardId(id).stream().map(WidgetsInDashboards::getId).toList());
        this.dashboardsRepository.deleteById(id);
    }

    @Override
    public List<Dashboards> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Integer> getAllDashboardsIdsByAzureId(String azureId) {
        return dashboardsRepository.getAllDashboardsIdsByAzureId(azureId);
    }

    @Override
    public void setFavouriteDashboard(Integer dashboardId, String azureId) {
        List<Dashboards> allDashboards = getAllDashboardsByAzureId(azureId);
        allDashboards.forEach(dashboards -> dashboards.setFavourite(false));
        saveAll(allDashboards);
        Dashboards dashboard = findDashboardByIdAndUserAzureId(dashboardId, azureId);
        dashboard.setFavourite(true);
        save(dashboard);
    }

    @Override
    public Dashboards findDashboardByIdAndUserAzureId(Integer dashboardId, String azureId) {
        return dashboardsRepository.findDashboardByIdAndUserAzureId(dashboardId, azureId);
    }

    @Override
    public List<Dashboards> getAllDashboardsByAzureId(String azureId) {
        return dashboardsRepository.getAllDashboardsByAzureId(azureId);
    }

    @Override
    @Transactional("postgresTransactionManager")
    public Dashboards saveUpdateDashboardWithWidgets(Dashboards dashboard) {
        if(dashboard.getId() != null) {
            Optional<Dashboards> savedDashboardOpt = findById(dashboard.getId());
            if(savedDashboardOpt.isPresent()) {
                Dashboards savedDashboard = savedDashboardOpt.get();
                savedDashboard.setDashboardName(dashboard.getDashboardName());
                savedDashboard.setChangedBy(dashboard.getChangedBy());
                List<WidgetsInDashboards> toBeDeleted = new ArrayList<>(savedDashboard.getWidgets());
                toBeDeleted.removeAll(dashboard.getWidgets());
                dashboard.getWidgets().removeAll(savedDashboard.getWidgets());
                widgetsInDashboardsService.deleteAllById(toBeDeleted.stream().map(WidgetsInDashboards::getId).toList());
                widgetsInDashboardsService.saveAll(dashboard.getWidgets());
                savedDashboard.setWidgets(widgetsInDashboardsService.getAllWidgetsByDashboardId(dashboard.getId()));
                return save(savedDashboard);
            }
        }
        return save(dashboard);
    }
}
