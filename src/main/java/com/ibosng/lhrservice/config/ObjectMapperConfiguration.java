package com.ibosng.lhrservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        // This property was dynamically (!) added in the original MoxisService
        // This could potentially create side effects as it was not present in the other services.
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // The ObjectMapper was originally only defined for the moxis and lhr services.
        // Suspicion: It now being defined "globally" means that without
        // "failing on unknown properties" being disabled, mappings might break
        // in other services (as has happened in the VertragsdatenDto in the
        // dbservice). This is a temporary, broad solution to prevent unforseen
        // bugs.
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }
}