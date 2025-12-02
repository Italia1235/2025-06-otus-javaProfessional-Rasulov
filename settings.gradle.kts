rootProject.name = "HWOTUS"
include("hw01-gradle")
include("hw02-collection")
include("hw03-annotations")
include("hw04-gc")
include("hw05-bytecode")
include("hw06-oop")
include("hw07-patterns")
include("hw08-io")
include("hw09-jdbc:demo")
include("hw09-jdbc:homework")
include("hw10-jpql:homework-template")
include("hw11-cache:demo")
include("hw11-cache:homework")
include("hw12-webserver")
include("hw13-di")
include("hw14-spring")
pluginManagement {
    val johnrengelmanShadow: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings


    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("org.springframework.boot") version springframeworkBoot
    }
}