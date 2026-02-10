package com.ibosng.moxisservice.services;

public interface MoxisEnvironmentService {
    boolean isProduction();
    String getProcessId(boolean isExternalWithHandySignatur);
}
