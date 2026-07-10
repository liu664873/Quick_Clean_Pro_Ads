package com.quickcleanpro.phonecleaner.use.skin.files.common.shell

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.quickcleanpro.phonecleaner.use.core.common.operation.FeatureOperationTracker
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.common.BaseFileManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.common.FileOperationPhase
import com.quickcleanpro.phonecleaner.use.skin.files.common.FileManagerDeleteConfirmDialog
import com.quickcleanpro.phonecleaner.use.skin.files.common.FileManagerErrorToastEffect
import com.quickcleanpro.phonecleaner.use.skin.files.common.FileManagerNoResultsDialog
import com.quickcleanpro.phonecleaner.use.skin.files.common.FileManagerOperationEventsEffect
import com.quickcleanpro.phonecleaner.use.skin.files.common.FileManagerPermissionState
import com.quickcleanpro.phonecleaner.use.skin.files.common.FileManagerStartEffect
import com.quickcleanpro.phonecleaner.use.skin.files.common.FileManagerStopOperationDialog
import com.quickcleanpro.phonecleaner.use.skin.files.common.leaveBackWithReturnAd
import com.quickcleanpro.phonecleaner.use.skin.navigation.RouteManager

internal class FileManagerFlowState {
    var showStopDialog by mutableStateOf(false)
    var completionAdInFlight by mutableStateOf(false)
    var blockedPhase by mutableStateOf<FileOperationPhase?>(null)
}

@Composable
internal fun rememberFileManagerFlowState(): FileManagerFlowState =
    remember { FileManagerFlowState() }

@Composable
internal fun FileManagerFlowEffects(
    viewModel: BaseFileManagerViewModel,
    tracker: FeatureOperationTracker,
    feature: FeatureKey,
    permissionState: FileManagerPermissionState,
    errorMessage: String?,
    onClearError: () -> Unit,
    onStartIfNeeded: () -> Unit,
    onPermissionRejected: () -> Unit,
    flowState: FileManagerFlowState,
) {
    FileManagerOperationEventsEffect(viewModel, tracker) { flowState.completionAdInFlight = it }
    FileManagerErrorToastEffect(errorMessage, onClearError)
    FileManagerStartEffect(feature, permissionState, onStartIfNeeded, onPermissionRejected)
}

@Composable
internal fun FileManagerFlowDialogs(
    flowState: FileManagerFlowState,
    permissionState: FileManagerPermissionState,
    router: RouteManager,
    tracker: FeatureOperationTracker,
    feature: FeatureKey,
    phase: FileOperationPhase,
    deleteDialogVisible: Boolean,
    selectedUris: List<Uri>,
    onCancelActiveOperation: () -> Unit,
    onCancelDelete: () -> Unit,
    onDeleteReady: () -> Unit,
    onDeleteRejected: () -> Unit,
) {
    FileManagerStopOperationDialog(
        visible = flowState.showStopDialog,
        permissionGranted = permissionState.granted,
        onQuit = {
            flowState.showStopDialog = false
            flowState.blockedPhase = null
            onCancelActiveOperation()
            permissionState.leaveBackWithReturnAd(router, feature, tracker)
        },
        onResume = {
            flowState.showStopDialog = false
            flowState.blockedPhase = null
        },
    )

    FileManagerDeleteConfirmDialog(
        visible = deleteDialogVisible,
        permissionGranted = permissionState.granted,
        selectedUris = selectedUris,
        onCancel = onCancelDelete,
        onDeleteReady = onDeleteReady,
        onRejected = onDeleteRejected,
    )

    FileManagerNoResultsDialog(
        visible = phase == FileOperationPhase.NoResults,
        permissionGranted = permissionState.granted,
        onBack = { permissionState.leaveBackWithReturnAd(router, feature, tracker) },
    )
}

internal fun FileManagerFlowState.blockAndShowStopDialog(phase: FileOperationPhase) {
    blockedPhase = phase
    showStopDialog = true
}
