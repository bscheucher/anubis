import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.util.*


plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.flywaydb.flyway") version "11.14.1"
    id("net.linguica.maven-settings") version "0.5"
    id("maven-publish")
    id("io.freefair.lombok") version "8.4"
    jacoco
}

group = "com.ibosng"
version = project.version

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

loadEnvironmentSpecificProperties()

// Ensures only ONE JAR is built, disables extra plain JAR
tasks.named<Jar>("jar") {
    enabled = false
}

tasks.named<BootJar>("bootJar") {
    archiveClassifier.set("") // Ensures only one JAR is created
}

tasks.named("compileJava") {
    inputs.files(tasks.named("processResources"))
}

repositories {
    mavenCentral()
    mavenLocal()
}

extra["springCloudAzureVersion"] = "5.18.0"
extra["azureSDKBom"] = "1.2.29"

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security") // duplicated below in historic section; kept intentionally
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.2") // version mismatch with Spring Boot plugin 3.2.1; consider aligning
    implementation("org.springframework.boot:spring-boot-starter-cache:3.3.4") // version mismatch with Spring Boot plugin 3.2.1; consider aligning
    implementation("org.springframework.boot:spring-boot-starter-aop")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Spring ecosystem / docs
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0") // likely outdated; latest 2.x is newer in 2025
    implementation("io.swagger.core.v3:swagger-annotations:2.2.20")
    implementation("io.smallrye:jandex:3.1.2")

    // Azure (via BOMs declared in dependencyManagement)
    implementation("com.azure.spring:spring-cloud-azure-starter")
    implementation("com.azure.spring:spring-cloud-azure-starter-keyvault")
    implementation("com.azure.spring:spring-cloud-azure-starter-storage-blob")
    implementation("com.azure:azure-messaging-eventgrid:4.21.0")
    implementation("com.azure:azure-storage-blob")
    implementation("com.azure:azure-storage-file-share")
    implementation("com.azure:azure-core")

    // Database & migration
    implementation("org.flywaydb:flyway-core:11.14.1")
    implementation("org.flywaydb:flyway-database-postgresql:11.14.1")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    implementation("com.microsoft.sqlserver:mssql-jdbc:12.10.0.jre11") // verify JRE version; project uses Java 17

    testImplementation("org.testcontainers:testcontainers:2.0.3")
    testImplementation("org.testcontainers:junit-jupiter:1.21.4")
    testImplementation("org.testcontainers:testcontainers-postgresql:2.0.1")
    testImplementation("org.testcontainers:testcontainers-mariadb:2.0.1")
    testImplementation("com.redis:testcontainers-redis:2.2.4")

    // Security & auth
    implementation("io.jsonwebtoken:jjwt:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("org.redisson:redisson-spring-boot-starter:3.44.0")

    // Caching
    implementation("javax.cache:cache-api:1.1.1") // JCache API (JSR-107)
    implementation("org.ehcache:ehcache:3.10.8")

    // Microsoft Graph
    implementation("com.microsoft.graph:microsoft-graph:5.77.0") // check for updates
    implementation("com.microsoft.graph:microsoft-graph-auth:0.3.0") // deprecated/archived; consider MSAL or Azure Identity
    implementation("com.microsoft.graph:microsoft-graph-core:2.0.21")

    // Storage, messaging, file handling & documents
    implementation("org.apache.poi:poi:5.4.1")
    implementation("org.apache.poi:poi-ooxml:5.4.1")
    implementation("org.apache.tika:tika-core:3.0.0")
    implementation("com.opencsv:opencsv:5.12.0")
    implementation("commons-io:commons-io:2.17.0")
    implementation("net.sf.jasperreports:jasperreports:6.21.2")
    implementation("net.sf.jasperreports:jasperreports-fonts:6.21.2")

    // Data formats & serialization
    implementation("org.json:json:20231013") // likely outdated; consider updating
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.5.3") // very old; consider aligning with Jackson BOM (~2.17+)
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3") // consider aligning with Jackson core version used by Spring Boot
    implementation("javax.xml.bind:jaxb-api:2.3.1") // for Jakarta/Java 11+, prefer jakarta.xml.bind if possible
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")

    // Validation
    implementation("commons-validator:commons-validator:1.8.0")
    implementation("com.networknt:json-schema-validator:3.0.0")

    // Azure OpenAI
    implementation("com.azure:azure-ai-openai-assistants:1.0.0-beta.4")

    // Utilities
    implementation("org.apache.commons:commons-lang3:3.19.0")
    implementation("com.google.guava:guava:33.4.0-jre")
    implementation("de.jollyday:jollyday:0.5.10")
    implementation("fr.marcwrobel:jbanking:4.2.0")
    implementation("org.modelmapper:modelmapper:3.2.0")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.29")
    implementation("org.jboss.logging:jboss-logging:3.6.1.Final")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.cloud:spring-cloud-contract-wiremock:4.1.4")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Spring configuration processor (duplicated in source; kept intentionally)
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.14.1")
    }
}

dependencyManagement {
    imports {
        mavenBom("com.azure.spring:spring-cloud-azure-dependencies:${property("springCloudAzureVersion")}")
        mavenBom("com.azure:azure-sdk-bom:${property("azureSDKBom")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

jacoco {
    toolVersion = "0.8.9"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml"))
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/test/html"))
    }
    dependsOn(tasks.test) // makes sure the tests are run before generating the report
    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            exclude("**/config/**")
            exclude("**/dtos/**")
            exclude("**/enums/**")
            exclude("**/JwkService.class")
            exclude("**/JwkServiceImpl.class")
            exclude("**/OpenIdConfigurationService.class")
            exclude("**/OpenIdConfigurationServiceImpl.class")
            exclude("**/GatewayServiceApplication.class")
        }
    }))
}

fun loadEnvironmentSpecificProperties() {
    val env = findProperty("env")?.toString() ?: "local-dev"
    println("Using environment: $env")

    val envPropsFile = file("gradle/$env.properties")
    if (! envPropsFile.exists()) {
        throw GradleException("No properties file found for: $env")
    }

    println("Loading properties from ${envPropsFile.name}")
    val props = Properties().apply {
        envPropsFile.inputStream().use { load(it) }
    }
    props.forEach { key, value ->
        extra[key.toString()] = value
    }
}