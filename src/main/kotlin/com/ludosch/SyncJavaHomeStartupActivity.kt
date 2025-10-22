package com.ludosch

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.roots.LanguageLevelProjectExtension

class SyncJavaHomeStartupActivity : ProjectActivity {
    private val log = Logger.getInstance(SyncJavaHomeStartupActivity::class.java)

    override suspend fun execute(project: Project) {
        log.info("ProjectActivity running for project: ${project.name}")

        // Show notification with option to sync SDK
        ApplicationManager.getApplication().invokeLater {
            showSyncNotification(project)
        }
    }

    private fun showSyncNotification(project: Project) {
        val notification = com.intellij.notification.Notification(
            "com.ludosch",
            "Would you like to synchronize the Project SDK with JAVA_HOME?",
            com.intellij.notification.NotificationType.INFORMATION
        )
        notification.setTitle("Sync Project SDK")

        notification.addAction(object : com.intellij.notification.NotificationAction("Sync Now") {
            override fun actionPerformed(e: com.intellij.openapi.actionSystem.AnActionEvent, notification: com.intellij.notification.Notification) {
                SdkSynchronizer.syncProjectSdkFromJavaHome(project)
                notification.expire()
            }
        })

        com.intellij.notification.Notifications.Bus.notify(notification, project)
    }
}

object SdkSynchronizer {
    private val log = Logger.getInstance(SdkSynchronizer::class.java)

    fun syncProjectSdkFromJavaHome(project: Project) {
        val javaHome = System.getenv("JAVA_HOME")?.trim().orEmpty()
        if (javaHome.isEmpty()) {
            log.warn("JAVA_HOME is not set; skipping Project SDK sync.")
            return
        }

        log.info("Using JAVA_HOME: $javaHome")

        val javaSdk = JavaSdk.getInstance()
        if (!javaSdk.isValidSdkHome(javaHome)) {
            log.warn("JAVA_HOME ($javaHome) is not a valid JDK home; skipping.")
            return
        }

        ApplicationManager.getApplication().invokeLater {
            ApplicationManager.getApplication().runWriteAction {
                val projectRootManager = ProjectRootManager.getInstance(project)
                val currentSdk = projectRootManager.projectSdk
                val currentSdkPath = currentSdk?.homePath

                // Check if SDK needs to be updated
                if (currentSdkPath != null && currentSdkPath.equals(javaHome, ignoreCase = true)) {
                    log.info("Project SDK is already set to JAVA_HOME: ${currentSdk.name} -> $currentSdkPath")
                    // SDK is correct, but we still update Language Level in case it wasn't set
                    updateLanguageLevel(project, currentSdk)
                    return@runWriteAction
                }

                // Log the change
                if (currentSdkPath != null) {
                    log.info("JAVA_HOME changed from $currentSdkPath to $javaHome, updating SDK")
                } else {
                    log.info("No SDK set, initializing from JAVA_HOME: $javaHome")
                }

                val jdkTable = ProjectJdkTable.getInstance()

                val existing: Sdk? = jdkTable.allJdks.firstOrNull {
                    it.sdkType == javaSdk && it.homePath.equals(javaHome, ignoreCase = true)
                }

                val sdk: Sdk = existing ?: run {
                    val created = SdkConfigurationUtil.createAndAddSDK(javaHome, javaSdk)
                    if (created == null) {
                        log.warn("Failed to create SDK for $javaHome")
                        return@runWriteAction
                    }
                    log.info("Created new SDK: ${created.name}")
                    created
                }

                projectRootManager.projectSdk = sdk
                log.info("Project SDK updated: ${sdk.name} -> ${sdk.homePath}")

                // Update Language Level to match SDK
                updateLanguageLevel(project, sdk)
            }
        }
    }

    private fun updateLanguageLevel(project: Project, sdk: Sdk) {
        try {
            val javaSdk = JavaSdk.getInstance()
            val sdkVersion = javaSdk.getVersion(sdk)

            if (sdkVersion == null) {
                log.warn("Could not determine SDK version for ${sdk.name}")
                return
            }

            val languageLevel = sdkVersion.maxLanguageLevel
            val languageLevelExt = LanguageLevelProjectExtension.getInstance(project)
            val currentLevel = languageLevelExt.languageLevel

            if (currentLevel != languageLevel) {
                languageLevelExt.languageLevel = languageLevel
                log.info("Language Level updated from ${currentLevel?.name ?: "null"} to ${languageLevel.name} (${languageLevel.presentableText})")
            } else {
                log.info("Language Level already set to ${languageLevel.name} (${languageLevel.presentableText})")
            }
        } catch (e: Exception) {
            log.warn("Failed to update Language Level: ${e.message}")
        }
    }
}
