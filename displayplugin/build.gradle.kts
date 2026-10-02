plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.1.0"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
    id("com.gradleup.shadow") version "9.6.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")

    implementation("org.postgresql:postgresql:42.7.13")
    implementation(project(":commons"))

    testImplementation(project(":migrations"))
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

paperPluginYaml {
    main = "club.selbsthilfe.kuscheltiermafia.displayplugin.Displayplugin"
    //bootstrapper = "club.selbsthilfe.kuscheltiermafia.echoCraft_displayplugin.EchoCraft_displaypluginBootstrap"
    //loader = "club.selbsthilfe.kuscheltiermafia.echoCraft_displayplugin.EchoCraft_displaypluginLoader"
    apiVersion = "26.2"

    authors.addAll("realMorgon", "CrAzyA22", "TheFlameBe")
    website = "kuscheltiermafia.selbsthilfe.club"
    prefix = "EchoCraft Display"
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.10")
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    processResources {
        val props = mapOf("version" to version )
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

tasks.shadowJar {
    archiveClassifier.set("") // Ersetzt die normale JAR durch das Shadow-JAR

    // Verhindert Service-Datei-Konflikte (falls du Flyway/exp4j etc. in commons nutzt)
    mergeServiceFiles()
}
