import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.sonarqube") version "3.0"
    id("jacoco")
}

group = "dev.baybay.lobiani"

allprojects {
    repositories {
        mavenCentral()
    }
    sonarqube {
        properties {
            property("sonar.projectKey", "lobiani-app-backend")
            property("sonar.projectName", "lobiani-app-backend")
            property("sonar.organization", "tsobe")
            property("sonar.host.url", "https://sonarcloud.io")
            property("sonar.coverage.jacoco.xmlReportPaths", "**/build/reports/jacoco/test/jacocoTestReport.xml")
        }
    }
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("jacoco")
    }

    version = "0.0.1-SNAPSHOT"

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    tasks.jacocoTestReport {
        reports {
            xml.isEnabled = true
        }
    }
}
