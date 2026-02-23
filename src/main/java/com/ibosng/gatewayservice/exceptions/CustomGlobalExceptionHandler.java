package com.ibosng.gatewayservice.exceptions;

import com.ibosng.dbservice.dtos.ErrorDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadType;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String ERROR_FILE_EXCEEDS_MAX_SIZE = "Datei überschreitet die Größe von %s MB";

    @Getter
    @Value("${maxFileUploadSize:#{null}}")
    private Integer maxFileUploadSize;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        if (ex instanceof MaxUploadSizeExceededException) {
            String errorMessage = String.format(ERROR_FILE_EXCEEDS_MAX_SIZE, getMaxFileUploadSize());
            CustomErrorResponse errorResponse = new CustomErrorResponse(errorMessage, HttpStatus.PAYLOAD_TOO_LARGE.value());
            return new ResponseEntity<>(errorResponse, headers, HttpStatus.PAYLOAD_TOO_LARGE);
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorDto errorDto = new ErrorDto();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errorDto.addError(error.getField(), error.getDefaultMessage()));

        List<ErrorDto> errorList = List.of(errorDto);
        PayloadTypeList<ErrorDto> errorPayload = new PayloadTypeList<>(PayloadTypes.ERROR.getValue(), errorList);

        String message = errorDto.getErrors().values().stream().findFirst().orElse("Validation failed");

        PayloadResponse payloadResponse = PayloadResponse.builder()
                .success(false)
                .message(message)
                .data(List.of(errorPayload))
                .build();

        return new ResponseEntity<>(payloadResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<PayloadResponse> handleBusinessLogicException(BusinessLogicException ex) {
        PayloadResponse payloadResponse = PayloadResponse.builder().success(false).message(ex.getMessage()).build();
        return new ResponseEntity<>(payloadResponse, ex.getHttpStatus());
    }
}
