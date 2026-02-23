package com.ibosng.gatewayservice.security;

import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Aspect
@Component
public class RequiredRolesAspect {

    private final BenutzerDetailsService benutzerDetailsService; // Required for checking if user has required roles

    public RequiredRolesAspect(BenutzerDetailsService benutzerDetailsService) {
        this.benutzerDetailsService = benutzerDetailsService;
    }

    /**
     * Behaviour implementation for {@link RequiredRoles} annotation.
     * Checks if the user in the servlet request has all the required roles listen in the annotation.
     * @param joinPoint AspectJ join point
     * @param requiredRoles The REST handler's {@code RequiredRoles} annotation instance
     */
    @Around("@annotation(requiredRoles)")
    public Object checkIfUserHasRequiredRoles(ProceedingJoinPoint joinPoint, RequiredRoles requiredRoles) throws Throwable {
        // Setup data
        var requiredRolesList = requiredRoles.value();
        var requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null; // The RequiredRoles annotation is only valid in a request context
        var request = requestAttributes.getRequest();
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        var token = getTokenFromAuthorizationHeader(authHeader);

        // Actual role checking logic
        if (!benutzerDetailsService.isUserEligible(token, List.of(requiredRolesList))) {
            var response = requestAttributes.getResponse();
            assert response != null; // As above: The RequiredRoles annotation is only valid in a request context
            response.sendError(HttpStatus.FORBIDDEN.value());
            return null;
        }

        return joinPoint.proceed();
    }
}
