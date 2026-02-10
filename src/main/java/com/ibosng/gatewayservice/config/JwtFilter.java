package com.ibosng.gatewayservice.config;

import com.ibosng.gatewayservice.services.TokenValidatorService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromRequest;

@Component
@Order(1)
@Slf4j
@Profile("!test")
public class JwtFilter implements Filter {

    private final TokenValidatorService tokenValidatorService;

    public JwtFilter(TokenValidatorService tokenValidatorService) {
        this.tokenValidatorService = tokenValidatorService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            // Let health check requests pass through
            if (request.getRequestURI().equals("/probe") ||
                    request.getRequestURI().contains("/swagger-ui") ||
                    request.getRequestURI().contains("/ai-engine") ||
                    request.getRequestURI().contains("/api-docs")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            String token = getTokenFromRequest(request);
            if (tokenValidatorService.isTokenValid(token)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\":\"Unauthorized: Invalid Token\"}");
                response.getWriter().flush();
            }
        } catch (ExpiredJwtException ex) {
            log.error("JWT Filter Error: Token expired: ", ex);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"Token has expired\"}");
            response.getWriter().flush();
        } catch (MalformedJwtException ex) {
            log.error("JWT Filter Error: Invalid token: ", ex);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"Token is invalid\"}");
            response.getWriter().flush();
        } catch (Exception ex) {
            log.error("JWT Filter Error: Internal error: ", ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"Internal Server Error\"}");
            response.getWriter().flush();
        }
    }
}
