package com.ibosng.microsoftgraphservice.services.impl;

import com.ibosng.microsoftgraphservice.services.MSEnvironmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MSEnvironmentServiceImpl implements MSEnvironmentService {
    @Value("${isProduction:#{null}}")
    private String isProduction;

    @Override
    public boolean isProduction() {
        return Boolean.parseBoolean(isProduction);
    }

}
