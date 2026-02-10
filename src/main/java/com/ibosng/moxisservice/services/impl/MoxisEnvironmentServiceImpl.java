package com.ibosng.moxisservice.services.impl;

import com.ibosng.moxisservice.services.MoxisEnvironmentService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MoxisEnvironmentServiceImpl implements MoxisEnvironmentService {
    @Getter
    @Value("${isProduction:#{null}}")
    private String isProduction;

    @Getter
    @Value("${moxisProcessInstanceWithSms:#{null}}")
    private String processInstanceWithSms;

    @Getter
    @Value("${moxisProcessInstanceWithoutSms:#{null}}")
    private String processInstanceWithoutSms;

    @Override
    public boolean isProduction() {
        return Boolean.parseBoolean(getIsProduction());
    }

    @Override
    public String getProcessId(boolean isExternalWithHandySignatur) {
        if(isExternalWithHandySignatur){
            return getProcessInstanceWithoutSms();
        } else {
            return getProcessInstanceWithSms();
        }
    }


}
