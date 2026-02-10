package com.ibosng.gatewayservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PayloadType implements Serializable {
    private String type;
}
