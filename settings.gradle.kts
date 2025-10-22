import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    // <— indispensable pour pouvoir utiliser `intellijPlatform { … }` dans settings
    id("org.jetbrains.intellij.platform.settings") version "2.10.1"
}

rootProject.name = "sync-java-home-plugin"

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
        // <— fournit les dépôts JetBrains requis (releases, snapshots si besoin)
        intellijPlatform {
            defaultRepositories()
        }
    }
}