package com.quickcleanpro.phonecleaner.use.skin.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import com.quickcleanpro.phonecleaner.use.core.navigation.Screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute
import com.quickcleanpro.phonecleaner.use.core.navigation.DETAIL_INITIAL_INDEX_ARG
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.audios.AudiosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.documents.DocumentsManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.largefiles.LargeFilesManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.photos.PhotosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.screenshots.ScreenshotsManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.similarphotos.SimilarPhotosManagerViewModel
import com.quickcleanpro.phonecleaner.use.feature.files.presentation.videos.VideosManagerViewModel
import com.quickcleanpro.phonecleaner.use.skin.files.audios.AudiosManagerScreen
import com.quickcleanpro.phonecleaner.use.skin.files.common.detail.AudiosFileDetailScreen
import com.quickcleanpro.phonecleaner.use.skin.files.common.detail.DocumentsFileDetailScreen
import com.quickcleanpro.phonecleaner.use.skin.files.common.detail.LargeFilesFileDetailScreen
import com.quickcleanpro.phonecleaner.use.skin.files.common.detail.PhotosFileDetailScreen
import com.quickcleanpro.phonecleaner.use.skin.files.common.detail.ScreenshotsFileDetailScreen
import com.quickcleanpro.phonecleaner.use.skin.files.common.detail.SimilarPhotosFileDetailScreen
import com.quickcleanpro.phonecleaner.use.skin.files.common.detail.VideosFileDetailScreen
import com.quickcleanpro.phonecleaner.use.skin.files.duplicates.DuplicateFilesManagerScreen
import com.quickcleanpro.phonecleaner.use.skin.files.documents.DocumentsManagerScreen
import com.quickcleanpro.phonecleaner.use.skin.files.largefiles.LargeFilesManagerScreen
import com.quickcleanpro.phonecleaner.use.skin.files.photoprivacy.PhotoPrivacyManagerScreen
import com.quickcleanpro.phonecleaner.use.skin.files.photos.PhotosManagerScreen
import com.quickcleanpro.phonecleaner.use.skin.files.screenshots.ScreenshotsManagerScreen
import com.quickcleanpro.phonecleaner.use.skin.files.similarphotos.SimilarPhotosManagerScreen
import com.quickcleanpro.phonecleaner.use.skin.files.videos.VideosManagerScreen
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.registerFileManagerRoutes() {
    composable(Screen.PhotosManager.route) {
        PhotosManagerScreen()
    }
    composable(AppRoute.PhotosDetail.detailPattern()) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry, router) {
            router.navController.fileManagerViewModelOwnerOr(AppRoute.PhotosManager, backStackEntry)
        }
        val viewModel: PhotosManagerViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        PhotosFileDetailScreen(viewModel = viewModel, initialIndex = backStackEntry.initialIndex())
    }
    composable(Screen.SimilarPhotosManager.route) {
        SimilarPhotosManagerScreen()
    }
    composable(AppRoute.SimilarPhotosDetail.detailPattern()) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry, router) {
            router.navController.fileManagerViewModelOwnerOr(AppRoute.SimilarPhotosManager, backStackEntry)
        }
        val viewModel: SimilarPhotosManagerViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        SimilarPhotosFileDetailScreen(viewModel = viewModel, initialIndex = backStackEntry.initialIndex())
    }
    composable(Screen.PhotoPrivacyManager.route) {
        PhotoPrivacyManagerScreen()
    }
    composable(Screen.ScreenshotsManager.route) {
        ScreenshotsManagerScreen()
    }
    composable(AppRoute.ScreenshotsDetail.detailPattern()) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry, router) {
            router.navController.fileManagerViewModelOwnerOr(AppRoute.ScreenshotsManager, backStackEntry)
        }
        val viewModel: ScreenshotsManagerViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        ScreenshotsFileDetailScreen(viewModel = viewModel, initialIndex = backStackEntry.initialIndex())
    }
    composable(Screen.VideosManager.route) {
        VideosManagerScreen()
    }
    composable(AppRoute.VideosDetail.detailPattern()) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry, router) {
            router.navController.fileManagerViewModelOwnerOr(AppRoute.VideosManager, backStackEntry)
        }
        val viewModel: VideosManagerViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        VideosFileDetailScreen(viewModel = viewModel, initialIndex = backStackEntry.initialIndex())
    }
    composable(Screen.AudiosManager.route) {
        AudiosManagerScreen()
    }
    composable(AppRoute.AudiosDetail.detailPattern()) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry, router) {
            router.navController.fileManagerViewModelOwnerOr(AppRoute.AudiosManager, backStackEntry)
        }
        val viewModel: AudiosManagerViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        AudiosFileDetailScreen(viewModel = viewModel, initialIndex = backStackEntry.initialIndex())
    }
    composable(Screen.LargeFilesManager.route) {
        LargeFilesManagerScreen()
    }
    composable(AppRoute.LargeFilesDetail.detailPattern()) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry, router) {
            router.navController.fileManagerViewModelOwnerOr(AppRoute.LargeFilesManager, backStackEntry)
        }
        val viewModel: LargeFilesManagerViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        LargeFilesFileDetailScreen(viewModel = viewModel, initialIndex = backStackEntry.initialIndex())
    }
    composable(Screen.DuplicateFilesManager.route) {
        DuplicateFilesManagerScreen()
    }
    composable(Screen.DocumentsManager.route) {
        DocumentsManagerScreen()
    }
    composable(AppRoute.DocumentsDetail.detailPattern()) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry, router) {
            router.navController.fileManagerViewModelOwnerOr(AppRoute.DocumentsManager, backStackEntry)
        }
        val viewModel: DocumentsManagerViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        DocumentsFileDetailScreen(viewModel = viewModel, initialIndex = backStackEntry.initialIndex())
    }
}

private fun NavBackStackEntry.initialIndex(): Int =
    arguments?.getString(DETAIL_INITIAL_INDEX_ARG)?.toIntOrNull()?.coerceAtLeast(0) ?: 0

private fun NavHostController.fileManagerViewModelOwnerOr(
    route: AppRoute,
    fallback: NavBackStackEntry,
): NavBackStackEntry =
    runCatching { getBackStackEntry(route.value) }.getOrDefault(fallback)
