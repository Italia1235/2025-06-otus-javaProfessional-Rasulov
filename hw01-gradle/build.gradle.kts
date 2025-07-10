
plugins {
    id("java")
}
dependencies {
    implementation ("com.google.guava:guava")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.rasulov.HelloOtus"
    }
}