package com.ibosng.aiservice.services;

import com.ibosng.aiservice.dtos.ServiceMethodDto;
import com.ibosng.aiservice.dtos.tooldefiinition.ToolDefinitionDto;
import com.ibosng.aiservice.enums.RequestType;

import java.util.List;
import java.util.Map;

public interface ServiceMethodRegistryService {
    List<ServiceMethodDto> getServiceMethods(RequestType requestType);

    List<ToolDefinitionDto> buildToolDefinitions(RequestType type);

    Object callServiceMethod(String methodName, Map<String, Object> params, RequestType type);
}
