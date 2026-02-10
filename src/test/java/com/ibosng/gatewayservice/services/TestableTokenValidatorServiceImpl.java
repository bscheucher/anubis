package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.UserSession;
import com.ibosng.dbservice.services.impl.BenutzerServiceImpl;
import com.ibosng.dbservice.services.impl.UserSessionServiceImpl;
import com.ibosng.gatewayservice.services.impl.TokenValidatorServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;
import org.mockito.Mockito;

import java.security.PublicKey;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// In your test package
public class TestableTokenValidatorServiceImpl extends TokenValidatorServiceImpl {

    private final PublicKey mockPublicKey = Mockito.mock(PublicKey.class);
    private final Jws<Claims> mockClaimsJws = Mockito.mock(Jws.class);

    @Getter
    private final Claims mockClaims = Mockito.mock(Claims.class);

    private final UserSession mockUserSession = Mockito.mock(UserSession.class);
    private final BenutzerServiceImpl mockBenutzerService = Mockito.mock(BenutzerServiceImpl.class);

    private final Benutzer mockBenutzer = Mockito.mock(Benutzer.class);

    private final UserSessionServiceImpl mockUserSessionService = Mockito.mock(UserSessionServiceImpl.class);

    public TestableTokenValidatorServiceImpl(OpenIdConfigurationService openIdConfigurationService, JwkService jwkService, UserSessionServiceImpl userSessionService, BenutzerServiceImpl benutzerService, Gateway2Validation gateway2Validation) {
        super(openIdConfigurationService, jwkService, userSessionService, benutzerService, 100, gateway2Validation);
    }

    protected PublicKey computePublicKey(String jwkJsonString) {
        return mockPublicKey;
    }

    @Override
    public Benutzer createUserIfNotExists(Claims tokenClaims) {
        return mockBenutzer;
    }

    @Override
    protected Jws<Claims> parseAndVerifyToken(PublicKey publicKey, String jwt) {
        return mockClaimsJws;
    }

    @Override
    public Claims getClaimsFromJwt(String jwt) {
        if (jwt.equals("invalid.jwt.token")) {
            return null;
        }
        when(mockClaimsJws.getPayload()).thenReturn(mockClaims);
        return super.getClaimsFromJwt(jwt);
    }

    @Override
    public Claims validateSession(String token) {
        when(mockUserSessionService.getSessionByToken(anyString())).thenReturn(mockUserSession);
        when(mockBenutzerService.getBenutzerByAzureId(anyString())).thenReturn(mockBenutzer);
        if (token.equals("valid.jwt.token")) {
            return mockClaims;
        }
        return super.validateSession(token);
    }

    @Override
    public boolean isTokenFresh(Claims claims) {
        if (claims != null) {
            return super.isTokenFresh(claims);
        }
        return false;
    }
}

