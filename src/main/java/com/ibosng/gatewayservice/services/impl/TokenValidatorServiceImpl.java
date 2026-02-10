package com.ibosng.gatewayservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.UserSession;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.impl.UserSessionServiceImpl;
import com.ibosng.gatewayservice.dtos.JwkResponseDto;
import com.ibosng.gatewayservice.dtos.OpenIdConfigurationDto;
import com.ibosng.gatewayservice.services.Gateway2Validation;
import com.ibosng.gatewayservice.services.JwkService;
import com.ibosng.gatewayservice.services.OpenIdConfigurationService;
import com.ibosng.gatewayservice.services.TokenValidatorService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

import static com.ibosng.gatewayservice.utils.Constants.*;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@Service
public class TokenValidatorServiceImpl implements TokenValidatorService {

    @Getter
    @Value("${ssoTenantId:#{null}}")
    private String ssoTenantId;

    @Getter
    @Value("${clockSkewInMilliseconds:5000}")
    private int clockSkewInMilliseconds;

    private final Integer MAX_TOKEN_AGE_SECONDS;

    private final OpenIdConfigurationService openIdConfigurationService;
    private final JwkService jwkService;
    private final UserSessionServiceImpl userSessionService;
    private final BenutzerService benutzerService;
    private final Gateway2Validation gateway2Validation;

    public TokenValidatorServiceImpl(OpenIdConfigurationService openIdConfigurationService,
                                     JwkService jwkService,
                                     UserSessionServiceImpl userSessionService,
                                     BenutzerService benutzerService,
                                     @Value("${login.maxTokenAge:1000}") final Integer MAX_TOKEN_AGE_SECONDS,
                                     Gateway2Validation gateway2Validation
    ) {
        this.openIdConfigurationService = openIdConfigurationService;
        this.jwkService = jwkService;
        this.userSessionService = userSessionService;
        this.benutzerService = benutzerService;
        this.MAX_TOKEN_AGE_SECONDS = MAX_TOKEN_AGE_SECONDS;
        this.gateway2Validation = gateway2Validation;
    }

    public Claims validateSession(String token) throws JwtException {
        Claims tokenClaims = getClaimsFromJwt(token);
        // Check token validity
        if (tokenClaims != null) {
            UserSession userSession = userSessionService.getSessionByToken(token);
            // Session found and active, return claims
            if (userSession != null && userSession.getActive()) {
                return tokenClaims;
            } else if (userSession != null && !userSession.getActive()) {
                // Session found but is invalid
                log.error("Expired session for token: " + token);
                log.error("Session expired at: " + userSession.getInvalidatedOn());
                return null;
            } else {
                // Create new session
                Benutzer benutzer = createUserIfNotExists(tokenClaims);
                if (benutzer != null && tokenClaims != null) {
                    // Check if token is less than X seconds old and create a new session
                    if (isTokenFresh(tokenClaims)) {
                        UserSession newSession = new UserSession();
                        newSession.setToken(token);
                        newSession.setBenutzer(benutzer);
                        newSession.setCreatedOn(LocalDateTime.now());
                        newSession.setActive(true);
                        userSessionService.save(newSession);
                        return tokenClaims;
                    }
                }
            }

        }
        return null;
    }


    protected boolean isTokenFresh(Claims claims) {

        long issuedAtTimeInMillis = claims.getIssuedAt().getTime() + getClockSkewInMilliseconds();
        long currentTimeInMillis = new Date().getTime();
        long differenceInSeconds = (currentTimeInMillis - issuedAtTimeInMillis) / 1000;
        if (differenceInSeconds < MAX_TOKEN_AGE_SECONDS) {
            return true;
        }
        log.info("Issued At: {}", claims.getIssuedAt());
        log.info("Current Time: {}", new Date());
        return false;
    }

    @Override
    public boolean isTokenValid(String token) throws JwtException {
        return token != null && validateSession(token) != null;
    }

