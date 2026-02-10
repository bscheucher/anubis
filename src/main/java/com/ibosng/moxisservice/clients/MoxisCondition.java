package com.ibosng.moxisservice.clients;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

/**
 * Defines a condition matching when
 * a) the profile is not "test" nor "localdev"
 * b) the profile is "localdev" and "useTextMoxis" is set true
 * <p>
 * This condition should be used to guard access to the test Moxis instance;
 * If it does not match, a Mock should be used.
 */
public class MoxisCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
        var env = context.getEnvironment();

        boolean isTest = Arrays.asList(env.getActiveProfiles()).contains("test");

        boolean isLocalDev = Arrays.asList(env.getActiveProfiles()).contains("localdev");
        boolean isLocalDevAndSpecificallyEnabled = isLocalDev && env.getProperty("useTestMoxis", Boolean.class, false);

        if (!isTest && !isLocalDev) return true;
        return isLocalDevAndSpecificallyEnabled;
    }


    public static class Reverse implements Condition {

        @Override
        public boolean matches(@NotNull ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
            return !new MoxisCondition().matches(context, metadata);
        }
    }
}
