package com.quickcleanpro.phonecleaner.use.skin.navigation

import androidx.navigation.NavHostController
import com.quickcleanpro.phonecleaner.use.core.ads.InterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.ads.NoOpInterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.navigation.Screen
import com.quickcleanpro.phonecleaner.use.service.notification.ToolNotificationIntentFactory

internal fun NavHostController.navigateToNotificationTarget(
    route: String,
    interstitialAdInterceptor: InterstitialAdInterceptor = NoOpInterstitialAdInterceptor,
) {
    val fromRoute = currentDestination?.route?.takeUnless { it == Screen.Splash.route || it == Screen.OnboardingScan.route }
    val navigateToTarget = {
        navigateToNotificationTargetNow(route)
    }
    if (route == Screen.Home.route || route in ToolNotificationIntentFactory.homeTabRoutes) {
        navigateToTarget()
        return
    }
    interstitialAdInterceptor.interceptRouteEntry(
        fromRoute = fromRoute,
        targetRoute = route,
        onContinue = navigateToTarget,
    )
}

private fun NavHostController.navigateToNotificationTargetNow(route: String) {
    while (popBackStack()) {
        // Clear the existing stack before rebuilding Home -> target.
    }
    val currentRoute = currentDestination?.route
    if (route in ToolNotificationIntentFactory.homeTabRoutes) {
        navigate(route) {
            currentRoute?.let { popUpTo(it) { inclusive = true } }
            launchSingleTop = true
        }
        return
    }
    navigate(Screen.Home.route) {
        currentRoute?.let { popUpTo(it) { inclusive = true } }
        launchSingleTop = true
    }
    if (route != Screen.Home.route) {
        navigate(route) { launchSingleTop = true }
    }
}
