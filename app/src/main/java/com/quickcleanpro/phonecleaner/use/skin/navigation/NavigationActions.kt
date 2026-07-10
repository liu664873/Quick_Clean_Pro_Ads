package com.quickcleanpro.phonecleaner.use.skin.navigation

import androidx.navigation.NavHostController
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRouteGroups

internal fun NavHostController.safePopBackStack(): Boolean =
    runCatching {
        val currentRoute = currentDestination?.route
        if (AppRouteGroups.isRootRoute(currentRoute)) {
            return@runCatching false
        }
        if (popBackStack()) {
            return@runCatching true
        }
        navigate(AppRoute.Home.value) {
            currentRoute?.let { route ->
                popUpTo(route) { inclusive = true }
            }
            launchSingleTop = true
        }
        true
    }.getOrDefault(false)

internal fun NavHostController.navigateToHomeClearingStack() {
    runCatching {
        val currentRoute = currentDestination?.route
        if (AppRouteGroups.isHomeRoute(currentRoute)) {
            return@runCatching
        }
        val existingHomeRoute =
            AppRouteGroups.homeRoutes.firstOrNull { route ->
                runCatching { getBackStackEntry(route) }.isSuccess
            }
        if (existingHomeRoute != null && popBackStack(existingHomeRoute, inclusive = false)) {
            return@runCatching
        }
        navigate(AppRoute.Home.value) {
            launchSingleTop = true
        }
    }
}

internal fun NavHostController.navigateReplaceStack(route: String) {
    runCatching {
        navigateToHomeClearingStack()
        if (route != AppRoute.Home.value) {
            navigate(route) { launchSingleTop = true }
        }
    }
}

internal fun NavHostController.navigateReplacingCurrent(route: String) {
    runCatching {
        val currentRoute = currentDestination?.route
        navigate(route) {
            currentRoute?.let { popUpTo(it) { inclusive = true } }
            launchSingleTop = true
        }
    }
}
