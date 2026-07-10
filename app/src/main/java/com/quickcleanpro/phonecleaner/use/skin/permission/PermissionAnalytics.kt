package com.quickcleanpro.phonecleaner.use.skin.permission

import com.quickcleanpro.phonecleaner.use.core.analytics.AnalyticsTracker
import com.quickcleanpro.phonecleaner.use.core.permission.CleanXPermissionItem
import com.quickcleanpro.phonecleaner.use.core.permission.CleanXPermissionRegistry
import com.quickcleanpro.phonecleaner.use.core.permission.PermissionRequestTarget

internal object PermissionAnalytics {
    fun trackDialogAccepted(target: PermissionRequestTarget) {
        if (target.isStorageFilesTarget()) {
            AnalyticsTracker.trackFileManagerPopup(ifOk = true)
        }
    }

    fun trackDismissed(target: PermissionRequestTarget, dialogVisible: Boolean) {
        if (target.isStorageFilesTarget()) {
            if (dialogVisible) {
                AnalyticsTracker.trackFileManagerPopup(ifOk = false)
            }
            AnalyticsTracker.trackFilePermissionResult(accepted = false)
        }
    }

    fun trackGranted(target: PermissionRequestTarget) {
        if (target.isStorageFilesTarget()) {
            AnalyticsTracker.trackFilePermissionResult(accepted = true)
        }
    }
}

private fun PermissionRequestTarget.isStorageFilesTarget(): Boolean =
    when (this) {
        is PermissionRequestTarget.Action ->
            CleanXPermissionRegistry.itemForAction(action) == CleanXPermissionItem.StorageFiles
        is PermissionRequestTarget.Item -> item == CleanXPermissionItem.StorageFiles
    }
