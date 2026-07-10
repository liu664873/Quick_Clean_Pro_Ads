package com.quickcleanpro.phonecleaner.use.skin.navigation

import com.quickcleanpro.phonecleaner.use.core.navigation.Screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quickcleanpro.phonecleaner.use.skin.settings.ManagePermissionsScreen
import com.quickcleanpro.phonecleaner.use.skin.settings.SettingsScreen

internal fun NavGraphBuilder.registerSettingsRoutes() {
    composable(Screen.Settings.route) {
        SettingsScreen()
    }
    composable(Screen.ManagePermissions.route) {
        ManagePermissionsScreen()
    }
}
