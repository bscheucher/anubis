package com.ibosng.validationservice.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<CustomErrorResponse> handleJsonMappingException(JsonMappingException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();

        String fieldPath = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .reduce((prev, curr) -> prev + "." + curr)
                .orElse("unknown field");

        errorResponse.setErrorMessage("Invalid value for: " + fieldPath);
        errorResponse.setField(fieldPath);

        log.warn("JSON mapping error at field '{}': {}", fieldPath, ex.getOriginalMessage());
        log.debug("Exception details for JsonMappingException", ex);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}