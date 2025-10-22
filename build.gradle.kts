plugins {
    kotlin("jvm") version "2.2.20"
    id("org.jetbrains.intellij.platform")
}

group = "com.ludosch"
version = "0.2.0"

kotlin {
    jvmToolchain(17)
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2025.2.3")
        bundledPlugin("com.intellij.java")

        // Install plugins in sandbox for testing
        plugin("systems.fehn.intellijdirenv", "0.2.10")
        plugin("com.chriscarini.jetbrains.environment-variable-settings-summary", "5.1.3")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild.set("252")
            untilBuild.set("252.*")
        }
    }

    publishing {
        token.set(providers.gradleProperty("publishToken").orElse(System.getenv("PUBLISH_TOKEN")))
        // Channel can be: default, eap, beta, or alpha
        channels.set(listOf("default"))
    }
}