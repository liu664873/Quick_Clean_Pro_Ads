package com.quickcleanpro.phonecleaner.use.skin.toolbox.networkscan

import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkscan.NetworkScanViewModel

import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkscan.NetworkScanUiState

import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkscan.NetworkScanPhase

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quickcleanpro.phonecleaner.R
import com.quickcleanpro.phonecleaner.use.app.LocalExternalActivityLaunchHandler
import com.quickcleanpro.phonecleaner.use.app.LocalFeatureOperationTracker
import com.quickcleanpro.phonecleaner.use.core.common.operation.exitHandler
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import com.quickcleanpro.phonecleaner.use.skin.common.components.CleanXScaffoldPage
import com.quickcleanpro.phonecleaner.use.skin.common.components.stableNavigationBarsPadding
import com.quickcleanpro.phonecleaner.use.skin.common.components.buttons.CleanXPrimaryButton
import com.quickcleanpro.phonecleaner.use.skin.common.theme.CleanXBlue
import com.quickcleanpro.phonecleaner.use.skin.common.theme.CleanXButtonHeight
import com.quickcleanpro.phonecleaner.use.skin.common.theme.CleanXButtonShape
import com.quickcleanpro.phonecleaner.use.core.permission.CleanXProtectedAction
import com.quickcleanpro.phonecleaner.use.skin.permission.LocalCleanXPermissionCoordinator
import com.quickcleanpro.phonecleaner.use.skin.navigation.LocalRouter
import com.quickcleanpro.phonecleaner.use.core.navigation.Screen
import com.quickcleanpro.phonecleaner.use.skin.toolbox.networkscan.views.NetworkScanContentView
import org.koin.androidx.compose.koinViewModel

@Composable
fun NetworkScanScreen(viewModel: NetworkScanViewModel = koinViewModel()) {
    val router = LocalRouter.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val externalActivityLaunchHandler = LocalExternalActivityLaunchHandler.current
    val permissionCoordinator = LocalCleanXPermissionCoordinator.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tracker = LocalFeatureOperationTracker.current
    val featureExit = tracker.exitHandler()

    fun handleBack() {
        if (uiState.phase == NetworkScanPhase.Scanning) {
            viewModel.cancelScan()
            featureExit.exitBack(FeatureKey.NETWORK_SCAN) {
                router.goBack()
            }
        } else {
            featureExit.exitBack(FeatureKey.NETWORK_SCAN) {
                router.goBack()
            }
        }
    }

    DisposableEffect(lifecycleOwner, viewModel) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.refreshNetworkStateUntilWifiConnected()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    BackHandler(onBack = ::handleBack)

    CleanXScaffoldPage(
        title = stringResource(R.string.network_scan),
        titleFontSize = 20.sp,
        onBack = ::handleBack,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        bottomBar = {
            NetworkScanBottomBar(
                uiState = uiState,
                onRefreshWifi = viewModel::refreshNetworkStateUntilWifiConnected,
                onSwitchWifi = {
                    val openedSettings =
                        openWifiSettings(
                            context = context,
                            onLaunchingSettings = externalActivityLaunchHandler.markLaunch,
                            onSettingsLaunchFailed = externalActivityLaunchHandler.cancelLaunch,
                        )
                    if (!openedSettings) {
                        externalActivityLaunchHandler.cancelLaunch()
                    }
                    viewModel.refreshNetworkStateUntilWifiConnected()
                },
                onScan = {
                    permissionCoordinator.guardDirect(CleanXProtectedAction.NetworkScanStart) {
                        viewModel.startScan()
                    }
                },
            )
        },
    ) {
        NetworkScanContentView(
            uiState = uiState,
            onDevicesClick = { router.navigate(Screen.NetworkScanDevices) },
        )
    }
}

@Composable
private fun NetworkScanBottomBar(
    uiState: NetworkScanUiState,
    onRefreshWifi: () -> Unit,
    onSwitchWifi: () -> Unit,
    onScan: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .stableNavigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        when {
            !uiState.hasWifi -> {
                CleanXPrimaryButton(
                    text = stringResource(R.string.scan_again),
                    onClick = onRefreshWifi,
                )
            }
            uiState.phase == NetworkScanPhase.Result -> {
                CleanXPrimaryButton(
                    text = stringResource(R.string.switch_wifi),
                    onClick = onSwitchWifi,
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onScan,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(CleanXButtonHeight),
                    shape = CleanXButtonShape,
                    border = BorderStroke(1.56.dp, CleanXBlue),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = CleanXBlue,
                        ),
                ) {
                    Text(
                        text = stringResource(R.string.scan_again),
                        fontSize = 20.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
            else -> {
                CleanXPrimaryButton(
                    text = stringResource(R.string.scan_wifi),
                    onClick = onScan,
                    enabled = uiState.phase != NetworkScanPhase.Scanning,
                )
            }
        }
    }
}

private fun openWifiSettings(
    context: android.content.Context,
    onLaunchingSettings: () -> Unit,
    onSettingsLaunchFailed: () -> Unit,
): Boolean {
    val intents =
        buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                add(Intent(Settings.Panel.ACTION_WIFI))
            }
            add(Intent(Settings.ACTION_WIFI_SETTINGS))
            add(Intent(Settings.ACTION_SETTINGS))
        }
    intents.forEach { intent ->
        try {
            onLaunchingSettings()
            context.startActivity(intent)
            return true
        } catch (_: ActivityNotFoundException) {
            onSettingsLaunchFailed()
        } catch (_: Exception) {
            onSettingsLaunchFailed()
        }
    }
    return false
}
