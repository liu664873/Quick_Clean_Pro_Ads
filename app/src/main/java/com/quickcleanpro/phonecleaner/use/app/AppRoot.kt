package com.quickcleanpro.phonecleaner.use.app

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pdffox.adv.AdvertiseSdk
import com.quickcleanpro.phonecleaner.use.core.ads.AdRuntimeState
import com.quickcleanpro.phonecleaner.use.core.ads.AppOpenAdSuppression
import com.quickcleanpro.phonecleaner.use.core.ads.AdvertisePreloader
import com.quickcleanpro.phonecleaner.use.core.ads.DefaultInterstitialAdInterceptor
import com.quickcleanpro.phonecleaner.use.core.ads.InterstitialAdManager
import com.quickcleanpro.phonecleaner.use.core.analytics.AnalyticsTracker
import com.quickcleanpro.phonecleaner.use.core.startup.AppLaunchCoordinator
import com.quickcleanpro.phonecleaner.use.core.startup.AppLaunchRequest
import com.quickcleanpro.phonecleaner.use.core.startup.NotificationLaunchSource
import com.quickcleanpro.phonecleaner.use.core.common.platform.ExternalActivityLaunchHandler
import com.quickcleanpro.phonecleaner.use.core.common.operation.DefaultFeatureOperationTracker
import com.quickcleanpro.phonecleaner.use.core.permission.NotificationRuntimePermissionController
import com.quickcleanpro.phonecleaner.use.core.repository.SettingsRepository
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRouteGroups
import com.quickcleanpro.phonecleaner.use.core.navigation.Screen
import com.quickcleanpro.phonecleaner.use.skin.navigation.AppNavGraph
import com.quickcleanpro.phonecleaner.use.skin.navigation.navigateToNotificationTarget
import com.quickcleanpro.phonecleaner.use.skin.permission.QuickCleanProPermissionUi
import com.quickcleanpro.phonecleaner.use.skin.permission.CleanXPermissionCoordinatorProvider
import com.quickcleanpro.phonecleaner.use.skin.permission.NotificationPermissionPromptState
import com.quickcleanpro.phonecleaner.use.skin.permission.rememberNotificationPermissionPromptState
import com.quickcleanpro.phonecleaner.use.skin.common.components.NotificationPermissionPrompt
import com.quickcleanpro.phonecleaner.use.feature.notification.presentation.NotificationPermissionSessionViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlinx.coroutines.delay

private const val EXTERNAL_ACTIVITY_RETURN_AD_COOLDOWN_MS = 1_200L

