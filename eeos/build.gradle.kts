import org.jetbrains.kotlin.builtins.StandardNames.FqNames.target

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
    alias(libs.plugins.asciidoctor)
    alias(libs.plugins.epages.restdocs)
    alias(libs.plugins.flyway)
}

group = "com.econovation"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

apply(from = "gradle/git-hooks.gradle.kts")
apply(from = "gradle/integration-test.gradle.kts")
apply(from = "gradle/asciidoctor.gradle.kts")

dependencies {
    // Spring Boot
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.spring.boot.starter.security)

    // Database
    implementation(libs.mysql.connector)
    implementation(libs.flyway.core)
    implementation(libs.flyway.mysql)

    // Security
    implementation(libs.jwt.api)
    implementation(libs.jwt.impl)
    implementation(libs.jwt.jackson)
    implementation(libs.jbcrypt)

    // Documentation
    implementation(libs.springdoc.openapi)
    testImplementation(libs.spring.restdocs.mockmvc)
    testImplementation(libs.epages.restdocs)

    // Test
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.cloud.starter.contract.stub.runner)
    testImplementation(libs.spring.security.test)

    // Integration Test
    "integrationTestImplementation"(libs.spring.boot.starter.test)
    "integrationTestImplementation"(libs.rest.assured)
    "integrationTestImplementation"(libs.spring.security.test)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // OpenFeign
    implementation(libs.spring.cloud.starter.openfeign)
}

dependencyManagement {
    imports {
        mavenBom("${libs.spring.cloud.dependencies.get().group}:${libs.spring.cloud.dependencies.get().name}:${libs.spring.cloud.dependencies.get().version}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

spotless {
    java {
        googleJavaFormat("1.17.0")
        indentWithTabs(2)
        endWithNewline()
        removeUnusedImports()
        trimTrailingWhitespace()
        target("src/*/java/**/*.java")
    }

    format("misc") {
        target("**/*.gradle.kts", "**/*.md", "**/.gitignore")
        targetExclude(".release/*.*")
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
