package com.ludosch

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger

class SyncSdkFromJavaHomeAction : AnAction() {
    private val log = Logger.getInstance(SyncSdkFromJavaHomeAction::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            log.warn("No project found")
            return
        }

        try {
            log.info("Manual SDK sync triggered")
            SdkSynchronizer.syncProjectSdkFromJavaHome(project)

            // Show success notification
            val notification = Notification(
                "com.ludosch",
                "Project SDK and Language Level have been synchronized with JAVA_HOME",
                NotificationType.INFORMATION
            )
            notification.setTitle("SDK Synchronized")
            Notifications.Bus.notify(notification, project)
        } catch (e: Exception) {
            log.error("Failed to sync SDK", e)

            // Show error notification
            val notification = Notification(
                "com.ludosch",
                "Failed to synchronize SDK: ${e.message}",
                NotificationType.ERROR
            )
            notification.setTitle("Sync Failed")
            Notifications.Bus.notify(notification, project)
        }
    }

    override fun update(e: AnActionEvent) {
        // Action is only available when a project is open
        e.presentation.isEnabledAndVisible = e.project != null
    }
}
