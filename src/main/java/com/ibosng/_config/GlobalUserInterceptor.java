package com.ibosng._config;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.utils.Helpers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.jboss.logging.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class GlobalUserInterceptor implements HandlerInterceptor {
    private final static String USER_ID_KEY = "user_id";
    private final static String USER_HEADER = "auth-user-id";

    private final BenutzerDetailsService benutzerDetailsService; // For Token
    private final BenutzerService benutzerService;             // For ID lookup
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. Skip whitelist (probe, swagger, etc.)
        if (isWhitelisted(request.getRequestURI())) {
            return true;
        }

        Benutzer benutzer = null;

        // 2. Strategy A: Try JWT Token (Primary)
        String token = Helpers.getTokenFromRequest(request);
        if (token != null) {
            try {
                benutzer = benutzerDetailsService.getUserFromToken(token);
            } catch (Exception e) {
                // Token invalid or expired, log if necessary but allow fallback
            }
        }

        // 3. Strategy B: Try Header (Fallback/Legacy for Moxis/LHR)
        if (benutzer == null) {
            String userId = request.getHeader(USER_HEADER);
            if (NumberUtils.isParsable(userId)) {
                benutzer = benutzerService.findById(NumberUtils.toInt(userId, 0)).orElse(null);
            }
        }

        // 4. Populate Context
        if (benutzer != null) {
            globalUserHolder.setUsername(benutzer.getEmail());
            globalUserHolder.setUserId(benutzer.getId());
            MDC.put(USER_ID_KEY, benutzer.getId());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(USER_ID_KEY);
    }

    private boolean isWhitelisted(String uri) {
        return uri.equals("/probe") ||
                uri.contains("/swagger-ui") ||
                uri.contains("/ai-engine") ||
                uri.contains("/api-docs");
    }
}