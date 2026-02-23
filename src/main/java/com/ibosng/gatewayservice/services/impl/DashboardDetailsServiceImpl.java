package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Dashboards;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.WidgetsInDashboards;
import com.ibosng.dbservice.services.impl.BenutzerServiceImpl;
import com.ibosng.dbservice.services.impl.WidgetServiceImpl;
import com.ibosng.gatewayservice.dtos.dashboards.AllDashboardsDto;
import com.ibosng.gatewayservice.dtos.dashboards.DashboardDto;
import com.ibosng.gatewayservice.dtos.dashboards.WidgetDto;
import com.ibosng.gatewayservice.services.DashboardDetailsService;
import com.ibosng.gatewayservice.services.TokenValidatorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardDetailsServiceImpl implements DashboardDetailsService {

    private final com.ibosng.dbservice.services.impl.DashboardsServiceImpl dashboardsService;

    private final BenutzerServiceImpl benutzerService;

    private final WidgetServiceImpl widgetService;

    private final TokenValidatorService tokenValidatorService;

    public DashboardDetailsServiceImpl(com.ibosng.dbservice.services.impl.DashboardsServiceImpl dashboardsService, BenutzerServiceImpl benutzerService, WidgetServiceImpl widgetService, TokenValidatorService tokenValidatorService) {
        this.dashboardsService = dashboardsService;
        this.benutzerService = benutzerService;
        this.widgetService = widgetService;
        this.tokenValidatorService = tokenValidatorService;
    }

    @Override
    public AllDashboardsDto getAllDashboards(String token) {
        List<Dashboards> dashboards = dashboardsService.getAllDashboardsByAzureId((String) tokenValidatorService.getClaimsFromJwt(token).get("oid"));
        AllDashboardsDto allDashboards = new AllDashboardsDto();
        allDashboards.setDashboards(dashboards.stream().map(this::dashboardsToDashboardDto).toList());
        allDashboards.setFavouriteDashboard(dashboards.stream().filter(Dashboards::isFavourite).findFirst().map(Dashboards::getId).orElse(null));
        return allDashboards;
    }

    @Override
    public DashboardDto saveDashboard(String token, DashboardDto dashboardDto) {
        Dashboards savedDashboard = dashboardsService.saveUpdateDashboardWithWidgets(dashboardDtoToDashboards(token, dashboardDto));
        dashboardDto.setDashboardId(savedDashboard.getId());
        return dashboardDto;
    }

    @Override
    public String deleteDashboard(Integer id) {
        dashboardsService.deleteById(id);
        return String.format("Dashboard with id %s successfully deleted!", id);
    }

    @Override
    public String setFavouriteDashboard(String token, Integer dashboardId) {
        if (dashboardsService.findById(dashboardId).isPresent()) {
            String azureId = (String) tokenValidatorService.getClaimsFromJwt(token).get("oid");
            if (dashboardsService.findDashboardByIdAndUserAzureId(dashboardId, azureId) != null) {
                dashboardsService.setFavouriteDashboard(dashboardId, azureId);
                return "Favourite dashboard successfully updated!";
            }
            return String.format("Dashboard with id %s does not belong to user with azure id %s", dashboardId, azureId);
        }
        return String.format("Dashboard with id %s does not exist", dashboardId);

    }

    private DashboardDto dashboardsToDashboardDto(Dashboards dashboard) {
        DashboardDto dashboardDto = new DashboardDto();
        dashboardDto.setDashboardId(dashboard.getId());
        dashboardDto.setDashboardName(dashboard.getDashboardName());
        dashboardDto.setWidgets(dashboard.getWidgets().stream().map(widget -> new WidgetDto(widget.getWidgetId().getId(), widget.getPositionX(), widget.getPositionY())).toList());
        return dashboardDto;
    }

    private Dashboards dashboardDtoToDashboards(String token, DashboardDto dashboardDto) {
        Dashboards dashboards = new Dashboards();
        dashboards.setDashboardName(dashboardDto.getDashboardName());
        Benutzer benutzer = benutzerService.getBenutzerByAzureId((String) tokenValidatorService.getClaimsFromJwt(token).get("oid"));
        dashboards.setBenutzer(benutzer);
        if (dashboardDto.getDashboardId() != null) {
            dashboards.setId(dashboardDto.getDashboardId());
            dashboards.setChangedBy(benutzer.getEmail());
        }
        dashboards.setCreatedBy(benutzer.getEmail());
        dashboards.setStatus(Status.ACTIVE);
        dashboards.setFavourite(dashboardDto.isFavourite());
        dashboards.setWidgets(widgetDtoToWidgetsInDashboards(dashboardDto.getWidgets(), dashboards));
        return dashboards;
    }

    private List<WidgetsInDashboards> widgetDtoToWidgetsInDashboards(List<WidgetDto> widgetDtos, Dashboards dashboards) {
        List<WidgetsInDashboards> widgetsInDashboards = new ArrayList<>();
        widgetDtos.forEach(widgetDto -> widgetsInDashboards.add(widgetToWidgetsInDashboards(widgetDto, dashboards)));
        return widgetsInDashboards;
    }

    private WidgetsInDashboards widgetToWidgetsInDashboards(WidgetDto widgetDto, Dashboards dashboards) {
        WidgetsInDashboards widgetsInDashboards = new WidgetsInDashboards();
        if(widgetService.findById(widgetDto.getId()).isPresent()) {
            widgetsInDashboards.setWidgetId(widgetService.findById(widgetDto.getId()).get());
            widgetsInDashboards.setPositionX(widgetDto.getPositionX());
            widgetsInDashboards.setPositionY(widgetDto.getPositionY());
            widgetsInDashboards.setStatus(Status.ACTIVE);
            widgetsInDashboards.setCreatedBy(dashboards.getCreatedBy());
            widgetsInDashboards.setDashboard(dashboards);
        }
        return widgetsInDashboards;
    }
}
