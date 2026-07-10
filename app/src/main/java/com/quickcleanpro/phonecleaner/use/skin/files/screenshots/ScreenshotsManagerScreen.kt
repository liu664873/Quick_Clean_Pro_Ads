package com.quickcleanpro.phonecleaner.use.skin.files.screenshots

import com.quickcleanpro.phonecleaner.use.feature.files.presentation.common.FileImageDisplayItem
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.screenshots.ScreenshotsManagerUiState
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.screenshots.ScreenshotsManagerViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quickcleanpro.phonecleaner.R
import com.quickcleanpro.phonecleaner.use.core.feature.FeatureKey
import com.quickcleanpro.phonecleaner.use.app.LocalFeatureOperationTracker
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import com.quickcleanpro.phonecleaner.use.skin.common.components.CleanXBottomActionBar
import com.quickcleanpro.phonecleaner.use.skin.navigation.LocalRouter
import com.quickcleanpro.phonecleaner.use.skin.files.common.FileManagerScaffold
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.common.FileOperationPhase
import com.quickcleanpro.phonecleaner.use.skin.files.common.components.FileManagerPageBrush
import com.quickcleanpro.phonecleaner.use.skin.files.common.components.FileManagerTopAction
import com.quickcleanpro.phonecleaner.use.skin.files.common.leaveBackForPermissionRejected
import com.quickcleanpro.phonecleaner.use.skin.files.common.leaveHome
import com.quickcleanpro.phonecleaner.use.skin.files.common.leaveHomeWithReturnAd
import com.quickcleanpro.phonecleaner.use.skin.files.common.rememberFileManagerPermissionState
import com.quickcleanpro.phonecleaner.use.skin.files.common.shell.FileManagerFlowDialogs
import com.quickcleanpro.phonecleaner.use.skin.files.common.shell.FileManagerFlowEffects
import com.quickcleanpro.phonecleaner.use.skin.files.common.shell.blockAndShowStopDialog
import com.quickcleanpro.phonecleaner.use.skin.files.common.shell.rememberFileManagerFlowState
import com.quickcleanpro.phonecleaner.use.skin.files.common.views.FileOperationPhaseContent
import com.quickcleanpro.phonecleaner.use.skin.files.common.views.list.FileManagerScreenshotGridView
import org.koin.androidx.compose.koinViewModel

private val FEATURE = FeatureKey.SCREENSHOTS

@Composable
fun ScreenshotsManagerScreen() {
    ScreenshotsManagerScreenState(viewModel = koinViewModel())
}

