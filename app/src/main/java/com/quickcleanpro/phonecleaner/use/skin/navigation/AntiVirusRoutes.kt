package com.quickcleanpro.phonecleaner.use.skin.navigation

import com.quickcleanpro.phonecleaner.use.skin.navigation.LocalRouter

import com.quickcleanpro.phonecleaner.use.core.navigation.Screen

import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.quickcleanpro.phonecleaner.use.skin.antivirus.AntiVirusScreen
import com.quickcleanpro.phonecleaner.use.skin.antivirus.DeepScanVirusScreen
import com.quickcleanpro.phonecleaner.use.skin.antivirus.NoVirusResultScreen
import com.quickcleanpro.phonecleaner.use.skin.antivirus.QuickScanVirusScreen
import com.quickcleanpro.phonecleaner.use.skin.antivirus.ScanVirusResultScreen
import com.quickcleanpro.phonecleaner.use.feature.antivirus.presentation.VirusScanViewModel
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.registerAntiVirusRoutes() {
    composable(Screen.AntiVirus.route) {
        val viewModel: VirusScanViewModel = koinViewModel()
        AntiVirusScreen(
            viewModel = viewModel,
        )
    }

    composable(Screen.VirusQuickScan.route) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry) {
            router.navController.antiVirusViewModelOwnerOr(backStackEntry)
        }
        val viewModel: VirusScanViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        QuickScanVirusScreen(
            viewModel = viewModel,
        )
    }

    composable(Screen.VirusDeepScan.route) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry, router) {
            router.navController.antiVirusViewModelOwnerOr(backStackEntry)
        }
        val viewModel: VirusScanViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        DeepScanVirusScreen(
            viewModel = viewModel,
        )
    }

    composable(Screen.VirusResult.route) { backStackEntry ->
        val router = LocalRouter.current
        val parentEntry = remember(backStackEntry) {
            router.navController.antiVirusViewModelOwnerOr(backStackEntry)
        }
        val viewModel: VirusScanViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
        ScanVirusResultScreen(
            viewModel = viewModel,
        )
    }

    composable(Screen.NoVirusResult.route) {
        NoVirusResultScreen()
    }
}

private fun NavHostController.antiVirusViewModelOwnerOr(
    fallback: NavBackStackEntry,
): NavBackStackEntry =
    runCatching { getBackStackEntry(Screen.AntiVirus.route) }.getOrDefault(fallback)
