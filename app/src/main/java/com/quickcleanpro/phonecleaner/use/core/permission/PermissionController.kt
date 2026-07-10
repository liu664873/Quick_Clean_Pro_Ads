package com.quickcleanpro.phonecleaner.use.core.permission

import com.quickcleanpro.phonecleaner.use.core.permission.AppPermission

enum class PermissionState {
    Granted,
    Denied,
    PermanentlyDenied,
    Unavailable,
}

sealed interface PermissionResult {
    data object Granted : PermissionResult
    data class Denied(
        val permanently: Boolean = false,
    ) : PermissionResult

    data object Unavailable : PermissionResult
}

interface PermissionController {
    fun state(permission: AppPermission): PermissionState

    fun request(
        permission: AppPermission,
        onResult: (PermissionResult) -> Unit,
    )
}

data class PermissionEvent(
    val permission: AppPermission,
    val source: String,
)