@Composable
private fun ScreenshotsManagerScreenState(viewModel: ScreenshotsManagerViewModel) {
    val router = LocalRouter.current
    val tracker = LocalFeatureOperationTracker.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val permissionState = rememberFileManagerPermissionState()
    val flowState = rememberFileManagerFlowState()
    val displayState = flowState.blockedPhase?.let { uiState.copy(phase = it) } ?: uiState
    val scrollState = remember { ScrollState(0) }
    val isDetailMode = displayState.detailStartIndex != null
    val displayItems = displayState.displayItems

    fun handleBack() {
        if (flowState.completionAdInFlight) return
        when {
            isDetailMode -> viewModel.closeDetail()
            !permissionState.granted -> permissionState.leaveBackForPermissionRejected(router, FEATURE, tracker)
            displayState.phase == FileOperationPhase.Deleting -> {
                viewModel.cancelDeletingAndReturnToBrowsing(); flowState.showStopDialog = true
            }
            displayState.phase == FileOperationPhase.Scanning -> flowState.blockAndShowStopDialog(displayState.phase)
            displayState.phase == FileOperationPhase.ConfirmDelete -> viewModel.cancelDelete()
            else -> permissionState.leaveHomeWithReturnAd(router, FEATURE, tracker)
        }
    }

    FileManagerFlowEffects(
        viewModel = viewModel,
        tracker = tracker,
        feature = FEATURE,
        permissionState = permissionState,
        errorMessage = uiState.errorMessage,
        onClearError = viewModel::clearError,
        onStartIfNeeded = viewModel::startIfNeeded,
        onPermissionRejected = { permissionState.leaveHome(router) },
        flowState = flowState,
    )

    BackHandler { handleBack() }

    FileManagerScaffold(
        title = stringResource(R.string.nav_screenshots),
        onBack = { handleBack() },
        actions = {
            val actionText = when {
                displayState.phase == FileOperationPhase.Browsing || displayState.phase == FileOperationPhase.ConfirmDelete ->
                    stringResource(if (displayState.allSelected) R.string.file_unselect_all else R.string.file_select_all)
                else -> null
            }
            FileManagerTopAction(
                actionText = actionText,
                onAction = viewModel::toggleVisibleItems,
            )
        },
        bottomBar = {
            if (permissionState.granted && displayState.phase == FileOperationPhase.Browsing && !isDetailMode) {
                CleanXBottomActionBar(
                    enabled = displayState.selectedIds.isNotEmpty(),
                    text = stringResource(R.string.file_delete),
                    onClick = viewModel::requestDelete,
                    backgroundColor = Color.Transparent,
                    buttonModifier = Modifier.height(52.dp),
                    buttonCornerRadius = 10.dp,
                    buttonFontSize = 20.sp,
                )
            }
        }
    ) {
        ScreenshotsManagerContent(
            uiState = displayState,
            scrollState = scrollState,
            onToggleAll = viewModel::toggleVisibleItems,
            onSelect = viewModel::toggleSelection,
            onOpenDetail = { item ->
                val index = displayItems.indexOfFirst { it.id == item.id }.coerceAtLeast(0)
                viewModel.openDetail(index)
                router.navigate(AppRoute.ScreenshotsDetail.withDetailInitialIndex(index))
            },
            onContinue = viewModel::continueManaging,
        )
    }

    FileManagerFlowDialogs(
        flowState = flowState,
        permissionState = permissionState,
        router = router,
        tracker = tracker,
        feature = FEATURE,
        phase = displayState.phase,
        deleteDialogVisible = displayState.phase == FileOperationPhase.ConfirmDelete && !isDetailMode,
        selectedUris = displayState.selectedUris,
        onCancelActiveOperation = viewModel::cancelActiveOperation,
        onCancelDelete = viewModel::cancelDelete,
        onDeleteReady = viewModel::deleteSelectedFiles,
        onDeleteRejected = viewModel::rejectSystemDelete,
    )
}

@Composable
private fun ScreenshotsManagerContent(
    uiState: ScreenshotsManagerUiState,
    scrollState: ScrollState,
    onToggleAll: () -> Unit,
    onSelect: (Int) -> Unit,
    onOpenDetail: (com.quickcleanpro.phonecleaner.use.feature.files.presentation.common.FileImageDisplayItem) -> Unit,
    onContinue: () -> Unit,
) {
    val displayItems = uiState.displayItems
    val result = uiState.resultSize
    Column(
        modifier = Modifier
            .background(FileManagerPageBrush)
            .padding(horizontal = 16.dp),
    ) {
        FileOperationPhaseContent(
            phase = uiState.phase,
            scanningText = stringResource(R.string.file_scanning_screenshots),
            deletingText = stringResource(R.string.file_deleting_files),
            resultAmount = result.first,
            resultUnit = result.second,
            resultCaption = stringResource(R.string.file_deleted_in_cleanup),
            onContinue = onContinue,
        ) {
            FileManagerScreenshotGridView(
                items = displayItems,
                selectedIds = uiState.selectedIds,
                allSelected = uiState.allSelected,
                scrollState = scrollState,
                onToggleAll = onToggleAll,
                onSelect = onSelect,
                onOpenDetail = onOpenDetail,
            )
        }
    }
}

