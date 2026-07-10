package com.quickcleanpro.phonecleaner.use.skin.permission

import android.content.Intent
import com.quickcleanpro.phonecleaner.use.core.permission.AppPermission
import com.quickcleanpro.phonecleaner.use.core.permission.PermissionRequestResult
import com.quickcleanpro.phonecleaner.use.core.permission.PermissionRequestTarget

internal data class PermissionSession(
    val target: PermissionRequestTarget,
    val missingPermission: AppPermission?,
    val onGranted: () -> Unit,
    val onRejected: () -> Unit,
    val onResult: (PermissionRequestResult) -> Unit = {},
    val showDialog: Boolean = true,
    val settingsLaunchPending: Boolean = false,
    val settingsLaunchObservedPause: Boolean = false,
)

internal sealed interface PermissionLaunch {
    val target: PermissionRequestTarget

    data class Runtime(
        override val target: PermissionRequestTarget,
        val permissions: Array<String>,
    ) : PermissionLaunch

    data class Settings(
        override val target: PermissionRequestTarget,
        val intents: List<Intent>,
    ) : PermissionLaunch
}

internal fun shouldContinuePermissionFlow(
    previousMissingPermission: AppPermission?,
    nextMissingPermission: AppPermission?,
): Boolean =
    previousMissingPermission?.key != null &&
        nextMissingPermission?.key != null &&
        previousMissingPermission.key != nextMissingPermission.key
