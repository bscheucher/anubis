package com.ibosng.microsoftgraphservice.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class MailProperties extends AzureProperties {
    @Value("${mailUserId:#{null}}")
    private String userId;

    public MailProperties(
            @Value("${mailClientId:#{null}}") String clientId,
            @Value("${mailClientSecret:#{null}}") String clientSecret,
            @Value("${mailTenantId:#{null}}") String tenantId,
            @Value("${mailScope:#{null}}") String scope) {
        super(clientId, clientSecret, tenantId, scope);
    }
}
