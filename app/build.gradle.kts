group = "com.lobiani.app"

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("groovy")
    kotlin("plugin.spring") version "1.3.71"
}

val gebVersion = "3.4"
val seleniumVersion = "3.141.59"
val e2eSourceSet = "e2eTest"
val axonVersion = "4.2.1"
val spockVersion = "2.0-M2-groovy-2.5"

sourceSets.create(e2eSourceSet)

val e2eTestImplementation by configurations.getting {}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.axonframework:axon-spring-boot-starter:$axonVersion")
// Unfortunately Axon Server doesn't work with DevTools
//    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.axonframework:axon-test:$axonVersion")
    testImplementation("org.spockframework:spock-core:$spockVersion")
    testImplementation("org.spockframework:spock-spring:$spockVersion")

    e2eTestImplementation("org.spockframework:spock-core:$spockVersion")
    e2eTestImplementation("org.gebish:geb-spock:$gebVersion") {
        exclude(group = "org.codehaus.groovy")
    }
    e2eTestImplementation("org.seleniumhq.selenium:selenium-remote-driver:$seleniumVersion")
}

val defaultBaseUrl = "http://host.docker.internal:8080/"
val defaultWebDriverUrl = "http://localhost:4444/wd/hub"

tasks.register<Test>("e2eTestRemote") {
    description = "Runs e2e tests against the remote WebDriver"
    group = JavaBasePlugin.VERIFICATION_GROUP
    outputs.upToDateWhen { false }
    testClassesDirs = sourceSets[e2eSourceSet].output.classesDirs
    classpath = sourceSets[e2eSourceSet].runtimeClasspath
    shouldRunAfter("test")
    systemProperty("geb.env", "remote")
    systemProperty("geb.build.baseUrl", System.getProperty("geb.build.baseUrl", defaultBaseUrl))
    systemProperty("remote.webdriver.url", System.getProperty("remote.webdriver.url", defaultWebDriverUrl))
    systemProperty("remote.webdriver.acceptInsecure", System.getProperty("remote.webdriver.acceptInsecure"))
    systemProperty("remote.webdriver.enableVideo", System.getProperty("remote.webdriver.enableVideo", "false"))
}

