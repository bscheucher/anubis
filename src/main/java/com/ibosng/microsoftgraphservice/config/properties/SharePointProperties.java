package com.ibosng.microsoftgraphservice.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class SharePointProperties extends AzureProperties {

    public SharePointProperties(
            @Value("${oneDriveClientId:#{null}}") String clientId,
            @Value("${oneDriveClientSecret:#{null}}") String clientSecret,
            @Value("${oneDriveTenantId:#{null}}") String tenantId,
            @Value("${oneDriveScope:#{null}}") String scope) {
        super(clientId, clientSecret, tenantId, scope);
    }

    @Value("${oneDriveSiteUrl:#{null}}")
    private String siteUrl;

    @Value("${oneDriveUserAnlageNeueBenutzer:#{null}}")
    private String neueBenutzer;

    @Value("${oneDriveUserAnlageAngelegteBenutzerNeu:#{null}}")
    private String angelegteBenutzerNeu;

    @Value("${oneDriveUserAnlageAngelegteBenutzerError:#{null}}")
    private String angelegteBenutzerError;

    @Value("${oneDriveUserAnlageAngelegteBenutzerProcessing:#{null}}")
    private String angelegteBenutzerProcessing;

    @Value("${oneDriveUserAnlageAngelegteBenutzerSuccessful:#{null}}")
    private String angelegteBenutzerSuccessful;

    @Value("${oneDriveSource:#{null}}")
    private String source;

    @Value("${oneDriveProcessing:#{null}}")
    private String processing;

    @Value("${oneDriveSuccessful:#{null}}")
    private String successful;

    @Value("${oneDriveError:#{null}}")
    private String error;

    @Value("${oneDriveBerufe:#{null}}")
    private String berufe;
}
