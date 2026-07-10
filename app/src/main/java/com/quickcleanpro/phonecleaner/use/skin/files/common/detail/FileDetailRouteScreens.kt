package com.quickcleanpro.phonecleaner.use.skin.files.common.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.audios.AudiosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.common.FileDetailUiState
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.common.FileOperationPhase
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.common.toFileDetailDisplayItem
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.documents.DocumentsManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.largefiles.LargeFilesManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.photos.PhotosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.screenshots.ScreenshotsManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.similarphotos.SimilarPhotosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.videos.VideosManagerViewModel
import com.quickcleanpro.phonecleaner.use.skin.files.common.rememberFileManagerPermissionState
import com.quickcleanpro.phonecleaner.use.skin.navigation.LocalRouter

@Composable
internal fun PhotosFileDetailScreen(
    viewModel: PhotosManagerViewModel,
    initialIndex: Int,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FileDetailRouteScreen(
        uiState =
            FileDetailUiState(
                items = uiState.currentDisplayItems.map { it.toFileDetailDisplayItem() },
                initialIndex = uiState.detailStartIndex ?: initialIndex,
                selectedIds = uiState.selectedIds,
                selectedSizeBytes = uiState.selectedSizeBytes,
                selectedUris = uiState.selectedUris,
                confirmDeleteVisible = uiState.phase == FileOperationPhase.ConfirmDelete,
            ),
        onRequestDelete = viewModel::requestDelete,
        onCancelDelete = viewModel::cancelDelete,
        onDeleteReady = viewModel::deleteSelectedFiles,
        onDeleteRejected = viewModel::rejectSystemDelete,
        onToggleSelection = viewModel::toggleSelection,
        onCloseDetail = viewModel::closeDetail,
    )
}

@Composable
internal fun SimilarPhotosFileDetailScreen(
    viewModel: SimilarPhotosManagerViewModel,
    initialIndex: Int,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FileDetailRouteScreen(
        uiState =
            FileDetailUiState(
                items = uiState.displayItems.map { it.toFileDetailDisplayItem() },
                initialIndex = uiState.detailStartIndex ?: initialIndex,
                selectedIds = uiState.selectedIds,
                selectedSizeBytes = uiState.selectedSizeBytes,
                selectedUris = uiState.selectedUris,
                confirmDeleteVisible = uiState.phase == FileOperationPhase.ConfirmDelete,
            ),
        onRequestDelete = viewModel::requestDelete,
        onCancelDelete = viewModel::cancelDelete,
        onDeleteReady = viewModel::deleteSelectedFiles,
        onDeleteRejected = viewModel::rejectSystemDelete,
        onToggleSelection = viewModel::toggleSelection,
        onCloseDetail = viewModel::closeDetail,
    )
}

@Composable
internal fun ScreenshotsFileDetailScreen(
    viewModel: ScreenshotsManagerViewModel,
    initialIndex: Int,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FileDetailRouteScreen(
        uiState =
            FileDetailUiState(
                items = uiState.displayItems.map { it.toFileDetailDisplayItem() },
                initialIndex = uiState.detailStartIndex ?: initialIndex,
                selectedIds = uiState.selectedIds,
                selectedSizeBytes = uiState.selectedSizeBytes,
                selectedUris = uiState.selectedUris,
                confirmDeleteVisible = uiState.phase == FileOperationPhase.ConfirmDelete,
            ),
        onRequestDelete = viewModel::requestDelete,
        onCancelDelete = viewModel::cancelDelete,
        onDeleteReady = viewModel::deleteSelectedFiles,
        onDeleteRejected = viewModel::rejectSystemDelete,
        onToggleSelection = viewModel::toggleSelection,
        onCloseDetail = viewModel::closeDetail,
    )
}

@Composable
internal fun VideosFileDetailScreen(
    viewModel: VideosManagerViewModel,
    initialIndex: Int,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FileDetailRouteScreen(
        uiState =
            FileDetailUiState(
                items = uiState.visibleDisplayItems.map { it.toFileDetailDisplayItem() },
                initialIndex = uiState.detailStartIndex ?: initialIndex,
                selectedIds = uiState.selectedIds,
                selectedSizeBytes = uiState.selectedSizeBytes,
                selectedUris = uiState.selectedUris,
                confirmDeleteVisible = uiState.phase == FileOperationPhase.ConfirmDelete,
            ),
        onRequestDelete = viewModel::requestDelete,
        onCancelDelete = viewModel::cancelDelete,
        onDeleteReady = viewModel::deleteSelectedFiles,
        onDeleteRejected = viewModel::rejectSystemDelete,
        onToggleSelection = viewModel::toggleSelection,
        onCloseDetail = viewModel::closeDetail,
    )
}

