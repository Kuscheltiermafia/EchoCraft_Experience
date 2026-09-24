plugins {
    id("java")
    id("application")
}

repositories {
    mavenCentral()
}

var flywayVersion = "13.7.0"
var testcontainersVersion = "2.0.5"

dependencies {
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:$flywayVersion")
    runtimeOnly("org.postgresql:postgresql:42.7.13")

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:testcontainers-postgresql:$testcontainersVersion")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter:$testcontainersVersion")
    testImplementation("org.slf4j:slf4j-simple:2.0.12")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("club.selbsthilfe.kuscheltiermafia.ec_db_migrations.MigrationService")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}