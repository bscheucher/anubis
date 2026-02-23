package com.ibosng.gatewayservice.security;

import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.ibosng.gatewayservice.utils.Constants.FN_MA_ZEITEN_LESEN;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequiredRolesAspectTest {
    @Mock
    BenutzerDetailsService benutzerDetailsService;

    @Mock
    ProceedingJoinPoint proceedingJoinPoint;

    @InjectMocks
    private RequiredRolesAspect requiredRolesAspect;

    @BeforeEach
    public void setUp() {
        var request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer mockToken");
        var response = new MockHttpServletResponse();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
    }

    private RequiredRoles getRequiredRolesAnnotation(String[] roles) {
        return new RequiredRoles()
        {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] value() {
                return new String[]{FN_MA_ZEITEN_LESEN};
            }
        };
    }

    @Test
    public void testRequiredRolesAspect_UserHasRoles_ShouldProceed() throws Throwable {
        var testRoles = new String[]{FN_MA_ZEITEN_LESEN};
        when(proceedingJoinPoint.proceed()).thenReturn(null);
        when(benutzerDetailsService.isUserEligible(anyString(), eq(List.of(testRoles)))).thenReturn(true);
        var requiredRolesAnnotation = getRequiredRolesAnnotation(testRoles);
        requiredRolesAspect.checkIfUserHasRequiredRoles(proceedingJoinPoint, requiredRolesAnnotation);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    public void testRequiredRolesAspect_UserDoesNotHaveRoles_ShouldRespondWithForbidden() throws Throwable {
        var testRoles = new String[]{FN_MA_ZEITEN_LESEN};
        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(false);
        var requiredRolesAnnotation = getRequiredRolesAnnotation(testRoles);
        requiredRolesAspect.checkIfUserHasRequiredRoles(proceedingJoinPoint, requiredRolesAnnotation);
        verify(proceedingJoinPoint, times(0)).proceed();
        var requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null; // Plain assertion not relevant for test. Should always be true
        var response = requestAttributes.getResponse();
        assert response != null; // Plain assertion not relevant for test. Should always be true
        assertEquals(response.getStatus(), HttpStatus.FORBIDDEN.value());
    }
}
