import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.spring") version "2.2.10"
    kotlin("plugin.jpa") version "2.2.10"
}

group = "com.example.csnexo"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // API REST
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Persistencia (JPA + H2 en memoria)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    // Soporte de Kotlin para Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Pruebas
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Alineamos Java y Kotlin al mismo target (Java 17, baseline de Spring Boot 3.5)
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