@Composable
fun AppRoot(
    launchCoordinator: AppLaunchCoordinator,
    onNotificationPermissionGranted: () -> Unit = {},
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val settingsRepository: SettingsRepository = koinInject()
    val notificationPermissionSessionViewModel: NotificationPermissionSessionViewModel = koinViewModel()
    val navController = rememberNavController()
    val activity = context.findActivity()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val pendingRequest by launchCoordinator.pendingRequest.collectAsStateWithLifecycle()
    var cleanXPermissionFlowActive by remember { mutableStateOf(false) }
    var featureOperationBusy by remember { mutableStateOf(false) }
    var externalActivityLaunchPending by remember { mutableStateOf(false) }
    var externalActivityReturning by remember { mutableStateOf(false) }
    var externalActivityReturnGeneration by remember { mutableStateOf(0) }
    var externalActivityAppOpenRelease by remember { mutableStateOf<(() -> Unit)?>(null) }

    fun markExternalActivityReturn() {
        if (!externalActivityLaunchPending) return
        externalActivityLaunchPending = false
        externalActivityReturning = true
        externalActivityReturnGeneration += 1
    }

    fun suppressAppOpenForExternalActivity() {
        if (externalActivityAppOpenRelease == null) {
            externalActivityAppOpenRelease = AppOpenAdSuppression.acquire()
        }
        AdvertiseSdk.suppressNextAppOpenAd = true
    }

    fun restoreAppOpenAfterExternalActivity() {
        externalActivityAppOpenRelease?.invoke()
        externalActivityAppOpenRelease = null
        AdvertiseSdk.suppressNextAppOpenAd = false
    }

    val externalActivityLaunchHandler =
        remember {
            ExternalActivityLaunchHandler(
                markLaunch = {
                    externalActivityLaunchPending = true
                    externalActivityReturning = false
                    suppressAppOpenForExternalActivity()
                },
                cancelLaunch = {
                    externalActivityLaunchPending = false
                    externalActivityReturning = false
                    restoreAppOpenAfterExternalActivity()
                },
                markReturn = { markExternalActivityReturn() },
            )
        }
    val notificationPermissionController =
        remember(context, externalActivityLaunchHandler) {
            NotificationRuntimePermissionController(
                context = context,
                externalActivityLaunchHandler = externalActivityLaunchHandler,
            )
        }
    val notificationPermissionPromptState = rememberNotificationPermissionPromptState()

    val shouldPauseSplashForInitialNotificationRequest =
        notificationPermissionPromptState.shouldPauseSplashForInitialNotificationRequest(
            currentRoute = currentRoute,
            splashRoute = Screen.Splash.route,
            settingsRepository = settingsRepository,
            permissionController = notificationPermissionController,
        )
    val latestAdRuntimeState = rememberUpdatedState(
        AdRuntimeState(
            permissionFlowActive =
                notificationPermissionPromptState.splashPermissionActive ||
                    notificationPermissionPromptState.notificationPermissionUiActive ||
                    shouldPauseSplashForInitialNotificationRequest ||
                    cleanXPermissionFlowActive,
            externalActivityReturning = externalActivityReturning,
            scanningOrCleaning = featureOperationBusy,
        ),
    )
    var interstitialAdInFlight by remember { mutableStateOf(false) }
    var previousRoute by remember { mutableStateOf<String?>(null) }
    val interstitialAdManager =
        remember(activity) {
            InterstitialAdManager(
                activityProvider = { activity },
                stateProvider = { latestAdRuntimeState.value },
                onInFlightChanged = { inFlight ->
                    interstitialAdInFlight = inFlight
                },
            )
        }
    val interstitialAdInterceptor =
        remember(interstitialAdManager) {
            DefaultInterstitialAdInterceptor(interstitialAdManager::show)
        }
    val operationTracker =
        remember(interstitialAdInterceptor) {
            DefaultFeatureOperationTracker(
                interstitialAdInterceptor = interstitialAdInterceptor,
                onOperationBusyChanged = { busy -> featureOperationBusy = busy },
            )
        }

    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    markExternalActivityReturn()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            restoreAppOpenAfterExternalActivity()
        }
    }

    LaunchedEffect(externalActivityReturnGeneration) {
        if (externalActivityReturnGeneration == 0) return@LaunchedEffect
        val generation = externalActivityReturnGeneration
        delay(EXTERNAL_ACTIVITY_RETURN_AD_COOLDOWN_MS)
        if (externalActivityReturnGeneration == generation && !externalActivityLaunchPending) {
            externalActivityReturning = false
            restoreAppOpenAfterExternalActivity()
        }
    }

    LaunchedEffect(currentRoute) {
        val appContext = context.applicationContext
        val route = currentRoute ?: return@LaunchedEffect
        if (AppRouteGroups.isHomeRoute(route)) {
            AnalyticsTracker.trackHomeEntered(AnalyticsTracker.homepageReferrer(previousRoute))
            AdvertisePreloader.preloadMainPageAds(appContext)
        }
        AnalyticsTracker.featureForPrimaryRoute(route)?.let(AnalyticsTracker::trackCoreFeatureEntered)
        previousRoute = route
    }

    CompositionLocalProvider(
        LocalExternalActivityLaunchHandler provides externalActivityLaunchHandler,
        LocalFeatureOperationTracker provides operationTracker,
        LocalInterstitialAdInterceptor provides interstitialAdInterceptor,
    ) {
        CleanXPermissionCoordinatorProvider(
            permissionPrompt = QuickCleanProPermissionUi::PermissionPrompt,
            onPermissionFlowActiveChange = { active ->
                cleanXPermissionFlowActive = active
            },
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppNavGraph(
                    navController = navController,
                    launchCoordinator = launchCoordinator,
                    startDestination = Screen.Splash.route,
                    splashPaused =
                        notificationPermissionPromptState.splashPermissionActive ||
                            shouldPauseSplashForInitialNotificationRequest,
                    externalBlockingPromptActive =
                        notificationPermissionPromptState.notificationPermissionUiActive,
                    splashNotificationPermissionPrompt = {
                        StorageNotificationPermissionPrompt(
                            isSplashVisible = true,
                            isHomeVisible = false,
                            settingsRepository = settingsRepository,
                            permissionController = notificationPermissionController,
                            promptState = notificationPermissionPromptState,
                            allowCustomPromptInCurrentSession = true,
                            sessionViewModel = notificationPermissionSessionViewModel,
                            onPermissionGranted = onNotificationPermissionGranted,
                        )
                    },
                    homeNotificationPermissionPrompt = {
                        StorageNotificationPermissionPrompt(
                            isSplashVisible = false,
                            isHomeVisible = true,
                            settingsRepository = settingsRepository,
                            permissionController = notificationPermissionController,
                            promptState = notificationPermissionPromptState,
                            allowCustomPromptInCurrentSession =
                                !notificationPermissionSessionViewModel.isHomeCustomPromptDeferredUntilNextLaunch,
                            sessionViewModel = notificationPermissionSessionViewModel,
                            onPermissionGranted = onNotificationPermissionGranted,
                        )
                    },
                    interstitialAdInterceptor = interstitialAdInterceptor,
                )
                InterstitialInteractionBlocker(enabled = interstitialAdInFlight)

            }

            LaunchedEffect(navController, pendingRequest, currentRoute) {
                val request = pendingRequest
                if (currentRoute == null) return@LaunchedEffect
                when (request) {
                    is AppLaunchRequest.NotificationTarget -> {
                        if (shouldLetSplashConsumeNotificationTarget(currentRoute, request)) {
                            return@LaunchedEffect
                        }
                        if (shouldConsumeNotificationTargetImmediately(currentRoute, request)) {
                            if (launchCoordinator.consumeRequestIfCurrent(request)) {
                                navController.navigateToNotificationTarget(request.route, interstitialAdInterceptor)
                            }
                            return@LaunchedEffect
                        }
                        navController.navigate(Screen.Splash.route) {
                            launchSingleTop = true
                        }
                    }
                    AppLaunchRequest.Normal -> Unit
                }
            }
        }
    }
}

