rootProject.name = "HWOTUS"
include("hw01-gradle")


pluginManagement {
    val johnrengelmanShadow: String by settings
    val dependencyManagement: String by settings


    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
    }
}