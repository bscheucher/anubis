package com.ibosng;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.Architectures;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class LayeredArchitectureTest {

    private static final String[] MODULES_TO_SCAN = {
            "com.ibosng.personalverwaltung",
            "com.ibosng.projektplanung",
            "com.ibosng.projektumsetzung"
    };
    private static final String WEB_PKG = "..web..";
    private static final String DOMAIN_PKG = "..domain..";
    private static final String PERSISTENCE_PKG = "..persistence..";

    @Test
    void layeredArchitectureIsRespected() {
        JavaClasses classes = new ClassFileImporter()
                .importPackages(MODULES_TO_SCAN);

        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .ignoreDependency(DescribedPredicate.alwaysTrue(), getPredicateForExcludingDependenciesToPackagesOutsideOfTheScannedModules())
                .layer("WEB").definedBy(WEB_PKG)
                .layer("DOMAIN").definedBy(DOMAIN_PKG)
                .layer("PERSISTENCE").definedBy(PERSISTENCE_PKG)

                .whereLayer("WEB").mayOnlyAccessLayers("DOMAIN")
                .whereLayer("DOMAIN").mayOnlyAccessLayers("PERSISTENCE")
                .whereLayer("PERSISTENCE").mayNotAccessAnyLayer()

                .check(classes);
    }

    private static @NotNull DescribedPredicate<JavaClass> getPredicateForExcludingDependenciesToPackagesOutsideOfTheScannedModules() {
        return new DescribedPredicate<>("target is outside of the modules to scan") {
            @Override
            public boolean test(JavaClass target) {
                return Arrays.stream(MODULES_TO_SCAN).noneMatch(target.getPackageName()::startsWith);
            }
        };
    }

}
