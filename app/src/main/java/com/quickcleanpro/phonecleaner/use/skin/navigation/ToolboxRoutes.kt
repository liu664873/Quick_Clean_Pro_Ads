package com.quickcleanpro.phonecleaner.use.skin.navigation

import com.quickcleanpro.phonecleaner.use.core.navigation.Screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quickcleanpro.phonecleaner.use.skin.toolbox.appusage.AppUsageScreen
import com.quickcleanpro.phonecleaner.use.skin.toolbox.batteryinfo.BatteryInfoScreen
import com.quickcleanpro.phonecleaner.use.skin.toolbox.deviceinfo.DeviceInfoScreen
import com.quickcleanpro.phonecleaner.use.skin.toolbox.networkscan.NetworkScanDevicesScreen
import com.quickcleanpro.phonecleaner.use.skin.toolbox.networkscan.NetworkScanScreen
import com.quickcleanpro.phonecleaner.use.skin.toolbox.networkspeed.NetworkSpeedScreen
import com.quickcleanpro.phonecleaner.use.skin.toolbox.networkusage.NetworkUsageScreen
import com.quickcleanpro.phonecleaner.use.skin.toolbox.notificationcleaner.NotificationCleanerScreen
import com.quickcleanpro.phonecleaner.use.skin.toolbox.whatsappcleaner.WhatsAppCleanerScreen

internal fun NavGraphBuilder.registerToolboxRoutes() {
    composable(Screen.DeviceInfo.route) {
        DeviceInfoScreen()
    }
    composable(Screen.BatteryInfo.route) {
        BatteryInfoScreen()
    }
    composable(Screen.AppUsage.route) {
        AppUsageScreen()
    }
    composable(Screen.NetworkUsage.route) {
        NetworkUsageScreen()
    }
    composable(Screen.NetworkScan.route) {
        NetworkScanScreen()
    }
    composable(Screen.NetworkScanDevices.route) {
        NetworkScanDevicesScreen()
    }
    composable(Screen.NetworkSpeed.route) {
        NetworkSpeedScreen()
    }
    composable(Screen.WhatsAppCleaner.route) {
        WhatsAppCleanerScreen()
    }
    composable(Screen.NotificationCleaner.route) {
        NotificationCleanerScreen()
    }
}
