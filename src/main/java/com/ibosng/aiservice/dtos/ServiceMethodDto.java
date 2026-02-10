package com.ibosng.aiservice.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ServiceMethodDto {
    private String methodName;
    private String returnType;
    private List<ParameterDescriptionDto> parameters;
    private String description;
}