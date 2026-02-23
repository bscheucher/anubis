package com.ibosng.gatewayservice.dtos.dashboards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class BasicDashboardDto {

    @JsonIgnore
    protected String requestType;

    protected Integer dashboardId;

    @JsonIgnore
    protected String token;

    @JsonProperty
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @JsonProperty
    public void setToken(String token) {
        this.token = token;
    }
}
