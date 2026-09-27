pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "net.fabricmc.fabric-loom") {
                useModule("net.fabricmc:fabric-loom:${requested.version}")
            }
        }
    }
}

rootProject.name = "EchoCraft_Experience"
include("lobby")
include("servermod")
