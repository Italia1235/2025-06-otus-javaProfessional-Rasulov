import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id ("com.github.johnrengelman.shadow")
}
dependencies {
    implementation ("com.google.guava:guava")

}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.rasulov.HelloOtus"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}