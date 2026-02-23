package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.gatewayservice.dtos.user.UserDetailsDto;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BenutzerDetailsService {
    ResponseEntity<UserDetailsDto> getUserDetailsReponse(String token);
    Benutzer getUserFromToken(String token);
    boolean isUserEligible(String token, List<String> functions);
    boolean isUserEligible(Benutzer benutzer, List<String> functions);

    boolean checkIfUserIsEligibleForMAOrTN(String personalnummerString, String authorizationHeader);

    Benutzer getBenutzerFromEmail(String email);
}
