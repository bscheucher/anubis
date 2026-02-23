package com.ibosng.aiservice.services.impl;

import com.ibosng.aiservice.annotations.MethodDescription;
import com.ibosng.aiservice.annotations.ParameterDescription;
import com.ibosng.aiservice.dtos.ParameterDescriptionDto;
import com.ibosng.aiservice.dtos.ServiceMethodDto;
import com.ibosng.aiservice.dtos.tooldefiinition.ToolDefinitionDto;
import com.ibosng.aiservice.dtos.tooldefiinition.ToolFunctionDto;
import com.ibosng.aiservice.dtos.tooldefiinition.ToolParameterPropertiesDto;
import com.ibosng.aiservice.dtos.tooldefiinition.ToolParametersDto;
import com.ibosng.aiservice.enums.RequestType;
import com.ibosng.aiservice.services.ServiceMethodRegistryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ibosng.aiservice.utils.Helpers.convertValue;
import static com.ibosng.aiservice.utils.Helpers.mapJavaTypeToOpenAI;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;


@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceMethodRegistryServiceImpl implements ServiceMethodRegistryService {

    private final ApplicationContext applicationContext;

    @Override
    public List<ServiceMethodDto> getServiceMethods(RequestType requestType) {
        List<ServiceMethodDto> methods = new ArrayList<>();
        try {
            // Get the service class using the RequestType's controller mapping
            String serviceClassName = requestType.getServiceClassName();
            if(isNullOrBlank(serviceClassName)) {
                return methods;
            }
            Class<?> serviceClass = Class.forName(requestType.getServiceClassName());

            // Extract methods from the class
            for (Method method : serviceClass.getDeclaredMethods()) {
                ServiceMethodDto methodDto = new ServiceMethodDto();
                methodDto.setMethodName(method.getName());
                methodDto.setReturnType(method.getReturnType().getSimpleName());

                // Extract parameters
                List<ParameterDescriptionDto> parameterDescriptions = new ArrayList<>();
                Parameter[] methodParameters = method.getParameters();
                
                // Check if method has parameter descriptions annotation
                ParameterDescription paramDescAnnotation = method.getAnnotation(ParameterDescription.class);
                Map<String, String> paramDescriptions = new HashMap<>();
                
                if (paramDescAnnotation != null) {
                    // Build a map of parameter names to descriptions
                    for (ParameterDescription.Param param : paramDescAnnotation.value()) {
                        paramDescriptions.put(param.name(), param.description());
                    }
                }
                
                // Process each parameter
                for (Parameter parameter : methodParameters) {
                    String paramName = parameter.getName();
                    String paramType = parameter.getType().getSimpleName();
                    String description = paramDescriptions.getOrDefault(paramName, "No description available");
                    
                    parameterDescriptions.add(new ParameterDescriptionDto(paramName, paramType, description));
                }
                
                methodDto.setParameters(parameterDescriptions);
                
                if (method.isAnnotationPresent(MethodDescription.class)) {
                    methodDto.setDescription(method.getAnnotation(MethodDescription.class).value());
                } else {
                    methodDto.setDescription("Keine Beschreibung verfügbar");
                }

                methods.add(methodDto);
            }
        } catch (ClassNotFoundException e) {
            log.error("Service class not found for RequestType: {}", requestType, e);
        }
        return methods;
    }

    @Override
    public List<ToolDefinitionDto> buildToolDefinitions(RequestType type) {
        List<ServiceMethodDto> serviceMethods = getServiceMethods(type);
        List<ToolDefinitionDto> tools = new ArrayList<>();

        for (ServiceMethodDto method : serviceMethods) {
            // Populate parameter properties
            Map<String, ToolParameterPropertiesDto> properties = new HashMap<>();
            List<String> requiredParameters = new ArrayList<>();
            
            for (ParameterDescriptionDto param : method.getParameters()) {
                String paramType = param.getType(); // The type (e.g., "String")
                String paramName = param.getName(); // The name (e.g., "location")
                String paramDesc = param.getDescription(); // The description

                ToolParameterPropertiesDto property = new ToolParameterPropertiesDto();
                property.setType(paramType.toLowerCase()); // Set type (e.g., "string")
                property.setDescription(paramDesc);

                if (paramType.contains("List") || paramType.contains("java.util.List") || paramType.endsWith("[]")) {
                    property.setType("array");  // ✅ OpenAI expects "array"

                    // Define array items type (assuming list of strings for now)
                    Map<String, String> items = new HashMap<>();
                    items.put("type", "string"); // 🔹 Ensure it's properly formatted
                    property.setItems(items);
                } else {
                    // Standard types (string, int, etc.)
                    property.setType(mapJavaTypeToOpenAI(paramType));
                }

                properties.put(paramName, property);
                requiredParameters.add(paramName); // Add to required list
            }

            // Create parameters object
            ToolParametersDto parameters = new ToolParametersDto();
            parameters.setType("object");
            parameters.setProperties(properties);
            parameters.setRequired(requiredParameters);

            // Create function object
            ToolFunctionDto function = new ToolFunctionDto();
            function.setName(method.getMethodName());
            function.setDescription(method.getDescription());
            function.setParameters(parameters);

            // Create tool definition
            ToolDefinitionDto tool = new ToolDefinitionDto();
            tool.setType("function");
            tool.setFunction(function);

            tools.add(tool);
        }

        return tools;
    }

    @Override
    public Object callServiceMethod(String methodName, Map<String, Object> params, RequestType type) {
        try {
            // Get the service class associated with the RequestType
            Class<?> serviceClass = Class.forName(type.getServiceClassName());
            Object serviceInstance = applicationContext.getBean(serviceClass);

            // Find the matching method by name
            Method[] methods = serviceClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    // Prepare method parameters
                    Object[] args = prepareMethodArguments(method, params);

                    // Invoke the method dynamically
                    Object result = method.invoke(serviceInstance, args);
                    return result != null ? result : "No response from service method.";
                }
            }
            throw new RuntimeException("Method not found: " + methodName);
        } catch (Exception e) {
            log.error("Failed to call service method: {}", e.getMessage());
            throw new RuntimeException("Error invoking service method: " + methodName, e);
        }
    }

    private Object[] prepareMethodArguments(Method method, Map<String, Object> params) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramName = parameter.getName();
            Class<?> paramType = parameter.getType();

            if (params.containsKey(paramName)) {
                Object value = params.get(paramName);
                if (paramType.isInstance(value)) {
                    args[i] = value;
                } else {
                    // Convert value to the required type (if needed)
                    args[i] = convertValue(value, paramType);
                }
            } else {
                args[i] = null; // Use null for missing parameters
            }
        }

        return args;
    }
}
