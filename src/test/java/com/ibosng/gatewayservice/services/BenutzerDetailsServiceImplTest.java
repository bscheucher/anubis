package com.ibosng.gatewayservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.gatewayservice.dtos.user.UserDetailsDto;
import com.ibosng.gatewayservice.services.impl.BenutzerDetailsServiceImpl;
import com.ibosng.gatewayservice.services.impl.TokenValidatorServiceImpl;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BenutzerDetailsServiceImplTest extends BaseIntegrationTest {
    @Mock
    private com.ibosng.dbservice.services.impl.BenutzerServiceImpl benutzerServiceImpl;

    @Mock
    private TokenValidatorServiceImpl tokenValidatorServiceImpl;

    @Mock
    private AzureSSOService azureSSOService;

    @Mock
    private PersonalnummerService personalnummerService;

    @InjectMocks
    private BenutzerDetailsServiceImpl benutzerServiceImplUnderTest;

    @BeforeEach
    public void prepare() {
        Claims mockClaims = Mockito.mock(Claims.class);
        when(tokenValidatorServiceImpl.getClaimsFromJwt(anyString())).thenReturn(mockClaims);
        when(tokenValidatorServiceImpl.getClaimsFromJwt(anyString()).get("oid")).thenReturn("fsadfsdfsd");
        List<String> listFromArraysAsList = Arrays.asList("element1", "element2");
        ArrayList<String> arrayList = new ArrayList<>(listFromArraysAsList);
        when(tokenValidatorServiceImpl.getClaimsFromJwt(anyString()).get("emails")).thenReturn(arrayList);

        benutzerServiceImplUnderTest = new BenutzerDetailsServiceImpl(benutzerServiceImpl, tokenValidatorServiceImpl, azureSSOService, personalnummerService);
    }


    @Test
    public void testGetUserFromToken_UserDoesNotExist() {
        String token = "mockToken";
        String azureId = "mockAzureId";

        Claims mockClaims = mock(Claims.class);
        when(tokenValidatorServiceImpl.getClaimsFromJwt(token)).thenReturn(mockClaims);
        when(mockClaims.get("oid")).thenReturn(azureId);

        when(benutzerServiceImpl.getBenutzerByAzureId(azureId)).thenReturn(null);

        Benutzer result = benutzerServiceImplUnderTest.getUserFromToken(token);

        assertNull(result);

        verify(benutzerServiceImpl, times(1)).getBenutzerByAzureId(azureId);
    }

    @Test
    public void testGetUserFromToken_UserExists() {
        String token = "mockToken";
        String azureId = "mockAzureId";

        Claims mockClaims = mock(Claims.class);
        when(tokenValidatorServiceImpl.getClaimsFromJwt(token)).thenReturn(mockClaims);
        when(mockClaims.get("oid")).thenReturn(azureId);

        Benutzer mockUser = new Benutzer();
        when(benutzerServiceImpl.getBenutzerByAzureId(azureId)).thenReturn(mockUser);

        Benutzer result = benutzerServiceImplUnderTest.getUserFromToken(token);

        assertNotNull(result);
        assertEquals(mockUser, result);

        verify(benutzerServiceImpl, times(1)).getBenutzerByAzureId(azureId);
    }


    @Test
    public void testGetUserDetails() {
        String token = "mockToken";

        Benutzer benutzer = new Benutzer();
        benutzer.setAzureId("testAzureId");
        benutzer.setFirstName("testFirstName");
        benutzer.setLastName("testLastName");
        when(benutzerServiceImplUnderTest.getUserFromToken(anyString())).thenReturn(benutzer).thenReturn(benutzer);
        when(benutzerServiceImpl.getBenutzerByAzureId(anyString())).thenReturn(benutzer).thenReturn(benutzer);
        when(tokenValidatorServiceImpl.createUserIfNotExists(any())).thenReturn(benutzer);

        UserDetailsDto userDetailsDto = benutzerServiceImplUnderTest.getUserDetailsReponse(token).getBody();

        assertAll(
                () -> assertEquals(benutzer.getAzureId(), userDetailsDto.getAzureId()),
                () -> assertEquals(benutzer.getFirstName(), userDetailsDto.getFirstName()),
                () -> assertEquals(benutzer.getLastName(), userDetailsDto.getLastName())
        );

    }

    @Test
    public void testGetBenutzerFromEmail_UserExists() {
        String email = "test@example.com";
        Benutzer expectedBenutzer = new Benutzer();
        expectedBenutzer.setEmail(email);
        expectedBenutzer.setFirstName("Max");
        expectedBenutzer.setLastName("Musterman");

        when(benutzerServiceImpl.findByEmail(email)).thenReturn(expectedBenutzer);

        Benutzer result = benutzerServiceImplUnderTest.getBenutzerFromEmail(email);

        assertNotNull(result, "Result should not be null");
        assertEquals(expectedBenutzer, result, "The returned Benutzer should match the expected Benutzer");

        verify(benutzerServiceImpl, times(1)).findByEmail(email);
    }

}
