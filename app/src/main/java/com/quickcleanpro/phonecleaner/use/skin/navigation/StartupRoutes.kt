package com.quickcleanpro.phonecleaner.use.skin.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quickcleanpro.phonecleaner.use.core.ads.AdScene
import com.quickcleanpro.phonecleaner.use.core.ads.StartupAdCoordinator
import com.quickcleanpro.phonecleaner.use.core.startup.AppLaunchCoordinator
import com.quickcleanpro.phonecleaner.use.core.startup.AppLaunchRequest
import com.quickcleanpro.phonecleaner.use.core.navigation.Screen
import com.quickcleanpro.phonecleaner.use.app.LocalExternalActivityLaunchHandler
import com.quickcleanpro.phonecleaner.use.app.LocalInterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.skin.onboarding.OnboardingScanScreen
import com.quickcleanpro.phonecleaner.use.skin.startup.SplashScreen
import com.quickcleanpro.phonecleaner.use.feature.startup.presentation.SplashViewModel
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.registerStartupRoutes(
    splashPaused: Boolean,
    launchCoordinator: AppLaunchCoordinator?,
    splashNotificationPermissionPrompt: @Composable () -> Unit = {},
) {
    composable(Screen.Splash.route) {
        val context = LocalContext.current
        val router = LocalRouter.current
        val viewModel: SplashViewModel = koinViewModel()
        val externalActivityLaunchHandler = LocalExternalActivityLaunchHandler.current
        val interstitialAdInterceptor = LocalInterstitialAdInterceptor.current
        val activity = context.findActivity()
        var finishStartupNavigation by remember { mutableStateOf<(() -> Unit)?>(null) }
        var openAdShowing by remember { mutableStateOf(false) }
        SplashScreen(
            paused = splashPaused || openAdShowing,
            externalActivityLaunchHandler = externalActivityLaunchHandler,
            onStartupReady = { onStartupComplete ->
                when (val request = launchCoordinator?.consumeRequest() ?: AppLaunchRequest.Normal) {
                    is AppLaunchRequest.NotificationTarget -> {
                        finishStartupNavigation = {
                            router.navController.navigateToNotificationTarget(request.route, interstitialAdInterceptor)
                        }
                        onStartupComplete()
                    }
                    AppLaunchRequest.Normal -> {
                        val targetScreen = startupTargetScreen(viewModel)
                        finishStartupNavigation = {
                            router.replaceCurrent(targetScreen)
                        }
                        StartupAdCoordinator.runColdStart(
                            activity = activity,
                            context = context.applicationContext,
                            onOpenAdShowing = { openAdShowing = true },
                            onOpenAdFinished = { openAdShowing = false },
                            onFinished = onStartupComplete,
                        )
                    }
                }
            },
            onSplashFinished = {
                val navigation = finishStartupNavigation
                finishStartupNavigation = null
                navigation?.invoke() ?: router.replaceCurrent(startupTargetScreen(viewModel))
            },
        )
        splashNotificationPermissionPrompt()
    }

    composable(Screen.OnboardingScan.route) {
        val router = LocalRouter.current
        val interstitialAdInterceptor = LocalInterstitialAdInterceptor.current
        OnboardingScanScreen(
            onSkipToHome = {
                interstitialAdInterceptor.interceptFeatureOperation(
                    scene = AdScene.OnboardingSkipped,
                    requestId = "onboarding_skip",
                ) {
                    router.replaceCurrent(Screen.Home)
                }
            },
            onScanFinishedAd = {
                interstitialAdInterceptor.interceptFeatureOperation(
                    scene = AdScene.OnboardingScanFinished,
                    requestId = "onboarding_scan_finish",
                ) {}
            },
            onGetStartedToHome = {
                interstitialAdInterceptor.interceptFeatureOperation(
                    scene = AdScene.OnboardingSkipped,
                    requestId = "onboarding_get_started",
                ) {
                    router.replaceCurrent(Screen.Home)
                }
            },
        )
    }
}

private fun startupTargetScreen(viewModel: SplashViewModel): Screen =
    if (viewModel.shouldShowOnboardingScan()) {
        Screen.OnboardingScan
    } else {
        Screen.Home
    }

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
