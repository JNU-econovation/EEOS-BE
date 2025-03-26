apply(plugin = "org.asciidoctor.jvm.convert")

val snippetsDir = file("build/generated-snippets")

tasks.named<Test>("test") {
    outputs.dir(snippetsDir)
}

tasks.named("asciidoctor") {
    dependsOn(tasks.named<Test>("test"))
    inputs.dir(snippetsDir)
    outputs.dir(file("$buildDir/docs/asciidoc"))
}

tasks.register("documentJar") {
    dependsOn(tasks.named("asciidoctor"))
    doLast {
        copy {
            from(file("$buildDir/docs/asciidoc"))
            into("src/main/resources/static/docs")
        }
    }
}

tasks.register<Copy>("copyDocument") {
    dependsOn(tasks.named("asciidoctor"))
    from(file("$buildDir/docs/asciidoc"))
    into(file("src/main/resources/static/docs"))
}

tasks.named("build") {
    dependsOn("copyDocument")
}
