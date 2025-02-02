tasks.register("gitExecutableHooks") {
    doLast {
        ProcessBuilder("chmod", "-R", "+x", "../../.git/hooks/")
            .inheritIO()
            .start()
            .waitFor()
    }
}

tasks.register<Copy>("installGitHooks") {
    val scriptDir = rootProject.rootDir.resolve("scripts")
    from(scriptDir.resolve("pre-commit"))
    into(rootProject.rootDir.resolve(".git/hooks"))
}

tasks.named("gitExecutableHooks") {
    dependsOn("installGitHooks")
}

tasks.named("clean") {
    dependsOn("gitExecutableHooks")
}
