plugins {
    id("java-library")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.23"
    id("xyz.jpenilla.run-paper") version "3.1.0"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
}

repositories {
    mavenCentral()
}

val paperApiVersion = extra["paperApiVersion"] as String

dependencies {
    paperweight.paperDevBundle(paperApiVersion)

    implementation("org.postgresql:postgresql:42.7.13")
    implementation(project(":commons"))
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
    runServer {
        minecraftVersion("26.2")
    }
}