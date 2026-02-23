package com.ibosng.lhrservice.client;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

public class LHRCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
        var env = context.getEnvironment();

        boolean isTest = Arrays.asList(env.getActiveProfiles()).contains("test");

        boolean isLocalDev = Arrays.asList(env.getActiveProfiles()).contains("localdev");
        boolean isLocalDevAndSpecificallyEnabled = isLocalDev && env.getProperty("useLhrApi", Boolean.class, false);

        if (!isTest && !isLocalDev) return true;
        return isLocalDevAndSpecificallyEnabled;
    }

    public static class Reverse implements Condition {

        @Override
        public boolean matches(@NotNull ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
            return !new LHRCondition().matches(context, metadata);
        }
    }
}
