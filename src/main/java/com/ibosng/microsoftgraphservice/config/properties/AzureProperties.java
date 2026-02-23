package com.ibosng.microsoftgraphservice.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AzureProperties {
    private String clientId;
    private String clientSecret;
    private String tenantId;
    private String scope;
}
