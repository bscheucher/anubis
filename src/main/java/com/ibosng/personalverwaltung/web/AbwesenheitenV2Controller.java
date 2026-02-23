package com.ibosng.personalverwaltung.web;


import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.security.RequiredRoles;
import com.ibosng.gatewayservice.services.EnvironmentService;
import com.ibosng.personalverwaltung.domain.exceptions.AbwesenheitCreationException;
import com.ibosng.personalverwaltung.domain.AbwesenheitenV2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ibosng.gatewayservice.utils.Constants.FN_ABWESENHEITEN_EDITIEREN;

@RestController
@RequestMapping("/v2/abwesenheiten")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "enableLhrIntegrationRework", havingValue = "true")
public class AbwesenheitenV2Controller {

    private final AbwesenheitenV2Service abwesenheitenV2Service;
    private final EnvironmentService environmentService;
    private final AbwesenheitRequestValidator abwesenheitRequestValidator;
    private final AbwesenheitV2DtoMapper abwesenheitV2DtoMapper;

    /**
     * This endpoint should replace zeiterfassung/abwesenheiten/edit
     * To keep it simple, only intended for URLAUB first
     */
    @PostMapping
    @RequiredRoles(FN_ABWESENHEITEN_EDITIEREN)
    ResponseEntity<AbwesenheitV2Dto> createAbwesenheit(@RequestBody AbwesenheitCreateV2Dto dto) {

        var personalnummer = environmentService.checkLoggedInUserAndGetPersonalnummer();
        if (personalnummer == null || personalnummer.isIncomplete()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        abwesenheitRequestValidator.throwIfAbwesenheitRequestInvalid(dto, personalnummer);

        var request = abwesenheitV2DtoMapper.map(dto, personalnummer);
        var createdAbwesenheit = abwesenheitenV2Service.createAbwesenheit(request);

        return ResponseEntity.ok(abwesenheitV2DtoMapper.map(createdAbwesenheit));
    }


    @ExceptionHandler(AbwesenheitCreationException.class)
    public ResponseEntity<PayloadResponse> handleAbwesenheitCreationException(AbwesenheitCreationException ex) {
        var payloadResponse = PayloadResponse.builder().success(false).message(ex.getMessage()).build();
        return new ResponseEntity<>(payloadResponse, ex.getHttpStatus());
    }
}
