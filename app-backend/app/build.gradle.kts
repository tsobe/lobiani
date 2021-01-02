group = "dev.baybay.lobiani.app"

plugins {
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("groovy")
    kotlin("plugin.spring") version "1.4.21"
}

val axonVersion = "4.4.2"
val spockVersion = "2.0-M2-groovy-2.5"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.axonframework:axon-spring-boot-starter:$axonVersion")
    implementation("org.hibernate.validator:hibernate-validator:6.0.13.Final")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.axonframework:axon-test:$axonVersion")
    testImplementation("org.spockframework:spock-core:$spockVersion")
    testImplementation("org.spockframework:spock-spring:$spockVersion")
    testImplementation("org.testcontainers:testcontainers:1.15.0-rc2")
    testImplementation("org.testcontainers:spock:1.14.3")
}
