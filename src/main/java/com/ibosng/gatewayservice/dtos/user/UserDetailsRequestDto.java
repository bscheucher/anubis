package com.ibosng.gatewayservice.dtos.user;

import lombok.Data;

@Data
public class UserDetailsRequestDto {

    private String requestType;

    private String token;
}
