import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.6"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("jacoco")
    /* plugin for spring rest doc */
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("plugin.noarg") version "1.6.21"
    kotlin("kapt") version "1.6.21"

}

group = "com.codepied"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_16

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    /* datasource */
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:42.5.0")
    implementation("com.querydsl:querydsl-jpa:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
    /* servlet */
    implementation("org.springframework.boot:spring-boot-starter-web")
    /* security */
    implementation("org.springframework.boot:spring-boot-starter-security:2.7.5")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    /* data validation */
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.5")
    /* webflux */
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.7.0")
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64")
    /* aws */
    implementation("software.amazon.awssdk:ses:2.18.41")
    implementation("software.amazon.awssdk:sdk-core:2.18.41")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.376")

    /* file (Tika) */
    implementation("org.apache.tika:tika-core:2.6.0")
    implementation("org.apache.tika:tika-parsers:2.6.0")
    /* kotlin */
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    /* lombok */
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    /* devtools */
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    /* testtools */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    /* Spring Rest Doc */
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    /* configuration processor */
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "16"
    }
}

/**
 * task setting for spring rest doc
 */
tasks {
    /**
     * snippetsDir 설정
     */
    val snippetsDir by extra { file("build/generated-snippets") }
    val jacocoExcludePatterns = listOf(
        "**/config/**",
        "**/domain/**",
        "**/*Config*",
        "com/codepied/api/api/security/application/JwtService.class",
        "**/*Factory*",
        "**/*Dto*",
        "**/dto/**",
        "com/codepied/api/api/externalApi/**",
        "**/exception/**",
        "**/event/**",
        "**/locale/**",
        "com/codepied/api/ApiApplicationKt.class",
    )

    clean {
        delete("src/main/resources/static/docs")
    }

    test {
        /**
         * test output dir 설정
         */
        useJUnitPlatform()
        systemProperty("org.springframework.restdocs.outputDir", snippetsDir)
        outputs.dir(snippetsDir)

        /**
         * jacoco setting
         */
        jacoco {
            version = "0.8.7"
            enabled = true
            this.reportsDirectory.set(file("$buildDir/jacoco/result/${name}.html"))
        }

        finalizedBy("jacocoTestReport")
    }

    /**
     * jacoco setting
     */
    jacocoTestReport {
        reports {
            html.required.set(true)
            xml.required.set(false)
            csv.required.set(false)
        }

        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude(jacocoExcludePatterns)
                }
            })
        )

        finalizedBy("jacocoTestCoverageVerification")
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                isEnabled = true
                element = "CLASS"

                limit {
                    counter = "BRANCH" // 조건분기
                    value = "COVEREDRATIO"
                    minimum = BigDecimal("0.8")
                }

                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = BigDecimal("0.8")
                }
            }
        }

        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude(jacocoExcludePatterns)
                }
            })
        )
    }

    build {
        dependsOn("copyDocument")
    }

    asciidoctor {
        dependsOn(test)
        attributes(mapOf("snippets" to snippetsDir))
        inputs.dir(snippetsDir)

        doFirst {
            delete("src/main/resources/static/docs")
        }
    }

    register<Copy>("copyDocument") {
        dependsOn(asciidoctor)

        destinationDir = file(".")
        from(asciidoctor.get().outputDir) {
            into("src/main/resources/static/docs")
        }
    }

    bootJar {
        dependsOn(asciidoctor)

        from(asciidoctor.get().outputDir) {
            into("BOOT-INF/classes/static/docs")
        }

        this.archiveFileName.set("codepied.jar")
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }


}
