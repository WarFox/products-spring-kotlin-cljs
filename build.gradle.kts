import dev.monosoul.jooq.GenerateJooqClassesTask
import dev.monosoul.jooq.RecommendedVersions
import org.gradle.kotlin.dsl.withType
import org.jooq.meta.jaxb.ForcedType

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.12.0"
    id("dev.monosoul.jooq-docker") version "7.0.0"
}

group = "io.github.warfox"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // database section
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    // postgresql
    runtimeOnly("org.postgresql:postgresql")
    // jooq
    implementation("org.jooq:jooq:${RecommendedVersions.JOOQ_VERSION}")
    jooqCodegen("org.postgresql:postgresql:42.7.2")
    jooqCodegen("org.flywaydb:flyway-database-postgresql:${RecommendedVersions.FLYWAY_VERSION}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:postgresql")

    testImplementation("io.kotest:kotest-runner-junit5:5.9.0")
    testImplementation("io.kotest:kotest-property:5.9.0")
    testImplementation("io.kotest.extensions:kotest-property-arbs:2.1.2")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

openApiGenerate {
    generatorName.set("kotlin-spring")

    inputSpec.set("$rootDir/src/main/resources/openapi.yaml")
    outputDir.set("${layout.buildDirectory.asFile.get()}/generated")
    apiPackage.set("io.github.warfox.app.api")
    modelPackage.set("io.github.warfox.app.model")
    configOptions.set(
        mapOf(
            "annotationsLibrary" to "none",
            "apiSuffix" to "ControllerApi",
            "dateLibrary" to "java8",
            "delegatePattern" to "false",
            "documentationProvider" to "none",
            "enumPropertyNaming" to "UPPERCASE",
            "exceptionHandler" to "false",
            "gradleBuildFile" to "false",
            "interfaceOnly" to "true",
            "reactive" to "false",
            "serializationLibrary" to "jackson",
            "skipDefaultInterface" to "true",
            "useBeanValidation" to "true",
            "useSpringBoot3" to "true",
            "useTags" to "true",
        )
    )
    globalProperties.set(
        mapOf(
            "skipFormModel" to "false"
        )
    )
}

tasks.withType<GenerateJooqClassesTask> {
    // basePackageName.set("com.cais.downstreamconsumers.jooq")
    usingJavaConfig {
        database
            .withExcludes("flyway_schema_history")
            .withForcedTypes(
                ForcedType()
                    .withName("INSTANT")
                    .withIncludeTypes("TIMESTAMP")
            )
    }
    outputSchemaToDefault.add("public")
}

sourceSets {
    main {
        java {
            srcDir("${layout.buildDirectory.asFile.get()}/generated/src/main/kotlin")
        }
    }
}

tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
}
