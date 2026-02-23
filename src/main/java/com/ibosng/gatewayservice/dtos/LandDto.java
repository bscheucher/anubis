package com.ibosng.gatewayservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandDto implements Serializable {
    private String landName;
    private String landCode;
    private String eldaCode;
    private String telefonVorwahl;
    private boolean isEuEeaCh;
}
