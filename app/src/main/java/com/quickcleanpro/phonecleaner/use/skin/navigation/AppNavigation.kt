package com.quickcleanpro.phonecleaner.use.skin.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.quickcleanpro.phonecleaner.use.core.ads.InterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.ads.NoOpInterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.startup.AppLaunchCoordinator
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import com.quickcleanpro.phonecleaner.use.core.navigation.NavigationInterceptor

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = AppRoute.Splash.value,
    launchCoordinator: AppLaunchCoordinator? = null,
    splashPaused: Boolean = false,
    externalBlockingPromptActive: Boolean = false,
    splashNotificationPermissionPrompt: @Composable () -> Unit = {},
    homeNotificationPermissionPrompt: @Composable () -> Unit = {},
    interceptors: List<NavigationInterceptor> = emptyList(),
    interstitialAdInterceptor: InterstitialAdInterceptor = NoOpInterstitialAdInterceptor,
) {
    val routeManager =
        remember(navController, interceptors, interstitialAdInterceptor) {
            RouteManager(
                navController = navController,
                interstitialAdInterceptor = interstitialAdInterceptor,
            ).apply {
                interceptors.forEach { addInterceptor(it) }
            }
        }

    CompositionLocalProvider(
        LocalRouter provides routeManager,
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                AppRouteTransitions.enterTransition(
                    scope = this,
                    initialRoute = initialState.destination.route,
                    targetRoute = targetState.destination.route,
                )
            },
            exitTransition = {
                AppRouteTransitions.exitTransition(
                    scope = this,
                    initialRoute = initialState.destination.route,
                    targetRoute = targetState.destination.route,
                )
            },
            popEnterTransition = {
                AppRouteTransitions.popEnterTransition(
                    scope = this,
                    initialRoute = initialState.destination.route,
                    targetRoute = targetState.destination.route,
                )
            },
            popExitTransition = {
                AppRouteTransitions.popExitTransition(
                    scope = this,
                    initialRoute = initialState.destination.route,
                    targetRoute = targetState.destination.route,
                )
            },
        ) {
            registerStartupRoutes(
                splashPaused = splashPaused,
                launchCoordinator = launchCoordinator,
                splashNotificationPermissionPrompt = splashNotificationPermissionPrompt,
            )
            registerHomeRoutes(
                externalBlockingPromptActive = externalBlockingPromptActive,
                homeNotificationPermissionPrompt = homeNotificationPermissionPrompt,
            )
            registerCleanRoutes()
            registerAntiVirusRoutes()
            registerAppLockRoutes()
            registerToolboxRoutes()
            registerFileManagerRoutes()
            registerSettingsRoutes()
        }
    }
}
