package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.masterdata.IbisFirma;

public interface LHREnvironmentService {
    boolean isProduction();
    Integer getFaNr(IbisFirma ibisFirma);
    String getFaKz(IbisFirma ibisFirma);
    String getLogin(String login);
}
