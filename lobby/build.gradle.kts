plugins {
    id("java-library")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.23"
    //id("xyz.jpenilla.run-paper") version "3.1.0"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"

    id("com.gradleup.shadow") version "9.6.1"
}

repositories {
    mavenCentral()
}

val paperApiVersion = extra["paperApiVersion"] as String
var testcontainersVersion = extra["testcontainersVersion"] as String
var flywayVersion = "13.7.0"

dependencies {
    paperweight.paperDevBundle(paperApiVersion)

    implementation("org.postgresql:postgresql:42.7.13")
    implementation(project(":commons"))

    testImplementation(project(":migrations"))

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.testcontainers:testcontainers-postgresql:$testcontainersVersion")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter:$testcontainersVersion")
    testImplementation("org.flywaydb:flyway-core:$flywayVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

paperPluginYaml {
    main = "club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.EchoCraft_Lobby"
    bootstrapper = "club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.EchoCraft_LobbyBootstrap"
    loader = "club.selbsthilfe.kuscheltiermafia.echoCraft_Lobby.EchoCraft_LobbyLoader"
    apiVersion = "26.2"

    authors.addAll("realMorgon", "CrAzyA22", "TheFlameBe")
    website = "kuscheltiermafia.selbsthilfe.club"
    prefix = "EchoCraft Lobby"
}



java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    //runServer {
    //    minecraftVersion("26.2")
    //}
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

tasks.shadowJar {
    archiveClassifier.set("") // Ersetzt die normale JAR durch das Shadow-JAR

    // Verhindert Service-Datei-Konflikte (falls du Flyway/exp4j etc. in commons nutzt)
    mergeServiceFiles()
}

// Stellt sicher, dass bei './gradlew build' immer das Shadow-JAR erzeugt wird
tasks.build {
    dependsOn(tasks.shadowJar)
}