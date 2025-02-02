import org.gradle.kotlin.dsl.*

apply(plugin = "java")

project.extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    java {
        target("src/*/java/**/*.java")
        indent("    ")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
