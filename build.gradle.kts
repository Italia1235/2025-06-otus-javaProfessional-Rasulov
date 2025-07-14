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
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
    }

}
