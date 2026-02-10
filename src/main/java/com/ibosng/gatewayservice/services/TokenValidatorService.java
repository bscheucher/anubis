package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.entities.Benutzer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

public interface TokenValidatorService {
    boolean isTokenValid(String token) throws JwtException;
    Claims getClaimsFromJwt(String jwt);
    Benutzer createUserIfNotExists(Claims tokenClaims);
}