@Composable
private fun InterstitialInteractionBlocker(enabled: Boolean) {
    if (!enabled) return

    BackHandler {}
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {},
    )
}

@Composable
private fun StorageNotificationPermissionPrompt(
    isSplashVisible: Boolean,
    isHomeVisible: Boolean,
    settingsRepository: SettingsRepository,
    permissionController: NotificationRuntimePermissionController,
    promptState: NotificationPermissionPromptState,
    allowCustomPromptInCurrentSession: Boolean,
    sessionViewModel: NotificationPermissionSessionViewModel,
    onPermissionGranted: () -> Unit,
) {
    NotificationPermissionPrompt(
        isSplashVisible = isSplashVisible,
        isHomeVisible = isHomeVisible,
        hasNotificationPermission = permissionController::hasPostNotificationsPermission,
        hasRequestedNotificationPermissionBefore =
            settingsRepository::hasRequestedNotificationRuntimePermissionBefore,
        saveNotificationPermissionRequestedBefore =
            settingsRepository::saveNotificationRuntimePermissionRequestedBefore,
        shouldShowNotificationPermissionRationale =
            permissionController::shouldShowPostNotificationsRationale,
        readLastCustomPromptAt = settingsRepository::readLastNotificationPermissionCustomPromptAt,
        saveLastCustomPromptAt = settingsRepository::saveLastNotificationPermissionCustomPromptAt,
        openAppSettings = permissionController::openAppSettings,
        allowCustomPromptInCurrentSession = allowCustomPromptInCurrentSession,
        onHomeSystemPermissionRejectedThisSession =
            sessionViewModel::markHomeCustomPromptDeferredUntilNextLaunch,
        onPermissionGranted = onPermissionGranted,
        onSplashPermissionActiveChange = { active ->
            promptState.splashPermissionActive = active
        },
        onPermissionUiActiveChange = { active ->
            promptState.notificationPermissionUiActive = active
        },
    )
}

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

internal fun shouldLetSplashConsumeNotificationTarget(
    currentRoute: String?,
    request: AppLaunchRequest.NotificationTarget,
): Boolean =
    currentRoute == Screen.Splash.route &&
        request.source == NotificationLaunchSource.InitialIntent

internal fun shouldConsumeNotificationTargetImmediately(
    currentRoute: String?,
    request: AppLaunchRequest.NotificationTarget,
): Boolean =
    currentRoute == Screen.Splash.route &&
        request.source == NotificationLaunchSource.NewIntent
