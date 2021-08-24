group = "dev.baybay.lobiani.app"

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("groovy")
    kotlin("plugin.spring") version "1.5.30"
}

val axonVersion = "4.5"
val spockVersion = "2.0-M2-groovy-2.5"
val openAPIVersion = "1.5.6"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springdoc:springdoc-openapi-ui:$openAPIVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$openAPIVersion")
    implementation("org.springdoc:springdoc-openapi-security:$openAPIVersion")
    implementation("org.axonframework:axon-spring-boot-starter:$axonVersion")
    implementation("org.dom4j:dom4j:2.1.3")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.axonframework:axon-test:$axonVersion")
    testImplementation("org.spockframework:spock-core:$spockVersion")
    testImplementation("org.spockframework:spock-spring:$spockVersion")
}
