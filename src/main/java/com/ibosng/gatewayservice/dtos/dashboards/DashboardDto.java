package com.ibosng.gatewayservice.dtos.dashboards;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DashboardDto {

    protected Integer dashboardId;

    private String dashboardName;

    private List<WidgetDto> widgets;

    @JsonProperty("isFavourite")
    private boolean isFavourite;

}
