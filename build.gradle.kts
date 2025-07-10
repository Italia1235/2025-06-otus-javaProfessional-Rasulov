


plugins{
    id("io.spring.dependency-management")
}

allprojects {
    group = "ru.rasulov"

    repositories {
        mavenLocal()
        mavenCentral()
    }


val guava: String by project

apply(plugin = "io.spring.dependency-management")
dependencyManagement {
    dependencies {
        dependency("com.google.guava:guava:$guava")
    }
}
}
