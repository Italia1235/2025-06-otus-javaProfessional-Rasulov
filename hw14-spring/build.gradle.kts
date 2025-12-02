dependencies {
    implementation("org.postgresql:postgresql")
    implementation ("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("ch.qos.logback:logback-classic")
    implementation("com.google.code.gson:gson")
    implementation ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    implementation("org.freemarker:freemarker")

    implementation("ch.qos.logback:logback-classic")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql:10.4.1")
    implementation("org.postgresql:postgresql")
}

plugins {
    id("org.springframework.boot")
}