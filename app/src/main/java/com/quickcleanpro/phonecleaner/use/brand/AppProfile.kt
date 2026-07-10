package com.quickcleanpro.phonecleaner.use.brand

import com.quickcleanpro.phonecleaner.use.core.navigation.AppRoute

data class LegalProfile(
    val termsOfServiceUrl: String,
    val privacyPolicyUrl: String,
)

data class NotificationProfile(
    val persistentShortcutCompactLayoutName: String? = null,
    val persistentShortcutExpandedLayoutName: String? = null,
    val persistentShortcuts: List<NotificationShortcutProfile> = emptyList(),
    val enabledToolRoutes: Set<String> = emptySet(),
)

data class NotificationShortcutProfile(
    val viewIdName: String,
    val route: String,
    val requestCode: Int,
)

data class AppServiceProfile(
    val trustlookApiKey: String,
)

data class AppProfile(
    val variantKey: String,
    val appName: String,
    val themeKey: String,
    val legalProfile: LegalProfile,
    val notificationProfile: NotificationProfile,
    val serviceProfile: AppServiceProfile,
) {
    val trustlookApiKey: String get() = serviceProfile.trustlookApiKey
    val termsOfServiceUrl: String get() = legalProfile.termsOfServiceUrl
    val privacyPolicyUrl: String get() = legalProfile.privacyPolicyUrl
}

object AppProfiles {
    lateinit var current: AppProfile
        private set

    fun initialize(context: android.content.Context) {
        if (::current.isInitialized) return
        current = AppProfileLoader.load(context)
    }
}

fun quickCleanProNotificationProfile(): NotificationProfile =
    NotificationProfile(
        persistentShortcutCompactLayoutName = "notification_persistent_shortcuts_compact",
        persistentShortcutExpandedLayoutName = "notification_persistent_shortcuts",
        persistentShortcuts =
            listOf(
                NotificationShortcutProfile(
                    viewIdName = "persistent_shortcut_clean",
                    route = AppRoute.JunkClean.value,
                    requestCode = 1711,
                ),
                NotificationShortcutProfile(
                    viewIdName = "persistent_shortcut_file",
                    route = "home_file_manager",
                    requestCode = 1712,
                ),
                NotificationShortcutProfile(
                    viewIdName = "persistent_shortcut_tools",
                    route = "home_toolbox",
                    requestCode = 1713,
                ),
                NotificationShortcutProfile(
                    viewIdName = "persistent_shortcut_battery",
                    route = AppRoute.BatteryInfo.value,
                    requestCode = 1714,
                ),
            ),
        enabledToolRoutes =
            setOf(
                AppRoute.DeviceInfo.value,
                AppRoute.JunkClean.value,
                AppRoute.BatteryInfo.value,
                AppRoute.NetworkScan.value,
                AppRoute.NetworkUsage.value,
                AppRoute.NotificationCleaner.value,
            ),
    )
