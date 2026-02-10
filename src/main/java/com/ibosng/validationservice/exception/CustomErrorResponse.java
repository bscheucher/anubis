package com.ibosng.validationservice.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomErrorResponse {

    private String errorMessage;
    private String field;
}