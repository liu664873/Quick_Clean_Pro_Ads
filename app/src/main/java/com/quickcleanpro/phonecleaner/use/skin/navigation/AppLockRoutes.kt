package com.quickcleanpro.phonecleaner.use.skin.navigation

import com.quickcleanpro.phonecleaner.use.core.navigation.Screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quickcleanpro.phonecleaner.use.skin.applock.AppLockRoute

internal fun NavGraphBuilder.registerAppLockRoutes() {
    composable(Screen.AppLock.route) {
        AppLockRoute()
    }
}