    // Parses and verifies the JWT Token, returns the claims contained in the Token
    @Override
    public Claims getClaimsFromJwt(String jwt) throws JwtException {
        Jws<Claims> jws = null;
        String META_DATA_URL = "https://login.microsoftonline.com/" + getSsoTenantId() + "/v2.0/.well-known/openid-configuration";
        log.info("META_DATA_URL: {}", META_DATA_URL);
        try {
            OpenIdConfigurationDto openIdConfigurationDto = openIdConfigurationService.getOpenIdConfiguration(META_DATA_URL);
            JwkResponseDto jwkResponseDto = jwkService.getJwk(openIdConfigurationDto.getJwksUri());

            // Step 2: Extract the `kid` from the JWT header
            String kid = extractKid(jwt);
            //log.info("Extracted kid: {}", kid);

            // Step 3: Find the matching JWK for the `kid`
            JwkResponseDto.JwkKey matchingKey = findMatchingKey(kid, jwkResponseDto);
            //log.info("Matching JWK Key: {}", matchingKey);

            // Step 4: Compute the public key from the matching JWK
            PublicKey publicKey = computePublicKey(matchingKey);
            jws = parseAndVerifyToken(publicKey, jwt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | JsonProcessingException ex) {
            log.error("Token validation failed for token: " + jwt);
        }
        return jws != null ? jws.getPayload() : null;
    }

    protected Jws<Claims> parseAndVerifyToken(PublicKey publicKey, String jwt) throws JwtException {
        return (Jws<Claims>) Jwts.parser().clockSkewSeconds(getClockSkewInMilliseconds() / 1000).verifyWith(publicKey).build().parse(jwt);
    }


    protected PublicKey computePublicKey(JwkResponseDto.JwkKey jwkKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            String modulusBase64Url = jwkKey.getN();
            String exponentBase64Url = jwkKey.getE();

            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(modulusBase64Url));
            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(exponentBase64Url));

            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyFactory.generatePublic(spec);
/*            log.info("Modulus (n): {}", modulusBase64Url);
            log.info("Exponent (e): {}", exponentBase64Url);
            log.info("Computed Public Key: {}", publicKey);*/
            return publicKey;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to compute public key from JWK", ex);
        }
    }

    private String extractKid(String jwt) {
        try {
            // JWT structure: header.payload.signature
            String headerEncoded = jwt.split("\\.")[0];
            String decodedHeader = new String(Base64.getUrlDecoder().decode(headerEncoded));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode headerNode = objectMapper.readTree(decodedHeader);

            String algorithm = headerNode.get("alg").asText();
            if (!"RS256".equals(algorithm)) {
                throw new IllegalArgumentException("Unsupported JWT algorithm: " + algorithm);
            }

            String kid = headerNode.get("kid").asText();
            if (kid == null) {
                throw new IllegalArgumentException("No 'kid' found in JWT header");
            }

            return kid;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to extract 'kid' from JWT header", ex);
        }
    }

    private JwkResponseDto.JwkKey findMatchingKey(String kid, JwkResponseDto jwkResponseDto) {
        return jwkResponseDto.getKeys().stream()
                .filter(key -> key.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No matching JWK found for kid: " + kid));
    }

    public Benutzer createUserIfNotExists(Claims tokenClaims) {
        String azureId = (String) tokenClaims.get(OID);
        String email = (String) tokenClaims.get(EMAIL);
        String firstName = (String) tokenClaims.get(FIRST_NAME);
        String lastName = (String) tokenClaims.get(LAST_NAME);
        String upn = (String) tokenClaims.get(UPN);
        Benutzer benutzer = null;
        if (!isNullOrBlank(upn)) {
            upn = upn.toLowerCase();
            benutzer = benutzerService.findByUpn(upn);
        }
        if (benutzer == null && !isNullOrBlank(azureId)) {
            benutzer = benutzerService.findByAzureId(azureId);
        }
        if (benutzer == null && !isNullOrBlank(email)) {
            email = email.toLowerCase();
            benutzer = benutzerService.findByEmail(email);
        }
        if (benutzer == null && !isNullOrBlank(firstName) && !isNullOrBlank(lastName)) {
            benutzer = benutzerService.findAllByFirstNameAndLastName(firstName, lastName);
        }
        if ((benutzer == null && !isNullOrBlank(upn)) || (benutzer != null && benutzer.getPersonalnummer() == null && !isNullOrBlank(upn))) {
            ResponseEntity<String> personalnummerResponse = gateway2Validation.validateSyncMitarbeiterWithUPN(upn);
            if (personalnummerResponse != null && personalnummerResponse.getStatusCode().is2xxSuccessful()) {
                benutzer = benutzerService.getBenutzerByPersonalnummer(personalnummerResponse.getBody());
                if (benutzer != null && !isNullOrBlank(email)) {
                    benutzer.setEmail(email);
                    benutzer.setChangedBy(GATEWAY_SERVICE);
                    benutzerService.save(benutzer);
                }
            }
        }
        if (benutzer == null) {
            benutzer = new Benutzer();
            benutzer.setStatus(Status.ACTIVE);
            benutzer.setCreatedBy(GATEWAY_SERVICE);
            if (!isNullOrBlank(azureId)) {
                benutzer.setAzureId(azureId);
            }
            if (!isNullOrBlank(email)) {
                benutzer.setEmail(email);
            }
            if (!isNullOrBlank(firstName)) {
                benutzer.setFirstName(firstName);
            }
            if (!isNullOrBlank(lastName)) {
                benutzer.setLastName(lastName);
            }
            if (!isNullOrBlank(upn)) {
                benutzer.setUpn(upn);
            }
            benutzer = benutzerService.save(benutzer);
        } else if (!isNullOrBlank(azureId) && (isNullOrBlank(benutzer.getAzureId()) || !azureId.equals(benutzer.getAzureId()))) {
            benutzer.setAzureId(azureId);
            benutzer.setUpn(upn);
            benutzer.setChangedBy(GATEWAY_SERVICE);
            benutzer = benutzerService.save(benutzer);
        }
        if (!isNullOrBlank(upn) && (isNullOrBlank(benutzer.getUpn()) || !upn.equals(benutzer.getUpn()))) {
            benutzer.setUpn(upn);
            benutzer.setChangedBy(GATEWAY_SERVICE);
            benutzer = benutzerService.save(benutzer);
        }

        if (!isNullOrBlank(firstName) && (isNullOrBlank(benutzer.getFirstName()) || !firstName.equals(benutzer.getFirstName()))) {
            benutzer.setFirstName(firstName);
            benutzer.setChangedBy(GATEWAY_SERVICE);
            benutzer = benutzerService.save(benutzer);
        }
        if (!isNullOrBlank(lastName) && (isNullOrBlank(benutzer.getLastName()) || !lastName.equals(benutzer.getLastName()))) {
            benutzer.setLastName(lastName);
            benutzer.setChangedBy(GATEWAY_SERVICE);
            benutzer = benutzerService.save(benutzer);
        }
        return benutzer;
    }
}
