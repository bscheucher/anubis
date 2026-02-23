package com.ibosng.gatewayservice.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsDto {

    private String azureId;

    private String firstName;

    private String lastName;

    private List<String> roles;

    private String firma;
}
