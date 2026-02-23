package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.gatewayservice.dtos.user.UserDetailsDto;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.TokenValidatorService;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.gatewayservice.utils.Constants.*;
import static com.ibosng.gatewayservice.utils.Helpers.areAllFieldsNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Slf4j
@Service
@RequiredArgsConstructor
public class BenutzerDetailsServiceImpl implements BenutzerDetailsService {

    private final static Map<String, Integer> USER_TRIES = new HashMap<>();

    private final static int MAX_USER_TRIES = 100;

    private final com.ibosng.dbservice.services.BenutzerService benutzerService;
    private final TokenValidatorService tokenValidatorService;
    private final AzureSSOService azureSSOService;
    private final PersonalnummerService personalnummerService;

    @Override
    public ResponseEntity<UserDetailsDto> getUserDetailsReponse(String token) {
        UserDetailsDto userDetailsDto = getUserDetails(token);
        if (USER_TRIES.get(token) == null) {
            USER_TRIES.put(token, 0);
        } else {
            USER_TRIES.replace(token, USER_TRIES.get(token) + 1);
        }
        if (USER_TRIES.get(token) > MAX_USER_TRIES) {
            return ResponseEntity.noContent().build();
        }
        if (areAllFieldsNull(userDetailsDto)) {
            getUserDetailsReponse(token);
        }
        return new ResponseEntity<>(userDetailsDto, HttpStatus.OK);
    }

    @Override
    public Benutzer getUserFromToken(String token) {
        Claims userClaims = tokenValidatorService.getClaimsFromJwt(token);
        return benutzerService.getBenutzerByAzureId((String) userClaims.get(OID));
    }

    @Override
    public boolean isUserEligible(String token, List<String> functions) {
        List<String> funktionen = getUserFunctions(getUserFromToken(token));
        return !Collections.disjoint(funktionen, functions);
    }

    @Override
    public boolean isUserEligible(Benutzer benutzer, List<String> functions) {
        List<String> benutzerFunktionen = getUserFunctions(benutzer);
        return benutzerFunktionen.containsAll(functions);
    }

    private UserDetailsDto getUserDetails(String token) {
        Benutzer user = tokenValidatorService.createUserIfNotExists(tokenValidatorService.getClaimsFromJwt(token));
        String firma = null;
        if (user.getPersonalnummer() != null && user.getPersonalnummer().getFirma() != null) {
            firma = user.getPersonalnummer().getFirma().getName();
        }
        return new UserDetailsDto(user.getAzureId(), user.getFirstName(), user.getLastName(), getUserFunctions(user), firma);
    }

    private List<String> getUserFunctions(Benutzer user) {
        if (user != null && !isNullOrBlank(user.getAzureId())) {
            return azureSSOService.getNestedGroups(user.getAzureId());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean checkIfUserIsEligibleForMAOrTN(String personalnummerString, String authorizationHeader) {
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(personalnummerString);
        if (personalnummer == null) {
            return false;
        }
        if (MitarbeiterType.MITARBEITER.equals(personalnummer.getMitarbeiterType()) && isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_STAMMDATEN_ERFASSEN, FN_VERTRAGSDATEN_ERFASSEN))) {
            return true;
        }
        if (MitarbeiterType.TEILNEHMER.equals(personalnummer.getMitarbeiterType()) && isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), List.of(FN_TN_ONBOARDING))) {
            return true;
        }
        return false;
    }

    @Override
    public Benutzer getBenutzerFromEmail(String email) {
        return benutzerService.findByEmail(email);
    }

}
