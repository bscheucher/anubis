package com.ibosng.microsoftgraphservice.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@EqualsAndHashCode(callSuper = true)
@Data
public class SSOProperties extends AzureProperties {

    public SSOProperties(
            @Value("${ssoClientId:#{null}}") String clientId,
            @Value("${ssoClientSecret:#{null}}") String clientSecret,
            @Value("${ssoTenantId:#{null}}") String tenantId,
            @Value("${mailScope:#{null}}") String scope) {
        super(clientId, clientSecret, tenantId, scope);
    }
}
