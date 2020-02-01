import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

fun findProperty(s: String) = project.findProperty(s) as String?

plugins {
    kotlin("jvm") version "1.3.61"

    // Publish plugins - start
    `java-library`
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
    // Publish plugins - end
}

// Publish - start
bintray {
    user = "vondrous"
    key = findProperty("bintrayApiKey")

    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "kebish"
        name = "keb-core"
//        userOrg = "bintray_user"
        setLicenses("WTFPL")
        vcsUrl = "https://gitlab.com/horca23/keb.git"

//        version(delegateClosureOf<BintrayExtension.VersionConfig> {
//            name = "0.2"
//        })
        setPublications("keb-publish-artifact")
    })

}

publishing {
    publications {
        create<MavenPublication>("keb-publish-artifact") {
            from(components["java"])
        }
    }
}
// Publish - end

repositories {
    mavenCentral()
    jcenter()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib-jdk8"))

}

java {
    withSourcesJar()
    //withJavadocJar() - no correct javadoc is generated needs to be setup for Kotlin
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = FULL
        events = setOf(PASSED, STARTED, FAILED, SKIPPED)
    }
}

dependencies {
    api(group = "org.seleniumhq.selenium", name = "selenium-java", version = "3.141.59")

    testImplementation(project(":keb-junit5"))
    testImplementation(group = "io.github.bonigarcia", name = "webdrivermanager", version = "3.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation(group = "io.ktor", name = "ktor-server-netty", version = "1.2.6")
    testImplementation(group = "com.nhaarman", name = "mockito-kotlin", version = "1.6.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")
}

