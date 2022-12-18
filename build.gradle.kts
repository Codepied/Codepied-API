import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.6"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("plugin.noarg") version "1.6.21"
    kotlin("kapt") version "1.6.21"
}

group = "com.codepied"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

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
    runtimeOnly("com.h2database:h2")
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
    /* aws */
    implementation("software.amazon.awssdk:ses:2.18.41")
    implementation("software.amazon.awssdk:sdk-core:2.18.41")
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
    /* configuration processor */
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
