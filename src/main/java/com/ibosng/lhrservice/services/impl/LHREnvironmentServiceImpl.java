package com.ibosng.lhrservice.services.impl;


import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.ibosng.lhrservice.utils.Constants.TEST_TENANT_UPN_PREFIX;

@Service
public class LHREnvironmentServiceImpl implements LHREnvironmentService {
    @Value("${isProduction:#{null}}")
    private String isProduction;

    @Getter
    @Value("${lhrTestFirmaKZ:#{null}}")
    private String lhrTestFirmaKZ;

    @Getter
    @Value("${lhrTestFirmaNr:#{null}}")
    private Integer lhrTestFirmaNr;

    @Override
    public boolean isProduction() {
        return Boolean.parseBoolean(isProduction);
    }

    @Override
    public Integer getFaNr(IbisFirma ibisFirma) {
        if (isProduction()) {
            return ibisFirma.getLhrNr();
        } else {
            return getLhrTestFirmaNr();
        }
    }

    @Override
    public String getFaKz(IbisFirma ibisFirma) {
        if (isProduction()) {
            return ibisFirma.getLhrKz();
        } else {
            return getLhrTestFirmaKZ();
        }
    }

    @Override
    public String getLogin(String login) {
        login = login.split("@")[0];
        if (!isProduction() && login.contains(TEST_TENANT_UPN_PREFIX)) {
            login = login.replace(TEST_TENANT_UPN_PREFIX, "");
        }
        return login;
    }
}
