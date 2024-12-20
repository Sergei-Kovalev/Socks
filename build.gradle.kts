import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
	java
	id("org.springframework.boot") version "2.7.9"
	id("io.spring.dependency-management") version "1.1.6"
	id("io.freefair.lombok") version "8.11"
	id("jacoco")
}

group = "kovalev"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val mapstructVersion = "1.6.3"
val apachePoiVersion = "5.3.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.liquibase:liquibase-core")

	implementation("org.springdoc:springdoc-openapi-ui:1.8.0")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.27")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")

	implementation("org.apache.poi:poi:${apachePoiVersion}")
	implementation("org.apache.poi:poi-ooxml:${apachePoiVersion}")
	implementation("org.apache.commons:commons-collections4:4.5.0-M2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.assertj:assertj-core:3.26.3")

	implementation ("org.mapstruct:mapstruct:${mapstructVersion}")
	annotationProcessor ("org.mapstruct:mapstruct-processor:${mapstructVersion}")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
		exceptionFormat = TestExceptionFormat.FULL
	}
}
