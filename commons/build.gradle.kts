plugins {
    id("java-library")
}

group = "club.selbsthilfe.kuscheltiermafia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

val paperApiVersion = extra["paperApiVersion"] as String
val postgresVersion = extra["postgresVersion"] as String

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperApiVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("com.zaxxer:HikariCP:7.1.0")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

java{
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}