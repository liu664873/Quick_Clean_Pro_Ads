package com.quickcleanpro.phonecleaner.use.skin.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.quickcleanpro.phonecleaner.use.core.ads.InterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.ads.NoOpInterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.navigation.AppNavigationEvent
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import com.quickcleanpro.phonecleaner.use.core.navigation.InterceptResult
import com.quickcleanpro.phonecleaner.use.core.navigation.NavigationInterceptor
import com.quickcleanpro.phonecleaner.use.core.navigation.Screen
import com.quickcleanpro.phonecleaner.use.core.navigation.finalRoute

class RouteManager(
    val navController: NavHostController,
    private val interstitialAdInterceptor: InterstitialAdInterceptor = NoOpInterstitialAdInterceptor,
    private val interceptors: MutableList<NavigationInterceptor> = mutableListOf(),
) {
    fun addInterceptor(interceptor: NavigationInterceptor) {
        interceptors.add(interceptor)
    }

    fun removeInterceptor(interceptor: NavigationInterceptor) {
        interceptors.remove(interceptor)
    }

    fun navigate(screen: Screen) = execute(AppNavigationEvent.Destination(screen.route))

    fun navigate(route: AppRoute) = execute(AppNavigationEvent.Destination(route.value))

    fun navigate(
        screen: Screen,
        args: Map<String, String>,
    ) = execute(AppNavigationEvent.Destination(screen.route, args))

    fun goBack() = execute(AppNavigationEvent.Back)

    fun goHome() = execute(AppNavigationEvent.Home)

    fun navigateAndClearStack(screen: Screen) = execute(AppNavigationEvent.ReplaceStack(screen.route))

    fun navigateAndClearStack(route: AppRoute) = execute(AppNavigationEvent.ReplaceStack(route.value))

    fun replaceCurrent(screen: Screen) = execute(AppNavigationEvent.ReplaceCurrent(screen.route))

    fun replaceCurrent(route: AppRoute) = execute(AppNavigationEvent.ReplaceCurrent(route.value))

    fun navigate(event: AppNavigationEvent): Boolean = execute(event)

    fun navigate(
        route: String,
        args: Map<String, String> = emptyMap(),
    ) = navigate(AppNavigationEvent.Destination(route, args))

    fun navigateAndClearStack(route: String) = execute(AppNavigationEvent.ReplaceStack(route))

    fun replaceCurrent(route: String) = execute(AppNavigationEvent.ReplaceCurrent(route))

    private fun execute(event: AppNavigationEvent): Boolean {
        val finalEvent = runInterceptors(event) ?: return false
        interstitialAdInterceptor.interceptRouteEntry(
            fromRoute = navController.currentDestination?.route,
            targetRoute = finalEvent.targetRouteForAds(),
        ) {
            navController.handleNavigationEvent(finalEvent)
        }
        return true
    }

    private fun runInterceptors(event: AppNavigationEvent): AppNavigationEvent? {
        var current = event
        for (interceptor in interceptors) {
            when (val result = interceptor.intercept(current)) {
                InterceptResult.Proceed -> continue
                InterceptResult.Block -> return null
                is InterceptResult.Redirect -> current = result.event
            }
        }
        return current
    }
}

private fun AppNavigationEvent.targetRouteForAds(): String? =
    when (this) {
        AppNavigationEvent.Back,
        AppNavigationEvent.Home,
            -> null
        is AppNavigationEvent.Destination -> finalRoute()
        is AppNavigationEvent.ReplaceCurrent -> route
        is AppNavigationEvent.ReplaceStack -> route
    }

val LocalRouter =
    staticCompositionLocalOf<RouteManager> {
        error("RouteManager not provided. Wrap root with CompositionLocalProvider(LocalRouter provides ...)")
    }
