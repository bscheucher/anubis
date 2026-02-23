package com.ibosng.gatewayservice.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for REST controller handlers to only allow authenticated users with
 * the supplied list of roles to access the resource.
 * Will respond with a FORBIDDEN response if the requesting user does not have all the required roles assigned.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiredRoles {
    /**
     * List of the roles the requesting user is required to have, to be able to access the protected resource.
     */
    String[] value();
}
