package com.ibosng.gatewayservice.dtos.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WidgetDto {

    private Integer id;

    private Integer positionX;

    private Integer positionY;
}
