package com.quickcleanpro.phonecleaner.use.skin.navigation

import com.quickcleanpro.phonecleaner.use.skin.navigation.LocalRouter

import com.quickcleanpro.phonecleaner.use.core.navigation.Screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quickcleanpro.phonecleaner.use.skin.junkclean.screens.JunkCleanScreen
import com.quickcleanpro.phonecleaner.use.feature.junkclean.presentation.JunkCleanViewModel
import org.koin.androidx.compose.koinViewModel

internal fun NavGraphBuilder.registerCleanRoutes() {
    composable(Screen.Scan.route) {
        val router = LocalRouter.current
        val viewModel: JunkCleanViewModel = koinViewModel()
        JunkCleanScreen(
            viewModel = viewModel,
            onNavigateBack = { router.goBack() },
            onNavigateHome = { router.goHome() },
            onNavigateHomeAfterComplete = { router.goHome() },
        )
    }
}
