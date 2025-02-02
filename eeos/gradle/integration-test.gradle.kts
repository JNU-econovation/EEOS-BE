import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting

apply(plugin = "java")

val sourceSets = the<SourceSetContainer>()

sourceSets.create("integrationTest") {
    compileClasspath += sourceSets.getByName("main").output
    runtimeClasspath += sourceSets.getByName("main").output
    java {
        srcDir(file("src/integrationTest/java"))
    }
    resources {
        srcDir(file("src/integrationTest/resources"))
    }
}

val integrationTest = tasks.create<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    shouldRunAfter("test")

    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

configurations {
    "integrationTestImplementation" {
        extendsFrom(configurations["implementation"])
    }
    "integrationTestRuntimeOnly" {
        extendsFrom(configurations["runtimeOnly"])
    }
}

tasks.named("check") {
    dependsOn(integrationTest)
}
