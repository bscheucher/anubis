package com.ibosng.gatewayservice.dtos.ai;

import lombok.Data;

@Data
public class RankedTrainerDto {
    private Integer id;
    private String name;
    private String score;
    private String email;
    private String telefon;
    private String isVorherigeVertretung;
    private String verfuegbarkeitPraesenz;
    private String verfuegbarkeitOnline;
    private String justification;
}