@Composable
internal fun AudiosFileDetailScreen(
    viewModel: AudiosManagerViewModel,
    initialIndex: Int,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FileDetailRouteScreen(
        uiState =
            FileDetailUiState(
                items = uiState.visibleDisplayItems.map { it.toFileDetailDisplayItem() },
                initialIndex = uiState.detailStartIndex ?: initialIndex,
                selectedIds = uiState.selectedIds,
                selectedSizeBytes = uiState.selectedSizeBytes,
                selectedUris = uiState.selectedUris,
                confirmDeleteVisible = uiState.phase == FileOperationPhase.ConfirmDelete,
            ),
        onRequestDelete = viewModel::requestDelete,
        onCancelDelete = viewModel::cancelDelete,
        onDeleteReady = viewModel::deleteSelectedFiles,
        onDeleteRejected = viewModel::rejectSystemDelete,
        onToggleSelection = viewModel::toggleSelection,
        onCloseDetail = viewModel::closeDetail,
    )
}

@Composable
internal fun LargeFilesFileDetailScreen(
    viewModel: LargeFilesManagerViewModel,
    initialIndex: Int,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FileDetailRouteScreen(
        uiState =
            FileDetailUiState(
                items = uiState.visibleDisplayItems.map { it.toFileDetailDisplayItem() },
                initialIndex = uiState.detailStartIndex ?: initialIndex,
                selectedIds = uiState.selectedIds,
                selectedSizeBytes = uiState.selectedSizeBytes,
                selectedUris = uiState.selectedUris,
                confirmDeleteVisible = uiState.phase == FileOperationPhase.ConfirmDelete,
            ),
        onRequestDelete = viewModel::requestDelete,
        onCancelDelete = viewModel::cancelDelete,
        onDeleteReady = viewModel::deleteSelectedFiles,
        onDeleteRejected = viewModel::rejectSystemDelete,
        onToggleSelection = viewModel::toggleSelection,
        onCloseDetail = viewModel::closeDetail,
    )
}

@Composable
internal fun DocumentsFileDetailScreen(
    viewModel: DocumentsManagerViewModel,
    initialIndex: Int,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FileDetailRouteScreen(
        uiState =
            FileDetailUiState(
                items = uiState.visibleDisplayItems.map { it.toFileDetailDisplayItem() },
                initialIndex = uiState.detailStartIndex ?: initialIndex,
                selectedIds = uiState.selectedIds,
                selectedSizeBytes = uiState.selectedSizeBytes,
                selectedUris = uiState.selectedUris,
                confirmDeleteVisible = uiState.phase == FileOperationPhase.ConfirmDelete,
            ),
        onRequestDelete = viewModel::requestDelete,
        onCancelDelete = viewModel::cancelDelete,
        onDeleteReady = viewModel::deleteSelectedFiles,
        onDeleteRejected = viewModel::rejectSystemDelete,
        onToggleSelection = viewModel::toggleSelection,
        onCloseDetail = viewModel::closeDetail,
    )
}

@Composable
private fun FileDetailRouteScreen(
    uiState: FileDetailUiState,
    onRequestDelete: () -> Unit,
    onCancelDelete: () -> Unit,
    onDeleteReady: () -> Unit,
    onDeleteRejected: () -> Unit,
    onToggleSelection: (Int) -> Unit,
    onCloseDetail: () -> Unit,
) {
    val router = LocalRouter.current
    val permissionState = rememberFileManagerPermissionState()

    fun closeAndBack() {
        onCloseDetail()
        router.goBack()
    }

    FileDetailScreen(
        uiState = uiState,
        permissionGranted = permissionState.granted,
        onBack = ::closeAndBack,
        onRequestDelete = onRequestDelete,
        onCancelDelete = onCancelDelete,
        onDeleteReady = onDeleteReady,
        onDeleteRejected = onDeleteRejected,
        onToggleSelection = onToggleSelection,
    )
}
