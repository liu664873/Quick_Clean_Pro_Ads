package com.quickcleanpro.phonecleaner.use.core.permission

data class PermissionPromptRequest(
    val target: PermissionRequestTarget,
    val missingPermission: AppPermission?,
)
