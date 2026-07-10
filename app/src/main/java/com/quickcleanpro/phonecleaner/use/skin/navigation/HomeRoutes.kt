package com.quickcleanpro.phonecleaner.use.skin.navigation

import com.quickcleanpro.phonecleaner.use.core.navigation.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quickcleanpro.phonecleaner.use.service.notification.ToolNotificationIntentFactory
import com.quickcleanpro.phonecleaner.use.skin.home.HomeScreen

internal fun NavGraphBuilder.registerHomeRoutes(
    externalBlockingPromptActive: Boolean = false,
    homeNotificationPermissionPrompt: @Composable () -> Unit = {},
) {
    composable(Screen.Home.route) {
        HomeScreen(
            externalBlockingPromptActive = externalBlockingPromptActive,
        )
        homeNotificationPermissionPrompt()
    }
    composable(ToolNotificationIntentFactory.ROUTE_HOME_FILE_MANAGER) {
        HomeScreen(
            externalBlockingPromptActive = externalBlockingPromptActive,
            initialTabIndex = 1,
        )
        homeNotificationPermissionPrompt()
    }
    composable(ToolNotificationIntentFactory.ROUTE_HOME_TOOLBOX) {
        HomeScreen(
            externalBlockingPromptActive = externalBlockingPromptActive,
            initialTabIndex = 2,
        )
        homeNotificationPermissionPrompt()
    }
}
