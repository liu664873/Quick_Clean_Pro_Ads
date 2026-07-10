package com.quickcleanpro.phonecleaner.use.core.navigation

import com.quickcleanpro.phonecleaner.use.service.notification.ToolNotificationIntentFactory

object AppRouteGroups {
    val startupRoutes: Set<String> =
        setOf(
            AppRoute.Splash.value,
            AppRoute.OnboardingScan.value,
        )

    val homeRoutes: Set<String> =
        setOf(AppRoute.Home.value) + ToolNotificationIntentFactory.homeTabRoutes

    val rootRoutes: Set<String> = startupRoutes + homeRoutes

    fun isStartupRoute(route: String?): Boolean = route != null && route in startupRoutes

    fun isHomeRoute(route: String?): Boolean = route != null && route in homeRoutes

    fun isRootRoute(route: String?): Boolean = route != null && route in rootRoutes
}
