package com.ibosng.microsoftgraphservice.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

/**
 * Condition controlling whether the real SharePoint implementation should be active.
 * <p>
 * Logic:
 * - If active profile is neither "test" nor ("develop" or "localdev"), real implementation is enabled.
 * - If active profile is "test" or ("develop"/"localdev"), then the real implementation is enabled only when
 * the property/env var "useSharePointLocally" is true. Otherwise, the local implementation is enabled.
 */
public class SharePointCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
        var env = context.getEnvironment();

        var profiles = Arrays.asList(env.getActiveProfiles());
        boolean isTest = profiles.contains("test");
        boolean isDevelop = profiles.contains("localdev");

        if (!isTest && !isDevelop) {
            return true;
        }
        return env.getProperty("useSharePointLocally", Boolean.class, false);
    }

    /**
     * Reverse of {@link SharePointCondition}: matches when the real implementation should NOT be used.
     * Useful for enabling local/mock implementation.
     */
    public static class Reverse implements Condition {
        @Override
        public boolean matches(@NotNull ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
            return !new SharePointCondition().matches(context, metadata);
        }
    }
}
