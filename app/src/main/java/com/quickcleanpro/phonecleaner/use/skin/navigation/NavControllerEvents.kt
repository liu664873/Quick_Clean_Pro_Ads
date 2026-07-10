package com.quickcleanpro.phonecleaner.use.skin.navigation

import androidx.navigation.NavHostController
import com.quickcleanpro.phonecleaner.use.core.navigation.AppNavigationEvent
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import com.quickcleanpro.phonecleaner.use.core.navigation.finalRoute
import com.quickcleanpro.phonecleaner.use.service.notification.ToolNotificationIntentFactory

internal fun NavHostController.handleNavigationEvent(
    event: AppNavigationEvent,
) {
    when (event) {
        AppNavigationEvent.Back -> safePopBackStack()
        AppNavigationEvent.Home -> {
            navigateToHomeClearingStack()
        }
        is AppNavigationEvent.Destination -> {
            navigate(event.finalRoute()) { launchSingleTop = true }
        }
        is AppNavigationEvent.ReplaceStack -> navigateReplaceStack(event.route)
        is AppNavigationEvent.ReplaceCurrent -> navigateReplacingCurrent(event.route)
    }
}

private val adHomeRoutes =
    setOf(AppRoute.Home.value) + ToolNotificationIntentFactory.homeTabRoutes
