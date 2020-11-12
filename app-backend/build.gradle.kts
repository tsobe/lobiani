import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.71"
    id("org.sonarqube") version "3.0"
}

group = "dev.baybay.lobiani"

allprojects {
    repositories {
        mavenCentral()
    }
    sonarqube {
        properties {
            property("sonar.projectKey", "tsobe_lobiani")
            property("sonar.organization", "tsobe")
            property("sonar.host.url", "https://sonarcloud.io")
        }
    }
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    version = "0.0.1-SNAPSHOT"

    val developmentOnly by configurations.creating
    configurations {
        runtimeClasspath {
            extendsFrom(developmentOnly)
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}
