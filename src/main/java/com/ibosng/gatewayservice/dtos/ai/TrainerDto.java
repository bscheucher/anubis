package com.ibosng.gatewayservice.dtos.ai;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrainerDto {
    private Integer id;
    private String name;
    private String punkte;
    private String email;
    private String phone;
    private Boolean hasExperience;
    private List<String> availability = new ArrayList<>();
}
