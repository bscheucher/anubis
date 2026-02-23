package com.ibosng.gatewayservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.services.impl.BenutzerServiceImpl;
import com.ibosng.dbservice.services.impl.UserSessionServiceImpl;
import com.ibosng.gatewayservice.dtos.JwkResponseDto;
import com.ibosng.gatewayservice.dtos.OpenIdConfigurationDto;
import com.ibosng.gatewayservice.services.impl.TokenValidatorServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Disabled
public class TokenValidatorServiceImplTest extends BaseIntegrationTest {

    @Mock
    private OpenIdConfigurationService openIdConfigurationService;

    @Mock
    private JwkService jwkService;

    @Mock
    private UserSessionServiceImpl userSessionService;

    @Mock
    private BenutzerServiceImpl benutzerService;
    @InjectMocks
    private TestableTokenValidatorServiceImpl tokenValidatorService;

    @Test
    public void testGetClaimsFromJwt() {
        String jwt = "sample.jwt.token";

        OpenIdConfigurationDto mockOpenIdConfig = new OpenIdConfigurationDto();
        mockOpenIdConfig.setJwksUri("mockJwksUri");
        try {
            when(openIdConfigurationService.getOpenIdConfiguration(anyString()))
                    .thenReturn(mockOpenIdConfig);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JwkResponseDto.JwkKey mockJwkKey = new JwkResponseDto.JwkKey();
        mockJwkKey.setKty("RSA");
        mockJwkKey.setE("AQAB");
        mockJwkKey.setN("modulus");

        JwkResponseDto mockJwkResponse = new JwkResponseDto();
        mockJwkResponse.setKeys(Collections.singletonList(mockJwkKey));

        try {
            when(jwkService.getJwk("mockJwksUri")).thenReturn(mockJwkResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Claims claims = tokenValidatorService.getClaimsFromJwt(jwt);

        assertNotNull(claims);
    }

    @Test
    public void testCreateUserIfNotExists_UserDoesNotExist() {
        String token = "mockToken";
        String azureId = "mockAzureId";

        when(benutzerService.getBenutzerByAzureId(azureId)).thenReturn(null);

        Benutzer savedBenutzer = new Benutzer();
        when(benutzerService.save(any(Benutzer.class))).thenReturn(savedBenutzer);

        Claims mockClaims = mock(Claims.class);
        when(benutzerService.getBenutzerByAzureId(anyString())).thenReturn(savedBenutzer);
        Benutzer result = tokenValidatorService.createUserIfNotExists(mockClaims);

        assertNotNull(result);
    }

    @Test
    public void testCreateUserIfNotExists_UserExists() throws JsonProcessingException {
        String token = "mockToken";

        when(benutzerService.getBenutzerByAzureId(anyString())).thenReturn(new Benutzer());
        JwkResponseDto mockJwkResponseDto = new JwkResponseDto();
        mockJwkResponseDto.setKeys(Collections.singletonList(mock(JwkResponseDto.JwkKey.class)));
        Claims mockClaims = mock(Claims.class);
        OpenIdConfigurationDto mockOpenIdConfig = new OpenIdConfigurationDto();
        mockOpenIdConfig.setJwksUri("mockJwksUri");
        when(openIdConfigurationService.getOpenIdConfiguration(anyString()))
                .thenReturn(mockOpenIdConfig);
        when(jwkService.getJwk(any())).thenReturn(mockJwkResponseDto);
        when(tokenValidatorService.getClaimsFromJwt(token)).thenReturn(mockClaims);
        Benutzer result = tokenValidatorService.createUserIfNotExists(mockClaims);

        assertNotNull(result);

        verify(benutzerService, never()).save(any(Benutzer.class));
    }


    @Test
    public void testParseAndVerifyTokenWithValidToken() {
        String validJwt = "valid.jwt.token";

        try {
            PublicKey publicKey = tokenValidatorService.computePublicKey("dsfa");

            OpenIdConfigurationDto mockOpenIdConfig = new OpenIdConfigurationDto();
            mockOpenIdConfig.setJwksUri("mockJwksUri");
            when(openIdConfigurationService.getOpenIdConfiguration(anyString()))
                    .thenReturn(mockOpenIdConfig);

            JwkResponseDto.JwkKey mockJwkKey = new JwkResponseDto.JwkKey();
            mockJwkKey.setKty("RSA");
            mockJwkKey.setE("AQAB");
            mockJwkKey.setN("modulus");

            JwkResponseDto mockJwkResponse = new JwkResponseDto();
            mockJwkResponse.setKeys(Collections.singletonList(mockJwkKey));
            when(jwkService.getJwk("mockJwksUri")).thenReturn(mockJwkResponse);

            Jws<Claims> claims = tokenValidatorService.parseAndVerifyToken(publicKey, validJwt);

            assertNotNull(claims, "Expected valid claims");
        } catch (JsonProcessingException e) {
            fail("Error generating dummy public key: " + e.getMessage());
        }
    }

    @Test
    public void testIsTokenValidWithValidToken() {

        String validJwt = "valid.jwt.token";

        OpenIdConfigurationDto mockOpenIdConfig = new OpenIdConfigurationDto();
        mockOpenIdConfig.setJwksUri("mockJwksUri");
        try {
            when(openIdConfigurationService.getOpenIdConfiguration(anyString()))
                    .thenReturn(mockOpenIdConfig);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JwkResponseDto.JwkKey mockJwkKey = new JwkResponseDto.JwkKey();
        mockJwkKey.setKty("RSA");
        mockJwkKey.setE("AQAB");
        mockJwkKey.setN("modulus");

        JwkResponseDto mockJwkResponse = new JwkResponseDto();
        mockJwkResponse.setKeys(Collections.singletonList(mockJwkKey));

        try {
            when(jwkService.getJwk("mockJwksUri")).thenReturn(mockJwkResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        boolean isValid = tokenValidatorService.isTokenValid(validJwt);

        assertTrue(isValid, "Expected the token to be valid");
    }

    @Test
    public void testIsTokenValidWithInvalidToken() {

        String invalidJwt = "invalid.jwt.token";

        OpenIdConfigurationDto mockOpenIdConfig = new OpenIdConfigurationDto();
        mockOpenIdConfig.setJwksUri("mockJwksUri");
        try {
            when(openIdConfigurationService.getOpenIdConfiguration(anyString()))
                    .thenReturn(mockOpenIdConfig);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JwkResponseDto.JwkKey mockJwkKey = new JwkResponseDto.JwkKey();
        mockJwkKey.setKty("RSA");
        mockJwkKey.setE("AQAB");
        mockJwkKey.setN("modulus");

        JwkResponseDto mockJwkResponse = new JwkResponseDto();
        mockJwkResponse.setKeys(Collections.singletonList(mockJwkKey));
        try {
            when(jwkService.getJwk("mockJwksUri")).thenReturn(mockJwkResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        boolean isValid = tokenValidatorService.isTokenValid(invalidJwt);
        assertFalse(isValid, "Expected the token to be invalid");
    }

    @Test
    public void testComputePublicKeyWithValidJwk() throws JsonProcessingException {

        String validJwkJson = "valid.jwk.json";

        JwkResponseDto.JwkKey mockValidJwkKey = new JwkResponseDto.JwkKey();
        mockValidJwkKey.setKty("RSA");
        mockValidJwkKey.setE("AQAB");
        mockValidJwkKey.setN("modulus");

        JwkResponseDto mockValidJwkResponse = new JwkResponseDto();
        mockValidJwkResponse.setKeys(Collections.singletonList(mockValidJwkKey));
        when(jwkService.getJwk("mockJwksUri")).thenReturn(mockValidJwkResponse);

        PublicKey publicKey = null;
        try {
            publicKey = tokenValidatorService.computePublicKey(validJwkJson);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNotNull(publicKey, "Expected a valid public key");
    }

    @Test
    public void testIsTokenValidWithNullToken() {
        // Null JWT token
        String nullJwt = null;

        boolean isValid = tokenValidatorService.isTokenValid(nullJwt);

        assertFalse(isValid, "Expected the token to be invalid for null input");
    }

    @Test
    public void testIsTokenValidWithExpiredToken() {
        // Expired JWT token
        String expiredJwt = "expired.jwt.token";

        // Mock the getClaimsFromJwt method to throw ExpiredJwtException
        TokenValidatorServiceImpl tokenValidatorServiceImpl = mock(TokenValidatorServiceImpl.class);
        when(tokenValidatorServiceImpl.getClaimsFromJwt(expiredJwt)).thenThrow(ExpiredJwtException.class);

        boolean isValid = tokenValidatorServiceImpl.isTokenValid(expiredJwt);

        assertFalse(isValid, "Expected the token to be invalid due to expiration");
    }

    @Test
    public void testIsTokenValidThrowsNoSuchAlgorithmException() throws JsonProcessingException {

        String jwt = "sample.jwt.token";

        OpenIdConfigurationDto mockOpenIdConfig = new OpenIdConfigurationDto();
        mockOpenIdConfig.setJwksUri("mockJwksUri");

        when(openIdConfigurationService.getOpenIdConfiguration(anyString())).thenReturn(mockOpenIdConfig);
        when(jwkService.getJwk(mockOpenIdConfig.getJwksUri())).thenReturn(new JwkResponseDto());

        Throwable throwable = assertThrows(Throwable.class, () -> {
            tokenValidatorService.isTokenValid(jwt);
        });

        assertEquals(NullPointerException.class, throwable.getClass());
    }

    @Test
    public void testComputePublicKeyWithDifferentAlgorithm() throws JsonProcessingException {
        String jwkJsonWithDifferentAlgorithm = "jwk.json.with.different.algorithm";

        JwkResponseDto.JwkKey mockJwkKey = new JwkResponseDto.JwkKey();
        mockJwkKey.setKty("HS256"); // Use a different algorithm
        // mockJwkKey.setK("secretKey");

        JwkResponseDto mockJwkResponse = new JwkResponseDto();
        mockJwkResponse.setKeys(Collections.singletonList(mockJwkKey));

        when(jwkService.getJwk("mockJwksUri")).thenReturn(mockJwkResponse);

        PublicKey publicKey = tokenValidatorService.computePublicKey(jwkJsonWithDifferentAlgorithm);

        assertNotNull(publicKey);
    }

    @Test
    public void testGetClaimsFromJwtWithMultipleKeys() throws JsonProcessingException {
        String jwt = "sample.jwt.token";

        OpenIdConfigurationDto mockOpenIdConfig = new OpenIdConfigurationDto();
        mockOpenIdConfig.setJwksUri("mockJwksUri");

        when(openIdConfigurationService.getOpenIdConfiguration(anyString())).thenReturn(mockOpenIdConfig);

        JwkResponseDto.JwkKey key1 = new JwkResponseDto.JwkKey();
        key1.setKty("RSA");
        key1.setE("AQAB");
        key1.setN("modulus1");

        JwkResponseDto.JwkKey key2 = new JwkResponseDto.JwkKey();
        key2.setKty("RSA");
        key2.setE("AQAB");
        key2.setN("modulus2");

        JwkResponseDto mockJwkResponse = new JwkResponseDto();
        mockJwkResponse.setKeys(Arrays.asList(key1, key2));

        when(jwkService.getJwk("mockJwksUri")).thenReturn(mockJwkResponse);

        Claims claims = tokenValidatorService.getClaimsFromJwt(jwt);

        assertNotNull(claims, "Expected valid claims with multiple keys");
    }

    @Test
    public void testGetClaimsFromJwtWithUnparsableToken() throws JsonProcessingException {
        String unparsableJwt = "unparsable.jwt.token";

        OpenIdConfigurationDto mockOpenIdConfig = new OpenIdConfigurationDto();
        mockOpenIdConfig.setJwksUri("mockJwksUri");

        when(openIdConfigurationService.getOpenIdConfiguration(anyString())).thenReturn(mockOpenIdConfig);

        JwkResponseDto.JwkKey mockJwkKey = new JwkResponseDto.JwkKey();
        mockJwkKey.setKty("RSA");
        mockJwkKey.setE("AQAB");
        mockJwkKey.setN("modulus");

        JwkResponseDto mockJwkResponse = new JwkResponseDto();
        mockJwkResponse.setKeys(Collections.singletonList(mockJwkKey));

        when(jwkService.getJwk("mockJwksUri")).thenReturn(mockJwkResponse);

        Claims claims = tokenValidatorService.getClaimsFromJwt(unparsableJwt);

        assertEquals(claims.size(), 0);
    }

}

