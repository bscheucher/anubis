package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.WidgetsInDashboards;
import com.ibosng.dbservice.repositories.WidgetsInDashboardsRepository;
import com.ibosng.dbservice.services.WidgetsInDashboardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WidgetsInDashboardsServiceImpl implements WidgetsInDashboardsService {

    private final WidgetsInDashboardsRepository widgetsInDashboardsRepository;

    @Autowired
    public WidgetsInDashboardsServiceImpl(WidgetsInDashboardsRepository widgetsInDashboardsRepository) {
        this.widgetsInDashboardsRepository = widgetsInDashboardsRepository;
    }

    @Override
    public Optional<WidgetsInDashboards> findById(Integer id) {
        return widgetsInDashboardsRepository.findById(id);
    }

    @Override
    public List<WidgetsInDashboards> findAll() {
        return widgetsInDashboardsRepository.findAll();
    }

    @Override
    public WidgetsInDashboards save(WidgetsInDashboards widgetsInDashboards) {
        return widgetsInDashboardsRepository.save(widgetsInDashboards);
    }

    @Override
    public List<WidgetsInDashboards> saveAll(List<WidgetsInDashboards> widgetsInDashboards) {
        return widgetsInDashboardsRepository.saveAll(widgetsInDashboards);
    }

    @Override
    public void deleteById(Integer id) {
        this.widgetsInDashboardsRepository.deleteById(id);
    }

    @Override
    public List<WidgetsInDashboards> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<WidgetsInDashboards> getAllWidgetsByDashboardId(Integer dashboardId) {
        return widgetsInDashboardsRepository.getAllWidgetsByDashboardId(dashboardId);
    }

    @Override
    public List<WidgetsInDashboards> getAllWidgetsInDashboardsByAzureId(Integer azureId) {
        return widgetsInDashboardsRepository.getAllWidgetsInDashboardsByAzureId(azureId);
    }

    @Override
    public List<WidgetsInDashboards> getAllWidgetsByDashboardIdAndWidgetId(Integer dashboardId, Integer widgetId) {
        return widgetsInDashboardsRepository.getAllWidgetsByDashboardIdAndWidgetId(dashboardId, widgetId);
    }

    @Override
    public void deleteAll(List<WidgetsInDashboards> widgets) {
        widgetsInDashboardsRepository.deleteAll(widgets);
    }

    @Override
    public void deleteAllById(List<Integer> ids) {
        widgetsInDashboardsRepository.deleteAllById(ids);
    }
}
