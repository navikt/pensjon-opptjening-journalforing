import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


val springVersion = "2.7.0"
val springKafkaVersion = "2.8.5"
val azureAdClientVersion = "0.0.7"
val brevmodelVersion = "1.3.3-SNAPSHOT"
val prometheusVersion = "1.9.0"
val logbackEncoderVersion = "7.1.1"

val wiremockVersion = "2.35.1"

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "no.nav.pensjon.opptjening"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://maven.pkg.github.com/navikt/pensjon-opptjening-azure-ad-client") {
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    //Spring
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-aop:$springVersion")
    implementation("org.springframework.kafka:spring-kafka:$springKafkaVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")


    // Log and metric
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheusVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")

    // OIDC
    implementation("no.nav.pensjonopptjening:pensjon-opptjening-azure-ad-client:$azureAdClientVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation("org.springframework.kafka:spring-kafka-test:$springKafkaVersion")
    testImplementation("com.github.tomakehurst:wiremock-jre8:$wiremockVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")

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
