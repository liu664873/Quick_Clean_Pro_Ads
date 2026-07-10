package com.quickcleanpro.phonecleaner.use.skin.toolbox.networkspeed

import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkspeed.NetworkSpeedViewModel

import com.quickcleanpro.phonecleaner.use.feature.toolbox.presentation.networkspeed.NetworkSpeedPhase

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quickcleanpro.phonecleaner.R
import com.quickcleanpro.phonecleaner.use.app.LocalFeatureOperationTracker
import com.quickcleanpro.phonecleaner.use.core.common.operation.exitHandler
import com.quickcleanpro.phonecleaner.use.core.common.operation.FeatureOperationEvent
import com.quickcleanpro.phonecleaner.use.core.common.operation.OperationAction
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import com.quickcleanpro.phonecleaner.use.skin.common.components.CleanXScaffoldPage
import com.quickcleanpro.phonecleaner.use.skin.common.components.stableNavigationBarsPadding
import com.quickcleanpro.phonecleaner.use.skin.common.components.buttons.CleanXPrimaryButton
import com.quickcleanpro.phonecleaner.use.skin.navigation.LocalRouter
import com.quickcleanpro.phonecleaner.use.skin.toolbox.networkspeed.views.NetworkSpeedContentView
import org.koin.androidx.compose.koinViewModel

@Composable
fun NetworkSpeedScreen(viewModel: NetworkSpeedViewModel = koinViewModel()) {
    val router = LocalRouter.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tracker = LocalFeatureOperationTracker.current
    val featureExit = tracker.exitHandler()
    var finishAdInFlight by remember { mutableStateOf(false) }

    fun handleBack() {
        if (finishAdInFlight || uiState.phase == NetworkSpeedPhase.Completing) return
        if (uiState.phase == NetworkSpeedPhase.Testing) {
            viewModel.stopSpeedTest()
            featureExit.exitBack(FeatureKey.NETWORK_SPEED) {
                router.goBack()
            }
        } else {
            featureExit.exitBack(FeatureKey.NETWORK_SPEED) {
                router.goBack()
            }
        }
    }

    DisposableEffect(lifecycleOwner, viewModel) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.refreshNetworkStateUntilNetworkAvailable()
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(viewModel, tracker) {
        viewModel.operationEvents.collect { event ->
            if (event.isNetworkSpeedCompletionSuccess()) {
                if (!finishAdInFlight) {
                    finishAdInFlight = true
                    tracker.trackWithAd(event) {
                        finishAdInFlight = false
                        viewModel.showResultAfterCompletionAd()
                    }
                }
            } else {
                tracker.trackWithAd(event) {}
            }
        }
    }

    BackHandler(onBack = ::handleBack)

    CleanXScaffoldPage(
        title = stringResource(R.string.network_speed),
        titleFontSize = 20.sp,
        onBack = ::handleBack,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        bottomBar = {
            if (uiState.phase != NetworkSpeedPhase.Result && uiState.phase != NetworkSpeedPhase.Completing) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .stableNavigationBarsPadding()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                ) {
                    CleanXPrimaryButton(
                        text =
                            if (uiState.phase == NetworkSpeedPhase.Testing) {
                                stringResource(R.string.stop)
                            } else {
                                stringResource(R.string.run_speed_test)
                            },
                        onClick =
                            if (uiState.phase == NetworkSpeedPhase.Testing) {
                                viewModel::stopSpeedTest
                            } else {
                                viewModel::runSpeedTest
                            },
                        enabled = uiState.hasNetwork || uiState.phase == NetworkSpeedPhase.Testing,
                    )
                }
            }
        },
    ) {
        NetworkSpeedContentView(uiState = uiState)
    }
}

private fun FeatureOperationEvent.isNetworkSpeedCompletionSuccess(): Boolean =
    this is FeatureOperationEvent.OperationFinished &&
        feature == FeatureKey.NETWORK_SPEED &&
        action == OperationAction.TEST &&
        success
