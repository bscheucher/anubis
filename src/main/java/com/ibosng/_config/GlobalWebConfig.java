package com.ibosng._config;

import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class GlobalWebConfig implements WebMvcConfigurer {

    private final BenutzerDetailsService benutzerDetailsService;
    private final BenutzerService benutzerService;
    private final GlobalUserHolder globalUserHolder;

    @Value("${cors.allowedOrigins}")
    private List<String> allowedOrigins;

    @Bean
    public GlobalUserInterceptor globalUserInterceptor() {
        return new GlobalUserInterceptor(benutzerDetailsService, benutzerService, globalUserHolder);
    }

    // This bean was only present in the validator service and disabled csrf protection for all of its endpoints.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalUserInterceptor())
                .addPathPatterns("/**"); // Apply to all paths
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
        if (allowedOrigins.isEmpty()) {
            throw new IllegalArgumentException(
                    "Property 'cors.allowedOrigins' is required. At least one allowed origin needs to be set."
            );
        }
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setExposedHeaders(Collections.singletonList("Content-Disposition"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}

