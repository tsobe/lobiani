group = "com.lobiani.app"

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("groovy")
    kotlin("plugin.spring") version "1.3.71"
}

sourceSets.create("e2eTest")

val e2eTestImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

val gebVersion = "3.4"
val seleniumVersion = "3.141.59"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.spockframework:spock-core:2.0-M2-groovy-2.5")
    e2eTestImplementation("org.gebish:geb-spock:$gebVersion") {
        exclude(group = "org.codehaus.groovy")
    }
    e2eTestImplementation("org.seleniumhq.selenium:selenium-remote-driver:$seleniumVersion")
}

tasks.register<Test>("e2eTestRemote") {
    description = "Runs e2e tests against the remote WebDriver"
    group = JavaBasePlugin.VERIFICATION_GROUP
    outputs.upToDateWhen { false }
    testClassesDirs = sourceSets["e2eTest"].output.classesDirs
    classpath = sourceSets["e2eTest"].runtimeClasspath
    shouldRunAfter("test")
    systemProperty("geb.env", "remote")
}
