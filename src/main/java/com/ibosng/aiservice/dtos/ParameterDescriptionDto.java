package com.ibosng.aiservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterDescriptionDto {
    private String name;
    private String type;
    private String description;
}