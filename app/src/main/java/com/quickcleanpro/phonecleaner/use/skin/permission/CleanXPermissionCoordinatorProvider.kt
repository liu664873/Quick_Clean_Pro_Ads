package com.quickcleanpro.phonecleaner.use.skin.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.quickcleanpro.phonecleaner.use.core.permission.CleanXPermissionRegistry
import com.quickcleanpro.phonecleaner.use.core.permission.PermissionPromptRequest

@Composable
fun CleanXPermissionCoordinatorProvider(
    permissionPrompt: @Composable (
        request: PermissionPromptRequest,
        onSubmit: () -> Unit,
        onDismiss: () -> Unit,
    ) -> Unit,
    onPermissionFlowActiveChange: (Boolean) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val actionManager =
        remember(context) {
            CleanXPermissionRegistry.protectedActionPermissionManager(context)
        }
    val itemManager =
        remember(context) {
            CleanXPermissionRegistry.permissionItemManager(context)
        }
    val state = remember(context, actionManager, itemManager) {
        CleanXPermissionCoordinatorState(context, actionManager, itemManager)
    }
    val latestContent by rememberUpdatedState(content)
    val latestOnPermissionFlowActiveChange by rememberUpdatedState(onPermissionFlowActiveChange)

    LaunchedEffect(state.session != null) {
        latestOnPermissionFlowActiveChange(state.session != null)
    }

    CompositionLocalProvider(LocalCleanXPermissionCoordinator provides state) {
        latestContent()
        CleanXPermissionPromptHost(
            state = state,
            permissionPrompt = permissionPrompt,
        )
    }
}
