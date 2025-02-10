import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
    jacoco
}

var mainClassName = "rogue.game.application.Rogue"
val cursesPath = "lib/jcurses/jcurses.jar"

group = "rogue.game"
version = "1.8.10"

jacoco {
    toolVersion = "0.8.12"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("org.mapstruct:mapstruct:1.6.2")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")

    implementation(files(cursesPath))
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = mainClassName
        attributes["Class-Path"] = configurations.compileClasspath.get().joinToString(" ")
    }
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
    }

    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}
