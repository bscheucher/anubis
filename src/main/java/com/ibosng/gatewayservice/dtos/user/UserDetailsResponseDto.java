package com.ibosng.gatewayservice.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsResponseDto {

    private String azureId;

    private String firstName;

    private String lastName;

    private List<String> roles;
}
