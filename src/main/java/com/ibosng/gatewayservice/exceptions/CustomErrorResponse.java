package com.ibosng.gatewayservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomErrorResponse {
    private String message;
    private int statusCode;
}