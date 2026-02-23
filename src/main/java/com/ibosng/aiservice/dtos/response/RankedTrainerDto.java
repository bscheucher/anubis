package com.ibosng.aiservice.dtos.response;

import lombok.Data;

@Data
public class RankedTrainerDto {
    private String name;
    private String score;
    private String verfuegbarkeitPraesenz;
    private String verfuegbarkeitOnline;
    private String justification;
}
